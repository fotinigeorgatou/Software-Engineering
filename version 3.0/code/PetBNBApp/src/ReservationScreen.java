import javax.swing.*;
import java.awt.*;

class ReservationScreen extends JPanel {
    public ReservationScreen(PetBNBApp controller) {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Οθόνη Κράτησης (ReservationScreen)", JLabel.LEFT));
        add(new JLabel("Ποσό Πληρωμής: " + controller.globalTotalAmount + "€"));

        add(new JLabel("Αριθμός Κάρτας:"));
        JTextField txtCard = new JTextField();
        add(txtCard);

        add(new JLabel("Υπόλοιπο (Γράψτε '0' ή 'error' για αποτυχία):"));
        JTextField txtBalance = new JTextField("OK");
        add(txtBalance);

        JButton btnCancel = new JButton("Ακύρωση (Αλλαγή σχεδίων)");
        JButton btnSubmit = new JButton("Έλεγχος & Επικύρωση");

        btnCancel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Η πληρωμή ακυρώθηκε. Επιστροφή στο προφίλ.");
            controller.navigateTo("PetOwnerScreen");
        });

        btnSubmit.addActionListener(e -> {
            controller.globalCardNumber = txtCard.getText();
            controller.globalBalanceStatus = txtBalance.getText();

            boolean bankApproved = controller.bank.checkData(controller.globalCardNumber, controller.globalBalanceStatus);

            if (!bankApproved) {
                JOptionPane.showMessageDialog(this, "Απόρριψη Συναλλαγής: Ανεπαρκές υπόλοιπο!", "Σφάλμα Τράπεζας", JOptionPane.ERROR_MESSAGE);
            } else {
                controller.navigateTo("PaymentVerificationScreen");
            }
        });

        add(btnCancel);
        add(btnSubmit);
    }
}