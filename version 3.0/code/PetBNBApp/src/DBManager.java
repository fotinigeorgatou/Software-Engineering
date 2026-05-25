import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private List<Reservation> database = new ArrayList<>();
    private int idCounter = 1;

    public boolean confirmReservation(int hostId, int petId, String dates, double totalAmount) {
        database.add(new Reservation(hostId, petId, dates, idCounter++, totalAmount));
        return true;
    }

    public double calcHostPayment(double totalAmount) {
        return totalAmount - (totalAmount * 0.15);
    }
}
