import javax.swing.*;
import java.awt.*;
class PaymentVerificationScreen extends JPanel {
    public PaymentVerificationScreen(PetBNBApp controller) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Οθόνη Επαλήθευσης Πληρωμής (PaymentVerificationScreen)", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(41, 128, 185));
        add(lblTitle, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Στοιχεία προς Επικύρωση"));

        detailsPanel.add(new JLabel("  Οικοδεσπότης ID: 101"));
        detailsPanel.add(new JLabel("  Κατοικίδιο ID: 55"));
        detailsPanel.add(new JLabel("  Ημερομηνίες: 12/06 έως 15/06"));
        detailsPanel.add(new JLabel("  Συνολικό Ποσό Χρέωσης: " + controller.globalTotalAmount + " €"));

        JLabel lblBankStatus = new JLabel("  Κατάσταση Τράπεζας: ΕΓΚΡΙΘΗΚΕ");
        lblBankStatus.setForeground(new Color(39, 174, 96));
        lblBankStatus.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(lblBankStatus);
        add(detailsPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        JButton btnCancel = new JButton("Ακύρωση Συναλλαγής");
        JButton btnConfirm = new JButton("Επιβεβαίωση & Πληρωμή");

        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);

        btnCancel.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Ακύρωση διαδικασίας;", "Ακύρωση", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                controller.navigateTo("PetOwnerScreen");
            }
        });

        btnConfirm.addActionListener(e -> {
            boolean isSaved = controller.dbManager.confirmReservation(101, 55, "12/06 - 15/06", controller.globalTotalAmount);
            if (isSaved) {
                double hostAmount = controller.dbManager.calcHostPayment(controller.globalTotalAmount);
                double commission = controller.globalTotalAmount - hostAmount;

                JOptionPane.showMessageDialog(this, "Η κράτηση καταχωρήθηκε στη ΒΔ!\nΠρομήθεια: " + commission + " €");
                controller.setFinalHostAmount(hostAmount);
                controller.navigateTo("PaymentScreen");
            }
        });

        actionPanel.add(btnCancel);
        actionPanel.add(btnConfirm);
        add(actionPanel, BorderLayout.SOUTH);
    }
}