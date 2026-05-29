public class Pet {

    private String petId;
    private String name;
    private String age;
    private String breed;
    private String description;
    private String imagePath;

    public Pet(String petId,
               String name,
               String age,
               String breed,
               String description,
               String imagePath) {

        this.petId = petId;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getPetId() {
        return petId;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}