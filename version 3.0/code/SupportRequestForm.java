import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SupportRequestForm extends JFrame {

    private static final Color BG_DARK    = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK       = new Color(255, 60, 91);
    private static final Color PURPLE     = new Color(193, 163, 229);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);

    private SupportController supportController;
    private List<String> attachedFiles = new ArrayList<>();
    private JTextField subjectField;
    private JTextArea descArea;
    private JComboBox<String> categoryBox;
    private JLabel attachLabel;

    public SupportRequestForm() {
        this.supportController = new SupportController();
        initUI();
    }

    private void initUI() {
        setTitle("petbnb");
        setSize(450, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new FormCard());
        add(wrapper);
    }

    class FormCard extends JPanel {
        public FormCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(390, 660));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(25, 28, 25, 28));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

            // Header
            JLabel title = new JLabel("Αίτημα Υποστήριξης", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridy = 0; gbc.insets = new Insets(0, 0, 4, 0);
            add(title, gbc);

            JLabel subtitle = new JLabel("Συμπληρώστε τη φόρμα παρακάτω", SwingConstants.CENTER);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            subtitle.setForeground(PLACEHOLDER_COLOR);
            gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
            add(subtitle, gbc);

            // Category
            gbc.gridy = 2; gbc.insets = new Insets(0, 0, 6, 0);
            add(sectionLabel("Κατηγορία"), gbc);

            categoryBox = new JComboBox<>(new String[]{
                "Κρατήσεις", "Πληρωμές", "Λογαριασμός", "Κατοικίδια", "Τεχνικό Πρόβλημα", "Άλλο"
            });
            categoryBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
            categoryBox.setBackground(INPUT_GRAY);
            categoryBox.setPreferredSize(new Dimension(334, 42));
            gbc.gridy = 3; gbc.insets = new Insets(0, 0, 14, 0);
            add(categoryBox, gbc);

            // Subject
            gbc.gridy = 4; gbc.insets = new Insets(0, 0, 6, 0);
            add(sectionLabel("Θέμα"), gbc);

            subjectField = createRoundedField("Σύντομη περιγραφή του προβλήματος");
            gbc.gridy = 5; gbc.insets = new Insets(0, 0, 14, 0);
            add(subjectField, gbc);

            // Description
            gbc.gridy = 6; gbc.insets = new Insets(0, 0, 6, 0);
            add(sectionLabel("Περιγραφή (τουλάχιστον 10 χαρακτήρες)"), gbc);

            descArea = new JTextArea(5, 20);
            descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setBorder(new EmptyBorder(10, 12, 10, 12));
            descArea.setBackground(INPUT_GRAY);
            JScrollPane descScroll = new JScrollPane(descArea);
            descScroll.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1, true));
            descScroll.setPreferredSize(new Dimension(334, 110));
            gbc.gridy = 7; gbc.insets = new Insets(0, 0, 14, 0);
            add(descScroll, gbc);

            // Attach
            gbc.gridy = 8; gbc.insets = new Insets(0, 0, 6, 0);
            add(sectionLabel("Επισύναψη αρχείων (προαιρετικό)"), gbc);

            JPanel attachRow = new JPanel(new BorderLayout(8, 0));
            attachRow.setOpaque(false);

            JButton attachBtn = new JButton("📎 Επισύναψη") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(INPUT_GRAY);
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            attachBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
            attachBtn.setForeground(new Color(60,60,60));
            attachBtn.setContentAreaFilled(false); attachBtn.setBorderPainted(false); attachBtn.setFocusPainted(false);
            attachBtn.setPreferredSize(new Dimension(140, 38));
            attachBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            attachBtn.addActionListener(e -> attachFiles());

            attachLabel = new JLabel("Κανένα αρχείο");
            attachLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            attachLabel.setForeground(PLACEHOLDER_COLOR);

            attachRow.add(attachBtn, BorderLayout.WEST);
            attachRow.add(attachLabel, BorderLayout.CENTER);
            gbc.gridy = 9; gbc.insets = new Insets(0, 0, 22, 0);
            add(attachRow, gbc);

            // Submit button
            JButton submitBtn = new JButton("Υποβολή Αιτήματος") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(PINK);
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            submitBtn.setForeground(PURPLE);
            submitBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
            submitBtn.setContentAreaFilled(false); submitBtn.setBorderPainted(false); submitBtn.setFocusPainted(false);
            submitBtn.setPreferredSize(new Dimension(300, 50));
            submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitBtn.addActionListener(e -> submitRequest());

            gbc.gridy = 10; gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(0, 0, 10, 0);
            add(submitBtn, gbc);

            // Back
            JButton backBtn = new JButton("← Πίσω") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(INPUT_GRAY);
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            backBtn.setForeground(new Color(80,80,80));
            backBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
            backBtn.setContentAreaFilled(false); backBtn.setBorderPainted(false); backBtn.setFocusPainted(false);
            backBtn.setPreferredSize(new Dimension(160, 40));
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> dispose());
            gbc.gridy = 11; gbc.insets = new Insets(0,0,0,0);
            add(backBtn, gbc);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.dispose();
        }
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(60, 60, 60));
        return l;
    }

    private JTextField createRoundedField(String placeholder) {
        JTextField f = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        f.setOpaque(false); f.setBorder(new EmptyBorder(10,20,10,20));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setForeground(PLACEHOLDER_COLOR);
        f.setPreferredSize(new Dimension(334, 44));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(PLACEHOLDER_COLOR); }
            }
        });
        return f;
    }

    public void enterDescription() { descArea.requestFocus(); }

    public void attachFiles() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            attachedFiles.clear();
            for (java.io.File f : fc.getSelectedFiles()) attachedFiles.add(f.getName());
            attachLabel.setText(attachedFiles.size() + " αρχείο(α) επισυνάφθηκε(αν)");
            attachLabel.setForeground(new Color(0, 150, 0));
        }
    }

    public void submitRequest() {
        String subject = subjectField.getText().trim();
        String desc = descArea.getText().trim();
        String category = (String) categoryBox.getSelectedItem();

        if (subject.isEmpty() || subject.equals("Σύντομη περιγραφή του προβλήματος")) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε το θέμα.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!supportController.validDescription(desc)) {
            JOptionPane.showMessageDialog(this, "Η περιγραφή πρέπει να είναι τουλάχιστον 10 χαρακτήρες.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SupportRequest request = supportController.createRequest(subject, desc, category);
        for (String f : attachedFiles) request.addAttachment(f);

        Response response = supportController.submitSupportRequest(request);

        if (response == null) {
            showConnectionError();
        } else {
            showConfirmation(response);
        }
    }

    public void showConnectionError() {
        JOptionPane.showMessageDialog(this,
            "⚠️ Αδυναμία σύνδεσης. Παρακαλώ δοκιμάστε αργότερα.",
            "Σφάλμα Σύνδεσης", JOptionPane.ERROR_MESSAGE);
    }

    private void showConfirmation(Response response) {
        JDialog dialog = new JDialog(this, "Επιβεβαίωση", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_DARK);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(10, 20, 8, 20); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel icon = new JLabel("✅", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        gbc.gridy = 0; dialog.add(icon, gbc);

        JLabel title = new JLabel("Το αίτημά σας υποβλήθηκε!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(CARD_WHITE);
        gbc.gridy = 1; dialog.add(title, gbc);

        JLabel msg = new JLabel("<html><center>" + response.getResponse_content() + "</center></html>", SwingConstants.CENTER);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msg.setForeground(new Color(180, 180, 180));
        gbc.gridy = 2; dialog.add(msg, gbc);

        JButton okBtn = new JButton("OK") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        okBtn.setForeground(PURPLE); okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setContentAreaFilled(false); okBtn.setBorderPainted(false); okBtn.setFocusPainted(false);
        okBtn.setPreferredSize(new Dimension(120, 42));
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(e -> { dialog.dispose(); dispose(); });
        gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(okBtn, gbc);

        dialog.setVisible(true);
    }

    public void displaySupportRequestForm() { setVisible(true); }
}
