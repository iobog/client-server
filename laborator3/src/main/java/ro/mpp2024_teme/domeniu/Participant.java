package ro.mpp2024_teme.domeniu;


public class Participant extends Entity<Long>{
    private String cnp;
    private String nume;
    private int varsta;

    public Participant(String cnp,String nume, int varsta) {
        this.cnp = cnp;
        this.nume = nume;
        this.varsta = varsta;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }
    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "cnp=" + cnp +
                ", nume='" + nume + '\'' +
                ", varsta=" + varsta +
                '}';
    }
}
