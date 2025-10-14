package concurs.utils;


import concurs.rpcprotocol.ConcursClientRpcReflectionWorker;
import concurs.services.IConcursServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;


public class ConcursRpcConcurrentServer extends AbsConcurrentServer {
    private IConcursServices concursServices;
    private static Logger logger = LogManager.getLogger(ConcursRpcConcurrentServer.class);
    public ConcursRpcConcurrentServer(int port, IConcursServices concursServices) {
        super(port);
        this.concursServices = concursServices;
        logger.info("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ConcursClientRpcReflectionWorker worker=new ConcursClientRpcReflectionWorker(concursServices, client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        logger.info("Stopping services ...");
    }
}
