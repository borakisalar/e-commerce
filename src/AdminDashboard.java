import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard(int adminId, String username) {
        setTitle("E-Commerce System - Admin Dashboard");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Admin Panel - " + username);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mainPanel.add(createMenuButton("Manage Users"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("Manage Categories"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("View Shipments"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(createMenuButton("System Statistics"));
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
