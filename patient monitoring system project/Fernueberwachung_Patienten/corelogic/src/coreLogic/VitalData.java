package coreLogic;

import util.FormateUtil;

import java.util.Date;

public class VitalData {

  private double heartRate;
  private double respirationRate;
  private double bodyTemperature;
  private double pulseOximetry;
  private Date timestamp;

  public VitalData(double heartRate, double respirationRate, double bodyTemperature, double pulseOximetry, Date timestamp) {
    this.heartRate = heartRate;
    this.respirationRate = respirationRate;
    this.bodyTemperature = bodyTemperature;
    this.pulseOximetry = pulseOximetry;
    this.timestamp = FormateUtil.getEuropeanDateFormat(timestamp);
  }

  public double getHeartRate() {
    return heartRate;
  }

  public double getRespirationRate() {
    return respirationRate;
  }

  public double getBodyTemperature() {
    return bodyTemperature;
  }

  public double getPulseOximetry() {
    return pulseOximetry;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
