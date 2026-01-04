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
        setSize(1000, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = { "ID", "Product", "Category", "Description", "Price", "Stock", "Seller", "Rating" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        loadData();

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Selected to Cart");
        JButton btnReviews = new JButton("View Reviews");

        btnAdd.addActionListener(e -> addToCartAction());
        btnReviews.addActionListener(e -> showReviewsAction());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnReviews);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
        if (quantityStr == null)
            return;

        try {
            int qty = Integer.parseInt(quantityStr);
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.");
                return;
            }
            orderService.addToCart(currentUserId, productId, qty);
            JOptionPane.showMessageDialog(this, "Added to cart!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding to cart: " + ex.getMessage());
        }
    }

    private void showReviewsAction() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to see reviews.");
            return;
        }

        int productId = (int) model.getValueAt(row, 0);
        String productName = (String) model.getValueAt(row, 1);
        List<Object[]> reviews = catalogService.getProductReviews(productId);

        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reviews yet for " + productName);
            return;
        }

        String[] revCols = { "Rating", "Comment", "User", "Date" };
        DefaultTableModel revModel = new DefaultTableModel(revCols, 0);
        for (Object[] rev : reviews) {
            revModel.addRow(rev);
        }

        JTable revTable = new JTable(revModel);
        JScrollPane scroll = new JScrollPane(revTable);
        scroll.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scroll, "Reviews for " + productName, JOptionPane.PLAIN_MESSAGE);
    }
}