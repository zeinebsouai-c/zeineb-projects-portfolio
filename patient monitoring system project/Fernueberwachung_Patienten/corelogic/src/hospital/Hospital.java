package hospital;

import coreLogic.Patient;
import coreLogic.PatientStatus;
import exeptions.ExceptionHelper;
import randomValue.RandomValueGeneration;

import java.util.Collections;
import java.util.HashMap;

public class Hospital {
    public String hospitalName;

    private HashMap<Integer, Floor> floors = new HashMap<>();
    private HashMap<PatientStatus.Level, Integer> statusOverview = new HashMap<>();
    private HashMap<Integer, Patient> patientList = new HashMap<>();
    private RandomValueGeneration gen = new RandomValueGeneration();
    private HospitalService hospitalService = new HospitalService(this);
    //TODO: Add patientList, which should contain all the patients and should be updated when a patient is added or delted to a room

    public Hospital(String name) {
        this.hospitalName = name;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public HashMap<Integer, Floor> getFloors() {
        return floors;
    }

    public HashMap<PatientStatus.Level, Integer> getStatusOverview() {
        return statusOverview;
    }

    public void addFloor(int etageId, Floor etage) {
        floors.put(etageId, etage);
    }

    public Floor getEtage(int etageId) {
        return floors.get(etageId);
    }

    public HashMap<Integer, Patient> getPatientList() {
        return patientList;
    }

    public void addPatient(Patient patient) {
        //TODO: MAKE RIGHT!
        if (patient == null) {
            ExceptionHelper.handleException(new IllegalArgumentException("Hospital: Patient cannot be null"));
        }
        patientList.put(patient.getPatientId(), patient);
    }

    public void initHospital(int numberOfFloors, int numberOfRoomsPerFloor) {
        for(int i = 1; i <= numberOfFloors; i++) {
            Floor f = new Floor(i);
            for (int j = 0; j <= numberOfRoomsPerFloor; j++) {
                String roomId = String.valueOf(i) + "0" + String.valueOf(j);

                Room room = new Room(Integer.parseInt(roomId), 4);
                f.addRoom(room.getRoomId(), room);
            }
           floors.put(f.getId(), f);
        }

        while(patientList.size() < numberOfFloors * 5){
            Patient patient = gen.buildNewPatient(hospitalService.getValidPatientId(), hospitalService.getValidRoomId());
            patientList.put(patient.getPatientId(), patient);
            hospitalService.putPatientToTheRightRoom(patient);
        }


    }

    public void setFloors(HashMap<Integer, Floor> floors) {
        this.floors = floors;
    }

    public int getValidPatientId(){
        if(patientList.isEmpty()) {
            return 01;
        }
        return Collections.max(patientList.keySet()) +1;
    }

    public int getValidRoomId() {
        // Assuming rooms are identified by their IDs and are stored in a way that allows us to find the minimum ID
        try{
            for(Floor floor : floors.values()) {
                for(Room room : floor.getRooms().values()) {
                    if(room.getPatients().size() < room.getCapacity()) {
                        return room.getRoomId();
                    }
                }
            }
        }catch(NullPointerException npe){
            ExceptionHelper.handleException(new IllegalArgumentException("HospitalService: No valid room found or hospital floors are not initialized"));
        }

        return -1;
    }
}
