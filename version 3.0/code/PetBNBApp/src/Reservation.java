public class Reservation {
    int hostId, petId, reservationId;
    String dates;
    double payment;

    public Reservation(int hostId, int petId, String dates, int reservationId, double payment) {
        this.hostId = hostId;
        this.petId = petId;
        this.dates = dates;
        this.reservationId = reservationId;
        this.payment = payment;
    }
}
