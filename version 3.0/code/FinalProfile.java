import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FinalProfile extends JFrame {

    private User user;
    private String currentProfilePicPath;

    private Map<String, JTextField> fieldMap = new HashMap<>();
    private JComboBox<String> cbPreferences;
    private CircularProfileLabel profileImg;

    private RoundedButton hostButton;
    private RoundedButton ownerButton;
    private RoundedButton exitBtn;

    // --- Color Palette (Ίδια με το ProfilePreview) ---
    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(245, 245, 245);
    private static final Color BUTTON_PINK = new Color(255, 110, 140);
    private static final Color LABEL_PINK = new Color(255, 160, 180);
    private static final Color FIELD_GRAY = new Color(235, 235, 235); // Ελαφρώς πιο σκούρο για read-only εφέ
    private static final Color PURPLE = new Color(193, 163, 229);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color GRAY_DISABLED = new Color(180, 180, 180);

    public FinalProfile(String email) {
        // Ανάκτηση των οριστικοποιημένων στοιχείων από τη βάση δεδομένων
        user = DatabaseManager.getUser(email);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Ο χρήστης δεν βρέθηκε στη βάση δεδομένων!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        this.currentProfilePicPath = user.profilePicPath != null ? user.profilePicPath.trim() : "";

        setTitle("petbnb - Οριστικοποιημένο Προφίλ Χρήστη");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 820);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 60));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = new JLabel("≡");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel settingsBtn = new JLabel("⚙");
        settingsBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        settingsBtn.setForeground(Color.WHITE);

        topBar.add(menuBtn, BorderLayout.WEST);
        topBar.add(settingsBtn, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // --- MAIN CONTENT CONTAINER ---
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(new Color(252, 252, 252));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(252, 252, 252));
        centerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        // ΣΤΑΤΙΚΟ AVATAR (Δεν αλλάζει πλέον με κλικ)
        profileImg = new CircularProfileLabel(currentProfilePicPath, 140);

        JLabel username = new JLabel((user.name != null ? user.name : "") + " " + (user.lastname != null ? user.lastname : ""));
        username.setFont(new Font("SansSerif", Font.BOLD, 20));
        username.setForeground(Color.BLACK);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setBorder(new EmptyBorder(10, 0, 10, 0));

        centerPanel.add(profileImg);
        centerPanel.add(username);

        // Role Buttons (Ενεργά ανάλογα με το ρόλο που αποθηκεύτηκε)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 10, 40));

        hostButton = new RoundedButton("Προβολή Host Profile", PURPLE, BUTTON_PINK);
        ownerButton = new RoundedButton("Προβολή Pet Owner Profile", PURPLE, BUTTON_PINK);

        // Ενεργοποίηση κουμπιών βάσει του ρόλου στη βάση δεδομένων
        if (user.role != null) {
            if (user.role.equalsIgnoreCase("Dual")) {
                hostButton.setEnabled(true);
                ownerButton.setEnabled(true);
            } else if (user.role.equalsIgnoreCase("Host")) {
                hostButton.setEnabled(true);
                ownerButton.setEnabled(false);
            } else { // Pet Owner
                hostButton.setEnabled(false);
                ownerButton.setEnabled(true);
            }
        }

        ownerButton.addActionListener(e -> new FinalPetOwnerProfile(user.email).setVisible(true));
        hostButton.addActionListener(e -> new HostProfile(user.email).setVisible(true));

        buttonPanel.add(hostButton);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(ownerButton);

        centerPanel.add(buttonPanel);
        contentContainer.add(centerPanel);

        // --- INFORMATION GRID PANEL (Η σειρά των πεδίων σου παραμένει ακριβώς η ίδια) ---
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 12));
        infoPanel.setBackground(new Color(252, 252, 252));
        infoPanel.setBorder(new EmptyBorder(5, 20, 15, 20));

        infoPanel.add(createReadOnlyInfoField("Name:", user.name));
        infoPanel.add(createReadOnlyInfoField("Pets:", user.pets));
        infoPanel.add(createReadOnlyInfoField("Lastname:", user.lastname));
        infoPanel.add(createReadOnlyInfoField("Preferences:", user.preferences));
        infoPanel.add(createReadOnlyInfoField("Age:", user.age));
        infoPanel.add(createReadOnlyInfoField("Email:", user.email));
        infoPanel.add(createReadOnlyInfoField("Location:", user.location));
        infoPanel.add(createReadOnlyInfoField("Rating:", user.rating));

        contentContainer.add(infoPanel);

        // --- BOTTOM BAR ---
        JPanel finalizationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        finalizationPanel.setBackground(new Color(252, 252, 252));
        finalizationPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        exitBtn = new RoundedButton("Έξοδος Εφαρμογής", Color.WHITE, SUCCESS_GREEN);
        exitBtn.setPreferredSize(new Dimension(340, 45));
        exitBtn.setMaximumSize(new Dimension(340, 45));
        exitBtn.addActionListener(e -> System.exit(0)); // Κλείνει την εφαρμογή
        finalizationPanel.add(exitBtn);

        contentContainer.add(finalizationPanel);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createReadOnlyInfoField(String labelText, String valueText) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(LABEL_PINK);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel fieldBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_GRAY);
                int arc = getHeight();
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                g2.dispose();
            }
        };

        fieldBg.setLayout(new BorderLayout());
        fieldBg.setOpaque(false);
        fieldBg.setPreferredSize(new Dimension(185, 36));

        String safeValue = (valueText != null) ? valueText.trim() : "";

        if (labelText.equals("Rating:")) {
            // ΔΙΟΡΘΩΣΗ: Φιλτράρισμα ώστε αν η βάση έχει στείλει κείμενο προτίμησης κατά λάθος, να μπαίνει "0"
            String cleanRating = "0";
            if (!safeValue.isEmpty() && (Character.isDigit(safeValue.charAt(0)) || safeValue.contains("."))) {
                cleanRating = safeValue;
            }

            JLabel ratingLabel = new JLabel(cleanRating + " \u2605");
            ratingLabel.setForeground(Color.DARK_GRAY);
            ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            ratingLabel.setBorder(new EmptyBorder(0, 15, 0, 15));
            fieldBg.add(ratingLabel);
        }
        else if (labelText.equals("Preferences:")) {
            // Εμφάνιση ως απλό ComboBox αλλά απενεργοποιημένο
            String[] options = { safeValue.isEmpty() ? "animal lover" : safeValue };
            cbPreferences = new JComboBox<>(options);
            cbPreferences.setFont(new Font("SansSerif", Font.BOLD, 12));
            cbPreferences.setBorder(new EmptyBorder(0, 10, 0, 10));
            cbPreferences.setOpaque(false);
            cbPreferences.setEnabled(false); // Read-only
            fieldBg.add(cbPreferences, BorderLayout.CENTER);
        }
        else {
            JTextField textInput = new JTextField(safeValue);
            textInput.setFont(new Font("SansSerif", Font.BOLD, 13));
            textInput.setBorder(new EmptyBorder(0, 15, 0, 15));
            textInput.setOpaque(false);
            textInput.setEditable(false); // Κλειδωμένο πεδίο
            textInput.setForeground(Color.DARK_GRAY);

            fieldMap.put(labelText, textInput);
            fieldBg.add(textInput, BorderLayout.CENTER);
        }

        p.add(label, BorderLayout.NORTH);
        p.add(fieldBg, BorderLayout.CENTER);

        return p;
    }

    // --- REUSABLE COMPONENTS ---

    class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton(String text, Color textColor, Color bgColor) {
            super(text);
            this.backgroundColor = bgColor;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setForeground(textColor);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setMaximumSize(new Dimension(320, 45));
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setCursor(new Cursor(enabled ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!isEnabled()) {
                g2.setColor(GRAY_DISABLED);
            } else {
                g2.setColor(backgroundColor);
            }

            int arc = getHeight();
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class CircularProfileLabel extends JLabel {
        private Image image;
        private final int borderThickness = 4;

        public CircularProfileLabel(String imagePath, int size) {
            setPreferredSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            updateImage(imagePath);
        }

        public void updateImage(String imagePath) {
            if (imagePath == null || imagePath.isEmpty()) {
                image = null;
                repaint();
                return;
            }
            try {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    image = new ImageIcon(imagePath).getImage();
                } else {
                    java.net.URL imgURL = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                    if (imgURL != null) {
                        image = new ImageIcon(imgURL).getImage();
                    } else {
                        image = null;
                    }
                }
            } catch (Exception e) {
                image = null;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int size = getWidth();
            int diameter = size - (borderThickness * 2);

            g2.setColor(new Color(230, 230, 230));
            g2.fill(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter));

            if (image != null) {
                g2.setClip(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter));
                g2.drawImage(image, borderThickness, borderThickness, diameter, diameter, this);
                g2.setClip(null);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String text = "Χωρίς Φωτό";
                int textX = (size - fm.stringWidth(text)) / 2;
                int textY = ((size - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, textX, textY);
            }

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.draw(new Ellipse2D.Double(
                    borderThickness / 2.0,
                    borderThickness / 2.0,
                    size - borderThickness,
                    size - borderThickness
            ));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FinalProfile("eratokapourani@gmail.com").setVisible(true);
        });
    }
}
