package school.management.system.modules;

import school.management.system.dao.CatalogDAO;
import school.management.system.dao.CourseDAO;
import school.management.system.models.CatalogEntry;
import school.management.system.models.Course;
import school.management.system.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


//Pagina in care profesori pot vedea si modifica notele studentilor - nu mai e utilizata?
public class CatalogPanel extends JPanel {

    private User currentUser;  // profesorul conectat
    private JComboBox<Course> courseCombo;
    private JTable catalogTable;
    private DefaultTableModel tableModel;
    private JButton addGradeButton, editGradeButton, refreshButton;

    private CatalogDAO catalogDAO;
    private CourseDAO courseDAO;

    public CatalogPanel(User user) {
        this.currentUser = user;
        this.catalogDAO = new CatalogDAO();
        this.courseDAO = new CourseDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Catalog Studenți", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        courseCombo = new JComboBox<>();
        topPanel.add(new JLabel("Selectați un curs:"));
        topPanel.add(courseCombo);

        // Acesta nu mai merge
        loadCoursesForTeacher();
        add(topPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{
                "intrareID", "StudentID", "CursID", "Nota", "ProcentajID", "Prezență", "DataA"
        }, 0);
        catalogTable = new JTable(tableModel);
        catalogTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(catalogTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addGradeButton = new JButton("Adaugă Notă");
        editGradeButton = new JButton("Editează Notă");
        refreshButton = new JButton("Reîmprospătează");

        bottomPanel.add(addGradeButton);
        bottomPanel.add(editGradeButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        courseCombo.addActionListener(this::handleCourseSelection);
        addGradeButton.addActionListener(this::handleAddGrade);
        editGradeButton.addActionListener(this::handleEditGrade);
        refreshButton.addActionListener(e -> loadCatalogEntries());


        loadCatalogEntries();
    }

    //Cursurile predate de un profesor
    private void loadCoursesForTeacher() {
        int profesorID = currentUser.getUserID();
        List<Course> teacherCourses = courseDAO.getCoursesByProfessor(profesorID);

        courseCombo.removeAllItems();
        for (Course c : teacherCourses) {
            courseCombo.addItem(c);
            // If Course overrides toString() with c.getNumeCurs(),
            // the combo will show the course name
        }
    }


    private void handleCourseSelection(ActionEvent e) {
        loadCatalogEntries();
    }

    //Incarca tuplele din catalog
    private void loadCatalogEntries() {
        tableModel.setRowCount(0);

        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        if (selectedCourse == null) {
            return; //Nu avem curs selectat
        }
        int cursID = selectedCourse.getCursID();

        List<CatalogEntry> catalogList = catalogDAO.getCatalogEntriesByCourse(cursID);

        for (CatalogEntry ce : catalogList) {
            tableModel.addRow(new Object[]{
                    ce.getIntrareID(),
                    ce.getStudentID(),
                    ce.getCursID(),
                    ce.getNota(),
                    ce.getProcentajID(),
                    ce.isPrezenta() ? "Prezent" : "Absent",
                    ce.getDataA()
            });
        }
    }

    //Adauga o nota noua pentru un student
    private void handleAddGrade(ActionEvent e) {
        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Selectați mai întâi un curs!");
            return;
        }
        int cursID = selectedCourse.getCursID();

        //Formular in care se alege studentul, cursul, procentajul
        JTextField studentIDField = new JTextField();
        JTextField notaField = new JTextField();
        JTextField procentajIDField = new JTextField();
        JCheckBox prezentaCheck = new JCheckBox("Prezent", true);
        JTextField dataAField = new JTextField("YYYY-MM-DD");

        Object[] fields = {
                "StudentID:", studentIDField,
                "Nota:", notaField,
                "ProcentajID (Activitate ID):", procentajIDField,
                "Prezență:", prezentaCheck,
                "DataA (YYYY-MM-DD):", dataAField
        };
        int option = JOptionPane.showConfirmDialog(this, fields, "Adaugă Notă", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int studentID = Integer.parseInt(studentIDField.getText().trim());
                int nota = Integer.parseInt(notaField.getText().trim());
                int procentajID = Integer.parseInt(procentajIDField.getText().trim());
                boolean prezenta = prezentaCheck.isSelected();
                java.sql.Date dataA = java.sql.Date.valueOf(dataAField.getText().trim());

                //Creare tupla in catalog
                CatalogEntry ce = new CatalogEntry(
                        0, // intrareID auto
                        currentUser.getUserID(), // profesorID
                        studentID,
                        cursID,
                        nota,
                        procentajID,
                        prezenta,
                        dataA
                );
                catalogDAO.addCatalogEntry(ce);
                loadCatalogEntries();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage());
            }
        }
    }

    //Editarea unei tuple din catalog
    private void handleEditGrade(ActionEvent e) {
        int row = catalogTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați o înregistrare din tabel!");
            return;
        }

        //Luam datele din tupla care exista deja
        int intrareID = (int) tableModel.getValueAt(row, 0);
        int studentID = (int) tableModel.getValueAt(row, 1);
        int cursID = (int) tableModel.getValueAt(row, 2);
        int nota = (int) tableModel.getValueAt(row, 3);
        int procentajID = (int) tableModel.getValueAt(row, 4);
        String prezentaStr = (String) tableModel.getValueAt(row, 5);
        java.sql.Date dataA = (java.sql.Date) tableModel.getValueAt(row, 6);

        boolean prezenta = "Prezent".equals(prezentaStr);

        //Informatiile noi
        JTextField notaField = new JTextField(String.valueOf(nota));
        JTextField procentajIDField = new JTextField(String.valueOf(procentajID));
        JCheckBox prezentaCheck = new JCheckBox("Prezent", prezenta);
        JTextField dataAField = new JTextField(String.valueOf(dataA));

        Object[] fields = {
                "Nota:", notaField,
                "ProcentajID:", procentajIDField,
                "Prezență:", prezentaCheck,
                "DataA (YYYY-MM-DD):", dataAField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Editează Notă", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int newNota = Integer.parseInt(notaField.getText().trim());
                int newProcID = Integer.parseInt(procentajIDField.getText().trim());
                boolean newPrezenta = prezentaCheck.isSelected();
                java.sql.Date newDate = java.sql.Date.valueOf(dataAField.getText().trim());

                //Luam informatiile din bd
                CatalogEntry ce = catalogDAO.getCatalogEntry(intrareID);
                if (ce != null) {
                    ce.setNota(newNota);
                    ce.setProcentajID(newProcID);
                    ce.setPrezenta(newPrezenta);
                    ce.setDataA(newDate);

                    catalogDAO.updateCatalogEntry(ce);
                    loadCatalogEntries();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la editare: " + ex.getMessage());
            }
        }
    }
}
