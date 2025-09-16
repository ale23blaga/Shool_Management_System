package school.management.system.services;

import school.management.system.dao.ActivityDAO;
import school.management.system.models.Activity;

import java.util.List;

public class ActivityService {
    private ActivityDAO activityDAO = new ActivityDAO();

    public void addActivity(Activity a) {
        activityDAO.addActivity(a);
    }

    public List<Activity> getAllActivities() {
        return activityDAO.getAllActivities();
    }

    public void updateActivity(Activity a) {
        activityDAO.updateActivity(a);
    }

    public void deleteActivity(int id) {
        activityDAO.deleteActivity(id);
    }
}
