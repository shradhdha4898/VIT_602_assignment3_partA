package au.vit.shams.domain;

/**
 * Represents a patient in the SHAMS system.
 * Stores basic identifying information used during appointment booking.
 */
public class Patient {

    // Unique identifier for the patient
    private final String id;

    // Patient's full name
    private final String name;

    // Contact phone number (used for notifications, logs, etc.)
    private final String phone;

    //Creates a new Patient record with essential details
    public Patient(String id, String name, String phone)
  {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // ---------------- Getters ----------------

    // @return patient ID 
    public String getId() { return id; }

    // @return patient name
    public String getName() { return name; }

    // @return patient phone number 
    public String getPhone() { return phone; }
}