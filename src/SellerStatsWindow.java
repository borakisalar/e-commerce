import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SellerStatsWindow extends JFrame {
    public SellerStatsWindow(int sellerId) {
        SellerService service = new SellerService();
        setTitle("Seller Statistics & Reviews");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setMargin(new Insets(15, 15, 15, 15));

        StringBuilder sb = new StringBuilder();
        sb.append(service.getSellerStatistics(sellerId));

        area.setText(sb.toString());
        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }
}
