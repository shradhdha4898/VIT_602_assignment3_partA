package au.vit.shams.service;
import au.vit.shams.domain.*; import au.vit.shams.repository.AppointmentRepository; import au.vit.shams.repository.DoctorRepository;
import java.time.LocalDate; import java.time.LocalDateTime; import java.util.List; import java.util.UUID;
public class SchedulingService {
  private final AppointmentRepository apptRepo; private final DoctorRepository doctorRepo;
  public SchedulingService(AppointmentRepository apptRepo, DoctorRepository doctorRepo){ this.apptRepo=apptRepo; this.doctorRepo=doctorRepo; }
  public Appointment book(AppointmentRequest req){
    var doctor = doctorRepo.findById(req.doctorId).orElseThrow(()->new IllegalArgumentException("Doctor not found"));
    var slots = doctor.getWorkingHours().get(req.start.getDayOfWeek());
    boolean withinHours = slots != null && slots.stream().anyMatch(ts ->
      !req.start.toLocalTime().isBefore(ts.getStart()) && !req.end.toLocalTime().isAfter(ts.getEnd()));
    if(!withinHours) throw new IllegalStateException("OUT_OF_HOURS");
    List<Appointment> existing = apptRepo.findByDoctorAndDate(req.doctorId, req.start.toLocalDate());
    for(var a: existing){ if(TimeSlot.overlaps(req.start, req.end, a.getStart(), a.getEnd())) throw new IllegalStateException("CONFLICT"); }
    var a = new Appointment(UUID.randomUUID().toString(), req.doctorId, req.patientId, req.start, req.end);
    return apptRepo.save(a);
  }
  public Appointment reschedule(String id, LocalDateTime newStart, LocalDateTime newEnd){
    var a = apptRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Not found"));
    List<Appointment> sameDay = apptRepo.findByDoctorAndDate(a.getDoctorId(), newStart.toLocalDate());
    for(var other: sameDay){ if(!other.getId().equals(id) && TimeSlot.overlaps(newStart, newEnd, other.getStart(), other.getEnd())) throw new IllegalStateException("CONFLICT"); }
    var updated = new Appointment(id, a.getDoctorId(), a.getPatientId(), newStart, newEnd);
    return apptRepo.save(updated);
  }
  public void cancel(String id){ var a = apptRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Not found")); a.cancel(); apptRepo.save(a); }
  public List<Appointment> listByDoctorAndDate(String doctorId, LocalDate day){ return apptRepo.findByDoctorAndDate(doctorId, day); }
}