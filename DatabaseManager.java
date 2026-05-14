import java.sql.*;

public class DatabaseManager {

    private static final String URL =
            "jdbc:mysql://localhost:3306/petbnb_db";

    private static final String USER = "root";
    private static final String PASSWORD = "mysql048130";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String loginOrRegister(String email, String password) {

        String checkQuery =
                "SELECT * FROM users WHERE email=?";

        String insertQuery =
                "INSERT INTO users(email,password) VALUES (?,?)";

        try(Connection conn = getConnection()) {

            // CHECK EMAIL
            PreparedStatement checkStmt =
                    conn.prepareStatement(checkQuery);

            checkStmt.setString(1, email);

            ResultSet rs = checkStmt.executeQuery();

            // USER EXISTS -> LOGIN
            if(rs.next()) {

                String storedPassword =
                        rs.getString("password");

                if(storedPassword.equals(password)) {

                    return "LOGIN_SUCCESS";

                } else {

                    return "WRONG_PASSWORD";
                }
            }

            // USER DOES NOT EXIST -> REGISTER
            PreparedStatement insertStmt =
                    conn.prepareStatement(insertQuery);

            insertStmt.setString(1, email);
            insertStmt.setString(2, password);

            insertStmt.executeUpdate();

            return "REGISTER_SUCCESS";

        } catch(SQLException e) {

            e.printStackTrace();

            return "ERROR";
        }
    }

    public static boolean loginUser(String email, String password) {

        String query =
                "SELECT * FROM users WHERE email=? AND password=?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt =
                    conn.prepareStatement(query)) {

            pstmt.setString(1,email);
            pstmt.setString(2,password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
