import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class PetBnbLogin extends JFrame {

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);

    public PetBnbLogin() {
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
            gbc.insets = new Insets(0, 0, 40, 0);
            add(new LogoPanel(), gbc);

            // 2. Title
            JLabel title = new JLabel("Log In to petbnb", SwingConstants.CENTER);
            title.setFont(new Font("Blinker", Font.PLAIN, 18));
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 30, 0);
            add(title, gbc);

            // 3. Email Field
            JTextField emailField = createRoundedTextField("email address");
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 15, 0);
            add(emailField, gbc);

            // 4. Password Field 
            JPasswordField passField = createRoundedPasswordField("password");
            gbc.gridy = 3;
            gbc.insets = new Insets(0, 0, 30, 0);
            add(passField, gbc);

            // 5. Button
            RoundedButton loginButton = new RoundedButton("log in");

            
            loginButton.setFont(new Font("Blinker", Font.BOLD, 19 ));
            loginButton.setForeground(PURPLE);

            add(loginButton, gbc);
            gbc.gridy = 4;
            gbc.fill = GridBagConstraints.NONE;
            add(loginButton, gbc);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.dispose();
        }
    }

    // --- CUSTOM FIELD CREATORS ---

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleField(field, placeholder);
        return field;
    }

    private JPasswordField createRoundedPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setEchoChar((char) 0); // Initially show text so we see "password"
        styleField(field, placeholder);
        return field;
    }

    private void styleField(JTextField field, String placeholder) {
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(10, 20, 10, 20));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(PLACEHOLDER_COLOR);
        field.setPreferredSize(new Dimension(300, 50));

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    // REUSED UI CLASSES

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setPreferredSize(new Dimension(140, 45));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PINK);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 45, 45));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class LogoPanel extends JPanel {
        private Image logoImage;

        public LogoPanel() {
            setPreferredSize(new Dimension(220, 120)); // Adjust to match your logo shape
            setOpaque(false);


            try {
                ImageIcon icon = new ImageIcon("petbnblogotran.png");
                logoImage = icon.getImage();
            } catch (Exception e) {
                System.out.println("Could not find logo.png");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (logoImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Calculate scaling to fit the panel while maintaining aspect ratio
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = logoImage.getWidth(null);
                int imgHeight = logoImage.getHeight(null);

                double ratio = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                int newWidth = (int) (imgWidth * ratio);
                int newHeight = (int) (imgHeight * ratio);

                // Center the image
                int x = (panelWidth - newWidth) / 2;
                int y = (panelHeight - newHeight) / 2;

                g2.drawImage(logoImage, x, y, newWidth, newHeight, null);
                g2.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetBnbLogin().setVisible(true));
    }
}
