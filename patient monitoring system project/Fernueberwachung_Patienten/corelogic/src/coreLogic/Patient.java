package coreLogic;

public class Patient {

  private int patientId;
  private String patientName;
  private String patientSurName ;
  private Integer age;
  private String gender; // Nullable for the non stationary patients
  private VitalData patientVitals;
  private PatientStatus patientStatus;
  private Integer roomid ; //Nullable for the non stationary patients

  public Patient(int patientId, String patientName, String patientSurName, Integer age, String gender, VitalData patientVitals, PatientStatus patientStatus, Integer roomid) {
    this.patientId = patientId;
    this.patientName = patientName;
    this.patientSurName = patientSurName;
    this.age = age;
    this.gender = gender;
    this.patientVitals = patientVitals;
    this.patientStatus = patientStatus;
    this.roomid = roomid;
  }

  public String getPatientName() {
    return patientName;
  }

  public int getPatientId() {
    return patientId;
  }

  public VitalData getPatientVitals() {
    return patientVitals;
  }

  public PatientStatus.Level getPatientStatusLevel() {
    return patientStatus.getLevel();
  }

  public Integer getRoomId() {
    return roomid;
  }

  public Integer getAge() {
    return age;
  }

  public String getGender() {
    return gender;
  }

  public String getPatientSurName() {return patientSurName;}

  //SETTER
  public void setPatientVitals(VitalData patientVitals) {
    this.patientVitals = patientVitals;
  }

  public void setPatientStatus(PatientStatus patientStatus) {
    this.patientStatus = patientStatus;
  }

}
