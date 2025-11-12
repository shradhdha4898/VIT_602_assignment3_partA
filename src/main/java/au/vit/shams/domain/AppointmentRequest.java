package au.vit.shams.domain;
import java.time.LocalDateTime;
public class AppointmentRequest {
  public final String patientId, doctorId; public final LocalDateTime start, end;
  public AppointmentRequest(String patientId, String doctorId, LocalDateTime start, LocalDateTime end){
    this.patientId=patientId; this.doctorId=doctorId; this.start=start; this.end=end;
  }
}