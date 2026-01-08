package coreLogic;

import util.FormateUtil;

import java.util.Date;

public class PatientStatus {
  public enum Level { NORMAL, WARNING, CRITICAL}

  private Level level;
  private String reason;
  private Date timestamp;

  public PatientStatus(Level level, String reason) {
    this.level = level;
    this.reason = reason;
    this.timestamp = FormateUtil.getEuropeanDateFormat(new Date()); // Set current time as default
  }

  public Level getLevel() { return level; }
  public String getReason() { return reason; }
  public Date getTimestamp() { return timestamp; }

  public void setLevel(Level level) {
    this.level = level;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
