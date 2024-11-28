package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerateBill extends JFrame {
    private int user_id; // Store the user_id

    public GenerateBill(int user_id) {
        this.user_id = user_id; // Save the user_id for use in the class

        // Set up JFrame properties
        setTitle("Generate Bill");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("Electricity Bill for User ID: " + user_id, JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Content panel to display bill details
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(5, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fetch and display data dynamically
        String buildingName = getBuildingName(user_id); // Fetch the user's building name(s)
        int totalUnits = getTotalUnitsConsumed(user_id); // Fetch total units consumed for 30 days of 6 hours/day
        double costPerUnit = getCostPerUnit(); // Fetch cost per unit (from DB or config)
        double totalAmount = totalUnits * costPerUnit; // Calculate the total bill amount

        // Add labels to display bill details
        contentPanel.add(new JLabel("Building Name(s):"));
        contentPanel.add(new JLabel(buildingName)); // Display fetched building name(s)
        contentPanel.add(new JLabel("Total Units Consumed:"));
        contentPanel.add(new JLabel(String.valueOf(totalUnits))); // Display fetched total units
        contentPanel.add(new JLabel("Cost per Unit:"));
        contentPanel.add(new JLabel("₹" + costPerUnit)); // Display cost per unit
        contentPanel.add(new JLabel("Total Amount:"));
        contentPanel.add(new JLabel("₹" + totalAmount)); // Display calculated total amount
        add(contentPanel, BorderLayout.CENTER);

        // Add a close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose()); // Close the window
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to fetch building name(s) associated with the user
    private String getBuildingName(int user_id) {
        StringBuilder buildingNames = new StringBuilder();
        String query = "SELECT building_name FROM building WHERE user_id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (buildingNames.length() > 0) {
                    buildingNames.append(", ");
                }
                buildingNames.append(resultSet.getString("building_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching building names: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return buildingNames.toString().isEmpty() ? "No Buildings Found" : buildingNames.toString();
    }

    // Method to fetch total units consumed by the user for 30 days and 6 hours/day
    private int getTotalUnitsConsumed(int user_id) {
        int totalUnits = 0;
        String query = "SELECT SUM(a.power_rating_kWh * a.quantity * 6 * 30) AS total_kwh " + // Multiply by 6 hours/day * 30 days
                "FROM appliance a " +
                "JOIN building b ON a.building_id = b.id " +
                "WHERE b.user_id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalUnits = resultSet.getInt("total_kwh");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching total units: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return totalUnits;
    }

    // Method to fetch cost per unit (fixed or from database/configuration)
    private double getCostPerUnit() {
        // You could also fetch this from the database if needed
        return 5.50; // Placeholder cost per unit (₹5.50)
    }
}
