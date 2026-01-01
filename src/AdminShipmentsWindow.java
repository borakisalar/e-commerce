import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminShipmentsWindow extends JFrame {
    private AdminShipmentService service = new AdminShipmentService();
    private JTable table;
    private DefaultTableModel model;

    public AdminShipmentsWindow() {
        setTitle("Manage Shipments");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new Object[] { "ID", "Order ID", "Tracking #", "Shipment Status", "Payment Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");

        updateBtn.addActionListener(e -> updateStatus());
        refreshBtn.addActionListener(e -> loadData());

        buttonPanel.add(updateBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<Object[]> shipments = service.getAllShipments();
        for (Object[] row : shipments) {
            model.addRow(row);
        }
    }

    private void updateStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a shipment.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String currentPayment = (String) model.getValueAt(row, 4);

        if (!"completed".equalsIgnoreCase(currentPayment)) {
            JOptionPane.showMessageDialog(this, "Error: You can only update shipments when payment is 'completed'.");
            return;
        }

        String[] statuses = { "processing", "shipped", "in_transit", "delivered" };
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Shipment",
                JOptionPane.QUESTION_MESSAGE, null, statuses, model.getValueAt(row, 3));

        if (newStatus != null) {
            try {
                service.updateShipmentStatus(id, newStatus);
                loadData();
                JOptionPane.showMessageDialog(this, "Status updated successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}
