import java.util.List;

public class HelpController {
    public String[] getHelpOptions() {
        return new String[]{"Επικοινωνία με Υποστήριξη", "Συχνές Ερωτήσεις (FAQ)", "Chatbot"};
    }

    public List<FAQ> getFAQTopics() {
        return FAQ.getAllFAQs();
    }
}
