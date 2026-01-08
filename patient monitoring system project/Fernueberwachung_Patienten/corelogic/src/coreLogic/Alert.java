package coreLogic;

import util.FormateUtil;

import java.util.Date;

public class Alert {
  private int patientId;
  private String description;
  private PatientStatus.Level severity;
  private Date timestamp;
  private Integer roomId;

  public Alert(int patientId, String description, PatientStatus.Level severity, Integer roomId) {
    this.patientId = patientId;
    this.description = description;
    this.severity = severity;
    this.timestamp = FormateUtil.getEuropeanDateFormat(new Date());
    this.roomId = roomId;
  }

  public int getPatientId() { return patientId; }
  public String getDescription() { return description; }
  public PatientStatus.Level getSeverity() { return severity; }
  public Date getTimestamp() { return timestamp; }
  public Integer getRoomId(){
    return roomId;
  }
}
