import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public void addToCart(int customerId, int productId, int quantity) throws SQLException, IllegalArgumentException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String productQuery = "SELECT Price, c.SellerID FROM PRODUCTS p " +
                    "JOIN CATALOGS c ON p.CatalogID = c.CatalogID " +
                    "WHERE ProductID = ?";
            PreparedStatement productStmt = conn.prepareStatement(productQuery);
            productStmt.setInt(1, productId);
            ResultSet rsProduct = productStmt.executeQuery();

            if (!rsProduct.next())
                throw new SQLException("Product not found.");
            double price = rsProduct.getDouble("Price");
            int productSellerId = rsProduct.getInt("SellerID");

            String orderCheckQuery = "SELECT OrderID, SellerID FROM ORDERS WHERE CustomerID = ? AND OrderStatus = 'pending'";
            PreparedStatement checkStmt = conn.prepareStatement(orderCheckQuery);
            checkStmt.setInt(1, customerId);
            ResultSet rsOrder = checkStmt.executeQuery();

            int orderId = -1;

            if (rsOrder.next()) {
                int currentOrderSellerId = rsOrder.getInt("SellerID");
                orderId = rsOrder.getInt("OrderID");

                if (currentOrderSellerId != productSellerId) {
                    throw new IllegalArgumentException("You can only order from one seller at a time.");
                }
            } else {
                String createOrder = "INSERT INTO ORDERS (CustomerID, SellerID, ShippingAddressID, TotalAmount, OrderStatus) VALUES (?, ?, ?, 0.0, 'pending')";
                PreparedStatement createStmt = conn.prepareStatement(createOrder, Statement.RETURN_GENERATED_KEYS);
                createStmt.setInt(1, customerId);
                createStmt.setInt(2, productSellerId);
                createStmt.setInt(3, 1);
                createStmt.executeUpdate();

                ResultSet rsKey = createStmt.getGeneratedKeys();
                if (rsKey.next())
                    orderId = rsKey.getInt(1);
            }

            String insertItem = "INSERT INTO ORDER_ITEMS (OrderID, ProductID, Quantity, PurchasePrice) VALUES (?, ?, ?, ?) "
                    +
                    "ON DUPLICATE KEY UPDATE Quantity = Quantity + ?";
            PreparedStatement itemStmt = conn.prepareStatement(insertItem);
            itemStmt.setInt(1, orderId);
            itemStmt.setInt(2, productId);
            itemStmt.setInt(3, quantity);
            itemStmt.setDouble(4, price);
            itemStmt.setInt(5, quantity);
            itemStmt.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw ex;
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                }
        }
    }

    public List<Object[]> getCartItems(int customerId) {
        List<Object[]> cartItems = new ArrayList<>();
        String query = "SELECT p.ProductName, oi.Quantity, oi.PurchasePrice, (oi.Quantity * oi.PurchasePrice) as Total "
                +
                "FROM ORDER_ITEMS oi " +
                "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                "JOIN ORDERS o ON oi.OrderID = o.OrderID " +
                "WHERE o.CustomerID = ? AND o.OrderStatus = 'pending'";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cartItems.add(new Object[] {
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("PurchasePrice"),
                        rs.getDouble("Total")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cartItems;
    }

    public void submitOrder(int customerId) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            String findOrder = "SELECT OrderID FROM ORDERS WHERE CustomerID = ? AND OrderStatus = 'pending'";
            PreparedStatement stmt = conn.prepareStatement(findOrder);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                throw new SQLException("No pending order found.");
            int orderId = rs.getInt("OrderID");

            String verifyItems = "SELECT COUNT(*) FROM ORDER_ITEMS WHERE OrderID = ?";
            PreparedStatement vStmt = conn.prepareStatement(verifyItems);
            vStmt.setInt(1, orderId);
            ResultSet vRs = vStmt.executeQuery();
            if (vRs.next() && vRs.getInt(1) == 0)
                throw new SQLException("Cannot submit empty order.");

            String updateTotal = "UPDATE ORDERS SET TotalAmount = (SELECT SUM(Quantity * PurchasePrice) FROM ORDER_ITEMS WHERE OrderID = ?) WHERE OrderID = ?";
            PreparedStatement upTotalStmt = conn.prepareStatement(updateTotal);
            upTotalStmt.setInt(1, orderId);
            upTotalStmt.setInt(2, orderId);
            upTotalStmt.executeUpdate();

            String updateStock = "UPDATE PRODUCTS p JOIN ORDER_ITEMS oi ON p.ProductID = oi.ProductID SET p.StockQuantity = p.StockQuantity - oi.Quantity WHERE oi.OrderID = ?";
            PreparedStatement stockStmt = conn.prepareStatement(updateStock);
            stockStmt.setInt(1, orderId);
            stockStmt.executeUpdate();

            String closeOrder = "UPDATE ORDERS SET OrderStatus = 'processing', OrderDate = CURRENT_TIMESTAMP WHERE OrderID = ?";
            PreparedStatement closeStmt = conn.prepareStatement(closeOrder);
            closeStmt.setInt(1, orderId);
            closeStmt.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException e) {
            }
            throw ex;
        } finally {
            if (conn != null)
                conn.close();
        }
    }

    public List<Object[]> getOrderHistory(int customerId) {
        List<Object[]> history = new ArrayList<>();
        String query = "SELECT OrderID, OrderDate, TotalAmount, OrderStatus FROM ORDERS WHERE CustomerID = ? ORDER BY OrderDate DESC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add(new Object[] {
                        rs.getInt("OrderID"),
                        rs.getTimestamp("OrderDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getString("OrderStatus")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return history;
    }

    public List<Object[]> getOrderItems(int orderId) {
        List<Object[]> items = new ArrayList<>();
        String query = "SELECT oi.OrderItemID, p.ProductName, oi.Quantity, oi.PurchasePrice " +
                "FROM ORDER_ITEMS oi " +
                "JOIN PRODUCTS p ON oi.ProductID = p.ProductID " +
                "WHERE oi.OrderID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Object[] {
                        rs.getInt("OrderItemID"),
                        rs.getString("ProductName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("PurchasePrice")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return items;
    }

    public void addReview(int orderItemId, int customerId, int rating, String comment) throws SQLException {
        String query = "INSERT INTO REVIEWS (OrderItemID, CustomerID, Rating, Comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderItemId);
            stmt.setInt(2, customerId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();
        }
    }
}