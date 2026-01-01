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
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[] {
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

    public void updateShipmentStatus(int shipmentId, String newStatus) throws SQLException {
        String checkQuery = "SELECT p.PaymentStatus FROM SHIPMENTS s JOIN PAYMENTS p ON s.OrderID = p.OrderID WHERE s.ShipmentID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, shipmentId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String paymentStatus = rs.getString("PaymentStatus");
                if (!"completed".equalsIgnoreCase(paymentStatus)) {
                    throw new SQLException("Cannot update shipment: Payment is not completed.");
                }
            } else {
                throw new SQLException("Shipment or associated payment not found.");
            }
        }

        String updateQuery = "UPDATE SHIPMENTS SET ShipmentStatus = ? WHERE ShipmentID = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, shipmentId);
            stmt.executeUpdate();
        }
    }
}
