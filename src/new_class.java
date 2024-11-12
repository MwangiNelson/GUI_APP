import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class new_class extends JFrame {
    private JTextField class_name;
    private JTextField pointsField;
    private JButton createClassButton;
    private JComboBox lecturersList;
    public JPanel newClassPanel;
    private JButton backToAllClassesButton;

    public new_class() {
        fetchLecturers();
        createClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = class_name.getText();
                String classLecturer = (String) lecturersList.getSelectedItem();
                String classPoints = pointsField.getText();

                if(className.isEmpty() || classLecturer.isEmpty() || classPoints.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }
                if(classLecturer.equals("Lecturer")){
                    JOptionPane.showMessageDialog(null,"Please select a lecturer from the list.");
                }
                if(Integer.parseInt(classPoints) < 0){
                    JOptionPane.showMessageDialog(null,"Please enter a valid number for points");
                }


                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "INSERT INTO tbl_classes (class_name, class_lecturer, class_points) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, className);
                    preparedStatement.setString(2, classLecturer);
                    preparedStatement.setString(3, classPoints);
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(null, "Class added successfully");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding class");
                }
            }
        });
        backToAllClassesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame dashboardFrame = new JFrame("User Dashboard");
                dashboardFrame.setContentPane(new student_dashboard().dashboardPanel);
                dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dashboardFrame.pack();
                dashboardFrame.setVisible(true);

                dispose();
            }
        });
    }

    private void fetchLecturers() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT user_name FROM tbl_users WHERE user_role = 2";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                lecturersList.addItem(rs.getString("user_name"));
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
