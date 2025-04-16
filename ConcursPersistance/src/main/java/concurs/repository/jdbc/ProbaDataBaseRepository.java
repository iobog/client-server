package concurs.repository.jdbc;


import concurs.model.Proba;
import concurs.repository.ProbaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ProbaDataBaseRepository implements ProbaRepository {
    private final JdbcUtils jbdcUtils;
    private static final Logger logger= LogManager.getLogger();


    public ProbaDataBaseRepository(@Qualifier("props")Properties props){
        this.logger.info("Initializing ProbaDataBaseRepository with properties: {}",props);
        this.jbdcUtils=new JdbcUtils(props);

    }
    @Autowired
    public ProbaDataBaseRepository(
            @Value("${jdbc.driver}") String driver,
            @Value("${jdbc.url}") String url,
            @Value("${jdbc.user}") String user,
            @Value("${jdbc.pass}") String pass) {
        Properties props = new Properties();
        props.setProperty("jdbc.driver", driver);
        props.setProperty("jdbc.url", url);
        props.setProperty("jdbc.user", user);
        props.setProperty("jdbc.pass", pass);

        logger.info("Initializing ProbaDataBaseRepository with properties: {}", props);
        jbdcUtils = new JdbcUtils(props);
    }


    @Override
    public Proba add(Proba elem) {
        logger.traceEntry("Adding proba {}", elem);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement(
                    "INSERT INTO Proba (nume, categorie_varsta, numar_participanti) VALUES (?, ?, ?)")) {
                preStmt.setString(1, elem.getNume());
                preStmt.setString(2, elem.getCategorieVarsta());
                preStmt.setInt(3, elem.getNumarParticipanti());

                int affectedRows = preStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new RuntimeException("Adding proba failed, no rows affected.");
                }

                // Get the last inserted ID using SQLite's specific function
                try (var idStmt = con.createStatement();
                     var rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        elem.setId(id);
                        return elem;
                    } else {
                        throw new RuntimeException("Adding proba failed, no ID obtained.");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error adding proba", e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    @Override
    public Proba delete(Integer id) {
        Connection con = null;
        PreparedStatement preStmt = null;

        try {
            con = jbdcUtils.getConnection();
            con.setAutoCommit(true); // sau false, dacă vrei control manual al tranzacției

            preStmt = con.prepareStatement("DELETE FROM Proba WHERE id = ?");
            preStmt.setInt(1, id);
            int rowsAffected = preStmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("No Proba found with id {}", id);
            } else {
                logger.info("Deleted Proba with id {}", id);
            }

            return null; // poți returna o entitate ștearsă, dacă o obții înainte
        } catch (SQLException e) {
            logger.error("Error deleting Proba with id " + id, e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } finally {
            try {
                if (preStmt != null) preStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                logger.warn("Failed to close resources", e);
            }
        }
    }


    @Override
    public Proba update(Integer integer, Proba elem) {
        logger.traceEntry("Updating proba with id {} ",integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("update Proba set nume=?,categorie_varsta=?,numar_participanti=? where id=?")) {
                preStmt.setString(1, elem.getNume());
                preStmt.setString(2, elem.getCategorieVarsta());
                preStmt.setInt(3, elem.getNumarParticipanti());
                preStmt.setInt(4, integer);
                preStmt.executeUpdate();
                //daca se efectueaza cu succes;
                return findOne(integer);
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
      return elem;
    }

    @Override
    public Proba findOne(Integer integer) {
        logger.traceEntry("Finding proba with id {} ",integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Proba where id=?")) {
                preStmt.setInt(1, integer);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        String nume = result.getString("nume");
                        String categorieVarsta = result.getString("categorie_varsta");
                        int numarParticipanti = result.getInt("numar_participanti");

                        Proba proba = new Proba(nume, categorieVarsta, numarParticipanti);
                        proba.setId(integer);
                        return proba;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Proba gasita cu succes");
        return null;
    }

    @Override
    public Iterable<Proba> findAll() {
        logger.traceEntry("Finding all probas");
        List<Proba> data= new ArrayList<>();
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Proba")) {
                try (var result = preStmt.executeQuery()) {
                    while (result.next()) {
                        int id = result.getInt("id");
                        String nume = result.getString("nume");
                        String categorieVarsta = result.getString("categorie_varsta");
                        int numarParticipanti = result.getInt("numar_participanti");

                        Proba proba = new Proba(nume, categorieVarsta, numarParticipanti);
                        proba.setId(id);
                        data.add(proba);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Proba gasita cu succes");
        return data;
    }
}
