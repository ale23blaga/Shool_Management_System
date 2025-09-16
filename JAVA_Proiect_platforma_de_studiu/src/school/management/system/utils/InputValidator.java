package school.management.system.utils;

import java.math.BigDecimal;
import java.sql.Date;

public class InputValidator {


    public static boolean isValidStudentInput(int studentID, int userID, int anStudiu, int nrOre) {
        if (studentID <= 0) return false;
        if (userID <= 0) return false;
        if (anStudiu < 1 || anStudiu > 10) return false;
        if (nrOre < 0) return false;
        return true;
    }

    public static boolean isValidTeacherInput(int profesorID, int userID,
                                              int nrMinOre, int nrMaximOre,
                                              int totalCursuri, BigDecimal salariu) {
        if (profesorID <= 0) return false;
        if (userID <= 0) return false;
        if (nrMinOre < 0 || nrMaximOre < nrMinOre) return false;
        if (totalCursuri < 0) return false;
        if (salariu.compareTo(BigDecimal.ZERO) < 0) return false;
        return true;
    }

    public static boolean isValidCourseInput(int cursID, String numeCurs, int nrMaximStudenti) {
        if (cursID <= 0) return false;
        if (numeCurs == null || numeCurs.isEmpty()) return false;
        if (nrMaximStudenti <= 0) return false;
        return true;
    }

    public static boolean isValidEnrollmentInput(int inscriereID, int studentID, int cursID, Date dataInscriere) {
        if (inscriereID <= 0) return false;
        if (studentID <= 0) return false;
        if (cursID <= 0) return false;
        if (dataInscriere == null) return false;
        return true;
    }

    public static boolean isValidActivityInput(int activitateID, String tipActivitate,
                                               int profesorID, int cursID,
                                               int procentaj, Date startDate,
                                               Date endDate, int maxParticipanti) {
        if (activitateID <= 0) return false;
        if (tipActivitate == null || tipActivitate.isEmpty()) return false;
        if (profesorID <= 0 || cursID <= 0) return false;
        if (procentaj < 0 || procentaj > 100) return false;
        if (startDate == null || endDate == null || endDate.before(startDate)) return false;
        if (maxParticipanti < 1) return false;
        return true;
    }

}
