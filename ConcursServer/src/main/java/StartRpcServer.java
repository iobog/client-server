import concurs.repository.InscriereRepository;
import concurs.repository.ParticipantRepository;
import concurs.repository.PersoanaOficiuRepository;
import concurs.repository.ProbaRepository;
import concurs.repository.jdbc.InscriereDataBaseRepository;
import concurs.repository.jdbc.ParticipantDataBaseRepository;
import concurs.repository.jdbc.PersoanaOficiuDataBaseRepository;
import concurs.repository.jdbc.ProbaDataBaseRepository;
import concurs.server.ConcursServerImpl;
import concurs.services.IConcursServices;
import concurs.utils.AbstractServer;
import concurs.utils.ConcursRpcConcurrentServer;
import concurs.utils.ServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class StartRpcServer {
    private static int defaultPort = 55555;
    private static Logger logger = LogManager.getLogger(StartRpcServer.class);
    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try{
            serverProps.load(StartRpcServer.class.getResourceAsStream("concursserver.properties"));
            logger.info("Server properties set {}",serverProps);
            // serverProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find chatserver.properties "+e);
            logger.debug("Looking for file in "+(new File(".")).getAbsolutePath());
            return;
        }
        InscriereRepository inscriereRepository = new InscriereDataBaseRepository(serverProps);
        ParticipantRepository participantRepository = new ParticipantDataBaseRepository(serverProps);
        PersoanaOficiuRepository persoanaOficiuRepository = new PersoanaOficiuDataBaseRepository(serverProps);
        ProbaRepository probaRepository = new ProbaDataBaseRepository(serverProps);

        IConcursServices concursServerImpl = new ConcursServerImpl(inscriereRepository, participantRepository, persoanaOficiuRepository, probaRepository);

        int concursServerPort = defaultPort;
        try{
            concursServerPort = Integer.parseInt(serverProps.getProperty("concurs.server.port"));
        }
        catch(NumberFormatException e){
            logger.error("Wrong port number + "+ e.getMessage());
            logger.debug("using default prot" + defaultPort);
        }
        logger.debug("Starting server on port{}" + concursServerPort);
        AbstractServer server = new ConcursRpcConcurrentServer(concursServerPort,concursServerImpl);
        try{
            server.start();
        }
        catch(ServerException e){
            logger.error("Server start failed"+ e.getMessage());

        }
        finally {
            try{
                server.stop();
            }
            catch(ServerException e){
                logger.error("Server stop failed"+ e.getMessage());
            }
        }
    }
}
