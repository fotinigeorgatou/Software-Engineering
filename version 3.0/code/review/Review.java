import java.time.LocalDate;

public class Review {

    private String reviewId;
    private User reviewer;
    private User reviewee;
    private String accommodationId;
    private int rating;
    private String comment;
    private LocalDate reviewDate;
    private ReviewStatus status;

    public Review(String reviewId,
                  User reviewer,
                  User reviewee,
                  String accommodationId,
                  int rating,
                  String comment,
                  LocalDate reviewDate) {

        validateRating(rating);

        this.reviewId = reviewId;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.accommodationId = accommodationId;
        this.rating = rating;
        this.comment = cleanComment(comment);
        this.reviewDate = reviewDate;
        this.status = ReviewStatus.ACTIVE;
    }

    public String getReviewId() {
        return reviewId;
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void updateReview(int rating, String comment) {
        validateRating(rating);
        this.rating = rating;
        this.comment = cleanComment(comment);
    }

    public void deleteReview() {
        this.status = ReviewStatus.DELETED;
    }

    public Boolean validateRating() {
        return rating >= 1 && rating <= 5;
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
    }

    private String cleanComment(String comment) {
        if (comment == null) {
            return "";
        }

        return comment
                .replace(";", ",")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }
}