// SHAMS Project - Comment added for documentation and readability.
package au.vit.shams.gui;
import au.vit.shams.repository.*; import au.vit.shams.service.*; import au.vit.shams.util.CsvDataLoader;
import javax.swing.*; import java.time.Clock;
public class GuiApp {
  public static void main(String[] args) throws Exception {

    // Core repositories
    AppointmentRepository apptRepo = new InMemoryAppointmentRepository();
    InMemoryDoctorRepository doctorRepo = new InMemoryDoctorRepository();

    // Core services
    NotificationService notificationService = new NotificationService(apptRepo, Clock.systemDefaultZone());
    SchedulingService schedulingService = new SchedulingService(apptRepo, doctorRepo);
 
    // Load demo doctors, patients, and appointments from CSV files
    CsvDataLoader.loadDoctors("data/doctors.csv", doctorRepo);
    CsvDataLoader.loadPatients("data/patients.csv", p -> {});
    CsvDataLoader.loadAppointments("data/appointments.csv", schedulingService);

    // Launch GUI window
    SwingUtilities.invokeLater(() -> new ShamsGui(schedulingService, notificationService, doctorRepo).setVisible(true));
  }
}