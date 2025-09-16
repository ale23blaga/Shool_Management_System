package school.management.system.modules;

import school.management.system.dao.TeacherDAO;
import school.management.system.dao.UserDAO;
import school.management.system.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherPage extends JFrame {
    private User currentUser;
    private JMenuBar menuBar;
    private JMenu menuActivities,  menuCatalog, menuAccount;
    private JMenuItem manageActivitiesItem, viewAllGradesItem, catalogItem, logoutItem, changePassItem, viewStudentsItem, gradesForCourseItem;
    private JMenuItem allCourses, allActivitiesDownload;
    private TeacherDAO teacherDAO = new TeacherDAO();
    private UserDAO userDAO = new UserDAO();

    public TeacherPage(User user) {
        this.currentUser = user;
        setTitle("Panou Profesor - " + user.getNume() + " " + user.getPrenume());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();

        infoPart();

    }

    private void infoPart() {

        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Profesor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));

        String name = currentUser.getNume() + " " + currentUser.getPrenume();
        String address = " " + userDAO.getAddress(currentUser.getUserID());
        String numCourses = " " + teacherDAO.getCourseCountByTeacher(currentUser.getUserID());

        infoPanel.add(new JLabel("Nume:"));
        infoPanel.add(new JLabel(name));

        infoPanel.add(new JLabel("Adresa:"));
        infoPanel.add(new JLabel(address));

        infoPanel.add(new JLabel("Numarul de cursuri:"));
        infoPanel.add(new JLabel(numCourses));

        add(infoPanel, BorderLayout.CENTER);
    }


    private void initMenu() {
        menuBar = new JMenuBar();

        menuActivities = new JMenu("Activități si cursuri");
        menuCatalog = new JMenu("Catalog");
        menuAccount = new JMenu("Cont");

        changePassItem = new JMenuItem("Schimbă Parola");
        manageActivitiesItem = new JMenuItem("Gestionează Activități");
        viewAllGradesItem = new JMenuItem("Vezi Toate Notele");
        catalogItem = new JMenuItem("Catalog Studenți");
        logoutItem = new JMenuItem("Deconectare");
        viewStudentsItem = new JMenuItem("Vezi Studenți la Curs");
        gradesForCourseItem = new JMenuItem("Catalog Curs");
        allCourses = new JMenuItem("Toate Cursurile");
        allActivitiesDownload = new JMenuItem("Activitatile Mele");

        menuActivities.add(allCourses);
        menuActivities.add(manageActivitiesItem);
        menuActivities.add(allActivitiesDownload);
        //menuCatalog.add(viewAllGradesItem);
        menuCatalog.add(catalogItem);
        menuCatalog.add(viewStudentsItem);
        menuCatalog.add(gradesForCourseItem);
        menuAccount.add(changePassItem);
        menuAccount.add(logoutItem);

        menuBar.add(menuActivities);
        menuBar.add(menuCatalog);
        menuBar.add(menuAccount);

        setJMenuBar(menuBar);

        // Action Listeners
        allCourses.addActionListener(e->{
            getContentPane().removeAll();
            add(new AllCoursesPanel());
            revalidate();
            repaint();
        });

        manageActivitiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new TeacherActivitiesPanel(currentUser));
                add(new ManageTeacherActivitiesPanel(currentUser));
                revalidate();
                repaint();
            }
        });

        catalogItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                add(new TeacherGradesPanel(currentUser));
                //add(new CatalogPanel(currentUser));
                revalidate();
                repaint();
            }
        });

        allActivitiesDownload.addActionListener(e->{
            getContentPane().removeAll();
            add(new TeacherActivitiesPanel(currentUser));
            revalidate();
            repaint();
        });

        logoutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        viewAllGradesItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new TeacherGradesPanel(currentUser));
            revalidate();
            repaint();
        });

        changePassItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new ChangePasswordPanel(currentUser));
            revalidate();
            repaint();
        });

        //viewStudents in course
        viewStudentsItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new StudentsInCoursePanel(currentUser));
            revalidate();
            repaint();
        });

        gradesForCourseItem.addActionListener(e -> {
            getContentPane().removeAll();
            add(new GradesForCoursePanel(currentUser));
            revalidate();
            repaint();
        });
    }
}
