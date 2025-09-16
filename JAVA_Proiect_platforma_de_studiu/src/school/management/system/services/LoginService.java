package school.management.system.services;

import school.management.system.dao.UserDAO;
import school.management.system.models.User;

public class LoginService {
    private UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {
        return userDAO.authenticate(username, password);
    }
}
