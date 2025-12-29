import java.sql.*;

public class StatsService {

    public String getCustomerStatistics(int customerId) {
        StringBuilder statsReport = new StringBuilder("=== Customer Statistics ===\n\n");

        try (Connection conn = DatabaseManager.getConnection()) {

            String q1 = "SELECT DATE_FORMAT(OrderDate, '%Y-%m') AS Month, SUM(TotalAmount) AS Spent " +
                    "FROM ORDERS WHERE CustomerID = ? AND OrderStatus != 'canceled' " +
                    "GROUP BY DATE_FORMAT(OrderDate, '%Y-%m')";
            PreparedStatement s1 = conn.prepareStatement(q1);
            s1.setInt(1, customerId);
            ResultSet rs1 = s1.executeQuery();

            statsReport.append("--- Monthly Spending ---\n");
            while(rs1.next()) {
                statsReport.append(rs1.getString("Month")).append(": $").append(rs1.getDouble("Spent")).append("\n");
            }

            String q2 = "SELECT c.CategoryName, COUNT(oi.ProductID) as Count " +
                    "FROM ORDER_ITEMS oi " +
                    "JOIN ORDERS o ON oi.OrderID = o.OrderID " +
                    "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                    "JOIN CATEGORIES c ON p.CategoryID = c.CategoryID " +
                    "WHERE o.CustomerID = ? " +
                    "GROUP BY c.CategoryID, c.CategoryName " +
                    "ORDER BY Count DESC LIMIT 1";
            PreparedStatement s2 = conn.prepareStatement(q2);
            s2.setInt(1, customerId);
            ResultSet rs2 = s2.executeQuery();

            statsReport.append("\n--- Favorite Category ---\n");
            if(rs2.next()) {
                statsReport.append(rs2.getString("CategoryName")).append(" (").append(rs2.getInt("Count")).append(" items bought)\n");
            }

            String q3 = "SELECT AVG(MonthlyTotal) as AvgSpend FROM (" +
                    "SELECT SUM(TotalAmount) as MonthlyTotal FROM ORDERS " +
                    "WHERE CustomerID = ? AND OrderStatus != 'canceled' " +
                    "GROUP BY DATE_FORMAT(OrderDate, '%Y-%m')) as SubQuery";
            PreparedStatement s3 = conn.prepareStatement(q3);
            s3.setInt(1, customerId);
            ResultSet rs3 = s3.executeQuery();

            statsReport.append("\n--- Average Monthly ---\n");
            if(rs3.next()) {
                statsReport.append("$").append(String.format("%.2f", rs3.getDouble("AvgSpend"))).append("\n");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error retrieving statistics.";
        }
        return statsReport.toString();
    }
}