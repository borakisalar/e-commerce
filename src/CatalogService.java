import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {

    public List<Object[]> getAvailableProducts() {
        List<Object[]> products = new ArrayList<>();
        String query = "SELECT p.ProductID, p.ProductName, cat.CategoryName, p.Description, p.Price, p.StockQuantity, u.Username AS Seller, "
                +
                "AVG(r.Rating) as AvgRating, COUNT(r.ReviewID) as ReviewCount " +
                "FROM PRODUCTS p " +
                "JOIN CATALOGS c ON p.CatalogID = c.CatalogID " +
                "JOIN USERS u ON c.SellerID = u.UserID " +
                "LEFT JOIN CATEGORIES cat ON p.CategoryID = cat.CategoryID " +
                "LEFT JOIN ORDER_ITEMS oi ON p.ProductID = oi.ProductID " +
                "LEFT JOIN REVIEWS r ON oi.OrderItemID = r.OrderItemID " +
                "WHERE p.StockQuantity > 0 " +
                "GROUP BY p.ProductID, p.ProductName, cat.CategoryName, p.Description, p.Price, p.StockQuantity, u.Username";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                double avg = rs.getDouble("AvgRating");
                int count = rs.getInt("ReviewCount");
                String ratingStr = count > 0 ? String.format("%.1f (%d reviews)", avg, count) : "No reviews";

                products.add(new Object[] {
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("CategoryName"),
                        rs.getString("Description"),
                        rs.getBigDecimal("Price"),
                        rs.getInt("StockQuantity"),
                        rs.getString("Seller"),
                        ratingStr
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    public List<Object[]> getProductReviews(int productId) {
        List<Object[]> reviews = new ArrayList<>();
        String query = "SELECT r.Rating, r.Comment, u.Username, r.ReviewDate " +
                "FROM REVIEWS r " +
                "JOIN ORDER_ITEMS oi ON r.OrderItemID = oi.OrderItemID " +
                "JOIN USERS u ON r.CustomerID = u.UserID " +
                "WHERE oi.ProductID = ? " +
                "ORDER BY r.ReviewDate DESC";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Object[] {
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getString("Username"),
                        rs.getTimestamp("ReviewDate")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reviews;
    }
}