import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class UserList extends JFrame {

    private static final Color BG_DARK    = new Color(26, 26, 26);
    private static final Color CARD_WHITE = new Color(249, 250, 243);
    private static final Color INPUT_GRAY = new Color(223, 223, 223);
    private static final Color PINK       = new Color(255, 60, 91);
    private static final Color PURPLE     = new Color(193, 163, 229);

    private String currentUser;
    private List<String> users;

    public UserList(String currentUser) {
        this.currentUser = currentUser;
        this.users = new ArrayList<>();
        loadUsers();
        initUI();
    }

    private void loadUsers() {
        users.add("Georgatou Fwteinh");
        users.add("Katsanta Vivi");
        users.add("Senko Kristian");
        users.add("Soukisian Mairy");
    }

    private void initUI() {
        setTitle("petbnb");
        setSize(450, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.add(new ListCard());
        add(wrapper);
    }

    class ListCard extends JPanel {
        public ListCard() {
            setOpaque(false);
            setPreferredSize(new Dimension(380, 620));
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(25, 25, 25, 25));

            // Title
            JLabel title = new JLabel("Επιλογή Χρήστη", SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            title.setBorder(new EmptyBorder(0, 0, 5, 0));

            JLabel subtitle = new JLabel("Συνδεδεμένη ως: " + currentUser, SwingConstants.CENTER);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            subtitle.setForeground(new Color(120, 120, 120));
            subtitle.setBorder(new EmptyBorder(0, 0, 20, 0));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setOpaque(false);
            topPanel.add(title);
            topPanel.add(subtitle);

            // Users list
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setOpaque(false);

            for (String user : users) {
                listPanel.add(createUserRow(user));
                listPanel.add(Box.createVerticalStrut(10));
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);

            add(topPanel, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);
        }

        private JPanel createUserRow(String userName) {
            JPanel row = new JPanel(new BorderLayout(12, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    g2.dispose();
                }
            };
            row.setOpaque(false);
            row.setBackground(INPUT_GRAY);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
            row.setPreferredSize(new Dimension(330, 68));
            row.setBorder(new EmptyBorder(10, 14, 10, 14));

            // Avatar
            JLabel avatar = new JLabel(String.valueOf(userName.charAt(0)).toUpperCase(), SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(PINK);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            avatar.setFont(new Font("SansSerif", Font.BOLD, 20));
            avatar.setForeground(PURPLE);
            avatar.setOpaque(false);
            avatar.setPreferredSize(new Dimension(46, 46));

            // Name
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            namePanel.setOpaque(false);

            JLabel nameLabel = new JLabel(userName);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setForeground(new Color(30, 30, 30));

            JLabel hint = new JLabel("Πατήστε για προφίλ");
            hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
            hint.setForeground(new Color(130, 130, 130));

            namePanel.add(nameLabel);
            namePanel.add(Box.createVerticalStrut(2));
            namePanel.add(hint);

            JLabel arrow = new JLabel("›");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
            arrow.setForeground(PINK);

            row.add(avatar, BorderLayout.WEST);
            row.add(namePanel, BorderLayout.CENTER);
            row.add(arrow, BorderLayout.EAST);

            row.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    row.setBackground(new Color(210, 210, 210));
                    row.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(MouseEvent e) {
                    row.setBackground(INPUT_GRAY);
                }
                public void mouseClicked(MouseEvent e) {
                    selectUser(userName);
                }
            });

            return row;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CARD_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.dispose();
        }
    }

    public void openUserList() { setVisible(true); }

    public void selectUser(String userName) {
        UserProfile profile = new UserProfile(userName, currentUser);
        profile.displayProfile();
    }

    public String getUser(int index) {
        if (index >= 0 && index < users.size()) return users.get(index);
        return null;
    }
}
