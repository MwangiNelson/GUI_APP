import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class student_dashboard extends JFrame {
    private JPanel panel1;
    private JLabel dash_header;
    private JList classes_list;
    private JButton addClassesButton;
    public JPanel dashboardPanel;

    public student_dashboard() {
        populateClassesList();

        classes_list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = (String) classes_list.getSelectedValue();
                if (selectedValue != null) {
                    String[] parts = selectedValue.split(" - ");
                    int classID = Integer.parseInt(parts[0]);
                    String class_id = String.valueOf(classID);

                    JFrame classViewFrame = new JFrame("Class View");
                    classViewFrame.setContentPane(new class_view(class_id).classViewPanel);
                    classViewFrame.pack();
                    classViewFrame.setLocationRelativeTo(null); // Center on screen
                    classViewFrame.setVisible(true);

                    dispose();
                }
            }
        });

        addClassesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var newClassPage = new new_class();

                JFrame newFrame = new JFrame("New Class Form");
                newFrame.setContentPane(newClassPage.newClassPanel);
                newFrame.pack();
                newFrame.setLocationRelativeTo(null); // Center on screen
                newFrame.setVisible(true);

                dispose();
            }
        });

    }

    private void populateClassesList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT class_id, class_name FROM tbl_classes";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("class_id");
                String name = rs.getString("class_name");
                listModel.addElement(id + " - " + name);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        classes_list.setModel(listModel);
    }
}
