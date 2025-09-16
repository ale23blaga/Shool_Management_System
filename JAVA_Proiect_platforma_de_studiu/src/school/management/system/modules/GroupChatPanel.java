package school.management.system.modules;

import school.management.system.dao.GroupChatDAO;
import school.management.system.models.GroupMessage;
import school.management.system.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class GroupChatPanel extends JPanel {

    private User currentUser;    // The logged-in student
    private JTextField groupIDField;
    private JButton loadButton, sendButton, refreshButton;
    private JTable messageTable;
    private DefaultTableModel tableModel;
    private JTextArea messageField;

    private GroupChatDAO chatDAO;

    public GroupChatPanel(User user) {
        this.currentUser = user;
        this.chatDAO = new GroupChatDAO();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Mesaje în Grup", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Top panel pentru selectare Group ID
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("GrupID:"));
        groupIDField = new JTextField(10);
        loadButton = new JButton("Încarcă Mesaje");
        topPanel.add(groupIDField);
        topPanel.add(loadButton);
        add(topPanel, BorderLayout.NORTH);

        // Tabel pentru mesaje
        tableModel = new DefaultTableModel(new Object[]{"Nume", "Prenume", "Mesaj", "Timestamp"}, 0);
        messageTable = new JTable(tableModel);
        messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Coloanele mesajelor
        messageTable.getColumnModel().getColumn(2).setCellRenderer(new MessageCellRenderer());
        add(new JScrollPane(messageTable), BorderLayout.CENTER);

        //Bottom Panel: mesajul nou + trimite
        JPanel bottomPanel = new JPanel(new BorderLayout());

        //JTextArea in care utilizatoru scrie mesajul
        messageField = new JTextArea(4, 30); // 4 rows, 30 columns
        messageField.setLineWrap(true);
        messageField.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageField);
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Scroll Bar vertical
        bottomPanel.add(messageScroll, BorderLayout.CENTER);

        //Panel pentru butoane sub zona in care se scrie mesajul
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sendButton = new JButton("Trimite");
        refreshButton = new JButton("Reîmprospătează");
        buttonPanel.add(sendButton);
        buttonPanel.add(refreshButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners
        loadButton.addActionListener(e -> loadMessages());
        refreshButton.addActionListener(e -> loadMessages());
        sendButton.addActionListener(e -> sendMessage());
    }

    private void loadMessages() {
        tableModel.setRowCount(0);
        int grupID = Integer.parseInt(groupIDField.getText().trim());

        int studentID = currentUser.getUserID();
        if (!chatDAO.isStudentInGroup(grupID, studentID)) {
            JOptionPane.showMessageDialog(this,
                    "Nu sunteți membru al grupului " + grupID + ", deci nu puteți vedea mesaje aici!",
                    "Acces refuzat", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<GroupMessage> messages = chatDAO.getMessagesByGroup(grupID);
        for (GroupMessage msg : messages) {
            tableModel.addRow(new Object[]{
                    msg.getNume(), // Numele
                    msg.getPrenume(), // prenumele
                    msg.getContent(),
                    msg.getTimestamp()
            });
        }

        // Ajustare inaltimea randului in functie de lungmiea mesajului
        for (int row = 0; row < messageTable.getRowCount(); row++) {
            int rowHeight = messageTable.getRowHeight();
            for (int column = 0; column < messageTable.getColumnCount(); column++) {
                TableCellRenderer renderer = messageTable.getCellRenderer(row, column);
                Component comp = messageTable.prepareRenderer(renderer, row, column);
                rowHeight = Math.max(comp.getPreferredSize().height + 5, rowHeight); // padding
            }
            messageTable.setRowHeight(row, rowHeight);
        }
    }

    private void sendMessage() {
        int grupID = Integer.parseInt(groupIDField.getText().trim());
        String content = messageField.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mesajul nu poate fi gol!");
            return;
        }

        // 1) Verificare daca apartine grupului
        int studentID = currentUser.getUserID();
        if (!chatDAO.isStudentInGroup(grupID, studentID)) {
            JOptionPane.showMessageDialog(this,
                    "Nu sunteți membru al grupului " + grupID + ", deci nu puteți trimite mesaje aici!",
                    "Acces refuzat", JOptionPane.ERROR_MESSAGE);
            return;
        }

        studentID = currentUser.getUserID();
        try {
            chatDAO.addMessage(grupID, studentID, content);
            messageField.setText("");
            loadMessages(); // refresh the chat
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la trimitere mesaj: " + ex.getMessage());
        }
    }


    class MessageCellRenderer extends JTextArea implements TableCellRenderer {
        public MessageCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setFont(new Font("Arial", Font.PLAIN, 12));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            return this;
        }
    }
}
