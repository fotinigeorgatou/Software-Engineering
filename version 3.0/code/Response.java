import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Response {
    private int response_id;
    private String response_content;
    private String response_date;
    private String response_type;
    private String response_status;
    private static int nextId = 1;

    public Response(String content, String type) {
        this.response_id = nextId++;
        this.response_content = content;
        this.response_type = type;
        this.response_status = "sent";
        this.response_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static Response createResponse(String content, String type) {
        return new Response(content, type);
    }

    public void displayAnswer() { System.out.println("Response: " + response_content); }

    public String getResponse_content() { return response_content; }
    public String getResponse_date() { return response_date; }
    public String getResponse_type() { return response_type; }
    public String getResponse_status() { return response_status; }
}
