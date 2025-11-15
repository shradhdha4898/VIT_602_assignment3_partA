package au.vit.shams.repository;

import au.vit.shams.domain.Appointment;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* In-memory implementation of AppointmentRepository.
 * Stores all appointments inside a thread-safe map.
 */

public class InMemoryAppointmentRepository implements AppointmentRepository {

  // Thread-safe storage for appointments keyed by ID
  private final Map<String, Appointment> store = new ConcurrentHashMap<>();

  // Save or update an appointment
  public Appointment save(Appointment a) {
    store.put(a.getId(), a);
    return a;
  }

  // Find an appointment by ID 
  public Optional<Appointment> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  // Retrieve all appointments for a doctor on a specific date 
  public List<Appointment> findByDoctorAndDate(String doctorId, LocalDate day) {
    return store.values().stream()
            .filter(a -> a.getDoctorId().equals(doctorId))
            .filter(a -> a.getDate().equals(day))
            .toList();
  }

  // Return all appointments stored (mainly for debugging/testing) 
  public List<Appointment> all() {
    return new ArrayList<>(store.values());
  }
}
