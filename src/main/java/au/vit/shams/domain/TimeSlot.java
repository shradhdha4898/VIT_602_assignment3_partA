package au.vit.shams.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a doctor's available working time window for a specific day.
 * Used to check whether requested appointments fall within valid hours.
 */

public class TimeSlot {

    // Daily start and end times for availability (e.g., 09:00–17:00)
    private final LocalTime start, end;

    //Creates a timeslot for a doctor's working hours
    public TimeSlot(LocalTime start, LocalTime end)
 {
        this.start = start;
        this.end = end;
    }

    // @return start time of the timeslot
    public LocalTime getStart() { return start; }

    // @return end time of the timeslot 
    public LocalTime getEnd() { return end; }

    /**
     * Checks if two appointment time ranges overlap.
     *
     * Logic:
     *  - Appointment A starts before B ends
     *  - and Appointment A ends after B starts
     *
     * @return true if schedules overlap (conflict)
     */

    public static boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd,
                                   LocalDateTime bStart, LocalDateTime bEnd) {
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }
}