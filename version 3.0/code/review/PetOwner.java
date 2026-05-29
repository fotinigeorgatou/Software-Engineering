public class PetOwner extends User {

    private Pet pet;

    public PetOwner(String userId,
                    String name,
                    String email,
                    Pet pet) {

        super(userId, name, email);
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }
}