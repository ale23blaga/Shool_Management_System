package school.management.system.dao;

import school.management.system.models.GroupMemberInfo;
import school.management.system.models.StudyGroup;
import school.management.system.models.MemberGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyGroupDAO {

    // CREATE
    public void addStudyGroup(StudyGroup g) {
        String sql = "INSERT INTO Grup_Studiu (GrupID, CursID, min_participanti) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, g.getGrupID());
            stmt.setInt(2, g.getCursID());
            stmt.setInt(3, g.getMinParticipanti());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ...
    // UPDATE ...
    // DELETE ...

    public StudyGroup getGroupById(int grupID) {
        StudyGroup group = null;
        String sql = "SELECT * FROM Grup_Studiu WHERE GrupID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, grupID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int cursID = rs.getInt("CursID");
                int minPart = rs.getInt("min_participanti");
                group = new StudyGroup(grupID, cursID, minPart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return group;
    }

    /**
     * READ ALL: Retrieves all study groups from the Grup_Studiu table.
     *
     * @return a List of all StudyGroup objects.
     */
    public List<StudyGroup> getAllGroups() {
        List<StudyGroup> groupList = new ArrayList<>();
        String sql = "SELECT * FROM Grup_Studiu";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int grupID = rs.getInt("GrupID");
                int cursID = rs.getInt("CursID");
                int minPart = rs.getInt("min_participanti");

                StudyGroup sg = new StudyGroup(grupID, cursID, minPart);
                groupList.add(sg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupList;
    }

    /**
     * UPDATE: Updates an existing StudyGroup in the Grup_Studiu table.
     */
    public void updateStudyGroup(StudyGroup group) {
        String sql = "UPDATE Grup_Studiu SET CursID = ?, min_participanti = ? WHERE GrupID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, group.getCursID());
            stmt.setInt(2, group.getMinParticipanti());
            stmt.setInt(3, group.getGrupID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * DELETE: Deletes a StudyGroup from the Grup_Studiu table by GrupID.
     */
    public void deleteStudyGroup(int grupID) {
        String sql = "DELETE FROM Grup_Studiu WHERE GrupID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, grupID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StudyGroup> getGroupsForStudent(int studentID) {
        List<StudyGroup> groups = new ArrayList<>();

        // We'll do a join + subquery to get membership count
        // Example:
        // SELECT g.GrupID, g.CursID, g.min_participanti,
        // (SELECT COUNT(*) FROM MembruGrup mg2 WHERE mg2.GrupID = g.GrupID) AS nrMembri
        // FROM MembruGrup mg
        // JOIN Grup_Studiu g ON mg.GrupID = g.GrupID
        // WHERE mg.StudentID = ?

        String sql = "SELECT g.GrupID, g.CursID, g.min_participanti, "
                + " (SELECT COUNT(*) FROM MembruGrup mg2 WHERE mg2.GrupID = g.GrupID) AS nrMembri "
                + "FROM MembruGrup mg "
                + "JOIN Grup_Studiu g ON mg.GrupID = g.GrupID "
                + "WHERE mg.StudentID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int grupID = rs.getInt("GrupID");
                    int cursID = rs.getInt("CursID");
                    int minPart = rs.getInt("min_participanti");
                    int nrMembri = rs.getInt("nrMembri");

                    StudyGroup sg = new StudyGroup(grupID, cursID, minPart, nrMembri);
                    groups.add(sg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public List<GroupMemberInfo> getMembersByGroup(int grupID) {
        List<GroupMemberInfo> list = new ArrayList<>();

        // Example schema:
        // MembruGrup(GrupID, StudentID)
        // Student(StudentID, UserID)
        // Users(UserID, Nume, Prenume)
        // We want (StudentID, Nume, Prenume).

        String sql = "SELECT s.StudentID, u.Nume, u.Prenume " +
                "FROM MembruGrup mg " +
                "JOIN Student s ON mg.StudentID = s.StudentID " +
                "JOIN Users u ON s.UserID = u.UserID " +
                "WHERE mg.GrupID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, grupID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int stID = rs.getInt("StudentID");
                    String nume = rs.getString("Nume");
                    String prenume = rs.getString("Prenume");

                    GroupMemberInfo info = new GroupMemberInfo(stID, nume, prenume);
                    list.add(info);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}