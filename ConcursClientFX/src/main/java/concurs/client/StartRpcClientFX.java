package concurs.client;
import concurs.client.gui.LoginController;
import concurs.client.gui.ProbeController;
import concurs.rpcprotocol.ConcursServicesGrpcProxy;
//import concurs.rpcprotocol.ConcursServicesRpcProxy;
import concurs.services.IConcursServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55556;
    private static String defaultServer = "127.0.0.1";

    private static Logger logger = LogManager.getLogger(StartRpcClientFX.class);

    public void start(Stage primaryStage) throws Exception {
        logger.debug("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/concursclient.properties"));
            logger.info("Concurs properties set {} ", clientProps);
            // clientProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find concursclient.properties " + e);
            logger.debug("Looking into folder {}", (new File(".")).getAbsolutePath());
            return;
        }
        String serverIP = clientProps.getProperty("concurs.server.host", defaultServer);
        int serverPort = defaultChatPort;
        System.out.println("Server IP: " + serverIP);
        System.out.println("Server port: " + serverPort);
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("concurs.server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultChatPort);
        }
        logger.info("Using server IP " + serverIP);
        logger.info("Using server port " + serverPort);

        ConcursServicesGrpcProxy server = new ConcursServicesGrpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login-view.fxml"));
        Parent root=loader.load();
        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("probe-view.fxml"));
        Parent croot=cloader.load();
        ProbeController chatCtrl =
                cloader.<ProbeController>getController();

        chatCtrl.setServer(server);

        ctrl.setProbeController(chatCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP concurs");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}


