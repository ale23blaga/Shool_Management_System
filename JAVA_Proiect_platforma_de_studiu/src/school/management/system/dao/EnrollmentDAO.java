package school.management.system.dao;

import school.management.system.models.Enrollment;
import school.management.system.models.StudentEnrollmentInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // CREATE
    public void addEnrollment(Enrollment e) {
        // Verificam daca studentul este inscris la curs
        String checkEnrollmentSQL = "SELECT COUNT(*) FROM Inscrieri_curs WHERE StudentID = ? AND CursID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkEnrollmentSQL)) {

            stmt.setInt(1, e.getStudentID()); // Set StudentID
            stmt.setInt(2, e.getCursID());    // Set CursID

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    // Daca studentul este deja inscris
                    throw new IllegalArgumentException("Student is already enrolled in this course.");
                } else {
                    // Daca nu este inscris la curs
                    String insertSQL = "INSERT INTO Inscrieri_curs (InscriereID, StudentID, CursID, Data_Inscriere) "
                            + "VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setInt(1, e.getInscriereID());
                        insertStmt.setInt(2, e.getStudentID());
                        insertStmt.setInt(3, e.getCursID());
                        insertStmt.setDate(4, e.getDataInscriere());
                        insertStmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException("Database error while enrolling student in course.", ex);
                    }

                    // Update student total hours dupa successful enrollment
                    updateStudentTotalHours(conn, e.getStudentID(), e.getCursID());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL errors
            throw new RuntimeException("Database error while checking course enrollment.", ex);
        }
    }

    //update nr de ore pentru student
    private void updateStudentTotalHours(Connection conn, int studentID, int courseID) {
        String getCourseHoursSQL = "SELECT Nr_maxim_studenti FROM Curs WHERE CursID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getCourseHoursSQL)) {
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int courseHours = 4 ;

                String updateStudentHoursSQL = "UPDATE Student SET Nr_ore = Nr_ore + ? WHERE StudentID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateStudentHoursSQL)) {
                    updateStmt.setInt(1, courseHours);
                    updateStmt.setInt(2, studentID);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while updating student total hours.", ex);
        }
    }

    private void updateStudentTotalHoursMinus(Connection conn, int studentID, int courseID) {
        String getCourseHoursSQL = "SELECT Nr_maxim_studenti FROM Curs WHERE CursID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getCourseHoursSQL)) {
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int courseHours = 4 ;

                String updateStudentHoursSQL = "UPDATE Student SET Nr_ore = Nr_ore + ? WHERE StudentID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateStudentHoursSQL)) {
                    updateStmt.setInt(1, courseHours);
                    updateStmt.setInt(2, studentID);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while updating student total hours.", ex);
        }
    }

    // READ ALL
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM Inscrieri_curs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Enrollment(
                        rs.getInt("InscriereID"),
                        rs.getInt("StudentID"),
                        rs.getInt("CursID"),
                        rs.getDate("Data_Inscriere")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updateEnrollment(Enrollment e) {
        String sql = "UPDATE Inscrieri_curs SET StudentID=?, CursID=?, Data_Inscriere=? WHERE InscriereID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getStudentID());
            stmt.setInt(2, e.getCursID());
            stmt.setDate(3, e.getDataInscriere());
            stmt.setInt(4, e.getInscriereID());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // DELETE
    public void deleteEnrollment(int inscriereID) {
        String sql = "DELETE FROM Inscrieri_curs WHERE InscriereID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscriereID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<StudentEnrollmentInfo> getStudentsInCourse(int cursID) {
        List<StudentEnrollmentInfo> list = new ArrayList<>();
        String sql = "SELECT s.StudentID," +
                "       u.Nume, " +
                "       u.Prenume, " +
                "       i.Data_Inscriere " +
                "FROM Inscrieri_curs i " +
                "JOIN Student s ON i.StudentID = s.StudentID " +
                "JOIN Users u ON s.UserID = u.UserID\n" +
                "WHERE i.CursID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cursID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int sid = rs.getInt("StudentID");
                    String nume = rs.getString("Nume");
                    String prenume = rs.getString("Prenume");
                    Date data = rs.getDate("Data_Inscriere");

                    StudentEnrollmentInfo info = new StudentEnrollmentInfo(sid, nume, prenume, data);
                    list.add(info);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public void removeEnrollment(int studentID, int cursID) {
        String sql = "DELETE FROM Inscrieri_curs WHERE StudentID = ? AND CursID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentID);
            stmt.setInt(2, cursID);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No enrollment found for the provided student and course.");
            }
            updateStudentTotalHoursMinus(conn, studentID, cursID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error while unenrolling student from course.", e);
        }
    }

}
