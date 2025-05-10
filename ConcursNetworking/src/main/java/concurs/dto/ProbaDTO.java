package concurs.dto;

import java.io.Serializable;

public class ProbaDTO implements Serializable {
    private Integer id;
    private String nume;
    private String categorieVarsta;
    private int numarParticipanti;
    private int participantCount;

    public ProbaDTO() {
    }

    public ProbaDTO(Integer id, String nume, String categorieVarsta, int numarParticipanti, int participantCount) {
        this.id = id;
        this.nume = nume;
        this.categorieVarsta = categorieVarsta;
        this.numarParticipanti = numarParticipanti;
        this.participantCount = participantCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCategorieVarsta() {
        return categorieVarsta;
    }

    public void setCategorieVarsta(String categorieVarsta) {
        this.categorieVarsta = categorieVarsta;
    }

    public int getNumarParticipanti() {
        return numarParticipanti;
    }

    public void setNumarParticipanti(int numarParticipanti) {
        this.numarParticipanti = numarParticipanti;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    @Override
    public String toString() {
        return "ProbaDTO{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", categorieVarsta='" + categorieVarsta + '\'' +
                ", numarParticipanti=" + numarParticipanti +
                ", participantCount=" + participantCount +
                '}';
    }
}
