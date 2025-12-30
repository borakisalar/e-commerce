import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageOrdersWindow extends JFrame {
    private int sellerId;
    private SellerService sellerService;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public ManageOrdersWindow(int sellerId) {
        this.sellerId = sellerId;
        this.sellerService = new SellerService();

        setTitle("Manage Orders");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] cols = { "Order ID", "Date", "Customer", "Total Amount", "Status" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        ordersTable = new JTable(tableModel);
        loadOrders();

        add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton updateStatusBtn = new JButton("Update Status");
        updateStatusBtn.addActionListener(e -> updateStatus());
        panel.add(updateStatusBtn);
        add(panel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Object[]> orders = sellerService.getSellerOrders(sellerId);
        for (Object[] row : orders) {
            tableModel.addRow(row);
        }
    }

    private void updateStatus() {
        int row = ordersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order.");
            return;
        }
        int orderId = (int) tableModel.getValueAt(row, 0);
        String currentStatus = (String) tableModel.getValueAt(row, 4);

        String[] statuses = { "pending", "processing", "shipped", "delivered", "canceled" };
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select Status", "Update Status",
                JOptionPane.PLAIN_MESSAGE, null, statuses, currentStatus);

        if (newStatus != null) {
            try {
                sellerService.updateOrderStatus(orderId, newStatus);
                loadOrders();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
