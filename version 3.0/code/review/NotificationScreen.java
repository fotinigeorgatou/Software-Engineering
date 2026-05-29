import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NotificationScreen extends JFrame {

    private String notificationId;
    private String message;

    private User currentUser;
    private User reviewee;
    private Accommodation accommodation;
    private DBManager dbManager;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);

    public NotificationScreen(String notificationId,
                              User currentUser,
                              User reviewee,
                              Accommodation accommodation,
                              DBManager dbManager) {

        this.notificationId = notificationId;
        this.currentUser = currentUser;
        this.reviewee = reviewee;
        this.accommodation = accommodation;
        this.dbManager = dbManager;

        this.message = createNotificationMessage();

        setTitle("petbnb - Ειδοποίηση Αξιολόγησης");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        displayNotification();
    }

    public void displayNotification() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = new JLabel("≡");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 32));

        JLabel titleLabel = new JLabel("Ειδοποιήσεις", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel settingsBtn = new JLabel("⚙");
        settingsBtn.setFont(new Font("SansSerif", Font.PLAIN, 28));
        settingsBtn.setForeground(Color.WHITE);

        topBar.add(menuBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        topBar.add(settingsBtn, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel notificationCard = new JPanel();
        notificationCard.setBackground(CARD_BG);
        notificationCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        notificationCard.setLayout(new BoxLayout(notificationCard, BoxLayout.Y_AXIS));
        notificationCard.setMaximumSize(new Dimension(420, 260));

        JLabel notificationTitle = new JLabel("Νέα αξιολόγηση");
        notificationTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        notificationTitle.setForeground(TEXT_PINK);
        notificationTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea notificationText = new JTextArea(message);
        notificationText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        notificationText.setLineWrap(true);
        notificationText.setWrapStyleWord(true);
        notificationText.setEditable(false);
        notificationText.setOpaque(false);
        notificationText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton reviewButton = new JButton("Άνοιγμα αξιολόγησης");
        reviewButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        reviewButton.setBackground(PINK_HEADER);
        reviewButton.setForeground(Color.WHITE);
        reviewButton.setFocusPainted(false);
        reviewButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        reviewButton.addActionListener(e -> selectReviewNotification());

        notificationCard.add(notificationTitle);
        notificationCard.add(Box.createVerticalStrut(15));
        notificationCard.add(notificationText);
        notificationCard.add(Box.createVerticalStrut(25));
        notificationCard.add(reviewButton);

        contentPanel.add(notificationCard);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void selectReviewNotification() {
        ReviewEligibility eligibility = new ReviewEligibility(
                "EL-" + accommodation.getAccommodationId(),
                accommodation.getEndDate().plusDays(5)
        );

        boolean existingReview = dbManager.reviewExists(
                currentUser.getUserId(),
                accommodation.getAccommodationId()
        );

        if (!existingReview) {
            Boolean allowed = eligibility.checkEligibility(
                    currentUser,
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
        }

        UserProfileScreen profileScreen = new UserProfileScreen(
                currentUser,
                reviewee,
                accommodation,
                dbManager
        );

        profileScreen.setVisible(true);
        dispose();
    }

    private String createNotificationMessage() {
        if (currentUser instanceof PetOwner && reviewee instanceof Host) {
            return "Η φιλοξενία σας στο κατάλυμα \""
                    + accommodation.getTitle()
                    + "\" ολοκληρώθηκε. Μπορείτε πλέον να αξιολογήσετε τον οικοδεσπότη "
                    + reviewee.getName()
                    + ".";
        }

        if (currentUser instanceof Host && reviewee instanceof PetOwner) {
            return "Η φιλοξενία ολοκληρώθηκε. Μπορείτε πλέον να αξιολογήσετε τον ιδιοκτήτη κατοικιδίου "
                    + reviewee.getName()
                    + ".";
        }

        return "Μπορείτε πλέον να υποβάλετε αξιολόγηση για την ολοκληρωμένη φιλοξενία.";
    }
}