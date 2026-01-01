import java.sql.*;

public class AdminStatsService {

    public String getSystemStatistics() {
        StringBuilder sb = new StringBuilder("=== System Statistics ===\n\n");

        try (Connection conn = DatabaseManager.getConnection()) {

            String q1 = "SELECT SUM(TotalAmount) as TotalSales FROM ORDERS WHERE OrderStatus != 'canceled'";
            try (Statement s1 = conn.createStatement(); ResultSet rs1 = s1.executeQuery(q1)) {
                if(rs1.next()) {
                    sb.append("Total Sales Revenue: $").append(rs1.getDouble("TotalSales")).append("\n\n");
                }
            }

            String q2 = "SELECT c.CategoryName, COUNT(oi.ProductID) as SalesCount " +
                    "FROM ORDER_ITEMS oi " +
                    "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                    "JOIN CATEGORIES c ON p.CategoryID = c.CategoryID " +
                    "GROUP BY c.CategoryID, c.CategoryName " +
                    "ORDER BY SalesCount DESC LIMIT 3";
            sb.append("--- Top Selling Categories ---\n");
            try (Statement s2 = conn.createStatement(); ResultSet rs2 = s2.executeQuery(q2)) {
                while(rs2.next()) {
                    sb.append(rs2.getString("CategoryName")).append(": ").append(rs2.getInt("SalesCount")).append(" items\n");
                }
            }

            String q3 = "SELECT u.Username, SUM(o.TotalAmount) as Revenue " +
                    "FROM ORDERS o " +
                    "JOIN USERS u ON o.SellerID = u.UserID " +
                    "WHERE o.OrderStatus != 'canceled' " +
                    "GROUP BY u.UserID, u.Username " +
                    "ORDER BY Revenue DESC LIMIT 3";
            sb.append("\n--- Top Sellers (Revenue) ---\n");
            try (Statement s3 = conn.createStatement(); ResultSet rs3 = s3.executeQuery(q3)) {
                while(rs3.next()) {
                    sb.append(rs3.getString("Username")).append(": $").append(rs3.getDouble("Revenue")).append("\n");
                }
            }

            String q4 = "SELECT p.ProductName, COUNT(oi.ProductID) as Volume " +
                    "FROM ORDER_ITEMS oi " +
                    "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                    "GROUP BY p.ProductID, p.ProductName " +
                    "ORDER BY Volume DESC LIMIT 3";
            sb.append("\n--- Most Popular Items ---\n");
            try (Statement s4 = conn.createStatement(); ResultSet rs4 = s4.executeQuery(q4)) {
                while(rs4.next()) {
                    sb.append(rs4.getString("ProductName")).append(": ").append(rs4.getInt("Amount")).append(" sold\n");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error calculating statistics.";
        }
        return sb.toString();
    }
}
