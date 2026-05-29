import java.io.*;
import java.util.ArrayList;

public class DBManager {

    private static final String REVIEWS_FILE = "reviews.txt";

    public Boolean saveReview(Review review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE, true))) {
            writer.write(reviewToFileLine(review));
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving review: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> loadReviews() {
        ArrayList<String> reviews = new ArrayList<>();
        File file = new File(REVIEWS_FILE);

        if (!file.exists()) {
            return reviews;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                reviews.add(line);
            }

        } catch (IOException e) {
            System.out.println("Error loading reviews: " + e.getMessage());
        }

        return reviews;
    }

    public Boolean reviewExists(String reviewerId, String accommodationId) {
        return findActiveReviewId(reviewerId, accommodationId) != null;
    }

    public String findActiveReviewId(String reviewerId, String accommodationId) {
        String reviewLine = findActiveReviewLine(reviewerId, accommodationId);

        if (reviewLine == null) {
            return null;
        }

        String[] parts = reviewLine.split(";", -1);
        return parts[0];
    }

    public String findActiveReviewLine(String reviewerId, String accommodationId) {
        ArrayList<String> reviews = loadReviews();

        for (String line : reviews) {
            String[] parts = line.split(";", -1);

            if (parts.length >= 8) {
                String fileReviewerId = parts[1];
                String fileAccommodationId = parts[3];
                String fileStatus = parts[7];

                if (fileReviewerId.equals(reviewerId)
                        && fileAccommodationId.equals(accommodationId)
                        && fileStatus.equals("ACTIVE")) {
                    return line;
                }
            }
        }

        return null;
    }

    public Boolean updateReview(Review updatedReview) {
        ArrayList<String> reviews = loadReviews();
        ArrayList<String> updatedReviews = new ArrayList<>();

        boolean found = false;

        for (String line : reviews) {
            String[] parts = line.split(";", -1);

            if (parts.length >= 8 && parts[0].equals(updatedReview.getReviewId())) {
                updatedReviews.add(reviewToFileLine(updatedReview));
                found = true;
            } else {
                updatedReviews.add(line);
            }
        }

        if (!found) {
            return false;
        }

        return rewriteFile(updatedReviews);
    }

    public Boolean deleteReview(String reviewId) {
        ArrayList<String> reviews = loadReviews();
        ArrayList<String> updatedReviews = new ArrayList<>();

        boolean found = false;

        for (String line : reviews) {
            String[] parts = line.split(";", -1);

            if (parts.length >= 8 && parts[0].equals(reviewId)) {
                parts[7] = "DELETED";
                updatedReviews.add(String.join(";", parts));
                found = true;
            } else {
                updatedReviews.add(line);
            }
        }

        if (!found) {
            return false;
        }

        return rewriteFile(updatedReviews);
    }

    private Boolean rewriteFile(ArrayList<String> reviews) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE))) {

            for (String reviewLine : reviews) {
                writer.write(reviewLine);
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error rewriting reviews file: " + e.getMessage());
            return false;
        }
    }

    private String reviewToFileLine(Review review) {
        return review.getReviewId() + ";"
                + review.getReviewer().getUserId() + ";"
                + review.getReviewee().getUserId() + ";"
                + review.getAccommodationId() + ";"
                + review.getRating() + ";"
                + cleanText(review.getComment()) + ";"
                + review.getReviewDate() + ";"
                + review.getStatus();
    }

    private String cleanText(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace(";", ",")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }
}