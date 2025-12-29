import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public LoginWindow() {
        setTitle("E-Commerce Project - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));

        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Event Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationWindow().setVisible(true);
                dispose();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- REAL DATABASE LOGIC ---
        User user = DatabaseManager.authenticate(username, password);

        if (user != null) {
            String role = user.getRole();
            int userId = user.getId();
            String fullName = user.getFullName(); // Use First + Last Name

            if (role.equalsIgnoreCase("Customer")) {
                new CustomerDashboard(userId, fullName).setVisible(true);
            } else if (role.equalsIgnoreCase("Seller")) {
                new SellerDashboard(userId, fullName).setVisible(true);
            } else if (role.equalsIgnoreCase("Administrator")) {
                new AdminDashboard(userId, fullName).setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Set System Look and Feel for better aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}