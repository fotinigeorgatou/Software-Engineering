import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class FinalPetOwnerProfile extends JFrame {

    private String userEmail;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color DESC_BOX_GRAY = new Color(235, 235, 235);
    private static final Color LINK_GRAY = new Color(160, 160, 160);
    private static final Color BUTTON_BLUE = new Color(52, 152, 219);

    public FinalPetOwnerProfile(String email) {
        this.userEmail = email;

        setTitle("petbnb - Οριστικοποιημένο Προφίλ Κατοικιδίων");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("Pet Owner Profile (Προβολή)", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topBar.add(titleLabel, BorderLayout.CENTER);

        // --- ΚΟΥΜΠΙ ΕΠΕΞΕΡΓΑΣΙΑΣ ---
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        rightButtonPanel.setOpaque(false);

        JButton editOwnerBtn = new JButton("Επεξεργασία Στοιχείων ✎");
        editOwnerBtn.setBackground(BUTTON_BLUE);
        editOwnerBtn.setForeground(Color.WHITE);
        editOwnerBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        editOwnerBtn.setFocusPainted(false);
        editOwnerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        editOwnerBtn.addActionListener(e -> {
            this.dispose();
            new PetOwnerProfile(userEmail).setVisible(true);
        });

        rightButtonPanel.add(editOwnerBtn);
        topBar.add(rightButtonPanel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // --- CONTENT PANEL ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        User user = DatabaseManager.getUser(userEmail);
        boolean hasPets = false;

        if (user != null && user.pets != null && !user.pets.trim().isEmpty() && !user.pets.equalsIgnoreCase("No pets")) {
            String[] petsArray = user.pets.split(", ");

            for (int i = 0; i < petsArray.length; i++) {
                String petInfo = petsArray[i];
                String[] parts = petInfo.split("\\|", -1);

                if (parts.length >= 19) {
                    // Φιλτράρισμα ασφαλείας: Αν κάποιο βασικό πεδίο (π.χ. Breed ή Rarity) πήρε κατά λάθος τιμή προτίμησης
                    for (int j = 0; j < parts.length; j++) {
                        if (parts[j] != null && (parts[j].equalsIgnoreCase("dog person") || parts[j].equalsIgnoreCase("cat person") || parts[j].equalsIgnoreCase("animal lover"))) {
                            parts[j] = "Κατοικίδιο"; // Επαναφορά σε safe default τιμή
                        }
                    }

                    contentPanel.add(createFinalPetIDCard(parts, i, user));
                    contentPanel.add(Box.createVerticalStrut(20));
                    hasPets = true;
                }
            }
        }

        if (!hasPets) {
            String mockData = "Sherlock|Σκύλος|1 έτους|Αρσενικό|Κατοικίδιο|Υψηλό|Καθόλου|Βασική|Φιλική|Κανένα|Κανονική|Καμία|Ναι|Όχι|Όχι|2|Ναι|Όχι|default_pet.png|";
            contentPanel.add(createFinalPetIDCard(mockData.split("\\|", -1), 0, null));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createFinalPetIDCard(String[] parts, final int petIndex, final User user) {
        String name = parts[0];
        String species = parts[1];
        String age = parts[2];
        String gender = parts[3];
        String breed = parts[4];

        String social = parts[5];
        String aggression = parts[6];
        String training = parts[7];
        String otherAnimals = parts[8];

        String health = parts[9];
        String diet = parts[10];
        String meds = parts[11];

        String indoor = parts[12];
        String yard = parts[13];
        String noOtherPets = parts[14];

        String walks = parts[15];
        String photos = parts[16];
        String grooming = parts[17];

        String imagePath = parts[18];

        String typeId = species.toLowerCase().contains("γάτα") || species.toLowerCase().contains("cat") ? "cat ID" : "dog ID";

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
        card.setPreferredSize(new Dimension(420, 270));
        card.setMinimumSize(new Dimension(420, 270));
        card.setMaximumSize(new Dimension(420, 270));

        JLabel lblTypeId = new JLabel(typeId);
        lblTypeId.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTypeId.setBounds(40, 15, 100, 25);
        card.add(lblTypeId);

        JPanel pinkBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK_HEADER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        pinkBanner.setLayout(new BorderLayout());
        pinkBanner.setOpaque(false);
        pinkBanner.setBounds(140, 15, 250, 30);
        JLabel lblBannerText = new JLabel("my petbnb ID", SwingConstants.CENTER);
        lblBannerText.setForeground(Color.WHITE);
        lblBannerText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        pinkBanner.add(lblBannerText, BorderLayout.CENTER);
        card.add(pinkBanner);

        // --- ΔΥΝΑΜΙΚΗ ΦΩΤΟΓΡΑΦΙΑ (Read Only) ---
        JLabel lblPetPhoto = new JLabel();
        lblPetPhoto.setBounds(25, 55, 90, 110);
        lblPetPhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        lblPetPhoto.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            String path = (imagePath != null) ? imagePath.trim() : "";
            Image img = null;

            if (!path.isEmpty() && !path.equalsIgnoreCase("null")) {
                File f = new File(path);
                if (f.exists()) {
                    img = new ImageIcon(path).getImage();
                } else {
                    java.net.URL imgURL = getClass().getResource(path.startsWith("/") ? path : "/" + path);
                    if (imgURL != null) {
                        img = new ImageIcon(imgURL).getImage();
                    } else {
                        File fallbackFile = new File(path);
                        if (fallbackFile.exists()) {
                            img = new ImageIcon(path).getImage();
                        }
                    }
                }
            }

            if (img != null) {
                Image scaledImg = img.getScaledInstance(90, 110, Image.SCALE_SMOOTH);
                lblPetPhoto.setIcon(new ImageIcon(scaledImg));
                lblPetPhoto.setText("");
            } else {
                lblPetPhoto.setIcon(null);
                lblPetPhoto.setText("<html><center>🐾<br>Pet Φωτό</center></html>");
            }
        } catch (Exception e) {
            lblPetPhoto.setIcon(null);
            lblPetPhoto.setText("<html><center>🐾<br>Error</center></html>");
        }
        card.add(lblPetPhoto);

        // --- ΣΤΟΙΧΕΙΑ ΚΑΤΟΙΚΙΔΙΟΥ ---
        int textX = 130;
        JLabel lblNameTag = new JLabel("Name:"); lblNameTag.setForeground(TEXT_PINK); lblNameTag.setBounds(textX, 55, 80, 15); card.add(lblNameTag);
        JLabel lblNameVal = new JLabel(name); lblNameVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblNameVal.setBounds(textX, 70, 100, 20); card.add(lblNameVal);

        JLabel lblAgeTag = new JLabel("Age:"); lblAgeTag.setForeground(TEXT_PINK); lblAgeTag.setBounds(textX, 95, 80, 15); card.add(lblAgeTag);
        JLabel lblAgeVal = new JLabel(age); lblAgeVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblAgeVal.setBounds(textX, 110, 100, 20); card.add(lblAgeVal);

        JLabel lblBreedTag = new JLabel("Breed:"); lblBreedTag.setForeground(TEXT_PINK); lblBreedTag.setBounds(textX, 135, 80, 15); card.add(lblBreedTag);
        JLabel lblBreedVal = new JLabel(breed); lblBreedVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblBreedVal.setBounds(textX, 150, 100, 20); card.add(lblBreedVal);

        // --- ΠΛΑΙΣΙΟ ΠΕΡΙΓΡΑΦΗΣ ---
        JPanel descBox = new JPanel();
        descBox.setOpaque(false);
        descBox.setBounds(235, 60, 160, 110);
        descBox.setLayout(new BorderLayout());

        String initialDesc = (parts.length > 19 && parts[19] != null && !parts[19].trim().isEmpty())
                ? parts[19]
                : name + " είναι ένα υπέροχο Κατοικίδιο που καταχωρήθηκε επιτυχώς στην πλατφόρμα petbnb!";

        // Αν η περιγραφή περιέχει κατά λάθος προτιμήσεις λόγω κακού database parsing, την αντικαθιστούμε με κάτι όμορφο
        if (initialDesc.equalsIgnoreCase("dog person") || initialDesc.equalsIgnoreCase("cat person") || initialDesc.equalsIgnoreCase("animal lover")) {
            initialDesc = name + " είναι ένα υπέροχο Κατοικίδιο που καταχωρήθηκε επιτυχώς στην πλατφόρμα petbnb!";
        }

        JTextArea txtDesc = new JTextArea(initialDesc);
        txtDesc.setLineWrap(true); txtDesc.setWrapStyleWord(true);
        txtDesc.setBackground(DESC_BOX_GRAY);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDesc.setBorder(new EmptyBorder(5, 5, 5, 5));
        txtDesc.setEditable(false);
        descBox.add(txtDesc, BorderLayout.CENTER);
        card.add(descBox);

        // UI Διακόσμηση
        JLabel lblHeart = new JLabel("❤"); lblHeart.setForeground(PINK_HEADER); lblHeart.setFont(new Font("SansSerif", Font.PLAIN, 42)); lblHeart.setBounds(40, 190, 50, 50); card.add(lblHeart);
        JLabel lblPaw = new JLabel("🐾"); lblPaw.setForeground(PINK_HEADER); lblPaw.setBounds(75, 225, 20, 20); card.add(lblPaw);

        JLabel lblVerified = new JLabel("<html>This pet and its documents have been verified<br>by the official petbnb government</html>");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 12)); lblVerified.setForeground(Color.DARK_GRAY); lblVerified.setBounds(110, 195, 280, 35); card.add(lblVerified);

        JButton btnViewFullProfile = new JButton("View full profile");
        btnViewFullProfile.setFont(new Font("SansSerif", Font.ITALIC, 11));
        btnViewFullProfile.setForeground(LINK_GRAY);
        btnViewFullProfile.setBounds(280, 235, 120, 25);
        btnViewFullProfile.setContentAreaFilled(false); btnViewFullProfile.setBorderPainted(false); btnViewFullProfile.setFocusPainted(false);
        btnViewFullProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnViewFullProfile.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "=== ΣΤΟΙΧΕΙΑ ΣΥΜΠΕΡΙΦΟΡΑΣ ===\n" +
                            "• Κοινωνικότητα: " + social + "\n" +
                            "• Επιθετικότητα: " + aggression + "\n" +
                            "• Εκπαίδευση: " + training + "\n" +
                            "• Σχέση με άλλα ζώα: " + otherAnimals + "\n\n" +
                            "=== ΙΑΤΡΙΚΟ ΙΣΤΟΡΙΚΟ ===\n" +
                            "• Προβλήματα/Αλλεργίες: " + health + "\n" +
                            "• Ειδική Διατροφή: " + diet + "\n" +
                            "• Φαρμακευτική Αγωγή: " + meds + "\n\n" +
                            "=== ΠΡΟΤΙΜΗΣΕΙΣ ΚΑΤΑΛΥΜΑΤΟΣ ===\n" +
                            "• Εσωτερικός χώρος: " + indoor + "\n" +
                            "• Απαιτείται αυλή: " + yard + "\n" +
                            "• Χωρίς άλλα κατοικίδια στον χώρο: " + noOtherPets + "\n\n" +
                            "=== ΠΑΡΟΧΕΣ & ΥΠΗΡΕΣΙΕΣ ===\n" +
                            "• Βόλτες ανά ημέρα: " + walks + "\n" +
                            "• Καθημερινές φωτογραφίες: " + photos + "\n" +
                            "• Υπηρεσία Grooming: " + grooming,
                    "Πλήρες Προφίλ: " + name, JOptionPane.INFORMATION_MESSAGE);
        });
        card.add(btnViewFullProfile);

        return card;
    }
}