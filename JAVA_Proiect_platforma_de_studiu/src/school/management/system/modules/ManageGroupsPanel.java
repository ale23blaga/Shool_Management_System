package school.management.system.modules;

import school.management.system.dao.StudyGroupDAO;
import school.management.system.models.StudyGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//Administratorul poate adauga, sterge, sau edita grupuri de studiu
public class ManageGroupsPanel extends JPanel {
    private StudyGroupDAO groupDAO;
    private JTable groupTable;
    private DefaultTableModel tableModel;

    private JButton addButton, editButton, deleteButton, refreshButton;

    public ManageGroupsPanel() {
        groupDAO = new StudyGroupDAO();
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Gestionare Grupuri de Studiu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"GrupID", "CursID", "Min. Participanți"}, 0);
        groupTable = new JTable(tableModel);
        groupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Adaugă Grup");
        editButton = new JButton("Editează Grup");
        deleteButton = new JButton("Șterge Grup");
        refreshButton = new JButton("Reîmprospătează");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addGroup());
        editButton.addActionListener(e -> editGroup());
        deleteButton.addActionListener(e -> deleteGroup());
        refreshButton.addActionListener(e -> loadGroups());

        // Initial load
        loadGroups();
    }

    private void loadGroups() {
        tableModel.setRowCount(0);
        List<StudyGroup> groupList = groupDAO.getAllGroups();
        for (StudyGroup g : groupList) {
            tableModel.addRow(new Object[]{
                    g.getGrupID(),
                    g.getCursID(),
                    g.getMinParticipanti()
            });
        }
    }

    private void addGroup() {
        JTextField grupIDField = new JTextField();
        JTextField cursIDField = new JTextField();
        JTextField minPartField = new JTextField();

        Object[] inputFields = {
                "GrupID:", grupIDField,
                "CursID:", cursIDField,
                "Min. Participanți:", minPartField
        };
        int choice = JOptionPane.showConfirmDialog(this, inputFields, "Adaugă Grup", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                int grupID = Integer.parseInt(grupIDField.getText().trim());
                int cursID = Integer.parseInt(cursIDField.getText().trim());
                int minP = Integer.parseInt(minPartField.getText().trim());

                StudyGroup newGroup = new StudyGroup(grupID, cursID, minP);
                groupDAO.addStudyGroup(newGroup);
                loadGroups();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "GrupID, CursID și Min. Participanți trebuie să fie valori numerice!",
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Eroare la adăugare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editGroup() {
        int row = groupTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un grup din tabel!");
            return;
        }
        int grupID = (int) groupTable.getValueAt(row, 0);
        int cursID = (int) groupTable.getValueAt(row, 1);
        int minP = (int) groupTable.getValueAt(row, 2);

        JTextField cursIDField = new JTextField(String.valueOf(cursID));
        JTextField minPartField = new JTextField(String.valueOf(minP));

        Object[] inputFields = {
                "CursID:", cursIDField,
                "Min. Participanți:", minPartField
        };
        int choice = JOptionPane.showConfirmDialog(this, inputFields, "Editează Grup", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                int newCursID = Integer.parseInt(cursIDField.getText().trim());
                int newMinP = Integer.parseInt(minPartField.getText().trim());

                StudyGroup existing = groupDAO.getGroupById(grupID);
                if (existing != null) {
                    existing.setCursID(newCursID);
                    existing.setMinParticipanti(newMinP);
                    groupDAO.updateStudyGroup(existing);
                    loadGroups();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "CursID și Min. Participanți trebuie să fie numerice!",
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la editare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteGroup() {
        int row = groupTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un grup din tabel!");
            return;
        }
        int grupID = (int) groupTable.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți acest grup?",
                "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                groupDAO.deleteStudyGroup(grupID);
                loadGroups();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Eroare la ștergere: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
