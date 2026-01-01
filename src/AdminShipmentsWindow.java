import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
public class AdminShipmentsWindow extends JFrame {
    private DefaultTableModel tableModel;
    private JTable shipmentsTable;
    private JButton updateStatusButton;

    public AdminShipmentsWindow() {
        setTitle("Shipments");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setContentPane(contentPanel);

        tableModel = new DefaultTableModel(new String[]{"ID", "Order", "Track", "Status"}, 0);
        shipmentsTable = new JTable(tableModel);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT ShipmentID, OrderID, TrackingNumber, ShipmentStatus FROM SHIPMENTS");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) tableModel.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)});
        } catch (Exception e) {}

        updateStatusButton = new JButton("Update Status");
        updateStatusButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        updateStatusButton.addActionListener(e -> UpdateStatus());

        contentPanel.add(new JScrollPane(shipmentsTable));
        contentPanel.add(updateStatusButton);
    }

    private void UpdateStatus() {
        int selectedRow = shipmentsTable.getSelectedRow();
        if (selectedRow != -1) {
            String newStatus = JOptionPane.showInputDialog("Status:");
            if (newStatus != null) {
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("UPDATE SHIPMENTS SET ShipmentStatus = ? WHERE ShipmentID = ?")) {
                    stmt.setString(1, newStatus);
                    stmt.setInt(2, (int)tableModel.getValueAt(selectedRow, 0));
                    stmt.executeUpdate();
                } catch (Exception ex) {}
            }
        }
    }
}
