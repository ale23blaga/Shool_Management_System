package school.management.system.services;

import school.management.system.dao.EnrollmentDAO;
import school.management.system.models.Enrollment;

import java.util.List;

public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public void enrollStudent(Enrollment e) {
        enrollmentDAO.addEnrollment(e);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.getAllEnrollments();
    }

    public void updateEnrollment(Enrollment e) {
        enrollmentDAO.updateEnrollment(e);
    }

    public void deleteEnrollment(int inscriereID) {
        enrollmentDAO.deleteEnrollment(inscriereID);
    }
}
