package concurs.server;

import concurs.model.*;
import concurs.repository.InscriereRepository;
import concurs.repository.ParticipantRepository;
import concurs.repository.PersoanaOficiuRepository;
import concurs.repository.ProbaRepository;
import concurs.services.ConcursException;
import concurs.services.IConcursOberver;
import concurs.services.IConcursServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConcursServerImpl implements IConcursServices {

    private InscriereRepository inscriereRepository;
    private ParticipantRepository participantRepository;
    private ProbaRepository probaRepository;
    private PersoanaOficiuRepository persoanaOficiuRepository;

    private Map<String,IConcursOberver> loggedPersoaneOficiu;
    private static Logger logger = LogManager.getLogger(ConcursServerImpl.class);
    private final int defaultThreadsNo = 3;

    public ConcursServerImpl(InscriereRepository inscriereRepository, ParticipantRepository participantRepository,
                          PersoanaOficiuRepository persoanaOficiuRepository, ProbaRepository probaRepository) {
        this.inscriereRepository = inscriereRepository;
        this.participantRepository = participantRepository;
        this.persoanaOficiuRepository = persoanaOficiuRepository;
        this.probaRepository = probaRepository;
        this.loggedPersoaneOficiu  = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException {
        PersoanaOficiu persOficiuFromRepo = persoanaOficiuRepository.findByUsernameAndPassword(persoanaOficiu.getUsername(), persoanaOficiu.getParola());
        if (persOficiuFromRepo != null) {
            if(loggedPersoaneOficiu.get(persoanaOficiu.getUsername())!=null){
                throw new ConcursException("Persoana oficiu already logged in");
            }
            loggedPersoaneOficiu.put(persoanaOficiu.getUsername(),client);
        }
        else
            throw new ConcursException("Persoana oficiu already logged in");
    }


    @Override
    public synchronized Proba[] getProbele() throws ConcursException {
        // i use this function only to update once
        //this.updateProbeIntoDataBAse();
        Iterable<Proba> data =  probaRepository.findAll();
        int i=0;
        int size = (int) StreamSupport.stream(data.spliterator(), false).count();
        Proba[] probe = new Proba[size];
        for (Proba proba : data) {
            probe[i] = proba;
            i++;
        }
        return probe;
    }

    @Override
    public synchronized Participant addParticipant(Participant participant) throws ConcursException {
        logger.info("Adding participant {}", participant.toString());
        Participant participantFromDataBase = participantRepository.FindByCnp(participant.getCnp());
        if (participantFromDataBase == null) {
            logger.info("Participant {} not found", participant.getCnp());
            Participant p = participantRepository.add(participant);
            logger.info("Added participant {} to database", p.getCnp());
            return p;
        } else {
            logger.info("Participant {} already exists in the database", participant.getCnp());
            return null;
        }
    }


    @Override
    public synchronized Participant[] getRegisteredParticipantsForProba(Proba proba) throws ConcursException {
        logger.info("Getting registered participants for proba {}", proba.toString());
        Iterable<Inscriere> inscrieri = inscriereRepository.findAll();
        List<Participant> result = new ArrayList<>();

        for (Inscriere inscriere : inscrieri) {
            if (inscriere.getProba() == proba.getId()) {
                Participant participant = participantRepository.findOne(inscriere.getParticipant());
                if (participant != null) {
                    result.add(participant);
                }
            }
        }

        return result.toArray(new Participant[result.size()]);
    }

    @Override
    public synchronized boolean addParticipantToProba(Integer participant, Integer probeId) throws ConcursException {
        logger.info("Adding participant {} to proba {}", participant, probeId.toString());
        List<Inscriere> inscrieri = (List<Inscriere>) inscriereRepository.findAll();
        Set<Integer> probeInscrise = inscrieri.stream()
                .filter(i -> i.getParticipant() == participant)
                .map(Inscriere::getProba)
                .collect(Collectors.toSet());
        boolean added = false;
        if (!probeInscrise.contains(probeId) && probeId != null) {
            Inscriere inscriere1 = new Inscriere(participant, probeId);
            inscriereRepository.add(inscriere1);

            Proba probaFromDb = probaRepository.findOne(probeId);
            probaFromDb.setNumarParticipanti(probaFromDb.getNumarParticipanti() + 1);
            probaRepository.update(probeId,probaFromDb);

            notifyParticipantInscris(inscriere1);
            logger.info("Added and notified inscriere {}", inscriere1.toString());
            added = true;
        }
        if (!added) {
            logger.info("Participant {} is already registered for both probe {}", participant, probeId.toString());
        }
        return added;
    }
    public synchronized void updateProbeIntoDataBAse()
    {
        logger.info("Updating probe into database");
        Iterable<Inscriere> inscrieri = inscriereRepository.findAll();
        List<Proba> probe = (List<Proba>) probaRepository.findAll();
        for (Proba proba : probe) {
            int numarParticipanti = 0;
            for (Inscriere inscriere : inscrieri) {
                if (inscriere.getProba() == proba.getId()) {
                    numarParticipanti++;
                }
            }
            proba.setNumarParticipanti(numarParticipanti);
            probaRepository.update(proba.getId(),proba);
        }
    }




//    @Override
//    public boolean addParticipantToProba(Participant participant, Tuple<Integer, Integer> probeId) throws ConcursException {
//        logger.info("Adding participant {} to proba {}", participant, probeId.toString());
//        // check if participant is already in the database if it is, get his id, else add him ang get his id
//        Participant participantFromDataBase = participantRepository.FindByCnp(participant.getCnp());
//        if (participantFromDataBase == null) {
//            logger.info("Participant {} not found", participant.getCnp());
//            participantFromDataBase = participantRepository.add(participant);
//            logger.info("Added participant {} to database", participantFromDataBase.getCnp());
//            participant.setId(participantFromDataBase.getId());
//        }
//
//        List<Inscriere> inscrierei = (List<Inscriere>) inscriereRepository.findAll();
//        List<Integer> probleLaCareEsteDejaInscris = new ArrayList<>();
//        for(Inscriere inscriere: inscrierei){
//            if(inscriere.getParticipant() == participant.getId())
//            {
//                probleLaCareEsteDejaInscris.add(inscriere.getProba());
//            }
//        }
//
//        boolean added = false;
//
//        // Try to add first proba
//        if (!probeInscrise.contains(probeId.first)) {
//            Inscriere inscriere1 = new Inscriere(participant.getId(), probeId.first);
//            inscriereRepository.add(inscriere1);
//            notifyParticipantInscris(inscriere1);
//            logger.info("Added and notified inscriere {}", inscriere1.toString());
//            added = true;
//        }
//
//        // Try to add second proba
//        if (!probeInscrise.contains(probeId.second)) {
//            Inscriere inscriere2 = new Inscriere(participant.getId(), probeId.second);
//            inscriereRepository.add(inscriere2);
//            notifyParticipantInscris(inscriere2);
//            logger.info("Added and notified inscriere {}", inscriere2.toString());
//            added = true;
//        }
//
//        if (!added) {
//            logger.info("Participant {} is already registered for both probe {}", participant.getCnp(), probeId.toString());
//        }
//
//
//        return false;
//    }

    private void notifyParticipantInscris(Inscriere inscriere) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (IConcursOberver observer : loggedPersoaneOficiu.values()) {
            executor.execute(() -> {
                try {
                    logger.debug("Notifying observer about new registration: " + inscriere);
                    observer.inscriereConcurs(inscriere);
                } catch (ConcursException e) {
                    logger.error("Error notifying observer: " + e.getMessage());
                    logger.error(e.getStackTrace());
                }
            });
        }
        executor.shutdown();
    }

    public synchronized void logout(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException {
        IConcursOberver localClient = loggedPersoaneOficiu.remove(persoanaOficiu.getUsername());
        if (localClient == null) {
            logger.error("Logout failed: client not found");
            throw new ConcursException("Logout failed: client not found");
        }
        logger.info("Logout successful: " + persoanaOficiu.getUsername());

    }
}
