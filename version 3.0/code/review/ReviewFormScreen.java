import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class ReviewFormScreen extends JFrame {

    private User reviewer;
    private User reviewee;
    private Accommodation accommodation;
    private DBManager dbManager;

    private String existingReviewLine;
    private boolean editMode;

    private JComboBox<Integer> ratingBox;
    private JTextArea commentArea;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);

    public ReviewFormScreen(User reviewer,
                            User reviewee,
                            Accommodation accommodation,
                            DBManager dbManager) {

        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.accommodation = accommodation;
        this.dbManager = dbManager;
        this.editMode = false;

        setTitle("petbnb - Φόρμα Αξιολόγησης");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        displayReviewForm();
    }

    public ReviewFormScreen(User reviewer,
                            User reviewee,
                            Accommodation accommodation,
                            DBManager dbManager,
                            String existingReviewLine) {

        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.accommodation = accommodation;
        this.dbManager = dbManager;
        this.existingReviewLine = existingReviewLine;
        this.editMode = true;

        setTitle("petbnb - Επεξεργασία Αξιολόγησης");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        displayReviewForm();
    }

    public void displayReviewForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        mainPanel.add(createTopBar(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel formCard = new JPanel();
        formCard.setBackground(CARD_BG);
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setMaximumSize(new Dimension(420, 430));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(editMode ? "Επεξεργασία αξιολόγησης" : "Νέα αξιολόγηση");
        title.setForeground(TEXT_PINK);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Αξιολογείτε: " + reviewee.getName());
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ratingLabel = new JLabel("Βαθμολογία:");
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingBox.setMaximumSize(new Dimension(100, 30));
        ratingBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel commentLabel = new JLabel("Σχόλιο:");
        commentLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        commentArea = new JTextArea();
        commentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setMaximumSize(new Dimension(360, 150));
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (editMode) {
            fillExistingReviewData();
        }

        JButton submitButton = createPinkButton("Ανάρτηση");
        submitButton.addActionListener(e -> submitReview());

        JButton cancelButton = createPinkButton("Ακύρωση");
        cancelButton.addActionListener(e -> backToProfile());

        formCard.add(title);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(subtitle);
        formCard.add(Box.createVerticalStrut(25));
        formCard.add(ratingLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(ratingBox);
        formCard.add(Box.createVerticalStrut(20));
        formCard.add(commentLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(commentScroll);
        formCard.add(Box.createVerticalStrut(25));
        formCard.add(submitButton);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(cancelButton);

        contentPanel.add(formCard);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void submitReview() {
        int rating = (Integer) ratingBox.getSelectedItem();
        String comment = commentArea.getText();

        if (comment == null || comment.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Παρακαλώ συμπληρώστε σχόλιο για την αξιολόγηση.",
                    "Ελλιπή στοιχεία",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            if (editMode) {
                updateExistingReview(rating, comment);
            } else {
                createNewReview(rating, comment);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Σφάλμα αξιολόγησης",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createNewReview(int rating, String comment) {
        ReviewEligibility eligibility = new ReviewEligibility(
                "EL-" + accommodation.getAccommodationId(),
                accommodation.getEndDate().plusDays(5)
        );

        Boolean allowed = eligibility.checkEligibility(
                reviewer,
                accommodation,
                dbManager
        );

        if (!allowed) {
            JOptionPane.showMessageDialog(
                    this,
                    "Δεν μπορείτε να υποβάλετε αξιολόγηση για αυτή τη φιλοξενία.",
                    "Μη διαθέσιμη αξιολόγηση",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Review review = new Review(
                generateReviewId(),
                reviewer,
                reviewee,
                accommodation.getAccommodationId(),
                rating,
                comment,
                LocalDate.now()
        );

        Boolean saved = dbManager.saveReview(review);

        if (saved) {
            JOptionPane.showMessageDialog(
                    this,
                    "Η αξιολόγηση αναρτήθηκε επιτυχώς.",
                    "Επιτυχής υποβολή",
                    JOptionPane.INFORMATION_MESSAGE
            );

            backToProfile();
        }
    }

    private void updateExistingReview(int rating, String comment) {
        ReviewEligibility eligibility = new ReviewEligibility(
                "EDIT-" + accommodation.getAccommodationId(),
                accommodation.getEndDate().plusDays(5)
        );

        if (!eligibility.checkEditEligibility(accommodation)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Η επεξεργασία επιτρέπεται μόνο εντός 5 ημερών από τη λήξη της φιλοξενίας.",
                    "Μη διαθέσιμη επεξεργασία",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] parts = existingReviewLine.split(";", -1);
        String reviewId = parts[0];

        Review updatedReview = new Review(
                reviewId,
                reviewer,
                reviewee,
                accommodation.getAccommodationId(),
                rating,
                comment,
                LocalDate.now()
        );

        Boolean updated = dbManager.updateReview(updatedReview);

        if (updated) {
            JOptionPane.showMessageDialog(
                    this,
                    "Η αξιολόγηση ενημερώθηκε επιτυχώς.",
                    "Επιτυχής επεξεργασία",
                    JOptionPane.INFORMATION_MESSAGE
            );

            backToProfile();
        }
    }

    private void fillExistingReviewData() {
        String[] parts = existingReviewLine.split(";", -1);

        if (parts.length >= 8) {
            int rating = Integer.parseInt(parts[4]);
            String comment = parts[5];

            ratingBox.setSelectedItem(rating);
            commentArea.setText(comment);
        }
    }

    private String generateReviewId() {
        return "R-" + System.currentTimeMillis();
    }

    private void backToProfile() {
        UserProfileScreen profileScreen = new UserProfileScreen(
                reviewer,
                reviewee,
                accommodation,
                dbManager
        );

        profileScreen.setVisible(true);
        dispose();
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = new JLabel("≡");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 32));

        JLabel titleLabel = new JLabel("Αξιολόγηση", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel settingsBtn = new JLabel("⚙");
        settingsBtn.setFont(new Font("SansSerif", Font.PLAIN, 28));
        settingsBtn.setForeground(Color.WHITE);

        topBar.add(menuBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        topBar.add(settingsBtn, BorderLayout.EAST);

        return topBar;
    }

    private JButton createPinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBackground(PINK_HEADER);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }
}