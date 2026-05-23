import java.io.*;

public class DatabaseManager {

    private static final String FILE_NAME = "users.txt";

    public static String loginOrRegister(String email, String password) {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(email)) {
                    reader.close();
                    if (parts[1].equals(password)) {
                        return "LOGIN_SUCCESS";
                    } else {
                        return "WRONG_PASSWORD";
                    }
                }
            }
            reader.close();

            // REGISTER NEW USER WITH DEFAULT AVATAR VALUE
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(
                    email + ";" +
                            password + ";" +
                            "Pet Owner;" +
                            "NewUser;" +
                            "Lastname;" +
                            "18;" +
                            "Athens;" +
                            "No pets;" +
                            "No preferences;" +
                            "0.0;" +
                            "profileimage.jpg" // Default fallback filename
            );
            writer.newLine();
            writer.close();

            return "REGISTER_SUCCESS";

        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static User getUser(String email) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts[0].equals(email)) {
                    reader.close();

                    return new User(
                            parts[0], // email
                            parts[1], // password
                            parts[2], // role
                            parts[3], // name
                            parts[4], // lastname
                            parts[5], // age
                            parts[6], // location
                            parts[7], // pets
                            parts[8], // preferences
                            parts[9], // rating
                            parts.length > 10 ? parts[10] : "profileimage.jpg" // Safe profilePicPath check
                    );
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
