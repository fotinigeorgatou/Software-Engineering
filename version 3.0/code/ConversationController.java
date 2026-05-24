import java.util.List;

public class ConversationController {
    private Conversation currentConversation;

    public Conversation findOrCreateConversation(String user1, String user2) {
        currentConversation = Conversation.findOrCreateConversation(user1, user2);
        return currentConversation;
    }

    public void openConversation(Conversation conv) { conv.openConversation(); }

    public List<Message> getMessage(Conversation conv) { return conv.getMessage(); }

    public Conversation getCurrentConversation() { return currentConversation; }
}
