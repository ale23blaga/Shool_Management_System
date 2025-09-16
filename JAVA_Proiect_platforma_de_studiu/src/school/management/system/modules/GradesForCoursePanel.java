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


public class GradesForCoursePanel extends JPanel {
    private JTextField courseIDField;
    private JButton searchButton, downloadButton;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private CatalogDAO catalogDAO;
    private User currentUser;

    public GradesForCoursePanel(User user) {
        this.currentUser = user;
        catalogDAO = new CatalogDAO();
        setLayout(new BorderLayout());

        // Top: input for course ID
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("CursID:"));
        courseIDField = new JTextField(10);
        topPanel.add(courseIDField);
        searchButton = new JButton("Vezi Note");
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"intrareID", "StudentID", "Nota", "ProcentajID", "Prezență", "DataA"}, 0);
        gradeTable = new JTable(tableModel);
        add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        // Bottom: Download button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        downloadButton = new JButton("Descarcă Note");
        bottomPanel.add(downloadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> handleSearch());
        downloadButton.addActionListener(e -> handleDownload());
    }

    private void handleSearch() {
        tableModel.setRowCount(0);
        int cursID = Integer.parseInt(courseIDField.getText().trim());
        List<CatalogEntry> grades = catalogDAO.getCatalogEntriesByCourse(cursID);
        for (CatalogEntry ce : grades) {
            tableModel.addRow(new Object[]{
                    ce.getIntrareID(),
                    ce.getStudentID(),
                    ce.getNota(),
                    ce.getProcentajID(),
                    ce.isPrezenta() ? "Prezent" : "Absent",
                    ce.getDataA()
            });
        }
    }

    private void handleDownload() {
        // Save file as CSV
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            FileExportUtils.exportTableToCSV(gradeTable, file.getAbsolutePath() + ".csv");
            JOptionPane.showMessageDialog(this, "Fișier exportat cu succes.");
        }
    }
}
