package patientservices;

import coreLogic.Patient;
import coreLogic.PatientStatus;
import coreLogic.VitalData;
import randomValue.RandomValueGeneration;
import sender.FrontEndEventSender;
import util.FormateUtil;

import java.util.Date;

public class PatientService {

    private final FrontEndEventSender frontEndEventSender = new FrontEndEventSender();
    private final RandomValueGeneration randomValueGeneration = new RandomValueGeneration();

    public Patient updatePatientVitalData(Patient patient) {
        patient.setPatientVitals(
                randomValueGeneration.generateVitalData());
        return patient;
    }

    public Patient createNewPatient(int patientId, int roomId) {
        return randomValueGeneration.buildNewPatient(patientId, roomId);
    }

    public Patient evaluatePatientStatus(Patient patient) {
        patient.setPatientStatus(evaluateVitalData(patient.getPatientVitals()));
        return patient;
    }

    private PatientStatus evaluateVitalData(VitalData vitalData) {
        PatientStatus newStatus = new PatientStatus(PatientStatus.Level.NORMAL, "");

        if (newStatus.getLevel() != PatientStatus.Level.CRITICAL){
            newStatus = evaluateHearthRate(vitalData.getHeartRate(), newStatus);
        }
        if (newStatus.getLevel() != PatientStatus.Level.CRITICAL){
            newStatus = evaluateRespirationRate(vitalData.getRespirationRate(), newStatus);
        }
        if (newStatus.getLevel() != PatientStatus.Level.CRITICAL){
            newStatus = evaluateBodyTemperature(vitalData.getBodyTemperature(), newStatus);
        }
        if (newStatus.getLevel() != PatientStatus.Level.CRITICAL){
            newStatus = evaluatePulseOximetry(vitalData.getHeartRate(), newStatus);
        }


        return newStatus;

    }

    private PatientStatus evaluateHearthRate(double hearthRate, PatientStatus newStatus) {
        if (hearthRate < 40) {
            newStatus.setLevel(PatientStatus.Level.CRITICAL);
            newStatus.setReason("Heart rate is too low: " + hearthRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (hearthRate < 60) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Heart rate is low: " + hearthRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (hearthRate < 100) {
            newStatus.setLevel(PatientStatus.Level.NORMAL);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (hearthRate < 120) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Heart rate is higher than normal: " + hearthRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        newStatus.setLevel(PatientStatus.Level.CRITICAL);
        newStatus.setReason("Heart rate is too high: " + hearthRate);
        newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));

        return newStatus;
    }

    private PatientStatus evaluateRespirationRate(double respirationRate, PatientStatus newStatus) {
        if (respirationRate < 8) {
            newStatus.setLevel(PatientStatus.Level.CRITICAL);
            newStatus.setReason("Respiration rate is too low: " + respirationRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (respirationRate < 12) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Respiration rate is low: " + respirationRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (respirationRate <= 20) {
            newStatus.setLevel(PatientStatus.Level.NORMAL);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (respirationRate <= 29) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Respiration rate is higher than normal: " + respirationRate);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        newStatus.setLevel(PatientStatus.Level.CRITICAL);
        newStatus.setReason("Respiration rate is too high: " + respirationRate);
        newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
        return newStatus;
    }



    private PatientStatus evaluateBodyTemperature(double temperature, PatientStatus newStatus) {
        if (temperature < 32.0) {
            newStatus.setLevel(PatientStatus.Level.CRITICAL);
            newStatus.setReason("Body temperature is dangerously low: " + temperature + " °C");
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (temperature < 36.0) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Body temperature is low: " + temperature + " °C");
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (temperature <= 37.0) {
            newStatus.setLevel(PatientStatus.Level.NORMAL);
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (temperature <= 39.0) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Body temperature is slightly elevated: " + temperature + " °C");
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        newStatus.setLevel(PatientStatus.Level.CRITICAL);
        newStatus.setReason("Body temperature is too high: " + temperature + " °C");
        newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
        return newStatus;
    }


    private PatientStatus evaluatePulseOximetry(double spo2, PatientStatus newStatus) {
        if (spo2 < 85.0) {
            newStatus.setLevel(PatientStatus.Level.CRITICAL);
            newStatus.setReason("Oxygen saturation is dangerously low: " + spo2 + "%");
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        if (spo2 < 95.0) {
            newStatus.setLevel(PatientStatus.Level.WARNING);
            newStatus.setReason("Oxygen saturation is slightly low: " + spo2 + "%");
            newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
            return newStatus;
        }

        newStatus.setLevel(PatientStatus.Level.NORMAL);
        newStatus.setTimestamp(FormateUtil.getEuropeanDateFormat(new Date()));
        return newStatus;
    }

}
