package school.management.system.services;

import school.management.system.dao.TeacherDAO;
import school.management.system.models.Teacher;
import java.util.List;

public class TeacherService {
    private TeacherDAO teacherDAO = new TeacherDAO();

    public void addTeacher(Teacher t) {
        teacherDAO.addTeacher(t);
    }

    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    public Teacher getTeacherById(int id) {
        return teacherDAO.getTeacherById(id);
    }

    public void updateTeacher(Teacher t) {
        teacherDAO.updateTeacher(t);
    }

    public void deleteTeacher(int id) {
        teacherDAO.deleteTeacher(id);
    }
}
