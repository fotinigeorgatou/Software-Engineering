import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class usecase10_model_classes {}

// Το καθεστώς συγχρονισμού (Για την Εναλλακτική Ροή 1: Offline)
enum SyncStatus { SYNCED, PENDING }

// Οι τύποι των εγγραφών
enum ActivityType { FOOD, WALK, TOILET, MEDS, SLEEP, STRANGE_BEHAVIOR, CALL_LOG, SYSTEM_MSG }

class DiaryEntry {
    private ActivityType type;
    private String details;
    private LocalDateTime timestamp;
    private SyncStatus syncStatus;
    private boolean hasOwnerLike; // Για το reaction του ιδιοκτήτη

    public DiaryEntry(ActivityType type, String details, LocalDateTime timestamp, SyncStatus status) {
        this.type = type;
        this.details = details;
        this.timestamp = timestamp;
        this.syncStatus = status;
        this.hasOwnerLike = false;
    }

    public ActivityType getType() { return type; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getFormattedTime() { return timestamp.format(DateTimeFormatter.ofPattern("HH:mm")); }
    public SyncStatus getSyncStatus() { return syncStatus; }
    public void setSyncStatus(SyncStatus status) { this.syncStatus = status; }
    public boolean hasOwnerLike() { return hasOwnerLike; }
    public void addOwnerLike() { this.hasOwnerLike = true; }

    public String getEmoji() {
        switch(type) {
            case FOOD: return "🦴";
            case WALK: return "🦮";
            case TOILET: return "💩";
            case MEDS: return "💊";
            case SLEEP: return "💤";
            case STRANGE_BEHAVIOR: return "⚠️";
            case CALL_LOG: return "🎥";
            case SYSTEM_MSG: return "💬";
            default: return "📌";
        }
    }
}

class LiveDiary {
    private List<DiaryEntry> entries;
    private boolean isNetworkOnline;

    public LiveDiary() {
        this.entries = new ArrayList<>();
        this.isNetworkOnline = true; // By default online
    }

    public void setNetworkStatus(boolean isOnline) {
        this.isNetworkOnline = isOnline;
        // Αν επιστρέψουμε online, συγχρονίζουμε τα PENDING
        if (isOnline) {
            for (DiaryEntry entry : entries) {
                if (entry.getSyncStatus() == SyncStatus.PENDING) {
                    entry.setSyncStatus(SyncStatus.SYNCED);
                }
            }
            System.out.println("[SYSTEM] Το δίκτυο επανήλθε. Εκκρεμείς εγγραφές συγχρονίστηκαν.");
        }
    }

    public boolean isOnline() { return isNetworkOnline; }

    public void addEntry(ActivityType type, String details) {
        SyncStatus status = isNetworkOnline ? SyncStatus.SYNCED : SyncStatus.PENDING;
        DiaryEntry newEntry = new DiaryEntry(type, details, LocalDateTime.now(), status);
        entries.add(newEntry);

        System.out.println("[LOG] Προστέθηκε: " + type + " - " + details + " | Κατάσταση: " + status);

        // Αν είναι online, κάνουμε simulate το Push Notification
        if (isNetworkOnline && type != ActivityType.SYSTEM_MSG && type != ActivityType.CALL_LOG) {
            System.out.println("[PUSH NOTIFICATION -> Ιδιοκτήτης] Νέα ενημέρωση: " + type);
        }
    }

    public List<DiaryEntry> getTimeline() {
        // Στην πραγματικότητα θα επιστρέφαμε ταξινομημένη λίστα, εδώ την έχουμε ήδη με σειρά προσθήκης.
        return entries;
    }
}
