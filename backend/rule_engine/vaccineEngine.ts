import { VaccineStatus } from './vaccineStatus';

const DUE_SOON_WINDOW_DAYS = 30;
const OVERDUE_GRACE_DAYS = 30;
const MIN_INTERVAL_DATE = 28;

const DAY_MS = 24 * 60 * 60 * 1000;

// getting nextDose
function getNextDose(lastVaccine : any, vaccineSchedule : any)
{
    let lastDoseMonth : number = lastVaccine ? (lastVaccine.dose_number ?? 0) : 0;
    let nextDose : any;
    
    // Find the next dose
    if(lastDoseMonth < vaccineSchedule.length)
        nextDose = vaccineSchedule[lastDoseMonth];
    else
        return null;

   return nextDose;
}

// Compute the recommend date for the vaccine
// null means out of doses
function computeRecDate(userInfo : any, lastVaccine : any, vaccineSchedule : any)
{
    let lastDoseMonth : number = lastVaccine ? (lastVaccine.dose_number ?? 0) : 0;
    let nextDose : any;
    
    // Find the next dose
    if(lastDoseMonth < vaccineSchedule.length)
        nextDose = vaccineSchedule[lastDoseMonth];
    else
        return null;

   const nextDoseMs: number = typeof nextDose === "number" ? nextDose * DAY_MS : Number(nextDose);

  // Recommend next dose due date
  const lastDoseTimeMs: number = lastVaccine?.dose_date ? lastVaccine.dose_date.getTime() : 0;

  const earliestDateMs: number = Math.max(
    userInfo.dob.getTime() + nextDoseMs,   
    lastDoseTimeMs + MIN_INTERVAL_DATE * DAY_MS
  );

  const recommendedDateMs: number = userInfo.dob.getTime() + nextDoseMs;

  return new Date(Math.max(earliestDateMs, recommendedDateMs));
    
}

// Determine the medical history to verify ineligible
function verifyMedicalHistory(userInfo : any, lastVaccine : any, vaccineSchedule : any)
{
    let nextDose = getNextDose(lastVaccine, vaccineSchedule);
    if(!(userInfo.medicalConditions in nextDose.requiredConditions) && (userInfo.medicalConditions in nextDose.ineligibilityConditions))
        return true;
    
    return false;
}

// Determine the status 
export function determineStatus(userInfo : any, lastVaccine : any, vaccineSchedule : any)
{
    let medicalStatus = verifyMedicalHistory(userInfo, lastVaccine, vaccineSchedule);

    if(medicalStatus) return VaccineStatus.NOT_ELIGIBLE;

    const TODAY : Date = new Date();
    let dueDate : Date | null = computeRecDate(userInfo , lastVaccine , vaccineSchedule );
    
    // out of doses / up to date
    if (dueDate == null) return VaccineStatus.NOT_ELIGIBLE;

    const dueSoonStart: Date = new Date(dueDate.getTime() - DUE_SOON_WINDOW_DAYS * DAY_MS);
    const overdueDate: Date = new Date(dueDate.getTime() + OVERDUE_GRACE_DAYS * DAY_MS);

    if (TODAY.getTime() > overdueDate.getTime()) return VaccineStatus.OVERDUE;

    if (TODAY.getTime() >= dueDate.getTime() && TODAY.getTime() <= overdueDate.getTime())
        return VaccineStatus.ELIGIBLE_NOW;

    if (TODAY.getTime() >= dueSoonStart.getTime() && TODAY.getTime() < dueDate.getTime())
        return VaccineStatus.DUE_SOON;

    return VaccineStatus.NOT_ELIGIBLE;
}
