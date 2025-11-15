package au.vit.shams.repository;

import au.vit.shams.domain.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Repository interface for storing and retrieving appointments.
 
public interface AppointmentRepository {

  // Save or update an appointment
  Appointment save(Appointment a);

  // Find an appointment by its unique ID 
  Optional<Appointment> findById(String id);

  // Get all appointments for a doctor on a specific date 
  List<Appointment> findByDoctorAndDate(String doctorId, LocalDate day);
}
