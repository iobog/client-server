package concurs.rpcprotocol;

import concurs.dto.*;
import concurs.model.Inscriere;
import concurs.model.Participant;
import concurs.model.PersoanaOficiu;
import concurs.model.Proba;
import concurs.services.ConcursException;
import concurs.services.IConcursOberver;
import concurs.services.IConcursServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class ConcursClientRpcReflectionWorker implements Runnable, IConcursOberver {
    private IConcursServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static Logger logger = LogManager.getLogger(ConcursClientRpcReflectionWorker.class);

    public ConcursClientRpcReflectionWorker(IConcursServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (Exception e) {
            logger.error(e);
            logger.error(e.getStackTrace());
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                logger.debug("Received request from client: "+request);
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            }
            catch(IOException|ClassNotFoundException e){
                logger.error(e);
                logger.error(e.getStackTrace());
                e.printStackTrace();
            }

            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                logger.error(e);
                logger.error(e.getStackTrace());
                e.printStackTrace();
            }
        }

        try{
            input.close();
            output.close();
            connection.close();
        }
        catch (IOException e){
            logger.error(e);
            logger.error(e.getStackTrace());
            e.printStackTrace();
        }
    }

    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    public Response handleRequest(Request request) {
        Response response = null;
        String handleName = "handle" + (request).type();
        logger.debug("Handling request: "+handleName);
        try{
            Method method = this.getClass().getDeclaredMethod(handleName, Request.class);
            response = (Response) method.invoke(this, request);
            logger.debug("Handling response: "+response);
        }
        catch (NoSuchMethodException | InvocationTargetException| IllegalAccessException e){
            logger.error(e);
            logger.error(e.getStackTrace());
            e.printStackTrace();
        }
        return response;
    }

    public void sendResponse(Response response) throws IOException {
        logger.debug("Sending response to client: " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    public Response handleLOGIN(Request request) {
        logger.debug("Login request: "+request.type());
        PersoanaOficiuDTO udto = (PersoanaOficiuDTO)request.data();
        PersoanaOficiu persoanaOficiu = UtilsDTO.fromDTO(udto);
        try{
            server.login(persoanaOficiu,this);
            return okResponse;
        }
        catch (ConcursException e){
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    public Response handleLOGOUT(Request request) {
        logger.debug("Logout request: "+request.type());
        PersoanaOficiuDTO udto = (PersoanaOficiuDTO)request.data();
        PersoanaOficiu persoanaOficiu = UtilsDTO.fromDTO(udto);
        try{
            server.logout(persoanaOficiu,this);
            connected=false;
            return okResponse;
        }
        catch (ConcursException e){
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    /// TODO: implement the rest of the methods
    /// getProbele
    public Response handleINSCRIE_PARTICIPANT(Request request) {
        logger.debug("Inscrie participant request: "+request.type());
        InscriereDTO udto = (InscriereDTO)request.data();
        try{
            server.addParticipantToProba(udto.getParticipantId(), udto.getProbaId());
            return new Response.Builder().type(ResponseType.PARTICIPANT_ADDED).build();
        }
        catch (ConcursException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    public Response handleGET_PARTICIPANTI_PENTRU_PROBA(Request request) {
        ProbaDTO dto = (ProbaDTO) request.data();
        Proba proba = UtilsDTO.fromDTO(dto);
        try {
            Participant[] participants = server.getRegisteredParticipantsForProba(proba);
            ParticipantDTO[] dtos = UtilsDTO.toDTO(participants);
            return new Response.Builder().type(ResponseType.PARTICIPANTI_LIST).data(dtos).build();
        } catch (ConcursException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
// 5140101765374

    public Response handleADD_PARTICIPANT(Request request) {
        logger.debug("Add participant request: " + request.type());
        try {
            ParticipantDTO udto = (ParticipantDTO) request.data();
            Participant participant = UtilsDTO.fromDTO(udto);
            Participant result = server.addParticipant(participant);

            if (result == null) {
                return new Response.Builder()
                        .type(ResponseType.ERROR)
                        .data("Participantul deja există în baza de date.")
                        .build();
            }

            return new Response.Builder()
                    .type(ResponseType.PARTICIPANT_ADDED)
                    .data(UtilsDTO.toDTO(result)) // opțional, dacă vrei să trimiți participantul înapoi
                    .build();
        } catch (ConcursException e) {
            return new Response.Builder()
                    .type(ResponseType.ERROR)
                    .data(e.getMessage())
                    .build();
        }
    }


    public Response handleGET_PROBE(Request request) {
        logger.debug("Get probe request: "+request.type());
        try{
            Proba[] proba = server.getProbele();
            return new Response.Builder().type(ResponseType.PROBE_LIST).data(UtilsDTO.toDTO(proba)).build();
        }
        catch (ConcursException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void inscriereConcurs(Inscriere inscriere) throws ConcursException {
        try {
            InscriereDTO dto = UtilsDTO.toDTO(inscriere);
            Response response = new Response.Builder()
                    .type(ResponseType.PARTICIPANT_REGISTERED)
                    .data(dto)
                    .build();
            sendResponse(response);
        } catch (IOException e) {
            throw new ConcursException("Error notifying participant inscription: " + e.getMessage());
        }
    }

}
