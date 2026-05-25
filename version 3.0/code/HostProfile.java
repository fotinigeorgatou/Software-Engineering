import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class HostProfile extends JFrame {

    private String userEmail;

    // --- Color Palette από το Screenshot ---
    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color LINK_GRAY = new Color(160, 160, 160);

    public HostProfile(String email) {
        this.userEmail = email;

        setTitle("petbnb - Host Profile");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        // --- 1. TOP BAR (ΡΟΖ ΕΠΙΚΕΦΑΛΙΔΑ) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = new JLabel("≡");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 32));
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel("Host Profile", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel settingsBtn = new JLabel("⚙");
        settingsBtn.setFont(new Font("SansSerif", Font.PLAIN, 28));
        settingsBtn.setForeground(Color.WHITE);
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        topBar.add(menuBtn, BorderLayout.WEST);
        topBar.add(titleLabel, BorderLayout.CENTER);
        topBar.add(settingsBtn, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // --- 2. CONTENT PANEL ΜΕ SCROLLBAR ΓΙΑ ΤΙΣ ΚΑΡΤΕΣ ΣΠΙΤΙΩΝ ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Προσθήκη Κάρτας 1: cat home
        contentPanel.add(createHostHomeCard(
                "cat home",
                "Apartment",
                "cats",
                "Two cats",
                "4.9/5",
                "Cat bed , toys , cat food",
                "apartment.png" // Αντικαταστήστε με το δικό σας αρχείο εικόνας
        ));

        contentPanel.add(Box.createVerticalStrut(20)); // Απόσταση μεταξύ των καρτών

        // Προσθήκη Κάρτας 2: animal home
        contentPanel.add(createHostHomeCard(
                "animal home",
                "House",
                "all animals",
                "one dog",
                "5/5",
                "bed dog, bed food, outside play space",
                "house.png" // Αντικαταστήστε με το δικό σας αρχείο εικόνας
        ));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // --- ΔΥΝΑΜΙΚΗ ΜΕΘΟΔΟΣ ΔΗΜΙΟΥΡΓΙΑΣ HOST HOME CARD ---
    private JPanel createHostHomeCard(String labelHome, String type, String homeTo, String roommates, String rating, String offers, String imagePath) {

        // Custom στρογγυλεμένο panel για την κάρτα σπιτιού
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
        card.setLayout(null); // Absolute layout για απόλυτη ακρίβεια με βάση το UI mockup
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 310));
        card.setMinimumSize(new Dimension(420, 310));
        card.setMaximumSize(new Dimension(420, 310));

        // 1. Ετικέτα καταλύματος (cat home / animal home) πάνω αριστερά
        JLabel lblHomeType = new JLabel(labelHome);
        lblHomeType.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblHomeType.setBounds(40, 15, 120, 25);
        card.add(lblHomeType);

        // 2. Μακρόστενο "my petbnb home" Badge πάνω δεξιά
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

        // 3. Φωτογραφία Καταλύματος (Αριστερά)
        JLabel lblHomePhoto = new JLabel();
        lblHomePhoto.setBounds(25, 55, 95, 110);
        lblHomePhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledImg = icon.getImage().getScaledInstance(95, 110, Image.SCALE_SMOOTH);
            lblHomePhoto.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            lblHomePhoto.setText("No Image");
            lblHomePhoto.setHorizontalAlignment(SwingConstants.CENTER);
        }
        card.add(lblHomePhoto);

        // --- ΔΙΣΤΗΛΟ ΠΛΕΓΜΑ ΠΛΗΡΟΦΟΡΙΩΝ (Στήλη 1: Αριστερά, Στήλη 2: Δεξιά) ---
        int col1X = 140;
        int col2X = 260;

        // --- ΣΤΗΛΗ 1 ---
        // Type
        JLabel lblTypeTag = new JLabel("Type:");
        lblTypeTag.setForeground(TEXT_PINK);
        lblTypeTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTypeTag.setBounds(col1X, 55, 100, 18);
        card.add(lblTypeTag);

        JLabel lblTypeVal = new JLabel(type);
        lblTypeVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTypeVal.setBounds(col1X, 73, 110, 18);
        card.add(lblTypeVal);

        // Home to
        JLabel lblHomeToTag = new JLabel("Home to:");
        lblHomeToTag.setForeground(TEXT_PINK);
        lblHomeToTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblHomeToTag.setBounds(col1X, 93, 100, 18);
        card.add(lblHomeToTag);

        JLabel lblHomeToVal = new JLabel(homeTo);
        lblHomeToVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblHomeToVal.setBounds(col1X, 111, 110, 18);
        card.add(lblHomeToVal);

        // Roommates
        JLabel lblRoommatesTag = new JLabel("Roommates:");
        lblRoommatesTag.setForeground(TEXT_PINK);
        lblRoommatesTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRoommatesTag.setBounds(col1X, 131, 110, 18);
        card.add(lblRoommatesTag);

        JLabel lblRoommatesVal = new JLabel(roommates);
        lblRoommatesVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRoommatesVal.setBounds(col1X, 149, 110, 18);
        card.add(lblRoommatesVal);


        // --- ΣΤΗΛΗ 2 ---
        // Rating
        JLabel lblRatingTag = new JLabel("Rating:");
        lblRatingTag.setForeground(TEXT_PINK);
        lblRatingTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRatingTag.setBounds(col2X, 55, 100, 18);
        card.add(lblRatingTag);

        JLabel lblRatingVal = new JLabel(rating + "⭐");
        lblRatingVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRatingVal.setBounds(col2X, 73, 130, 18);
        card.add(lblRatingVal);

        // Offers
        JLabel lblOffersTag = new JLabel("Offers:");
        lblOffersTag.setForeground(TEXT_PINK);
        lblOffersTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblOffersTag.setBounds(col2X, 93, 100, 18);
        card.add(lblOffersTag);

        // Χρήση JTextArea για τα Offers ώστε να αλλάζει γραμμή αυτόματα αν είναι μεγάλο το κείμενο
        JTextArea txtOffersVal = new JTextArea(offers);
        txtOffersVal.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtOffersVal.setLineWrap(true);
        txtOffersVal.setWrapStyleWord(true);
        txtOffersVal.setEditable(false);
        txtOffersVal.setOpaque(false);
        txtOffersVal.setBounds(col2X, 111, 140, 55);
        card.add(txtOffersVal);


        // 4. Μεγάλη Καρδιά & Πατημασίες (Verified Logo)
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

        // 5. Κείμενο Πιστοποίησης Σπιτιού
        JLabel lblVerified = new JLabel("This home has been verified by the official petbnb government.");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVerified.setBounds(100, 205, 300, 40);
        card.add(lblVerified);

        // 6. "View full profile" Link στο κάτω μέρος
        JLabel lblLink = new JLabel("View full profile", SwingConstants.RIGHT);
        lblLink.setForeground(LINK_GRAY);
        lblLink.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.setBounds(250, 265, 140, 20);
        card.add(lblLink);

        return card;
    }

    // --- Σύνδεση με το κουμπί Host της προηγούμενης φόρμας ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HostProfile("eratokapourani@gmail.com").setVisible(true);
        });
    }
}
