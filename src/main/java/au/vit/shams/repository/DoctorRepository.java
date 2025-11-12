package au.vit.shams.repository;
import au.vit.shams.domain.Doctor;
import java.util.Optional;
public interface DoctorRepository {
  Doctor save(Doctor d);
  Optional<Doctor> findById(String id);
}