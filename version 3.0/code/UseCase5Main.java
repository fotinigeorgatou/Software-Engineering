import javax.swing.*;

public class UseCase5Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            UserList userList = new UserList("Eleftheria");
            userList.openUserList();
        });
    }
}
