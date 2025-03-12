package ro.mpp2024_teme.domeniu;


public class PersoanaOficiu extends Entity<Long>{
    public String oras;
    public String username;
    public String parola;

    public PersoanaOficiu(String oras, String username, String parola) {
        this.oras = oras;
        this.username = username;
        this.parola = parola;
    }

    public String getOras() {
        return oras;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "PersoanaOficiu{" +
                "oras=" + oras +
                ", username='" + username + '\'' +
                ", parola=" + parola +
                '}';
    }
}
