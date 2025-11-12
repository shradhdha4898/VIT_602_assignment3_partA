package au.vit.shams.domain;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
public class Doctor {
  private final String id, name, specialty;
  private final Map<DayOfWeek, List<TimeSlot>> workingHours;
  public Doctor(String id, String name, String specialty, Map<DayOfWeek, List<TimeSlot>> workingHours){
    this.id=id; this.name=name; this.specialty=specialty; this.workingHours=workingHours;
  }
  public String getId(){return id;} public String getName(){return name;}
  public String getSpecialty(){return specialty;}
  public Map<DayOfWeek, List<TimeSlot>> getWorkingHours(){return workingHours;}
}