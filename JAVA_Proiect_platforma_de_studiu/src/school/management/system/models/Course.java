package school.management.system.models;

public class Course {
    private int cursID;  // PK
    private String numeCurs;
    private String descriere;
    private int nrMaximStudenti;

    public Course(int cursID, String numeCurs, String descriere, int nrMaximStudenti) {
        this.cursID = cursID;
        this.numeCurs = numeCurs;
        this.descriere = descriere;
        this.nrMaximStudenti = nrMaximStudenti;
    }

    public int getCursID() {
        return cursID;
    }

    public void setCursID(int cursID) {
        this.cursID = cursID;
    }

    public String getNumeCurs() {
        return numeCurs;
    }

    public void setNumeCurs(String numeCurs) {
        this.numeCurs = numeCurs;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getNrMaximStudenti() {
        return nrMaximStudenti;
    }

    public void setNrMaximStudenti(int nrMaximStudenti) {
        this.nrMaximStudenti = nrMaximStudenti;
    }
}
