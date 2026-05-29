import javax.swing.*;

class DBManager {
    public boolean status = true;
    public void saveResults() {
        if (status) {
            JOptionPane.showMessageDialog(null, "Τα δεδομένα αποθηκεύτηκαν επιτυχώς στη Βάση Δεδομένων!", "DB Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}