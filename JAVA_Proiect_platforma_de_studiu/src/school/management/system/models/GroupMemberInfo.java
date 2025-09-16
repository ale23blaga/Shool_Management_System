package school.management.system.models;

public class GroupMemberInfo {
    private int studentID;
    private String nume;
    private String prenume;

    public GroupMemberInfo(int studentID, String nume, String prenume) {
        this.studentID = studentID;
        this.nume = nume;
        this.prenume = prenume;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
}
