package electricity.billing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Viewinformation extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private int userId; // Store the logged-in user's ID

    public Viewinformation(int userId) {
        this.userId = userId; // Pass the userId when initializing the class

        setTitle("Building Information");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("Building and Appliance Information", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Set up table
        tableModel = new DefaultTableModel(new Object[]{"Building", "Appliance Name", "Units", "kWh Consumed"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load data into the table
        loadDataFromDatabase();

        setVisible(true);
    }

    private void loadDataFromDatabase() {
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT b.building_name, a.appliance_name, a.quantity AS units, a.power_rating_kWh AS kwh_consumed " +
                             "FROM building b " +
                             "JOIN appliance a ON b.id = a.building_id " +
                             "WHERE b.user_id = ? " + // Filter by user_id
                             "ORDER BY b.building_name"
             )) {
            pstmt.setInt(1, userId); // Set the user_id parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                // Clear existing rows
                tableModel.setRowCount(0);

                // Populate table with result set data
                while (rs.next()) {
                    String buildingName = rs.getString("building_name");
                    String applianceName = rs.getString("appliance_name");
                    int units = rs.getInt("units");
                    double kwhConsumed = rs.getDouble("kwh_consumed");

                    tableModel.addRow(new Object[]{buildingName, applianceName, units, kwhConsumed});
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
