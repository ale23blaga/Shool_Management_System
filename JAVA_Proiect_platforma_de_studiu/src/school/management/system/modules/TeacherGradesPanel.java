package school.management.system.modules;

import school.management.system.dao.CatalogDAO;
import school.management.system.models.CatalogEntry;
import school.management.system.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

public class TeacherGradesPanel extends JPanel {

    private User currentUser;
    private JTable catalogTable;
    private DefaultTableModel tableModel;
    private JButton addGradeButton, editGradeButton, refreshButton;

    private CatalogDAO catalogDAO;

    public TeacherGradesPanel(User user) {
        this.currentUser = user;
        this.catalogDAO = new CatalogDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Catalog - Note (Pentru toate cursurile)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(
                new Object[] { "StudentID", "CursID", "Nota", "ProcentajID", "Prezență", "DataA" },
                0
        );
        catalogTable = new JTable(tableModel);
        catalogTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(catalogTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addGradeButton = new JButton("Adaugă Notă");
        editGradeButton = new JButton("Editează Notă");
        refreshButton = new JButton("Reîmprospătează");

        bottomPanel.add(addGradeButton);
        bottomPanel.add(editGradeButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners for buttons
        addGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddGrade();
            }
        });

        editGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEditGrade();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGrades();
            }
        });

        // Initial load of grades
        loadGrades();
    }

    // Load grades for all the courses taught by the teacher
    private void loadGrades() {
        tableModel.setRowCount(0); // Clear previous data

        int profesorID = currentUser.getUserID();

        // Get catalog entries for all courses that the teacher is teaching
        List<CatalogEntry> entries = catalogDAO.getCatalogEntriesByProfessor(profesorID);
        for (CatalogEntry ce : entries) {
            tableModel.addRow(new Object[]{
                    ce.getStudentID(),
                    ce.getCursID(),
                    ce.getNota(),
                    ce.getProcentajID(),
                    ce.isPrezenta() ? "Prezent" : "Absent",
                    ce.getDataA()
            });
        }
    }

    // Handle adding a new grade
    private void handleAddGrade() {
        JTextField studentIDField = new JTextField();
        JTextField cursIDField = new JTextField();
        JTextField notaField = new JTextField();
        JTextField procIDField = new JTextField();
        JCheckBox prezentaCheck = new JCheckBox("Prezent", true);
        JTextField dataAField = new JTextField("2025-01-01");

        Object[] formFields = {
                "StudentID:", studentIDField,
                "CursID:", cursIDField,
                "Nota:", notaField,
                "ProcentajID (ActivitateID):", procIDField,
                "Prezență:", prezentaCheck,
                "Data (YYYY-MM-DD):", dataAField
        };

        int option = JOptionPane.showConfirmDialog(this, formFields,
                "Adaugă Notă Nouă", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int studentID = Integer.parseInt(studentIDField.getText().trim());
                int cursID = Integer.parseInt(cursIDField.getText().trim());
                int nota = Integer.parseInt(notaField.getText().trim());
                int procID = Integer.parseInt(procIDField.getText().trim());
                boolean prezenta = prezentaCheck.isSelected();
                Date dataA = Date.valueOf(dataAField.getText().trim());

                int profesorID = currentUser.getUserID();

                CatalogEntry newEntry = new CatalogEntry(
                        0,           // intrareID (auto-increment)
                        profesorID,
                        studentID,
                        cursID,
                        nota,
                        procID,
                        prezenta,
                        dataA
                );
                catalogDAO.addCatalogEntry(newEntry);
                loadGrades();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Eroare la adăugare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Handle editing an existing grade
    private void handleEditGrade() {
        int row = catalogTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o notă din tabel!");
            return;
        }

        int intrareID = (int) tableModel.getValueAt(row, 0);
        int studentID = (int) tableModel.getValueAt(row, 1);
        int cursID = (int) tableModel.getValueAt(row, 2);
        int nota = (int) tableModel.getValueAt(row, 3);
        int procID = (int) tableModel.getValueAt(row, 4);
        String prezentaStr = (String) tableModel.getValueAt(row, 5);
        Date dataA = (Date) tableModel.getValueAt(row, 6);

        JTextField studentIDField = new JTextField(String.valueOf(studentID));
        JTextField cursIDField = new JTextField(String.valueOf(cursID));
        JTextField notaField = new JTextField(String.valueOf(nota));
        JTextField procIDField = new JTextField(String.valueOf(procID));
        JCheckBox prezentaCheck = new JCheckBox("Prezent", "Prezent".equals(prezentaStr));
        JTextField dataAField = new JTextField(String.valueOf(dataA));

        Object[] formFields = {
                "StudentID:", studentIDField,
                "CursID:", cursIDField,
                "Nota:", notaField,
                "ProcentajID:", procIDField,
                "Prezență:", prezentaCheck,
                "Data (YYYY-MM-DD):", dataAField
        };

        int option = JOptionPane.showConfirmDialog(this, formFields,
                "Editează Notă", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int newStudentID = Integer.parseInt(studentIDField.getText().trim());
                int newCursID = Integer.parseInt(cursIDField.getText().trim());
                int newNota = Integer.parseInt(notaField.getText().trim());
                int newProcID = Integer.parseInt(procIDField.getText().trim());
                boolean newPrezenta = prezentaCheck.isSelected();
                Date newDataA = Date.valueOf(dataAField.getText().trim());

                CatalogEntry existing = catalogDAO.getCatalogEntry(intrareID);
                if (existing != null) {
                    existing.setStudentID(newStudentID);
                    existing.setCursID(newCursID);
                    existing.setNota(newNota);
                    existing.setProcentajID(newProcID);
                    existing.setPrezenta(newPrezenta);
                    existing.setDataA(newDataA);

                    catalogDAO.updateCatalogEntry(existing);
                    loadGrades();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Eroare la editare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
