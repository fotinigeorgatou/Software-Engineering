import javax.swing.*;
import java.awt.*;

// --- ΟΘΟΝΗ 3: DeclineScreen ---
class DeclineScreen extends JPanel {
    public DeclineScreen(PetBNBApp controller) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblDecline = new JLabel("Η κράτηση δεν έγινε αποδεκτή!", JLabel.CENTER);
        lblDecline.setFont(new Font("Arial", Font.BOLD, 18));
        lblDecline.setForeground(Color.RED);
        add(lblDecline, BorderLayout.CENTER);

        JButton btnBack = new JButton("<- Επιστροφή στο Αρχικό Προφίλ");
        btnBack.addActionListener(e -> controller.restartApp());
        add(btnBack, BorderLayout.SOUTH);
    }
}
