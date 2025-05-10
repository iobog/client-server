package concurs.repository.jdbc;


import concurs.model.Participant;
import concurs.repository.ParticipantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDataBaseRepository implements ParticipantRepository {
    private JdbcUtils jbdcUtils;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantDataBaseRepository(Properties jbdcUtils) {
        this.jbdcUtils = new JdbcUtils(jbdcUtils);
    }

    @Override
    public Participant FindByCnp(String cnp) {
        logger.traceEntry("Finding participant with cnp {}", cnp);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Participant where cnp=?")) {
                preStmt.setString(1, cnp);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        int id = result.getInt("id");
                        String nume = result.getString("nume");
                        String cnp1 = result.getString("cnp");
                        int varsta = result.getInt("varsta");

                        Participant participant = new Participant(nume, cnp1, varsta);
                        participant.setId( id);
                        return participant;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Participant found successfully");
        return null;
    }

    @Override
    public Participant add(Participant elem) {
        logger.traceEntry("saving participant {} ",elem);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("insert into Participant (cnp, nume, varsta) values (?,?,?)")) {
                preStmt.setString(1, elem.getCnp());
                preStmt.setString(2, elem.getNume());
                preStmt.setInt(3, elem.getVarsta());
                preStmt.executeUpdate();

                return FindByCnp(elem.getCnp());
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Participant saved successfully");
        return null;
    }
    @Override
    public Participant delete(Integer integer) {

        logger.traceEntry("deleting participant with id {} ",integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("delete from Participant where id=?")) {
                preStmt.setInt(1, integer);
                preStmt.executeUpdate();
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Participant deleted successfully");
        return null;

    }
    @Override
    public Participant update(Integer integer, Participant elem) {
        return null;
    }

    @Override
    public Participant findOne(Integer integer) {

        logger.traceEntry("Finding participant with id {} ", integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Participant where id=?")) {
                preStmt.setInt(1, integer);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        int id = result.getInt("id");
                        String nume = result.getString("nume");
                        String cnp = result.getString("cnp");
                        int varsta = result.getInt("varsta");

                        Participant participant = new Participant(nume, cnp, varsta);
                        participant.setId( id);
                        return participant;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Participant found successfully");
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {

        logger.traceEntry("Finding all participants");
        List<Participant> data= new ArrayList<>();
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from Participant")) {
                try (var result = preStmt.executeQuery()) {
                    while (result.next()) {
                        int id = result.getInt("id");
                        String nume = result.getString("nume");
                        String cnp = result.getString("cnp");
                        int varsta = result.getInt("varsta");

                        Participant participant = new Participant(cnp, nume, varsta);
                        participant.setId( id);
                        data.add(participant);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Participants found successfully");
        return data;
    }
}
