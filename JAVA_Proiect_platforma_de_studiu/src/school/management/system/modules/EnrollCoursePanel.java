package school.management.system.modules;

import school.management.system.dao.CourseDAO;
import school.management.system.dao.EnrollmentDAO;
import school.management.system.models.Course;
import school.management.system.models.Enrollment;
import school.management.system.models.User;
import school.management.system.services.CourseService;
import school.management.system.services.EnrollmentService;
import school.management.system.utils.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

//Panel care ii ofera studentului optiunea de a vedea cursuri, de a se inscrie si de a parasi cursuri
public class EnrollCoursePanel extends JPanel {
    private User currentUser;                        // user-ul logat (student)
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    private JTextField searchField;
    private JTable courseTable;
    private JButton enrollButton, unrollButton, searchButton;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    public EnrollCoursePanel(User currentUser) {
        this.currentUser = currentUser;
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        setLayout(new BorderLayout());

        // Top label
        JLabel titleLabel = new JLabel("Înscriere la curs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        //Search pentru cursuri
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchField = new JTextField(20);
        searchButton = new JButton("Caută Curs");
        topPanel.add(new JLabel("Nume/ID curs:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Table cu cursuri
        tableModel = new DefaultTableModel(new Object[]{"CursID", "Nume Curs", "Descriere", "Nr. Maxim Studenți"}, 0);
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load data into the table
        loadCourses();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        enrollButton = new JButton("Înscrie-te la curs");
        unrollButton = new JButton("Renunță la curs");

        bottomPanel.add(enrollButton);
        bottomPanel.add(unrollButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollInSelectedCourse();
            }
        });

        unrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unenrollFromSelectedCourse();
            }
        });

        searchButton.addActionListener(e -> handleSearch());
    }

    // Load all courses available for enrollment
    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courseList = courseService.getAllCourses();

        for (Course c : courseList) {
            tableModel.addRow(new Object[]{
                    c.getCursID(),
                    c.getNumeCurs(),
                    c.getDescriere(),
                    c.getNrMaximStudenti()
            });
        }
    }

    // Enroll the student in the selected course
    private void enrollInSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selectați mai întâi un curs din listă.");
            return;
        }

        int cursID = (int) tableModel.getValueAt(selectedRow, 0);
        int randomEnrollmentID = (int) (Math.random() * 100000);
        int studentID = currentUser.getUserID();

        Date dataInscriere = Date.valueOf(LocalDate.now());

        // Validate input
        if (!InputValidator.isValidEnrollmentInput(randomEnrollmentID, studentID, cursID, dataInscriere)) {
            JOptionPane.showMessageDialog(this, "Date invalide pentru înscriere.");
            return;
        }

        // Create enrollment entry
        Enrollment enrollment = new Enrollment(randomEnrollmentID, studentID, cursID, dataInscriere);

        // Save the enrollment in the database
        try {
            enrollmentService.enrollStudent(enrollment);
            JOptionPane.showMessageDialog(this, "V-ați înscris cu succes la cursul cu ID: " + cursID);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la înscriere: " + ex.getMessage());
        }
    }

    // Unenroll the student from the selected course
    private void unenrollFromSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selectați mai întâi un curs din listă.");
            return;
        }

        int cursID = (int) tableModel.getValueAt(selectedRow, 0);
        int studentID = currentUser.getUserID();


        try {
            enrollmentDAO.removeEnrollment(studentID, cursID);  // Call the DAO to handle the database operation
            JOptionPane.showMessageDialog(this, "Ați renunțat cu succes la cursul cu ID: " + cursID);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la renunțarea de la curs: " + ex.getMessage());
        }
    }


    private void handleSearch() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        List<Course> results = courseDAO.searchCourses(keyword);
        for (Course c : results) {
            tableModel.addRow(new Object[]{c.getCursID(), c.getNumeCurs(), c.getDescriere(), c.getNrMaximStudenti()});
        }
    }
}
