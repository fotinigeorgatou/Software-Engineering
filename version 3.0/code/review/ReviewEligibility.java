import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReviewEligibility {

    private String eligibilityId;
    private LocalDate expiresAt;
    private Boolean isEligible;

    public ReviewEligibility(String eligibilityId, LocalDate expiresAt) {
        this.eligibilityId = eligibilityId;
        this.expiresAt = expiresAt;
        this.isEligible = false;
    }

    public String getEligibilityId() {
        return eligibilityId;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public Boolean getIsEligible() {
        return isEligible;
    }

    public Boolean checkEligibility(User reviewer,
                                    Accommodation accommodation,
                                    DBManager dbManager) {

        if (!accommodation.isCompleted()) {
            return false;
        }

        if (!validateFiveDayWindow(accommodation)) {
            return false;
        }

        if (!validateSingleReviewPerStay(
                reviewer.getUserId(),
                accommodation.getAccommodationId(),
                dbManager)) {
            return false;
        }

        isEligible = true;
        return true;
    }

    public Boolean checkEditEligibility(Accommodation accommodation) {
        if (!accommodation.isCompleted()) {
            return false;
        }

        return validateFiveDayWindow(accommodation);
    }

    public Boolean validateFiveDayWindow(Accommodation accommodation) {
        long daysPassed = ChronoUnit.DAYS.between(
                accommodation.getEndDate(),
                LocalDate.now()
        );

        return daysPassed <= 5;
    }

    public Boolean validateSingleReviewPerStay(String reviewerId,
                                               String accommodationId,
                                               DBManager dbManager) {

        return !dbManager.reviewExists(reviewerId, accommodationId);
    }
}