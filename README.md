# VaccineWatch

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
