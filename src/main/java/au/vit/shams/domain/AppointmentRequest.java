package au.vit.shams.domain;
import java.time.LocalDateTime;

/**
 * Represents the raw request that a patient submits when attempting to book an appointment with a doctor.
 * 
 * This is used by SchedulingService to validate:
 *  - working hours
 *  - time conflicts
 *  - doctor/patient existence
 */

public class AppointmentRequest {
  public final String patientId, doctorId;
  public final LocalDateTime start, end;
     
  //Creates a new appointment request containing only the information needed for validation and booking.
    
  public AppointmentRequest(String patientId, String doctorId, LocalDateTime start, LocalDateTime end){
    this.patientId=patientId; this.doctorId=doctorId; this.start=start; this.end=end;
  }
}