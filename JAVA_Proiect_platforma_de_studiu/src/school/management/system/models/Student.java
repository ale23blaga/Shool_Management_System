package school.management.system.models;

public class Student {
    private int studentID; // PK in Student table
    private int userID;    // FK to Users
    private int anStudiu;
    private int nrOre;

    public Student(int studentID, int userID, int anStudiu, int nrOre) {
        this.studentID = studentID;
        this.userID = userID;
        this.anStudiu = anStudiu;
        this.nrOre = nrOre;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAnStudiu() {
        return anStudiu;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    public int getNrOre() {
        return nrOre;
    }

    public void setNrOre(int nrOre) {
        this.nrOre = nrOre;
    }
}
