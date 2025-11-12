package au.vit.shams.service;
import au.vit.shams.domain.Appointment; import au.vit.shams.repository.AppointmentRepository;
import java.time.Clock; import java.time.Instant; import java.time.ZoneId; import java.util.PriorityQueue;
public class NotificationService {
  public static class Notification implements Comparable<Notification> {
    public final String appointmentId; public final Instant when;
    public Notification(String appointmentId, Instant when){ this.appointmentId=appointmentId; this.when=when; }
    public int compareTo(Notification o){ return this.when.compareTo(o.when); }
  }
  private final AppointmentRepository apptRepo; private final Clock clock; private final PriorityQueue<Notification> queue = new PriorityQueue<>();
  public NotificationService(AppointmentRepository apptRepo, Clock clock){ this.apptRepo=apptRepo; this.clock=clock; }
  public void onAppointmentBooked(Appointment a){
    var t1 = a.getStart().minusHours(24).atZone(ZoneId.systemDefault()).toInstant();
    var t2 = a.getStart().minusHours(2).atZone(ZoneId.systemDefault()).toInstant();
    queue.add(new Notification(a.getId(), t1)); queue.add(new Notification(a.getId(), t2));
  }
  public int dispatchDue(){ Instant now = clock.instant(); int dispatched=0; while(!queue.isEmpty() && !queue.peek().when.isAfter(now)){ queue.poll(); dispatched++; } return dispatched; }
}