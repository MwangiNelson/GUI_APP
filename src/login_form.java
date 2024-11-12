import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login_form extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton signInButton;
    public JPanel mainPanel;
    private JButton registerButton;

    public login_form() {
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get username and password
                String username = usernameTextField.getText();
                String password = String.valueOf(passwordField.getPassword());

                // Check if username and password have been inserted
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                // Authenticate user
                if (authenticateUser(username, password)) {
                    // Retrieve user credentials from the database and store in session-like storage
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "SELECT * FROM tbl_users WHERE user_name = ?";
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setString(1, username);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            String userEmail = rs.getString("user_email");

                            // Create session-like storage (for simplicity using static variables here)
                            UserSession.setUsername(username);
                            UserSession.setUserEmail(userEmail);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Proceed to the next window or functionality
                    SwingUtilities.invokeLater(() -> {
                        JFrame dashboardFrame = new JFrame("User Dashboard");
                        dashboardFrame.setContentPane(new student_dashboard().dashboardPanel);
                        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dashboardFrame.pack();
                        dashboardFrame.setVisible(true);

                        dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registration_form frame = new registration_form();
                JFrame registrationFrame = new JFrame("Registration Form");
                registrationFrame.setContentPane(frame.mainPanel);
                registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                registrationFrame.pack();
                registrationFrame.setVisible(true);
            }
        });
    }

    private boolean authenticateUser(String username, String password) {
        // SQL query to check if the username and password exist
        String query = "SELECT * FROM tbl_users WHERE user_name = ? AND user_password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute query
            ResultSet rs = pstmt.executeQuery();

            // Check if a record was found
            if (rs.next()) {
                return true; // Authentication successful
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return false; // Authentication failed
    }
}

class UserSession {
    private static String username;
    private static String userEmail;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        UserSession.userEmail = userEmail;
    }
}
