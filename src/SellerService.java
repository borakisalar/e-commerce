import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerService {

    private int getOrCreateCatalogId(Connection conn, int sellerId) throws SQLException {
        String findCatalog = "SELECT CatalogID FROM CATALOGS WHERE SellerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(findCatalog)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("CatalogID");
            }
        }

        String createCatalog = "INSERT INTO CATALOGS (SellerID, CatalogName) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(createCatalog, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, sellerId);
            stmt.setString(2, "Seller " + sellerId + " Catalog");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to create catalog for seller.");
    }

    public List<Object[]> getSellerProducts(int sellerId) {
        List<Object[]> products = new ArrayList<>();
        String query = "SELECT p.ProductID, p.ProductName, p.Description, p.Price, p.StockQuantity, c.CategoryName " +
                "FROM PRODUCTS p " +
                "JOIN CATALOGS cat ON p.CatalogID = cat.CatalogID " +
                "LEFT JOIN CATEGORIES c ON p.CategoryID = c.CategoryID " +
                "WHERE cat.SellerID = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Object[] {
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Description"),
                        rs.getDouble("Price"),
                        rs.getInt("StockQuantity"),
                        rs.getString("CategoryName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void addProduct(int sellerId, String name, String desc, double price, int stock, String categoryName)
            throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            int catalogId = getOrCreateCatalogId(conn, sellerId);

            int categoryId = -1;
            String catQuery = "SELECT CategoryID FROM CATEGORIES WHERE CategoryName = ?";
            PreparedStatement catStmt = conn.prepareStatement(catQuery);
            catStmt.setString(1, categoryName);
            ResultSet rsCat = catStmt.executeQuery();
            if (rsCat.next()) {
                categoryId = rsCat.getInt("CategoryID");
            } else {
                throw new SQLException("Category not found: " + categoryName);
            }

            String insert = "INSERT INTO PRODUCTS (CatalogID, CategoryID, ProductName, Description, Price, StockQuantity) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, catalogId);
            stmt.setInt(2, categoryId);
            stmt.setString(3, name);
            stmt.setString(4, desc);
            stmt.setDouble(5, price);
            stmt.setInt(6, stock);
            stmt.executeUpdate();
        }
    }

    public void updateProduct(int productId, String name, String desc, double price, int stock) throws SQLException {
        String update = "UPDATE PRODUCTS SET ProductName = ?, Description = ?, Price = ?, StockQuantity = ? WHERE ProductID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setDouble(3, price);
            stmt.setInt(4, stock);
            stmt.setInt(5, productId);
            stmt.executeUpdate();
        }
    }

    public void removeProduct(int productId) throws SQLException {
        String delete = "DELETE FROM PRODUCTS WHERE ProductID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }

    public List<String> getCategories() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT CategoryName FROM CATEGORIES")) {
            while (rs.next())
                list.add(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getSellerOrders(int sellerId) {
        List<Object[]> orders = new ArrayList<>();
        String query = "SELECT o.OrderID, o.OrderDate, u.Username AS Customer, o.TotalAmount, o.OrderStatus " +
                "FROM ORDERS o " +
                "JOIN USERS u ON o.CustomerID = u.UserID " +
                "WHERE o.SellerID = ? " +
                "ORDER BY o.OrderDate DESC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Object[] {
                        rs.getInt("OrderID"),
                        rs.getTimestamp("OrderDate"),
                        rs.getString("Customer"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getString("OrderStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String update = "UPDATE ORDERS SET OrderStatus = ? WHERE OrderID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    public String getSellerStatistics(int sellerId) {
        StringBuilder sb = new StringBuilder("=== Seller Statistics ===\n\n");

        try (Connection conn = DatabaseManager.getConnection()) {
            String q1 = "SELECT DATE_FORMAT(OrderDate, '%Y-%m') as Month, SUM(TotalAmount) as Revenue " +
                    "FROM ORDERS WHERE SellerID = ? AND OrderStatus != 'canceled' " +
                    "GROUP BY DATE_FORMAT(OrderDate, '%Y-%m')";
            PreparedStatement s1 = conn.prepareStatement(q1);
            s1.setInt(1, sellerId);
            ResultSet rs1 = s1.executeQuery();
            sb.append("--- Monthly Revenue ---\n");
            while (rs1.next()) {
                sb.append(rs1.getString("Month")).append(": $").append(rs1.getDouble("Revenue")).append("\n");
            }

            String q2 = "SELECT p.ProductName, SUM(oi.Quantity) as TotalSold " +
                    "FROM ORDER_ITEMS oi " +
                    "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                    "JOIN CATALOGS c ON p.CatalogID = c.CatalogID " +
                    "WHERE c.SellerID = ? " +
                    "GROUP BY p.ProductID " +
                    "ORDER BY TotalSold DESC LIMIT 1";
            PreparedStatement s2 = conn.prepareStatement(q2);
            s2.setInt(1, sellerId);
            ResultSet rs2 = s2.executeQuery();
            sb.append("\n--- Best Selling Product ---\n");
            if (rs2.next()) {
                sb.append(rs2.getString("ProductName")).append(" (").append(rs2.getInt("TotalSold"))
                        .append(" units)\n");
            }

            String q3 = "SELECT p.ProductName, COUNT(r.ReviewID) as ReviewCount " +
                    "FROM REVIEWS r " +
                    "JOIN ORDER_ITEMS oi ON r.OrderItemID = oi.OrderItemID " +
                    "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                    "JOIN CATALOGS c ON p.CatalogID = c.CatalogID " +
                    "WHERE c.SellerID = ? " +
                    "GROUP BY p.ProductID " +
                    "ORDER BY ReviewCount DESC LIMIT 1";
            PreparedStatement s3 = conn.prepareStatement(q3);
            s3.setInt(1, sellerId);
            ResultSet rs3 = s3.executeQuery();
            sb.append("\n--- Most Rated Product ---\n");
            if (rs3.next()) {
                sb.append(rs3.getString("ProductName")).append(" (").append(rs3.getInt("ReviewCount"))
                        .append(" reviews)\n");
            }

            String q4 = "SELECT AVG(TotalAmount) as AvgVal FROM ORDERS WHERE SellerID = ? AND OrderStatus != 'canceled'";
            PreparedStatement s4 = conn.prepareStatement(q4);
            s4.setInt(1, sellerId);
            ResultSet rs4 = s4.executeQuery();
            sb.append("\n--- Average Order Value ---\n");
            if (rs4.next()) {
                sb.append("$").append(String.format("%.2f", rs4.getDouble("AvgVal"))).append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error calculating statistics.";
        }
        return sb.toString();
    }
}
