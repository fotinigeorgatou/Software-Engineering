package models;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//VetClinic klasi 

public class VetClinic {
    private String name, address, phone, doctor, mapLink, distance;

    public VetClinic(String name, String address, String phone, String doctor, String mapLink, String distance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.doctor = doctor;
        this.mapLink = mapLink;
        this.distance = distance;
    }

    public String getName() { return name; }
    public String getDetails() {
        return "Κλινική: " + name +
                "\nΔιεύθυνση: " + address +
                "\nΑπόσταση: " + distance + " km" +
                "\nΤηλέφωνο: " + phone +
                "\nΥπεύθυνος Γιατρός: " + doctor;
    }
    public String getMapLink() { return mapLink; }

    @Override
    public String toString() { return name + " (" + distance + " km)"; }
}



//ApprovalRequest klasi

class ApprovalRequest {
    public enum Status { PENDING, APPROVED, REJECTED }

    private VetClinic selectedClinic;
    private Status status;

    public ApprovalRequest(VetClinic clinic) {
        this.selectedClinic = clinic;
        this.status = Status.PENDING;
    }

    public VetClinic getSelectedClinic() { return selectedClinic; }
    public Status getStatus() { return status; }

    public void approve() { this.status = Status.APPROVED; }
    public void reject() { this.status = Status.REJECTED; }
}


//EmergencyEvent klasi

class EmergencyEvent {
    private String category;
    private String comment;
    private List<String> emergencyLog;

    public EmergencyEvent() {
        this.emergencyLog = new ArrayList<>();
    }

    public void startEvent(String category, String comment) {
        this.category = category;
        this.comment = comment;
        addLogEntry("ΕΝΑΡΞΗ ΣΥΜΒΑΝΤΟΣ: Κατηγορία '" + category + "' - Σχόλιο: " + comment);
    }

    public void addLogEntry(String action) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String log = "[" + time + "] " + action;
        emergencyLog.add(log);
        System.out.println(log);
    }

    public List<String> getEmergencyLog() { return emergencyLog; }
}

