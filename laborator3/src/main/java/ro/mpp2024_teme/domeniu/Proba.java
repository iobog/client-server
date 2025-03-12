package ro.mpp2024_teme.domeniu;


public class Proba extends  Entity<Long> {
    public String nume;
    public String categorieVarsta;
    public int numarParticipanti;

    public Proba(String nume, String categorieVarsta, int numarParticipanti) {
        this.nume = nume;
        this.categorieVarsta = categorieVarsta;
        this.numarParticipanti = numarParticipanti;
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

    @Override
    public String toString() {
        return "Proba{" +
                "nume=" + nume +
                ", categorieVarsta='" + categorieVarsta + '\'' +
                ", numarParticipanti=" + numarParticipanti +
                '}';
    }
}

