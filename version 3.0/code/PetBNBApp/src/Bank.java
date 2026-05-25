import javax.swing.*;
import java.awt.*;

// Τράπεζα για τον έλεγχο των στοιχείων
public class Bank {
    public boolean checkData(String cardNo, String balanceCheck) {
        return !(cardNo.trim().isEmpty() || balanceCheck.equalsIgnoreCase("error") || balanceCheck.equals("0"));
    }
}

