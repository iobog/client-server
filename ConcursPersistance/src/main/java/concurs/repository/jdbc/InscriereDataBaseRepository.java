package concurs.repository.jdbc;


import concurs.model.Inscriere;
import concurs.repository.InscriereRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class InscriereDataBaseRepository implements InscriereRepository {
    private JdbcUtils jbdcUtils;
    private static final Logger logger= LogManager.getLogger();

    public InscriereDataBaseRepository(Properties jbdcUtils) {
        this.jbdcUtils = new JdbcUtils(jbdcUtils);
    }

    @Override
    public Inscriere add(Inscriere elem) {

        logger.traceEntry("Adding inscriere {}", elem);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("insert into Inscriere(participant_id, proba_id) values (?,?)")) {
                preStmt.setInt(1, elem.getParticipant());
                preStmt.setInt(2, elem.getProba());
                preStmt.executeUpdate();
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Inscriere added successfully");
        return elem;
    }

    @Override
    public Inscriere delete(Integer integer) {
        return null;
    }

    @Override
    public Inscriere update(Integer integer, Inscriere elem) {

        return null;
    }

    @Override
    public Inscriere findOne(Integer integer) {

        logger.traceEntry("Finding inscriere with id {}", integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Inscriere where id=?")) {
                preStmt.setInt(1, integer);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        int id = result.getInt("id");
                        int participant = result.getInt("participant_id");
                        int proba = result.getInt("proba_id");
                        Inscriere inscriere = new Inscriere(participant, proba);
                        inscriere.setId(id);
                        return inscriere;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Inscriere found successfully");
        return null;

    }

    @Override
    public Iterable<Inscriere> findAll() {
        logger.traceEntry("Finding all inscrieri");
        List<Inscriere> data= new ArrayList<>();
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Inscriere")) {
                try (var result = preStmt.executeQuery()) {
                    while (result.next()) {
                        int id = result.getInt("id");
                        int participant = result.getInt("participant_id");
                        int proba = result.getInt("proba_id");
                        Inscriere inscriere = new Inscriere(participant, proba);
                        inscriere.setId(id);
                        data.add(inscriere);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }

        logger.traceExit("Inscrieri found successfully");
        return data;
    }
}
