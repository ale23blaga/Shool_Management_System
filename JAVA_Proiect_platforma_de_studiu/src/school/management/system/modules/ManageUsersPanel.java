package school.management.system.modules;

import school.management.system.dao.DatabaseConnection;
import school.management.system.dao.UserDAO;
import school.management.system.models.User;
import school.management.system.utils.MyTableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ManageUsersPanel extends JPanel {
    private JTextField searchField;
    private JButton  allUsersButton, searchButton, addButton, editButton, deleteButton;
    private JTable table;
    private UserDAO userDAO;

    public ManageUsersPanel() {
        userDAO = new UserDAO();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        allUsersButton = new JButton("Toti Utilizatori");
        searchField = new JTextField(15);
        searchButton = new JButton("Caută");
        addButton = new JButton("Adaugă Utilizator");
        editButton = new JButton("Editează Utilizator");
        deleteButton = new JButton("Șterge Utilizator");

        topPanel.add(allUsersButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        table = new JTable(); // poate custom TableModel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        allUsersButton.addActionListener(e -> loadUsers(searchField.getText()));
        searchButton.addActionListener(e -> loadUsers2(searchField.getText()));
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int userId = (int) table.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(
                        this, "Sigur doriți să ștergeți acest utilizator?", "Confirmare", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userDAO.deleteUser(userId);
                    loadUsers("");
                }
            }
        });

        loadUsers("");
    }

    private void loadUsers(String keyword) {
        ResultSet rs = null;
        try {
            rs = userDAO.getAllUsers();
            //Converteste result setul in tabel model
            table.setModel(MyTableUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la încărcarea utilizatorilor: " + e.getMessage(),
                    "Eroare", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (rs != null) {
                try {
                    Statement stmt = rs.getStatement();
                    rs.close();
                    if (stmt != null) stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void loadUsers2(String keyword) {
        if(keyword.equals(""))
        {
            ResultSet rs = null;
            try {
                rs = userDAO.getAllUsers();
                //Converteste result setul in tabel model
                table.setModel(MyTableUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la încărcarea utilizatorilor: " + e.getMessage(),
                        "Eroare", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (rs != null) {
                    try {
                        Statement stmt = rs.getStatement();
                        rs.close();
                        if (stmt != null) stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        else
        {

            String sql = "SELECT * FROM USERS WHERE UserID =? or Nume=? or Prenume=? ";
            try(Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, keyword);  // for UserID
                stmt.setString(2, keyword);  // for Nume
                stmt.setString(3, keyword);  // for Prenume
                ResultSet rs = stmt.executeQuery();
                table.setModel(MyTableUtils.resultSetToTableModel(rs));
            }catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    //Adaugare useri
    private void handleAdd() {
        JTextField cnpField = new JTextField();
        JTextField numeField = new JTextField();
        JTextField prenumeField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField ibanField = new JTextField();
        JTextField contractField = new JTextField();

        String[] userTypes = {"student", "professor"};
        JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);

        JTextField taraField = new JTextField();
        JTextField judetField = new JTextField();
        JTextField orasField = new JTextField();
        JTextField stradaField = new JTextField();
        JTextField numarField = new JTextField();

        Object[] formFields = {
                "CNP:", cnpField,
                "Nume:", numeField,
                "Prenume:", prenumeField,
                "Telefon:", phoneField,
                "Email:", emailField,
                "IBAN:", ibanField,
                "ContractNumber:", contractField,
                "UserType:", userTypeCombo,
                // Address fields
                "Tara:", taraField,
                "Judet:", judetField,
                "Oras:", orasField,
                "Strada:", stradaField,
                "Numar:", numarField
        };

        int option = JOptionPane.showConfirmDialog(
                this, formFields, "Adaugă Utilizator", JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                // 2) Construirea obiectului user
                User newUser = new User(
                        // userID - auto-increment,
                        0,
                        cnpField.getText().trim(),
                        numeField.getText().trim(),
                        prenumeField.getText().trim(),
                        // AddressID
                        0,
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        ibanField.getText().trim(),
                        contractField.getText().trim(),
                        userTypeCombo.getSelectedItem().toString(),
                        numeField.getText().trim() + prenumeField.getText().trim(),   // username
                        "1234"// parola
                );

                //Verificam daca adresa adaugata exista, si daca nu exista cream alta
                int adresaID = ensureAddressExists(taraField.getText().trim(),
                        judetField.getText().trim(),
                        orasField.getText().trim(),
                        stradaField.getText().trim(),
                        numarField.getText().trim());
                newUser.setAdresaID(adresaID);


                userDAO.addUser(newUser);

                // 4) Refresh the table
                loadUsers("");
                JOptionPane.showMessageDialog(this, "Utilizator adăugat cu succes!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Eroare la adăugare: " + ex.getMessage());
            }
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectați un utilizator din tabel!");
            return;
        }

        int userID = (int) table.getValueAt(row, 0);

        try {
            User existingUser = userDAO.getUserById(userID);
            if (existingUser == null) {
                JOptionPane.showMessageDialog(this, "Utilizator inexistent cu ID: " + userID);
                return;
            }

            // Dialog cu datele vechi pentru admin.
            JTextField cnpField = new JTextField(existingUser.getCnp());
            JTextField numeField = new JTextField(existingUser.getNume());
            JTextField prenumeField = new JTextField(existingUser.getPrenume());
            JTextField phoneField = new JTextField(existingUser.getNrTelefon());
            JTextField emailField = new JTextField(existingUser.getEmail());
            JTextField ibanField = new JTextField(existingUser.getIban());
            JTextField contractField = new JTextField(existingUser.getContractNumber());

            String[] userTypes = {"student", "professor"};
            JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);
            userTypeCombo.setSelectedItem(existingUser.getRole());

            //adresa
            JTextField taraField = new JTextField();
            JTextField judetField = new JTextField();
            JTextField orasField = new JTextField();
            JTextField stradaField = new JTextField();
            JTextField numarField = new JTextField();

            Object[] formFields = {
                    "CNP:", cnpField,
                    "Nume:", numeField,
                    "Prenume:", prenumeField,
                    "Telefon:", phoneField,
                    "Email:", emailField,
                    "IBAN:", ibanField,
                    "ContractNumber:", contractField,
                    "UserType:", userTypeCombo,
                    // Address fields
                    "Tara:", taraField,
                    "Judet:", judetField,
                    "Oras:", orasField,
                    "Strada:", stradaField,
                    "Numar:", numarField
            };

            int option = JOptionPane.showConfirmDialog(
                    this, formFields, "Editează Utilizator", JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION) {
                // 5) update user
                existingUser.setCnp(cnpField.getText().trim());
                existingUser.setNume(numeField.getText().trim());
                existingUser.setPrenume(prenumeField.getText().trim());
                existingUser.setNrTelefon(phoneField.getText().trim());
                existingUser.setEmail(emailField.getText().trim());
                existingUser.setIban(ibanField.getText().trim());
                existingUser.setContractNumber(contractField.getText().trim());
                existingUser.setUserType(userTypeCombo.getSelectedItem().toString());

                int adresaID = existingUser.getAdresaID();

                //updatare user
                userDAO.updateUser(existingUser);

                //  Refresh tabel
                loadUsers("");
                JOptionPane.showMessageDialog(this, "Utilizator actualizat cu succes!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la editare: " + ex.getMessage());
        }
    }

    //ne asiguram ca avem adresa

    public int ensureAddressExists(String tara, String judet, String oras, String strada, String numar) throws SQLException {
        // 1) Verificam daca adresa exista deja
        String checkSql = "SELECT AdresaID FROM Adresa "
                + "WHERE Tara=? AND Judet=? AND Oras=? AND Strada=? AND Numar=? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, tara);
            checkStmt.setString(2, judet);
            checkStmt.setString(3, oras);
            checkStmt.setString(4, strada);
            checkStmt.setString(5, numar);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Daca exita returnam id-ul ei
                    return rs.getInt("AdresaID");
                }
            }
        }

        // 2) Daca nu cream una noua si returnam id ul celei noi
        String insertSql = "INSERT INTO Adresa (Tara, Judet, Oras, Strada, Numar) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, tara);
            insertStmt.setString(2, judet);
            insertStmt.setString(3, oras);
            insertStmt.setString(4, strada);
            insertStmt.setString(5, numar);

            insertStmt.executeUpdate();

            try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // adresa nou inserata
                }
            }
        }
        return -1;
    }

}
