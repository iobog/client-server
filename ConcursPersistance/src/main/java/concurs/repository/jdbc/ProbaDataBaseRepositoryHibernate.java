//package concurs.repository.jdbc;
//
//import concurs.model.Proba;
//import concurs.repository.ProbaRepository;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class ProbaDataBaseRepositoryHibernate implements ProbaRepository {
//
//  private static final Logger logger = LogManager.getLogger(ProbaDataBaseRepositoryHibernate.class);
//
//  public ProbaDataBaseRepositoryHibernate() {
//    logger.info("Initializing ProbaDataBaseRepositoryHibernate");
//  }
//
//  @Override
//  public Proba add(Proba elem) {
//    logger.traceEntry("Adding proba: {}", elem);
//    Transaction tx = null;
//    try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//      tx = session.beginTransaction();
//      session.persist(elem);
//      tx.commit();
//      logger.info("Proba added successfully: {}", elem);
//      return elem;
//    } catch (Exception e) {
//      if (tx != null) tx.rollback();
//      logger.error("Error adding proba", e);
//      throw new RuntimeException("Failed to add Proba", e);
//    }
//  }
//
//  @Override
//  public Proba delete(Integer id) {
//    logger.traceEntry("Deleting proba with id: {}", id);
//    Transaction tx = null;
//    try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//      Proba proba = session.find(Proba.class, id);
//      if (proba == null) {
//        logger.warn("Proba with id {} not found", id);
//        return null;
//      }
//      tx = session.beginTransaction();
//      session.remove(proba);
//      tx.commit();
//      logger.info("Proba deleted successfully: {}", proba);
//      return proba;
//    } catch (Exception e) {
//      if (tx != null) tx.rollback();
//      logger.error("Error deleting proba", e);
//      throw new RuntimeException("Failed to delete Proba", e);
//    }
//  }
//
//  @Override
//  public Proba update(Integer id, Proba elem) {
//    logger.traceEntry("Updating proba with id: {}", id);
//    Transaction tx = null;
//    try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//      Proba existing = session.find(Proba.class, id);
//      if (existing == null) {
//        logger.warn("Proba with id {} not found", id);
//        return null;
//      }
//
//      tx = session.beginTransaction();
//      existing.setNume(elem.getNume());
//      existing.setCategorieVarsta(elem.getCategorieVarsta());
//      existing.setNumarParticipanti(elem.getNumarParticipanti());
//      session.merge(existing);
//      tx.commit();
//      logger.info("Proba updated successfully: {}", existing);
//      return existing;
//    } catch (Exception e) {
//      if (tx != null) tx.rollback();
//      logger.error("Error updating proba", e);
//      throw new RuntimeException("Failed to update Proba", e);
//    }
//  }
//
//  @Override
//  public Proba findOne(Integer id) {
//    logger.traceEntry("Finding proba with id: {}", id);
//    try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//      Proba proba = session.find(Proba.class, id);
//      if (proba == null) {
//        logger.warn("Proba not found with id: {}", id);
//      }
//      return proba;
//    } catch (Exception e) {
//      logger.error("Error finding proba", e);
//      throw new RuntimeException("Failed to find Proba", e);
//    }
//  }
//
//  @Override
//  public Iterable<Proba> findAll() {
//    logger.trace("Finding all probas");
//    try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//      List<Proba> results = session.createQuery("from Proba", Proba.class).getResultList();
//      logger.info("Found {} probas", results.size());
//      return results;
//    } catch (Exception e) {
//      logger.error("Error retrieving all probas", e);
//      throw new RuntimeException("Failed to retrieve all Proba", e);
//    }
//  }
//}
