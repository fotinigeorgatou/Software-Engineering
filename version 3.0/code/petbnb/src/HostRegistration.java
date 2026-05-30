import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;

public class HostRegistration extends JFrame {

    private String userEmail;
    private boolean isDualFlow;

    private ArrayList<String> accommodationsList = new ArrayList<>();

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);

    private JComboBox<String> cbPropertyType;
    private JCheckBox chkFencedYard;

    private JCheckBox chkDogs, chkCats, chkBirds, chkOtherPets;
    private JSpinner spinMaxPets;

    private JCheckBox chkOwnPets;
    private JTextField txtOwnPetsGender;
    private JComboBox<String> cbOwnPetsFriendly;

    private JCheckBox chkFirstAid, chkTrainingExp, chkAdministerMeds;
    private JTextArea txtExperienceDetails;

    private JCheckBox chkDailyWalk, chkTransportation, chkBathGrooming;
    private JLabel lblHouseImgPath;

    public HostRegistration(String email, boolean isDualFlow) {
        this.userEmail = email;
        this.isDualFlow = isDualFlow;

        setTitle("petbnb - Host Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Καταχώρηση Στοιχείων Φιλοξενητή (Host)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // 1.3 Περιγραφή Καταλύματος
        JPanel p1 = createSectionPanel("1.3 Περιγραφή Καταλύματος");
        String[] properties = {"Διαμέρισμα", "Μονοκατοικία με κήπο", "Μεζονέτα", "Άλλο"};
        cbPropertyType = addComboField(p1, "Τύπος Κατοικίας:", properties);
        chkFencedYard = new JCheckBox("Υπάρχει περιφραγμένος εξωτερικός χώρος (αυλή/κήπος)");
        p1.add(chkFencedYard);
        contentPanel.add(p1);

        // 1.4 Δυνατότητα Φιλοξενίας
        JPanel p2 = createSectionPanel("1.4 Δυνατότητα Φιλοξενίας");
        p2.add(new JLabel("Δεχόμενα είδη ζώων (*):"));
        chkDogs = new JCheckBox("Σκύλοι");
        chkCats = new JCheckBox("Γάτες");
        chkBirds = new JCheckBox("Πουλιά");
        chkOtherPets = new JCheckBox("Άλλα μικρά ζώα");
        p2.add(chkDogs); p2.add(chkCats); p2.add(chkBirds); p2.add(chkOtherPets);

        p2.add(Box.createVerticalStrut(5));
        p2.add(new JLabel("Μέγιστος αριθμός ζώων για ταυτόχρονη φιλοξενία:"));
        spinMaxPets = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinMaxPets.setMaximumSize(new Dimension(420, 30));
        p2.add(spinMaxPets);
        contentPanel.add(p2);

        // 1.5 Παρουσία άλλων ζώων
        JPanel p3 = createSectionPanel("1.5 Παρουσία Δικών σας Ζώων στο Χώρο");
        chkOwnPets = new JCheckBox("Έχω δικά μου κατοικίδια στο σπίτι");
        txtOwnPetsGender = addFormField(p3, "Φύλο/Ράτσα δικών σας ζώων (αν υπάρχουν):");
        cbOwnPetsFriendly = addComboField(p3, "Είναι εξοικειωμένα με ξένους επισκέπτες;", new String[]{"Ναι, απόλυτα", "Σχετικά εξοικειωμένα", "Όχι, προτιμούν απομόνωση"});
        p3.add(chkOwnPets);
        contentPanel.add(p3);

        // 1.6 Λεπτομέρειες Εμπειρίας
        JPanel p4 = createSectionPanel("1.6 Εμπειρία & Γνώσεις");
        chkFirstAid = new JCheckBox("Γνώσεις Πρώτων Βοηθειών για ζώα");
        chkTrainingExp = new JCheckBox("Εμπειρία στην εκπαίδευση ζώων");
        chkAdministerMeds = new JCheckBox("Δυνατότητα χορήγησης φαρμάκων / ενέσεων");
        p4.add(chkFirstAid); p4.add(chkTrainingExp); p4.add(chkAdministerMeds);
        txtExperienceDetails = addTextAreaField(p4, "Επιπλέον λεπτομέρειες εμπειρίας (προαιρετικό):");
        contentPanel.add(p4);

        // 1.7 Παρεχόμενες Υπηρεσίες
        JPanel p5 = createSectionPanel("1.7 Παρεχόμενες Extra Υπηρεσίες");
        chkDailyWalk = new JCheckBox("Καθημερινή βόλτα (έξω από το χώρο)");
        chkTransportation = new JCheckBox("Μεταφορά (από και προς τον ιδιοκτήτη)");
        chkBathGrooming = new JCheckBox("Μπάνιο / Καλλωπισμός (Bath & Grooming)");
        p5.add(chkDailyWalk); p5.add(chkTransportation); p5.add(chkBathGrooming);
        contentPanel.add(p5);


        // Κουμπιά Διαχείρισης Ροής
        JPanel ActionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        ActionPanel.setBackground(CARD_WHITE);
        ActionPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnAddMoreHouse = new RoundedButton("Προσθήκη 2ου Χώρου");
        btnAddMoreHouse.addActionListener(e -> handleAddMoreAccommodation());

        JButton btnFinalizeHost = new RoundedButton("Προεπισκόπηση & Δημοσίευση");
        btnFinalizeHost.addActionListener(e -> handleHostFinalization());

        ActionPanel.add(btnAddMoreHouse);
        ActionPanel.add(btnFinalizeHost);
        contentPanel.add(ActionPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSectionPanel(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD_WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PURPLE, 1), title,
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), PURPLE));
        return p;
    }

    private JTextField addFormField(JPanel panel, String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(420, 30));
        panel.add(lbl);
        panel.add(tf);
        panel.add(Box.createVerticalStrut(5)); // <--- Διορθώθηκε σε createVerticalStrut
        return tf;
    }

    private JComboBox<String> addComboField(JPanel panel, String labelText, String[] items) {
        JLabel lbl = new JLabel(labelText);
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setMaximumSize(new Dimension(420, 30));
        panel.add(lbl);
        panel.add(cb);
        panel.add(Box.createVerticalStrut(5)); // <--- Διορθώθηκε σε createVerticalStrut
        return cb;
    }

    private JTextArea addTextAreaField(JPanel panel, String labelText) {
        JLabel lbl = new JLabel(labelText);
        JTextArea ta = new JTextArea(2, 20);
        ta.setLineWrap(true);
        JScrollPane sp = new JScrollPane(ta);
        sp.setMaximumSize(new Dimension(420, 50));
        panel.add(lbl);
        panel.add(sp);
        panel.add(Box.createVerticalStrut(5)); // <--- Διορθώθηκε σε createVerticalStrut
        return ta;
    }


    // --- ΒΟΗΘΗΤΙΚΗ ΜΕΘΟΔΟΣ ΕΛΕΓΧΟΥ ΠΕΡΙΟΡΙΣΜΩΝ (VALIDATION) ---
    private boolean validateHostInputs() {
        // 1. [Use Case 1 - 1.4]: Έλεγχος αν επιλέχθηκε τουλάχιστον ένα είδος ζώου
        if (!chkDogs.isSelected() && !chkCats.isSelected() && !chkBirds.isSelected() && !chkOtherPets.isSelected()) {
            JOptionPane.showMessageDialog(this, "Μήνυμα Error: Παρακαλώ επιλέξτε τουλάχιστον ένα είδος ζώου που μπορείτε να φιλοξενήσετε.", "Σφάλμα Ελέγχου Στοιχείων", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 2. [TC_NEG_03]: Έλεγχος ορίων για το μέγιστο αριθμό ζώων
        int maxPets = (int) spinMaxPets.getValue();
        if (maxPets <= 0 || maxPets > 10) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: Ο μέγιστος αριθμός ζώων πρέπει να είναι μεταξύ 1 και 10.", "Σφάλμα Ελέγχου Επιλογών", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 3. [TC_NEG_06]: Αν δηλωθεί ύπαρξη δικών του ζώων, η περιγραφή τους γίνεται υποχρεωτική
        if (chkOwnPets.isSelected() && txtOwnPetsGender.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: Εφόσον έχετε δικά σας κατοικίδια, πρέπει να συμπληρώσετε το πεδίο 'Φύλο/Ράτσα δικών σας ζώων'.", "Ελλιπή Στοιχεία", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 4. [TC_NEG_05]: Αντικρουόμενες επιλογές - Μη κοινωνικά ιδιόκτητα ζώα με μεγάλο αριθμό φιλοξενούμενων
        String friendliness = cbOwnPetsFriendly.getSelectedItem().toString();
        if (chkOwnPets.isSelected() && friendliness.equals("Όχι, προτιμούν απομόνωση") && maxPets > 1) {
            JOptionPane.showMessageDialog(this, "Έλεγχος Επιλογών (Error): Δεν μπορείτε να φιλοξενήσετε πάνω από 1 ζώο ταυτόχρονα, καθώς τα δικά σας κατοικίδια προτιμούν απομόνωση.", "Μήνυμα Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // Όλοι οι έλεγχοι πέτυχαν
    }

    private void handleAddMoreAccommodation() {
        // Εκτέλεση ελέγχων πριν την προσθήκη στη λίστα
        if (!validateHostInputs()) {
            return; // Διακοπή ροής (Unsuccessful)
        }

        String currentSelection = cbPropertyType.getSelectedItem().toString();
        accommodationsList.add(currentSelection);

        // Reset φόρμας για το επόμενο κατάλυμα
        cbPropertyType.setSelectedIndex(0);
        chkFencedYard.setSelected(false);
        if (lblHouseImgPath != null) {
            lblHouseImgPath.setText("Δεν επιλέχθηκαν αρχεία");
        }

        JOptionPane.showMessageDialog(this, "Το κατάλυμα αποθηκεύτηκε στη λίστα! Τώρα μπορείτε να εισάγετε τα στοιχεία για το επόμενο κατάλυμα.");
    }

    private void handleHostFinalization() {
        // Εκτέλεση ελέγχων πριν την προεπισκόπηση και οριστικοποίηση
        if (!validateHostInputs()) {
            return; // Διακοπή ροής (Unsuccessful)
        }

        String currentSelection = cbPropertyType.getSelectedItem().toString();
        if (!accommodationsList.contains(currentSelection)) {
            accommodationsList.add(currentSelection);
        }

        // 1. Δημιουργία λίστας για το "Services"
        ArrayList<String> servicesList = new ArrayList<>();
        if (chkDogs.isSelected()) servicesList.add("Σκύλοι");
        if (chkCats.isSelected()) servicesList.add("Γάτες");
        if (chkBirds.isSelected()) servicesList.add("Πουλιά");
        if (chkOtherPets.isSelected()) servicesList.add("Άλλα ζώα");
        String servicesString = String.join(", ", servicesList);

        // 2. Δημιουργία για το "Roommates"
        String roommatesString = chkOwnPets.isSelected() ? "Ναι" : "Όχι";

        // 3. Δημιουργία για το "Offers"
        ArrayList<String> offersList = new ArrayList<>();
        if (chkDailyWalk.isSelected()) offersList.add("Βόλτα");
        if (chkTransportation.isSelected()) offersList.add("Μεταφορά");
        if (chkBathGrooming.isSelected()) offersList.add("Grooming");
        String offersString = offersList.isEmpty() ? "Καμία extra παροχή" : String.join(", ", offersList);

        StringBuilder hostPreview = new StringBuilder();
        hostPreview.append("--- ΠΡΟΕΠΙΣΚΟΠΗΣΗ ΔΗΜΟΣΙΑΣ ΕΙΚΟΝΑΣ HOST ---\n");
        hostPreview.append("Email: ").append(userEmail).append("\n");
        hostPreview.append("Τύπος Καταλύματος: ").append(currentSelection).append("\n");
        hostPreview.append("Δεχόμενα Ζώα (Services): ").append(servicesString).append("\n");
        hostPreview.append("Ύπαρξη άλλων κατοικίδιων (Roommates): ").append(roommatesString).append("\n");
        hostPreview.append("Παρεχόμενες Υπηρεσίες (Offers): ").append(offersString).append("\n\n");
        hostPreview.append("Αποδέχεστε τους κανόνες ασφαλείας για την τελική δημοσίευση του προφίλ;");

        int finalizeChoice = JOptionPane.showConfirmDialog(this, hostPreview.toString(), "Προεπισκόπηση & Ενεργοποίηση", JOptionPane.YES_NO_OPTION);

        if (finalizeChoice == JOptionPane.YES_OPTION) {
            User user = DatabaseManager.getUser(userEmail);
            if (user != null) {
                ArrayList<String> encodedHouses = new ArrayList<>();
                for (String acc : accommodationsList) {
                    encodedHouses.add(acc + "#" + servicesString + "#" + roommatesString + "#" + offersString);
                }

                user.location = String.join(" | ", encodedHouses);
                user.preferences = offersString;
                DatabaseManager.updateUser(user);
            }

            JOptionPane.showMessageDialog(this, "Το Host προφίλ σας αποθηκεύτηκε στη βάση επιτυχώς!");
            this.dispose();

            new ProfilePreview(userEmail, false).setVisible(true);
        }
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PINK);
            int arc = getHeight();
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}