package concurs.repository.jdbc;

import concurs.model.Participant;
import concurs.repository.ParticipantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ParticipantDataBaseRepositoryHibernate implements ParticipantRepository {

    private static final Logger logger= LogManager.getLogger();

    public ParticipantDataBaseRepositoryHibernate() {
        // Initialize Hibernate session factory or any other required setup
    }

    @Override
    public Participant FindByCnp(String cnp) {
        logger.traceEntry("Finding participant with cnp {}", cnp);

        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Participant paricipant = session.createQuery("From Participant where cnp = :cnp", Participant.class)
                    .setParameter("cnp", cnp)
                    .uniqueResult();
            logger.traceExit("Found participant with cnp {}", paricipant);
            return paricipant;
        }
        catch (Exception e) {
            logger.error("Error finding participant with cnp {}: {}", cnp, e.getMessage());
            System.out.println("Error DB " + e);
            return null;
        }
    }

    @Override
    public Participant add(Participant elem) {
       logger.traceEntry("Adding participant {}", elem);
       Transaction tx = null;
       try(Session session = HibernateUtils.getSessionFactory().openSession()){
           tx = session.beginTransaction();
           session.persist(elem);
           tx.commit();
           logger.traceExit("Added participant successfully");
           return FindByCnp(elem.getCnp());
       }
         catch (Exception e) {
              if (tx != null) {
                tx.rollback();
              }
              logger.error("Error adding participant {}: {}", elem, e.getMessage());
              System.out.println("Error DB " + e);
              return null;
         }
    }

    @Override
    public Participant delete(Integer integer) {
        return null;
    }

    @Override
    public Participant update(Integer integer, Participant elem) {
        return null;
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("Finding participant with integer {}", integer);
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Participant participant = session.find(Participant.class, integer);
            logger.traceExit("Found participant with integer {}", participant);
            return participant;
        }
        catch (Exception e) {
            logger.error("Error finding participant with integer {}: {}", integer, e.getMessage());
            System.out.println("Error DB " + e);
            return null;
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry("Finding all participants");
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            List<Participant> data = session.createQuery("from Participant", Participant.class).list();
            logger.traceExit("Found {} participants", data.size());
            return data;
        }
        catch (Exception e) {
            logger.error("Error finding all participants: {}", e.getMessage());
            System.out.println("Error DB " + e);
            return List.of();
        }
    }
}
