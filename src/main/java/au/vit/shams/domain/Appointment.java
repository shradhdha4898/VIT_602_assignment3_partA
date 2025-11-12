package au.vit.shams.domain;
import java.time.LocalDate; import java.time.LocalDateTime;
public class Appointment {
  public enum Status { BOOKED, CANCELLED }
  private final String id, doctorId, patientId;
  private final LocalDateTime start, end; private Status status;
  public Appointment(String id, String doctorId, String patientId, LocalDateTime start, LocalDateTime end){
    this.id=id; this.doctorId=doctorId; this.patientId=patientId; this.start=start; this.end=end; this.status=Status.BOOKED;
  }
  public String getId(){return id;} public String getDoctorId(){return doctorId;} public String getPatientId(){return patientId;}
  public LocalDateTime getStart(){return start;} public LocalDateTime getEnd(){return end;}
  public Status getStatus(){return status;} public void cancel(){ this.status=Status.CANCELLED; }
  public LocalDate getDate(){ return start.toLocalDate(); }
  @Override public String toString(){ return "Appointment{"+id+", patient="+patientId+", doctor="+doctorId+", "+start+" to "+end+", status="+status+"}"; }
}