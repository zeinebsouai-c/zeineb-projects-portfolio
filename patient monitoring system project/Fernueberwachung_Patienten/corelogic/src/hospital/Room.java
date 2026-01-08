package hospital;

import coreLogic.Patient;
import coreLogic.PatientStatus;
import exeptions.ExceptionHelper;

import java.util.HashMap;

public class Room {

    private int roomId;
    //hier Integer, damit man im constructor null übergeben kann und dort der default gesetzt wird
    private Integer capacity;

    private HashMap<Integer, Patient> patients = new HashMap<>();
    private PatientStatus.Level roomStatus;


    public Room(int roomNumber, Integer capacity) {
        this.roomId = roomNumber;
        if (capacity == null) {
            this.capacity = 4; // Default capacity if none is provided
        } else {
            this.capacity = capacity;
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public HashMap<Integer, Patient> getPatients() {
        return patients;
    }

    public Patient getPatientById(int patientId) {
        return patients.get(patientId);
    }

    public PatientStatus.Level getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(PatientStatus.Level roomStatus) {
        this.roomStatus = roomStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public void addPatient(int patientId, Patient patient) {

        if(patients.size() == capacity){
            ExceptionHelper.handleException(new Exception("Room " + roomId + " is full. Cannot add patient with ID: " + patientId));
        }else{
            patients.put(patientId, patient);
        }

    }
}
