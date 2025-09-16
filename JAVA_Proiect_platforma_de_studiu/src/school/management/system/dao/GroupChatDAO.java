package school.management.system.dao;

import school.management.system.models.GroupMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupChatDAO {

    //recuperare mesajele dintr un anumit grup
    public List<GroupMessage> getMessagesByGroup(int grupID) {
        List<GroupMessage> messages = new ArrayList<>();

        String sql = "SELECT gm.MessageID, gm.GrupID, gm.StudentID, gm.Content, gm.Timestamp, u.Nume, u.Prenume " +
                "FROM GroupMessages gm " +
                "JOIN Student s ON gm.StudentID = s.StudentID " +
                "JOIN Users u ON s.UserID = u.UserID " +
                "WHERE gm.GrupID = ? " +
                "ORDER BY gm.Timestamp ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, grupID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int messageID = rs.getInt("MessageID");
                    int studentID = rs.getInt("StudentID");
                    String content = rs.getString("Content");
                    Timestamp timestamp = rs.getTimestamp("Timestamp");
                    String nume = rs.getString("Nume");
                    String prenume = rs.getString("Prenume");

                    GroupMessage msg = new GroupMessage(messageID, grupID, studentID, content, timestamp, nume, prenume);
                    messages.add(msg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    //Inserare de mesaje noi in chat.
    public void addMessage(int grupID, int studentID, String content) throws SQLException {
        String sql = "INSERT INTO GroupMessages (GrupID, StudentID, Content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grupID);
            stmt.setInt(2, studentID);
            stmt.setString(3, content);
            stmt.executeUpdate();
        }
    }

    public boolean isStudentInGroup(int grupID, int studentID) {
        String sql = "SELECT 1 FROM MembruGrup WHERE GrupID = ? AND StudentID = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grupID);
            stmt.setInt(2, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
