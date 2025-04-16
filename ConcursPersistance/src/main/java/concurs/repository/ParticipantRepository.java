package concurs.repository;


import concurs.model.Participant;

public interface ParticipantRepository extends Repository<Integer, Participant> {
    Participant FindByCnp(String cnp);
}