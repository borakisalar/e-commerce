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

        String[] columns = { "Order ID", "Date", "Total Amount", "Status" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton reviewBtn = new JButton("View Items & Review");
        reviewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int orderId = (int) model.getValueAt(row, 0);
                String status = (String) model.getValueAt(row, 3);
                showOrderItems(orderId, status);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order.");
            }
        });
        buttonPanel.add(reviewBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadHistory();
    }

    private void loadHistory() {
        model.setRowCount(0);
        List<Object[]> rows = orderService.getOrderHistory(currentUserId);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    private void showOrderItems(int orderId, String status) {
        JDialog itemsDialog = new JDialog(this, "Order Items - Order #" + orderId, true);
        itemsDialog.setSize(500, 400);
        itemsDialog.setLayout(new BorderLayout());
        itemsDialog.setLocationRelativeTo(this);

        String[] cols = { "Item ID", "Product", "Qty", "Price" };
        DefaultTableModel itemsModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable itemsTable = new JTable(itemsModel);
        List<Object[]> items = orderService.getOrderItems(orderId);
        for (Object[] item : items)
            itemsModel.addRow(item);

        JButton leaveReviewBtn = new JButton("Leave Review");
        leaveReviewBtn.addActionListener(e -> {
            int row = itemsTable.getSelectedRow();
            if (row != -1) {
                if (!"delivered".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(itemsDialog,
                            "You can only review items after the order is delivered.");
                    return;
                }
                int orderItemId = (int) itemsModel.getValueAt(row, 0);
                String productName = (String) itemsModel.getValueAt(row, 1);
                openReviewDialog(orderItemId, productName);
            } else {
                JOptionPane.showMessageDialog(itemsDialog, "Please select an item to review.");
            }
        });

        itemsDialog.add(new JScrollPane(itemsTable), BorderLayout.CENTER);
        itemsDialog.add(leaveReviewBtn, BorderLayout.SOUTH);
        itemsDialog.setVisible(true);
    }

    private void openReviewDialog(int orderItemId, String productName) {
        JDialog reviewDialog = new JDialog(this, "Review - " + productName, true);
        reviewDialog.setSize(400, 320);
        reviewDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("Rating (1-5):"));
        String[] ratings = { "1", "2", "3", "4", "5" };
        JComboBox<String> ratingBox = new JComboBox<>(ratings);
        ratingBox.setSelectedIndex(4);
        ratingPanel.add(ratingBox);

        JPanel commentPanel = new JPanel(new BorderLayout(0, 5));
        commentPanel.add(new JLabel("Your Comment:"), BorderLayout.NORTH);
        JTextArea commentArea = new JTextArea(6, 20);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setMargin(new Insets(5, 5, 5, 5));
        commentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Review");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setPreferredSize(new Dimension(150, 40));
        submitBtn.addActionListener(e -> {
            int rating = Integer.parseInt((String) ratingBox.getSelectedItem());
            String comment = commentArea.getText().trim();
            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(reviewDialog, "Please enter a comment.");
                return;
            }
            try {
                orderService.addReview(orderItemId, currentUserId, rating, comment);
                JOptionPane.showMessageDialog(reviewDialog, "Review submitted successfully!");
                reviewDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(reviewDialog, "Error submitting review: " + ex.getMessage());
            }
        });

        mainPanel.add(ratingPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(commentPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(submitBtn);

        reviewDialog.add(mainPanel);
        reviewDialog.setVisible(true);
    }
}