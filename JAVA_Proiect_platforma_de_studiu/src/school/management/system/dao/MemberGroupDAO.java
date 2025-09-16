package school.management.system.dao;

import school.management.system.models.StudyGroup;
import school.management.system.models.MemberGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberGroupDAO {

    // CREATE
    public void addMember(MemberGroup m) {
        String sql = "INSERT INTO MembruGrup (GrupID, StudentID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getGrupID());
            stmt.setInt(2, m.getStudentID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMemberGroup(MemberGroup member) throws SQLException {
        String sql = "INSERT INTO MembruGrup (GrupID, StudentID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, member.getGrupID());
            stmt.setInt(2, member.getStudentID());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    member.setIdMembru(rs.getInt(1));
                }
            }
        }
    }

    //Returnam o lista cu toti membri grupului ca MemberGroup objects.
    public List<MemberGroup> getAllMemberGroups() throws SQLException {
        List<MemberGroup> list = new ArrayList<>();
        String sql = "SELECT * FROM MembruGrup";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idMembru = rs.getInt("ID_Membru");
                int grupID = rs.getInt("GrupID");
                int studentID = rs.getInt("StudentID");
                MemberGroup mg = new MemberGroup(idMembru, grupID, studentID);
                list.add(mg);
            }
        }
        return list;
    }

    //Luam toate grupurile in care cineva e membru
    public MemberGroup getMemberGroupById(int idMembru) throws SQLException {
        MemberGroup mg = null;
        String sql = "SELECT * FROM MembruGrup WHERE ID_Membru = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMembru);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int grupID = rs.getInt("GrupID");
                    int studentID = rs.getInt("StudentID");
                    mg = new MemberGroup(idMembru, grupID, studentID);
                }
            }
        }
        return mg;
    }

    //Update in membru grup in functie de id_membru
    public void updateMemberGroup(MemberGroup member) throws SQLException {
        String sql = "UPDATE MembruGrup SET GrupID = ?, StudentID = ? WHERE ID_Membru = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getGrupID());
            stmt.setInt(2, member.getStudentID());
            stmt.setInt(3, member.getIdMembru());

            stmt.executeUpdate();
        }
    }

    //Sterge membru grup in functie de idMembru
    public void deleteMemberGroup(int idMembru) throws SQLException {
        String sql = "DELETE FROM MembruGrup WHERE ID_Membru = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMembru);
            stmt.executeUpdate();
        }
    }



    //Adaugarea unui Membru in grup - stergere record
    public void addMemberToGroup(int grupID, int studentID) throws SQLException {
        String sql = "INSERT INTO MembruGrup (GrupID, StudentID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grupID);
            stmt.setInt(2, studentID);
            stmt.executeUpdate();
        }
    }

    //Inlaturarea unui membru dintr un grup - stergere record
    public void removeMemberFromGroup(int grupID, int studentID) throws SQLException {
        String sql = "DELETE FROM MembruGrup WHERE GrupID = ? AND StudentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grupID);
            stmt.setInt(2, studentID);
            stmt.executeUpdate();
        }
    }


}