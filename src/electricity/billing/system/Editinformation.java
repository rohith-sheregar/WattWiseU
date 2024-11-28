package electricity.billing.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Editinformation extends JFrame implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtBuilding, txtAppliance, txtUnits, txtKwh;
    private JButton addButton, modifyButton, deleteButton;
    private int userId; // Store the logged-in user's ID

    public Editinformation(int userId) {
        this.userId = userId; // Pass the userId when initializing the class

        setTitle("Edit Building and Appliance Information");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("Edit Building and Appliance Information", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Set up table
        tableModel = new DefaultTableModel(new Object[]{"Building", "Appliance Type", "Units", "kWh Consumed"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from database
        loadDataFromDatabase();

        // Input fields and buttons for adding/modifying/deleting records
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));

        txtBuilding = new JTextField();
        txtAppliance = new JTextField();
        txtUnits = new JTextField();
        txtKwh = new JTextField();

        inputPanel.add(new JLabel("Building:"));
        inputPanel.add(txtBuilding);
        inputPanel.add(new JLabel("Appliance Type:"));
        inputPanel.add(txtAppliance);
        inputPanel.add(new JLabel("Units:"));
        inputPanel.add(txtUnits);
        inputPanel.add(new JLabel("kWh Consumed:"));
        inputPanel.add(txtKwh);

        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);

        inputPanel.add(addButton);
        inputPanel.add(modifyButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadDataFromDatabase() {
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT b.building_name, a.appliance_name AS appliance_type, a.quantity AS units, a.power_rating_kWh AS kwh_consumed " +
                             "FROM building b " +
                             "JOIN appliance a ON b.id = a.building_id " +
                             "WHERE b.user_id = ? " +
                             "ORDER BY b.building_name"
             )) {
            pstmt.setInt(1, userId); // Set the user_id parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                tableModel.setRowCount(0);  // Clear existing rows

                while (rs.next()) {
                    String buildingName = rs.getString("building_name");
                    String applianceType = rs.getString("appliance_type");
                    int units = rs.getInt("units");
                    double kwhConsumed = rs.getDouble("kwh_consumed");

                    tableModel.addRow(new Object[]{buildingName, applianceType, units, kwhConsumed});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addRecord();
        } else if (e.getSource() == modifyButton) {
            modifyRecord();
        } else if (e.getSource() == deleteButton) {
            deleteRecord();
        }
    }

    private void addRecord() {
        String buildingName = txtBuilding.getText().trim();
        String applianceType = txtAppliance.getText().trim();
        int units;
        double kwhConsumed;

        // Validate inputs
        if (buildingName.isEmpty() || applianceType.isEmpty() || txtUnits.getText().isEmpty() || txtKwh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            units = Integer.parseInt(txtUnits.getText().trim());
            kwhConsumed = Double.parseDouble(txtKwh.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for units and kWh consumed.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO appliance (building_id, appliance_name, quantity, power_rating_kWh) VALUES " +
                             "((SELECT id FROM building WHERE LOWER(building_name) = LOWER(?) AND user_id = ?), ?, ?, ?)"
             )) {
            pstmt.setString(1, buildingName);
            pstmt.setInt(2, userId); // Ensure the building belongs to the logged-in user
            pstmt.setString(3, applianceType);
            pstmt.setInt(4, units);
            pstmt.setDouble(5, kwhConsumed);
            pstmt.executeUpdate();

            tableModel.addRow(new Object[]{buildingName, applianceType, units, kwhConsumed});
            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding record to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to modify.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String buildingName = (String) tableModel.getValueAt(selectedRow, 0);
        String applianceType = txtAppliance.getText().trim();
        int units;
        double kwhConsumed;

        try {
            units = Integer.parseInt(txtUnits.getText().trim());
            kwhConsumed = Double.parseDouble(txtKwh.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for units and kWh consumed.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE appliance SET quantity = ?, power_rating_kWh = ? " +
                             "WHERE appliance_name = ? AND building_id = (SELECT id FROM building WHERE LOWER(building_name) = LOWER(?) AND user_id = ?)"
             )) {
            pstmt.setInt(1, units);
            pstmt.setDouble(2, kwhConsumed);
            pstmt.setString(3, applianceType);
            pstmt.setString(4, buildingName);
            pstmt.setInt(5, userId); // Ensure modification is done for the logged-in user
            pstmt.executeUpdate();

            tableModel.setValueAt(applianceType, selectedRow, 1);
            tableModel.setValueAt(units, selectedRow, 2);
            tableModel.setValueAt(kwhConsumed, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Record modified successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error modifying record in database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String buildingName = (String) tableModel.getValueAt(selectedRow, 0);
        String applianceType = (String) tableModel.getValueAt(selectedRow, 1);

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM appliance WHERE appliance_name = ? AND building_id = (SELECT id FROM building WHERE LOWER(building_name) = LOWER(?) AND user_id = ?)"
             )) {
            pstmt.setString(1, applianceType);
            pstmt.setString(2, buildingName);
            pstmt.setInt(3, userId); // Ensure deletion is done for the logged-in user
            pstmt.executeUpdate();

            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Record deleted successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
