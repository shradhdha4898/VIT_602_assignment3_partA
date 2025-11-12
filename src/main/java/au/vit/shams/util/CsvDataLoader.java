package au.vit.shams.util;
import au.vit.shams.domain.*; import au.vit.shams.repository.InMemoryDoctorRepository; import au.vit.shams.service.SchedulingService;
import java.io.BufferedReader; import java.io.FileReader; import java.nio.file.Files; import java.nio.file.Path;
import java.time.LocalDateTime; import java.time.LocalTime; import java.time.DayOfWeek; import java.util.*;
public class CsvDataLoader {
  public static void loadDoctors(String path, InMemoryDoctorRepository repo) throws Exception {
    if(!Files.exists(Path.of(path))) return;
    try(BufferedReader br=new BufferedReader(new FileReader(path))){
      String line=br.readLine(); while((line=br.readLine())!=null){
        String[] t=line.split(","); String id=t[0].trim(), name=t[1].trim(), sp=t[2].trim();
        LocalTime s=LocalTime.parse(t[3].trim()), e=LocalTime.parse(t[4].trim());
        Map<DayOfWeek, java.util.List<TimeSlot>> wh=new EnumMap<>(DayOfWeek.class);
        for(DayOfWeek dow: DayOfWeek.values()) wh.put(dow, java.util.List.of(new TimeSlot(s,e)));
        repo.save(new Doctor(id,name,sp,wh));
      }
    }
  }
  public static void loadPatients(String path, java.util.function.Consumer<Patient> consumer) throws Exception {
    if(!Files.exists(Path.of(path))) return;
    try(BufferedReader br=new BufferedReader(new FileReader(path))){
      String line=br.readLine(); while((line=br.readLine())!=null){
        String[] t=line.split(","); consumer.accept(new Patient(t[0].trim(), t[1].trim(), t[2].trim()));
      }
    }
  }
  public static void loadAppointments(String path, SchedulingService scheduler) throws Exception {
    if(!Files.exists(Path.of(path))) return;
    try(BufferedReader br=new BufferedReader(new FileReader(path))){
      String line=br.readLine(); while((line=br.readLine())!=null){
        String[] t=line.split(","); var req=new AppointmentRequest(t[0].trim(), t[1].trim(),
          LocalDateTime.parse(t[2].trim()), LocalDateTime.parse(t[3].trim()));
        try{ scheduler.book(req);}catch(Exception ignored){}
      }
    }
  }
}