package school.management.system.services;

import school.management.system.dao.CourseDAO;
import school.management.system.models.Course;

import java.util.List;

public class CourseService {
    private CourseDAO courseDAO = new CourseDAO();

    public void addCourse(Course c) {
        courseDAO.addCourse(c);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public Course getCourseById(int id) {
        return courseDAO.getCourseById(id);
    }

    public void updateCourse(Course c) {
        courseDAO.updateCourse(c);
    }

    public void deleteCourse(int id) {
        courseDAO.deleteCourse(id);
    }
}
