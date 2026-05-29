public class Host extends User {

    private Accommodation accommodation;

    public Host(String userId, String name, String email,
                Accommodation accommodation) {

        super(userId, name, email);
        this.accommodation = accommodation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }
}