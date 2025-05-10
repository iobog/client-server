package concurs.dto;
import java.io.Serializable;

public class PersoanaOficiuDTO implements Serializable {
    private Integer id;
    private String oras;
    private String username;
    private String parola;

    public PersoanaOficiuDTO() {
    }

    public PersoanaOficiuDTO(Integer id, String oras, String username, String parola) {
        this.id = id;
        this.oras = oras;
        this.username = username;
        this.parola = parola;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
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

    @Override
    public String toString() {
        return "PersoanaOficiuDTO{" +
                "id=" + id +
                ", oras='" + oras + '\'' +
                ", username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}

