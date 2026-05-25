import javax.swing.*;
import java.awt.*;
// --- ΟΘΟΝΗ 2: HostProfileScreen ---
class HostProfileScreen extends JPanel {
    public HostProfileScreen(PetBNBApp controller) {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Προφίλ Οικοδεσπότη (HostProfile)", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle);

        JLabel lblInfo = new JLabel("Οικοδεσπότης: Γιάννης Παπαδόπουλος | Κόστος: 30€ / ημέρα");
        add(lblInfo);

        JButton btnRequest = new JButton("1. Υποβολή Αιτήματος Κράτησης (request())");
        JLabel lblStatus = new JLabel("Κατάσταση Αιτήματος: Εκκρεμεί", JLabel.CENTER);
        lblStatus.setForeground(Color.BLUE);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnAccept = new JButton("Αποδοχή από Οικοδεσπότη");
        JButton btnDecline = new JButton("Απόρριψη από Οικοδεσπότη");
        btnAccept.setEnabled(false);
        btnDecline.setEnabled(false);
        actionPanel.add(btnAccept);
        actionPanel.add(btnDecline);

        btnRequest.addActionListener(e -> {
            btnRequest.setEnabled(false);
            btnAccept.setEnabled(true);
            btnDecline.setEnabled(true);
            lblStatus.setText("Κατάσταση: Ο οικοδεσπότης εξετάζει το αίτημα...");
        });

        btnAccept.addActionListener(e -> {
            String result = controller.requestManager.requestResults(true);
            if (result.equals("APPROVED")) {
                controller.navigateTo("ReservationScreen");
            }
        });

        btnDecline.addActionListener(e -> {
            controller.requestManager.requestResults(false);
            controller.navigateTo("DeclineScreen");
        });

        add(btnRequest);
        add(lblStatus);
        add(actionPanel);
    }
}