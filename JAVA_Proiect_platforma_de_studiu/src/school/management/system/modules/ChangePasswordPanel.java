package school.management.system.modules;

import school.management.system.dao.UserDAO;
import school.management.system.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Un panel pentru orice utilizator
public class ChangePasswordPanel extends JPanel {

    private User currentUser;       // User ul care este conectat
    private UserDAO userDAO;        // Operatii pe bd

    // UI Components
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton changeButton;

    public ChangePasswordPanel(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel oldPassLabel = new JLabel("Parola Curentă:");
        JLabel newPassLabel = new JLabel("Parola Nouă:");
        JLabel confirmPassLabel = new JLabel("Confirmare Parolă:");

        oldPasswordField = new JPasswordField(15);
        newPasswordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        changeButton = new JButton("Schimbă Parola");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(oldPassLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(oldPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(newPassLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(confirmPassLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(confirmPasswordField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.CENTER;
        add(changeButton, gbc);

        // Action
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangePassword();
            }
        });
    }

    //Verificat parola veche, ne asiguram ca parola noua e aceeasi cu confirmarea. Apleam bd sa schimbam parola
    private void handleChangePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        //1. verificam daca parola vhece este corecta
        if (!userDAO.validateUser(currentUser.getUsername(), oldPass)) {
            JOptionPane.showMessageDialog(this,
                    "Parola veche este incorectă!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2) Verificam daca parola noua este aceeasi cu confirmarea
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                    "Parolele noi nu coincid!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Parola nouă nu poate fi goală!",
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            userDAO.updatePassword(currentUser.getUserID(), newPass);
            JOptionPane.showMessageDialog(this,
                    "Parola a fost schimbată cu succes!");

            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Eroare la actualizarea parolei: " + ex.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
