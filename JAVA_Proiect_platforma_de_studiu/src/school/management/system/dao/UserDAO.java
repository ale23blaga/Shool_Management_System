package school.management.system.dao;

import school.management.system.models.Teacher;
import school.management.system.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // CREATE
    public void addUser(User user) {
        String sql = "INSERT INTO Users "
                + "(CNP, Nume, Prenume, AdresaID, NrTelefon, Email, IBAN, "
                + " ContractNumber, UserType, username, parola) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Ensure the username is unique
        String uniqueUsername = user.getUsername();
        int suffix = 1;

        // Check if the username already exists in the Users table
        while (doesUsernameExist(uniqueUsername)) {
            // If the username exists, append a suffix to it
            uniqueUsername = user.getUsername() + suffix;
            suffix++;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Use the unique username
            stmt.setString(1, user.getCnp());
            stmt.setString(2, user.getNume());
            stmt.setString(3, user.getPrenume());
            stmt.setInt(4, user.getAdresaID());
            stmt.setString(5, user.getNrTelefon());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getIban());
            stmt.setString(8, user.getContractNumber());
            stmt.setString(9, user.getRole());
            stmt.setString(10, uniqueUsername);  // Use the unique username
            stmt.setString(11, user.getParola());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean doesUsernameExist(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // If count > 0, username exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // READ ALL
    public ResultSet getAllUsers() {
        String sql = "SELECT * FROM Users";
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
            // You might want to parse into a List<User> for convenience
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // READ: Search by ID
    public User getUserById(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("CNP"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getInt("AdresaID"),
                        rs.getString("NrTelefon"),
                        rs.getString("Email"),
                        rs.getString("IBAN"),
                        rs.getString("ContractNumber"),
                        rs.getString("UserType"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById2(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("CNP"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getInt("AdresaID"),
                        rs.getString("NrTelefon"),
                        rs.getString("Email"),
                        rs.getString("IBAN"),
                        rs.getString("ContractNumber"),
                        rs.getString("UserType"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void updateUser(User user) {
        String sql = "UPDATE Users SET CNP=?, Nume=?, Prenume=?, AdresaID=?, NrTelefon=?, "
                + "Email=?, IBAN=?, ContractNumber=?, UserType=?, username=?, parola=? "
                + "WHERE UserID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getCnp());
            stmt.setString(2, user.getNume());
            stmt.setString(3, user.getPrenume());
            stmt.setInt(4, user.getAdresaID());
            stmt.setString(5, user.getNrTelefon());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getIban());
            stmt.setString(8, user.getContractNumber());
            stmt.setString(9, user.getRole());
            stmt.setString(10, user.getUsername());
            stmt.setString(11, user.getParola());
            stmt.setInt(12, user.getUserID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // AUTHENTICATE: user by username + parola
    public User authenticate(String username, String parola) {
        String sql = "SELECT * FROM Users WHERE username=? AND parola=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, parola);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("CNP"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getInt("AdresaID"),
                        rs.getString("NrTelefon"),
                        rs.getString("Email"),
                        rs.getString("IBAN"),
                        rs.getString("ContractNumber"),
                        rs.getString("UserType"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAdminsAsList() {
        List<User> adminList = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE UserType IN ('administrator','super-administrator')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("UserID"),
                        rs.getString("CNP"),
                        rs.getString("Nume"),
                        rs.getString("Prenume"),
                        rs.getInt("AdresaID"),
                        rs.getString("NrTelefon"),
                        rs.getString("Email"),
                        rs.getString("IBAN"),
                        rs.getString("ContractNumber"),
                        rs.getString("UserType"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
                adminList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    public ResultSet getAdmins() throws SQLException {
        String sql = "SELECT * FROM Users WHERE UserType IN ('administrator','super-administrator')";
        Connection conn = DatabaseConnection.getConnection();

        // We use TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE if we want to possibly update
        // data. If just reading, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY is often enough.
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
        );

        // Execute query and return the ResultSet.
        // IMPORTANT: The calling method (e.g., ManageAdminsPanel) will be responsible
        // for closing this ResultSet (and statement) after using it.
        return stmt.executeQuery(sql);
    }

    public void updatePassword(int userID, String newPass) throws SQLException {
        String sql = "UPDATE Users SET parola=? WHERE UserID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPass);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username=? AND parola=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // if we get a row, credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAddress(int userID) {
        String address = "";
        String query = "SELECT CONCAT(a.Strada, ' ', a.Numar, ', ', a.Oras, ', ', a.Judet, ', ', a.Tara) AS address " +
                "FROM Adresa a " +
                "JOIN Users u ON u.AdresaID = a.AdresaID " +
                "WHERE u.UserID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    address = rs.getString("address");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }


}
