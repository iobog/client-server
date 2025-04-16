package concurs.dto;

import java.io.Serializable;

public class InscriereDTO implements Serializable {
    private Integer id;
    private int participantId;
    private int probaId;

    public InscriereDTO() {
    }

    public InscriereDTO(Integer id, int participantId, int probaId) {
        this.id = id;
        this.participantId = participantId;
        this.probaId = probaId;
    }

    public InscriereDTO(Integer participant, Integer first) {
        this.participantId = participant;
        this.probaId = first;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getProbaId() {
        return probaId;
    }

    public void setProbaId(int probaId) {
        this.probaId = probaId;
    }

    @Override
    public String toString() {
        return "InscriereDTO{" +
                "id=" + id +
                ", participantId=" + participantId +
                ", probaId=" + probaId +
                '}';
    }
}
