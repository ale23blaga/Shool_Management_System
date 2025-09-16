package school.management.system.modules;

import school.management.system.dao.UserDAO;
import school.management.system.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;


//Pagina prin care super-administratorul poate sa adauge sau sa sterga alti administratori sau
    //super administratori
public class ManageAdminsPanel extends JPanel {
    private UserDAO userDAO;
    private JTable adminTable;
    private DefaultTableModel tableModel;

    private JButton addButton, editButton, deleteButton, refreshButton;

    public ManageAdminsPanel() {
        userDAO = new UserDAO();
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Gestionare Administratori / Super-Administratori", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"UserID", "Nume", "Prenume", "UserType", "Username"}, 0);
        adminTable = new JTable(tableModel);
        adminTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Adaugă Admin");
        editButton = new JButton("Editează Admin");
        deleteButton = new JButton("Șterge Admin");
        refreshButton = new JButton("Reîmprospătează");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addAdmin());
        editButton.addActionListener(e -> editAdmin());
        deleteButton.addActionListener(e -> deleteAdmin());
        refreshButton.addActionListener(e -> loadAdmins());

        // Initial load
        loadAdmins();
    }

   //afiseaza toti admini
    private void loadAdmins() {
        tableModel.setRowCount(0); // Clear table
        try {
            ResultSet rs = userDAO.getAdmins();
            while (rs != null && rs.next()) {
                int userID = rs.getInt("UserID");
                String nume = rs.getString("Nume");
                String prenume = rs.getString("Prenume");
                String userType = rs.getString("UserType");
                String username = rs.getString("username");

                tableModel.addRow(new Object[]{userID, nume, prenume, userType, username});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAdmin() {
        JTextField userIDField = new JTextField();
        JTextField numeField = new JTextField();
        JTextField prenumeField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField parolaField = new JTextField();

        // Adaugare de  'administrator' sau 'super-administrator'
        String[] roles = {"administrator", "super-administrator"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        Object[] formFields = {
                "UserID:", userIDField,
                "Nume:", numeField,
                "Prenume:", prenumeField,
                "Username:", usernameField,
                "Parola:", parolaField,
                "Rol:", roleCombo
        };

        int option = JOptionPane.showConfirmDialog(this, formFields, "Adaugă Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int userID = Integer.parseInt(userIDField.getText().trim());
                String nume = numeField.getText().trim();
                String prenume = prenumeField.getText().trim();
                String username = usernameField.getText().trim();
                String parola = parolaField.getText().trim();
                String role = (String) roleCombo.getSelectedItem();


                User newUser = new User(
                        userID,
                        "0000000000000", // dummy CNP
                        nume,
                        prenume,
                        1,               // dummy AdresaID
                        "0722xxxxxx",    // dummy phone
                        "user@example.com", // email
                        "RO00XXXX000000",   // IBAN
                        "ContractXXX",      // contract
                        role,
                        username,
                        parola
                );

                userDAO.addUser(newUser);
                JOptionPane.showMessageDialog(this, "Utilizator adăugat cu succes!");
                loadAdmins();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "UserID trebuie să fie numeric!", "Eroare", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editAdmin() {
        int row = adminTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un admin din tabel!");
            return;
        }

        int userID = (int) adminTable.getValueAt(row, 0);
        String nume = (String) adminTable.getValueAt(row, 1);
        String prenume = (String) adminTable.getValueAt(row, 2);
        String userType = (String) adminTable.getValueAt(row, 3);
        String username = (String) adminTable.getValueAt(row, 4);

        JTextField numeField = new JTextField(nume);
        JTextField prenumeField = new JTextField(prenume);
        JTextField usernameField = new JTextField(username);

        String[] roles = {"administrator", "super-administrator"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setSelectedItem(userType);

        Object[] formFields = {
                "Nume:", numeField,
                "Prenume:", prenumeField,
                "Username:", usernameField,
                "Rol:", roleCombo
        };

        int option = JOptionPane.showConfirmDialog(this, formFields, "Editează Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                User existingUser = userDAO.getUserById(userID);
                if (existingUser != null) {
                    existingUser.setNume(numeField.getText().trim());
                    existingUser.setPrenume(prenumeField.getText().trim());
                    existingUser.setUsername(usernameField.getText().trim());
                    existingUser.setUserType((String) roleCombo.getSelectedItem());

                    userDAO.updateUser(existingUser);
                    JOptionPane.showMessageDialog(this, "Admin actualizat cu succes!");
                    loadAdmins();
                } else {
                    JOptionPane.showMessageDialog(this, "Nu s-a găsit utilizatorul cu ID: " + userID, "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la actualizare: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAdmin() {
        int row = adminTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un admin din tabel!");
            return;
        }

        int userID = (int) adminTable.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Sigur doriți să ștergeți acest administrator?",
                "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userDAO.deleteUser(userID);
                JOptionPane.showMessageDialog(this, "Admin șters cu succes!");
                loadAdmins();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la ștergere: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
