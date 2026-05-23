import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class ProfilePreview extends JFrame {

    private User user;

    // --- Color Palette ---
    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(245, 245, 245);
    private static final Color BUTTON_PINK = new Color(255, 110, 140);
    private static final Color LABEL_PINK = new Color(255, 160, 180);
    private static final Color FIELD_GRAY = new Color(225, 225, 225);
    private static final Color PURPLE = new Color(193, 163, 229);


    public ProfilePreview(String email) {

        user = DatabaseManager.getUser(email);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found!");
            dispose();
            return;
        }

        setTitle("Profile Preview");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 750);
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

        // --- CENTER PANEL (Uniform Container) ---
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(new Color(252, 252, 252));

        // Center Profile View
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(252, 252, 252));
        centerPanel.setBorder(new EmptyBorder(25, 0, 10, 0));
        centerPanel.setOpaque(true);

        // DYNAMIC PROFILE IMAGE LOADER
        CircularProfileLabel profileImg = new CircularProfileLabel(user.profilePicPath, 150);

        JLabel username = new JLabel(user.name + " " + user.lastname);
        username.setFont(new Font("SansSerif", Font.BOLD, 22));
        username.setForeground(Color.BLACK);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setBorder(new EmptyBorder(12, 0, 15, 0));

        centerPanel.add(profileImg);
        centerPanel.add(username);

        // Action Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 40, 15, 40));

        RoundedButton hostButton = new RoundedButton("Host Profile", PURPLE);
        RoundedButton ownerButton = new RoundedButton("Pet Owner Profile", PURPLE);

        buttonPanel.add(hostButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(ownerButton);

        centerPanel.add(buttonPanel);
        contentContainer.add(centerPanel);

        // --- GRID INFORMATION PANEL ---
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 20, 15));
        infoPanel.setBackground(new Color(252, 252, 252));
        infoPanel.setBorder(new EmptyBorder(10, 25, 25, 25));

        infoPanel.add(createInfoField("Name:", user.name));
        infoPanel.add(createInfoField("Pets:", user.pets));
        infoPanel.add(createInfoField("Lastname:", user.lastname));
        infoPanel.add(createInfoField("Preferences:", user.preferences));
        infoPanel.add(createInfoField("Age:", user.age));
        infoPanel.add(createInfoField("Email:", user.email));
        infoPanel.add(createInfoField("Location:", user.location));
        infoPanel.add(createInfoField("Rating:", user.rating));

        contentContainer.add(infoPanel);

        mainPanel.add(contentContainer, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createInfoField(String labelText, String valueText) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(LABEL_PINK);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

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
        fieldBg.setPreferredSize(new Dimension(180, 35));

        JLabel value = new JLabel(valueText);
        value.setForeground(Color.BLACK);
        value.setFont(new Font("SansSerif", Font.BOLD, 13));
        value.setBorder(new EmptyBorder(0, 15, 0, 15));

        if (labelText.equals("Rating:")) {
            String starChar = " \u2605";
            value.setText(user.rating + starChar);
        }

        fieldBg.add(value);
        p.add(label, BorderLayout.NORTH);
        p.add(fieldBg, BorderLayout.CENTER);

        return p;
    }

    class RoundedButton extends JButton {
        // Added a textColor parameter to the constructor
        public RoundedButton(String text, Color textColor) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setForeground(textColor); // Set your custom text color here!
            setFont(new Font("SansSerif", Font.BOLD, 17));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setMaximumSize(new Dimension(320, 45));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BUTTON_PINK); // Keeps the background pink
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

            try {
                ImageIcon icon = new ImageIcon(imagePath);
                image = icon.getImage();
            } catch (Exception e) {
                System.out.println("Profile image file missing.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int size = getWidth();
            int diameter = size - (borderThickness * 2);

            g2.setColor(new Color(235, 235, 235));
            g2.fill(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter));

            if (image != null && image.getWidth(null) > 0) {
                g2.setClip(new Ellipse2D.Double(borderThickness, borderThickness, diameter, diameter));
                g2.drawImage(image, borderThickness, borderThickness, diameter, diameter, this);
                g2.setClip(null);
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
            new ProfilePreview("eratokapourani@gmail.com").setVisible(true);
        });
    }
}
