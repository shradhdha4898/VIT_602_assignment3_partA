package au.vit.shams.domain;
public class Patient {
  private final String id, name, phone;
  public Patient(String id, String name, String phone){ this.id=id; this.name=name; this.phone=phone; }
  public String getId(){return id;} public String getName(){return name;} public String getPhone(){return phone;}
}