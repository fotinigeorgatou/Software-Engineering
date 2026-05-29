import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends JFrame {
    private HostManager hostManager = new HostManager();

    // CheckBoxes και τα αντίστοιχα TextFields για τις τιμές τους
    private JCheckBox cleanBox;
    private JTextField cleanPriceField;

    private JCheckBox breakfastBox;
    private JTextField breakfastPriceField;

    private JCheckBox tourBox;
    private JTextField tourPriceField;

    private JTextField arrivalField;
    private JTextField departureField;
    private JTextField priceNightField;
    private JButton saveButton;

    public Main() {
        setTitle("Profile Settings Screen");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("Ρυθμίσεις Προφίλ Οικοδεσπότη", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("Επιλογή Υπηρεσιών & Τιμής:"));

        // Πλέγμα 3x2 για τις Υπηρεσίες και τις Τιμές τους
        JPanel servicesPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        cleanBox = new JCheckBox("Καθημερινή βόλτα");
        cleanPriceField = new JTextField("15.0");
        cleanPriceField.setEnabled(false); // Απενεργοποιημένο μέχρι να επιλεγεί το κουτάκι
        cleanBox.addActionListener(e -> cleanPriceField.setEnabled(cleanBox.isSelected()));
        servicesPanel.add(cleanBox);
        servicesPanel.add(cleanPriceField);


        tourBox = new JCheckBox("Επίσκεψη σε κτηνίατρο");
        tourPriceField = new JTextField("25.0");
        tourPriceField.setEnabled(false);
        tourBox.addActionListener(e -> tourPriceField.setEnabled(tourBox.isSelected()));
        servicesPanel.add(tourBox);
        servicesPanel.add(tourPriceField);

        breakfastBox = new JCheckBox("Επίσκεψη σε pet groomer");
        breakfastPriceField = new JTextField("10.0");
        breakfastPriceField.setEnabled(false);
        breakfastBox.addActionListener(e -> breakfastPriceField.setEnabled(breakfastBox.isSelected()));
        servicesPanel.add(breakfastBox);
        servicesPanel.add(breakfastPriceField);


        formPanel.add(servicesPanel);

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
                String arrivalStr = arrivalField.getText();
                String departureStr = departureField.getText();

                // 1. Έλεγχος εγκυρότητας πεδίων και custom τιμών υπηρεσιών
                if (hostManager.validateData(pNightRaw, arrivalStr, departureStr,
                        cleanBox.isSelected(), cleanPriceField.getText(),
                        breakfastBox.isSelected(), breakfastPriceField.getText(),
                        tourBox.isSelected(), tourPriceField.getText())) {

                    double pNight = Double.parseDouble(pNightRaw);
                    double totalServicesCost = 0;
                    ArrayList<String> selectedServices = new ArrayList<>();

                    // 2. Υπολογισμός κόστους βάσει των custom τιμών που έγραψε ο χρήστης
                    if (cleanBox.isSelected()) {
                        double price = Double.parseDouble(cleanPriceField.getText());
                        totalServicesCost += price;
                        selectedServices.add("Καθαριότητα (" + price + "€)");
                    }
                    if (breakfastBox.isSelected()) {
                        double price = Double.parseDouble(breakfastPriceField.getText());
                        totalServicesCost += price;
                        selectedServices.add("Πρωινό (" + price + "€)");
                    }
                    if (tourBox.isSelected()) {
                        double price = Double.parseDouble(tourPriceField.getText());
                        totalServicesCost += price;
                        selectedServices.add("Ξενάγηση (" + price + "€)");
                    }

                    String servicesResult = String.join(", ", selectedServices);
                    long nights = hostManager.calculateNights(arrivalStr, departureStr);
                    double total = hostManager.calcFee(pNight, nights, totalServicesCost);

                    hostManager.executeSave();

                    PreviewProfileScreen preview = new PreviewProfileScreen(
                            Main.this,
                            servicesResult,
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
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

}