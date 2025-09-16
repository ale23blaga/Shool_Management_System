package school.management.system.modules;

import school.management.system.models.User;
import school.management.system.services.LoginService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginService loginService;

    public LoginPage() {
        loginService = new LoginService();
        setTitle("Logare");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Nume de utilizator: ");
        userLabel.setBounds(50, 50, 200, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 50, 160, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Parola: ");
        passwordLabel.setBounds(50, 100, 200, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 100, 160, 25);
        add(passwordField);

        loginButton = new JButton("Conectare");
        loginButton.setBounds(200, 150, 120, 25);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String parola = new String(passwordField.getPassword());

                User user = loginService.authenticate(username, parola);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Conectare Reușită");
                    openRolePage(user);
                } else {
                    JOptionPane.showMessageDialog(null, "Nume de utilizator sau parolă incorectă!");
                }
            }
        });
    }

    private void openRolePage(User user) {
        String role = user.getRole().toLowerCase();
        JFrame nextFrame = null;
        switch (role) {
            case "administrator":
                nextFrame = new AdminPage(user);
                break;
            case "super-administrator":
                nextFrame = new SuperAdminPage(user);
                break;
            case "professor":
                nextFrame = new TeacherPage(user);
                break;
            case "student":
                nextFrame = new StudentPage(user);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Rol necunoscut!");
                System.exit(0);
        }

        if (nextFrame != null) {
            nextFrame.setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
