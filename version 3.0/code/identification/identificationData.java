public class identificationData {

    private String identificationDataId;
    private String documentType;
    private String documentNumber;

    public identificationData(String identificationDataId, String documentType, String documentNumber) {
        this.identificationDataId = identificationDataId;
        this.documentType = documentType;
        this.documentNumber = documentNumber.trim().toUpperCase();
    }

    public String getIdentificationData() {
        return "Type: " + documentType + " Number: " + documentNumber;
    }

    public boolean validateIdentificationData() {
        return documentNumber.matches("[A-ZΑ-Ω]{2}\\s?\\d{4}");
    }

    public String getIdentificationDataId() {
        return identificationDataId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }
}