import java.time.LocalDate;

public class Accommodation {

    private String accommodationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean completed;

    private String title;
    private String type;
    private String homeTo;
    private String roommates;
    private String offers;
    private String imagePath;
    private double rating;

    public Accommodation(String accommodationId,
                         LocalDate startDate,
                         LocalDate endDate,
                         Boolean completed,
                         String title,
                         String type,
                         String homeTo,
                         String roommates,
                         String offers,
                         String imagePath,
                         double rating) {

        this.accommodationId = accommodationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;

        this.title = title;
        this.type = type;
        this.homeTo = homeTo;
        this.roommates = roommates;
        this.offers = offers;
        this.imagePath = imagePath;
        this.rating = rating;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public String getStayDetails() {
        return "Stay from " + startDate + " to " + endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getHomeTo() {
        return homeTo;
    }

    public String getRoommates() {
        return roommates;
    }

    public String getOffers() {
        return offers;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getRating() {
        return rating;
    }
}