import java.util.List;

public class MessageController {
    private static int nextMessageId = 1;

    public boolean validMessage(String content) {
        return content != null && !content.trim().isEmpty() && content.length() <= 500;
    }

    public Message createMessage(String content, String senderName, Conversation conversation) {
        Message msg = new Message(nextMessageId++, content, senderName);
        conversation.addMessage(msg);
        return msg;
    }

    public boolean sendMessage(String content, String senderName, Conversation conversation) {
        if (!validMessage(content)) return false;
        createMessage(content, senderName, conversation);
        return true;
    }

    public boolean updatedMessage(Message msg, String newContent) {
        if (!validMessage(newContent)) return false;
        msg.updatedMessage(newContent);
        return true;
    }

    public void deleteMessage(Message msg, Conversation conversation) {
        conversation.removeMessage(msg);
    }

    public List<Message> getMessages(Conversation conversation) {
        return conversation.getMessage();
    }
}
