public class verificationStatus {

    private String statusId;
    private String status;

    public verificationStatus(
            String statusId) {

        this.statusId = statusId;

        status = "Pending";
    }

    public void setPendingStatus(){

        status = "Pending";
    }

    public void setRejectedStatus(){

        status = "Rejected";
    }

    public void setApprovedStatus(){

        status = "Approved";
    }

    public String getStatus(){

        return status;
    }
}