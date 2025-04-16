package concurs.dto;
import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private Integer id;
    private String cnp;
    private String nume;
    private int varsta;

    public ParticipantDTO() {
    }

    public ParticipantDTO(Integer id, String cnp, String nume, int varsta) {
        this.id = id;
        this.cnp = cnp;
        this.nume = nume;
        this.varsta = varsta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return "ParticipantDTO{" +
                "id=" + id +
                ", cnp='" + cnp + '\'' +
                ", nume='" + nume + '\'' +
                ", varsta=" + varsta +
                '}';
    }
}
