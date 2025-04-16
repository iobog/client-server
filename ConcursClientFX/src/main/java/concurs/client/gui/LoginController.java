package concurs.client.gui;

import concurs.model.PersoanaOficiu;
import concurs.services.ConcursException;
import concurs.services.IConcursServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    private IConcursServices server;
    private ProbeController probeCtrl;
    private PersoanaOficiu crtPersoanaOficiu;
    private Parent mainProbeParent;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void setServer(IConcursServices server) {
        this.server = server;
    }

    public void setParent(Parent p) {
        this.mainProbeParent = p;
    }

    public void setProbeController(ProbeController probeController) {
        this.probeCtrl = probeController;
    }

//    @FXML
//    public void handleLogin(ActionEvent actionEvent) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//        crtPersoanaOficiu = new PersoanaOficiu(username, password);
//
//        try {
//            server.login(crtPersoanaOficiu, probeCtrl);
//            logger.info("Persoana oficiu logged in: " + username);
//
//            Stage stage = new Stage();
//            stage.setTitle("Concurs - " + username);
//            stage.setScene(new Scene(mainProbeParent));
//
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent event) {
//                    probeCtrl.logout();
//                    logger.info("Closing application for: " + username);
//                    System.exit(0);
//                }
//            });
//
//            stage.show();
//            probeCtrl.setPersoanaOficiu(crtPersoanaOficiu);
//
//            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
//
//
//        } catch (ConcursException e) {
//            logger.error("Login error for: " + username, e);
//            showErrorAlert("Authentication failure", "Wrong username or password");
//        }
//    }



    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        crtPersoanaOficiu = new PersoanaOficiu(username, password);

        try {
            server.login(crtPersoanaOficiu, probeCtrl);
            logger.info("Persoana oficiu logged in: " + username);

            Stage stage = new Stage();
            stage.setTitle("Concurs - " + username);
            stage.setScene(new Scene(mainProbeParent));

            stage.setOnCloseRequest(event -> {
                probeCtrl.logout();
                logger.info("Closing application for: " + username);
                System.exit(0);
            });

            stage.show();
            probeCtrl.setPersoanaOficiu(crtPersoanaOficiu);

            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        } catch (ConcursException e) {
            logger.error("Login error: ", e);
            String errorMessage = "Authentication failure: " + e.getMessage();
            if (e.getMessage().contains("UNAVAILABLE")) {
                errorMessage += "\nThe server is unavailable. Please ensure the server is running.";
            }
            showErrorAlert("Authentication failure", errorMessage);
        }
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Concurs App");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}