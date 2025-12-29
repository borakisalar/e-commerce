import javax.swing.*;
import java.awt.*;

public class ViewStatsWindow extends JFrame {

    private final int currentUserId;
    private final StatsService statsService;

    public ViewStatsWindow(int userId) {
        this.currentUserId = userId;
        this.statsService = new StatsService();

        setTitle("My Statistics");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setMargin(new Insets(15, 15, 15, 15));

        String report = statsService.getCustomerStatistics(currentUserId);
        statsArea.setText(report);

        add(new JScrollPane(statsArea), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> this.dispose());
        add(btnClose, BorderLayout.SOUTH);
    }
}