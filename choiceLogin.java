import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class choicelogin extends JFrame {
    private String userEmail;

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private static final Color PURPLE = new Color(193, 163, 229);


    public choicelogin(String email) {

        this.userEmail = email;
        setTitle("petbnb");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 750);
        setLocationRelativeTo(null);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new LoginCard());

        add(wrapper);
    }

    class LoginCard extends JPanel {
        public LoginCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 620));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 1. Logo
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 20, 0);
            add(new LogoPanel(), gbc);

            // 2. Title
            JLabel title = new JLabel("Welcome to petbnb!", SwingConstants.CENTER);
            title.setFont(new Font("Blinker", Font.BOLD, 22));
            gbc.gridy = 1;
            gbc.insets = new Insets(10, 0, 5, 0);
            add(title, gbc);

            // 3. Subtitle
            JLabel subtitle = new JLabel("Continue as:", SwingConstants.CENTER);
            subtitle.setFont(new Font("Blinker", Font.BOLD, 18));
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 30, 0);
            add(subtitle, gbc);

            // 4. Buttons
// ΑΛΛΑΓΗ 1: Μικραίνουμε τα insets αριστερά/δεξιά (5) για να πλατύνουν
            gbc.insets = new Insets(10, 5, 10, 5);

            // ΑΛΛΑΓΗ 2: Μειώνουμε το εσωτερικό ύψος (ipady)
            gbc.ipady = 8;

            // Pet Owner Button
            gbc.gridy = 4;
            add(new RoundedButton("Pet Owner"), gbc);

            // Host Button
            gbc.gridy = 5;
            add(new RoundedButton("Host"), gbc);

            // Dual Button
            gbc.gridy = 6;
            add(new RoundedButton("Dual"), gbc);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            // Increased arc for the card itself (the white background)
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
            g2.dispose();
        }
    }

    //  CUSTOM BUTTON CLASS

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(PURPLE);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Ενεργοποίηση εξομάλυνσης για τέλειες καμπύλες
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Χρώμα κουμπιού
            g2.setColor(PINK);

            // Το μυστικό είναι εδώ: Χρησιμοποιούμε το getHeight() για τις γωνίες
            // ώστε να γίνει τελείως ημικυκλικό στις άκρες (pill shape)
            int arc = getHeight();
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    //  LOGO PANEL

    class LogoPanel extends JPanel {
        private Image logoImage;

        public LogoPanel() {
            setPreferredSize(new Dimension(220, 120));
            setOpaque(false);
            try {
                ImageIcon icon = new ImageIcon("petbnblogotran.png");
                logoImage = icon.getImage();
            } catch (Exception e) {
                System.out.println("Could not find petbnblogotran.png");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (logoImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = logoImage.getWidth(null);
                int imgHeight = logoImage.getHeight(null);

                double ratio = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                int newWidth = (int) (imgWidth * ratio);
                int newHeight = (int) (imgHeight * ratio);

                int x = (panelWidth - newWidth) / 2;
                int y = (panelHeight - newHeight) / 2;

                g2.drawImage(logoImage, x, y, newWidth, newHeight, null);
                g2.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new choicelogin("test@test.com").setVisible(true);        });
    }
}
