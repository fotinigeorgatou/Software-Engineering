import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UserProfile extends JFrame {

    private static final Color BG_DARK    = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK       = new Color(255, 60, 91);
    private static final Color PURPLE     = new Color(193, 163, 229);

    private String userName;
    private String currentUser;

    public UserProfile(String userName, String currentUser) {
        this.userName = userName;
        this.currentUser = currentUser;
        initUI();
    }

    private String getPhone(String name) {
        if (name.contains("Fwteinh"))   return "6980817958";
        if (name.contains("Vivi"))      return "6984248062";
        if (name.contains("Mairy"))     return "6947035114";
        if (name.contains("Kristian"))  return "6978760978";
        return "+30 69X XXX XXXX";
    }

    private String getPet(String name) {
        if (name.contains("Fwteinh"))  return "Κατοικίδιο: γάτα 🐱";
        if (name.contains("Vivi"))     return "Κατοικίδιο: σκύλος 🐶";
        if (name.contains("Kristian")) return "Κατοικίδιο: χελώνα 🐢";
        if (name.contains("Mairy"))    return "Κατοικίδιο: πτηνό 🐦";
        return "Κατοικίδιο: -";
    }

    private String getEmail(String name) {
        if (name.contains("Fwteinh"))  return "georgatou.fwteinh@petbnb.gr";
        if (name.contains("Vivi"))     return "katsanta.vivi@petbnb.gr";
        if (name.contains("Kristian")) return "senko.kristian@petbnb.gr";
        if (name.contains("Mairy"))    return "soukisian.mairy@petbnb.gr";
        return name.toLowerCase().replace(" ", ".") + "@petbnb.gr";
    }

    private void initUI() {
        setTitle("petbnb");
        setSize(450, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new ProfileCard());
        add(wrapper);
    }

    class ProfileCard extends JPanel {
        public ProfileCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 580));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Avatar
            JLabel avatar = new JLabel(String.valueOf(userName.charAt(0)).toUpperCase(), SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(PINK);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            avatar.setFont(new Font("SansSerif", Font.BOLD, 36));
            avatar.setForeground(PURPLE);
            avatar.setOpaque(false);
            avatar.setPreferredSize(new Dimension(80, 80));
            avatar.setMaximumSize(new Dimension(80, 80));

            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 15, 0);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            add(avatar, gbc);

            // Name
            JLabel nameLabel = new JLabel(userName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 5, 0);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(nameLabel, gbc);

            // Rating
            JLabel ratingLabel = new JLabel("⭐ 4.8 / 5.0 αξιολόγηση", SwingConstants.CENTER);
            ratingLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            ratingLabel.setForeground(new Color(100, 100, 100));
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 25, 0);
            add(ratingLabel, gbc);

            // Info rows
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(6, 0, 6, 0);

            gbc.gridy = 3; add(infoRow("📧", getEmail(userName)), gbc);
            gbc.gridy = 4; add(infoRow("📞", getPhone(userName)), gbc);
            gbc.gridy = 5; add(infoRow("🐾", getPet(userName)), gbc);

            // Chat button
            JButton chatBtn = new JButton("💬  Έναρξη Συνομιλίας") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(PINK);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            chatBtn.setForeground(PURPLE);
            chatBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
            chatBtn.setContentAreaFilled(false);
            chatBtn.setBorderPainted(false);
            chatBtn.setFocusPainted(false);
            chatBtn.setPreferredSize(new Dimension(300, 50));
            chatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chatBtn.addActionListener(e -> openCoversation());

            gbc.gridy = 6;
            gbc.insets = new Insets(30, 0, 0, 0);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            add(chatBtn, gbc);

            // Back button
            JButton backBtn = new JButton("← Πίσω") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(INPUT_GRAY);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            backBtn.setForeground(new Color(80, 80, 80));
            backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
            backBtn.setContentAreaFilled(false);
            backBtn.setBorderPainted(false);
            backBtn.setFocusPainted(false);
            backBtn.setPreferredSize(new Dimension(160, 44));
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> dispose());

            gbc.gridy = 7;
            gbc.insets = new Insets(10, 0, 0, 0);
            add(backBtn, gbc);
        }

        private JPanel infoRow(String icon, String text) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            row.setOpaque(false);
            JLabel i = new JLabel(icon);
            i.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
            JLabel t = new JLabel(text);
            t.setFont(new Font("SansSerif", Font.PLAIN, 14));
            t.setForeground(new Color(60, 60, 60));
            row.add(i); row.add(t);
            return row;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.dispose();
        }
    }

    public void displayProfile() { setVisible(true); }

    public void openCoversation() {
        ChatScreen chat = new ChatScreen(currentUser, userName);
        chat.setVisible(true);
        dispose();
    }
}
