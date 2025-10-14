package concurs.services;


import concurs.model.Participant;
import concurs.model.PersoanaOficiu;
import concurs.model.Proba;
import concurs.model.Tuple;


public interface IConcursServices {

    void login(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException;

    Proba[] getProbele() throws ConcursException;

    Participant[] getRegisteredParticipantsForProba(Proba proba) throws ConcursException;

    boolean addParticipantToProba(Integer participant, Integer probaId) throws ConcursException;

    Participant addParticipant(Participant participant) throws ConcursException;

    void logout(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException;
}
