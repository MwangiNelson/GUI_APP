import javax.swing.*;
import java.sql.*;

public class class_view extends JFrame {
    private JLabel classTitle;
    private JLabel lecturerName;
    private JLabel classPoints;
    private JList list1;
    private JButton addNewTopicButton;
    public JPanel classViewPanel;
    private String classID;

    public class_view(String classID) {
        this.classID = classID;
        initializeUI();
        queryDatabaseAndUpdateUI();
    }

    private void initializeUI() {
        // Set up JFrame and JPanel
        setTitle("Class View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        classViewPanel = new JPanel();
        setContentPane(classViewPanel);
        classViewPanel.setLayout(new BoxLayout(classViewPanel, BoxLayout.Y_AXIS));

        // Initialize components
        classTitle = new JLabel("Class Title");
        lecturerName = new JLabel("Lecturer Name");
        classPoints = new JLabel("Class Points");
        list1 = new JList();
        addNewTopicButton = new JButton("Add New Topic");

        // Add components to the panel
        classViewPanel.add(classTitle);
        classViewPanel.add(lecturerName);
        classViewPanel.add(classPoints);
        classViewPanel.add(list1);
        classViewPanel.add(addNewTopicButton);
    }

    private void queryDatabaseAndUpdateUI() {
        // Replace the below code with actual database query logic
        DatabaseConnection dbConnection = new DatabaseConnection();
        try {
            String query = "SELECT class_name, class_lecturer, class_points FROM tbl_classes WHERE class_id = ?";
            PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, classID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String classTitleText = resultSet.getString("class_name");
                String lecturerNameText = resultSet.getString("class_lecturer");
                String classPointsText = resultSet.getString("class_points");

                // Update UI components with retrieved data
                classTitle.setText(classTitleText);
                lecturerName.setText(lecturerNameText);
                classPoints.setText(classPointsText);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
