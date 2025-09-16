package school.management.system.models;

public class StudyGroup {
    private int grupID;        // PK
    private int cursID;        //  FK
    private int minParticipanti;
    private int nrMembri; // filed pentru a stii cati membri sunt si de cati mai e nevoie

    public StudyGroup(int grupID, int cursID, int minParticipanti) {
        this.grupID = grupID;
        this.cursID = cursID;
        this.minParticipanti = minParticipanti;
    }

    public StudyGroup(int grupID, int cursID, int minParticipanti, int nrMembri) {
        this.grupID = grupID;
        this.cursID = cursID;
        this.minParticipanti = minParticipanti;
        this.nrMembri = nrMembri;
    }

    public int getGrupID() {
        return grupID;
    }

    public void setGrupID(int grupID) {
        this.grupID = grupID;
    }

    public int getCursID() {
        return cursID;
    }

    public void setCursID(int cursID) {
        this.cursID = cursID;
    }

    public int getMinParticipanti() {
        return minParticipanti;
    }

    public void setMinParticipanti(int minParticipanti) {
        this.minParticipanti = minParticipanti;
    }

    public int getNrMembri() {
        return nrMembri;
    }

    public void setNrMembri(int nrMembri) {
        this.nrMembri = nrMembri;
    }
}


