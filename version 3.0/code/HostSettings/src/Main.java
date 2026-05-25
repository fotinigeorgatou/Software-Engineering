import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private HostManager hostManager = new HostManager();

    // Στοιχεία GUI
    private JComboBox<String> srvCombo;
    private JTextField arrivalField;
    private JTextField departureField;
    private JTextField priceNightField;
    private JTextField priceSrvField;
    private JButton saveButton;

    public Main() {
        // Ρυθμίσεις Παραθύρου
        setTitle("Profile Settings Screen");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Τίτλος
        JLabel headerLabel = new JLabel("Ρυθμίσεις Προφίλ Οικοδεσπότη", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        // Φόρμα Δεδομένων
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Επιλογή Υπηρεσίας:"));
        String[] services = {"Καθημερινή Βόλτα", "Επίσκεψη σε κτηνίατρο", "Παιχνίδι","Επίσκεψη σε Pet Groomer","Μπάνιο στο σπίτι"};
        srvCombo = new JComboBox<>(services);
        formPanel.add(srvCombo);

        formPanel.add(new JLabel("Τιμή Υπηρεσίας:"));
        priceSrvField = new JTextField("15.0");
        formPanel.add(priceSrvField);

        formPanel.add(new JLabel("Ημερομηνία Άφιξης (ΗΗ/ΜΜ/ΕΕΕΕ):"));
        arrivalField = new JTextField("01/06/2026");
        formPanel.add(arrivalField);

        formPanel.add(new JLabel("Ημερομηνία Αποχώρησης (ΗΗ/ΜΜ/ΕΕΕΕ):"));
        departureField = new JTextField("10/06/2026");
        formPanel.add(departureField);

        formPanel.add(new JLabel("Τιμή ανά Νύχτα (PaymentPerNight):"));
        priceNightField = new JTextField("50.0");
        formPanel.add(priceNightField);

        add(formPanel, BorderLayout.CENTER);

        saveButton = new JButton("Αποθήκευση & Ενεργοποίηση Διαθεσιμότητας");
        saveButton.setFont(new Font("Arial", Font.BOLD, 13));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        add(saveButton, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pNightRaw = priceNightField.getText();
                String pSrvRaw = priceSrvField.getText();
                String arrivalStr = arrivalField.getText();
                String departureStr = departureField.getText();

                // 1. Έλεγχος εγκυρότητας (ValidateData)
                if (hostManager.validateData(pNightRaw, pSrvRaw, arrivalStr, departureStr)) {
                    double pNight = Double.parseDouble(pNightRaw);
                    double pSrv = Double.parseDouble(pSrvRaw);

                    // 2. Υπολογισμός διανυκτερεύσεων και συνολικού κόστους (calcFee)
                    long nights = hostManager.calculateNights(arrivalStr, departureStr);
                    double total = hostManager.calcFee(pNight, nights, pSrv);

                    // 3. Αποθήκευση
                    hostManager.executeSave();

                    // 4. Εμφάνιση Προεπισκόπησης
                    PreviewProfileScreen preview = new PreviewProfileScreen(
                            Main.this,
                            srvCombo.getSelectedItem().toString(),
                            arrivalStr,
                            departureStr,
                            nights,
                            total
                    );
                    preview.setVisible(true);
                }
            }
        });


    }
    public static void main(String[] args) {
        // Εκτέλεση του GUI στο σωστό Thread της Java
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}