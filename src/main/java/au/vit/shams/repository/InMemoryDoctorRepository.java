package au.vit.shams.repository;

import au.vit.shams.domain.Doctor;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//In-memory repository for storing doctor records.
 
public class InMemoryDoctorRepository implements DoctorRepository {

  // Thread-safe storage of doctors by ID
  private final Map<String, Doctor> store = new ConcurrentHashMap<>();

  // Save or update a doctor 
  public Doctor save(Doctor d) {
    store.put(d.getId(), d);
    return d;
  }

  // Find a doctor by ID 
  public Optional<Doctor> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  // Expose full store (mainly for debugging or loading) 
  public Map<String, Doctor> store() {
    return store;
  }
}