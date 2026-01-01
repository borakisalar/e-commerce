import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminShipmentService {

    public List<Object[]> getAllShipments() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT s.ShipmentID, s.OrderID, s.TrackingNumber, s.ShipmentStatus, p.PaymentStatus " +
                "FROM SHIPMENTS s " +
                "JOIN PAYMENTS p ON s.OrderID = p.OrderID";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("ShipmentID"),
                        rs.getInt("OrderID"),
                        rs.getString("TrackingNumber"),
                        rs.getString("ShipmentStatus"),
                        rs.getString("PaymentStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void updateShipmentStatus(int shipmentId, int orderId, String newStatus) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String checkPayment = "SELECT PaymentStatus FROM PAYMENTS WHERE OrderID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkPayment)) {
                checkStmt.setInt(1, orderId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    String status = rs.getString("PaymentStatus");
                    if (!"completed".equalsIgnoreCase(status)) {
                        throw new SQLException("Cannot update shipment. Payment is not done.");
                    }
                } else {
                    throw new SQLException("No payment found for this order.");
                }
            }

            String update = "UPDATE SHIPMENTS SET ShipmentStatus = ? WHERE ShipmentID = ?";
            try (PreparedStatement upStmt = conn.prepareStatement(update)) {
                upStmt.setString(1, newStatus);
                upStmt.setInt(2, shipmentId);
                upStmt.executeUpdate();
            }
        }
    }
}
