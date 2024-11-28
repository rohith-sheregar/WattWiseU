package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ViewConsumption extends JFrame {

    private final int userId;

    // Constructor takes userId as input
    public ViewConsumption(int userId) {
        this.userId = userId;

        setTitle("Electricity Consumption - WattWiseU");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fetch the consumption data
        HashMap<String, Double> buildingData = getBuildingConsumption();
        if (buildingData == null || buildingData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data available to display.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Add the bar graph panel
        add(new BarGraphPanel(buildingData));
        setVisible(true);
    }

    // Method to fetch consumption data for each building
    private HashMap<String, Double> getBuildingConsumption() {
        HashMap<String, Double> data = new HashMap<>();

        // SQL query to calculate total kWh consumed for each building based on working hours and days
        String query = "SELECT b.building_name, SUM(a.power_rating_kWh * a.quantity * 6 * 30) AS total_kwh " +
                "FROM building b " +
                "JOIN appliance a ON b.id = a.building_id " +
                "WHERE b.user_id = ? " +
                "GROUP BY b.building_name";

        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId); // Set userId to filter
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String buildingName = resultSet.getString("building_name");
                double totalKwh = resultSet.getDouble("total_kwh");
                data.put(buildingName, totalKwh); // Add building data to HashMap
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return data;
    }

    // Custom panel for drawing the bar graph
    class BarGraphPanel extends JPanel {
        private final HashMap<String, Double> data;

        public BarGraphPanel(HashMap<String, Double> data) {
            this.data = data;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int barWidth = (width - 2 * padding) / data.size();
            double maxValue = data.values().stream().max(Double::compareTo).orElse(1.0); // Avoid division by zero

            // Draw axes
            g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
            g2d.drawLine(padding, padding, padding, height - padding); // Y-axis

            // Draw bars and labels
            int x = padding + 10;
            int colorIndex = 0;

            // Colors for the bars
            Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN};

            for (String building : data.keySet()) {
                double value = data.get(building);
                int barHeight = (int) ((value / maxValue) * (height - 2 * padding));

                // Set bar color
                g2d.setColor(colors[colorIndex % colors.length]);
                colorIndex++;

                // Draw bar
                g2d.fillRect(x, height - padding - barHeight, barWidth - 10, barHeight);

                // Draw building name and value
                g2d.setColor(Color.BLACK);
                g2d.drawString(building, x + 5, height - padding + 15); // Building name below bar
                g2d.drawString(String.format("%.2f kWh", value), x + 5, height - padding - barHeight - 5); // Value above bar

                x += barWidth; // Move to the next bar position
            }
        }
    }

    // Main method for testing (replace 123 with actual userId)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewConsumption(123).setVisible(true));
    }
}
