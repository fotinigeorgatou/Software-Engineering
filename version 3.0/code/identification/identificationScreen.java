import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class identificationScreen extends JFrame {

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);

    private user user;

    public identificationScreen(user user) {
        this.user = user;

        setTitle("petbnb");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 750);
        setLocationRelativeTo(null);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new IdentificationCard());

        add(wrapper);
    }

    class IdentificationCard extends JPanel {

        public IdentificationCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 620));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Ταυτοποίηση", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.PLAIN, 22));

            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 35, 0);
            add(title, gbc);

            JLabel text = new JLabel(
                    "<html><div style='text-align:center;'>"
                            + "Για να ολοκληρωθεί η ενεργοποίηση<br>"
                            + "του λογαριασμού, χρειάζεται<br>"
                            + "επιβεβαίωση ταυτότητας."
                            + "</div></html>",
                    SwingConstants.CENTER
            );

            text.setFont(new Font("SansSerif", Font.PLAIN, 16));

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 45, 0);
            add(text, gbc);

            RoundedButton button =
                    new RoundedButton("Ταυτοποίηση με φωτογραφία ");

            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setForeground(PURPLE);

            button.addActionListener(e -> {
                dispose();
                new photoSubmissionScreen(user).setVisible(true);
            });

            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            add(button, gbc);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

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

    class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setPreferredSize(new Dimension(260, 50));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

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
}