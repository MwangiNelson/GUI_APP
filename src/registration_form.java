import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;



public class registration_form {
    private JTextField usernameTextfield;
    private JTextField userEmailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPassword;
    private JButton registerButton;
    public JPanel mainPanel;
    private JButton loginButton;

    public registration_form() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextfield.getText();
                String password = String.valueOf(passwordField.getPassword());
                String email = userEmailField.getText();
                String confirm_password = String.valueOf(confirmPassword.getPassword());
                // Check if fields are empty
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }
                if( !password.equals(confirm_password)){
                    JOptionPane.showMessageDialog(null,"Passwords do not match!");
                }
                // Register user
                if (registerUser(username,email, password)) {
                    JOptionPane.showMessageDialog(null, "Registration successful! You can now log in.");

                    // Optionally, open the login form
                    var frame = new login_form();
                    frame.setContentPane(frame.mainPanel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.");
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var frame = new login_form();
                frame.setContentPane(frame.mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private boolean registerUser(String username, String email, String password) {
        // SQL query to insert a new user
        String query = "INSERT INTO tbl_users (user_name, user_email, user_password,user_role) VALUES (?,?,?,'1')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            // Execute update
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0; // Registration successful

        } catch (SQLIntegrityConstraintViolationException ex) {
            // This exception is thrown if the username already exists (due to UNIQUE constraint)
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            return false;
        }
    }

}
