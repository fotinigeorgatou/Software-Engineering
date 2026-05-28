import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class SupportScreen extends JFrame {

    private static final Color BG_DARK    = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK       = new Color(255, 60, 91);
    private static final Color PURPLE     = new Color(193, 163, 229);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);

    private HelpController helpController = new HelpController();

    public SupportScreen() { initUI(); }

    private void initUI() {
        setTitle("petbnb");
        setSize(450, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new HelpCard());
        add(wrapper);
    }

    class HelpCard extends JPanel {
        public HelpCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 600));
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(30, 28, 30, 28));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

            // Logo area
            JLabel logo = new JLabel("🐾 petbnb", SwingConstants.CENTER);
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            gbc.gridy = 0; gbc.insets = new Insets(0, 0, 6, 0);
            add(logo, gbc);

            JLabel title = new JLabel("Κέντρο Βοήθειας", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            gbc.gridy = 1; gbc.insets = new Insets(0, 0, 4, 0);
            add(title, gbc);

            JLabel subtitle = new JLabel("Πώς μπορούμε να σας βοηθήσουμε;", SwingConstants.CENTER);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
            subtitle.setForeground(PLACEHOLDER_COLOR);
            gbc.gridy = 2; gbc.insets = new Insets(0, 0, 28, 0);
            add(subtitle, gbc);

            // Option buttons
            gbc.insets = new Insets(0, 0, 12, 0);

            gbc.gridy = 3;
            add(optionButton("💬", "Επικοινωνία με Υποστήριξη",
                    "Στείλτε αίτημα στην ομάδα μας", e -> selectSupport()), gbc);

            gbc.gridy = 4;
            add(optionButton("❓", "Συχνές Ερωτήσεις",
                    "Βρείτε γρήγορες απαντήσεις", e -> selectFAQ()), gbc);

            gbc.gridy = 5;
            add(optionButton("🤖", "Chatbot Βοήθειας",
                    "Άμεση βοήθεια από το bot μας", e -> openChatbot()), gbc);

            // Back button
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
            backBtn.setPreferredSize(new Dimension(160, 42));
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> dispose());
            gbc.gridy = 6; gbc.insets = new Insets(16, 0, 0, 0);
            gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
            add(backBtn, gbc);
        }

        private JPanel optionButton(String icon, String label, String desc, ActionListener action) {
            JPanel card = new JPanel(new BorderLayout(12, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),20,20));
                    g2.dispose();
                }
            };
            card.setOpaque(false);
            card.setBackground(INPUT_GRAY);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            card.setPreferredSize(new Dimension(324, 72));
            card.setBorder(new EmptyBorder(12, 16, 12, 16));

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
            iconLabel.setPreferredSize(new Dimension(36, 36));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(label);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setForeground(new Color(30,30,30));

            JLabel descLabel = new JLabel(desc);
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            descLabel.setForeground(PLACEHOLDER_COLOR);

            textPanel.add(nameLabel);
            textPanel.add(Box.createVerticalStrut(2));
            textPanel.add(descLabel);

            JLabel arrow = new JLabel("›");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
            arrow.setForeground(PINK);

            card.add(iconLabel, BorderLayout.WEST);
            card.add(textPanel, BorderLayout.CENTER);
            card.add(arrow, BorderLayout.EAST);

            card.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(new Color(210,210,210));
                    card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) { card.setBackground(INPUT_GRAY); }
                public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
            });

            return card;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.dispose();
        }
    }

    public void pressHelp() { setVisible(true); }

    public void selectSupport() {
        SupportRequestForm form = new SupportRequestForm();
        form.displaySupportRequestForm();
    }

    public void selectFAQ() {
        displayRelatedQuestions(helpController.getFAQTopics(), "");
    }

    public void displayRelatedQuestions(List<FAQ> faqs, String keyword) {
        JDialog dialog = new JDialog(this, "Συχνές Ερωτήσεις", true);
        dialog.setSize(440, 680);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_DARK);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout(8,0));
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(16, 16, 10, 16));

        JLabel title = new JLabel("Συχνές Ερωτήσεις");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(CARD_WHITE);

        // Search bar
        JTextField searchField = new JTextField("Αναζήτηση...") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        searchField.setOpaque(false); searchField.setBorder(new EmptyBorder(6,14,6,14));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.setForeground(PLACEHOLDER_COLOR);
        searchField.setPreferredSize(new Dimension(0, 36));
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Αναζήτηση...")) { searchField.setText(""); searchField.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) { searchField.setText("Αναζήτηση..."); searchField.setForeground(PLACEHOLDER_COLOR); }
            }
        });

        JPanel faqListPanel = new JPanel();
        faqListPanel.setLayout(new BoxLayout(faqListPanel, BoxLayout.Y_AXIS));
        faqListPanel.setBackground(BG_DARK);
        faqListPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        Runnable refreshFAQ = () -> {
            faqListPanel.removeAll();
            String kw = searchField.getText().equals("Αναζήτηση...") ? "" : searchField.getText();
            List<FAQ> results = kw.isEmpty() ? FAQ.getAllFAQs() : FAQ.searchFAQ(kw);
            for (FAQ faq : results) {
                faqListPanel.add(createFAQItem(faq));
                faqListPanel.add(Box.createVerticalStrut(6));
            }
            faqListPanel.revalidate(); faqListPanel.repaint();
        };

        searchField.addActionListener(e -> refreshFAQ.run());
        refreshFAQ.run();

        header.add(title, BorderLayout.NORTH);
        header.add(searchField, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(faqListPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel createFAQItem(FAQ faq) {
        JPanel item = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),16,16));
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setBackground(new Color(40,40,40));
        item.setBorder(new EmptyBorder(12, 14, 12, 14));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 999));

        JLabel catLabel = new JLabel(faq.getFaq_category());
        catLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        catLabel.setForeground(PINK);

        JLabel qLabel = new JLabel("<html>" + faq.getQuestion() + "</html>");
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        qLabel.setForeground(CARD_WHITE);

        JLabel aLabel = new JLabel("<html><body style='width:340px'>" + faq.getAnswer() + "</body></html>");
        aLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        aLabel.setForeground(new Color(180,180,180));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(catLabel);
        content.add(Box.createVerticalStrut(4));
        content.add(qLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(aLabel);

        item.add(content, BorderLayout.CENTER);
        return item;
    }

    public void displayAnswer(String answer) {
        JOptionPane.showMessageDialog(this, answer, "Απάντηση", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showConnectionError() {
        JOptionPane.showMessageDialog(this, "⚠️ Αδυναμία σύνδεσης. Δοκιμάστε αργότερα.",
                "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    public void showDelayedMessage() {
        JOptionPane.showMessageDialog(this,
                "⏳ Η απάντηση καθυστερεί. Θα ειδοποιηθείτε σύντομα.",
                "Καθυστέρηση", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openChatbot() {
        JDialog chatDialog = new JDialog(this, "petbnb Chatbot", false);
        chatDialog.setSize(420, 600);
        chatDialog.setLocationRelativeTo(this);
        chatDialog.getContentPane().setBackground(BG_DARK);
        chatDialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(14, 16, 12, 16));
        JLabel botTitle = new JLabel("🤖  petbnb Bot");
        botTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        botTitle.setForeground(CARD_WHITE);
        JLabel botSub = new JLabel("Πάντα διαθέσιμος για βοήθεια");
        botSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        botSub.setForeground(PLACEHOLDER_COLOR);
        JPanel hText = new JPanel(); hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS)); hText.setBackground(BG_DARK);
        hText.add(botTitle); hText.add(botSub);
        header.add(hText, BorderLayout.CENTER);

        // Messages
        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setBackground(CARD_WHITE);
        msgPanel.setBorder(new EmptyBorder(10,10,10,10));

        JScrollPane scroll = new JScrollPane(msgPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD_WHITE);

        Chatbot bot = new Chatbot();

        // Welcome message
        addChatBubble(msgPanel, "Γεια! Είμαι το petbnb bot. Πώς μπορώ να βοηθήσω;", false);

        // Input
        JPanel inputRow = new JPanel(new BorderLayout(8,0));
        inputRow.setBackground(BG_DARK);
        inputRow.setBorder(new EmptyBorder(8,10,10,10));

        JTextField chatInput = new JTextField("Γράψτε ερώτηση...") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        chatInput.setOpaque(false); chatInput.setBorder(new EmptyBorder(9,16,9,16));
        chatInput.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chatInput.setForeground(PLACEHOLDER_COLOR);
        chatInput.setPreferredSize(new Dimension(0, 42));
        chatInput.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (chatInput.getText().equals("Γράψτε ερώτηση...")) { chatInput.setText(""); chatInput.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (chatInput.getText().isEmpty()) { chatInput.setText("Γράψτε ερώτηση..."); chatInput.setForeground(PLACEHOLDER_COLOR); }
            }
        });

        JButton sendBtn = new JButton("➤") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        sendBtn.setForeground(PURPLE); sendBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        sendBtn.setContentAreaFilled(false); sendBtn.setBorderPainted(false); sendBtn.setFocusPainted(false);
        sendBtn.setPreferredSize(new Dimension(46, 42));
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ActionListener sendAction = e -> {
            String txt = chatInput.getText().trim();
            if (txt.isEmpty() || txt.equals("Γράψτε ερώτηση...")) return;
            addChatBubble(msgPanel, txt, true);
            chatInput.setText(""); chatInput.setForeground(PLACEHOLDER_COLOR);
            chatInput.setText("Γράψτε ερώτηση...");
            String reply = bot.processRequest(txt);
            Timer t = new Timer(600, ev -> {
                addChatBubble(msgPanel, reply, false);
                msgPanel.revalidate(); msgPanel.repaint();
                JScrollBar v = scroll.getVerticalScrollBar();
                v.setValue(v.getMaximum());
            });
            t.setRepeats(false); t.start();
        };
        sendBtn.addActionListener(sendAction);
        chatInput.addActionListener(sendAction);

        inputRow.add(chatInput, BorderLayout.CENTER);
        inputRow.add(sendBtn, BorderLayout.EAST);

        chatDialog.add(header, BorderLayout.NORTH);
        chatDialog.add(scroll, BorderLayout.CENTER);
        chatDialog.add(inputRow, BorderLayout.SOUTH);
        chatDialog.setVisible(true);
    }

    private void addChatBubble(JPanel panel, String text, boolean isUser) {
        JPanel wrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 1));
        wrapper.setBackground(CARD_WHITE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel bubble = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isUser ? PINK : INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),18,18));
                g2.dispose();
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel lbl = new JLabel("<html><body style='width:200px'>" + text + "</body></html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(isUser ? PURPLE : new Color(40,40,40));
        bubble.add(lbl);

        wrapper.add(bubble);
        panel.add(wrapper);
        panel.add(Box.createVerticalStrut(3));
    }
}
