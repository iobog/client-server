package concurs.repository.jdbc;

import concurs.model.PersoanaOficiu;
import concurs.repository.PersoanaOficiuRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PersoanaOficiuDataBaseRepository implements PersoanaOficiuRepository {
    private JdbcUtils jbdcUtils;
    private static final Logger logger= LogManager.getLogger();

    public PersoanaOficiuDataBaseRepository(Properties jbdcUtils) {

        this.jbdcUtils = new JdbcUtils(jbdcUtils);
    }

    @Override
    public PersoanaOficiu add(PersoanaOficiu elem) {
        logger.traceEntry("Adding persoana oficiu {}", elem);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("insert into PersoanaOficiu(oras, username, password) values (?,?,?)")) {

                preStmt.setString(1, elem.getOras());
                preStmt.setString(2, elem.getUsername());
                preStmt.setString(3, elem.getParola());
                preStmt.executeUpdate();

            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Persoana oficiu added successfully");
        return elem;
    }

    @Override
    public PersoanaOficiu delete(Integer integer) {
        return null;
    }

    @Override
    public PersoanaOficiu update(Integer integer, PersoanaOficiu elem) {
        return null;
    }

    @Override
    public PersoanaOficiu findOne(Integer integer) {
        logger.traceEntry("Finding persoana oficiu with id {} ", integer);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from PersoanaOficiu where id=?")) {
                preStmt.setInt(1, integer);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        int id = result.getInt("id");
                        String oras = result.getString("oras");
                        String username = result.getString("username");
                        String password = result.getString("password");
                        PersoanaOficiu persoanaOficiu = new PersoanaOficiu(oras, username, password);
                        persoanaOficiu.setId(id);
                        return persoanaOficiu;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Persoana oficiu found successfully");
        return null;
    }

    @Override
    public Iterable<PersoanaOficiu> findAll() {

        logger.traceEntry("Finding all persoane oficiu");
        List<PersoanaOficiu> persoanaOficiuList= new ArrayList<>();

        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from PersoanaOficiu")) {
                try (var result = preStmt.executeQuery()) {
                    while (result.next()) {
                        int id = result.getInt("id");
                        String oras = result.getString("oras");
                        String username = result.getString("username");
                        String password = result.getString("password");
                        PersoanaOficiu persoanaOficiu = new PersoanaOficiu(oras, username, password);
                        persoanaOficiu.setId(id);
                        persoanaOficiuList.add(persoanaOficiu);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Persoane oficiu found successfully");
        return persoanaOficiuList;

    }


    @Override
    public PersoanaOficiu findByUsernameAndPassword(String username, String password) {
        logger.traceEntry("Finding persoana oficiu with username {} and password {}", username, password);
        try {
            var con = jbdcUtils.getConnection();
            try (var preStmt = con.prepareStatement("select * from PersoanaOficiu where username=? and parola=?")) {
                preStmt.setString(1, username);
                preStmt.setString(2, password);
                try (var result = preStmt.executeQuery()) {
                    if (result.next()) {
                        int id = result.getInt("id");
                        String oras = result.getString("oras");
                        PersoanaOficiu persoanaOficiu = new PersoanaOficiu(oras, username, password);
                        persoanaOficiu.setId(id);
                        return persoanaOficiu;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit("Persoana oficiu found successfully");
        return null;
    }
}
