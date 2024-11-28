package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField usernameTextField;
    JPasswordField passwordTextField;
    JButton loginButton, cancelButton, signupButton, forgotPasswordButton;

    public Login() {
        super("Login");
        getContentPane().setBackground(Color.white);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Admin");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(250, 20, 200, 30);
        add(welcomeLabel);

        JLabel universityLabel = new JLabel("University Name");
        universityLabel.setBounds(300, 60, 120, 20);
        add(universityLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(420, 60, 150, 20);
        add(usernameTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(300, 100, 100, 20);
        add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(420, 100, 150, 20);
        add(passwordTextField);

        loginButton = new JButton("Login");
        loginButton.setBounds(330, 180, 100, 20);
        loginButton.addActionListener(this);
        add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(460, 180, 100, 20);
        cancelButton.addActionListener(this);
        add(cancelButton);

        signupButton = new JButton("Signup");
        signupButton.setBounds(400, 220, 100, 20);
        signupButton.addActionListener(this);
        add(signupButton);

        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setBounds(380, 260, 160, 20);
        forgotPasswordButton.addActionListener(this);
        add(forgotPasswordButton);

        ImageIcon profileOne = new ImageIcon(ClassLoader.getSystemResource("icon/profile.png"));
        Image profileTwo = profileOne.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon finalProfileOne = new ImageIcon(profileTwo);
        JLabel profileLabel = new JLabel(finalProfileOne);
        profileLabel.setBounds(5, 5, 250, 250);
        add(profileLabel);

        setSize(640, 350);
        setLocation(400, 200);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameTextField.getText();
            String password = new String(passwordTextField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username or Password cannot be empty");
                return;
            }

            Connection conn = null;

            try {
                // Establish database connection
                database db = new database();
                conn = db.getConnection();

                // Check for valid login
                String query = "SELECT id, password FROM Signup WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, username);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    int userId = resultSet.getInt("id");

                    // Check password match
                    if (storedPassword.equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        setVisible(false);
                        new MainClass(userId); // Pass the user ID to the main class
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Password");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username");
                }
                resultSet.close();
                pstmt.close();
            } catch (Exception exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        } else if (e.getSource() == signupButton) {
            setVisible(false);
            new SignUp();  // Navigate to signup screen
        } else if (e.getSource() == forgotPasswordButton) {
            String email = JOptionPane.showInputDialog(this, "Enter your registered email:");

            if (email != null && !email.isEmpty()) {
                Connection conn = null;
                try {
                    database db = new database();
                    conn = db.getConnection();
                    String query = "SELECT * FROM Signup WHERE email = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, email);
                    ResultSet resultSet = pstmt.executeQuery();

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "An email with password reset instructions has been sent.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Email not registered.");
                    }
                    resultSet.close();
                    pstmt.close();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email cannot be empty.");
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
