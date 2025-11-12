package au.vit.shams.gui;
import au.vit.shams.repository.*; import au.vit.shams.service.*; import au.vit.shams.util.CsvDataLoader;
import javax.swing.*; import java.time.Clock;
public class GuiApp {
  public static void main(String[] args) throws Exception {
    AppointmentRepository apptRepo = new InMemoryAppointmentRepository();
    InMemoryDoctorRepository doctorRepo = new InMemoryDoctorRepository();
    NotificationService notificationService = new NotificationService(apptRepo, Clock.systemDefaultZone());
    SchedulingService schedulingService = new SchedulingService(apptRepo, doctorRepo);
    // CSV loading
    CsvDataLoader.loadDoctors("data/doctors.csv", doctorRepo);
    CsvDataLoader.loadPatients("data/patients.csv", p -> {});
    CsvDataLoader.loadAppointments("data/appointments.csv", schedulingService);
    SwingUtilities.invokeLater(() -> new ShamsGui(schedulingService, notificationService, doctorRepo).setVisible(true));
  }
}