import javax.swing.*;
import java.awt.*;

public class PreviewProfileScreen extends JDialog {
    public PreviewProfileScreen(JFrame parent, String services, String arrival, String departure, long nights, double totalCost) {
        super(parent, "Προεπισκόπηση Προφίλ", true);
        setSize(450, 280);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 1, 10, 10));

        JLabel titleLabel = new JLabel("Preview Profile Screen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel srvLabel = new JLabel("  Υπηρεσία: " + services);
        JLabel dateLabel = new JLabel("  Διαμονή: Από " + arrival + " έως " + departure + " (" + nights + " νύχτες)");
        JLabel costLabel = new JLabel("  Συνολικό Κόστος: " + totalCost + "€");

        JButton closeButton = new JButton("Κλείσιμο");
        closeButton.addActionListener(e -> dispose());

        add(titleLabel);
        add(srvLabel);
        add(dateLabel);
        add(costLabel);
        add(new JLabel("")); // Κενό για σωστή στοίχιση
        add(closeButton);
    }

}
