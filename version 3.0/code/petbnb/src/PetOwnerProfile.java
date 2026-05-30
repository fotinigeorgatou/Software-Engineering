import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PetOwnerProfile extends JFrame {

    private String userEmail;
    // Λίστα για να κρατάμε τα text areas των σημειώσεων ώστε να τα αποθηκεύσουμε μαζικά στο τέλος
    private List<JTextArea> descTextAreaList = new ArrayList<>();

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color DESC_BOX_GRAY = new Color(225, 225, 225);
    private static final Color LINK_GRAY = new Color(160, 160, 160);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);

    public PetOwnerProfile(String email) {
        this.userEmail = email;

        setTitle("petbnb - Pet Owner Profile");
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

        JLabel titleLabel = new JLabel("Pet Owner Profile", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topBar.add(titleLabel, BorderLayout.CENTER);

        // --- ΚΟΥΜΠΙ ΟΡΙΣΤΙΚΟΠΟΙΗΣΗΣ ΣΤΗΝ ΚΟΡΥΦΗ ---
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        rightButtonPanel.setOpaque(false);

        JButton finalizeOwnerBtn = new JButton("Οριστικοποίηση Pet Owner ✓");
        finalizeOwnerBtn.setBackground(SUCCESS_GREEN);
        finalizeOwnerBtn.setForeground(Color.WHITE);
        finalizeOwnerBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        finalizeOwnerBtn.setFocusPainted(false);
        finalizeOwnerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        finalizeOwnerBtn.addActionListener(e -> {
            User currentUser = DatabaseManager.getUser(userEmail);

            // ΑΥΤΟΜΑΤΟ ΜΑΖΙΚΟ SAVE ΠΡΙΝ ΤΗΝ ΕΞΟΔΟ
            if (currentUser != null && currentUser.pets != null && !currentUser.pets.trim().isEmpty() && !currentUser.pets.equalsIgnoreCase("No pets")) {
                String[] petsArray = currentUser.pets.split(", ");

                // Ανανέωση των σημειώσεων για κάθε κατοικίδιο από το UI στη βάση
                for (int i = 0; i < petsArray.length; i++) {
                    if (i < descTextAreaList.size()) {
                        String[] currentParts = petsArray[i].split("\\|", -1);
                        int dynamicSize = Math.max(20, currentParts.length);
                        String[] newParts = new String[dynamicSize];

                        // Αρχικοποίηση με άδεια Strings αντί για null!
                        for (int j = 0; j < dynamicSize; j++) {
                            newParts[j] = "";
                        }

                        // Αντιγραφή των παλιών δεδομένων με ασφάλεια
                        System.arraycopy(currentParts, 0, newParts, 0, currentParts.length);

                        // Διασφάλιση ότι η εικόνα δεν θα χαθεί ή αντικατασταθεί από null
                        if (currentParts.length > 18 && currentParts[18] != null && !currentParts[18].trim().isEmpty() && !currentParts[18].equalsIgnoreCase("null")) {
                            newParts[18] = currentParts[18].trim();
                        } else {
                            newParts[18] = "default_pet.png";
                        }

                        // Προσθήκη του κειμένου από το JTextArea στη θέση [19]
                        newParts[19] = descTextAreaList.get(i).getText().trim().replace("|", "").replace(";", "");

                        petsArray[i] = String.join("|", newParts);
                    }
                }
                currentUser.pets = String.join(", ", petsArray);
                DatabaseManager.updateUser(currentUser); // Οριστική αποθήκευση στη Βάση Δεδομένων
            }

            JOptionPane.showMessageDialog(this, "Το Pet Owner προφίλ οριστικοποιήθηκε επιτυχώς!");
            this.dispose();

            User user = DatabaseManager.getUser(userEmail);
            if (user != null && user.role.equalsIgnoreCase("Dual")) {
                // 3ο Ενδεχόμενο: Μετά το Pet Owner Profile, ανοίγει το Host Profile
                JOptionPane.showMessageDialog(null, "Ροή Dual: Ανακατεύθυνση στο Host Profile για τη δεύτερη οριστικοποίηση.");
                new HostProfile(userEmail).setVisible(true);
            } else {
                // 1ο Ενδεχόμενο: Πηγαίνει κατευθείαν στο FinalProfile
                new FinalProfile(userEmail).setVisible(true);
            }
        });

        rightButtonPanel.add(finalizeOwnerBtn);
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
                String[] parts = petInfo.split("\\|", -1); // Χρήση -1 για σωστό bounding

                // Διορθωμένο: Επιτρέπει την εμφάνιση ακόμα κι αν το attribute count διαφέρει ελαφρώς
                if (parts.length >= 5) {
                    contentPanel.add(createPetIDCard(parts, i, user));
                    contentPanel.add(Box.createVerticalStrut(20));
                    hasPets = true;
                }
            }
        }

        if (!hasPets) {
            String mockData = "Sherlock|Σκύλος|1 έτους|Αρσενικό|Κατοικίδιο|Υψηλό|Καθόλου|Βασική|Φιλική|Κανένα|Κανονική|Καμία|Ναι|Όχι|Όχι|2|Ναι|Όχι|default_pet.png|";
            contentPanel.add(createPetIDCard(mockData.split("\\|", -1), 0, null));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createPetIDCard(String[] parts, final int petIndex, final User user) {
        // Ασφαλής ανάκτηση μεταβλητών βάσει διαθέσιμου μήκους πίνακα
        String name = parts.length > 0 ? parts[0] : "Άγνωστο";
        String species = parts.length > 1 ? parts[1] : "Σκύλος";
        String age = parts.length > 2 ? parts[2] : "-";
        String gender = parts.length > 3 ? parts[3] : "-";
        String breed = parts.length > 4 ? parts[4] : "-";

        String social = parts.length > 5 ? parts[5] : "-";
        String aggression = parts.length > 6 ? parts[6] : "-";
        String training = parts.length > 7 ? parts[7] : "-";
        String otherAnimals = parts.length > 8 ? parts[8] : "-";

        String health = parts.length > 9 ? parts[9] : "-";
        String diet = parts.length > 10 ? parts[10] : "-";
        String meds = parts.length > 11 ? parts[11] : "-";

        String indoor = parts.length > 12 ? parts[12] : "-";
        String yard = parts.length > 13 ? parts[13] : "-";
        String noOtherPets = parts.length > 14 ? parts[14] : "-";

        String walks = parts.length > 15 ? parts[15] : "-";
        String photos = parts.length > 16 ? parts[16] : "-";
        String grooming = parts.length > 17 ? parts[17] : "-";

        String imagePath = (parts.length > 18 && !parts[18].isEmpty()) ? parts[18] : "default_pet.png";

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

        // Κουμπί Φωτογραφίας
        JButton btnPetPhoto = new JButton();
        btnPetPhoto.setBounds(25, 55, 90, 110);
        btnPetPhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btnPetPhoto.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final String[] currentImgPath = { imagePath };
        Runnable updateButtonIcon = () -> {
            try {
                if (currentImgPath[0] == null || currentImgPath[0].isEmpty()) {
                    currentImgPath[0] = "default_pet.png";
                }
                File f = new File(currentImgPath[0]);
                if (f.exists() || currentImgPath[0].equals("default_pet.png")) {
                    ImageIcon icon = new ImageIcon(currentImgPath[0]);
                    Image scaledImg = icon.getImage().getScaledInstance(90, 110, Image.SCALE_SMOOTH);
                    btnPetPhoto.setIcon(new ImageIcon(scaledImg));
                    btnPetPhoto.setText("");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                btnPetPhoto.setIcon(null);
                btnPetPhoto.setText("<html><center>Ανέβασμα<br>Φωτό</center></html>");
            }
        };
        updateButtonIcon.run();

        btnPetPhoto.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentImgPath[0] = chooser.getSelectedFile().getAbsolutePath();
                updateButtonIcon.run();

                if (user != null) {
                    String[] petsArray = user.pets.split(", ");
                    String[] currentParts = petsArray[petIndex].split("\\|", -1);
                    int dSize = Math.max(20, currentParts.length);
                    String[] updatedParts = new String[dSize];
                    System.arraycopy(currentParts, 0, updatedParts, 0, currentParts.length);

                    updatedParts[18] = currentImgPath[0].replace(";", "").replace(",", "");
                    petsArray[petIndex] = String.join("|", updatedParts);
                    user.pets = String.join(", ", petsArray);
                    DatabaseManager.updateUser(user); // Άμεση αποθήκευση της εικόνας στη βάση
                }
            }
        });
        card.add(btnPetPhoto);

        // Στοιχεία Κειμένου
        int textX = 130;
        JLabel lblNameTag = new JLabel("Name:"); lblNameTag.setForeground(TEXT_PINK); lblNameTag.setBounds(textX, 55, 80, 15); card.add(lblNameTag);
        JLabel lblNameVal = new JLabel(name); lblNameVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblNameVal.setBounds(textX, 70, 100, 20); card.add(lblNameVal);

        JLabel lblAgeTag = new JLabel("Age:"); lblAgeTag.setForeground(TEXT_PINK); lblAgeTag.setBounds(textX, 95, 80, 15); card.add(lblAgeTag);
        JLabel lblAgeVal = new JLabel(age); lblAgeVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblAgeVal.setBounds(textX, 110, 100, 20); card.add(lblAgeVal);

        JLabel lblBreedTag = new JLabel("Breed:"); lblBreedTag.setForeground(TEXT_PINK); lblBreedTag.setBounds(textX, 135, 80, 15); card.add(lblBreedTag);
        JLabel lblBreedVal = new JLabel(breed); lblBreedVal.setFont(new Font("SansSerif", Font.BOLD, 14)); lblBreedVal.setBounds(textX, 150, 100, 20); card.add(lblBreedVal);

        // Πλαίσιο Περιγραφής Δεξιά
        JPanel descBox = new JPanel();
        descBox.setOpaque(false);
        descBox.setBounds(235, 60, 160, 110);
        descBox.setLayout(new BorderLayout());

        String initialDesc = (parts.length > 19 && !parts[19].isEmpty()) ? parts[19] : name + " είναι ένα υπέροχο Κατοικίδιο που καταχωρήθηκε επιτυχώς στην πλατφόρμα petbnb!";

        JTextArea txtDesc = new JTextArea(initialDesc);
        txtDesc.setLineWrap(true); txtDesc.setWrapStyleWord(true);
        txtDesc.setBackground(DESC_BOX_GRAY);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtDesc.setBorder(new EmptyBorder(5, 5, 5, 5));
        descBox.add(txtDesc, BorderLayout.CENTER);

        // Κρατάμε αναφορά του JTextArea στη λίστα μας για το αυτόματο save στο τέλος
        descTextAreaList.add(txtDesc);

        // Κουμπί Χειροκίνητης Αποθήκευσης Σημειώσεων
        JButton btnSaveNotes = new JButton("💾 Save Notes");
        btnSaveNotes.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnSaveNotes.setForeground(Color.DARK_GRAY);
        btnSaveNotes.setBackground(Color.LIGHT_GRAY);
        btnSaveNotes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSaveNotes.addActionListener(e -> {
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Mock Mode: Οι σημειώσεις άλλαξαν προσωρινά!");
                return;
            }

            String[] petsArray = user.pets.split(", ");
            String petInfo = petsArray[petIndex];
            String[] currentParts = petInfo.split("\\|", -1);
            int dSize = Math.max(20, currentParts.length);
            String[] newParts = new String[dSize];

            for (int j = 0; j < dSize; j++) {
                newParts[j] = "";
            }

            System.arraycopy(currentParts, 0, newParts, 0, currentParts.length);

            if (currentParts.length > 18 && currentParts[18] != null && !currentParts[18].isEmpty()) {
                newParts[18] = currentParts[18];
            } else {
                newParts[18] = "default_pet.png";
            }

            newParts[19] = txtDesc.getText().trim().replace("|", "").replace(";", "");

            petsArray[petIndex] = String.join("|", newParts);
            user.pets = String.join(", ", petsArray);
            DatabaseManager.updateUser(user);

            JOptionPane.showMessageDialog(this, "Οι σημειώσεις οριστικοποιήθηκαν και αποθηκεύτηκαν μόνιμα!", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
        });
        descBox.add(btnSaveNotes, BorderLayout.SOUTH);
        card.add(descBox);

        // UI Διακόσμηση
        JLabel lblHeart = new JLabel("❤"); lblHeart.setForeground(PINK_HEADER); lblHeart.setFont(new Font("SansSerif", Font.PLAIN, 42)); lblHeart.setBounds(40, 190, 50, 50); card.add(lblHeart);
        JLabel lblPaw = new JLabel("🐾"); lblPaw.setForeground(PINK_HEADER); lblPaw.setBounds(75, 225, 20, 20); card.add(lblPaw);

        JLabel lblVerified = new JLabel("<html>This pet and its documents have been verified<br>by the official petbnb government</html>");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 12)); lblVerified.setForeground(Color.DARK_GRAY); lblVerified.setBounds(110, 195, 280, 35); card.add(lblVerified);

        // Link Button
        JButton btnViewFullProfile = new JButton("View full profile");
        btnViewFullProfile.setFont(new Font("SansSerif", Font.ITALIC, 11));
        btnViewFullProfile.setForeground(LINK_GRAY);
        btnViewFullProfile.setBounds(280, 235, 120, 25);
        btnViewFullProfile.setContentAreaFilled(false); btnViewFullProfile.setBorderPainted(false); btnViewFullProfile.setFocusPainted(false);
        btnViewFullProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnViewFullProfile.addActionListener(e -> {
            String details = "=== ΣΤΟΙΧΕΙΑ ΣΥΜΠΕΡΙΦΟΡΑΣ ===\n• Κοινωνικότητα: " + social + "\n• Επιθετικότητα: " + aggression + "\n• Εκπαίδευση: " + training + "\n• Σχέση με άλλα ζώα: " + otherAnimals + "\n\n" +
                    "=== ΙΑΤΡΙΚΟ ΙΣΤΟΡΙΚΟ ===\n• Προβλήματα/Αλλεργίες: " + health + "\n• Ειδική Διατροφή: " + diet + "\n• Φαρμακευτική Αγωγή: " + meds + "\n\n" +
                    "=== ΠΡΟΤΙΜΗΣΕΙΣ ΚΑΤΑΛΥΜΑΤΟΣ ===\n• Εσωτερικός χώρος: " + indoor + "\n• Απαιτείται αυλή: " + yard + "\n• Χωρίς άλλα κατοικίδια στον χώρο: " + noOtherPets + "\n\n" +
                    "=== ΠΑΡΟΧΕΣ & ΥΠΗΡΕΣΙΕΣ ===\n• Βόλτες ανά ημέρα: " + walks + "\n• Καθημερινές φωτογραφίες: " + photos + "\n• Υπηρεσία Grooming: " + grooming;
            JOptionPane.showMessageDialog(this, details, "Πλήρες Προφίλ: " + name, JOptionPane.INFORMATION_MESSAGE);
        });
        card.add(btnViewFullProfile);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetOwnerProfile("axkex@gmail.com").setVisible(true));
    }
}