package hospital;

import coreLogic.Patient;

import java.util.HashMap;

public class HospitalService {

    private Hospital hospital;

    public int getValidPatientId(){
        return hospital.getValidPatientId();
    }

    public int getValidRoomId() {
       return hospital.getValidRoomId();
    }

    public HospitalService(Hospital hospital) {
        this.hospital = hospital;
    }


    public HashMap<Integer, Floor> putPatientToTheRightRoom(Patient patient){

        for(Floor floor : hospital.getFloors().values()){
            for(Room room : floor.getRooms().values()){

                if(patient.getRoomId() == room.getRoomId()){
                    room.getPatients().put(patient.getPatientId(), patient);
                }
            }
        }

        return hospital.getFloors();
    }

}
