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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnCatalogs = createMenuButton("View Catalogs");
        btnCatalogs.addActionListener(e -> {
            new ViewCatalogWindow(loggedInUserId).setVisible(true);
        });
        mainPanel.add(btnCatalogs);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnCart = createMenuButton("View Ongoing Order");
        btnCart.addActionListener(e -> new ViewCartWindow(loggedInUserId).setVisible(true));
        mainPanel.add(btnCart);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnHistory = createMenuButton("View Order History");
        btnHistory.addActionListener(e -> new ViewOrderHistoryWindow(loggedInUserId).setVisible(true));
        mainPanel.add(btnHistory);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnStats = createMenuButton("Customer Statistics");
        btnStats.addActionListener(e -> new ViewStatsWindow(loggedInUserId).setVisible(true));
        mainPanel.add(btnStats);
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
        button.setMaximumSize(new Dimension(450, 80));
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);

        return button;
    }
}
