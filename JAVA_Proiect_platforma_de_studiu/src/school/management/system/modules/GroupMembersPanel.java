package school.management.system.modules;


import school.management.system.dao.StudyGroupDAO;
import school.management.system.models.GroupMemberInfo; // a simple DTO
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

//Cautarea membrilor unui grup dupa id ul study groupului
public class GroupMembersPanel extends JPanel {

    private JTextField groupIDField;
    private JButton searchButton;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private StudyGroupDAO groupDAO;

    public GroupMembersPanel() {
        groupDAO = new StudyGroupDAO();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Membrii Grup (Căutare după GrupID)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Top: cautare
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("GrupID:"));
        groupIDField = new JTextField(10);
        searchButton = new JButton("Caută Membri");
        topPanel.add(groupIDField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"StudentID", "Nume", "Prenume"}, 0);
        membersTable = new JTable(tableModel);
        add(new JScrollPane(membersTable), BorderLayout.CENTER);

        // Action
        searchButton.addActionListener(e -> handleSearch());
    }

    private void handleSearch() {
        tableModel.setRowCount(0);
        int grupID = Integer.parseInt(groupIDField.getText().trim());

        List<GroupMemberInfo> members = groupDAO.getMembersByGroup(grupID);
        for (GroupMemberInfo m : members) {
            tableModel.addRow(new Object[]{
                    m.getStudentID(),
                    m.getNume(),
                    m.getPrenume()
            });
        }
    }
}
