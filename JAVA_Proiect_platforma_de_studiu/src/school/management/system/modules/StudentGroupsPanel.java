package school.management.system.modules;

import javax.swing.*;
import school.management.system.dao.CourseDAO;
import school.management.system.models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import school.management.system.models.*;
import school.management.system.utils.*;
import school.management.system.dao.*;


public class StudentGroupsPanel extends JPanel {
    private User currentUser;
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private StudyGroupDAO groupDAO; // or StudyGroupDAO
    private MemberGroupDAO memberDAO;

    public StudentGroupsPanel(User user) {
        this.currentUser = user;
        groupDAO = new StudyGroupDAO();
        memberDAO = new MemberGroupDAO();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Grupurile Mele", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"GrupID", "CursID", "Min. Participanți", "Nr. Membri"}, 0);
        groupTable = new JTable(tableModel);
        add(new JScrollPane(groupTable), BorderLayout.CENTER);

        // Possibly a refresh button
        JButton refreshButton = new JButton("Reîmprospătează");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadMyGroups());
        loadMyGroups();
    }

    private void loadMyGroups() {
        tableModel.setRowCount(0);

        int studentID = currentUser.getUserID();
        List<StudyGroup> groups = groupDAO.getGroupsForStudent(studentID);
        for (StudyGroup g : groups) {
            tableModel.addRow(new Object[]{
                    g.getGrupID(),
                    g.getCursID(),
                    g.getMinParticipanti(),
                    g.getNrMembri()
            });
        }
    }
}