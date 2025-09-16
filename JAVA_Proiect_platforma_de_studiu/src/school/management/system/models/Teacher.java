package school.management.system.models;

import java.math.BigDecimal;

public class Teacher {
    private int profesorID;   // PK
    private int userID;       // FK to Users
    private int departamentID;
    private int nrMinOre;
    private int nrMaximOre;
    private int totalCursuri;
    private BigDecimal salariu;

    public Teacher(int profesorID, int userID, int departamentID,
                   int nrMinOre, int nrMaximOre, int totalCursuri, BigDecimal salariu) {
        this.profesorID = profesorID;
        this.userID = userID;
        this.departamentID = departamentID;
        this.nrMinOre = nrMinOre;
        this.nrMaximOre = nrMaximOre;
        this.totalCursuri = totalCursuri;
        this.salariu = salariu;
    }

    public int getProfesorID() {
        return profesorID;
    }

    public void setProfesorID(int profesorID) {
        this.profesorID = profesorID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDepartamentID() {
        return departamentID;
    }

    public void setDepartamentID(int departamentID) {
        this.departamentID = departamentID;
    }

    public int getNrMinOre() {
        return nrMinOre;
    }

    public void setNrMinOre(int nrMinOre) {
        this.nrMinOre = nrMinOre;
    }

    public int getNrMaximOre() {
        return nrMaximOre;
    }

    public void setNrMaximOre(int nrMaximOre) {
        this.nrMaximOre = nrMaximOre;
    }

    public int getTotalCursuri() {
        return totalCursuri;
    }

    public void setTotalCursuri(int totalCursuri) {
        this.totalCursuri = totalCursuri;
    }

    public BigDecimal getSalariu() {
        return salariu;
    }

    public void setSalariu(BigDecimal salariu) {
        this.salariu = salariu;
    }
}
