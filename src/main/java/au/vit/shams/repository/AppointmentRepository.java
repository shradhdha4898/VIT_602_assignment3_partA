package au.vit.shams.repository;
import au.vit.shams.domain.Appointment;
import java.time.LocalDate; import java.util.List; import java.util.Optional;
public interface AppointmentRepository {
  Appointment save(Appointment a);
  Optional<Appointment> findById(String id);
  List<Appointment> findByDoctorAndDate(String doctorId, LocalDate day);
}