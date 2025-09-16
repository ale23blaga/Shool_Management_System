package school.management.system.modules;

import school.management.system.dao.ActivityDAO;
import school.management.system.models.Activity;
import school.management.system.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


//Pagina in care profesori isi pot manageria activitatea (cursuri, seminare, etc)
public class ManageTeacherActivitiesPanel extends JPanel {

    private User currentUser;
    private JTable activityTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;

    private ActivityDAO activityDAO;

    public ManageTeacherActivitiesPanel(User user) {
        this.currentUser = user;
        this.activityDAO = new ActivityDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gestionare Activități", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table columns to display
        tableModel = new DefaultTableModel(new Object[]{
                "ActivitateID", "Tip Activitate", "CursID", "Procentaj", "StartDate", "EndDate", "Recurență", "Max. Participanți"
        }, 0);
        activityTable = new JTable(tableModel);
        activityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(activityTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with CRUD buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Adaugă Activitate");
        editButton = new JButton("Editează Activitate");
        deleteButton = new JButton("Șterge Activitate");
        refreshButton = new JButton("Reîmprospătează");

        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(this::handleAdd);
        editButton.addActionListener(this::handleEdit);
        deleteButton.addActionListener(this::handleDelete);
        refreshButton.addActionListener(e -> loadActivities());

        loadActivities();
    }


    private int getProfesorID() {
        return currentUser.getUserID();
    }


    //tabel cu toate activitatile profesorului
    private void loadActivities() {
        tableModel.setRowCount(0);

        int profesorID = getProfesorID();
        List<Activity> activities = activityDAO.getActivitiesByProfessor(profesorID);
        for (Activity a : activities) {
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

    //Adaugare unei noi activitati
    private void handleAdd(ActionEvent e) {
        // Minimal dialog
        JTextField tipField = new JTextField();
        JTextField cursIDField = new JTextField();
        JTextField procentajField = new JTextField();
        JTextField startDateField = new JTextField("YYYY-MM-DD");
        JTextField endDateField = new JTextField("YYYY-MM-DD");
        JTextField recurentaField = new JTextField("saptamanal / lunar etc.");
        JTextField maxPartField = new JTextField();

        Object[] fields = {
                "Tip Activitate:", tipField,
                "CursID:", cursIDField,
                "Procentaj:", procentajField,
                "StartDate:", startDateField,
                "EndDate:", endDateField,
                "Recurență:", recurentaField,
                "Max. Participanți:", maxPartField
        };
        int option = JOptionPane.showConfirmDialog(this, fields, "Adaugă Activitate", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String tip = tipField.getText().trim();
                int cursID = Integer.parseInt(cursIDField.getText().trim());
                int procentaj = Integer.parseInt(procentajField.getText().trim());
                java.sql.Date startDate = java.sql.Date.valueOf(startDateField.getText().trim());
                java.sql.Date endDate = java.sql.Date.valueOf(endDateField.getText().trim());
                String recurenta = recurentaField.getText().trim();
                int maxPart = Integer.parseInt(maxPartField.getText().trim());

                // Construirea obiectului activitate
                Activity newActivity = new Activity(
                        0,
                        tip,
                        getProfesorID(),
                        cursID,
                        procentaj,
                        startDate,
                        endDate,
                        recurenta,
                        maxPart
                );
                activityDAO.addActivity(newActivity);
                loadActivities();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage());
            }
        }
    }

    //Editarea activitati selectate
    private void handleEdit(ActionEvent e) {
        int row = activityTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o activitate din tabel!");
            return;
        }

        int activitateID = (int) tableModel.getValueAt(row, 0);
        String tip = (String) tableModel.getValueAt(row, 1);
        int cursID = (int) tableModel.getValueAt(row, 2);
        int procentaj = (int) tableModel.getValueAt(row, 3);
        java.sql.Date startDate = (java.sql.Date) tableModel.getValueAt(row, 4);
        java.sql.Date endDate = (java.sql.Date) tableModel.getValueAt(row, 5);
        String recurenta = (String) tableModel.getValueAt(row, 6);
        int maxPart = (int) tableModel.getValueAt(row, 7);

        JTextField tipField = new JTextField(tip);
        JTextField cursIDField = new JTextField(String.valueOf(cursID));
        JTextField procentajField = new JTextField(String.valueOf(procentaj));
        JTextField startDateField = new JTextField(String.valueOf(startDate));
        JTextField endDateField = new JTextField(String.valueOf(endDate));
        JTextField recurentaField = new JTextField(recurenta);
        JTextField maxPartField = new JTextField(String.valueOf(maxPart));

        Object[] fields = {
                "Tip Activitate:", tipField,
                "CursID:", cursIDField,
                "Procentaj:", procentajField,
                "StartDate:", startDateField,
                "EndDate:", endDateField,
                "Recurență:", recurentaField,
                "Max. Participanți:", maxPartField
        };
        int option = JOptionPane.showConfirmDialog(this, fields, "Editează Activitate", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String newTip = tipField.getText().trim();
                int newCursID = Integer.parseInt(cursIDField.getText().trim());
                int newProcentaj = Integer.parseInt(procentajField.getText().trim());
                java.sql.Date newStart = java.sql.Date.valueOf(startDateField.getText().trim());
                java.sql.Date newEnd = java.sql.Date.valueOf(endDateField.getText().trim());
                String newRecurenta = recurentaField.getText().trim();
                int newMaxPart = Integer.parseInt(maxPartField.getText().trim());

                Activity existing = activityDAO.getActivityById(activitateID);
                if (existing != null) {
                    existing.setTipActivitate(newTip);
                    existing.setCursID(newCursID);
                    existing.setProcentaj(newProcentaj);
                    existing.setStartDate(newStart);
                    existing.setEndDate(newEnd);
                    existing.setRecurenta(newRecurenta);
                    existing.setMaxParticipanti(newMaxPart);

                    activityDAO.updateActivity(existing);
                    loadActivities();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la editare: " + ex.getMessage());
            }
        }
    }

    //Stergerea unei activitati selectate
    private void handleDelete(ActionEvent e) {
        int row = activityTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o activitate din tabel!");
            return;
        }
        int activitateID = (int) tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți această activitate?",
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
}
