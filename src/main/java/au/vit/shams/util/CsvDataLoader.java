package au.vit.shams.util;

import au.vit.shams.domain.*; 
import au.vit.shams.repository.InMemoryDoctorRepository; // Specific repository used for loading.
import au.vit.shams.service.SchedulingService; // Service used to book appointments (applying all business rules).
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.nio.file.Files; 
import java.nio.file.Path;
import java.time.LocalDateTime; 
import java.time.LocalTime; 
import java.time.DayOfWeek; 
import java.util.*;

public class CsvDataLoader {

    /**
     * Loads Doctor data from a CSV file into the provided repository.
     * Assumes a fixed daily working time for all doctors (e.g., 9:00 to 17:00, defined in the CSV).
     *
     * @param path The file path to the CSV data.
     * @param repo The {@link InMemoryDoctorRepository} to save the loaded doctors to.
     */
    public static void loadDoctors(String path, InMemoryDoctorRepository repo) throws Exception {
        if(!Files.exists(Path.of(path))) return; // Skip if file doesn't exist.
        try(BufferedReader br=new BufferedReader(new FileReader(path))){
            String line=br.readLine(); // Skip header line.
            while((line=br.readLine())!=null){
                String[] t=line.split(","); 
                String id=t[0].trim(), name=t[1].trim(), sp=t[2].trim();
                LocalTime s=LocalTime.parse(t[3].trim()), e=LocalTime.parse(t[4].trim());

                // Map working hours: sets the same TimeSlot for every DayOfWeek.
                Map<DayOfWeek, java.util.List<TimeSlot>> wh=new EnumMap<>(DayOfWeek.class);
                for(DayOfWeek dow: DayOfWeek.values()) wh.put(dow, java.util.List.of(new TimeSlot(s,e)));
                
                // Create and save the immutable Doctor object.
                repo.save(new Doctor(id,name,sp,wh));
            }
        }
    }

    /**
     * Loads Patient data from a CSV file.
     * Uses a {@code Consumer} functional interface to allow the caller to decide how to handle each loaded patient.
     *
     * @param path The file path to the CSV data.
     * @param consumer The function to execute on each created {@link Patient} object.
     */
    public static void loadPatients(String path, java.util.function.Consumer<Patient> consumer) throws Exception {
        if(!Files.exists(Path.of(path))) return; // Skip if file doesn't exist.
        try(BufferedReader br=new BufferedReader(new FileReader(path))){
            String line=br.readLine(); // Skip header line.
            while((line=br.readLine())!=null){
                String[] t=line.split(","); 
                // Create new Patient and pass it to the consumer (e.g., an in-memory repository's save method).
                consumer.accept(new Patient(t[0].trim(), t[1].trim(), t[2].trim()));
            }
        }
    }

    /**
     * Loads initial Appointment data from a CSV file by delegating to the {@link SchedulingService}.
     * This ensures all business rules (like conflict detection) are applied even during startup.
     * Errors during booking (e.g., conflicts) are **ignored** to allow the application to proceed with valid data.
     *
     * @param path The file path to the CSV data.
     * @param scheduler The {@link SchedulingService} used to book the appointments.
     */
    public static void loadAppointments(String path, SchedulingService scheduler) throws Exception {
        if(!Files.exists(Path.of(path))) return; // Skip if file doesn't exist.
        try(BufferedReader br=new BufferedReader(new FileReader(path))){
            String line=br.readLine(); // Skip header line.
            while((line=br.readLine())!=null){
                String[] t=line.split(","); 
                // Map CSV data to an AppointmentRequest DTO.
                var req=new AppointmentRequest(t[0].trim(), t[1].trim(),
                  LocalDateTime.parse(t[2].trim()), LocalDateTime.parse(t[3].trim()));
                
                // Attempt to book the appointment using the service layer.
                try{ scheduler.book(req);}
                catch(Exception ignored){
                    // Ignoring exceptions (like CONFLICT or OUT_OF_HOURS) to load only valid appointments.
                }
            }
        }
    }
}