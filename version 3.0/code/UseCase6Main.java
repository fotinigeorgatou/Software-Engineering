import javax.swing.*;

public class UseCase6Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            SupportScreen screen = new SupportScreen();
            screen.pressHelp();
        });
    }
}
