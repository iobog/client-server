package concurs.repository.jdbc;
import concurs.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
// hibernate utils
public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = createNewSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(PersoanaOficiu.class)
                .addAnnotatedClass(Participant.class)
                .addAnnotatedClass(Proba.class)
                .addAnnotatedClass(Inscriere.class);

        return configuration.buildSessionFactory();
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}