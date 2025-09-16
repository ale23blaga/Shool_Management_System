package school.management.system.modules;

import school.management.system.dao.UserDAO;
import school.management.system.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame {
    private User currentUser;
    private JMenuBar menuBar;
    private JMenu menuUsers, menuCourses, menuGroups, menuOther;
    private JMenuItem manageUsersItem, manageCoursesItem, manageGroupsItem, logoutItem, passwordItem;
    private UserDAO userDAO = new UserDAO();

    public AdminPage(User user) {
        this.currentUser = user;
        setTitle("Panou Administrator");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();

        infoPart();
    }

    private void infoPart() {
        setLayout(new BorderLayout());

        // Titlu
        JLabel titleLabel = new JLabel("Admin Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Information Panel (Name, Adresa)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));  //doua coloane cu spatiu intre ele

        //Facem rost de informatie din user si adminDOA
        String name = currentUser.getNume() + " " + currentUser.getPrenume();
        String address = " " + userDAO.getAddress(currentUser.getUserID());


        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(name));

        infoPanel.add(new JLabel("Address:"));
        infoPanel.add(new JLabel(address));

        add(infoPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        menuBar = new JMenuBar();

        menuUsers = new JMenu("Utilizatori");
        menuCourses = new JMenu("Cursuri");
        menuGroups = new JMenu("Grupuri");
        menuOther = new JMenu("Altele");

        manageUsersItem = new JMenuItem("Gestionează Utilizatori");
        manageCoursesItem = new JMenuItem("Gestionează Cursuri");
        manageGroupsItem = new JMenuItem("Gestionează Grupuri");
        logoutItem = new JMenuItem("Logout");
        passwordItem = new JMenuItem("Schimbare Parola");

        menuUsers.add(manageUsersItem);
        menuCourses.add(manageCoursesItem);
        menuGroups.add(manageGroupsItem);
        menuOther.add(passwordItem);
        menuOther.add(logoutItem);

        menuBar.add(menuUsers);
        menuBar.add(menuCourses);
        menuBar.add(menuGroups);
        menuBar.add(menuOther);

        setJMenuBar(menuBar);

        manageUsersItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new ManageUsersPanel());
            revalidate();
            repaint();
        });

        manageCoursesItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new ManageCoursesPanel());
            revalidate();
            repaint();
        });

        manageGroupsItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new ManageGroupsPanel());
            revalidate();
            repaint();
        });

        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        passwordItem.addActionListener(e-> {
           getContentPane().removeAll();
           add(new ChangePasswordPanel(currentUser));
           revalidate();
           repaint();
        });
    }
}
