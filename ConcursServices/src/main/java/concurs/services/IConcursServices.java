package concurs.services;


import concurs.model.Participant;
import concurs.model.PersoanaOficiu;
import concurs.model.Proba;
import concurs.model.Tuple;


public interface IConcursServices {

    void login(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException;

    Proba[] getProbele() throws ConcursException;

    // Get all participants registered for a specific proba
    // This method should be called by the client to get the list of participants for a specific proba
    // It should return an array of Participant objects
    Participant[] getRegisteredParticipantsForProba(Proba proba) throws ConcursException;

    // if the participant is already registered for the proba, it should throw an exception
    // if the participant is not registered for the proba, i register it for that proba
    // if the participant is not in tha database, i add him, then i register him for the proba
    boolean addParticipantToProba(Integer participant, Integer probaId) throws ConcursException;

    Participant addParticipant(Participant participant) throws ConcursException;

    void logout(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException;
}
