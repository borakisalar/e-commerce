import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageProductsWindow extends JFrame {
    private int sellerId;
    private SellerService sellerService;
    private JTable productsTable;
    private DefaultTableModel tableModel;

    public ManageProductsWindow(int sellerId) {
        this.sellerId = sellerId;
        this.sellerService = new SellerService();

        setTitle("Manage Products");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = { "ID", "Name", "Description", "Price", "Stock", "Category" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(tableModel);
        loadProducts();

        add(new JScrollPane(productsTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add Product");
        JButton updateBtn = new JButton("Update Product");
        JButton removeBtn = new JButton("Remove Product");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> showAddProductDialog());
        updateBtn.addActionListener(e -> showUpdateProductDialog());
        removeBtn.addActionListener(e -> removeSelectedProduct());
        refreshBtn.addActionListener(e -> loadProducts());

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Object[]> products = sellerService.getSellerProducts(sellerId);
        for (Object[] p : products) {
            tableModel.addRow(p);
        }
    }

    private void showAddProductDialog() {
        JDialog dialog = new JDialog(this, "Add Product", true);
        dialog.setLayout(new GridLayout(6, 2));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        List<String> categories = sellerService.getCategories();
        JComboBox<String> categoryBox = new JComboBox<>(categories.toArray(new String[0]));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Description:"));
        dialog.add(descField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Stock:"));
        dialog.add(stockField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryBox);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String cat = (String) categoryBox.getSelectedItem();

                if (name.isEmpty() || price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(dialog, "Invalid input.");
                    return;
                }

                sellerService.addProduct(sellerId, name, desc, price, stock, cat);
                JOptionPane.showMessageDialog(dialog, "Product added successfully!");
                dialog.dispose();
                loadProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        dialog.add(new JLabel(""));
        dialog.add(saveBtn);

        dialog.setVisible(true);
    }

    private void showUpdateProductDialog() {
        int row = productsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String desc = (String) tableModel.getValueAt(row, 2);
        double price = (double) tableModel.getValueAt(row, 3);
        int stock = (int) tableModel.getValueAt(row, 4);

        JDialog dialog = new JDialog(this, "Update Product", true);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField(name);
        JTextField descField = new JTextField(desc);
        JTextField priceField = new JTextField(String.valueOf(price));
        JTextField stockField = new JTextField(String.valueOf(stock));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Description:"));
        dialog.add(descField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("New Stock (Total):"));
        dialog.add(stockField);

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> {
            try {
                String n = nameField.getText();
                String d = descField.getText();
                double p = Double.parseDouble(priceField.getText());
                int s = Integer.parseInt(stockField.getText());

                sellerService.updateProduct(id, n, d, p, s);
                JOptionPane.showMessageDialog(dialog, "Product updated!");
                dialog.dispose();
                loadProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        dialog.add(new JLabel(""));
        dialog.add(updateBtn);
        dialog.setVisible(true);
    }

    private void removeSelectedProduct() {
        int row = productsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to remove.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this product?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                sellerService.removeProduct(id);
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error removing product: " + e.getMessage());
            }
        }
    }
}
