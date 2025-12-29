import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {

    public List<Object[]> getAvailableProducts() {
        List<Object[]> products = new ArrayList<>();
        String query = "SELECT p.ProductID, p.ProductName, p.Price, p.StockQuantity, u.Username AS Seller " +
                "FROM PRODUCTS p " +
                "JOIN CATALOGS c ON p.CatalogID = c.CatalogID " +
                "JOIN USERS u ON c.SellerID = u.UserID " +
                "WHERE p.StockQuantity > 0";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getBigDecimal("Price"),
                        rs.getInt("StockQuantity"),
                        rs.getString("Seller")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }
}