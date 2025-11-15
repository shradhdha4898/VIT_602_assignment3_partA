package au.vit.shams.repository;

import au.vit.shams.domain.Doctor;
import java.util.Optional;

//Repository interface for managing doctor records.

public interface DoctorRepository {

  //Save or update a doctor 
  Doctor save(Doctor d);

  //Find a doctor by ID 
  Optional<Doctor> findById(String id);
}