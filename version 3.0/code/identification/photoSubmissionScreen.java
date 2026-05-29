import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class photoSubmissionScreen extends JFrame {

    private static final Color BG_DARK = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK = new Color(255, 60, 91);
    private static final Color PURPLE = new Color(193, 163, 229);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);

    private user user;

    public photoSubmissionScreen(user user) {
        this.user = user;

        setTitle("petbnb");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 750);
        setLocationRelativeTo(null);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new PhotoSubmissionCard());

        add(wrapper);
    }

    class PhotoSubmissionCard extends JPanel {

        private JTextField documentNumberField;
        private JTextField frontPhotoField;
        private JTextField backPhotoField;

        public PhotoSubmissionCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 620));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 30, 30, 30));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Οδηγίες Φωτογράφησης", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.PLAIN, 20));

            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 20, 0);
            add(title, gbc);

            JLabel instructions = new JLabel(
                    "<html><div style='text-align:center;'>"
                            + "Επίλεξε καθαρές φωτογραφίες της<br>"
                            + "μπροστά και πίσω πλευράς της ταυτότητας.<br>"
                            + "Η εικόνα δεν πρέπει να είναι θολή,<br>"
                            + "κουνημένη ή υπερβολικά μικρή."
                            + "</div></html>",
                    SwingConstants.CENTER
            );

            instructions.setFont(new Font("SansSerif", Font.PLAIN, 14));

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 20, 0);
            add(instructions, gbc);

            documentNumberField = createRoundedTextField("αριθμός ταυτότητας π.χ. ΑΝ6856");

            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 15, 0);
            add(documentNumberField, gbc);

            frontPhotoField = createRoundedTextField("μπροστινή φωτογραφία");
            frontPhotoField.setEditable(false);

            gbc.gridy = 3;
            gbc.insets = new Insets(0, 0, 10, 0);
            add(frontPhotoField, gbc);

            RoundedButton frontButton = new RoundedButton("επιλογή μπροστά");
            frontButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            frontButton.setForeground(PURPLE);
            frontButton.addActionListener(e -> chooseImage(frontPhotoField));

            gbc.gridy = 4;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 15, 0);
            add(frontButton, gbc);

            gbc.fill = GridBagConstraints.HORIZONTAL;

            backPhotoField = createRoundedTextField("πίσω φωτογραφία");
            backPhotoField.setEditable(false);

            gbc.gridy = 5;
            gbc.insets = new Insets(0, 0, 10, 0);
            add(backPhotoField, gbc);

            RoundedButton backButton = new RoundedButton("επιλογή πίσω");
            backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            backButton.setForeground(PURPLE);
            backButton.addActionListener(e -> chooseImage(backPhotoField));

            gbc.gridy = 6;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 25, 0);
            add(backButton, gbc);

            RoundedButton submitButton = new RoundedButton("υποβολή στοιχείων");
            submitButton.setFont(new Font("SansSerif", Font.BOLD, 15));
            submitButton.setForeground(PURPLE);
            submitButton.addActionListener(e -> submitVerification());

            gbc.gridy = 7;
            gbc.insets = new Insets(5, 0, 0, 0);
            add(submitButton, gbc);
        }

        private void chooseImage(JTextField targetField) {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image files",
                    "jpg",
                    "jpeg",
                    "png"
            );

            chooser.setFileFilter(filter);

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();

                targetField.setText(selectedFile.getAbsolutePath());
                targetField.setForeground(Color.BLACK);
            }
        }

        private void submitVerification() {
            String documentNumber = documentNumberField.getText().trim();
            String frontPhoto = frontPhotoField.getText().trim();
            String backPhoto = backPhotoField.getText().trim();

            identificationData data = new identificationData(
                    "ID1",
                    "Identity Card",
                    documentNumber
            );

            if (!data.validateIdentificationData()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Λάθος μορφή αριθμού ταυτότητας.\n"
                                + "Δεκτή μορφή: ΑΝ6856 ή ΑΝ 6856."
                );

                return;
            }

            if (frontPhoto.equals("μπροστινή φωτογραφία")
                    || backPhoto.equals("πίσω φωτογραφία")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please choose both photos."
                );

                return;
            }

            identityPhotoSet photoSet = new identityPhotoSet("PS1");
            photoSet.captureIdentityPhoto("front", frontPhoto);
            photoSet.captureIdentityPhoto("back", backPhoto);

            verificationRequest request = new verificationRequest(
                    "REQ" + System.currentTimeMillis(),
                    user
            );

            request.attachIdentityPhotoSet(photoSet);
            request.attachIdentificationData(data);
            request.submitRequest();

            String frontPhotoProblem = getPhotoProblem(frontPhoto);
            String backPhotoProblem = getPhotoProblem(backPhoto);

            if (frontPhotoProblem != null || backPhotoProblem != null) {
                String reason = "";

                if (frontPhotoProblem != null) {
                    reason += "Μπροστινή φωτογραφία: " + frontPhotoProblem + "\n";
                }

                if (backPhotoProblem != null) {
                    reason += "Πίσω φωτογραφία: " + backPhotoProblem + "\n";
                }

                request.rejectRequest(reason);
                DBManager.saveVerificationRequest(request);

                JOptionPane.showMessageDialog(
                        this,
                        "Το αίτημα ταυτοποίησης απορρίφθηκε.\n"
                                + reason
                                + "Θα επιστρέψετε στην επανάληψη λήψης."
                );

                dispose();
                new photoSubmissionScreen(user).setVisible(true);

                return;
            }

            DBManager.saveVerificationRequest(request);

            dispose();
            new verificationStatusScreen(user, request).setVisible(true);
        }

        private String getPhotoProblem(String path) {
            try {
                File file = new File(path);

                if (!file.exists()) {
                    return "το αρχείο δεν βρέθηκε";
                }

                String lower = file.getName().toLowerCase();

                if (!(lower.endsWith(".jpg")
                        || lower.endsWith(".jpeg")
                        || lower.endsWith(".png"))) {
                    return "μη αποδεκτός τύπος αρχείου";
                }

                BufferedImage image = ImageIO.read(file);

                if (image == null) {
                    return "το αρχείο δεν αναγνωρίζεται ως εικόνα";
                }

                if (image.getWidth() < 250 || image.getHeight() < 150) {
                    return "η εικόνα είναι πολύ μικρή ή κομμένη";
                }

                double sharpness = calculateSharpness(image);

                if (sharpness < 3.0) {
                    return "η εικόνα φαίνεται θολή ή κουνημένη";
                }

                return null;

            } catch (Exception e) {
                return "η εικόνα δεν μπορεί να διαβαστεί";
            }
        }

        private double calculateSharpness(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();

            int stepX = Math.max(1, width / 250);
            int stepY = Math.max(1, height / 250);

            double totalDifference = 0;
            int comparisons = 0;

            for (int y = stepY; y < height - stepY; y += stepY) {
                for (int x = stepX; x < width - stepX; x += stepX) {

                    int center = getGrayValue(image.getRGB(x, y));
                    int right = getGrayValue(image.getRGB(x + stepX, y));
                    int bottom = getGrayValue(image.getRGB(x, y + stepY));

                    totalDifference += Math.abs(center - right);
                    totalDifference += Math.abs(center - bottom);

                    comparisons += 2;
                }
            }

            if (comparisons == 0) {
                return 0;
            }

            return totalDifference / comparisons;
        }

        private int getGrayValue(int rgb) {
            int red = (rgb >> 16) & 0xff;
            int green = (rgb >> 8) & 0xff;
            int blue = rgb & 0xff;

            return (red + green + blue) / 3;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(CARD_WHITE);

            g2.fill(
                    new RoundRectangle2D.Float(
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            40,
                            40
                    )
            );

            g2.dispose();
        }
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(INPUT_GRAY);

                g2.fill(
                        new RoundRectangle2D.Float(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                getHeight(),
                                getHeight()
                        )
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        styleField(field, placeholder);

        return field;
    }

    private void styleField(JTextField field, String placeholder) {
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(10, 25, 10, 25));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("SansSerif", Font.BOLD, 14));
        field.setForeground(PLACEHOLDER_COLOR);
        field.setPreferredSize(new Dimension(300, 50));

        field.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setPreferredSize(new Dimension(210, 50));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(PINK);

            g2.fill(
                    new RoundRectangle2D.Float(
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            getHeight(),
                            getHeight()
                    )
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}