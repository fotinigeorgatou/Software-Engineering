import javax.swing.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            DBManager dbManager = new DBManager();

            Accommodation accommodation = new Accommodation(
                    "A1",
                    LocalDate.now().minusDays(7),
                    LocalDate.now().minusDays(1),
                    true,
                    "cat home",
                    "Apartment",
                    "cats",
                    "Two cats",
                    "Cat bed, toys, cat food",
                    "images/apartment.png",
                    4.9
            );

            Host host = new Host(
                    "U1",
                    "Ερατώ",
                    "host@example.com",
                    accommodation
            );

            Pet pet = new Pet(
                    "P1",
                    "Simba",
                    "1 year old",
                    "cat",
                    "Ο Simba είναι μία γάτα γεμάτη ενέργεια, φιλική με άλλες γάτες και λατρεύει το φαγητό.",
                    "images/cat.png"
            );

            PetOwner petOwner = new PetOwner(
                    "U2",
                    "Μαρία",
                    "petowner@example.com",
                    pet
            );

            String[] options = {"Ιδιοκτήτης κατοικιδίου", "Οικοδεσπότης"};

            String selectedRole = (String) JOptionPane.showInputDialog(
                    null,
                    "Επιλέξτε με ποιον ρόλο θέλετε να συνδεθείτε:",
                    "Σύνδεση χρήστη",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selectedRole == null) {
                return;
            }

            User currentUser;
            User reviewee;

            if (selectedRole.equals("Ιδιοκτήτης κατοικιδίου")) {
                currentUser = petOwner;
                reviewee = host;
            } else {
                currentUser = host;
                reviewee = petOwner;
            }

            NotificationScreen notificationScreen = new NotificationScreen(
                    "N1",
                    currentUser,
                    reviewee,
                    accommodation,
                    dbManager
            );

            notificationScreen.setVisible(true);
        });
    }
}