import javax.swing.*;

public class PetBnbIdentificationApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            user user = new user(
                    "U1",
                    "Maria Papadopoulou",
                    "maria@petbnb.com"
            );

            new notificationScreen(user).setVisible(true);
        });
    }
}