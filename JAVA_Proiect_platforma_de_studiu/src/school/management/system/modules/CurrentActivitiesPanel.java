package school.management.system.modules;

import school.management.system.models.User;
import school.management.system.models.Activity;
import school.management.system.dao.ActivityDAO;
import school.management.system.utils.FileExportUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Displays the current (or upcoming) activities for the student.
 *
 * Example: labs, seminars, or courses the student is enrolled in for the current day/week.
 */
//Afiseaza activitatile curente, si cele care urmeaza: laboratoare, seminarii
public class CurrentActivitiesPanel extends JPanel {

    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton, downloadButton;

    private ActivityDAO activityDAO;

    public CurrentActivitiesPanel(User user) {
        this.currentUser = user;
        this.activityDAO = new ActivityDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Activitățile curente / viitoare", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
                "ID Activitate", "Tip", "Curs ID", "Profesor ID", "Data Start", "Data End", "Recurență"
        }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        refreshButton = new JButton("Reîncarcă activități");
        downloadButton = new JButton("Descarca");
        bottomPanel.add(refreshButton);
        bottomPanel.add(downloadButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action
        refreshButton.addActionListener(e -> loadActivities());
        downloadButton.addActionListener(e -> handleDownload());

        // Initial load
        loadActivities();
    }


    private void loadActivities() {
        tableModel.setRowCount(0);

        int studentID = currentUser.getUserID();


        List<Activity> activities = activityDAO.getActivitiesForStudent(studentID);

        for (Activity a : activities) {
            tableModel.addRow(new Object[]{
                    a.getActivitateID(),
                    a.getTipActivitate(),
                    a.getCursID(),
                    a.getProfesorID(),
                    a.getStartDate(),
                    a.getEndDate(),
                    a.getRecurenta()
            });
        }
    }

    //descarca activitati
    private void handleDownload() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            FileExportUtils.exportTableToCSV(table, file.getAbsolutePath() + ".csv");
            JOptionPane.showMessageDialog(this, "Activitățile au fost exportate cu succes!");
        }
    }
}
