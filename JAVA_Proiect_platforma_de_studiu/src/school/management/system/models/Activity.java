package school.management.system.models;



import school.management.system.dao.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class Activity {
    private int activitateID;  // PK
    private String tipActivitate; // ENUM('CURS','SEMINAR','LABORATOR','EXAMEN', etc.)
    private int profesorID;
    private int cursID;
    private int procentaj;      // e.g., 25
    private Date startDate;
    private Date endDate;
    private String recurenta;   // 'saptamanal', 'lunar', etc.
    private int maxParticipanti;

    public Activity(int activitateID, String tipActivitate, int profesorID, int cursID,
                    int procentaj, Date startDate, Date endDate,
                    String recurenta, int maxParticipanti) {
        this.activitateID = activitateID;
        this.tipActivitate = tipActivitate;
        this.profesorID = profesorID;
        this.cursID = cursID;
        this.procentaj = procentaj;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurenta = recurenta;
        this.maxParticipanti = maxParticipanti;
    }

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

                    // Build your Activity model
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

    public int getActivitateID() {
        return activitateID;
    }

    public void setActivitateID(int activitateID) {
        this.activitateID = activitateID;
    }

    public String getTipActivitate() {
        return tipActivitate;
    }

    public void setTipActivitate(String tipActivitate) {
        this.tipActivitate = tipActivitate;
    }

    public int getProfesorID() {
        return profesorID;
    }

    public void setProfesorID(int profesorID) {
        this.profesorID = profesorID;
    }

    public int getCursID() {
        return cursID;
    }

    public void setCursID(int cursID) {
        this.cursID = cursID;
    }

    public int getProcentaj() {
        return procentaj;
    }

    public void setProcentaj(int procentaj) {
        this.procentaj = procentaj;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRecurenta() {
        return recurenta;
    }

    public void setRecurenta(String recurenta) {
        this.recurenta = recurenta;
    }

    public int getMaxParticipanti() {
        return maxParticipanti;
    }

    public void setMaxParticipanti(int maxParticipanti) {
        this.maxParticipanti = maxParticipanti;
    }
}
