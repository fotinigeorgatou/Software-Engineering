import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class usecase9_model_classes {}

class VetClinic {
    private String name, address, phone, doctor, mapLink, distance;

    public VetClinic(String name, String address, String phone, String doctor, String mapLink, String distance) {
        this.name = name; this.address = address; this.phone = phone;
        this.doctor = doctor; this.mapLink = mapLink; this.distance = distance;
    }

    public String getName()     { return name; }
    public String getAddress()  { return address; }
    public String getPhone()    { return phone; }
    public String getDoctor()   { return doctor; }
    public String getMapLink()  { return mapLink; }
    public String getDistance() { return distance; }

    public String getDetails() {
        return "Κλινική: " + name +
                "\nΔιεύθυνση: " + address +
                "\nΑπόσταση: " + distance + " km" +
                "\nΤηλέφωνο: " + phone +
                "\nΥπεύθυνος Γιατρός: " + doctor;
    }

    @Override
    public String toString() { return name + "  (" + distance + " km)"; }
}

class ApprovalRequest {
    public enum Status { PENDING, APPROVED, REJECTED }

    private VetClinic selectedClinic;
    private Status status;
    private String ownerCounterProposal;

    public ApprovalRequest(VetClinic clinic) {
        this.selectedClinic = clinic;
        this.status = Status.PENDING;
    }

    public VetClinic getSelectedClinic()        { return selectedClinic; }
    public Status getStatus()                    { return status; }
    public String getOwnerCounterProposal()      { return ownerCounterProposal; }
    public void approve()                        { this.status = Status.APPROVED; }
    public void reject(String counterProposal)   { this.status = Status.REJECTED; this.ownerCounterProposal = counterProposal; }
}

class EmergencyEvent {
    public enum EventStatus { ACTIVE, RESOLVED, FALSE_ALARM }

    private String category;
    private String comment;
    private List<String> emergencyLog;
    private EventStatus status;
    private LocalDateTime startTime;

    public EmergencyEvent() {
        this.emergencyLog = new ArrayList<>();
        this.status = EventStatus.ACTIVE;
    }

    public void startEvent(String category, String comment) {
        this.category = category;
        this.comment  = comment;
        this.startTime = LocalDateTime.now();
        addLogEntry("ΕΝΑΡΞΗ ΣΥΜΒΑΝΤΟΣ: Κατηγορία '" + category + "' — Σχόλιο: " + comment);
    }

    public void addLogEntry(String action) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String log  = "[" + time + "] " + action;
        emergencyLog.add(log);
        System.out.println(log);
    }

    public void resolve() {
        this.status = EventStatus.RESOLVED;
        addLogEntry("ΛΗΞΗ ΣΥΝΑΓΕΡΜΟΥ — Το συμβάν επιλύθηκε.");
    }

    public void markFalseAlarm() {
        this.status = EventStatus.FALSE_ALARM;
        // Καμία καταγραφή στο emergency log (βάσει use case)
        System.out.println("[FALSE ALARM] Ακύρωση από Host — χωρίς καταγραφή.");
    }

    public List<String> getEmergencyLog() { return emergencyLog; }
    public EventStatus getStatus()         { return status; }
    public String getCategory()            { return category; }
    public LocalDateTime getStartTime()    { return startTime; }
}
