package school.management.system.modules;

import school.management.system.models.User;
import school.management.system.models.StudyGroup;
import school.management.system.dao.StudyGroupDAO;
import school.management.system.dao.MemberGroupDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


//Afiseaza si le permite studentilor sa se alature sau sa paraseasca grupuri de studiu.
public class GroupsPanel extends JPanel {

    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton joinButton, leaveButton, refreshButton;

    private StudyGroupDAO groupDAO;
    private MemberGroupDAO memberDAO;

    public GroupsPanel(User user) {
        this.currentUser = user;
        this.groupDAO = new StudyGroupDAO();
        this.memberDAO = new MemberGroupDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Grupuri de studiu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"GrupID", "CursID", "Min Participanți"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        joinButton = new JButton("Înscriere în grup");
        leaveButton = new JButton("Părăsește grup");
        refreshButton = new JButton("Reîncarcă grupuri");

        bottomPanel.add(joinButton);
        bottomPanel.add(leaveButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        joinButton.addActionListener(e -> joinSelectedGroup());
        leaveButton.addActionListener(e -> leaveSelectedGroup());
        refreshButton.addActionListener(e -> loadGroups());

        loadGroups();
    }

    //Incarcare grupuri
    private void loadGroups() {
        tableModel.setRowCount(0);

        int studentID = currentUser.getUserID();

        List<StudyGroup> allGroups = groupDAO.getAllGroups();

        for (StudyGroup g : allGroups) {
            tableModel.addRow(new Object[]{
                    g.getGrupID(),
                    g.getCursID(),
                    g.getMinParticipanti()
            });
        }
    }

    //Alaturare la grupul selectat
    private void joinSelectedGroup() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un grup din tabel.");
            return;
        }
        int grupID = (int) tableModel.getValueAt(row, 0);
        int studentID = currentUser.getUserID();

        try {
            memberDAO.addMemberToGroup(grupID, studentID);
            JOptionPane.showMessageDialog(this, "V-ați înscris în grupul " + grupID);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la înscriere: " + e.getMessage());
        }
    }

    //Parasirea grupului selectat
    private void leaveSelectedGroup() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un grup din tabel.");
            return;
        }
        int grupID = (int) tableModel.getValueAt(row, 0);
        int studentID = currentUser.getUserID();

        try {
            memberDAO.removeMemberFromGroup(grupID, studentID);
            JOptionPane.showMessageDialog(this, "Ați părăsit grupul " + grupID);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la părăsire: " + e.getMessage());
        }
    }
}
