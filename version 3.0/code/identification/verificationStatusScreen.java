import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class verificationStatusScreen extends JFrame {

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);

    private user user;
    private verificationRequest request;

    public verificationStatusScreen(user user, verificationRequest request) {
        this.user = user;
        this.request = request;

        setTitle("petbnb");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 750);
        setLocationRelativeTo(null);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new StatusCard());

        add(wrapper);
    }

    class StatusCard extends JPanel {

        private JLabel statusMessage;
        private RoundedButton retryButton;

        public StatusCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 620));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Κατάσταση Ταυτοποίησης", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.PLAIN, 20));

            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 35, 0);
            add(title, gbc);

            statusMessage = new JLabel(
                    "<html><div style='text-align:center;'>"
                            + "Η διαδικασία ταυτοποίησης<br>"
                            + "βρίσκεται σε εξέλιξη.<br><br>"
                            + "Κατάσταση: Pending<br><br>"
                            + "Το σύστημα ελέγχει το αίτημα..."
                            + "</div></html>",
                    SwingConstants.CENTER
            );

            statusMessage.setFont(new Font("SansSerif", Font.PLAIN, 17));

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 35, 0);
            add(statusMessage, gbc);

            retryButton = new RoundedButton("νέα υποβολή");
            retryButton.setFont(new Font("SansSerif", Font.BOLD, 15));
            retryButton.setForeground(PURPLE);
            retryButton.setVisible(false);

            retryButton.addActionListener(e -> {
                dispose();
                new photoSubmissionScreen(user).setVisible(true);
            });

            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(5, 0, 0, 0);
            add(retryButton, gbc);

            Timer timer = new Timer(2500, e -> systemReview());
            timer.setRepeats(false);
            timer.start();
        }

        private void systemReview() {
            boolean approved = decideSystemResult();

            if (approved) {
                DBManager.updateVerificationStatus(
                        request.getRequestId(),
                        "Approved",
                        null
                );

                statusMessage.setText(
                        "<html><div style='text-align:center;'>"
                                + "Η ταυτοποίηση ολοκληρώθηκε<br>"
                                + "επιτυχώς.<br><br>"
                                + "Ο λογαριασμός είναι πλέον<br>"
                                + "ταυτοποιημένος.<br><br>"
                                + "Κατάσταση: "
                                + request.getStatus().getStatus()
                                + "</div></html>"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Ο λογαριασμός ταυτοποιήθηκε επιτυχώς!"
                );

            } else {
                String reason = "Η ταυτοποίηση δεν ολοκληρώθηκε επιτυχώς.";

                DBManager.updateVerificationStatus(
                        request.getRequestId(),
                        "Rejected",
                        reason
                );

                statusMessage.setText(
                        "<html><div style='text-align:center;'>"
                                + "Η ταυτοποίηση δεν ολοκληρώθηκε<br>"
                                + "επιτυχώς.<br><br>"
                                + "Κατάσταση: "
                                + request.getStatus().getStatus()
                                + "<br><br>"
                                + "Το σύστημα ζητά νέα υποβολή<br>"
                                + "στοιχείων ταυτοποίησης."
                                + "</div></html>"
                );

                retryButton.setVisible(true);

                JOptionPane.showMessageDialog(
                        this,
                        "Το αίτημα ταυτοποίησης απορρίφθηκε.\n"
                                + "Παρακαλώ κάντε νέα υποβολή."
                );
            }
        }

        private boolean decideSystemResult() {
            Random random = new Random();

            return random.nextBoolean();
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

            setPreferredSize(new Dimension(220, 50));
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