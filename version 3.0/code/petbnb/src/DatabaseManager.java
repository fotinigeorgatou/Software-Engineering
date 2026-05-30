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

            // Βήμα 1.1: Δημιουργία λογαριασμού με προκαθορισμένα στοιχεία
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(email + ";" + password + ";Unassigned;NewUser;Lastname;18;Athens;No pets;No preferences;0.0;profileimage.jpg");
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
            File file = new File(FILE_NAME);
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(email)) {
                    reader.close();
                    return new User(parts[0], parts[1], parts[2], parts[3], parts[4],
                            parts[5], parts[6], parts[7], parts[8], parts[9],
                            parts.length > 10 ? parts[10] : "profileimage.jpg");
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUser(User user) {
        try {
            File file = new File(FILE_NAME);
            File tempFile = new File("users_temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(user.email)) {
                    writer.write(user.email + ";" + user.password + ";" + user.role + ";" + user.name + ";" +
                            user.lastname + ";" + user.age + ";" + user.location + ";" + user.pets + ";" +
                            user.preferences + ";" + user.rating + ";" + user.profilePicPath);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
            reader.close();
            writer.close();

            file.delete();
            tempFile.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}