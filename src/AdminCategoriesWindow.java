import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

public class AdminCategoriesWindow extends JFrame {
    private AdminCategoryService service = new AdminCategoryService();
    private DefaultTableModel tableModel;
    private JTable categoriesTable;
    private JButton addButton, editButton, deleteButton;

    public AdminCategoriesWindow() {
        setTitle("Manage Categories");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] { "ID", "Name", "Description" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoriesTable = new JTable(tableModel);
        add(new JScrollPane(categoriesTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Category");
        editButton = new JButton("Modify Category");
        deleteButton = new JButton("Delete Category");

        addButton.addActionListener(e -> addCategory());
        editButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Object[]> categories = service.getAllCategories();
        for (Object[] row : categories) {
            tableModel.addRow(row);
        }
    }

    private void addCategory() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        Object[] message = {
                "Category Name:", nameField,
                "Description:", descField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Category", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                service.addCategory(nameField.getText(), descField.getText());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage());
            }
        }
    }

    private void updateCategory() {
        int selectedRow = categoriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to modify.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentDesc = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField nameField = new JTextField(currentName);
        JTextField descField = new JTextField(currentDesc);
        Object[] message = {
                "Category Name:", nameField,
                "Description:", descField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Modify Category", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                service.updateCategory(id, nameField.getText(), descField.getText());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating category: " + ex.getMessage());
            }
        }
    }

    private void deleteCategory() {
        int selectedRow = categoriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteCategory(id);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + ex.getMessage());
            }
        }
    }
}
