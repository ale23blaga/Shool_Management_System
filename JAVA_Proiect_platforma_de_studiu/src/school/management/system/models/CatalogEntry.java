package school.management.system.models;

import java.sql.Date;

public class CatalogEntry {
    private int intrareID;    // PK
    private int profesorID;
    private int studentID;
    private int cursID;
    private int nota;
    private int procentajID;  // references Activity (ActivitateID)
    private boolean prezenta; // default 1
    private Date dataA;

    public CatalogEntry(int intrareID, int profesorID, int studentID,
                        int cursID, int nota, int procentajID,
                        boolean prezenta, Date dataA) {
        this.intrareID = intrareID;
        this.profesorID = profesorID;
        this.studentID = studentID;
        this.cursID = cursID;
        this.nota = nota;
        this.procentajID = procentajID;
        this.prezenta = prezenta;
        this.dataA = dataA;
    }

    public int getIntrareID() {
        return intrareID;
    }

    public void setIntrareID(int intrareID) {
        this.intrareID = intrareID;
    }

    public int getProfesorID() {
        return profesorID;
    }

    public void setProfesorID(int profesorID) {
        this.profesorID = profesorID;
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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getProcentajID() {
        return procentajID;
    }

    public void setProcentajID(int procentajID) {
        this.procentajID = procentajID;
    }

    public boolean isPrezenta() {
        return prezenta;
    }

    public void setPrezenta(boolean prezenta) {
        this.prezenta = prezenta;
    }

    public Date getDataA() {
        return dataA;
    }

    public void setDataA(Date dataA) {
        this.dataA = dataA;
    }
}
