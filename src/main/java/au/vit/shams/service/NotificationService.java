package au.vit.shams.service;

import au.vit.shams.domain.Appointment;
import au.vit.shams.repository.AppointmentRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.PriorityQueue;

public class NotificationService {

    public static class Notification implements Comparable<Notification> {
        public final String appointmentId;
        public final Instant when;

        public Notification(String appointmentId, Instant when){
            this.appointmentId=appointmentId; this.when=when;
        }

        // Compares based on 'when' time; critical for PriorityQueue behavior
        public int compareTo(Notification o){
            return this.when.compareTo(o.when);
        }
    }

    private final AppointmentRepository apptRepo; // Repository for potential data retrieval.
    private final Clock clock; // Injected dependency for getting deterministic current time ('now').
    private final PriorityQueue<Notification> queue = new PriorityQueue<>(); // Stores reminders, automatically ordered by dispatch time.

    // Constructor using Dependency Injection.
     
    public NotificationService(AppointmentRepository apptRepo, Clock clock){
        this.apptRepo=apptRepo; this.clock=clock;
    }

    //Logic triggered after a successful booking to schedule two reminders (24h and 2h before start).
     
    public void onAppointmentBooked(Appointment a){
        // Calculate 24-hour reminder time.
        var t1 = a.getStart().minusHours(24).atZone(ZoneId.systemDefault()).toInstant();
        // Calculate 2-hour reminder time.
        var t2 = a.getStart().minusHours(2).atZone(ZoneId.systemDefault()).toInstant();

        // Add both reminders to the priority queue.
        queue.add(new Notification(a.getId(), t1)); queue.add(new Notification(a.getId(), t2));
    }

   
    public int dispatchDue(){
        Instant now = clock.instant(); // Get current time from the injected Clock.
        int dispatched=0;
        // Loop while the earliest notification is due (its time is NOT after now).
        while(!queue.isEmpty() && !queue.peek().when.isAfter(now)){
            queue.poll(); // Remove the element (simulating dispatch).
            dispatched++;
        }
        return dispatched;
    }
}