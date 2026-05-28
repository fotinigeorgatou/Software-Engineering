public class Support {
    private int support_id;
    private String support_type;
    private static int nextId = 1;

    public Support(String type) {
        this.support_id = nextId++;
        this.support_type = type;
    }

    public Response processRequest(SupportRequest request) {
        String answer = "Το αίτημά σας (ID: " + request.getRequest_id() + ") παρελήφθη. "
                + "Η ομάδα υποστήριξής μας θα επικοινωνήσει μαζί σας εντός 24 ωρών.";
        return Response.createResponse(answer, "support");
    }

    public void forwardRequest(SupportRequest request) {
        request.setSupport_status("forwarded");
    }

    public int getSupport_id() { return support_id; }
    public String getSupport_type() { return support_type; }
}
