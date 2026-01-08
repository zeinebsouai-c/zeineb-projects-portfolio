package hospital;

import coreLogic.PatientStatus;

import java.util.HashMap;
import java.util.Map;

public class Floor {
    private int id;

    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<PatientStatus.Level, Integer> statusOverview = new HashMap<>();


    public Floor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public Map<PatientStatus.Level, Integer> getStatusOverview() {
        return statusOverview;
    }

    public void addRoom(int roomId, Room room) {
        rooms.put(roomId, room);
    }

}
