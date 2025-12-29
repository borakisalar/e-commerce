import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewCartWindow extends JFrame {

    private final int currentUserId;
    private final OrderService orderService;
    private DefaultTableModel model;
    private JLabel totalLabel;

    public ViewCartWindow(int userId) {
        this.currentUserId = userId;
        this.orderService = new OrderService();

        setTitle("My Shopping Cart");
        setSize(600, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Product", "Quantity", "Price", "Total"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnSubmit = new JButton("Submit Order");
        btnSubmit.addActionListener(e -> submitOrderAction());

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnSubmit, BorderLayout.EAST);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadCart();
    }

    private void loadCart() {
        model.setRowCount(0);
        List<Object[]> rows = orderService.getCartItems(currentUserId);
        double grandTotal = 0.0;
        for (Object[] row : rows) {
            model.addRow(row);
            grandTotal += (Double) row[3];
        }
        totalLabel.setText("Total: $" + String.format("%.2f", grandTotal));
    }

    private void submitOrderAction() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit this order?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                orderService.submitOrder(currentUserId);
                JOptionPane.showMessageDialog(this, "Order Submitted Successfully!");
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}