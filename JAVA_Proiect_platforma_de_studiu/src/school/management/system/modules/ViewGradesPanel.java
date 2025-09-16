package school.management.system.modules;

import school.management.system.models.User;
import school.management.system.models.CatalogEntry;
import school.management.system.dao.CatalogDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//Notele studentului in toate activitatile
public class ViewGradesPanel extends JPanel {

    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    private CatalogDAO catalogDAO;

    public ViewGradesPanel(User user) {
        this.currentUser = user;
        this.catalogDAO = new CatalogDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Notele tale", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
                "intrareID", "ProfesorID", "CursID", "Nota", "ActivitateID", "Prezență", "Dată"
        }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        refreshButton = new JButton("Reîmprospătează notele");
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadGrades());

        loadGrades();
    }

    private void loadGrades() {
        tableModel.setRowCount(0);

        int studentID = currentUser.getUserID();

        List<CatalogEntry> list = catalogDAO.getGradesForStudent(studentID);
        for (CatalogEntry ce : list) {
            tableModel.addRow(new Object[]{
                    ce.getIntrareID(),
                    ce.getProfesorID(),
                    ce.getCursID(),
                    ce.getNota(),
                    ce.getProcentajID(),
                    ce.isPrezenta() ? "Prezent" : "Absent",
                    ce.getDataA()
            });
        }
    }
}
