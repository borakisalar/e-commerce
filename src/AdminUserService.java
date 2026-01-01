import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserService {

    public List<Object[]> getAllUsers() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT UserID, Username, Email, UserRole, FirstName, LastName " + "FROM USERS";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[] {
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Email"),
                        rs.getString("UserRole"),
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addUser(String username, String email, String pass, String role, String first, String last)
            throws SQLException {
        String query = "INSERT INTO USERS (Username, Email, PasswordHash, UserRole, FirstName, LastName) VALUES (?, ?, ?, ?, ?, ?)";
        String hashedPassword = PasswordHasher.hashPassword(pass);
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, role);
            stmt.setString(5, first);
            stmt.setString(6, last);
            stmt.executeUpdate();
        }
    }

    public void updateUser(int userId, String email, String password, String role, String first, String last)
            throws SQLException {
        boolean updatePassword = password != null && !password.isEmpty();
        String query;
        if (updatePassword) {
            query = "UPDATE USERS SET Email = ?, PasswordHash = ?, UserRole = ?, FirstName = ?, LastName = ? WHERE UserID = ?";
        } else {
            query = "UPDATE USERS SET Email = ?, UserRole = ?, FirstName = ?, LastName = ? WHERE UserID = ?";
        }

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            int idx = 1;
            stmt.setString(idx++, email);
            if (updatePassword) {
                stmt.setString(idx++, PasswordHasher.hashPassword(password));
            }
            stmt.setString(idx++, role);
            stmt.setString(idx++, first);
            stmt.setString(idx++, last);
            stmt.setInt(idx++, userId);
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM USERS WHERE UserID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
