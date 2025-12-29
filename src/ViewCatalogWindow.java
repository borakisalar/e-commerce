import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewCatalogWindow extends JFrame {

    private final int currentUserId;
    private final CatalogService catalogService;
    private final OrderService orderService;
    private JTable table;
    private DefaultTableModel model;

    public ViewCatalogWindow(int userId) {
        this.currentUserId = userId;
        this.catalogService = new CatalogService();
        this.orderService = new OrderService();

        setTitle("All Catalogs");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Product", "Price", "Stock", "Seller"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);

        loadData();

        JButton btnAdd = new JButton("Add Selected to Cart");
        btnAdd.addActionListener(e -> addToCartAction());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnAdd, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Object[]> rows = catalogService.getAvailableProducts();
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    private void addToCartAction() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first.");
            return;
        }

        int productId = (int) model.getValueAt(row, 0);
        String quantityStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
        if (quantityStr == null) return;

        try {
            int qty = Integer.parseInt(quantityStr);
            orderService.addToCart(currentUserId, productId, qty);
            JOptionPane.showMessageDialog(this, "Added to cart!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}