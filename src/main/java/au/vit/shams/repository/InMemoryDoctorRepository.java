package au.vit.shams.repository;
import au.vit.shams.domain.Doctor;
import java.util.Map; import java.util.Optional; import java.util.concurrent.ConcurrentHashMap;
public class InMemoryDoctorRepository implements DoctorRepository {
  private final Map<String, Doctor> store = new ConcurrentHashMap<>();
  public Doctor save(Doctor d){ store.put(d.getId(), d); return d; }
  public Optional<Doctor> findById(String id){ return Optional.ofNullable(store.get(id)); }
  public Map<String, Doctor> store(){ return store; }
}