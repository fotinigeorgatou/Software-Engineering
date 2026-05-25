import javax.swing.*;
import java.awt.*;

class PaymentScreen extends JPanel {
    private JLabel lblFinalHostAmount = new JLabel("", JLabel.CENTER);

    public PaymentScreen(PetBNBApp controller) {
        setLayout(new GridLayout(4, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Οθόνη Πληρωμής & Επιβεβαίωσης", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 128, 0));

        lblFinalHostAmount.setFont(new Font("Arial", Font.ITALIC, 14));
        JLabel lblNotify = new JLabel("Ο οικοδεσπότης ενημερώθηκε επιτυχώς.", JLabel.CENTER);

        JButton btnFinish = new JButton("Ολοκλήρωση");
        btnFinish.addActionListener(e -> controller.restartApp());

        add(lblTitle);
        add(lblFinalHostAmount);
        add(lblNotify);
        add(btnFinish);
    }

    public void updateAmountText(double amount) {
        lblFinalHostAmount.setText("Το ποσό των " + amount + "€ καταβλήθηκε στον Οικοδεσπότη!");
    }
}
