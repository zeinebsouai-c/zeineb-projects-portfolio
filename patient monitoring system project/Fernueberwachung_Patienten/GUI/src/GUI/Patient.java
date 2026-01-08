package GUI;

public class Patient {
    private String name;
    private String status; // "green", "yellow", "red"
    private String stationType; //"ICU", "Ward", etc.
    private String roomNumber;

    public Patient(String name, String status, String roomNumber) {
        this.name = name;
        this.status = status;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}

