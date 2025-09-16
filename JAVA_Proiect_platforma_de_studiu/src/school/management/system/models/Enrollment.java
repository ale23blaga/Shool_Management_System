package school.management.system.models;

import java.sql.Date;

public class Enrollment {
    private int inscriereID;     // PK
    private int studentID;       // FK
    private int cursID;          // FK
    private Date dataInscriere;

    public Enrollment(int inscriereID, int studentID, int cursID, Date dataInscriere) {
        this.inscriereID = inscriereID;
        this.studentID = studentID;
        this.cursID = cursID;
        this.dataInscriere = dataInscriere;
    }

    public int getInscriereID() {
        return inscriereID;
    }

    public void setInscriereID(int inscriereID) {
        this.inscriereID = inscriereID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getCursID() {
        return cursID;
    }

    public void setCursID(int cursID) {
        this.cursID = cursID;
    }

    public Date getDataInscriere() {
        return dataInscriere;
    }

    public void setDataInscriere(Date dataInscriere) {
        this.dataInscriere = dataInscriere;
    }
}
