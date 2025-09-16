package school.management.system.modules;

import school.management.system.dao.ActivityDAO;
import school.management.system.models.Activity;
import school.management.system.models.User;
import school.management.system.utils.FileExportUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Date;
import java.util.List;

//Pagina in care profesori pot programa si modifica activitati
public class TeacherActivitiesPanel extends JPanel {

    private User currentUser;  // profesorul conectat
    private JTable activitiesTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton, downloadButton;

    private ActivityDAO activityDAO;

    public TeacherActivitiesPanel(User user) {
        this.currentUser = user;
        this.activityDAO = new ActivityDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gestionare Activități", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
                "ActivitateID", "Tip", "CursID", "Procentaj",
                "StartDate", "EndDate", "Recurență", "Max Participanți"
        }, 0);
        activitiesTable = new JTable(tableModel);
        activitiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(activitiesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Adaugă Activitate");
        editButton = new JButton("Editează Activitate");
        deleteButton = new JButton("Șterge Activitate");
        refreshButton = new JButton("Reîmprospătează");
        downloadButton = new JButton("Descarca Activitati");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(downloadButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(this::handleAdd);
        editButton.addActionListener(this::handleEdit);
        deleteButton.addActionListener(this::handleDelete);
        refreshButton.addActionListener(e -> loadActivities());
        downloadButton.addActionListener(e -> handleDownload());

        loadActivities();
    }


    private int getProfesorID() {
        return currentUser.getUserID();
    }

    //activitatile existente ale profesorului in db
    private void loadActivities() {
        tableModel.setRowCount(0);

        int profesorID = getProfesorID();
        List<Activity> list = activityDAO.getActivitiesByProfessor(profesorID);
        for (Activity a : list) {
            tableModel.addRow(new Object[]{
                    a.getActivitateID(),
                    a.getTipActivitate(),
                    a.getCursID(),
                    a.getProcentaj(),
                    a.getStartDate(),
                    a.getEndDate(),
                    a.getRecurenta(),
                    a.getMaxParticipanti()
            });
        }
    }

    private void handleAdd(ActionEvent e) {
        // Minimal dialog
        JTextField tipField = new JTextField("CURS");
        JTextField cursIDField = new JTextField("101");
        JTextField procentajField = new JTextField("25");
        JTextField startDateField = new JTextField("2025-01-01");
        JTextField endDateField = new JTextField("2025-06-15");
        JTextField recurentaField = new JTextField("saptamanal");
        JTextField maxPartField = new JTextField("30");

        Object[] formFields = {
                "Tip Activitate:", tipField,
                "CursID:", cursIDField,
                "Procentaj:", procentajField,
                "StartDate (YYYY-MM-DD):", startDateField,
                "EndDate (YYYY-MM-DD):", endDateField,
                "Recurență:", recurentaField,
                "Max Participanți:", maxPartField
        };

        int option = JOptionPane.showConfirmDialog(this, formFields,
                "Adaugă Activitate", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String tip = tipField.getText().trim();
                int cursID = Integer.parseInt(cursIDField.getText().trim());
                int procentaj = Integer.parseInt(procentajField.getText().trim());
                Date startD = Date.valueOf(startDateField.getText().trim());
                Date endD = Date.valueOf(endDateField.getText().trim());
                String recurenta = recurentaField.getText().trim();
                int maxPart = Integer.parseInt(maxPartField.getText().trim());

                Activity newActivity = new Activity(
                        0, tip, getProfesorID(), cursID, procentaj,
                        startD, endD, recurenta, maxPart
                );
                activityDAO.addActivity(newActivity);
                loadActivities();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage());
            }
        }
    }

    private void handleEdit(ActionEvent e) {
        int row = activitiesTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o activitate din tabel!");
            return;
        }

        int activitateID = (int) tableModel.getValueAt(row, 0);
        String tip = (String) tableModel.getValueAt(row, 1);
        int cursID = (int) tableModel.getValueAt(row, 2);
        int procentaj = (int) tableModel.getValueAt(row, 3);
        Date startD = (Date) tableModel.getValueAt(row, 4);
        Date endD = (Date) tableModel.getValueAt(row, 5);
        String recurenta = (String) tableModel.getValueAt(row, 6);
        int maxP = (int) tableModel.getValueAt(row, 7);

        // Dialog
        JTextField tipField = new JTextField(tip);
        JTextField cursIDField = new JTextField(String.valueOf(cursID));
        JTextField procField = new JTextField(String.valueOf(procentaj));
        JTextField startField = new JTextField(String.valueOf(startD));
        JTextField endField = new JTextField(String.valueOf(endD));
        JTextField recField = new JTextField(recurenta);
        JTextField maxField = new JTextField(String.valueOf(maxP));

        Object[] formFields = {
                "Tip:", tipField,
                "CursID:", cursIDField,
                "Procentaj:", procField,
                "StartDate:", startField,
                "EndDate:", endField,
                "Recurență:", recField,
                "Max Participanți:", maxField
        };
        int option = JOptionPane.showConfirmDialog(this, formFields,
                "Editează Activitate", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String newTip = tipField.getText().trim();
                int newCursID = Integer.parseInt(cursIDField.getText().trim());
                int newProc = Integer.parseInt(procField.getText().trim());
                Date newStart = Date.valueOf(startField.getText().trim());
                Date newEnd = Date.valueOf(endField.getText().trim());
                String newRec = recField.getText().trim();
                int newMax = Integer.parseInt(maxField.getText().trim());

                Activity existing = activityDAO.getActivityById(activitateID);
                if (existing != null) {
                    existing.setTipActivitate(newTip);
                    existing.setCursID(newCursID);
                    existing.setProcentaj(newProc);
                    existing.setStartDate(newStart);
                    existing.setEndDate(newEnd);
                    existing.setRecurenta(newRec);
                    existing.setMaxParticipanti(newMax);

                    activityDAO.updateActivity(existing);
                    loadActivities();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la editare: " + ex.getMessage());
            }
        }
    }

    private void handleDelete(ActionEvent e) {
        int row = activitiesTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o activitate!");
            return;
        }
        int activitateID = (int) tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți activitatea " + activitateID + "?",
                "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                activityDAO.deleteActivity(activitateID);
                loadActivities();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la ștergere: " + ex.getMessage());
            }
        }
    }

    private void handleDownload() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            FileExportUtils.exportTableToCSV(activitiesTable, file.getAbsolutePath() + ".csv");
            JOptionPane.showMessageDialog(this, "Activități exportate cu succes!");
        }
    }
}
