import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class UserProfileScreen extends JFrame {

    private User currentUser;
    private User reviewee;
    private Accommodation accommodation;
    private DBManager dbManager;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color DESC_BOX_GRAY = new Color(225, 225, 225);
    private static final Color LINK_GRAY = new Color(160, 160, 160);

    public UserProfileScreen(User currentUser,
                             User reviewee,
                             Accommodation accommodation,
                             DBManager dbManager) {

        this.currentUser = currentUser;
        this.reviewee = reviewee;
        this.accommodation = accommodation;
        this.dbManager = dbManager;

        setTitle("petbnb - Προφίλ Χρήστη");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        displayRevieweeProfile(reviewee);
    }

    public void displayRevieweeProfile(User user) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel profileTitle = new JLabel("Προφίλ: " + user.getName());
        profileTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        profileTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(profileTitle);
        contentPanel.add(Box.createVerticalStrut(20));

        if (user instanceof Host) {

            contentPanel.add(createHostHomeCard(
                    accommodation.getTitle(),
                    accommodation.getType(),
                    accommodation.getHomeTo(),
                    accommodation.getRoommates(),
                    String.valueOf(accommodation.getRating()),
                    accommodation.getOffers(),
                    accommodation.getImagePath()
            ));

        } else if (user instanceof PetOwner) {

            Pet pet = ((PetOwner) user).getPet();

            contentPanel.add(createPetIDCard(
                    pet.getBreed() + " ID",
                    pet.getName(),
                    pet.getAge(),
                    pet.getBreed(),
                    pet.getDescription(),
                    pet.getImagePath()
            ));
        }

        contentPanel.add(Box.createVerticalStrut(20));

        String existingReviewLine = dbManager.findActiveReviewLine(
                currentUser.getUserId(),
                accommodation.getAccommodationId()
        );

        if (existingReviewLine == null) {
            JButton reviewButton = createPinkButton("Αξιολόγηση");
            reviewButton.addActionListener(e -> openReviewForm());
            contentPanel.add(reviewButton);
        } else {
            displayReview(contentPanel, existingReviewLine);
            contentPanel.add(Box.createVerticalStrut(15));

            JButton actionsButton = createPinkButton("Ενέργειες αξιολόγησης");
            actionsButton.addActionListener(e -> openReviewActionsMenu(actionsButton, existingReviewLine));
            contentPanel.add(actionsButton);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    public void displayReview(JPanel contentPanel, String reviewLine) {
        String[] parts = reviewLine.split(";", -1);

        String rating = parts[4];
        String comment = parts[5];

        JPanel reviewCard = new JPanel();
        reviewCard.setBackground(CARD_BG);
        reviewCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        reviewCard.setLayout(new BoxLayout(reviewCard, BoxLayout.Y_AXIS));
        reviewCard.setMaximumSize(new Dimension(420, 180));
        reviewCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Η αξιολόγησή σας");
        title.setForeground(TEXT_PINK);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel ratingLabel = new JLabel("Βαθμολογία: " + rating + "/5 ⭐");
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextArea commentArea = new JTextArea(comment);
        commentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setEditable(false);
        commentArea.setOpaque(false);

        reviewCard.add(title);
        reviewCard.add(Box.createVerticalStrut(10));
        reviewCard.add(ratingLabel);
        reviewCard.add(Box.createVerticalStrut(10));
        reviewCard.add(commentArea);

        contentPanel.add(reviewCard);
    }

    public void openReviewActionsMenu(Component parent, String existingReviewLine) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Επεξεργασία");
        JMenuItem deleteItem = new JMenuItem("Διαγραφή");
        JMenuItem cancelItem = new JMenuItem("Ακύρωση");

        editItem.addActionListener(e -> editReview(existingReviewLine));
        deleteItem.addActionListener(e -> deleteReview());

        menu.add(editItem);
        menu.add(deleteItem);
        menu.add(cancelItem);

        menu.show(parent, 0, parent.getHeight());
    }

    private void openReviewForm() {
        ReviewFormScreen form = new ReviewFormScreen(
                currentUser,
                reviewee,
                accommodation,
                dbManager
        );

        form.setVisible(true);
        dispose();
    }

    private void editReview(String existingReviewLine) {
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

        ReviewFormScreen form = new ReviewFormScreen(
                currentUser,
                reviewee,
                accommodation,
                dbManager,
                existingReviewLine
        );

        form.setVisible(true);
        dispose();
    }

    private void deleteReview() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Είστε σίγουροι ότι θέλετε να διαγράψετε την αξιολόγηση;",
                "Επιβεβαίωση διαγραφής",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            String reviewId = dbManager.findActiveReviewId(
                    currentUser.getUserId(),
                    accommodation.getAccommodationId()
            );

            Boolean deleted = dbManager.deleteReview(reviewId);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Η αξιολόγηση διαγράφηκε επιτυχώς.",
                        "Επιτυχής διαγραφή",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshScreen();
            }
        }
    }

    public void displaySuccess(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Επιτυχία",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void refreshScreen() {
        displayRevieweeProfile(reviewee);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = new JLabel("≡");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 32));

        JLabel titleLabel = new JLabel("User Profile", SwingConstants.CENTER);
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

   private ImageIcon loadImageIcon(String imagePath, int width, int height) {

    File file = new File(imagePath);

    if (!file.exists()) {
        return null;
    }

    ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());

    if (originalIcon.getIconWidth() <= 0 ||
        originalIcon.getIconHeight() <= 0) {
        return null;
    }

    Image scaledImg = originalIcon.getImage().getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
    );

    return new ImageIcon(scaledImg);
}

    private JPanel createHostHomeCard(String labelHome,
                                      String type,
                                      String homeTo,
                                      String roommates,
                                      String rating,
                                      String offers,
                                      String imagePath) {

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 35, 35));
                g2.dispose();
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 310));
        card.setMinimumSize(new Dimension(420, 310));
        card.setMaximumSize(new Dimension(420, 310));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHomeType = new JLabel(labelHome);
        lblHomeType.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblHomeType.setBounds(40, 15, 120, 25);
        card.add(lblHomeType);

        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK_HEADER);
                int arc = getHeight();
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                g2.dispose();
            }
        };

        badge.setLayout(new BorderLayout());
        badge.setOpaque(false);
        badge.setBounds(135, 12, 255, 35);

        JLabel lblBadge = new JLabel("my petbnb home", SwingConstants.CENTER);
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setFont(new Font("SansSerif", Font.PLAIN, 16));
        badge.add(lblBadge, BorderLayout.CENTER);
        card.add(badge);

        JLabel lblHomePhoto = new JLabel();
        lblHomePhoto.setBounds(25, 55, 95, 110);
        lblHomePhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        ImageIcon icon = loadImageIcon(imagePath, 95, 110);

        if (icon == null) {
            lblHomePhoto.setText("No Image");
            lblHomePhoto.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            lblHomePhoto.setIcon(icon);
        }

        card.add(lblHomePhoto);

        int col1X = 140;
        int col2X = 260;

        JLabel lblTypeTag = new JLabel("Type:");
        lblTypeTag.setForeground(TEXT_PINK);
        lblTypeTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTypeTag.setBounds(col1X, 55, 100, 18);
        card.add(lblTypeTag);

        JLabel lblTypeVal = new JLabel(type);
        lblTypeVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTypeVal.setBounds(col1X, 73, 110, 18);
        card.add(lblTypeVal);

        JLabel lblHomeToTag = new JLabel("Home to:");
        lblHomeToTag.setForeground(TEXT_PINK);
        lblHomeToTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblHomeToTag.setBounds(col1X, 93, 100, 18);
        card.add(lblHomeToTag);

        JLabel lblHomeToVal = new JLabel(homeTo);
        lblHomeToVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblHomeToVal.setBounds(col1X, 111, 110, 18);
        card.add(lblHomeToVal);

        JLabel lblRoommatesTag = new JLabel("Roommates:");
        lblRoommatesTag.setForeground(TEXT_PINK);
        lblRoommatesTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRoommatesTag.setBounds(col1X, 131, 110, 18);
        card.add(lblRoommatesTag);

        JLabel lblRoommatesVal = new JLabel(roommates);
        lblRoommatesVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRoommatesVal.setBounds(col1X, 149, 110, 18);
        card.add(lblRoommatesVal);

        JLabel lblRatingTag = new JLabel("Rating:");
        lblRatingTag.setForeground(TEXT_PINK);
        lblRatingTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRatingTag.setBounds(col2X, 55, 100, 18);
        card.add(lblRatingTag);

        JLabel lblRatingVal = new JLabel(rating + "⭐");
        lblRatingVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRatingVal.setBounds(col2X, 73, 130, 18);
        card.add(lblRatingVal);

        JLabel lblOffersTag = new JLabel("Offers:");
        lblOffersTag.setForeground(TEXT_PINK);
        lblOffersTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblOffersTag.setBounds(col2X, 93, 100, 18);
        card.add(lblOffersTag);

        JTextArea txtOffersVal = new JTextArea(offers);
        txtOffersVal.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtOffersVal.setLineWrap(true);
        txtOffersVal.setWrapStyleWord(true);
        txtOffersVal.setEditable(false);
        txtOffersVal.setOpaque(false);
        txtOffersVal.setBounds(col2X, 111, 140, 55);
        card.add(txtOffersVal);

        JLabel lblHeart = new JLabel("❤");
        lblHeart.setForeground(PINK_HEADER);
        lblHeart.setFont(new Font("SansSerif", Font.PLAIN, 42));
        lblHeart.setBounds(40, 195, 50, 50);
        card.add(lblHeart);

        JLabel lblPaw = new JLabel("🐾");
        lblPaw.setForeground(PINK_HEADER);
        lblPaw.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblPaw.setBounds(75, 230, 20, 20);
        card.add(lblPaw);

        JLabel lblVerified = new JLabel("This home has been verified by the official petbnb government.");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVerified.setBounds(100, 205, 300, 40);
        card.add(lblVerified);

        JLabel lblLink = new JLabel("View full profile", SwingConstants.RIGHT);
        lblLink.setForeground(LINK_GRAY);
        lblLink.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.setBounds(250, 265, 140, 20);
        card.add(lblLink);

        return card;
    }

    private JPanel createPetIDCard(String typeId,
                                   String name,
                                   String age,
                                   String breed,
                                   String description,
                                   String imagePath) {

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 35, 35));
                g2.dispose();
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 310));
        card.setMinimumSize(new Dimension(420, 310));
        card.setMaximumSize(new Dimension(420, 310));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTypeId = new JLabel(typeId);
        lblTypeId.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTypeId.setBounds(40, 15, 100, 25);
        card.add(lblTypeId);

        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK_HEADER);
                int arc = getHeight();
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                g2.dispose();
            }
        };

        badge.setLayout(new BorderLayout());
        badge.setOpaque(false);
        badge.setBounds(130, 12, 260, 35);

        JLabel lblBadge = new JLabel("my petbnb ID", SwingConstants.CENTER);
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setFont(new Font("SansSerif", Font.PLAIN, 16));
        badge.add(lblBadge, BorderLayout.CENTER);
        card.add(badge);

        JLabel lblPetPhoto = new JLabel();
        lblPetPhoto.setBounds(25, 55, 90, 110);
        lblPetPhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));


        ImageIcon icon = loadImageIcon(imagePath, 90, 110);

        if (icon == null) {
            lblPetPhoto.setText("No Image");
            lblPetPhoto.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            lblPetPhoto.setIcon(icon);
        }
        card.add(lblPetPhoto);

        int textX = 140;

        JLabel lblNameTag = new JLabel("Name:");
        lblNameTag.setForeground(TEXT_PINK);
        lblNameTag.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblNameTag.setBounds(textX, 60, 80, 20);
        card.add(lblNameTag);

        JLabel lblNameVal = new JLabel(name);
        lblNameVal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNameVal.setBounds(textX, 80, 100, 20);
        card.add(lblNameVal);

        JLabel lblAgeTag = new JLabel("Age:");
        lblAgeTag.setForeground(TEXT_PINK);
        lblAgeTag.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblAgeTag.setBounds(textX, 105, 80, 20);
        card.add(lblAgeTag);

        JLabel lblAgeVal = new JLabel(age);
        lblAgeVal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblAgeVal.setBounds(textX, 125, 100, 20);
        card.add(lblAgeVal);

        JLabel lblBreedTag = new JLabel("Breed:");
        lblBreedTag.setForeground(TEXT_PINK);
        lblBreedTag.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblBreedTag.setBounds(textX, 150, 80, 20);
        card.add(lblBreedTag);

        JLabel lblBreedVal = new JLabel(breed);
        lblBreedVal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblBreedVal.setBounds(textX, 170, 100, 20);
        card.add(lblBreedVal);

        JPanel descBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(DESC_BOX_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };

        descBox.setOpaque(false);
        descBox.setBounds(225, 65, 165, 120);
        descBox.setLayout(new BorderLayout());

        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(DESC_BOX_GRAY);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDesc.setBorder(new EmptyBorder(10, 10, 10, 10));

        descBox.add(txtDesc, BorderLayout.CENTER);
        card.add(descBox);

        JLabel lblHeart = new JLabel("❤");
        lblHeart.setForeground(PINK_HEADER);
        lblHeart.setFont(new Font("SansSerif", Font.PLAIN, 42));
        lblHeart.setBounds(40, 195, 50, 50);
        card.add(lblHeart);

        JLabel lblPaw = new JLabel("🐾");
        lblPaw.setForeground(PINK_HEADER);
        lblPaw.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblPaw.setBounds(75, 230, 20, 20);
        card.add(lblPaw);

        JLabel lblVerified = new JLabel("<html>This pet and its documents have been verified<br>by the official petbnb government</html>");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVerified.setBounds(100, 205, 300, 40);
        card.add(lblVerified);

        JLabel lblLink = new JLabel("View full profile", SwingConstants.RIGHT);
        lblLink.setForeground(LINK_GRAY);
        lblLink.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.setBounds(250, 265, 140, 20);
        card.add(lblLink);

        return card;
    }
}
