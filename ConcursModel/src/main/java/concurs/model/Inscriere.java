package concurs.model;


public class Inscriere extends Entity<Integer> {

    private int participant_id;
    private int proba_id;
    public Inscriere(int participant, int proba) {
        this.participant_id = participant;
        this.proba_id = proba;
    }

    public Inscriere(){}

    public int getParticipant() {
        return participant_id;
    }

    public int getProba() {
        return proba_id;
    }


    public void setParticipant(int participant_id) {
        this.participant_id = participant_id;
    }

    public void setProba(int proba) {
        this.proba_id = proba;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "participant=" + participant_id +
                ", proba=" + proba_id +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Inscriere)) {
            return false;
        }
        Inscriere inscriere = (Inscriere) obj;
        return participant_id == inscriere.participant_id && proba_id == inscriere.proba_id;
    }

}

//
//package concurs.model;
//
//
//import jakarta.persistence.*;
//import jakarta.persistence.Entity;
//
//@Entity
//@Table(name="Inscrieri")
//public class Inscriere {
//
//    private Integer id;
//    @Column(name= "participant_id")
//    private int participant_id;
//    @Column(name= "proba_id")
//    private int proba_id;
//
//    public Inscriere(int participant, int proba) {
//        this.participant_id = participant;
//        this.proba_id = proba;
//    }
//
//    public Inscriere() {
//    }
//
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Integer getId() {
//        return id;
//    }
//
//    @Column(nullable = false)
//    public int getParticipant() {
//        return participant_id;
//    }
//
//    @Column(nullable = false)
//    public int getProba() {
//        return proba_id;
//    }
//
//    public void setParticipant(int participant_id) {
//        this.participant_id = participant_id;
//    }
//
//    public void setProba(int proba) {
//        this.proba_id = proba;
//    }
//
//    @Override
//    public String toString() {
//        return "Inscriere{" +
//                "participant=" + participant_id +
//                ", proba=" + proba_id +
//                '}';
//    }
//
//}
//
//
