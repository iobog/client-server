package concurs.model;



public class Proba extends  Entity<Integer> {
    public String nume;
    public String categorieVarsta;
    public int numarParticipanti;
    private int participantCount;

    public Proba(String nume, String categorieVarsta, int numarParticipanti) {
        this.nume = nume;
        this.categorieVarsta = categorieVarsta;
        this.numarParticipanti = numarParticipanti;
    }

    public Proba(String nume, int numarParticipanti) {
        this.nume = nume;
        this.numarParticipanti = numarParticipanti;
    }

    public Proba(int numarParticipanti){
        this.numarParticipanti = numarParticipanti;
    }

    public Proba(CompetitionType competitionType, String ageCat) {
        super();
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

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public int getParticipantCount() {
        return participantCount;
    }


}

