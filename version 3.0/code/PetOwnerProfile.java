import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class PetOwnerProfile extends JFrame {

    private String userEmail;

    // --- Color Palette από το Screenshot ---
    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243); // Off-white κάρτας
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color DESC_BOX_GRAY = new Color(225, 225, 225);
    private static final Color LINK_GRAY = new Color(160, 160, 160);

    public PetOwnerProfile(String email) {
        this.userEmail = email;

        setTitle("petbnb - Pet Owner Profile");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 800);
        setLocationRelativeTo(null);

        // Κεντρικό Panel με BorderLayout
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

        JLabel titleLabel = new JLabel("Pet Owner Profile", SwingConstants.CENTER);
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

        // --- 2. CONTENT PANEL ΜΕ SCROLLBAR ΓΙΑ ΤΙΣ ΚΑΡΤΕΣ ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Προσθήκη Κάρτας 1: Simba (Cat)
        contentPanel.add(createPetIDCard(
                "cat ID",
                "Simba",
                "1 year old",
                "Lion's king",
                "Simba is a young cat full of energy, friendly with other cats and loves food.",
                "cat.png" // Αντικαταστήστε με το δικό σας αρχείο εικόνας
        ));

        contentPanel.add(Box.createVerticalStrut(20)); // Απόσταση μεταξύ των καρτών

        // Προσθήκη Κάρτας 2: Sherlock (Dog)
        contentPanel.add(createPetIDCard(
                "dog ID",
                "Sherlock",
                "1 year old",
                "Detective",
                "Sherlock is a shy puppy that loves to cuddle and go on walks but he is very shy and doesn't like playing with other dogs",
                "dog.png" // Αντικαταστήστε με το δικό σας αρχείο εικόνας
        ));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // --- ΔΥΝΑΜΙΚΗ ΜΕΘΟΔΟΣ ΔΗΜΙΟΥΡΓΙΑΣ PET ID CARD ---
    private JPanel createPetIDCard(String typeId, String name, String age, String breed, String description, String imagePath) {

        // Custom στρογγυλεμένο panel για την κάρτα
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
        card.setLayout(null); // Absolute layout για ακριβή τοποθέτηση όπως στο UI mockup
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 310));
        card.setMinimumSize(new Dimension(420, 310));
        card.setMaximumSize(new Dimension(420, 310));

        // 1. Τύπος ID (cat ID / dog ID) πάνω αριστερά
        JLabel lblTypeId = new JLabel(typeId);
        lblTypeId.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTypeId.setBounds(40, 15, 100, 25);
        card.add(lblTypeId);

        // 2. Κόκκινο / Ροζ "my petbnb ID" Badge πάνω δεξιά
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

        // 3. Φωτογραφία Κατοικιδίου (Τετράγωνη)
        JLabel lblPetPhoto = new JLabel();
        lblPetPhoto.setBounds(25, 55, 90, 110);
        lblPetPhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            // Scaled ομαλά στις διαστάσεις του κουτιού
            Image scaledImg = icon.getImage().getScaledInstance(90, 110, Image.SCALE_SMOOTH);
            lblPetPhoto.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            lblPetPhoto.setText("No Image");
            lblPetPhoto.setHorizontalAlignment(SwingConstants.CENTER);
        }
        card.add(lblPetPhoto);

        // 4. Στοιχεία: Name, Age, Breed
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

        // 5. Γκρι Φούσκα Περιγραφής (Description Text Box)
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

        // 6. Καρδιά & Πατημασίες (Verified Icon/Label)
        // Σημείωση: Μπορείτε να βάλετε εικόνα, εδώ χρησιμοποιούμε Unicode χαρακτήρες και σχήμα για πιστότητα
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

        // 7. Verified Κείμενο
        JLabel lblVerified = new JLabel("<html>This pet and its documents have been verified<br>by the official petbnb government</html>");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVerified.setBounds(100, 205, 300, 40);
        card.add(lblVerified);

        // 8. "View full profile" Link κάτω δεξιά
        JLabel lblLink = new JLabel("View full profile", SwingConstants.RIGHT);
        lblLink.setForeground(LINK_GRAY);
        lblLink.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.setBounds(250, 265, 140, 20);
        card.add(lblLink);

        return card;
    }

    // --- Σύνδεση με το κουμπί της προηγούμενης φόρμας ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PetOwnerProfile("eratokapourani@gmail.com").setVisible(true);
        });
    }
}
