package school.management.system.dao;

import school.management.system.models.Activity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    // CREATE
    public void addActivity(Activity a) {
        String sql = "INSERT INTO Activitati_Curs (ActivitateID, Tip_activitate, ProfesorID, CursID, "
                + "Procentaj, StartDate, EndDate, Recurenta, max_participanti) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getActivitateID());
            stmt.setString(2, a.getTipActivitate());
            stmt.setInt(3, a.getProfesorID());
            stmt.setInt(4, a.getCursID());
            stmt.setInt(5, a.getProcentaj());
            stmt.setDate(6, a.getStartDate());
            stmt.setDate(7, a.getEndDate());
            stmt.setString(8, a.getRecurenta());
            stmt.setInt(9, a.getMaxParticipanti());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<Activity> getAllActivities() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM Activitati_Curs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Activity(
                        rs.getInt("ActivitateID"),
                        rs.getString("Tip_activitate"),
                        rs.getInt("ProfesorID"),
                        rs.getInt("CursID"),
                        rs.getInt("Procentaj"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Recurenta"),
                        rs.getInt("max_participanti")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public void updateActivity(Activity a) {
        String sql = "UPDATE Activitati_Curs SET Tip_activitate=?, ProfesorID=?, CursID=?, Procentaj=?, "
                + "StartDate=?, EndDate=?, Recurenta=?, max_participanti=? WHERE ActivitateID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getTipActivitate());
            stmt.setInt(2, a.getProfesorID());
            stmt.setInt(3, a.getCursID());
            stmt.setInt(4, a.getProcentaj());
            stmt.setDate(5, a.getStartDate());
            stmt.setDate(6, a.getEndDate());
            stmt.setString(7, a.getRecurenta());
            stmt.setInt(8, a.getMaxParticipanti());
            stmt.setInt(9, a.getActivitateID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteActivity(int activitateID) {
        String sql = "DELETE FROM Activitati_Curs WHERE ActivitateID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activitateID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Activitatile studentilor
    public List<Activity> getActivitiesForStudent(int studentID) {
        List<Activity> activities = new ArrayList<>();

        String sql = "SELECT a.* "
                + "FROM Activitati_Curs a "
                + "JOIN Inscrieri_curs i ON i.CursID = a.CursID "
                + "WHERE i.StudentID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int activitateID = rs.getInt("ActivitateID");
                    String tipActivitate = rs.getString("Tip_activitate");
                    int profesorID = rs.getInt("ProfesorID");
                    int cursID = rs.getInt("CursID");
                    int procentaj = rs.getInt("Procentaj");
                    Date startDate = rs.getDate("StartDate");
                    Date endDate = rs.getDate("EndDate");
                    String recurenta = rs.getString("Recurenta");
                    int maxParticipanti = rs.getInt("max_participanti");

                    Activity activity = new Activity(
                            activitateID,
                            tipActivitate,
                            profesorID,
                            cursID,
                            procentaj,
                            startDate,
                            endDate,
                            recurenta,
                            maxParticipanti
                    );
                    activities.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    //activitatile profesorului
    public List<Activity> getActivitiesByProfessor(int profesorID) {
        List<Activity> activityList = new ArrayList<>();

        String sql = "SELECT * FROM Activitati_Curs WHERE ProfesorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, profesorID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Activity a = extractActivityFromResultSet(rs);
                    activityList.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activityList;
    }

    //activitati dupa id
    public Activity getActivityById( int activitateID){
        String sql = "SELECT * FROM Activitati_Curs WHERE ActivitateID = ?";
        Activity a = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, activitateID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                     a = extractActivityFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    //extract la activitati
    private Activity extractActivityFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ActivitateID");
        int profesorID = rs.getInt("ProfesorID");
        int cursID = rs.getInt("CursID");
        String tip = rs.getString("Tip_activitate");
        int procentaj = rs.getInt("Procentaj");
        Date startDate = rs.getDate("StartDate");
        Date endDate = rs.getDate("EndDate");
        String recurenta = rs.getString("Recurenta");
        int maxPart = rs.getInt("max_participanti");

        return new Activity(id, tip, profesorID, cursID, procentaj, startDate, endDate, recurenta, maxPart);
    }
}
