package school.management.system.dao;

import school.management.system.models.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // CREATE
    public void addCourse(Course c) {
        String sql = "INSERT INTO Curs (CursID, Nume_curs, Descriere, Nr_maxim_studenti) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getCursID());
            stmt.setString(2, c.getNumeCurs());
            stmt.setString(3, c.getDescriere());
            stmt.setInt(4, c.getNrMaximStudenti());
            stmt.executeUpdate();

            int professorID = getProfessorIDForCourse(conn, c.getCursID());

            if (professorID != -1) {
                updateTeacherCourseCount(conn, professorID);
            } else {
                System.out.println("No professor assigned to this course.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getProfessorIDForCourse(Connection conn, int cursID) {
        String sql = "SELECT ProfesorID FROM Profesor_Curs WHERE CursID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProfesorID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Metoda pentru a updata nr cursuri la care preda un profesor
    private void updateTeacherCourseCount(Connection conn, int professorID) {
        TeacherDAO teacherDAO = new TeacherDAO();
        String getTotalCoursesSQL = "SELECT Total_cursuri FROM Profesor WHERE ProfesorID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getTotalCoursesSQL)) {
            stmt.setInt(1, professorID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int currentTotalCourses = teacherDAO.getCourseCountByTeacher(professorID);
                currentTotalCourses++;
                String updateCoursesSQL = "UPDATE Profesor SET Total_cursuri = ? WHERE ProfesorID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateCoursesSQL)) {
                    updateStmt.setInt(1, currentTotalCourses);
                    updateStmt.setInt(2, professorID);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Curs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("CursID"),
                        rs.getString("Nume_curs"),
                        rs.getString("Descriere"),
                        rs.getInt("Nr_maxim_studenti")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ by ID
    public Course getCourseById(int cursID) {
        String sql = "SELECT * FROM Curs WHERE CursID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getInt("CursID"),
                        rs.getString("Nume_curs"),
                        rs.getString("Descriere"),
                        rs.getInt("Nr_maxim_studenti")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Course> getCoursesByProfessor(int profID) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Curs\n" +
                "JOIN Profesor_Curs ON Curs.CursID = Profesor_Curs.CursID\n" +
                "WHERE Profesor_Curs.ProfesorID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Course c = extractCourseFromResultSet(rs);
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getCourseNameByProfessor(int profID)
    {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT * FROM profesor_curs WHERE ProfesorID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer c = extractCourseNameFromResultSet(rs);
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updateCourse(Course c) {
        String sql = "UPDATE Curs SET Nume_curs=?, Descriere=?, Nr_maxim_studenti=? WHERE CursID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNumeCurs());
            stmt.setString(2, c.getDescriere());
            stmt.setInt(3, c.getNrMaximStudenti());
            stmt.setInt(4, c.getCursID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteCourse(int cursID) {
        String sql = "DELETE FROM Curs WHERE CursID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        int cursID = rs.getInt("CursID");
        String numeCurs = rs.getString("Nume_curs");
        String descriere = rs.getString("Descriere");
        int nrMaximStudenti = rs.getInt("Nr_maxim_studenti");
        return new Course(cursID, numeCurs, descriere, nrMaximStudenti);
    }
    private int extractCourseNameFromResultSet(ResultSet rs) throws SQLException {
        int cursID = rs.getInt("CursID");
        return cursID;
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Curs "
                + "WHERE CursID LIKE ? OR Nume_curs LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeStr = "%" + keyword + "%";
            stmt.setString(1, likeStr);
            stmt.setString(2, likeStr);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                            rs.getInt("CursID"),
                            rs.getString("Nume_curs"),
                            rs.getString("Descriere"),
                            rs.getInt("Nr_maxim_studenti")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
