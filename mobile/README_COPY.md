# VaccineWatch

Take control of your health, with the flick of a wrist. **VaccineWatch** is an Android app with smartwatch integrations built during **.devHacks 2026**, for the **QDoc's Challenge: Smart Vaccine Eligibility and Reminder System** category.

## Problem Statement

As we were doing this hackathon, we received an email that a case of measles was detected on University's property a week ago.

In just the first week of February 2026, [Manitoba confirmed 44 new cases of measles](https://www.cbc.ca/news/canada/manitoba/measles-cases-february-9.7089341), prompting blanket warnings across southern municipalities and alerts for major public gatherings in Winnipeg, including a Winnipeg Jets game. Adults frequenly forget booster schedules or mistakenly believe their primary series is sufficient for long-term children protection, which consequently lead to children missing age-based vaccines, creating preventable outbreaks.

Meanwhile, clinics struggle to track eligibility because Manitoba's public health guidelines are incredibly complex and change frequently based on regional outbreaks and new vaccine releases.

**VaccineWatch** seeks to solve this problem by giving everyone the ability to take control of their immunization past, present, and future.

## Features

- Notifications and smart reminders delivered straight to the user's smartwatch on their upcoming vaccination dates.
- Rule-based AI engine that determines if patients are eligible for vaccines following the [Manitoba vaccine eligibility](https://www.gov.mb.ca/health/publichealth/cdc/vaccineeligibility.html).
- Immunization timeline visualizations with accompanying optional explanations.
- Mobile app with minimal, accessible UI for patients to update their information quickly and conveniently.

## Tech Stack

- **Hardware**: [PineTime](https://pine64.org/devices/pinetime/).
- **Firmware**: [InfiniTime](https://github.com/InfiniTimeOrg/InfiniTime).
- **Watch Integration**: [Gadgetbridge](https://gadgetbridge.org/).
- **Mobile (Android)**: [Expo](https://expo.dev/).
- **Backend**: [Supabase](https://supabase.com/).

## Architecture

1. A new user register their information in the VaccineWatch application, with previous vaccination history, if applicable.
1. The user's information is persisted in the database, and the rule engine builds out the list of vaccines they're eligible for.
1. Taking into account the user's vaccination history (if any), the rule engine returns the user's immunization schedule to the mobile app.
1. The mobile app forwards the user's immunization schedule to the watch, and will push notifications on immunization reminders to the watch.
1. Every day, the mobile app updates the user's immunization timeline and upcoming schedule appropriately, and dispatch notifications to the watch as necessary. Users may input more of their vaccination records into the app, which will update the timeline.

### Mobile App + Backend Service

#### Patient Profile Input

The app will display an onboarding screen before prompting the user to input the following information. After registering their email (or phone number) and password for authentication, the form is broken up using **progressive disclosure**:

1. **Demographics**
    * Date of Birth
    * Sex at Birth & Gender Identity
    * Postal Code
    * First Nations Community Resident (*Yes/No*)
2. **Medical Profile**
    * Are you currently pregnant? (*Yes/No*)
        * (*Yes*) Estimated due date
    * Do you have any immunocompromising conditions? (e.g., HIV, congenital immune deficiencies, lack of a spleen) (*Yes/No/Unsure*)
    * Are you currently receiving specialized medical care? (e.g., cancer treatments, dialysis, stem cell or solid organ transplants) (*Yes/No/Unsure*)
    * Do you have a chronic illness? (e.g., chronic liver/kidney disease, severe asthma/COPD) (*Yes/No/Unsure*)
3. **Lifestyle**
    * Are you a healthcare worker, first responder, or do you work in a high-risk setting? (*Yes/No*)
    * Some vaccines are recommended based on lifestyle factors (e.g., men who have sex with men, illicit drug use). Would you like to review these criteria to see if you qualify for additional coverage? (*Yes/No*)
        * (*Yes*) Extra questions...TBA
    * Are you planning international travel in the next 6 months? (*Yes/No*)
4. **History**: Option to upload photos of immunization records, or to add each row of vaccine (with dropdown menu + search bar) and date injected
5. **Reminders**: Multiple checkboxes
    * Smartwatch Integration
    * Email
        * (*Yes*) Input email
    * SMS
        * (*Yes*) Input phone number

After the patient's profile is added to the system, they can edit it anytime.

#### Vaccine Rule Engine

The backend service stores information about each vaccine, their eligibility, and recommended vaccination frequencies. When a new user is registered to the system, the service compares each vaccine in the database with the user's information and immunization history to catalogue *all* vaccines into one of six categories:

1. **Completed** (Blue): The user has already taken the maximum number of doses of the vaccine.
1. **Not yet due** (Yellow): The user is physically allowed to get the vaccine, but is still far away from the recommended date.
1. **Eligible** (Green): The user has reached the recommended date for the vaccine.
1. **Overdue** (Orange): The user has missed the recommended timeframe for the vaccine and should not get it.
1. **Not eligible** (Red): The user cannot physically get the vaccine due to a hard medical rule.
1. **Confirm with Doctor** (Question Mark Overlay): The potential risks of the vaccine are uncertain and the user should double check with a doctor. This is to cover the cases where the user fills out "Unsure"s in the questionnaires.
1. **Irrelevant** (Grey): The vaccine is neither beneficial nor harmful to the user and is not worth consideration.

In the mobile app, a tab called **Vaccines** will display the full database of vaccines, their statuses in relation to the user, and optional pop-up explanations of what each vaccine does. Vaccines may be sorted or filtered by their category. **Irrelevant** vaccines are hidden by default. The **Irrelevant** category is to separate the vaccines that aren't helpful from vaccines that would be actively harmful without overloading the user with information.

Statuses of vaccines of category **Not yet due**, **Eligible**, and **Overdue** are updated via a daily cron job. Statuses of all vaccines are updated when the user's profile is updated, or a vaccination record is added.

#### Immunization Timeline Visualization

The second tab of the app, **Tracker**, contains two different sections:

1. **Add Vaccination Record**: A button that allows the user to input a new vaccination record.
1. **Immunization History**: The user's vaccination record history listed in reverse chronological order. All entries are editable and deletable, as and with safeguards for misinputs.

#### Smart Reminder System

The final, and default tab of the app, **Reminder**, contains three different sections:

1. **Due Vaccinations**: The user's currently **Eligible** vaccines, ordered by how much time remaining until the vaccine becomes overdue. At the bottom of the section, a reminder button saying "Just vaccinated? Track your vaccination!" that leads to the **Add Vaccination Record** menu above.
1. **Upcoming Vaccinations**: The user's currently **Not yet due** vaccines, ordered by how much time remaining until the vaccine is eligible.
1. **Notifications**: Allow the user to connect/disconnect from their smartwatch/email/phone number as a notification mechanism, as well as options for frequency and conditions of alerts.

### Smart Watch Integration

The Android **VaccineWatch** application communicates to the smart watch using a companion app available only on Android **Gadgetbridge**, which is capable of interfacing with many devices using BLE (Bluetooth Low Energy). **PineTime**, an open-source watch, is used in the demo.

**Gadgetbridge**'s biggest advantage is its security guarantee via not being connected to the internet anyhow. The technical complexity of this is, then, how to pass required messages to the smart watch via the backend service without internet. Therefore, the communication protocol relies on Android's native [Intent API](https://developer.android.com/reference/android/content/Intent). The application, built and running on Android, will broadcast an Intent to the Android OS with an action such as "push notifications to gadgets," who will then forward it to **Gadgetbridge**.

### PineTime + InfiniTime

**InfiniTime** is an open-source firmware written in C++ to accompany **PineTime** watches. This allows for development of custom **PineTime** apps, making the watch more than a notification forwarder. Currently, the **Smart Reminder System** is implemented directly on **PineTime**, as a more convenient alternative to the phone app.

## Future Directions

* **Clinic-Facing Portals**: The system should support doctors tracking their patients' vaccination records instead of relying on the patient to do it themselves. A short-term solution is to utilize a 2FA-like system where the patient can share with the doctor administering the vaccine a code that permits them to directly update the vaccination record of said patient. Scalable solutions will require some form of authentication and data governance, for both patients and clinics.
* **More Notification Options**: Working implementations for notifying via email and phone number require completion.
* **Smoother Onboarding**: The onboarding questionnaire may be lacking in medical specificity, or conversely, be too detailed and overwhelming for new patients. Further user studies and feedback need to be collected from both users and experts to improve this. Given a clinic-facing portal, doctors should also be able to do this for their clients.
* **More Ecosystem Support**: Currently, reliance on **Gadgetbridge** makes it such that any architecture not having an Android frontend in there somewhere is completely unable to interface with the smartwatch. This is a fundamental limitation with how there is no "general solution" when working with IoTs and firmwares especially for the smartwatch ecosystem, but alternatives can be looked into.
* **More Smart Watch Apps**: The watch technology is still relatively underused. More tinkering with the watch for extra features, for example a QR code display that doctors can scan to list the patient's eligibility checks, would be able to address real problems.

## Team Members

- Jagrit Sharma
- Juan Rempel
- Duy Kha Tran
- Logan Furedi
- Peter Vu

## Credits

- Organizers: [.devClub](https://devclub.ca/)
- Sponsors: QDoc Virtual Healthcare, Payworks, G3, Taiv, IG Wealth Management, Ubisoft Winnipeg, Niche Technology, Pollard Banknote Limited, Department of Computer Science University of Manitoba, Priceline Partner Solutions

## QDoc's Challenge: Smart Vaccine Eligibility and Reminder System

### The Problem

Patients often:

* Don’t know which vaccines they are eligible for
* Forget booster schedules
* Miss age-based or risk-based immunizations
* Lose track of vaccination history

Clinics struggle with:

* Manual eligibility checks
* Tracking booster timelines
* Reaching patients at the right time
* Preventing missed immunizations

### The Challenge

Build a system that:

* Determines vaccine eligibility based on:
    * Age
    * Medical history
    * Last dose date
    * Risk factors (e.g.: chronic conditions, pregnancy, immunocompromised status)
* Identifies overdue or upcoming vaccines
* Sends intelligent reminders
* Displays a patient-friendly immunization timeline

Features:

* Patient Profile Input
    * A simple interface where users can enter their information (e.g.: Demographics, DOB, chronic conditions, vaccination history)
    * Can mock patient profiles, upload CSV or use a form-based input
* Vaccine Rule Engine
    * Create a rule-based eligibility engine either from scratch using custom logic or using an open-source rule engine framework.
    * Reference examples:
        * [Manitoba vaccine eligibility](https://www.gov.mb.ca/health/publichealth/cdc/vaccineeligibility.html)
        * [Manitoba immunization schedules](https://www.gov.mb.ca/health/publichealth/cdc/div/schedules.html)
    * The system should check patient data, apply rule logic and determine which vaccines are eligible now, due soon, overdue and not eligible.
    * Simplified/mocked rules are fine.
* Immunization Timeline Visualization
    * Display completed, due soon, overdue, and not eligible vaccines.
    * Display vaccination history with a timeline.
    * Include clear visual indications (e.g.: color-coded status)
* Smart Reminder System
    * Identify vaccines due within x days.
    * Simulate email reminder, SMS alert, and/or in-app notification.