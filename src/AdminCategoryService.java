import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminCategoryService {

    public List<Object[]> getAllCategories() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT CategoryID, CategoryName, Description FROM CATEGORIES";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void addCategory(String name, String desc) throws SQLException {
        String query = "INSERT INTO CATEGORIES (CategoryName, Description) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.executeUpdate();
        }
    }

    public void updateCategory(int id, String name, String desc) throws SQLException {
        String query = "UPDATE CATEGORIES SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }
    public void deleteCategory(int id) throws SQLException {
        String query = "DELETE FROM CATEGORIES WHERE CategoryID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
