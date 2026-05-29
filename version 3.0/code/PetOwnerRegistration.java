import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class PetOwnerRegistration extends JFrame {

    private boolean incomingFromDual;
    private String userEmail;
    private boolean isDualRole;

    private ArrayList<String> petsList = new ArrayList<>();

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);

    private JTextField txtName, txtSpecies, txtAge, txtGender, txtBreed;
    private JComboBox<String> cbSocial, cbAggression, cbTraining, cbOtherAnimals;
    private JTextArea txtHealth, txtDiet, txtMeds;
    private JCheckBox chkIndoor, chkYard, chkNoOtherPets;
    private JSpinner spinWalks;
    private JCheckBox chkPhotos, chkGrooming;
    private JLabel lblHealthBookPath;
    private JTextField txtEmergencyPhone, txtVetPhone, txtContactPhone;

    public PetOwnerRegistration(String email, boolean isDualRole, boolean incomingFromDual) {
        this.userEmail = email;
        this.isDualRole = isDualRole;
        this.incomingFromDual = incomingFromDual;

        setTitle("petbnb - Pet Owner Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Καταχώρηση Στοιχείων Ιδιοκτήτη & Ζώου", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // 1.3 Βασικά Στοιχεία Κατοικιδίου
        JPanel p1 = createSectionPanel("1.3 Βασικά Στοιχεία Κατοικιδίου");
        txtName = addFormField(p1, "Όνομα (*):");
        txtSpecies = addFormField(p1, "Είδος (π.χ. Σκύλος, Γάτα) (*):");
        txtAge = addFormField(p1, "Ηλικία:");
        txtGender = addFormField(p1, "Φύλο:");
        txtBreed = addFormField(p1, "Ράτσα:");
        contentPanel.add(p1);

        // 1.4 Ερωτηματολόγιο Συμπεριφοράς
        JPanel p2 = createSectionPanel("1.4 Συμπεριφορά Κατοικιδίου");
        String[] levels = {"Χαμηλό", "Μεσαίο", "Υψηλό"};
        cbSocial = addComboField(p2, "Επίπεδο Κοινωνικότητας:", levels);
        cbAggression = addComboField(p2, "Επιθετικότητα:", new String[]{"Καθόλου", "Λίγο", "Αρκετά"});
        cbTraining = addComboField(p2, "Εκπαίδευση:", new String[]{"Καμία", "Βασική", "Προχωρημένη"});
        cbOtherAnimals = addComboField(p2, "Σχέση με άλλα ζώα:", new String[]{"Φιλική", "Αδιάφορη", "Επιθετική"});
        contentPanel.add(p2);

        // 1.5 Ιατρικό Ιστορικό
        JPanel p3 = createSectionPanel("1.5 Ιατρικό Ιστορικό");
        txtHealth = addTextAreaField(p3, "Προβλήματα Υγείας / Αλλεργίες:");
        txtDiet = addTextAreaField(p3, "Ειδική Διατροφή:");
        txtMeds = addTextAreaField(p3, "Φαρμακευτική Αγωγή:");
        contentPanel.add(p3);

        // 1.6 Προτιμήσεις Καταλύματος
        JPanel p4 = createSectionPanel("1.6 Προτιμήσεις Καταλύματος");
        chkIndoor = new JCheckBox("Να μένει σε εσωτερικό χώρο");
        chkYard = new JCheckBox("Απαιτείται αυλή");
        chkNoOtherPets = new JCheckBox("Ο host να μην έχει άλλα κατοικίδια");
        p4.add(chkIndoor); p4.add(chkYard); p4.add(chkNoOtherPets);
        contentPanel.add(p4);

        // 1.7 Παροχές και Υπηρεσίες
        JPanel p5 = createSectionPanel("1.7 Απαραίτητες Παροχές");
        p5.add(new JLabel("Συχνότητα βόλτας (ανά ημέρα):"));
        spinWalks = new JSpinner(new SpinnerNumberModel(2, 1, 6, 1));
        p5.add(spinWalks);
        chkPhotos = new JCheckBox("Καθημερινή αποστολή φωτογραφιών");
        chkGrooming = new JCheckBox("Υπηρεσία Καλλωπισμού (Grooming)");
        p5.add(chkPhotos); p5.add(chkGrooming);
        contentPanel.add(p5);

        // 1.8 Έγγραφα
        JPanel p6 = createSectionPanel("1.8 Έγγραφα & Πιστοποιητικά");
        JButton btnDoc = new RoundedButton("Αντίγραφο Βιβλιαρίου Υγείας");
        lblHealthBookPath = new JLabel("Δεν επιλέχθηκε αρχείο");
        btnDoc.addActionListener(e -> chooseFile(lblHealthBookPath));
        p6.add(btnDoc); p6.add(lblHealthBookPath);
        contentPanel.add(p6);

        // 1.9 Επικοινωνία Έκτακτης Ανάγκης
        JPanel p7 = createSectionPanel("1.9 Επικοινωνία Έκτακτης Ανάγκης");
        txtEmergencyPhone = addFormField(p7, "Τηλέφωνο Ιδιοκτήτη (*):");
        txtVetPhone = addFormField(p7, "Τηλέφωνο Κτηνιάτρου (Προαιρετικό):");
        txtContactPhone = addFormField(p7, "Τηλέφωνο Οικείου Προσώπου (Προαιρετικό):");
        contentPanel.add(p7);

        JPanel ActionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        ActionPanel.setBackground(CARD_WHITE);
        ActionPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnAddMore = new RoundedButton("Προσθήκη Επιπλέον Ζώου");
        btnAddMore.addActionListener(e -> handleAddMorePet());

        JButton btnFinalize = new RoundedButton("Ολοκλήρωση Εγγραφής");
        btnFinalize.addActionListener(e -> handleFinalization());

        ActionPanel.add(btnAddMore);
        ActionPanel.add(btnFinalize);
        contentPanel.add(ActionPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private String compilePetData() {
        String name = txtName.getText().trim();
        String species = txtSpecies.getText().trim();
        String age = txtAge.getText().trim().isEmpty() ? "Άγνωστη ηλικία" : txtAge.getText().trim();
        String gender = txtGender.getText().trim().isEmpty() ? "Μη ορισμένο" : txtGender.getText().trim();
        String breed = txtBreed.getText().trim().isEmpty() ? "Μιξ / Άγνωστη" : txtBreed.getText().trim();

        String social = cbSocial.getSelectedItem().toString();
        String aggression = cbAggression.getSelectedItem().toString();
        String training = cbTraining.getSelectedItem().toString();
        String otherAnimals = cbOtherAnimals.getSelectedItem().toString();

        String health = txtHealth.getText().trim().isEmpty() ? "Κανένα" : txtHealth.getText().trim();
        String diet = txtDiet.getText().trim().isEmpty() ? "Κανονική" : txtDiet.getText().trim();
        String meds = txtMeds.getText().trim().isEmpty() ? "Καμία" : txtMeds.getText().trim();

        String indoor = chkIndoor.isSelected() ? "Ναι" : "Όχι";
        String yard = chkYard.isSelected() ? "Ναι" : "Όχι";
        String noOtherPets = chkNoOtherPets.isSelected() ? "Ναι" : "Όχι";

        String walks = spinWalks.getValue().toString();
        String photos = chkPhotos.isSelected() ? "Ναι" : "Όχι";
        String grooming = chkGrooming.isSelected() ? "Ναι" : "Όχι";

        String imgName = "default_pet.png";

        return name + "|" + species + "|" + age + "|" + gender + "|" + breed + "|" +
                social + "|" + aggression + "|" + training + "|" + otherAnimals + "|" +
                health + "|" + diet + "|" + meds + "|" +
                indoor + "|" + yard + "|" + noOtherPets + "|" +
                walks + "|" + photos + "|" + grooming + "|" + imgName;
    }

    private void handleAddMorePet() {
        if (txtName.getText().trim().isEmpty() || txtSpecies.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε τουλάχιστον όνομα και είδος.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        petsList.add(compilePetData());
        clearFields();
        JOptionPane.showMessageDialog(this, "Το κατοικίδιο προστέθηκε! Έτοιμο για το επόμενο.");
    }

    // --- ΔΙΟΡΘΩΜΕΝΗ ΜΕΘΟΔΟΣ ΟΛΟΚΛΗΡΩΣΗΣ ΡΟΗΣ ---
    private void handleFinalization() {
        if (txtName.getText().trim().isEmpty() && petsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Πρέπει να καταχωρήσετε τουλάχιστον ένα κατοικίδιο.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtEmergencyPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Το τηλέφωνο ανάγκης είναι υποχρεωτικό.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!txtName.getText().trim().isEmpty()) {
            petsList.add(compilePetData());
        }

        User user = DatabaseManager.getUser(userEmail);
        if (user != null) {
            user.pets = String.join(" | ", petsList);
            DatabaseManager.updateUser(user);

            JOptionPane.showMessageDialog(this, "Τα στοιχεία των κατοικιδίων αποθηκεύτηκαν!");
            this.dispose();

            if (isDualRole) {
                // 3ο Ενδεχόμενο: Πηγαίνει στο HostRegistration
                new HostRegistration(userEmail, true).setVisible(true);
            } else {
                // 1ο Ενδεχόμενο: Πηγαίνει στο ProfilePreview πριν την οριστικοποίηση
                new ProfilePreview(userEmail, false).setVisible(true);
            }
        }


    }

    private void clearFields() {
        txtName.setText(""); txtSpecies.setText(""); txtAge.setText("");
        txtGender.setText(""); txtBreed.setText(""); txtHealth.setText("");
        txtDiet.setText(""); txtMeds.setText("");
        lblHealthBookPath.setText("Δεν επιλέχθηκε αρχείο");
    }

    private JPanel createSectionPanel(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD_WHITE);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PURPLE, 1), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13), PURPLE));
        return p;
    }

    private JTextField addFormField(JPanel panel, String labelText) {
        JLabel lbl = new JLabel(labelText);
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(420, 30));
        panel.add(lbl); panel.add(tf);
        panel.add(Box.createVerticalStrut(5));
        return tf;
    }

    private JComboBox<String> addComboField(JPanel panel, String labelText, String[] items) {
        JLabel lbl = new JLabel(labelText);
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setMaximumSize(new Dimension(420, 30));
        panel.add(lbl); panel.add(cb);
        panel.add(Box.createVerticalStrut(5));
        return cb;
    }

    private JTextArea addTextAreaField(JPanel panel, String labelText) {
        JLabel lbl = new JLabel(labelText);
        JTextArea ta = new JTextArea(2, 20);
        ta.setLineWrap(true);
        JScrollPane sp = new JScrollPane(ta);
        sp.setMaximumSize(new Dimension(420, 50));
        panel.add(lbl); panel.add(sp);
        panel.add(Box.createVerticalStrut(5));
        return ta;
    }

    private void chooseFile(JLabel label) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            label.setText(chooser.getSelectedFile().getName());
        }
    }

    class RoundedButton extends JButton {
        private Color buttonColor = PINK;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public RoundedButton(String text, Color customColor) {
            this(text);
            this.buttonColor = customColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(buttonColor);
            int arc = getHeight();
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
