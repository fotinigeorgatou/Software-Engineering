import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private int conversation_id;
    private String last_message;
    private String conversation_status;
    private List<Message> messages;
    private static int nextId = 1;

    public Conversation(int id) {
        this.conversation_id = id;
        this.conversation_status = "active";
        this.messages = new ArrayList<>();
        this.last_message = "";
    }

    public static Conversation findOrCreateConversation(String user1, String user2) {
        return new Conversation(nextId++);
    }

    public void openConversation() { this.conversation_status = "open"; }

    public List<Message> getMessage() { return messages; }

    public void addMessage(Message msg) {
        messages.add(msg);
        last_message = msg.getMessage_content();
    }

    public void removeMessage(Message msg) { msg.removeMessage(); }

    public int getConversation_id() { return conversation_id; }
    public String getLast_message() { return last_message; }
    public String getConversation_status() { return conversation_status; }
}
