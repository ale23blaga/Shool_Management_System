package school.management.system.services;

import school.management.system.dao.StudentDAO;
import school.management.system.models.Student;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAO();

    public void addStudent(Student s) {
        studentDAO.addStudent(s);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    public void updateStudent(Student s) {
        studentDAO.updateStudent(s);
    }

    public void deleteStudent(int id) {
        studentDAO.deleteStudent(id);
    }
}
