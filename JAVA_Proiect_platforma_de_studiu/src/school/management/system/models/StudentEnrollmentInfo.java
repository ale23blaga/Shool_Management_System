package school.management.system.models;

import java.sql.Date;

//Obiect pentru a verifica inscrierea unui student la un curs mai usor
public class StudentEnrollmentInfo {
    private int studentID;
    private String nume;
    private String prenume;
    private Date dataInscriere;

    public StudentEnrollmentInfo(int studentID, String nume, String prenume, Date dataInscriere) {
        this.studentID = studentID;
        this.nume = nume;
        this.prenume = prenume;
        this.dataInscriere = dataInscriere;
    }

    // Getters & Setters
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

    public Date getDataInscriere() {
        return dataInscriere;
    }

    public void setDataInscriere(Date dataInscriere) {
        this.dataInscriere = dataInscriere;
    }
}
