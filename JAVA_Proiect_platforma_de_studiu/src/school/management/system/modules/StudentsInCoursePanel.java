package school.management.system.modules;

import javax.swing.*;
import school.management.system.dao.CourseDAO;
import school.management.system.models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import school.management.system.models.*;
import school.management.system.utils.*;
import school.management.system.dao.*;

public class StudentsInCoursePanel extends JPanel {
    private User currentUser; // teacher
    private JTextField courseIDField;
    private JButton searchButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private EnrollmentDAO enrollmentDAO;

    public StudentsInCoursePanel(User user) {
        this.currentUser = user;
        enrollmentDAO = new EnrollmentDAO();

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("CursID:"));
        courseIDField = new JTextField(10);
        topPanel.add(courseIDField);
        searchButton = new JButton("Caută Studenți");
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"StudentID", "Nume", "Prenume", "Data Înscriere"}, 0);
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        searchButton.addActionListener(e -> handleSearch());
    }

    private void handleSearch() {
        tableModel.setRowCount(0);
        int cursID = Integer.parseInt(courseIDField.getText().trim());
        List<StudentEnrollmentInfo> list = enrollmentDAO.getStudentsInCourse(cursID);
        for (StudentEnrollmentInfo s : list) {
            tableModel.addRow(new Object[]{s.getStudentID(), s.getNume(), s.getPrenume(), s.getDataInscriere()});
        }
    }
}