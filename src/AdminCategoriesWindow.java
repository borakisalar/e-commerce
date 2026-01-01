import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class AdminCategoriesWindow extends JFrame {
    private DefaultTableModel tableModel;
    private JTable categoriesTable;
    private JButton addButton;

    public AdminCategoriesWindow() {
        setTitle("Categories");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setContentPane(contentPanel);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Desc"}, 0);
        categoriesTable = new JTable(tableModel);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM CATEGORIES");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) tableModel.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
        } catch (Exception e) {}

        addButton = new JButton("Add");
        addButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> AddCategory());

        contentPanel.add(new JScrollPane(categoriesTable));
        contentPanel.add(addButton);
    }

    private void AddCategory() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        if (JOptionPane.showConfirmDialog(this, new Object[]{"Name", nameField, "Desc", descField}, "Add", JOptionPane.OK_CANCEL_OPTION) == 0) {
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO CATEGORIES (CategoryName, Description) VALUES (?, ?)")) {
                stmt.setString(1, nameField.getText());
                stmt.setString(2, descField.getText());
                stmt.executeUpdate();
                dispose();
                new AdminCategoriesWindow().setVisible(true);
            } catch (Exception ex) {}
        }
    }
}
