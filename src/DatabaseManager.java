import java.sql.*;

public class DatabaseManager {
    // Database credentials - Update these to match your MySQL configuration
    private static final String URL = "jdbc:mysql://192.168.1.152:3306/ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "_9q@2$KNf>xpVyW-;5)o"; // Update this!

    // Static connection method
    public static Connection getConnection() throws SQLException {
        try {
            // Loading the driver (optional in newer JDBC versions but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // AUTHENTICATION LOGIC
    public static User authenticate(String username, String password) {
        String query = "SELECT UserID, Username, UserRole FROM USERS WHERE Username = ? AND PasswordHash = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Note: In real apps, compare hashes!

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("UserRole"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // REGISTRATION LOGIC
    public static boolean registerUser(String username, String email, String password, String role, String firstName,
            String lastName) {
        String query = "INSERT INTO USERS (Username, Email, PasswordHash, UserRole, FirstName, LastName) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, firstName);
            pstmt.setString(6, lastName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // Handle duplicate entry errors specifically if needed
            if (e.getErrorCode() == 1062) { // MySQL error code for duplicate
                System.err.println("Username or Email already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
