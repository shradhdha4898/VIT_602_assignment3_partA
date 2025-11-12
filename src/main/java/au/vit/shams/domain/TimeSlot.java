package au.vit.shams.domain;
import java.time.LocalDateTime; import java.time.LocalTime;
public class TimeSlot {
  private final LocalTime start, end;
  public TimeSlot(LocalTime start, LocalTime end){ this.start=start; this.end=end; }
  public LocalTime getStart(){return start;} public LocalTime getEnd(){return end;}
  public static boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd, LocalDateTime bStart, LocalDateTime bEnd){
    return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
  }
}