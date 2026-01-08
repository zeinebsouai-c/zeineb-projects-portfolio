package frontend;

import coreLogic.Patient;
import coreLogic.VitalData;
import events.AddPatientEvent;
import events.AlertEvent;
import events.UpdatePStatusEvent;
import hospital.Floor;
import hospital.Hospital;
import hospital.HospitalService;
import hospital.Room;
import sender.FrontEndEventSender;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides functions for the frontend to retrieve, search,
 * and group patient and room data from the hospital model.
 * <p>
 * Mainly used by the dashboard to display filtered lists
 * of patients and rooms in the GUI.
 */

public class FrontEndService {

  private static Hospital hospital = new Hospital("Demo Hospital");
  private static HospitalService hospitalService= new HospitalService(hospital);
  private FrontEndEventSender sender = new FrontEndEventSender();
  private Timer timer = new Timer();

  /**
   * Returns a list of all patients in the hospital.
   *
   * @return list of all patients
   */

  public Hospital getHospital(){
    return this.hospital;
  }

  public HashMap<Integer, Patient> getPatientList(){
    return hospital.getPatientList();
  }

  public HashMap<Integer, Floor> getFloors(){
    return hospital.getFloors();
  }

  public FrontEndService() {
    this.hospital.initHospital(5, 8);
    startAutomaticUpdateStatus();
  }

  /**
   * Returns a list of all patients in the hospital.
   *
   * @return list of all patients
   */

  public List<Patient> getAllPatients() {
    return new ArrayList<>(hospital.getPatientList().values());
  }

  /**
   * Groups all patients by their room names.
   *
   * @return a map with room names as keys and lists of patients as values
   */

  public Map<String, List<Patient>> getPatientsGroupedByRoom() {
    Map<String, List<Patient>> result = new HashMap<>();

    for (Floor floor : hospital.getFloors().values()) {
      for (Room room : floor.getRooms().values()) {
        String roomName = "Room " + room.getRoomId();
        List<Patient> patients = new ArrayList<>(room.getPatients().values());
        result.put(roomName, patients);
      }
    }

    return result;
  }

  /**
   * Returns a list of all room names in the hospital.
   *
   * @return list of room names (e.g. "Room 101")
   */

  public List<String> getAllRoomNames() {
    List<String> roomNames = new ArrayList<>();

    for (Floor floor : hospital.getFloors().values()) {
      for (Room room : floor.getRooms().values()) {
        roomNames.add("Room " + room.getRoomId());
      }
    }

    return roomNames;
  }

  /**
   * Searches for patients by their name (case-insensitive).
   *
   * @param query the search term for the patient's name
   * @return list of patients whose names contain the search term
   */

  public List<Patient> searchPatientsByName(String query) {
    return hospital.getPatientList().values().stream()
      .filter(p -> p.getPatientName().toLowerCase().contains(query.toLowerCase()))
      .collect(Collectors.toList());
  }

  /**
   * Searches for room names that match the given query (case-insensitive).
   *
   * @param query the search term for the room name
   * @return list of room names containing the search term
   */

  public List<String> searchRoomsByName(String query) {
    return getAllRoomNames().stream()
      .filter(name -> name.toLowerCase().contains(query.toLowerCase()))
      .collect(Collectors.toList());
  }

  /**
   * Returns all patients in a specified room.
   *
   * @param roomName the name of the room (e.g. "Room 101")
   * @return list of patients in the given room, or an empty list if the room does not exist
   */

  public List<Patient> getPatientsByRoom(String roomName) {
    String roomNumberStr = roomName.replace("Room ", "");
    int roomNumber;

    try {
      roomNumber = Integer.parseInt(roomNumberStr);
    } catch (NumberFormatException e) {
      return new ArrayList<>(); // Invalid room name
    }

    for (Floor floor : hospital.getFloors().values()) {
      Room room = floor.getRooms().get(roomNumber);
      if (room != null) {
        return new ArrayList<>(room.getPatients().values());
      }
    }

    return new ArrayList<>();
  }

  /**
   * Returns a patient by their unique ID.
   *
   * @param patientId the ID of the patient
   * @return the patient with the specified ID, or {@code null} if not found
   */

  public Patient getPatientById(int patientId) {
    return hospital.getPatientList().get(patientId);
  }

  /**
   * Adds a Patient to the patientList of Hospital
   * Since .put also updated the value, if the key already exists, this function should also be used for updating a patient
   * @param patient
   */
  public static void addPatientToList(Patient patient){
    hospital.getPatientList().put(patient.getPatientId(), patient);
    hospital.setFloors(hospitalService.putPatientToTheRightRoom(patient));
  }

  /**
   * In the future, this function should build up the alert Scene and hand it over to the application
   * @param alertEvent
   */
  public static void handleAlert(AlertEvent alertEvent) {
  //TODO: Implement logic so that the alert is shown!
  }

  /**
   * this function is triggered by the Add Patient Button in the frontend
   * It starts the chain in the eventsystem, which eventually returns a Patient to the Hospital in this very class
   */
  public void sendAddPatientEvent(){
      sender.sendAddPatientEvent(
              new AddPatientEvent(
                      hospitalService.getValidPatientId(),
                      hospitalService.getValidRoomId()
              )
      );
  }


  /**
   * this function is triggered every 15 seconds
   * It starts the chain in the eventsystem, which eventually returns a Patient with new VitalDatas and a updated status if needed to the Hospital
   * @param patient
   */
  public void sendUpdatePatientStatusEvent(Patient patient){
    sender.sendUpdatePStatusEvent(
            new UpdatePStatusEvent(
                    patient
            )
    );
  }

  /**
   * starts the timer, which calls the UpdateVitalData chain of Events every 15 seconds.
   */
  private void startAutomaticUpdateStatus(){

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        for (Patient patient : hospital.getPatientList().values()) {
          sendUpdatePatientStatusEvent(patient);
        }
      }
    }, 0, 15000);
  }


  /**
   * This function returns a Map with the PatientId and the VitalData of the corresponding Patient
   * @return {@Code HashMap<Integer, VitalData> patientId, corresponding VitalData}
   */
  public HashMap<Integer, VitalData> getVitalData(){
    HashMap<Integer, VitalData> returnList = new HashMap<>();

    for(Patient patient : hospital.getPatientList().values()){
      returnList.put(patient.getPatientId(), patient.getPatientVitals());
    }

    return returnList;
  }
}
