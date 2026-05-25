import javax.swing.*;
import java.awt.*;

public class PetBNBApp extends JFrame {

    // Αντικείμενα λογικής
    public RequestManager requestManager = new RequestManager();
    public DBManager dbManager = new DBManager();
    public Bank bank = new Bank();

    // Δεδομένα
    public String globalCardNumber = "";
    public String globalBalanceStatus = "";
    public double globalTotalAmount = 100.0;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private PaymentScreen paymentScreen;

    public PetBNBApp() {
        setTitle("PetBNB Application");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        paymentScreen = new PaymentScreen(this);

        mainPanel.add(new PetOwnerScreen(this), "PetOwnerScreen");
        mainPanel.add(new HostProfileScreen(this), "HostProfileScreen");
        mainPanel.add(new DeclineScreen(this), "DeclineScreen");
        mainPanel.add(new ReservationScreen(this), "ReservationScreen");
        mainPanel.add(new PaymentVerificationScreen(this), "PaymentVerificationScreen");
        mainPanel.add(paymentScreen, "PaymentScreen");

        add(mainPanel);
        navigateTo("PetOwnerScreen");
    }

    public void navigateTo(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void setFinalHostAmount(double amount) {
        paymentScreen.updateAmountText(amount);
    }

    public void restartApp() {
        new PetBNBApp().setVisible(true);
        this.dispose();
    }

    // ==========================================
    // Η MAIN ΜΕΘΟΔΟΣ - ΕΚΤΕΛΕΣΗ ΑΠΟ ΕΔΩ!
    // ==========================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetBNBApp app = new PetBNBApp();
            app.setVisible(true);
        });
    }
}




