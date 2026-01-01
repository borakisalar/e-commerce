import javax.swing.*;
import java.awt.*;

public class SellerDashboard extends JFrame {
    private int sellerId;

    public SellerDashboard(int sellerId, String username) {
        this.sellerId = sellerId;

        setTitle("E-Commerce System - Seller Dashboard");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Seller Panel - " + username);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton manageBtn = createMenuButton("Manage Catalog & Products");
        manageBtn.addActionListener(e -> new ManageProductsWindow(sellerId).setVisible(true));
        mainPanel.add(manageBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton ordersBtn = createMenuButton("View Orders");
        ordersBtn.addActionListener(e -> new ManageOrdersWindow(sellerId).setVisible(true));
        mainPanel.add(ordersBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton statsBtn = createMenuButton("Seller Statistics");
        statsBtn.addActionListener(e -> new SellerStatsWindow(sellerId).setVisible(true));
        mainPanel.add(statsBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

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
