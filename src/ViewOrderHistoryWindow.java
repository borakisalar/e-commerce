import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewOrderHistoryWindow extends JFrame {

    private final int currentUserId;
    private final OrderService orderService;
    private DefaultTableModel model;

    public ViewOrderHistoryWindow(int userId) {
        this.currentUserId = userId;
        this.orderService = new OrderService();

        setTitle("Order History");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Order ID", "Date", "Total Amount", "Status"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadHistory();
    }

    private void loadHistory() {
        model.setRowCount(0);
        List<Object[]> rows = orderService.getOrderHistory(currentUserId);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
}