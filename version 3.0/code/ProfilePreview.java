import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProfilePreview extends JFrame {

    private User user;
    private String currentProfilePicPath = "";
    private boolean isFinalized = false;

    private Map<String, JTextField> fieldMap = new HashMap<>();
    private JComboBox<String> cbPreferences;
    private CircularProfileLabel profileImg;

    private RoundedButton hostButton;
    private RoundedButton ownerButton;
    private RoundedButton finalizeBtn;

    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(245, 245, 245);
    private static final Color BUTTON_PINK = new Color(255, 110, 140);
    private static final Color LABEL_PINK = new Color(255, 160, 180);
    private static final Color FIELD_GRAY = new Color(225, 225, 225);
    private static final Color PURPLE = new Color(193, 163, 229);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color GRAY_DISABLED = new Color(180, 180, 180);

    public ProfilePreview(String email) {
        user = DatabaseManager.getUser(email);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Ο χρήστης δεν βρέθηκε στη βάση δεδομένων!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // ΦΟΡΤΩΣΗ ΕΙΚΟΝΑΣ ΑΠΟ ΒΑΣΗ
        this.currentProfilePicPath = (user.profilePicPath != null && !user.profilePicPath.trim().isEmpty()) ? user.profilePicPath : "profileimage.jpg";

        setTitle("Profile Preview & Edit");
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
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        profileImg = new CircularProfileLabel(currentProfilePicPath, 140);
        profileImg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileImg.setToolTipText("Κάντε κλικ για εισαγωγή φωτογραφίας προφίλ");
        profileImg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isFinalized) return;

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ProfilePreview.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    currentProfilePicPath = selectedFile.getAbsolutePath();
                    profileImg.updateImage(currentProfilePicPath);
                }
            }
        });

        String displayName = (user.name != null && !user.name.equals("NewUser")) ? (user.name + " " + user.lastname) : "Συμπλήρωση Νέου Προφίλ";
        JLabel username = new JLabel(displayName);
        username.setFont(new Font("SansSerif", Font.BOLD, 18));
        username.setForeground(Color.GRAY);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setBorder(new EmptyBorder(10, 0, 10, 0));

        centerPanel.add(profileImg);
        centerPanel.add(username);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 10, 40));

        hostButton = new RoundedButton("Host Profile Available", PURPLE, BUTTON_PINK);
        ownerButton = new RoundedButton("Pet Owner Profile Active", PURPLE, BUTTON_PINK);

        hostButton.setEnabled(false);
        ownerButton.setEnabled(false);

        ownerButton.addActionListener(e -> new PetOwnerProfile(user.email).setVisible(true));
        hostButton.addActionListener(e -> new HostProfile(user.email).setVisible(true));

        buttonPanel.add(hostButton);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(ownerButton);

        centerPanel.add(buttonPanel);
        contentContainer.add(centerPanel);

        // --- EDITABLE INFORMATION GRID PANEL (ΦΟΡΤΩΣΗ ΑΠΟ ΒΑΣΗ) ---
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 12));
        infoPanel.setBackground(new Color(252, 252, 252));
        infoPanel.setBorder(new EmptyBorder(5, 20, 15, 20));

        infoPanel.add(createEditableInfoField("Name:", user.name.equals("NewUser") ? "" : user.name));
        infoPanel.add(createEditableInfoField("Pets:", user.pets));
        infoPanel.add(createEditableInfoField("Lastname:", user.lastname.equals("Lastname") ? "" : user.lastname));
        infoPanel.add(createEditableInfoField("Preferences:", user.preferences));
        infoPanel.add(createEditableInfoField("Age:", user.age));
        infoPanel.add(createEditableInfoField("Email:", user.email));
        infoPanel.add(createEditableInfoField("Location:", user.location.equals("Athens") ? "" : user.location));
        infoPanel.add(createEditableInfoField("Rating:", user.rating));

        contentContainer.add(infoPanel);

        // --- FINALIZATION BAR ---
        JPanel finalizationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        finalizationPanel.setBackground(new Color(252, 252, 252));
        finalizationPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        finalizeBtn = new RoundedButton("Οριστικοποίηση & Αποθήκευση", Color.WHITE, SUCCESS_GREEN);
        finalizeBtn.setPreferredSize(new Dimension(340, 45));
        finalizeBtn.setMaximumSize(new Dimension(340, 45));
        finalizeBtn.addActionListener(e -> saveAndFinalizeProfile());
        finalizationPanel.add(finalizeBtn);

        contentContainer.add(finalizationPanel);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
        add(mainPanel);
    }

    public ProfilePreview(String email, boolean startAsFinalized) {
        this(email);
        if (startAsFinalized) {
            freezeUI();
        }
    }

    private void freezeUI() {
        this.isFinalized = true;
        profileImg.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        profileImg.setToolTipText(null);

        for (JTextField tf : fieldMap.values()) {
            tf.setEditable(false);
            tf.setForeground(Color.DARK_GRAY);
        }
        if (cbPreferences != null) {
            cbPreferences.setEnabled(false);
        }

        finalizeBtn.setEnabled(false);
        finalizeBtn.setText("Το προφίλ οριστικοποιήθηκε ✓");

        if (user != null && user.role != null) {
            hostButton.setEnabled(user.role.equalsIgnoreCase("Host") || user.role.equalsIgnoreCase("Dual"));
            ownerButton.setEnabled(user.role.equalsIgnoreCase("Pet Owner") || user.role.equalsIgnoreCase("Dual"));
        }
    }

    private JPanel createEditableInfoField(String labelText, String valueText) {
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
            String[] options = {
                    "- Επιλέξτε -", "animal lover", "dog person", "cat person", "nemo lover",
                    "chef with a mouse in his hat", "stole jafar's parrot", "other"
            };
            cbPreferences = new JComboBox<>(options);
            cbPreferences.setFont(new Font("SansSerif", Font.BOLD, 12));
            cbPreferences.setBorder(new EmptyBorder(0, 10, 0, 10));
            cbPreferences.setOpaque(false);

            for (String option : options) {
                if (option.equalsIgnoreCase(safeValue)) {
                    cbPreferences.setSelectedItem(option);
                    break;
                }
            }
            fieldBg.add(cbPreferences, BorderLayout.CENTER);
        }
        else {
            JTextField textInput = new JTextField(safeValue);
            textInput.setForeground(Color.BLACK);
            textInput.setFont(new Font("SansSerif", Font.BOLD, 13));
            textInput.setBorder(new EmptyBorder(0, 15, 0, 15));
            textInput.setOpaque(false);
            textInput.setCaretColor(PINK_HEADER);

            if (labelText.equals("Email:")) {
                textInput.setEditable(false);
                textInput.setForeground(Color.DARK_GRAY);
            }

            fieldMap.put(labelText, textInput);
            fieldBg.add(textInput, BorderLayout.CENTER);
        }

        p.add(label, BorderLayout.NORTH);
        p.add(fieldBg, BorderLayout.CENTER);

        return p;
    }

    private void saveAndFinalizeProfile() {
        String updatedName = fieldMap.get("Name:") != null ? fieldMap.get("Name:").getText().trim() : "";
        String updatedLastname = fieldMap.get("Lastname:") != null ? fieldMap.get("Lastname:").getText().trim() : "";
        String updatedAge = fieldMap.get("Age:") != null ? fieldMap.get("Age:").getText().trim() : "";
        String updatedEmail = fieldMap.get("Email:") != null ? fieldMap.get("Email:").getText().trim() : "";
        String updatedPets = fieldMap.get("Pets:") != null ? fieldMap.get("Pets:").getText().trim() : "";
        String updatedLocation = fieldMap.get("Location:") != null ? fieldMap.get("Location:").getText().trim() : "";
        String updatedPrefs = cbPreferences != null ? cbPreferences.getSelectedItem().toString() : "- Επιλέξτε -";

        if (currentProfilePicPath.isEmpty() || currentProfilePicPath.equals("profileimage.jpg")) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Πρέπει να επιλέξετε μια φωτογραφία προφίλ!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (updatedName.isEmpty() || updatedLastname.isEmpty() || updatedAge.isEmpty() || updatedPets.isEmpty() || updatedLocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Όλα τα πεδία κειμένου πρέπει να συμπληρωθούν!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (updatedPrefs.equals("- Επιλέξτε -")) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Παρακαλώ επιλέξτε μια προτίμηση από τη λίστα!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.name = updatedName;
        user.lastname = updatedLastname;
        user.age = updatedAge;
        user.email = updatedEmail;
        user.pets = updatedPets;
        user.preferences = updatedPrefs;
        user.location = updatedLocation;
        user.profilePicPath = currentProfilePicPath;

        DatabaseManager.updateUser(user);
        freezeUI();

        JOptionPane.showMessageDialog(this,
                "Μήνυμα Επιβεβαίωσης: Το βασικό προφίλ αποθηκεύτηκε επιτυχώς!",
                "petbnb Σύστημα", JOptionPane.INFORMATION_MESSAGE);

        this.dispose();

        // --- ΕΛΕΓΧΟΣ ΡΟΗΣ ΒΑΣΕΙ ΤΟΥ user.role ΠΟΥ ΕΠΕΛΕΞΕ ΣΤΟ CHOICELOGIN ---
        if (user.role != null && user.role.equalsIgnoreCase("Dual")) {
            // 3ο Ενδεχόμενο: Μετά το ProfilePreview ανοίγει το PetOwnerProfile για οριστικοποίηση
            JOptionPane.showMessageDialog(null, "Ροή Dual: Ανακατεύθυνση στο Pet Owner Profile για οριστικοποίηση.");
            new PetOwnerProfile(user.email).setVisible(true);
        }
        else if (user.role != null && user.role.equalsIgnoreCase("Host")) {
            // 2ο Ενδεχόμενο: Μετά το ProfilePreview ανοίγει το HostProfile για οριστικοποίηση
            JOptionPane.showMessageDialog(null, "Ροή Host: Ανακατεύθυνση στο Host Profile για οριστικοποίηση.");
            new HostProfile(user.email).setVisible(true);
        }
        else {
            // 1ο Ενδεχόμενο: Μετά το ProfilePreview ανοίγει το Pet Owner Profile για οριστικοποίηση
            JOptionPane.showMessageDialog(null, "Ροή Pet Owner: Ανακατεύθυνση στο Pet Owner Profile για οριστικοποίηση.");
            new PetOwnerProfile(user.email).setVisible(true);
        }
    }

    // --- REUSABLE COMPONENTS ---
    class RoundedButton extends JButton {
        private Color backgroundColor;
        public RoundedButton(String text, Color textColor, Color bgColor) {
            super(text);
            this.backgroundColor = bgColor;
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setForeground(textColor); fontSet();
        }
        private void fontSet() { setFont(new Font("SansSerif", Font.BOLD, 15)); setCursor(new Cursor(Cursor.HAND_CURSOR)); setAlignmentX(Component.CENTER_ALIGNMENT); setMaximumSize(new Dimension(320, 45)); }
        @Override public void setEnabled(boolean enabled) { super.setEnabled(enabled); setCursor(new Cursor(enabled ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR)); repaint(); }
        @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(isEnabled() ? backgroundColor : GRAY_DISABLED); int arc = getHeight(); g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc)); g2.dispose(); super.paintComponent(g); }
    }

    class CircularProfileLabel extends JLabel {
        private Image image; private final int borderThickness = 4;
        public CircularProfileLabel(String imagePath, int size) { setPreferredSize(new Dimension(size, size)); setMaximumSize(new Dimension(size, size)); setMinimumSize(new Dimension(size, size)); setAlignmentX(Component.CENTER_ALIGNMENT); updateImage(imagePath); }
        public void updateImage(String imagePath) { if (imagePath == null || imagePath.isEmpty() || imagePath.equals("profileimage.jpg")) { image = null; repaint(); return; } try { File imgFile = new File(imagePath); if (imgFile.exists()) { image = new ImageIcon(imagePath).getImage(); } else { image = new ImageIcon(getClass().getResource(imagePath)).getImage(); } } catch (Exception e) { image = null; } repaint(); }
        @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); int size = getWidth(); int diameter = size - (borderThickness * 2); g2.setColor(new Color(230, 230, 230)); g2.fill(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter)); if (image != null) { g2.setClip(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter)); g2.drawImage(image, borderThickness, borderThickness, diameter, diameter, this); g2.setClip(null); } else { g2.setColor(Color.DARK_GRAY); g2.setFont(new Font("SansSerif", Font.BOLD, 13)); FontMetrics fm = g2.getFontMetrics(); String text = "Προσθήκη Φωτό"; int textX = (size - fm.stringWidth(text)) / 2; int textY = ((size - fm.getHeight()) / 2) + fm.getAscent(); g2.drawString(text, textX, textY); } g2.setColor(Color.WHITE); g2.setStroke(new BasicStroke(borderThickness)); g2.draw(new Ellipse2D.Double(borderThickness / 2.0, borderThickness / 2.0, size - borderThickness, size - borderThickness)); g2.dispose(); }
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new ProfilePreview("eratokapourani@gmail.com", false).setVisible(true)); }
}
