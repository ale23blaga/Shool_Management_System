package school.management.system.modules;

import school.management.system.dao.UserDAO;
import school.management.system.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuperAdminPage extends JFrame {
    private User currentUser;
    private UserDAO userDAO = new UserDAO();
    // Menus
    private JMenuBar menuBar;
    private JMenu menuUsers, menuCourses, menuGroups, menuSystem;
    private JMenuItem passwordItem, manageUsersItem, manageCoursesItem, manageGroupsItem, manageAdminsItem, systemSettingsItem, logoutItem;

    public SuperAdminPage(User user) {
        this.currentUser = user;
        setTitle("Panou Super Administrator - " + user.getNume() + " " + user.getPrenume());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        infoPart();
    }

    private void infoPart() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Admin Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Information Panel (Name, Address)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));

        String name = currentUser.getNume() + " " + currentUser.getPrenume();
        String address = "Address: " + userDAO.getAddress(currentUser.getUserID());

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
        menuGroups = new JMenu("Grupuri de studiu");
        menuSystem = new JMenu("Sistem");

        // Admin-level items (also available to super-admin)
        manageUsersItem = new JMenuItem("Gestionează Utilizatori");
        manageCoursesItem = new JMenuItem("Gestionează Cursuri");
        manageGroupsItem = new JMenuItem("Gestionează Grupuri");

        // Super-admin-only items
        manageAdminsItem = new JMenuItem("Gestionează Administratori");
        systemSettingsItem = new JMenuItem("Setări Sistem");
        passwordItem = new JMenuItem("Schimbare Parola");

        // Logout
        logoutItem = new JMenuItem("Deconectare");

        // Assemble the menu
        menuUsers.add(manageUsersItem);
        menuUsers.add(manageAdminsItem);   // only super admin can manage administrators

        menuCourses.add(manageCoursesItem);
        menuGroups.add(manageGroupsItem);

        menuSystem.add(systemSettingsItem);
        menuSystem.add(passwordItem);
        menuSystem.add(logoutItem);

        menuBar.add(menuUsers);
        menuBar.add(menuCourses);
        menuBar.add(menuGroups);
        menuBar.add(menuSystem);

        setJMenuBar(menuBar);

        manageUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new ManageUsersPanel());
                revalidate();
                repaint();
            }
        });

        manageCoursesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new ManageCoursesPanel());
                revalidate();
                repaint();
            }
        });

        manageGroupsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new ManageGroupsPanel());
                revalidate();
                repaint();
            }
        });

        manageAdminsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new ManageAdminsPanel());
                revalidate();
                repaint();
            }
        });

        systemSettingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SuperAdminPage.this,
                        "Aici veti putea configura setari generale ale sistemului.",
                        "Setări Sistem",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        passwordItem.addActionListener(e->{
            getContentPane().removeAll();
            add(new ChangePasswordPanel(currentUser));
            revalidate();
            repaint();
        });
    }
}
