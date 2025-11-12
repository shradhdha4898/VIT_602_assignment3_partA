package au.vit.shams.repository;
import au.vit.shams.domain.Appointment;
import java.time.LocalDate; import java.util.*; import java.util.concurrent.ConcurrentHashMap;
public class InMemoryAppointmentRepository implements AppointmentRepository {
  private final java.util.Map<String, Appointment> store = new ConcurrentHashMap<>();
  public Appointment save(Appointment a){ store.put(a.getId(), a); return a; }
  public java.util.Optional<Appointment> findById(String id){ return java.util.Optional.ofNullable(store.get(id)); }
  public java.util.List<Appointment> findByDoctorAndDate(String doctorId, LocalDate day){
    return store.values().stream().filter(a->a.getDoctorId().equals(doctorId)).filter(a->a.getDate().equals(day)).toList();
  }
  public java.util.List<Appointment> all(){ return new java.util.ArrayList<>(store.values()); }
}