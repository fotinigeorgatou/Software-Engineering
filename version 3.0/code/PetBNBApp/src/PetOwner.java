import javax.swing.*;
import java.awt.*;

// --- ΟΘΟΝΗ 1: PetOwnerScreen ---
class PetOwnerScreen extends JPanel {
    public PetOwnerScreen(PetBNBApp controller) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Προφίλ Ιδιοκτήτη Ζώου (PetOwner)", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea("Καλώς ορίσατε στο PetBNB!\nΕδώ μπορείτε να βρείτε οικοδεσπότες για το κατοικίδιό σας.");
        infoArea.setEditable(false);
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JButton btnNext = new JButton("Αναζήτηση & Μετάβαση στο Προφίλ Οικοδεσπότη ->");
        btnNext.addActionListener(e -> controller.navigateTo("HostProfileScreen"));
        add(btnNext, BorderLayout.SOUTH);
    }
}
