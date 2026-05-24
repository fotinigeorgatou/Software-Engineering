import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class ChatScreen extends JFrame {

    private static final Color BG_DARK        = new Color(26, 26, 26);
    private static final Color CARD_WHITE     = new Color(249, 250, 243);
    private static final Color INPUT_GRAY     = new Color(223, 223, 223);
    private static final Color PINK           = new Color(255, 60, 91);
    private static final Color PURPLE         = new Color(193, 163, 229);
    private static final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);

    private String currentUser;
    private String otherUser;
    private Conversation conversation;
    private ConversationController convController;
    private MessageController msgController;

    private JPanel messagesPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;

    public ChatScreen(String currentUser, String otherUser) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.convController = new ConversationController();
        this.msgController = new MessageController();
        this.conversation = convController.findOrCreateConversation(currentUser, otherUser);
        convController.openConversation(conversation);
        initUI();
        loadDemoMessages();
    }

    private void initUI() {
        setTitle("petbnb");
        setSize(450, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // ── HEADER (dark bar) ──
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(BG_DARK);
        header.setPreferredSize(new Dimension(450, 60));
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton backBtn = new JButton("←");
        backBtn.setForeground(PURPLE);
        backBtn.setBackground(BG_DARK);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        JLabel nameLabel = new JLabel(otherUser);
        nameLabel.setForeground(CARD_WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel onlineLabel = new JLabel("● online");
        onlineLabel.setForeground(PINK);
        onlineLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(BG_DARK);
        namePanel.add(nameLabel);
        namePanel.add(onlineLabel);

        header.add(backBtn, BorderLayout.WEST);
        header.add(namePanel, BorderLayout.CENTER);

        // ── MESSAGES AREA (card-white background) ──
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(CARD_WHITE);
        messagesPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(CARD_WHITE);

        // ── INPUT AREA ──
        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setBackground(BG_DARK);
        inputPanel.setBorder(new EmptyBorder(10, 12, 12, 12));

        messageField = new JTextField("Γράψτε μήνυμα...") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        messageField.setOpaque(false);
        messageField.setBorder(new EmptyBorder(10, 20, 10, 20));
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageField.setForeground(PLACEHOLDER_COLOR);
        messageField.setPreferredSize(new Dimension(0, 46));
        messageField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (messageField.getText().equals("Γράψτε μήνυμα...")) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (messageField.getText().isEmpty()) {
                    messageField.setText("Γράψτε μήνυμα...");
                    messageField.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
        messageField.addActionListener(e -> typeMessage());

        // Send button (pill shape, PINK)
        JButton sendBtn = new JButton("➤") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        sendBtn.setForeground(PURPLE);
        sendBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        sendBtn.setContentAreaFilled(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setFocusPainted(false);
        sendBtn.setPreferredSize(new Dimension(50, 46));
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> typeMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadDemoMessages() {
        Message m1 = new Message(100, "Γεια! Ενδιαφέρομαι για τη φιλοξενία του κατοικίδιού μου.", otherUser);
        Message m2 = new Message(101, "Γεια! Βέβαια, πότε το χρειάζεσαι;", currentUser);
        Message m3 = new Message(102, "Το επόμενο Σαββατοκύριακο αν είναι εντάξει.", otherUser);
        conversation.addMessage(m1);
        conversation.addMessage(m2);
        conversation.addMessage(m3);
        displayMessages();
    }

    private void displayMessages() {
        messagesPanel.removeAll();
        List<Message> messages = msgController.getMessages(conversation);
        for (Message msg : messages) {
            if (!msg.getMessage_status().equals("deleted")) {
                addMessageBubble(msg);
            } else {
                addDeletedBubble(msg);
            }
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
        scrollToBottom();
    }

    private void addMessageBubble(Message msg) {
        boolean isMe = msg.getSenderName().equals(currentUser);

        JPanel wrapper = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 1));
        wrapper.setBackground(CARD_WHITE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Bubble panel with rounded corners
        JPanel bubble = new JPanel(new BorderLayout(0, 4)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isMe ? PINK : INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        bubble.setOpaque(false);
        bubble.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel contentLabel = new JLabel("<html><body style='width:190px'>" + msg.getMessage_content() + "</body></html>");
        contentLabel.setForeground(isMe ? PURPLE : new Color(40, 40, 40));
        contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel timeLabel = new JLabel(msg.getSent_at());
        timeLabel.setForeground(isMe ? new Color(249, 200, 210) : PLACEHOLDER_COLOR);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        timeLabel.setHorizontalAlignment(isMe ? SwingConstants.RIGHT : SwingConstants.LEFT);

        bubble.add(contentLabel, BorderLayout.CENTER);
        bubble.add(timeLabel, BorderLayout.SOUTH);

        // Right-click menu for my messages
        if (isMe) {
            JPopupMenu popup = new JPopupMenu();
            popup.setBackground(CARD_WHITE);

            JMenuItem editItem = new JMenuItem("✏️  Επεξεργασία");
            editItem.setFont(new Font("SansSerif", Font.PLAIN, 13));
            editItem.addActionListener(e -> selectEdit(msg));

            JMenuItem deleteItem = new JMenuItem("🗑️  Διαγραφή");
            deleteItem.setFont(new Font("SansSerif", Font.PLAIN, 13));
            deleteItem.setForeground(new Color(200, 0, 0));
            deleteItem.addActionListener(e -> selectDelete(msg));

            popup.add(editItem);
            popup.addSeparator();
            popup.add(deleteItem);

            bubble.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e))
                        popup.show(bubble, e.getX(), e.getY());
                }
            });
        }

        wrapper.add(bubble);
        messagesPanel.add(wrapper);
        messagesPanel.add(Box.createVerticalStrut(2));
    }

    private void addDeletedBubble(Message msg) {
        boolean isMe = msg.getSenderName().equals(currentUser);
        JPanel wrapper = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 1));
        wrapper.setBackground(CARD_WHITE);
        JLabel label = new JLabel("🚫 Το μήνυμα διαγράφηκε");
        label.setForeground(PLACEHOLDER_COLOR);
        label.setFont(new Font("SansSerif", Font.ITALIC, 12));
        label.setBorder(new EmptyBorder(4, 10, 4, 10));
        wrapper.add(label);
        messagesPanel.add(wrapper);
        messagesPanel.add(Box.createVerticalStrut(2));
    }

    public void typeMessage() { sendMessage(); }

    public void sendMessage() {
        String content = messageField.getText().trim();
        if (content.isEmpty() || content.equals("Γράψτε μήνυμα...")) return;

        if (!msgController.validMessage(content)) {
            showFailureMessage("Μη έγκυρο μήνυμα! Μέγιστο 500 χαρακτήρες.");
            return;
        }
        boolean success = msgController.sendMessage(content, currentUser, conversation);
        if (success) {
            messageField.setText("");
            messageField.setForeground(PLACEHOLDER_COLOR);
            messageField.setText("Γράψτε μήνυμα...");
            displayMessages();
            Notification notif = Notification.createNotification("new_message",
                currentUser + ": " + (content.length() > 30 ? content.substring(0, 30) + "…" : content));
            notif.showNotification(this);
        } else {
            showFailureMessage("Αποτυχία αποστολής! Ελέγξτε τη σύνδεσή σας.");
        }
    }

    public void selectEdit(Message msg) {
        // Custom input dialog with petbnb style
        JDialog dialog = new JDialog(this, "Επεξεργασία μηνύματος", true);
        dialog.setSize(380, 200);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_DARK);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(8, 20, 8, 20);

        JLabel lbl = new JLabel("Νέο κείμενο μηνύματος:", SwingConstants.CENTER);
        lbl.setForeground(CARD_WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 0; dialog.add(lbl, gbc);

        JTextField editField = new JTextField(msg.getMessage_content()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_GRAY);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        editField.setOpaque(false);
        editField.setBorder(new EmptyBorder(8, 16, 8, 16));
        editField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        editField.setPreferredSize(new Dimension(300, 44));
        gbc.gridy = 1; dialog.add(editField, gbc);

        JButton saveBtn = new JButton("Αποθήκευση") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PINK);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
                g2.dispose(); super.paintComponent(g);
            }
        };
        saveBtn.setForeground(PURPLE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setContentAreaFilled(false); saveBtn.setBorderPainted(false); saveBtn.setFocusPainted(false);
        saveBtn.setPreferredSize(new Dimension(160, 42));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            String newContent = editField.getText().trim();
            if (!newContent.isEmpty()) {
                msgController.updatedMessage(msg, newContent);
                displayUpdatedMessage();
            }
            dialog.dispose();
        });
        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);
        dialog.setVisible(true);
    }

    public void selectDelete(Message msg) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Θέλετε σίγουρα να διαγράψετε αυτό το μήνυμα;",
            "Επιβεβαίωση διαγραφής",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            msgController.deleteMessage(msg, conversation);
            displayMessages();
        }
    }

    public void displayUpdatedMessage() { displayMessages(); }

    public void showFailureMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar v = scrollPane.getVerticalScrollBar();
            v.setValue(v.getMaximum());
        });
    }
}
