import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminStatsService {

    public double getTotalSales() {
        String query = "SELECT SUM(TotalAmount) FROM ORDERS WHERE OrderStatus != 'canceled'";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getTopSellingCategories() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT c.CategoryName, SUM(oi.Quantity) as TotalSold " +
                "FROM ORDER_ITEMS oi " +
                "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                "JOIN CATEGORIES c ON p.CategoryID = c.CategoryID " +
                "GROUP BY c.CategoryID, c.CategoryName " +
                "ORDER BY TotalSold DESC LIMIT 5";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[] { rs.getString("CategoryName"), rs.getInt("TotalSold") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getMostPopularItems() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT p.ProductName, SUM(oi.Quantity) as TotalSold " +
                "FROM ORDER_ITEMS oi " +
                "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                "GROUP BY p.ProductID, p.ProductName " +
                "ORDER BY TotalSold DESC LIMIT 5";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[] { rs.getString("ProductName"), rs.getInt("TotalSold") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getTopSellers() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT u.FirstName, u.LastName, SUM(o.TotalAmount) as TotalRevenue " +
                "FROM ORDERS o " +
                "JOIN USERS u ON o.SellerID = u.UserID " +
                "GROUP BY o.SellerID, u.FirstName, u.LastName " +
                "ORDER BY TotalRevenue DESC LIMIT 5";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[] { rs.getString("FirstName") + " " + rs.getString("LastName"),
                        rs.getDouble("TotalRevenue") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
