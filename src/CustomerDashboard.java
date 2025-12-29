import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {
    private int loggedInUserId;

    public CustomerDashboard(int userId, String username) {
        this.loggedInUserId = userId;

        setTitle("E-Commerce System - Customer Dashboard");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Menu Buttons
        mainPanel.add(createMenuButton("View Catalogs"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("View Ongoing Order"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("View Order History"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("Customer Statistics"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(400, 40));
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });
        mainPanel.add(logoutBtn);

        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(450, 80)); // Large buttons as in image
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        // Action listeners can be added here later to open specific panels/windows
        return button;
    }
}
