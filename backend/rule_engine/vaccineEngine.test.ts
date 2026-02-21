// determineStatus.medical.test.ts
// Vitest test suite focusing on medicalConditions as a LIST (e.g. ["pregnant"])
// and edge cases.
//
// Run with: npx vitest

import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { determineStatus } from "./vaccineEngine"; // <-- change to your file path
import { VaccineStatus } from "./vaccineStatus";

const DAY_MS = 24 * 60 * 60 * 1000;

/**
 * Your code does:
 * - Number(nextDose) (expects ms offset)
 * - nextDose.requiredConditions / nextDose.ineligibilityConditions (NOW: string[])
 *
 * This helper creates a "dose object" that supports both.
 */
function dose(
  daysFromDob: number,
  requiredConditions: string[] = [],
  ineligibilityConditions: string[] = []
) {
  return {
    requiredConditions,
    ineligibilityConditions,
    valueOf() {
      return daysFromDob * DAY_MS; // Number(nextDose) -> ms
    },
  };
}

function d(iso: string) {
  return new Date(`${iso}T00:00:00.000Z`);
}

/**
 * These tests assume intended behavior when medicalConditions is a LIST:
 * - if ANY condition is in ineligibilityConditions => NOT_ELIGIBLE
 * - if requiredConditions is non-empty and user has NONE of them => NOT_ELIGIBLE
 * - if requiredConditions is empty => no extra eligibility restriction
 * - if conflict (same condition in both required + ineligible) => ineligibility wins
 */

describe("determineStatus with medicalConditions as a LIST", () => {
  beforeEach(() => vi.useFakeTimers());
  afterEach(() => vi.useRealTimers());

  it("NOT_ELIGIBLE: pregnancy listed and dose has pregnancy in ineligibilityConditions (even if due today)", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60, [], ["pregnant"])];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: ["pregnant"] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.NOT_ELIGIBLE);
  });

  it("ELIGIBLE_NOW: no ineligibility conditions hit and dueDate is today", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60)];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: [] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.ELIGIBLE_NOW);
  });

  it("DUE_SOON: dueDate in 10 days and no medical restrictions", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60)];
    const userInfo = { dob: d("2026-01-01"), medicalConditions: [] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.DUE_SOON);
  });

  it("OVERDUE: today is beyond (dueDate + grace)", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60)];
    const userInfo = { dob: d("2025-11-17"), medicalConditions: [] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.OVERDUE);
  });

  it("NOT_ELIGIBLE: requiredConditions present but user has none of them (should block)", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60, ["highRisk"], [])];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: ["asthma"] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.NOT_ELIGIBLE);
  });

  it("ELIGIBLE_NOW: requiredConditions present and user has at least one required condition", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60, ["highRisk"], [])];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: ["highRisk", "asthma"] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.ELIGIBLE_NOW);
  });

  it("NOT_ELIGIBLE: multiple conditions, any one in ineligibilityConditions should block", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60, [], ["pregnant"])];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: ["diabetes", "pregnant"] };
    const lastVaccine = null;

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.NOT_ELIGIBLE);
  });

  it("Harder: min-interval pushes dose 2 later (turns what looks due into DUE_SOON)", () => {
    // On 2026-01-25, dose 2 'recommended age' is already passed,
    // but last dose was too recent so min interval pushes due date into the future.
    vi.setSystemTime(d("2026-01-25"));

    const vaccineSchedule = [
      dose(60),  // dose 1 at 60 days
      dose(120), // dose 2 at 120 days (but min interval can push it later)
    ];

    const userInfo = { dob: d("2025-09-22"), medicalConditions: [] };

    const lastVaccine = {
      dose_number: 1,            // next is schedule[1] (dose 2)
      dose_date: d("2026-01-13") // +28 days => 2026-02-10 (future)
    };

    const status = determineStatus(userInfo, lastVaccine, vaccineSchedule);
    expect(status).toBe(VaccineStatus.DUE_SOON);
  });

  it("Harder edge case: out-of-doses should return COMPLETED (and not crash)", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60)];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: [] };

    const lastVaccine = {
      dose_number: 1, // schedule.length === 1 => nextDose null
      dose_date: d("2026-01-01"),
    };

    expect(() => determineStatus(userInfo, lastVaccine, vaccineSchedule)).not.toThrow();
    expect(determineStatus(userInfo, lastVaccine, vaccineSchedule)).toBe(VaccineStatus.COMPLETED);
  });

  it("Harder: conflict case — condition is both required and ineligible (ineligibility should win)", () => {
    vi.setSystemTime(d("2026-02-20"));

    const vaccineSchedule = [dose(60, ["pregnant"], ["pregnant"])];
    const userInfo = { dob: d("2025-12-22"), medicalConditions: ["pregnant"] };

    const status = determineStatus(userInfo, null, vaccineSchedule);
    expect(status).toBe(VaccineStatus.NOT_ELIGIBLE);
  });
});