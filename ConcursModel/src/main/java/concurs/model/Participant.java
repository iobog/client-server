package concurs.model;

public class Participant extends Entity<Integer>{
    private String cnp;
    private String nume;
    private int varsta;

    public Participant(String cnp,String nume, int varsta) {
        this.cnp = cnp;
        this.nume = nume;
        this.varsta = varsta;
    }

    public Participant(){}

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Participant)) {
            return false;
        }
        Participant participant = (Participant) obj;
        return cnp.equals(participant.cnp) && nume.equals(participant.nume) && varsta == participant.varsta;
    }
}
