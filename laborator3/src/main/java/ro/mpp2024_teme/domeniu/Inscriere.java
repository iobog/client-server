package ro.mpp2024_teme.domeniu;


public class Inscriere extends Entity<Long> {
    private Participant participant;
    private Proba proba;

    public Inscriere(Participant participant, Proba proba) {
        this.participant = participant;
        this.proba = proba;
    }


    public Participant getParticipant() {
        return participant;
    }

    public Proba getProba() {
        return proba;
    }


    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "participant=" + participant +
                ", proba=" + proba +
                '}';
    }

}


