package concurs.repository.jdbc;


import concurs.model.Proba;
import concurs.repository.ProbaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProbaDataBaseRepository implements ProbaRepository {
    private JdbcUtils jbdcUtils;
    private static final Logger logger= LogManager.getLogger();


    public ProbaDataBaseRepository(Properties props){
        this.logger.info("Initializing ProbaDataBaseRepository with properties: {}",props);
        this.jbdcUtils=new JdbcUtils(props);

    }

    @Override
    public Proba add(Proba elem) {
        return null;
    }

    @Override
    public Proba delete(Integer integer) {
        return null;
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
