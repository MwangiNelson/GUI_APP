import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class new_topic extends JFrame {
    private JTextField textField1;
    private JComboBox comboBox1;
    private JButton saveTopicButton;
    private JButton backToClassesButton;

    public new_topic() {
        populateClassesComboBox();

        backToClassesButton.addActionListener(new ActionListener() {
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
        saveTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topicName = textField1.getText();
                String classId = (String) comboBox1.getSelectedItem();

                if (topicName.isEmpty() || classId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "INSERT INTO tbl_topics (topic_title, topic_unit) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, topicName);
                    preparedStatement.setInt(2, Integer.parseInt(classId.split(" - ")[0]));
                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(null, "Topic added successfully");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding topic");
                }
            }
        });
    }

    private void populateClassesComboBox() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT class_id, class_name FROM tbl_classes";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("class_id");
                String name = rs.getString("class_name");
                comboBox1.addItem(id + " - " + name);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
