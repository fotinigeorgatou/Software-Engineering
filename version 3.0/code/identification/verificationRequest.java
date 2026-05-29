public class verificationRequest {

    private String requestId;
    private user user;
    private identityPhotoSet photoSet;
    private identificationData identificationData;
    private verificationStatus status;
    private String rejectionReason;

    public verificationRequest(String requestId, user user) {
        this.requestId = requestId;
        this.user = user;
    }

    public void attachIdentityPhotoSet(identityPhotoSet photoSet) {
        this.photoSet = photoSet;
    }

    public void attachIdentificationData(identificationData data) {
        this.identificationData = data;
    }

    public void submitRequest() {
        status = new verificationStatus("VS1");
        status.setPendingStatus();
    }

    public void approveRequest() {
        if (status == null) {
            status = new verificationStatus("VS1");
        }

        status.setApprovedStatus();
        user.setVerified(true);
    }

    public void rejectRequest(String reason) {
        if (status == null) {
            status = new verificationStatus("VS1");
        }

        rejectionReason = reason;
        status.setRejectedStatus();
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public verificationStatus getStatus() {
        return status;
    }

    public user getUser() {
        return user;
    }

    public identityPhotoSet getPhotoSet() {
        return photoSet;
    }

    public identificationData getIdentificationData() {
        return identificationData;
    }

    public String getRequestId() {
        return requestId;
    }
}