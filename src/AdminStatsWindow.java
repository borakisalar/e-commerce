import javax.swing.*;
import java.sql.*;

public class AdminStatsWindow extends JFrame{
    private JTextArea statsArea;

    public AdminStatsWindow() {
        setTitle("System Stats");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        setContentPane(contentPanel);

        StringBuilder statsBuilder = new StringBuilder();
        try (Connection conn = DatabaseManager.getConnection()) {
            ResultSet rs1 = conn.prepareStatement("SELECT SUM(TotalAmount) FROM ORDERS").executeQuery();
            if (rs1.next()) statsBuilder.append("Total Sales: $").append(rs1.getDouble(1)).append("\n");

            ResultSet rs2 = conn.prepareStatement("SELECT u.Username, SUM(o.TotalAmount) as T FROM ORDERS o JOIN USERS u ON o.SellerID=u.UserID GROUP BY u.UserID ORDER BY T DESC LIMIT 1").executeQuery();
            if (rs2.next()) statsBuilder.append("Top Seller: ").append(rs2.getString(1)).append("\n");
        } catch (Exception e) {}

        statsArea = new JTextArea(statsBuilder.toString());
        statsArea.setEditable(false);
        contentPanel.add(new JScrollPane(statsArea));
    }
}
