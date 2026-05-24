import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private int message_id;
    private String message_content;
    private String message_status;
    private String sent_at;
    private String senderName;

    public Message(int id, String content, String sender) {
        this.message_id = id;
        this.message_content = content;
        this.senderName = sender;
        this.message_status = "sent";
        this.sent_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public int getMessage_id() { return message_id; }
    public String getMessage_content() { return message_content; }
    public void setMessage_content(String content) { this.message_content = content; }
    public String getMessage_status() { return message_status; }
    public void setMessage_status(String status) { this.message_status = status; }
    public String getSent_at() { return sent_at; }
    public String getSenderName() { return senderName; }

    public void updatedMessage(String newContent) {
        this.message_content = newContent + " (επεξεργασμένο)";
    }

    public void removeMessage() {
        this.message_content = "Το μήνυμα διαγράφηκε";
        this.message_status = "deleted";
    }
}
