package school.management.system.models;

import java.sql.Timestamp;

//Mesaju din chat pentru un grup
public class GroupMessage {
    private int messageID;
    private int grupID;
    private int studentID;
    private String content;
    private Timestamp timestamp;
    private String nume;
    private String prenume;

    public GroupMessage(int messageID, int grupID, int studentID, String content, Timestamp timestamp, String nume, String prenume) {
        this.messageID = messageID;
        this.grupID = grupID;
        this.studentID = studentID;
        this.content = content;
        this.timestamp = timestamp;
        this.nume = nume;
        this.prenume = prenume;
    }

    // Getters and setters
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getGrupID() {
        return grupID;
    }

    public void setGrupID(int grupID) {
        this.grupID = grupID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
