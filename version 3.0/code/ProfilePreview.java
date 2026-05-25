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
    private String currentProfilePicPath;
    private boolean isFinalized = false; // Flag για την κατάσταση οριστικοποίησης

    // Maps για τη δυναμική παρακολούθηση των inputs
    private Map<String, JTextField> fieldMap = new HashMap<>();
    private JComboBox<String> cbPreferences;
    private CircularProfileLabel profileImg;

    // Κουμπιά που θα ενεργοποιηθούν ΜΕΤΑ την οριστικοποίηση
    private RoundedButton hostButton;
    private RoundedButton ownerButton;
    private RoundedButton finalizeBtn;

    // --- Color Palette ---
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
            JOptionPane.showMessageDialog(this, "User not found!");
            dispose();
            return;
        }

        this.currentProfilePicPath = user.profilePicPath;

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

        // Center Profile Avatar View
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(252, 252, 252));
        centerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        // CLICKABLE AVATAR LOADER
        profileImg = new CircularProfileLabel(currentProfilePicPath, 140);
        profileImg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileImg.setToolTipText("Click to change profile picture");
        profileImg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isFinalized) return; // Απαγόρευση αλλαγής αν έχει γίνει οριστικοποίηση

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ProfilePreview.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    currentProfilePicPath = selectedFile.getAbsolutePath();
                    profileImg.updateImage(currentProfilePicPath);
                }
            }
        });

        JLabel username = new JLabel(user.name + " " + user.lastname);
        username.setFont(new Font("SansSerif", Font.BOLD, 20));
        username.setForeground(Color.BLACK);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setBorder(new EmptyBorder(10, 0, 10, 0));

        centerPanel.add(profileImg);
        centerPanel.add(username);

        // Role Indicator View Buttons (ΑΡΧΙΚΑ ΑΠΕΝΕΡΓΟΠΟΙΗΜΕΝΑ)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 10, 40));

        hostButton = new RoundedButton("Host Profile Available", PURPLE, BUTTON_PINK);
        ownerButton = new RoundedButton("Pet Owner Profile Active", PURPLE, BUTTON_PINK);

        // Απενεργοποίηση στην εκκίνηση
        hostButton.setEnabled(false);
        ownerButton.setEnabled(false);

        // Action Listeners για τα κουμπιά ρόλων
        hostButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ανακατεύθυνση στο Host Profile..."));
        ownerButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ανακατεύθυνση στο Pet Owner Profile..."));

        buttonPanel.add(hostButton);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(ownerButton);

        centerPanel.add(buttonPanel);
        contentContainer.add(centerPanel);

        ownerButton.addActionListener(e -> {
            new PetOwnerProfile(user.email).setVisible(true);
        });
        hostButton.addActionListener(e -> {
            new HostProfile(user.email).setVisible(true);
        });

        // --- EDITABLE INFORMATION GRID PANEL ---
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 12));
        infoPanel.setBackground(new Color(252, 252, 252));
        infoPanel.setBorder(new EmptyBorder(5, 20, 15, 20));

        infoPanel.add(createEditableInfoField("Name:", user.name));
        infoPanel.add(createEditableInfoField("Pets:", user.pets));
        infoPanel.add(createEditableInfoField("Lastname:", user.lastname));
        infoPanel.add(createEditableInfoField("Preferences:", user.preferences));
        infoPanel.add(createEditableInfoField("Age:", user.age));
        infoPanel.add(createEditableInfoField("Email:", user.email));
        infoPanel.add(createEditableInfoField("Location:", user.location));
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

        if (labelText.equals("Rating:")) {
            JLabel ratingLabel = new JLabel(valueText + " \u2605");
            ratingLabel.setForeground(Color.DARK_GRAY);
            ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            ratingLabel.setBorder(new EmptyBorder(0, 15, 0, 15));
            fieldBg.add(ratingLabel);
        }
        else if (labelText.equals("Preferences:")) {
            String[] options = {
                    "animal lover", "dog person", "cat person", "nemo lover",
                    "chef with a mouse in his hat", "stole jafar's parrot", "other"
            };
            cbPreferences = new JComboBox<>(options);
            cbPreferences.setFont(new Font("SansSerif", Font.BOLD, 12));
            cbPreferences.setBorder(new EmptyBorder(0, 10, 0, 10));
            cbPreferences.setOpaque(false);
            cbPreferences.setSelectedItem(valueText.toLowerCase());
            fieldBg.add(cbPreferences, BorderLayout.CENTER);
        }
        else {
            JTextField textInput = new JTextField(valueText);
            textInput.setForeground(Color.BLACK);
            textInput.setFont(new Font("SansSerif", Font.BOLD, 13));
            textInput.setBorder(new EmptyBorder(0, 15, 0, 15));
            textInput.setOpaque(false);
            textInput.setCaretColor(PINK_HEADER);

            fieldMap.put(labelText, textInput);
            fieldBg.add(textInput, BorderLayout.CENTER);
        }

        p.add(label, BorderLayout.NORTH);
        p.add(fieldBg, BorderLayout.CENTER);

        return p;
    }

    private void saveAndFinalizeProfile() {
        String updatedName = fieldMap.get("Name:").getText().trim();
        String updatedLastname = fieldMap.get("Lastname:").getText().trim();
        String updatedAge = fieldMap.get("Age:").getText().trim();
        String updatedEmail = fieldMap.get("Email:").getText().trim();
        String updatedPets = fieldMap.get("Pets:").getText().trim();
        String updatedLocation = fieldMap.get("Location:").getText().trim();
        String updatedPrefs = cbPreferences.getSelectedItem().toString();

        if (updatedName.isEmpty() || updatedLastname.isEmpty() || updatedEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields like Name, Lastname, and Email cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Αποθήκευση αλλαγών στο αντικείμενο user
        user.name = updatedName;
        user.lastname = updatedLastname;
        user.age = updatedAge;
        user.email = updatedEmail;
        user.pets = updatedPets;
        user.preferences = updatedPrefs;
        user.location = updatedLocation;
        user.profilePicPath = currentProfilePicPath;

        DatabaseManager.updateUser(user);

        // --- ΚΛΕΙΔΩΜΑ ΤΗΣ ΦΟΡΜΑΣ (Freeze UI) ---
        isFinalized = true;
        profileImg.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        profileImg.setToolTipText(null);

        // Απενεργοποίηση όλων των TextFields
        for (JTextField tf : fieldMap.values()) {
            tf.setEditable(false);
            tf.setForeground(Color.DARK_GRAY); // Αλλαγή χρώματος για ένδειξη read-only
        }
        // Απενεργοποίηση Dropdown
        cbPreferences.setEnabled(false);

        // Απενεργοποίηση του κουμπιού Οριστικοποίησης
        finalizeBtn.setEnabled(false);
        finalizeBtn.setText("Το προφίλ οριστικοποιήθηκε ✓");

        // --- ΕΝΕΡΓΟΠΟΙΗΣΗ ΤΩΝ ΚΟΥΜΠΙΩΝ ΡΟΛΩΝ ---
        hostButton.setEnabled(true);
        ownerButton.setEnabled(true);

        // Ενημερωτικό Μήνυμα Επιτυχίας
        JOptionPane.showMessageDialog(this,
                "Το τελικό προφίλ αποθηκεύτηκε επιτυχώς!\nΤώρα μπορείτε να περιηγηθείτε στα κουμπιά Host και Pet Owner.",
                "petbnb Σύστημα", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- REUSABLE DYNAMIC STYLED COMPONENTS ---

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
            // Δυναμική αλλαγή του κέρσορα ανάλογα με το αν είναι ενεργό το κουμπί
            if (enabled) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Αν το κουμπί είναι απενεργοποιημένο, δείξε απαλό γκρι χρώμα
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
            try {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    image = new ImageIcon(imagePath).getImage();
                } else {
                    image = new ImageIcon(getClass().getResource(imagePath)).getImage();
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
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                String text = isFinalized ? "Profile Photo" : "Edit Photo";
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

    // --- MOCK DATABASE CONTROLLERS ---
    public static class User {
        public String name, lastname, email, age, location, rating, preferences, pets, profilePicPath;
        public User(String n, String l, String e, String a, String loc, String r, String p, String pt, String pic) {
            this.name = n; this.lastname = l; this.email = e; this.age = a;
            this.location = loc; this.rating = r; this.preferences = p; this.pets = pt; this.profilePicPath = pic;
        }
    }

    public static class DatabaseManager {
        public static User getUser(String email) {
            return new User("Erato", "Kapourani", email, "24", "Athens, GR", "4.9", "stole jafar's parrot", "Rex (Dog), Luna (Cat)", "default_avatar.png");
        }
        public static void updateUser(User user) {
            System.out.println("User metadata written successfully into core database storage context.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfilePreview("eratokapourani@gmail.com").setVisible(true);
        });
    }
}
