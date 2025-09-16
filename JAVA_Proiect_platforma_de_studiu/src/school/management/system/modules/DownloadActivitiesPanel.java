package school.management.system.modules;



import school.management.system.dao.ActivityDAO;
import school.management.system.dao.CatalogDAO;
import school.management.system.dao.CourseDAO;
import school.management.system.dao.EnrollmentDAO;
import school.management.system.models.*;
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

public class DownloadActivitiesPanel extends JPanel {
    private JButton downloadButton;
    private JTable activityTable;
    private DefaultTableModel tableModel;
    private ActivityDAO activityDAO;
    private User currentUser;

    public DownloadActivitiesPanel(User user) {
        this.currentUser = user;
        activityDAO = new ActivityDAO();
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Activitățile Mele", SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ActivitateID", "Tip", "CursID", "Procentaj", "StartDate", "EndDate", "Recurenta", "Max Part"}, 0);
        activityTable = new JTable(tableModel);
        add(new JScrollPane(activityTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        downloadButton = new JButton("Descarcă Activități");
        bottomPanel.add(downloadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load data
        loadMyActivities();

        downloadButton.addActionListener(e -> downloadActivities());
    }

    private void loadMyActivities() {
        tableModel.setRowCount(0);
        int profesorID = currentUser.getUserID(); // or separate lookup
        List<Activity> list = activityDAO.getActivitiesByProfessor(profesorID);
        for (Activity a : list) {
            tableModel.addRow(new Object[]{
                    a.getActivitateID(), a.getTipActivitate(), a.getCursID(),
                    a.getProcentaj(), a.getStartDate(), a.getEndDate(),
                    a.getRecurenta(), a.getMaxParticipanti()
            });
        }
    }

    private void downloadActivities() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            FileExportUtils.exportTableToCSV(activityTable, file.getAbsolutePath() + ".csv");
            JOptionPane.showMessageDialog(this, "Fișier exportat cu succes!");
        }
    }
}
