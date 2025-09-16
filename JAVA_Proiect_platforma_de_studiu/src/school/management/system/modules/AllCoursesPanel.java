package school.management.system.modules;

import school.management.system.dao.CatalogDAO;
import school.management.system.dao.CourseDAO;
import school.management.system.dao.EnrollmentDAO;
import school.management.system.models.CatalogEntry;
import school.management.system.models.Course;
import school.management.system.models.Enrollment;
import school.management.system.models.User;
import school.management.system.services.CourseService;
import school.management.system.services.EnrollmentService;
import school.management.system.utils.FileExportUtils;
import school.management.system.utils.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AllCoursesPanel extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO;

    public AllCoursesPanel() {
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Toate Cursurile din Universitate", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"CursID", "Nume", "Descriere", "Max Studenți"}, 0);
        courseTable = new JTable(tableModel);
        add(new JScrollPane(courseTable), BorderLayout.CENTER);

        loadAllCourses();
    }

    private void loadAllCourses() {
        tableModel.setRowCount(0);
        List<Course> list = courseDAO.getAllCourses();
        for (Course c : list) {
            tableModel.addRow(new Object[]{c.getCursID(), c.getNumeCurs(), c.getDescriere(), c.getNrMaximStudenti()});
        }
    }
}
