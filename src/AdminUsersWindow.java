import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class AdminUsersWindow extends JFrame {
    private AdminUserService service = new AdminUserService();
    private DefaultTableModel model;
    private JTable table;

    public AdminUsersWindow() {
        setTitle("Manage Users");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] cols = {"ID", "Username", "Email", "Role", "First Name", "Last Name"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        JPanel panel = new JPanel();
        JButton btnAdd = new JButton("Add User");
        JButton btnEdit = new JButton("Edit User");
        JButton btnDel = new JButton("Delete User");

        btnAdd.addActionListener(e -> addUser());
        btnEdit.addActionListener(e -> editUser());
        btnDel.addActionListener(e -> deleteUser());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<Object[]> users = service.getAllUsers();
        for (Object[] row : users) model.addRow(row);
    }

    private void addUser() {
        JTextField userF = new JTextField();
        JTextField emailF = new JTextField();
        JPasswordField passF = new JPasswordField();
        String[] roles = {"Customer", "Seller", "Administrator"};
        JComboBox<String> roleB = new JComboBox<>(roles);
        JTextField firstF = new JTextField();
        JTextField lastF = new JTextField();

        Object[] msg = {
                "Username:", userF, "Email:", emailF, "Password:", passF,
                "Role:", roleB, "First Name:", firstF, "Last Name:", lastF
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Add User", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                service.addUser(userF.getText(), emailF.getText(), new String(passF.getPassword()),
                        (String) roleB.getSelectedItem(), firstF.getText(), lastF.getText());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void editUser() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);
        JTextField emailF = new JTextField(model.getValueAt(row, 2).toString());
        String[] roles = {"Customer", "Seller", "Administrator"};
        JComboBox<String> roleB = new JComboBox<>(roles);
        roleB.setSelectedItem(model.getValueAt(row, 3).toString());
        JTextField firstF = new JTextField(model.getValueAt(row, 4).toString());
        JTextField lastF = new JTextField(model.getValueAt(row, 5).toString());

        Object[] msg = {"Email:", emailF, "Role:", roleB, "First Name:", firstF, "Last Name:", lastF};

        if (JOptionPane.showConfirmDialog(this, msg, "Edit User", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                service.updateUser(id, emailF.getText(), (String) roleB.getSelectedItem(), firstF.getText(), lastF.getText());
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row != -1 && JOptionPane.showConfirmDialog(this, "Delete this user?") == JOptionPane.YES_OPTION) {
            try {
                service.deleteUser((int) model.getValueAt(row, 0));
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

}
