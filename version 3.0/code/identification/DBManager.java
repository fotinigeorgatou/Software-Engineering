import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static List<verificationRequest> database = new ArrayList<>();

    public static boolean saveVerificationRequest(verificationRequest request) {
        database.add(request);
        return true;
    }

    public static String checkVerificationStatus(String requestId) {
        for (verificationRequest request : database) {
            if (request.getRequestId().equals(requestId)) {
                return request.getStatus().getStatus();
            }
        }

        return "Not Found";
    }

    public static boolean updateVerificationStatus(String requestId, String status, String reason) {
        for (verificationRequest request : database) {
            if (request.getRequestId().equals(requestId)) {

                if (status.equalsIgnoreCase("Approved")) {
                    request.approveRequest();
                } else if (status.equalsIgnoreCase("Rejected")) {
                    request.rejectRequest(reason);
                }

                return true;
            }
        }

        return false;
    }

    public static verificationRequest findVerificationRequest(String requestId) {
        for (verificationRequest request : database) {
            if (request.getRequestId().equals(requestId)) {
                return request;
            }
        }

        return null;
    }
}