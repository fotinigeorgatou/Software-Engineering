import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// ─────────────────────────────────────────────────────────────────────────────
// usecase10_model_classes.java
// Πλήρης υλοποίηση όλων των κλάσεων του Class Diagram (Use Case 10 – Live Diary)
// Καλύπτει: Βασική Ροή + Εναλλακτικές Ροές 1, 2, 3
// ─────────────────────────────────────────────────────────────────────────────

public class usecase10_model_classes {}

// ══════════════════════════════════════════════════════════════════════════════
// ENUMERATIONS
// ══════════════════════════════════════════════════════════════════════════════

/** Κατάσταση συγχρονισμού μιας εγγραφής — Εναλλακτική Ροή 1 (Offline) */
enum SyncStatus {
    SYNCED,   // Εστάλη επιτυχώς στον server
    PENDING   // Εκκρεμεί — αποθηκεύτηκε τοπικά λόγω offline
}

/** Τύποι δραστηριότητας που μπορεί να καταγράψει ο Host */
enum ActivityType {
    FOOD,             // Γεύμα
    WALK,             // Βόλτα
    TOILET,           // Τουαλέτα
    MEDS,             // Φάρμακα
    SLEEP,            // Ύπνος
    STRANGE_BEHAVIOR, // Περίεργη Συμπεριφορά (Εναλλακτική Ροή 2)
    CALL_LOG,         // Καταγραφή βιντεοκλήσης
    SYSTEM_MSG        // Αυτόματο μήνυμα συστήματος (Εναλλακτική Ροή 3)
}

/** Τύποι reaction του Ιδιοκτήτη */
enum ReactionType {
    LIKE,   // ❤️
    HEART,  // 🥰
    LAUGH,  // 😂
    WOW     // 😮
}

// ══════════════════════════════════════════════════════════════════════════════
// ENTITY CLASSES  (από το Class Diagram)
// ══════════════════════════════════════════════════════════════════════════════

// ── DiaryEntry ─────────────────────────────────────────────────────────────

/**
 * Αντιπροσωπεύει μία καταχώρηση στο Live Diary.
 * Περιέχει SyncStatus (Εναλλακτική Ροή 1) και υποστηρίζει reactions ιδιοκτήτη.
 */
class DiaryEntry {
    private ActivityType type;
    private String       details;
    private String       photoPath;        // Προαιρετική φωτογραφία (Βασική Ροή βήμα 3)
    private int          durationMinutes;  // π.χ. 30 λεπτά βόλτα
    private LocalDateTime timestamp;
    private SyncStatus   syncStatus;
    private Reaction     ownerReaction;    // Reaction ιδιοκτήτη (Βασική Ροή βήμα 7)

    public DiaryEntry(ActivityType type, String details,
                      int durationMinutes, String photoPath,
                      LocalDateTime timestamp, SyncStatus status) {
        this.type            = type;
        this.details         = details;
        this.durationMinutes = durationMinutes;
        this.photoPath       = photoPath;
        this.timestamp       = timestamp;
        this.syncStatus      = status;
        this.ownerReaction   = null;
    }

    // Getters
    public ActivityType  getType()            { return type; }
    public String        getDetails()         { return details; }
    public String        getPhotoPath()       { return photoPath; }
    public int           getDurationMinutes() { return durationMinutes; }
    public LocalDateTime getTimestamp()       { return timestamp; }
    public SyncStatus    getSyncStatus()      { return syncStatus; }
    public Reaction      getOwnerReaction()   { return ownerReaction; }
    public boolean       hasPhoto()           { return photoPath != null && !photoPath.isEmpty(); }

    // Setters
    public void setSyncStatus(SyncStatus status) { this.syncStatus = status; }

    /** Ο Ιδιοκτήτης προσθέτει reaction — Βασική Ροή βήμα 7 */
    public void addOwnerReaction(ReactionType reactionType) {
        this.ownerReaction = new Reaction(reactionType, LocalDateTime.now());
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getEmoji() {
        switch (type) {
            case FOOD:             return "🦴";
            case WALK:             return "🦮";
            case TOILET:           return "💩";
            case MEDS:             return "💊";
            case SLEEP:            return "💤";
            case STRANGE_BEHAVIOR: return "⚠️";
            case CALL_LOG:         return "🎥";
            case SYSTEM_MSG:       return "💬";
            default:               return "📌";
        }
    }

    public String getDisplayLabel() {
        switch (type) {
            case FOOD:             return "Γεύμα";
            case WALK:             return "Βόλτα";
            case TOILET:           return "Τουαλέτα";
            case MEDS:             return "Φάρμακα";
            case SLEEP:            return "Ύπνος";
            case STRANGE_BEHAVIOR: return "Περίεργη Συμπεριφορά";
            case CALL_LOG:         return "Βιντεοκλήση";
            case SYSTEM_MSG:       return "Μήνυμα Συστήματος";
            default:               return type.toString();
        }
    }
}

// ── Reaction ───────────────────────────────────────────────────────────────

/**
 * Reaction Ιδιοκτήτη σε μία καταχώρηση του Timeline.
 * Βασική Ροή βήμα 7-8. Αντιστοιχεί στο Class Diagram.
 */
class Reaction {
    private ReactionType  type;
    private LocalDateTime timestamp;

    public Reaction(ReactionType type, LocalDateTime timestamp) {
        this.type      = type;
        this.timestamp = timestamp;
    }

    public ReactionType  getType()      { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String getEmoji() {
        switch (type) {
            case LIKE:  return "❤️";
            case HEART: return "🥰";
            case LAUGH: return "😂";
            case WOW:   return "😮";
            default:    return "❤️";
        }
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}

// ── Message ────────────────────────────────────────────────────────────────

/**
 * Μήνυμα chat μεταξύ Host και Ιδιοκτήτη.
 * Καλύπτει: Βασική Ροή (chat), Εναλλακτική Ροή 3 (έτοιμα/custom μηνύματα απόρριψης).
 * Κεντρικό αρχείο επικοινωνίας — αποθηκεύει και αυτοματοποιημένα μηνύματα συστήματος.
 */
class Message {
    private String        senderId;
    private String        content;
    private LocalDateTime timestamp;
    private boolean       isAutoReply;   // Αυτόματο μήνυμα απόρριψης (Εναλλακτική Ροή 3)

    public Message(String senderId, String content,
                   LocalDateTime timestamp, boolean isAutoReply) {
        this.senderId    = senderId;
        this.content     = content;
        this.timestamp   = timestamp;
        this.isAutoReply = isAutoReply;
    }

    public String        getSenderId()   { return senderId; }
    public String        getContent()    { return content; }
    public LocalDateTime getTimestamp()  { return timestamp; }
    public boolean       isAutoReply()   { return isAutoReply; }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /** Έτοιμα μηνύματα απόρριψης — Εναλλακτική Ροή 3 */
    public static String[] getPredefinedBusyMessages() {
        return new String[] {
                "Είμαι απασχολημένος, θα σας καλέσω σε 10 λεπτά!",
                "Σε λίγο είμαι μαζί σας!",
                "Αυτή τη στιγμή δεν μπορώ, θα επικοινωνήσω σύντομα.",
                "Παρακαλώ αφήστε μήνυμα, θα σας πάρω αμέσως!"
        };
    }
}

// ── VideoCall ──────────────────────────────────────────────────────────────

/**
 * Αντιπροσωπεύει μία βιντεοκλήση μεταξύ Host και Ιδιοκτήτη.
 * Βασική Ροή βήμα 9-10. Εναλλακτική Ροή 3 (απόρριψη κλήσης).
 * Αντιστοιχεί στο Class Diagram (callerId, durationSeconds, status).
 */
class VideoCall {
    private String        callerId;
    private String        receiverId;
    private int           durationSeconds;
    private String        status;           // "ringing", "active", "ended", "rejected", "missed"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public VideoCall(String callerId, String receiverId) {
        this.callerId        = callerId;
        this.receiverId      = receiverId;
        this.durationSeconds = 0;
        this.status          = "ringing";
        this.startTime       = LocalDateTime.now();
    }

    public void accept() {
        this.status = "active";
        System.out.println("[VIDEO CALL] Η κλήση έγινε αποδεκτή.");
    }

    /** Τερματισμός κλήσης — καταγραφή διάρκειας (Βασική Ροή βήμα 10) */
    public void end(int durationSeconds) {
        this.durationSeconds = durationSeconds;
        this.endTime         = LocalDateTime.now();
        this.status          = "ended";
        System.out.println("[VIDEO CALL] Κλήση τερματίστηκε. Διάρκεια: " + durationSeconds + " δευτερόλεπτα.");
    }

    /** Απόρριψη κλήσης — Εναλλακτική Ροή 3 */
    public void reject() {
        this.status  = "rejected";
        this.endTime = LocalDateTime.now();
        System.out.println("[VIDEO CALL] Η κλήση απορρίφθηκε.");
    }

    /** Αναπάντητη κλήση */
    public void markMissed() {
        this.status  = "missed";
        this.endTime = LocalDateTime.now();
    }

    public String getCallSummary() {
        if (status.equals("ended")) {
            return "Βιντεοκλήση — Διάρκεια: " + (durationSeconds / 60) + " λεπτά";
        } else if (status.equals("rejected")) {
            return "Αναπάντητη βιντεοκλήση";
        } else {
            return "Βιντεοκλήση (" + status + ")";
        }
    }

    // Getters
    public String getCallerId()        { return callerId; }
    public String getReceiverId()      { return receiverId; }
    public int    getDurationSeconds() { return durationSeconds; }
    public String getStatus()          { return status; }
}

// ── Notification ───────────────────────────────────────────────────────────

/**
 * Push Notification που στέλνεται στον Ιδιοκτήτη.
 * Βασική Ροή βήμα 6 (νέα ενημέρωση) και βήμα 8 (reaction ειδοποίηση Host).
 * Αντιστοιχεί στο Class Diagram (title, body, timestamp, dispatch()).
 */
class Notification {
    private String        title;
    private String        body;
    private LocalDateTime timestamp;
    private String        targetOwnerId;  // Σύνδεση με Owner.deviceToken

    public Notification(String title, String body, String targetOwnerId) {
        this.title         = title;
        this.body          = body;
        this.timestamp     = LocalDateTime.now();
        this.targetOwnerId = targetOwnerId;
    }

    /**
     * Αποστολή push notification στον παραλήπτη.
     * Στην παραγωγή: FCM/APNs call με Owner.deviceToken.
     */
    public void dispatch() {
        System.out.println("[PUSH → " + targetOwnerId + "] "
                + title + ": " + body
                + " @ " + timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public String getTitle()     { return title; }
    public String getBody()      { return body; }
    public String getTargetId()  { return targetOwnerId; }
}

// ── Pet ────────────────────────────────────────────────────────────────────

/** Το κατοικίδιο που φιλοξενείται. Αντιστοιχεί στο Class Diagram. */
class Pet {
    private String name;
    private String breed;
    private int    age;

    public Pet(String name, String breed, int age) {
        this.name  = name;
        this.breed = breed;
        this.age   = age;
    }

    public String getName()  { return name; }
    public String getBreed() { return breed; }
    public int    getAge()   { return age; }
}

// ── Host ───────────────────────────────────────────────────────────────────

/** Ο Host (φιλοξενών). Αντιστοιχεί στο Class Diagram. */
class Host {
    private String  hostId;
    private String  name;
    private boolean isAvailable;

    public Host(String hostId, String name) {
        this.hostId      = hostId;
        this.name        = name;
        this.isAvailable = true;
    }

    /** Καταγράφει νέα δραστηριότητα μέσω EntryController — Βασική Ροή βήμα 1-4 */
    public void createEntry(EntryController controller,
                            ActivityType type, String details,
                            int durationMinutes, String photoPath) {
        controller.createEntry(type, details, durationMinutes, photoPath);
    }

    /** Απαντά σε κλήση — Βασική Ροή βήμα 9 */
    public void respondToCall(VideoCall call, boolean accept) {
        if (accept) call.accept();
        else        call.reject();
    }

    public String  getHostId()      { return hostId; }
    public String  getName()        { return name; }
    public boolean isAvailable()    { return isAvailable; }
    public void    setAvailable(boolean v) { this.isAvailable = v; }
}

// ── Owner ──────────────────────────────────────────────────────────────────

/** Ο Ιδιοκτήτης του κατοικιδίου. Αντιστοιχεί στο Class Diagram. */
class Owner {
    private String ownerId;
    private String name;
    private String deviceToken; // Για push notifications (FCM/APNs)

    public Owner(String ownerId, String name, String deviceToken) {
        this.ownerId     = ownerId;
        this.name        = name;
        this.deviceToken = deviceToken;
    }

    /** Στέλνει reaction σε καταχώρηση — Βασική Ροή βήμα 7 */
    public void sendReaction(DiaryEntry entry, ReactionType reactionType,
                             NotificationController notifController, String hostId) {
        entry.addOwnerReaction(reactionType);
        // Ειδοποίηση Host για reaction — Βασική Ροή βήμα 8
        notifController.notifyHostReaction(hostId, reactionType, entry);
        System.out.println("[OWNER " + name + "] Έστειλε reaction "
                + reactionType + " στην καταχώρηση: " + entry.getDetails());
    }

    /** Ξεκινά βιντεοκλήση — Βασική Ροή βήμα 9 */
    public VideoCall callHost(String hostId, CommunicationManager commManager) {
        return commManager.routeCall(ownerId, hostId);
    }

    public String getOwnerId()    { return ownerId; }
    public String getName()       { return name; }
    public String getDeviceToken(){ return deviceToken; }
}

// ── Booking ────────────────────────────────────────────────────────────────

/**
 * Η ενεργή κράτηση που συνδέει Host, Owner, Pet, LiveDiary.
 * Αντιστοιχεί στο Class Diagram — κεντρικό σημείο σύνδεσης.
 */
class Booking {
    private String    bookingId;
    private Host      host;
    private Owner     owner;
    private Pet       pet;
    private LiveDiary diary;    // composition — ένα Booking έχει ένα LiveDiary
    private List<VideoCall> calls;    // 0..* VideoCall
    private List<Message>  messages;  // 0..* Message

    public Booking(String bookingId, Host host, Owner owner, Pet pet) {
        this.bookingId = bookingId;
        this.host      = host;
        this.owner     = owner;
        this.pet       = pet;
        this.diary     = new LiveDiary();
        this.calls     = new ArrayList<>();
        this.messages  = new ArrayList<>();
    }

    public LiveDiary   getDiary()    { return diary; }
    public Host        getHost()     { return host; }
    public Owner       getOwner()    { return owner; }
    public Pet         getPet()      { return pet; }
    public String      getBookingId(){ return bookingId; }

    public void addCall(VideoCall call)       { calls.add(call); }
    public void addMessage(Message message)   { messages.add(message); }
    public List<VideoCall> getCalls()         { return calls; }
    public List<Message>   getMessages()      { return messages; }
}

// ── LiveDiary ──────────────────────────────────────────────────────────────

/**
 * Το κεντρικό Timeline των καταχωρήσεων.
 * Αντιστοιχεί στο Class Diagram.
 * Υποστηρίζει Offline mode (Εναλλακτική Ροή 1) με syncPendingEntries().
 */
class LiveDiary {
    private List<DiaryEntry> entries;
    private boolean          isNetworkOnline;

    public LiveDiary() {
        this.entries        = new ArrayList<>();
        this.isNetworkOnline = true;
    }

    public void setNetworkStatus(boolean isOnline) {
        boolean wasOffline = !isNetworkOnline && isOnline;
        this.isNetworkOnline = isOnline;
        if (wasOffline) {
            syncPendingEntries();
        }
        System.out.println("[NETWORK] Κατάσταση δικτύου: " + (isOnline ? "Online ✅" : "Offline 🔴"));
    }

    public boolean isOnline() { return isNetworkOnline; }

    /**
     * Προσθήκη εγγραφής.
     * Αν offline → PENDING, αλλιώς SYNCED + αποστολή notification.
     */
    public DiaryEntry addEntry(ActivityType type, String details,
                               int durationMinutes, String photoPath,
                               NotificationController notifController,
                               String ownerDeviceToken) {
        SyncStatus status = isNetworkOnline ? SyncStatus.SYNCED : SyncStatus.PENDING;
        // Offline: χρησιμοποιεί timestamp στιγμής πληκτρολόγησης (Εναλλακτική Ροή 1)
        DiaryEntry entry = new DiaryEntry(type, details, durationMinutes, photoPath,
                LocalDateTime.now(), status);
        entries.add(entry);

        System.out.println("[DIARY] +" + entry.getEmoji() + " " + entry.getDisplayLabel()
                + " | " + entry.getFormattedTime()
                + " | " + status
                + (durationMinutes > 0 ? " | " + durationMinutes + " λεπτά" : "")
                + (photoPath != null ? " | 📷 φωτογραφία" : ""));

        // Push Notification μόνο αν online και δεν είναι system/call entry
        if (isNetworkOnline && notifController != null
                && type != ActivityType.SYSTEM_MSG
                && type != ActivityType.CALL_LOG) {
            notifController.sendPushNotification(
                    "Νέα ενημέρωση: " + entry.getDisplayLabel(),
                    entry.getDetails(),
                    ownerDeviceToken
            );
        }
        return entry;
    }

    /**
     * Συγχρονισμός εκκρεμών εγγραφών — Εναλλακτική Ροή 1 (επάνοδος δικτύου).
     * Οι εγγραφές στέλνονται με το αρχικό τους timestamp.
     */
    public void syncPendingEntries() {
        int count = 0;
        for (DiaryEntry entry : entries) {
            if (entry.getSyncStatus() == SyncStatus.PENDING) {
                entry.setSyncStatus(SyncStatus.SYNCED);
                count++;
                System.out.println("[SYNC] Συγχρονίστηκε εγγραφή: "
                        + entry.getDisplayLabel() + " @ " + entry.getFormattedTime());
            }
        }
        if (count > 0) {
            System.out.println("[SYNC] ✅ " + count + " εκκρεμείς εγγραφές συγχρονίστηκαν.");
        }
    }

    public List<DiaryEntry> getTimeline() { return entries; }

    public long getPendingCount() {
        return entries.stream().filter(e -> e.getSyncStatus() == SyncStatus.PENDING).count();
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// CONTROLLER / SERVICE CLASSES  (από το Class Diagram)
// ══════════════════════════════════════════════════════════════════════════════

// ── EntryController ────────────────────────────────────────────────────────

/**
 * Ελέγχει τη δημιουργία / επικύρωση εγγραφών.
 * Αντιστοιχεί στο Class Diagram (createEntry, validateEntry).
 */
class EntryController {
    private LiveDiary            diary;
    private NotificationController notifController;
    private String               ownerDeviceToken;

    public EntryController(LiveDiary diary,
                           NotificationController notifController,
                           String ownerDeviceToken) {
        this.diary            = diary;
        this.notifController  = notifController;
        this.ownerDeviceToken = ownerDeviceToken;
    }

    /**
     * Δημιουργία νέας καταχώρησης — Βασική Ροή βήμα 4-5.
     * Επικυρώνει πρώτα τα δεδομένα.
     */
    public DiaryEntry createEntry(ActivityType type, String details,
                                  int durationMinutes, String photoPath) {
        if (!validateEntry(type, details)) {
            System.out.println("[ENTRY CONTROLLER] ❌ Μη έγκυρη καταχώρηση.");
            return null;
        }
        return diary.addEntry(type, details, durationMinutes, photoPath,
                notifController, ownerDeviceToken);
    }

    /** Επικύρωση εισόδου — τύπος και λεπτομέρειες απαιτούνται */
    public boolean validateEntry(ActivityType type, String details) {
        if (type == null) {
            System.out.println("[VALIDATION] ❌ Ο τύπος δραστηριότητας είναι υποχρεωτικός.");
            return false;
        }
        if (details == null || details.trim().isEmpty()) {
            System.out.println("[VALIDATION] ❌ Οι λεπτομέρειες δεν μπορούν να είναι κενές.");
            return false;
        }
        return true;
    }
}

// ── NotificationController ─────────────────────────────────────────────────

/**
 * Διαχειρίζεται αποστολή push notifications.
 * Βασική Ροή βήμα 6 (νέα ενημέρωση → ιδιοκτήτης)
 * και βήμα 8 (reaction → host).
 */
class NotificationController {

    /** Στέλνει push notification στον Ιδιοκτήτη — Βασική Ροή βήμα 6 */
    public void sendPushNotification(String title, String body, String deviceToken) {
        Notification notification = new Notification(title, body, deviceToken);
        notification.dispatch();
    }

    /** Ειδοποιεί τον Host για reaction του Ιδιοκτήτη — Βασική Ροή βήμα 8 */
    public void notifyHostReaction(String hostId, ReactionType reactionType, DiaryEntry entry) {
        System.out.println("[NOTIF → HOST " + hostId + "] "
                + "Ο ιδιοκτήτης αντέδρασε με "
                + new Reaction(reactionType, LocalDateTime.now()).getEmoji()
                + " στη δραστηριότητα: " + entry.getDisplayLabel());
    }
}

// ── CommunicationManager ───────────────────────────────────────────────────

/**
 * Δρομολογεί βιντεοκλήσεις και διαχειρίζεται chat μηνύματα.
 * Αντιστοιχεί στο Sequence Diagram (routeCall, establishConnection, logCallDuration).
 * Εναλλακτική Ροή 3: απόρριψη + αυτόματο μήνυμα.
 */
class CommunicationManager {

    /**
     * Δρομολόγηση κλήσης — Βασική Ροή βήμα 9.
     * Επιστρέφει αντικείμενο VideoCall.
     */
    public VideoCall routeCall(String callerId, String receiverId) {
        VideoCall call = new VideoCall(callerId, receiverId);
        System.out.println("[COMM] 📞 Κλήση από " + callerId + " → " + receiverId + " (Σε αναμονή...)");
        return call;
    }

    /**
     * Εγκαθίδρυση σύνδεσης μετά από αποδοχή — Βασική Ροή βήμα 9.
     */
    public void establishConnection(VideoCall call) {
        call.accept();
        System.out.println("[COMM] ✅ Σύνδεση εγκαθιδρύθηκε.");
    }

    /**
     * Τερματισμός κλήσης + καταγραφή διάρκειας στο Timeline — Βασική Ροή βήμα 10.
     */
    public DiaryEntry endCallAndLog(VideoCall call, int durationSeconds,
                                    EntryController entryController) {
        call.end(durationSeconds);
        String summary = call.getCallSummary();
        // Καταγραφή στο Timeline
        return entryController.createEntry(ActivityType.CALL_LOG, summary, 0, null);
    }

    /**
     * Αποστολή μηνύματος chat.
     * Βασική Ροή + Εναλλακτική Ροή 3 (αυτόματο/custom μήνυμα).
     */
    public Message sendMessage(Booking booking, String senderId,
                               String content, boolean isAutoReply) {
        Message msg = new Message(senderId, content, LocalDateTime.now(), isAutoReply);
        booking.addMessage(msg);
        System.out.println("[CHAT " + (isAutoReply ? "AUTO" : "USER") + " → "
                + booking.getOwner().getOwnerId() + "] " + content);
        return msg;
    }

    /**
     * Απόρριψη κλήσης + αποστολή έτοιμου/custom μηνύματος — Εναλλακτική Ροή 3.
     */
    public void rejectCallWithMessage(VideoCall call, Booking booking,
                                      String senderId, String busyMessage,
                                      boolean isAutoReply) {
        call.reject();
        sendMessage(booking, senderId, busyMessage, isAutoReply);
        System.out.println("[COMM] ❌ Κλήση απορρίφθηκε. Εστάλη μήνυμα: \"" + busyMessage + "\"");
    }
}