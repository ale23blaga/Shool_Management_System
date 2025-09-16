package school.management.system.dao;

import school.management.system.models.Teacher;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    // CREATE
    public void addTeacher(Teacher t) {
        String sql = "INSERT INTO Profesor (ProfesorID, UserID, DepartamentID, Nr_min_ore, Nr_maxim_ore, Total_cursuri, Salariu) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getProfesorID());
            stmt.setInt(2, t.getUserID());
            stmt.setInt(3, t.getDepartamentID());
            stmt.setInt(4, t.getNrMinOre());
            stmt.setInt(5, t.getNrMaximOre());
            stmt.setInt(6, t.getTotalCursuri());
            stmt.setBigDecimal(7, t.getSalariu());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM Profesor";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Teacher(
                        rs.getInt("ProfesorID"),
                        rs.getInt("UserID"),
                        rs.getInt("DepartamentID"),
                        rs.getInt("Nr_min_ore"),
                        rs.getInt("Nr_maxim_ore"),
                        rs.getInt("Total_cursuri"),
                        rs.getBigDecimal("Salariu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ by ID
    public Teacher getTeacherById(int profesorID) {
        String sql = "SELECT * FROM Profesor WHERE ProfesorID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profesorID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Teacher(
                        rs.getInt("ProfesorID"),
                        rs.getInt("UserID"),
                        rs.getInt("DepartamentID"),
                        rs.getInt("Nr_min_ore"),
                        rs.getInt("Nr_maxim_ore"),
                        rs.getInt("Total_cursuri"),
                        rs.getBigDecimal("Salariu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Teacher getTeacherByUserID(int userID)
    {
        String sql = "SELECT * FROM Profesor WHERE userID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Teacher(
                        rs.getInt("ProfesorID"),
                        rs.getInt("UserID"),
                        rs.getInt("DepartamentID"),
                        rs.getInt("Nr_min_ore"),
                        rs.getInt("Nr_maxim_ore"),
                        rs.getInt("Total_cursuri"),
                        rs.getBigDecimal("Salariu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public void updateTeacher(Teacher t) {
        String sql = "UPDATE Profesor SET UserID=?, DepartamentID=?, Nr_min_ore=?, Nr_maxim_ore=?, "
                + "Total_cursuri=?, Salariu=? WHERE ProfesorID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getUserID());
            stmt.setInt(2, t.getDepartamentID());
            stmt.setInt(3, t.getNrMinOre());
            stmt.setInt(4, t.getNrMaximOre());
            stmt.setInt(5, t.getTotalCursuri());
            stmt.setBigDecimal(6, t.getSalariu());
            stmt.setInt(7, t.getProfesorID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteTeacher(int profesorID) {
        String sql = "DELETE FROM Profesor WHERE ProfesorID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profesorID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCourseCountByTeacher(int profesorID) {
        int courseCount = 0;
        String query = "SELECT COUNT(*) AS course_count " +
                "FROM Profesor_Curs " +
                "WHERE ProfesorID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, profesorID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    courseCount = rs.getInt("course_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseCount;
    }

}
