package school.management.system.modules;

import school.management.system.dao.CourseDAO;
import school.management.system.models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


//Iti permite sa adaugi, sa stergi sau sa madofici cursuri daca esti administrator
public class ManageCoursesPanel extends JPanel {
    private CourseDAO courseDAO;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;//chestie noua
    private JButton addButton, editButton, deleteButton, refreshButton, searchButton;

    public ManageCoursesPanel() {
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Gestionare Cursuri", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Top search panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchField = new JTextField(20);
        searchButton = new JButton("Căutare Curs");
        topPanel.add(new JLabel("Numele/ID Curs:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"CursID", "Nume Curs", "Descriere", "Max. Studenți"}, 0);
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Adaugă Curs");
        editButton = new JButton("Editează Curs");
        deleteButton = new JButton("Șterge Curs");
        refreshButton = new JButton("Reîmprospătează");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addCourse());
        editButton.addActionListener(e -> editCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        refreshButton.addActionListener(e -> loadCourses());
        searchButton.addActionListener(e -> handleSearch());

        // Initial load
        loadCourses();
    }

    private void loadCourses() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                    c.getCursID(),
                    c.getNumeCurs(),
                    c.getDescriere(),
                    c.getNrMaximStudenti()
            });
        }
    }

    private void addCourse() {
        // Campuri for new course
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField maxStudentsField = new JTextField();

        Object[] inputFields = {
                "CursID:", idField,
                "Nume curs:", nameField,
                "Descriere:", descField,
                "Nr. maxim studenți:", maxStudentsField
        };
        int choice = JOptionPane.showConfirmDialog(this, inputFields, "Adaugă Curs", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                int cursID = Integer.parseInt(idField.getText().trim());
                String numeCurs = nameField.getText().trim();
                String descriere = descField.getText().trim();
                int maxStud = Integer.parseInt(maxStudentsField.getText().trim());

                Course newCourse = new Course(cursID, numeCurs, descriere, maxStud);
                courseDAO.addCourse(newCourse);
                loadCourses();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "CursID și Nr. maxim studenți trebuie să fie valori numerice!",
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un curs din tabel!");
            return;
        }
        int cursID = (int) courseTable.getValueAt(row, 0);
        String numeCurs = (String) courseTable.getValueAt(row, 1);
        String descriere = (String) courseTable.getValueAt(row, 2);
        int maxStud = (int) courseTable.getValueAt(row, 3);

        // Prefill dialog
        JTextField nameField = new JTextField(numeCurs);
        JTextField descField = new JTextField(descriere);
        JTextField maxField = new JTextField(String.valueOf(maxStud));

        Object[] inputFields = {
                "Nume curs:", nameField,
                "Descriere:", descField,
                "Nr. maxim studenți:", maxField
        };
        int choice = JOptionPane.showConfirmDialog(this, inputFields, "Editează Curs", JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                String newDesc = descField.getText().trim();
                int newMax = Integer.parseInt(maxField.getText().trim());

                Course existing = courseDAO.getCourseById(cursID);
                if (existing != null) {
                    existing.setNumeCurs(newName);
                    existing.setDescriere(newDesc);
                    existing.setNrMaximStudenti(newMax);

                    courseDAO.updateCourse(existing);
                    loadCourses();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nr. maxim studenți trebuie să fie numeric!",
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare la editare: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un curs din tabel!");
            return;
        }
        int cursID = (int) courseTable.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sigur doriți să ștergeți acest curs?", "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                courseDAO.deleteCourse(cursID);
                loadCourses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare la ștergere: " + ex.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            }
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
