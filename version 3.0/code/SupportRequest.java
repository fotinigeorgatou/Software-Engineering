import java.util.ArrayList;
import java.util.List;

public class SupportRequest {
    private int request_id;
    private String subject;
    private String support_description;
    private String support_category;
    private String support_status;
    private String priority;
    private List<String> attachments;
    private String request_type;
    private static int nextId = 1;

    public SupportRequest(String subject, String description, String category) {
        this.request_id = nextId++;
        this.subject = subject;
        this.support_description = description;
        this.support_category = category;
        this.support_status = "pending";
        this.priority = "normal";
        this.attachments = new ArrayList<>();
        this.request_type = "support";
    }

    public boolean validDescription() {
        return support_description != null && !support_description.trim().isEmpty()
                && support_description.length() >= 10;
    }

    public SupportRequest createRequest(String subject, String desc, String category) {
        return new SupportRequest(subject, desc, category);
    }

    public void addAttachment(String filename) { attachments.add(filename); }

    public int getRequest_id() { return request_id; }
    public String getSubject() { return subject; }
    public String getSupport_description() { return support_description; }
    public String getSupport_category() { return support_category; }
    public String getSupport_status() { return support_status; }
    public void setSupport_status(String s) { this.support_status = s; }
    public List<String> getAttachments() { return attachments; }
}
