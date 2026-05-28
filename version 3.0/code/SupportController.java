public class SupportController {
    private Support support = new Support("general");

    public boolean validDescription(String desc) {
        return desc != null && !desc.trim().isEmpty() && desc.trim().length() >= 10;
    }

    public SupportRequest createRequest(String subject, String desc, String category) {
        return new SupportRequest(subject, desc, category);
    }

    public Response submitSupportRequest(SupportRequest request) {
        // Simulate occasional connection failure
        if (Math.random() < 0.08) return null;
        support.forwardRequest(request);
        return forwardRequest(request);
    }

    public Response forwardRequest(SupportRequest request) {
        return support.processRequest(request);
    }

    public void showConnectionError() {}
    public void showDelayedMessage() {}
}
