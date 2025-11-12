**SHAMS- Smart Healthcare Appointments Management System.**

========================================================

**Module:** ICT602 Software Design and Implementation.

**Date:** 12 Nov 2025
--------------------------------------------------------
**PROJECT DESCRIPTION**
--------------------------------------------------------
SHAMS is a Java-based desktop-based healthcare scheduling system.

It enables users to create, reschedule, cancel, and manage doctor appointments
through both a Command-Line Interface (CLI) and a Graphical User Interface (GUI).

The system will have a reminder-notification module that will automatically.
appointment reminders are sent out by queues and dispatches.


--------------------------------------------------------
**TECHNOLOGY STACK**
--------------------------------------------------------
Language: Java 20

Java Verification Test: Apache Maven 3.9.

Architecture: Layered (N-Tier) – Presentation → Service → Domain → Repository

GUI Library: Java Swing

Testing: JUnit 5

Dependencies: Maven -managed through pom.xml.


--------------------------------------------------------
**PROJECT STRUCTURE**
--------------------------------------------------------
src/main/java/au/vit/shams/
 ├─ domain/       → Appointment, Doctor, Patient, TimeSlot, AppointmentRequest
 ├─ repository/   → AppointmentRepository, DoctorRepository, InMemory implementations
 ├─ service/      → SchedulingService, NotificationService
 ├─ util/         → CsvDataLoader
 └─ gui/          → ShamsGui (UI) and GuiApp (entry point)
data/             → doctors.csv, patients.csv, appointments.csv
Run-GUI.bat
pom.xml


--------------------------------------------------------
**HOW TO BUILD AND RUN**
--------------------------------------------------------

**Option 1 – Using Maven**

1\. Elevated Command Prompt in this folder.

2\. Run the following commands:

Commands:
mvn clean package

The command to run the project would be, java -cp target/shams-1.0.0.jar au.vit.shams.gui.GuiApp


(This starts Swing GUI version)

**Option 2 – Using the Batch File**

1\. Double-click Run-GUI.bat

2\. In case Windows SmartScreen displays a warning, it should be clicked “Run Anyway.”

3\. Wait until the SHAMS GUI window is open.

Auto-Loaded Sample Data

At startup, SHAMS automatically loads sample data from:

/data/doctors.csv
/data/patients.csv
/data/appointments.csv


**Option 3 - CLI Version (Optional)**

java -cp target/shams-1.0.0.jar au.vit.shams.Main


--------------------------------------------------------
**TESTING**
--------------------------------------------------------
Execution of unit and integration tests:

 mvn test

The result of the tests of JUnit 5 are displayed in the console and are saved as:
target/surefire-reports/

--------------------------------------------------------
**Run Instructions**
--------------------------------------------------------
1. mvn clean package

2. java -cp target/shams-1.0.0.jar au.vit.shams.GuiApp
   OR double-click Run-GUI.bat



--------------------------------------------------------
**GUI DEMONSTRATION STEPS**
--------------------------------------------------------

| Action                       | Description                                                                         |
| ---------------------------- | ----------------------------------------------------------------------------------- |
| **Seed Doctor**              | Adds two demo doctors (D1 – Dr Smith – 09:00-17:00, D2 – Dr Johnson – 10:00-18:00). |
| **Book**                     | Creates a new appointment for the entered patient, doctor, and time slot.           |
| **Reschedule**               | Updates an existing appointment to a new date/time.                                 |
| **Cancel by ID**             | Cancels an appointment using its ID.                                                |
| **List Tomorrow by Doctor**  | Displays all appointments scheduled for a specific doctor tomorrow.                 |
| **Dispatch Reminders (Now)** | Simulates reminder notifications being sent (24 h & 2 h before).                    |


--------------------------------------------------------
**SHAMS Manual Test Examples**
--------------------------------------------------------
1️⃣ Test Case 1 – Basic Doctor Seeding

Seed Doctors

Click Seed Doctor (D1 09:00–17:00) to load D1 (Dr Smith).

Click Seed Doctor (D2 10:00–18:00) to load D2 (Dr Jones).

Use D1 or D2 in Doctor ID when booking.

Expected Result:
Seeded Doctor D1 – Dr Smith (09:00–17:00). Use Patient P1 for demo.


✅ Confirms that Doctor D1 was created in memory.
-------------------------------------------------
2️⃣ Test Case 2 – Book Appointment (Valid Slot)

Inputs (Booking Panel):

Field	Value
Patient ID	P1
Doctor ID	D1
Start	2025-11-10T10:00
End	2025-11-10T10:30

Click Book
Expected Result (in log):

Booked: Appointment{<UUID>, patient=P1, doctor=D1, 2025-11-10T10:00 to 2025-11-10T10:30, status=BOOKED}
Reminder queued (24h & 2h) for <UUID>

✅ The appointment is stored and reminder notifications queued.
----------------------------------------------------
3️⃣ Test Case 3 – Attempt Double Booking (Conflict Detection)

Inputs (Booking Panel):

Field	Value
Patient ID	P2
Doctor ID	D1
Start	2025-11-10T10:15
End	2025-11-10T10:45

Click Book

Expected Result:
Error: CONFLICT

✅ The system rejects overlapping bookings for the same doctor.
----------------------------------------------------
4️⃣ Test Case 4 – Out of Hours Appointment

Inputs:

Field	Value
Patient ID	P3
Doctor ID	D1
Start	2025-11-10T19:00
End	2025-11-10T19:30

Click Book
Expected Result:
Error: OUT_OF_HOURS

✅ Confirms working-hours validation is functioning.
----------------------------------------------------
5️⃣ Test Case 5 – Reschedule Appointment

Use the Appointment ID displayed after booking (txtApptId is pre-filled automatically).

Inputs (Reschedule Panel):

Field	Value
Appointment ID	<UUID from earlier booking>
New Start	2025-11-10T12:00
New End	2025-11-10T12:30

Click Reschedule
Expected Result:
Rescheduled: Appointment{<UUID>, patient=P1, doctor=D1, 2025-11-10T12:00 to 2025-11-10T12:30, status=BOOKED}


✅ The appointment time updates successfully.
----------------------------------------------------
6️⃣ Test Case 6 – Cancel Appointment

Inputs:

Field	Value
Appointment ID	<UUID>

Click Cancel by ID
Expected Result:
Cancelled: <UUID>


✅ Appointment is marked as CANCELLED.
----------------------------------------------------
7️⃣ Test Case 7 – List Tomorrow’s Appointments

Inputs:
Field	Value
Doctor ID	D1
Click List Tomorrow by Doctor

Expected Result:
If any appointments exist for tomorrow, they’ll be listed. Otherwise:

No appointments for D1 on 2025-11-11

--------------------------------------------------------
**KNOWN LIMITATIONS**
--------------------------------------------------------

\- Only in-memory repositories are currently used.

\- The system now seeds two demo doctors (D1 – Dr Smith and D2 – Dr Johnson) for testing.

\- Simulated Reminder dispatch done through GUI log rather than email/SMS.


--------------------------------------------------------
**FUTURE ENHANCEMENTS**
--------------------------------------------------------

\- Add database persistence (JDBC/ MySQL).

\- Implement multi-physician management.

\- Dailies of exports in CSV or PDF.

\- Introduce REST API mobile access points.








