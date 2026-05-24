import javax.swing.*;
import java.awt.*;

public class Notification {
    private int notification_id;
    private String notification_type;
    private String message;
    private static int nextId = 1;

    private static final Color BG_DARK  = new Color(26, 26, 26);
    private static final Color PINK     = new Color(255, 60, 91);
    private static final Color PURPLE   = new Color(193, 163, 229);

    public Notification(String type, String message) {
        this.notification_id = nextId++;
        this.notification_type = type;
        this.message = message;
    }

    public static Notification createNotification(String type, String msg) {
        return new Notification(type, msg);
    }

    public void showNotification(Component parent) {
        JWindow popup = new JWindow(SwingUtilities.getWindowAncestor(parent));
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK, 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel icon = new JLabel("🔔");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel label = new JLabel(message);
        label.setForeground(PURPLE);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        panel.add(icon, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        popup.add(panel);
        popup.pack();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        popup.setLocation(screen.width - popup.getWidth() - 20, screen.height - popup.getHeight() - 60);
        popup.setVisible(true);

        Timer timer = new Timer(3000, e -> popup.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    public String getMessage() { return message; }
    public String getNotification_type() { return notification_type; }
}
