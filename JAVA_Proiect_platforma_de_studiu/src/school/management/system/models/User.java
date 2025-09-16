package school.management.system.models;

public class User {
    private int userID;
    private String cnp;
    private String nume;
    private String prenume;
    private int adresaID;
    private String nrTelefon;
    private String email;
    private String iban;
    private String contractNumber;
    private String userType;  // 'student', 'professor', 'administrator', 'super-administrator'
    private String username;
    private String parola;

    public User(int userID, String cnp, String nume, String prenume,
                int adresaID, String nrTelefon, String email, String iban,
                String contractNumber, String userType, String username, String parola) {
        this.userID = userID;
        this.cnp = cnp;
        this.nume = nume;
        this.prenume = prenume;
        this.adresaID = adresaID;
        this.nrTelefon = nrTelefon;
        this.email = email;
        this.iban = iban;
        this.contractNumber = contractNumber;
        this.userType = userType;
        this.username = username;
        this.parola = parola;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
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

    public int getAdresaID() {
        return adresaID;
    }

    public void setAdresaID(int adresaID) {
        this.adresaID = adresaID;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getRole() {
        return userType;
    }
}
