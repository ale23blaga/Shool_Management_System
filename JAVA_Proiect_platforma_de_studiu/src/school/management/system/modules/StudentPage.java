package school.management.system.modules;

import school.management.system.dao.StudentDAO;
import school.management.system.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentPage extends JFrame {
    private User currentUser;
    private StudentDAO studentDAO;

    // Menu components
    private JMenuBar menuBar;
    private JMenu menuCourses, menuGroups, menuActivities, menuAccount, menuGroupChat;
    private JMenuItem enrollCourseItem, viewGradesItem, groupsItem, currentActivitiesItem, logoutItem, changePassItem, studentGroupItem, groupMembersItem, groupChatItem;

    public StudentPage(User user) {
        this.currentUser = user;
        this.studentDAO = new StudentDAO();
        setTitle("Panou Student - " + user.getNume() + " " + user.getPrenume());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();
        infoPart();
    }

    private void infoPart() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Information Panel (Name, Address, Numarul Cursurilor)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 1, 1));

        String name = currentUser.getNume() + " " + currentUser.getPrenume();
        String address = " " + studentDAO.getAddress(currentUser.getUserID());
        String numClasses = "" + studentDAO.getClassesCountByStudent(currentUser.getUserID());

        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(name));

        infoPanel.add(new JLabel("Adresa:"));
        infoPanel.add(new JLabel(address));

        infoPanel.add(new JLabel("Numar Cursuri:"));
        infoPanel.add(new JLabel(numClasses));

        add(infoPanel, BorderLayout.CENTER);

    }

    private void initMenu() {
        menuBar = new JMenuBar();

        // Top-level menus
        menuCourses = new JMenu("Cursuri");
        menuGroups = new JMenu("Grupuri de studiu");
        menuActivities = new JMenu("Activități");
        menuAccount = new JMenu("Cont");
        menuGroupChat = new JMenu("Grupuri");
        // Menu items
        changePassItem = new JMenuItem("Schimbă Parola");
        enrollCourseItem = new JMenuItem("Înscriere la Curs");
        viewGradesItem = new JMenuItem("Vizualizare Note");
        groupsItem = new JMenuItem("Vizualizeaza Grupuri");
        currentActivitiesItem = new JMenuItem("Activitățile Mele");
        logoutItem = new JMenuItem("Deconectare");
        studentGroupItem = new JMenuItem("Grupurile Mele");
        groupMembersItem = new JMenuItem("Cauta Membri in Grupuri");
        groupChatItem = new JMenuItem("Conversatii Grupuri");

        // Assemble
        menuCourses.add(enrollCourseItem);
        menuCourses.add(viewGradesItem);
        menuGroups.add(groupsItem);
        menuGroups.add(studentGroupItem);
        menuGroups.add(groupMembersItem);
        menuActivities.add(currentActivitiesItem);
        menuAccount.add(changePassItem);
        menuAccount.add(logoutItem);
        menuGroupChat.add(groupChatItem);

        menuBar.add(menuCourses);
        menuBar.add(menuGroups);
        menuBar.add(menuActivities);
        menuBar.add(menuGroupChat);
        menuBar.add(menuAccount);

        setJMenuBar(menuBar);

        // Action Listeners
        enrollCourseItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new EnrollCoursePanel(currentUser));
                revalidate();
                repaint();
            }
        });

        viewGradesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new ViewGradesPanel(currentUser));
                revalidate();
                repaint();
            }
        });

        groupsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new GroupsPanel(currentUser));
                revalidate();
                repaint();
            }
        });

        currentActivitiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new CurrentActivitiesPanel(currentUser));
                revalidate();
                repaint();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });


        changePassItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new ChangePasswordPanel(currentUser));
            revalidate();
            repaint();
        });

        studentGroupItem.addActionListener(e-> {
            getContentPane().removeAll();
            add(new StudentGroupsPanel(currentUser));
            revalidate();
            repaint();
        });

        groupMembersItem.addActionListener(e-> {
            getContentPane().removeAll();
            add(new GroupMembersPanel());
            revalidate();
            repaint();
        });

        groupChatItem.addActionListener(e-> {
            getContentPane().removeAll();
            add(new GroupChatPanel(currentUser));
            revalidate();
            repaint();
        });
    }
}
