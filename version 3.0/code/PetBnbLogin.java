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

                // LOGO
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 0, 40, 0);
                add(new LogoPanel(), gbc);

                // TITLE
                JLabel title = new JLabel("Log In to petbnb", SwingConstants.CENTER);
                title.setFont(new Font("SansSerif", Font.PLAIN, 18));

                gbc.gridy = 1;
                gbc.insets = new Insets(0, 0, 30, 0);

                add(title, gbc);

                // EMAIL FIELD
                JTextField emailField =
                        createRoundedTextField("email address");

                gbc.gridy = 2;
                gbc.insets = new Insets(0, 0, 15, 0);

                add(emailField, gbc);

                // PASSWORD FIELD
                JPasswordField passField =
                        createRoundedPasswordField("password");

                gbc.gridy = 3;
                gbc.insets = new Insets(0, 0, 30, 0);

                add(passField, gbc);

                // LOGIN BUTTON
                RoundedButton loginButton = new RoundedButton("log in");
                loginButton.setFont(new Font("", Font.BOLD, 19));
                loginButton.setForeground(PURPLE);

                loginButton.addActionListener(e -> {
                    String email = emailField.getText().trim();
                    String password = new String(passField.getPassword());

                    // VALIDATION
                    if(email.isEmpty()
                            || email.equals("email address")
                            || password.isEmpty()
                            || password.equals("password")) {

                        JOptionPane.showMessageDialog(this, "Please fill all fields.");
                        return;
                    }

                    // LOGIN
                    String result = DatabaseManager.loginOrRegister(email, password);

                    if(result.equals("LOGIN_SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Welcome back!");
                        PetBnbLogin.this.dispose();

                        // CHANGED HERE: Open ProfilePreview for existing accounts
                        new ProfilePreview(email).setVisible(true);

                    }
                    else if(result.equals("REGISTER_SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Account created successfully!");
                        PetBnbLogin.this.dispose();

                        // Keeps choicelogin for new registrations so they can choose their initial role
                        new choicelogin(email).setVisible(true);

                    }
                    else if(result.equals("WRONG_PASSWORD")) {
                        JOptionPane.showMessageDialog(this, "Wrong password.");
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Database error.");
                    }
                });

                gbc.gridy = 4;
                gbc.fill = GridBagConstraints.NONE;
                gbc.insets = new Insets(10, 0, 0, 0);

                add(loginButton, gbc);
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(CARD_WHITE);

                g2.fill(
                        new RoundRectangle2D.Float(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                40,
                                40
                        )
                );

                g2.dispose();
            }
        }

        // TEXT FIELD
        private JTextField createRoundedTextField(String placeholder) {

            JTextField field = new JTextField(placeholder) {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 =
                            (Graphics2D) g.create();

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    g2.setColor(INPUT_GRAY);

                    g2.fill(
                            new RoundRectangle2D.Float(
                                    0,
                                    0,
                                    getWidth(),
                                    getHeight(),
                                    getHeight(),
                                    getHeight()
                            )
                    );

                    g2.dispose();

                    super.paintComponent(g);
                }
            };

            styleField(field, placeholder);

            return field;
        }

        // PASSWORD FIELD
        private JPasswordField createRoundedPasswordField(
                String placeholder) {

            JPasswordField field =
                    new JPasswordField(placeholder) {

                        @Override
                        protected void paintComponent(Graphics g) {

                            Graphics2D g2 =
                                    (Graphics2D) g.create();

                            g2.setRenderingHint(
                                    RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON
                            );

                            g2.setColor(INPUT_GRAY);

                            g2.fill(
                                    new RoundRectangle2D.Float(
                                            0,
                                            0,
                                            getWidth(),
                                            getHeight(),
                                            getHeight(),
                                            getHeight()
                                    )
                            );

                            g2.dispose();

                            super.paintComponent(g);
                        }
                    };

            field.setEchoChar((char) 0);

            styleField(field, placeholder);

            return field;
        }

        // FIELD STYLE
        private void styleField(
                JTextField field,
                String placeholder) {

            field.setOpaque(false);

            field.setBorder(
                    new EmptyBorder(10, 25, 10, 25));

            field.setHorizontalAlignment(
                    JTextField.CENTER);

            field.setFont(
                    new Font("SansSerif", Font.BOLD, 14));

            field.setForeground(PLACEHOLDER_COLOR);

            field.setPreferredSize(
                    new Dimension(300, 50));

            field.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {

                    if(field.getText().equals(placeholder)) {

                        field.setText("");

                        field.setForeground(Color.BLACK);

                        if(field instanceof JPasswordField) {

                            ((JPasswordField) field)
                                    .setEchoChar('•');
                        }
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {

                    if(field.getText().isEmpty()) {

                        field.setText(placeholder);

                        field.setForeground(
                                PLACEHOLDER_COLOR);

                        if(field instanceof JPasswordField) {

                            ((JPasswordField) field)
                                    .setEchoChar((char) 0);
                        }
                    }
                }
            });
        }

        // BUTTON
        class RoundedButton extends JButton {

            public RoundedButton(String text) {

                super(text);

                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);

                setPreferredSize(
                        new Dimension(160, 50));

                setCursor(
                        new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(PINK);

                g2.fill(
                        new RoundRectangle2D.Float(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                getHeight(),
                                getHeight()
                        )
                );

                g2.dispose();

                super.paintComponent(g);
            }
        }

        // LOGO PANEL
        class LogoPanel extends JPanel {

            private Image logoImage;

            public LogoPanel() {

                setPreferredSize(
                        new Dimension(220, 120));

                setOpaque(false);

                try {

                    ImageIcon icon =
                            new ImageIcon("petbnblogotran.png");

                    logoImage = icon.getImage();

                } catch (Exception e) {

                    System.out.println("Logo not found.");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                if (logoImage != null) {

                    Graphics2D g2 =
                            (Graphics2D) g.create();

                    g2.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    g2.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR
                    );

                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    int imgWidth =
                            logoImage.getWidth(null);

                    int imgHeight =
                            logoImage.getHeight(null);

                    double ratio = Math.min(
                            (double) panelWidth / imgWidth,
                            (double) panelHeight / imgHeight
                    );

                    int newWidth =
                            (int) (imgWidth * ratio);

                    int newHeight =
                            (int) (imgHeight * ratio);

                    int x =
                            (panelWidth - newWidth) / 2;

                    int y =
                            (panelHeight - newHeight) / 2;

                    g2.drawImage(
                            logoImage,
                            x,
                            y,
                            newWidth,
                            newHeight,
                            null
                    );

                    g2.dispose();
                }
            }
        }

        public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {

                new PetBnbLogin().setVisible(true);

            });
        }
    }
