import javax.swing.*;
import java.awt.*;

public class RegistrationWindow extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton backButton;

    public RegistrationWindow() {
        setTitle("E-Commerce Project - Sign Up");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(firstNameField, gbc);


        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(lastNameField, gbc);


        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);


        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);


        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("I am a:"), gbc);
        String[] roles = { "Customer", "Seller" };
        roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35));
        backButton = new JButton("Cancel");
        backButton.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);


        registerButton.addActionListener(e -> handleRegistration());

        backButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            this.dispose();
        });
    }

    private void handleRegistration() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        boolean success = DatabaseManager.registerUser(username, email, password, role, firstName, lastName);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration Successful! You can now login.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            new LoginWindow().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username or email might already be taken.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
