/**Doctor Domain Model
 * Represents an immutable data structure for a Doctor in the system.
 * This class primarily holds scheduling and identification data.
 */

package au.vit.shams.domain;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class Doctor
 {
  // --- Immutable Attributes---
  // @return The unique ID of the doctor, full name, medical specialty
  private final String id, name, specialty;
  private final Map<DayOfWeek, List<TimeSlot>> workingHours;
  public Doctor(String id, String name, String specialty, Map<DayOfWeek, List<TimeSlot>> workingHours){
    this.id=id; this.name=name; this.specialty=specialty; this.workingHours=workingHours;
  }

     // --- Public Accessors (Getters)---
    // All methods return the immutable state of the object.
  public String getId()

{
 return id;
} 
public String getName(){return name;}
  public String getSpecialty(){return specialty;}
  public Map<DayOfWeek, List<TimeSlot>> getWorkingHours(){return workingHours;}
}