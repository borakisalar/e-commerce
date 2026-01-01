import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminStatsWindow extends JFrame {
    private AdminStatsService service = new AdminStatsService();

    public AdminStatsWindow() {
        setTitle("System Wide Statistics");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("=== SYSTEM STATISTICS ===\n\n");

        double totalSales = service.getTotalSales();
        sb.append(String.format("Total System Sales: %.2f TL\n", totalSales));
        sb.append("-----------------------------\n\n");

        sb.append("[Top Selling Categories]\n");
        List<Object[]> categories = service.getTopSellingCategories();
        for (Object[] row : categories) {
            sb.append(String.format("- %s: %d units\n", row[0], row[1]));
        }
        sb.append("\n");

        sb.append("[Most Popular Items]\n");
        List<Object[]> items = service.getMostPopularItems();
        for (Object[] row : items) {
            sb.append(String.format("- %s: %d units\n", row[0], row[1]));
        }
        sb.append("\n");

        sb.append("[Top Sellers]\n");
        List<Object[]> sellers = service.getTopSellers();
        for (Object[] row : sellers) {
            sb.append(String.format("- %s: %.2f TL\n", row[0], row[1]));
        }

        statsArea.setText(sb.toString());
        add(new JScrollPane(statsArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel bp = new JPanel();
        bp.add(closeButton);
        add(bp, BorderLayout.SOUTH);
    }
}
