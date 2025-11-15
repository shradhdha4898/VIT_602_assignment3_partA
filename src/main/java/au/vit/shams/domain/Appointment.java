package au.vit.shams.domain;
import java.time.LocalDate; import java.time.LocalDateTime;


/**
 * Represents a single appointment between a patient and a doctor.
 * Stores appointment ID, doctor ID, patient ID, time range,
 * and the status (BOOKED / CANCELLED).
 */
public class Appointment {

   
    //Possible states of an appointment.
    public enum Status { BOOKED, CANCELLED }

    // Unique ID for the appointment
    private final String id;

    // ID of the doctor for this appointment
    private final String doctorId;

    // ID of the patient booking this appointment
    private final String patientId;

    // Start and end time of the appointment
    private final LocalDateTime start, end;

    // Current status of the appointment
    private Status status;

    // Creates a new Appointment instance.
     public Appointment(String id, String doctorId, String patientId,
                       LocalDateTime start, LocalDateTime end) {

        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.start = start;
        this.end = end;

        // New appointments start in BOOKED state
        this.status = Status.BOOKED;
    }

    // ------------------- Getters -------------------


    public String getId() { return id; }

    public String getDoctorId() { return doctorId; }

    public String getPatientId() { return patientId; }

    public LocalDateTime getStart() { return start; }

    public LocalDateTime getEnd() { return end; }

    public Status getStatus() { return status; }

    // ------------------- Business Methods -------------------

    
    public void cancel() {
        this.status = Status.CANCELLED;
    }

     // @return the date (yyyy-MM-dd) of the appointment, without time information
    
    public LocalDate getDate() {
        return start.toLocalDate();
    }

    // Creates a readable representation of the appointment, useful for logs and debugging.
 
    @Override
    public String toString() {
        return "Appointment{" + id +
                ", patient=" + patientId +
                ", doctor=" + doctorId +
                ", " + start + " to " + end +
                ", status=" + status + "}";
    }
}