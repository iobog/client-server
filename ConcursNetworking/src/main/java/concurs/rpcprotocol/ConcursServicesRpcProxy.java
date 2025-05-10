package concurs.rpcprotocol;

import concurs.dto.*;
import concurs.model.*;
import concurs.services.ConcursException;
import concurs.services.IConcursOberver;
import concurs.services.IConcursServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcursServicesRpcProxy implements IConcursServices {
    private String host;
    private int port;
    private static Logger logger = LogManager.getLogger(ConcursServicesRpcProxy.class);

    private IConcursOberver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ConcursServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    private void startReader(){
        Thread tw  =  new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() throws ConcursException {
        try{
            connection = new Socket(this.host, this.port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            System.out.println("Connection established");
            System.out.println("input: " + input);
            finished = false;
            startReader();
        }
        catch (IOException e)
        {
            logger.error("Error initializing connection to server: " + e);
            logger.error(e.getStackTrace());
        }
    }

    private void sendRequest(Request request) throws ConcursException {
        logger.debug("Sending request: " + request);
        try{
            output.writeObject(request);
            output.flush();
        }
        catch (IOException e)
        {
            throw new ConcursException("error sending object" + e);
        }
    }

    public void login(PersoanaOficiu persoanaOficiu, IConcursOberver clinet) throws ConcursException {
        initializeConnection();
        PersoanaOficiuDTO udto = UtilsDTO.toDTO(persoanaOficiu);
        Request req = new Request.Builder()
                .type(RequestType.LOGIN)
                .data(udto)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = clinet;
            logger.info("Login successful");
            return;
        }
        if(response.type() == ResponseType.ERROR) {
            logger.error("Login failed");
            String err = response.data().toString();
            closeConnection();

            throw new ConcursException(err);
        }
    }

    private Response readResponse() throws ConcursException {
        Response response = null;
        try{
            response = qresponses.take();
        }
        catch (InterruptedException e){
            logger.error("Error reading response: " + e);
            logger.error(e.getStackTrace());
        }
        return response;
    }

    @Override
    public Proba[] getProbele() throws ConcursException {
        Request req = new Request.Builder()
                .type(RequestType.GET_PROBE)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConcursException(err);
        }
        ProbaDTO[] probe = (ProbaDTO[]) response.data();
        Proba[] proba = UtilsDTO.fromDTO(probe);
        return proba;
    }

    @Override
    public Participant[] getRegisteredParticipantsForProba(Proba proba) throws ConcursException {
        Request req = new Request.Builder()
                .type(RequestType.GET_PARTICIPANTI_PENTRU_PROBA)
                .data(UtilsDTO.toDTO(proba))
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConcursException(err);
        }
        ParticipantDTO[] participants = (ParticipantDTO[]) response.data();
        Participant[] participant = UtilsDTO.fromDTO(participants);
        return participant;
    }

    @Override
    public boolean addParticipantToProba(Integer participant, Integer probaId) throws ConcursException {
        Request req = new Request.Builder()
                .type(RequestType.INSCRIE_PARTICIPANT)
                .data(new InscriereDTO(participant, probaId))
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConcursException(err);
        }
        return true;
    }


    @Override
    public void logout(PersoanaOficiu persoanaOficiu, IConcursOberver client) throws ConcursException {
        logger.info("Logout");
        PersoanaOficiuDTO udto = UtilsDTO.toDTO(persoanaOficiu);
        Request req = new Request.Builder()
                .type(RequestType.LOGOUT)
                .data(udto)
                .build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConcursException(err);
        }
    }

    public Participant addParticipant(Participant participant) throws ConcursException {
        Request req = new Request.Builder()
                .type(RequestType.ADD_PARTICIPANT)
                .data(UtilsDTO.toDTO(participant))
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ConcursException(err);
        }
        return UtilsDTO.fromDTO((ParticipantDTO) response.data());
    }




    private void closeConnection() {
        logger.debug("Closing connection");
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch (IOException e)
        {
            logger.error("Error closing connection: " + e);
            logger.error(e.getStackTrace());
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.PARTICIPANT_REGISTERED;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    logger.debug("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (IOException|ClassNotFoundException e) {
                    logger.error("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.PARTICIPANT_REGISTERED) {
            Inscriere inscriere = UtilsDTO.fromDTO((InscriereDTO) response.data());
            logger.debug("inscriere efectuata"+inscriere);
            try {
                client.inscriereConcurs(inscriere);
            } catch (ConcursException e) {
                logger.error("Error handling update: ", e);
                logger.error(e.getStackTrace());
            }
        }
    }


}

