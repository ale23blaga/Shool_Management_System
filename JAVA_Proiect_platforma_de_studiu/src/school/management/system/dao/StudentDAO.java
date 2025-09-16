package school.management.system.dao;

import school.management.system.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE
    public void addStudent(Student s) {
        String sql = "INSERT INTO Student (StudentID, UserID, An_studiu, Nr_ore) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, s.getStudentID());
            stmt.setInt(2, s.getUserID());
            stmt.setInt(3, s.getAnStudiu());
            stmt.setInt(4, s.getNrOre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("StudentID"),
                        rs.getInt("UserID"),
                        rs.getInt("An_studiu"),
                        rs.getInt("Nr_ore")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ by StudentID
    public Student getStudentById(int studentID) {
        String sql = "SELECT * FROM Student WHERE StudentID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("StudentID"),
                        rs.getInt("UserID"),
                        rs.getInt("An_studiu"),
                        rs.getInt("Nr_ore")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void updateStudent(Student s) {
        String sql = "UPDATE Student SET UserID=?, An_studiu=?, Nr_ore=? WHERE StudentID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, s.getUserID());
            stmt.setInt(2, s.getAnStudiu());
            stmt.setInt(3, s.getNrOre());
            stmt.setInt(4, s.getStudentID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteStudent(int studentID) {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get adress by user
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

    public int getClassesCountByStudent(int studentID) {
        int classesCount = 0;
        String query = "SELECT COUNT(*) AS class_count " +
                "FROM Inscrieri_curs " +
                "WHERE StudentID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    classesCount = rs.getInt("class_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classesCount;
    }
}
