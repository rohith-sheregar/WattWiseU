package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignUp extends JFrame implements ActionListener {
    JTextField emailTextField, nameTextField;
    JPasswordField passwordTextField;
    JButton createButton, backButton;

    /**
     * Description: This page allows new administrators to create an account by
     * entering essential information, such as a university name, password, and email address.
     * It includes validations to ensure the email is unique. Once registered, the
     * administrator’s credentials are securely saved in the database. If successful,
     * the administrator receives a confirmation message and is redirected to the
     * login page.
     */
    public SignUp() {
        super("Signup Page");

        getContentPane().setBackground(new Color(168, 203, 255));

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setBounds(30, 50, 125, 20);
        add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setBounds(170, 50, 225, 20);
        add(emailTextField);

        JLabel nameLabel = new JLabel("University Name");
        nameLabel.setBounds(30, 100, 125, 20);
        add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(170, 100, 225, 20);
        add(nameTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30, 140, 125, 20);
        add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(170, 140, 225, 20);
        add(passwordTextField);

        createButton = new JButton("Create");
        createButton.setBackground(new Color(66, 127, 219));
        createButton.setForeground(Color.BLACK);
        createButton.setBounds(50, 180, 100, 25);
        createButton.addActionListener(this);
        add(createButton);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(66, 127, 219));
        backButton.setForeground(Color.BLACK);
        backButton.setBounds(180, 180, 100, 25);
        backButton.addActionListener(this);
        add(backButton);

        ImageIcon boyIcon = new ImageIcon(ClassLoader.getSystemResource("icon/boy.png"));
        Image boyImg = boyIcon.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon boyIcon2 = new ImageIcon(boyImg);
        JLabel boyLabel = new JLabel(boyIcon2);
        boyLabel.setBounds(320, 30, 250, 250);
        add(boyLabel);

        setSize(600, 300);
        setLocation(500, 200);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            String email = emailTextField.getText();
            String universityName = nameTextField.getText();
            String password = new String(passwordTextField.getPassword());

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(null, "Invalid email format");
                return;
            }

            try {
                // Load database driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver loaded successfully");

                // Connect to database
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/WattWiseU", "root", "Rohith@123")) {
                    System.out.println("Connected to the database successfully");

                    // Check if the email is already registered
                    String emailCheckQuery = "SELECT COUNT(*) FROM signup WHERE email = ?";
                    try (PreparedStatement emailCheckStmt = conn.prepareStatement(emailCheckQuery)) {
                        emailCheckStmt.setString(1, email);
                        try (ResultSet rs = emailCheckStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                JOptionPane.showMessageDialog(null, "Email already exists");
                                return;
                            }
                        }
                    }

                    // Insert new admin details into the database
                    String insertQuery = "INSERT INTO signup (email, username, name, password) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, universityName);
                        pstmt.setString(3, universityName); // Name can be university name or custom
                        pstmt.setString(4, password);
                        pstmt.executeUpdate();
                    }

                    // Show confirmation message and redirect to login
                    JOptionPane.showMessageDialog(null, "Account Created Successfully!");
                    setVisible(false);
                    new Login(); // Redirect to login page
                }
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Driver not found: " + cnfe.getMessage());
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error in database: " + sqle.getMessage());
            }
        } else if (e.getSource() == backButton) {
            setVisible(false);
            new Login(); // Go back to login screen
        }
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
