package school.management.system.services;


import school.management.system.dao.DatabaseConnection;
import school.management.system.dao.StudyGroupDAO;
import school.management.system.dao.MemberGroupDAO;
import school.management.system.models.StudyGroup;
import school.management.system.models.MemberGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.List;

public class StudyGroupService {
    private StudyGroupDAO groupDAO = new StudyGroupDAO();
    private MemberGroupDAO memberDAO = new MemberGroupDAO();

    public void addStudyGroup(StudyGroup g) {
        groupDAO.addStudyGroup(g);
    }

    public void addMemberToGroup(MemberGroup m) {
        // SQL to check if the student is already a member of the study group
        String checkMembershipSql = "SELECT COUNT(*) FROM MembruGrup WHERE StudentID = ? AND GrupID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Check if the student is already a member of the study group
            try (PreparedStatement stmt = conn.prepareStatement(checkMembershipSql)) {
                stmt.setInt(1, m.getStudentID());  // StudentID
                stmt.setInt(2, m.getGrupID());     // GrupID

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int memberCount = rs.getInt(1);
                    if (memberCount > 0) {
                        throw new IllegalArgumentException("Student is already a member of this study group.");
                    }
                }
            }

            // If the student is not already a member, add them to the group
            memberDAO.addMember(m);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error while adding student to study group.", e);
        }
    }


    // Similar read/update/delete methods
}
