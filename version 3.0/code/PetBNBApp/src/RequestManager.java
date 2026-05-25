public class RequestManager {
    public String requestResults(boolean accepted) {
        return accepted ? "APPROVED" : "DECLINED";
    }
}