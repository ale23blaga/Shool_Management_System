package school.management.system.models;

import school.management.system.dao.DatabaseConnection;

import java.sql.*;

public class MemberGroup {
    private int idMembru;   // auto_increment PK
    private int grupID;     // FK
    private int studentID;  // FK

    public MemberGroup(int idMembru, int grupID, int studentID) {
        this.idMembru = idMembru;
        this.grupID = grupID;
        this.studentID = studentID;
    }

    public void addMemberToGroup(int grupID, int studentID) throws SQLException {
        String sql = "INSERT INTO MembruGrup (GrupID, StudentID) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grupID);
            stmt.setInt(2, studentID);
            stmt.executeUpdate();
        }
    }

    public int getIdMembru() {
        return idMembru;
    }

    public void setIdMembru(int idMembru) {
        this.idMembru = idMembru;
    }

    public int getGrupID() {
        return grupID;
    }

    public void setGrupID(int grupID) {
        this.grupID = grupID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
}