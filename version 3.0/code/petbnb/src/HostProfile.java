import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class HostProfile extends JFrame {

    private String userEmail;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(240, 240, 240);
    private static final Color CARD_BG = new Color(249, 250, 243);
    private static final Color TEXT_PINK = new Color(255, 105, 180);
    private static final Color LINK_GRAY = new Color(160, 160, 160);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);

    public HostProfile(String email) {
        this.userEmail = email;

        setTitle("petbnb - Host Profile");
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

        JLabel titleLabel = new JLabel("Host Profile", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topBar.add(titleLabel, BorderLayout.CENTER);

        // --- ΚΟΥΜΠΙ ΟΡΙΣΤΙΚΟΠΟΙΗΣΗΣ ΣΤΗΝ ΚΟΡΥΦΗ ---
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        rightButtonPanel.setOpaque(false);

        JButton finalizeHostBtn = new JButton("Οριστικοποίηση Host ✓");
        finalizeHostBtn.setBackground(SUCCESS_GREEN);
        finalizeHostBtn.setForeground(Color.WHITE);
        finalizeHostBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        finalizeHostBtn.setFocusPainted(false);
        finalizeHostBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        finalizeHostBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Το προφίλ του Φιλοξενητή (Host) οριστικοποιήθηκε επιτυχώς!",
                    "petbnb Σύστημα", JOptionPane.INFORMATION_MESSAGE);

            this.dispose();

            JOptionPane.showMessageDialog(null,
                    "Η διαδικασία ρύθμισης του προφίλ σας ολοκληρώθηκε με επιτυχία!",
                    "petbnb Σύστημα", JOptionPane.INFORMATION_MESSAGE);

            // Άμεσο άνοιγμα του FinalProfile μετά την οριστικοποίηση του Host
            new FinalProfile(userEmail).setVisible(true);
        });

        // Προσθήκη των στοιχείων του Top Bar για να εμφανιστούν στο UI
        rightButtonPanel.add(finalizeHostBtn);
        topBar.add(rightButtonPanel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // --- CONTENT PANEL ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        User user = DatabaseManager.getUser(userEmail);
        boolean hasAccommodations = false;

        if (user != null && user.location != null && !user.location.trim().isEmpty()) {
            // Διορθωμένο split regex για να αντέχει αν υπάρχουν αποκλίσεις στα κενά διαστήματα
            String[] accArray = user.location.split("\\s*\\|\\s*");

            for (int i = 0; i < accArray.length; i++) {
                String rawHouseData = accArray[i];
                if (rawHouseData.trim().isEmpty()) continue;

                // Διάσπαση των δεδομένων βάσει του χαρακτήρα '#'
                String[] tokens = rawHouseData.split("#", -1);

                String typeOfProperty = (tokens.length > 0) ? tokens[0] : "Κατάλυμα";
                String services = (tokens.length > 1) ? tokens[1] : "-";
                String roommates = (tokens.length > 2) ? tokens[2] : "Όχι";
                String offers = (tokens.length > 3) ? tokens[3] : "Καμία";

                contentPanel.add(createHostHomeCard(
                        "home ID #" + (i + 1),
                        typeOfProperty,  // 1.3 Combo Box
                        services,        // 1.4 Animals
                        roommates,       // 1.5 Own Pets (Ναι/Όχι)
                        "5.0",           // Rating
                        offers,          // 1.7 Extra Services
                        "apartment.png",
                        i,
                        user
                ));
                contentPanel.add(Box.createVerticalStrut(20));
                hasAccommodations = true;
            }
        }

        if (!hasAccommodations) {
            contentPanel.add(createHostHomeCard(
                    "cat home", "Διαμέρισμα", "Γάτες", "Όχι", "4.9", "Βόλτα", "apartment.png", 0, null
            ));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // Επιτάχυνση της ταχύτητας κύλισης για μεγάλο αριθμό καταλυμάτων
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHostHomeCard(String labelHome, String type, String homeTo, String roommates, String rating, String offers, String imagePath, final int houseIndex, final User user) {
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
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
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

        JButton btnHomePhoto = new JButton();
        btnHomePhoto.setBounds(25, 55, 95, 110);
        btnHomePhoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btnHomePhoto.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final String[] currentImgPath = { imagePath };
        Runnable updateHomeIcon = () -> {
            try {
                File f = new File(currentImgPath[0]);
                if (f.exists() || currentImgPath[0].equals("apartment.png")) {
                    ImageIcon icon = new ImageIcon(currentImgPath[0]);
                    Image scaledImg = icon.getImage().getScaledInstance(95, 110, Image.SCALE_SMOOTH);
                    btnHomePhoto.setIcon(new ImageIcon(scaledImg));
                    btnHomePhoto.setText("");
                } else { throw new Exception(); }
            } catch (Exception e) {
                btnHomePhoto.setIcon(null);
                btnHomePhoto.setText("<html><center>Ανέβασμα<br>Φωτό</center></html>");
            }
        };
        updateHomeIcon.run();

        btnHomePhoto.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentImgPath[0] = chooser.getSelectedFile().getAbsolutePath();
                updateHomeIcon.run();

                if (user != null && user.location != null) {
                    String[] accArray = user.location.split("\\s*\\|\\s*");
                    String[] tokens = accArray[houseIndex].split("#", -1);
                    // Μπορείτε να προσθέσετε αποθήκευση εικόνας καταλύματος στη θέση token αν χρειαστεί
                }
            }
        });
        card.add(btnHomePhoto);

        int col1X = 140;
        int col2X = 260;

        // --- ΣΤΗΛΗ 1 ---
        JLabel lblTypeTag = new JLabel("Type:");
        lblTypeTag.setForeground(TEXT_PINK);
        lblTypeTag.setBounds(col1X, 55, 100, 18);
        card.add(lblTypeTag);

        JLabel lblTypeVal = new JLabel(type);
        lblTypeVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTypeVal.setBounds(col1X, 73, 110, 18);
        card.add(lblTypeVal);

        JLabel lblHomeToTag = new JLabel("Services:");
        lblHomeToTag.setForeground(TEXT_PINK);
        lblHomeToTag.setBounds(col1X, 93, 100, 18);
        card.add(lblHomeToTag);

        JLabel lblHomeToVal = new JLabel(homeTo);
        lblHomeToVal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblHomeToVal.setBounds(col1X, 111, 110, 18);
        card.add(lblHomeToVal);

        JLabel lblRoommatesTag = new JLabel("Roomates:");
        lblRoommatesTag.setForeground(TEXT_PINK);
        lblRoommatesTag.setBounds(col1X, 131, 110, 18);
        card.add(lblRoommatesTag);

        JLabel lblRoommatesVal = new JLabel(roommates);
        lblRoommatesVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRoommatesVal.setBounds(col1X, 149, 110, 18);
        card.add(lblRoommatesVal);

        // --- ΣΤΗΛΗ 2 ---
        JLabel lblRatingTag = new JLabel("Rating:");
        lblRatingTag.setForeground(TEXT_PINK);
        lblRatingTag.setBounds(col2X, 55, 100, 18);
        card.add(lblRatingTag);

        JLabel lblRatingVal = new JLabel(rating + " ⭐");
        lblRatingVal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRatingVal.setBounds(col2X, 73, 130, 18);
        card.add(lblRatingVal);

        JLabel lblOffersTag = new JLabel("Offers:");
        lblOffersTag.setForeground(TEXT_PINK);
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
        lblPaw.setBounds(75, 225, 20, 20);
        card.add(lblPaw);

        JLabel lblVerified = new JLabel("This home has been verified by the official petbnb government.");
        lblVerified.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblVerified.setBounds(100, 205, 300, 40);
        card.add(lblVerified);

        // 6. "View full profile" Link Button για τον Host
        JButton btnViewFullProfile = new JButton("View full profile");
        btnViewFullProfile.setFont(new Font("SansSerif", Font.ITALIC, 11));
        btnViewFullProfile.setForeground(LINK_GRAY);
        btnViewFullProfile.setBounds(260, 265, 140, 20);
        btnViewFullProfile.setContentAreaFilled(false);
        btnViewFullProfile.setBorderPainted(false);
        btnViewFullProfile.setFocusPainted(false);
        btnViewFullProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnViewFullProfile.addActionListener(e -> {
            String experienceInfo = "Δεν καταχωρήθηκε επιπλέον εμπειρία";
            if (user != null) {
                experienceInfo = "Διαθέσιμη στο αναλυτικό προφίλ του Host";
            }

            String hostDetails = "=== ΠΑΡΟΥΣΙΑ ΔΙΚΩΝ ΣΑΣ ΖΩΩΝ (1.5) ===\n" +
                    "• Ύπαρξη κατοικίδιων στο χώρο: " + roommates + "\n" +
                    "• Φύλο / Ράτσα / Εξοικείωση: Διαθέσιμο κατόπιν επικοινωνίας\n\n" +

                    "=== ΕΜΠΕΙΡΙΑ & ΓΝΩΣΕΙΣ (1.6) ===\n" +
                    "• Ικανότητες: Πρώτες Βοήθειες, Εκπαίδευση, Χορήγηση Φαρμάκων (Ανάλογα την πιστοποίηση)\n" +
                    "• Λεπτομέρειες: " + experienceInfo + "\n\n" +

                    "=== ΠΑΡΕΧΟΜΕΝΕΣ EXTRA ΥΠΗΡΕΣΙΕΣ (1.7) ===\n" +
                    "• Επιλογές: " + offers + "\n\n" +

                    "=== ΔΥΝΑΤΟΤΗΤΑ ΦΙΛΟΞΕΝΙΑΣ (1.4) ===\n" +
                    "• Κατάλληλο για: " + homeTo + "\n\n" +
                    "--------------------------------------------------\n" +
                    "* Όλοι οι φιλοξενητές έχουν δεσμευτεί για την τήρηση\n" +
                    "των επίσημων κανόνων ασφαλείας του petbnb.";

            JOptionPane.showMessageDialog(this, hostDetails, "Πλήρες Προφίλ Φιλοξενητή", JOptionPane.INFORMATION_MESSAGE);
        });
        card.add(btnViewFullProfile);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HostProfile("eratokapourani@gmail.com").setVisible(true));
    }
}