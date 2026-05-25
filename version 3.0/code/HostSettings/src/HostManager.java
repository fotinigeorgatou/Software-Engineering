import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class HostManager {
    private DBManager dbManager = new DBManager();
    private HostProfile hostProfile = new HostProfile();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public boolean validateData(String priceNight, String priceSrv, String arrivalStr, String departureStr) {
        try {
            Double.parseDouble(priceNight);
            Double.parseDouble(priceSrv);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε έγκυρους αριθμούς στα πεδία τιμών!", "Σφάλμα Εγκυρότητας", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Έλεγχος ημερομηνιών
        try {
            LocalDate arrival = LocalDate.parse(arrivalStr, formatter);
            LocalDate departure = LocalDate.parse(departureStr, formatter);

            if (!departure.isAfter(arrival)) {
                JOptionPane.showMessageDialog(null, "Η ημερομηνία αποχώρησης πρέπει να είναι μετά την ημερομηνία άφιξης!", "Σφάλμα Ημερομηνίας", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε τις ημερομηνίες στη μορφή ΗΗ/ΜΜ/ΕΕΕΕ (π.χ. 01/06/2026)!", "Σφάλμα Μορφής", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    public long calculateNights(String arrivalStr, String departureStr) {
        LocalDate arrival = LocalDate.parse(arrivalStr, formatter);
        LocalDate departure = LocalDate.parse(departureStr, formatter);
        return ChronoUnit.DAYS.between(arrival, departure);
    }

    // calcFee: (Τιμή ανά νύχτα * Σύνολο νυχτών) + Κόστος Υπηρεσίας
    public double calcFee(double priceNight, long nights, double priceSrv) {
        return (priceNight * nights) + priceSrv;
    }

    public void executeSave() {
        hostProfile.availabilityActivation();
        dbManager.saveResults();
    }
}
