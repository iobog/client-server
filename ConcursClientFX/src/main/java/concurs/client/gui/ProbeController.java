
package concurs.client.gui;

import concurs.model.*;
import concurs.services.ConcursException;
import concurs.services.IConcursOberver;
import concurs.services.IConcursServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProbeController implements Initializable, IConcursOberver {
    private static final Logger logger = LogManager.getLogger(ProbeController.class);
    public TableView ProbeTableView;
    public TableColumn ProbeColumn;

    // TODO
    ///  sa creez o lista locala de Probe

    private IConcursServices server;
    private PersoanaOficiu crtPersoanaOficiu;
    private Parent mainParent;

    // UI Components
    @FXML private TableView<Proba> probeTableView;
    @FXML private TableColumn<Proba, String> numeProbaColumn;
    @FXML private TableColumn<Proba, String> categorieVarstaColumn;
    @FXML private TableColumn<Proba, Integer> numarParticipantiColumn;
    @FXML private TableView<Participant> participantiTableView;
    @FXML private TableColumn<Participant, String> numeParticipantColumn;
    @FXML private TableColumn<Participant, Integer> varstaParticipantColumn;
    @FXML private TextField numeProbaField;
    @FXML private ComboBox<Integer> varstaMinCombo;
    @FXML private ComboBox<Integer> varstaMaxCombo;
    @FXML private CheckBox disciplina1CheckBox;
    @FXML private CheckBox disciplina2CheckBox;
    @FXML private CheckBox disciplina3CheckBox;
    @FXML private TextField numeParticipantField;
    @FXML private TextField cnpParticipantField;
    @FXML private Button registerButton;
    @FXML private Button logoutButton;

    private final ObservableList<Proba> probeModel = FXCollections.observableArrayList();
    private final ObservableList<Participant> participantiModel = FXCollections.observableArrayList();
    private final ObservableList<Integer> varsteOptions = FXCollections.observableArrayList(6,7,8,9,10,11,12,13,14,15);

    // Lista mea de probe local pe care o folosesec pentru id uri doar;
    private Iterable<Proba> probeList = new ArrayList<>();
    private Proba selectedProba;
    public void setServer(IConcursServices server) {
        this.server = server;
        setData();
    }

    private void setData() {
        initializeTableColumns();
        setupComboBoxes();

    }

    private void setProbeList( Iterable<Proba> probeList) {
        this.probeList = probeList;
    }

    public void setPersoanaOficiu(PersoanaOficiu persoanaOficiu) {
        this.crtPersoanaOficiu = persoanaOficiu;
        loadProbe();

    }

    public void setParent(Parent parent) {
        this.mainParent = parent;
    }

    private void initializeTableColumns() {
        numeProbaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNume()));
        categorieVarstaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorieVarsta()));
        numarParticipantiColumn.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getNumarParticipanti()).asObject());
        numeParticipantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCnp()));
        varstaParticipantColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVarsta()).asObject());
    }

    private void setupComboBoxes() {
        varstaMinCombo.setItems(FXCollections.observableArrayList(6, 9, 12));
        varstaMaxCombo.setItems(FXCollections.observableArrayList(8, 11, 15));
    }

    private void loadProbe() {
        try {
            var dataFromServe = server.getProbele();
            Iterable<Proba> data = new ArrayList<>(List.of(dataFromServe));
            probeModel.clear();
            probeModel.setAll(StreamSupport.stream(data.spliterator(), false)
                    .collect(Collectors.toList()));
            setProbeList(data);
            probeTableView.setItems(probeModel);
            logger.info("Loaded {} competitions", probeModel.size());
        } catch (ConcursException e) {
            showAlert("Error", "Failed to load competitions", e.getMessage());
            logger.error("Failed to load competitions", e);
        }
    }

    private Integer findProbaId(Proba selectedProba) {
        for (Proba proba : probeList) {
            if (proba.getNume().equals(selectedProba.getNume()) && proba.getCategorieVarsta().equals(selectedProba.getCategorieVarsta())) {
                return proba.getId();
            }
        }
        return -1;
    }

    @FXML
    private void handleShowParticipanti() {
        selectedProba = probeTableView.getSelectionModel().getSelectedItem();
        int id = findProbaId(selectedProba);

        if (id == -1) {
            showAlert("Warning", "No selection", "Please select a competition first");
            return;
        }
        if (selectedProba == null) {
            showAlert("Warning", "No selection", "Please select a competition first");
            return;
        }

        try {
            selectedProba.setCategorieVarsta(probeTableView.getSelectionModel().getSelectedItem().getCategorieVarsta());
            selectedProba.setId(id);
            List<Participant> participanti = List.of(server.getRegisteredParticipantsForProba(selectedProba));
            participantiModel.setAll(participanti);
            participantiTableView.setItems(participantiModel);
            logger.debug("Loaded {} participants for competition {}", participanti.size(), selectedProba.getNume());
        } catch (ConcursException e) {
            showAlert("Error", "Failed to load participants", e.getMessage());
            logger.error("Failed to load participants", e);

        }
    }


    private void handleShowParticipantiSelected() {
        int id = findProbaId(selectedProba);

        if (id == -1) {
            showAlert("Warning", "No selection", "Please select a competition first");
            return;
        }
        if (selectedProba == null) {
            showAlert("Warning", "No selection", "Please select a competition first");
            return;
        }

        try {
            List<Participant> participanti = List.of(server.getRegisteredParticipantsForProba(selectedProba));
            participantiModel.setAll(participanti);
            participantiTableView.setItems(participantiModel);
            logger.debug("Loaded {} participants for competition {}", participanti.size(), selectedProba.getNume());
        } catch (ConcursException e) {
            showAlert("Error", "Failed to load participants", e.getMessage());
            logger.error("Failed to load participants", e);

        }
    }

    private void handleShowParticipantiForSelectedProba(Proba proba) {
        Proba selectedProba = proba;

        if (selectedProba == null) {
            showAlert("Warning", "No selection", "Please select a competition first");
            return;
        }

        int id = findProbaId(selectedProba);
        if (id == -1) {
            showAlert("Warning", "Invalid competition", "Selected competition is invalid");
            return;
        }

        try {
            Proba tableSelected = probeTableView.getSelectionModel().getSelectedItem();
            if (tableSelected != null) {
                selectedProba.setCategorieVarsta(tableSelected.getCategorieVarsta());
            }

            selectedProba.setId(id);
            List<Participant> participanti = List.of(server.getRegisteredParticipantsForProba(selectedProba));
            participantiModel.setAll(participanti);
            participantiTableView.setItems(participantiModel);
            logger.debug("Loaded {} participants for competition {}", participanti.size(), selectedProba.getNume());
        } catch (ConcursException e) {
            showAlert("Error", "Failed to load participants", e.getMessage());
            logger.error("Failed to load participants", e);
        }
    }

    @FXML
    private void handleCauta() {

        // TODO
        // functia asta nu are sens aici, nu face nimic util
        String numeCautare = numeProbaField.getText().trim();
        Integer varstaMin = varstaMinCombo.getValue();
        Integer varstaMax = varstaMaxCombo.getValue();
        List<Proba> probeFiltrate = cautaProbe(numeCautare, varstaMin, varstaMax);
        probeModel.setAll(probeFiltrate);
        logger.debug("Found {} competitions matching criteria", probeFiltrate.size());
    }

    private List<Proba> cautaProbe(String numeCautare, Integer varstaMin, Integer varstaMax) {

        return StreamSupport.stream(probeList.spliterator(), false)
                .filter(proba -> proba.getNume().toLowerCase().contains(numeCautare.toLowerCase()))
                .filter(proba ->convertorVarstaFromStringToInt(proba.getCategorieVarsta()).first >= varstaMin && convertorVarstaFromStringToInt(proba.getCategorieVarsta()).second <= varstaMax)
                .collect(Collectors.toList());
    }

    private Tuple<Integer, Integer> convertorVarstaFromStringToInt(String varsta) {
        switch (varsta) {
            case "6-8": return new Tuple<>(6, 8);
            case "9-11": return new Tuple<>(9, 11);
            case "12-15": return new Tuple<>(12, 15);
            default: return new Tuple<>(0, 0);
        }
    }

    private Integer findProbaIdFromGivenNameAndAge(String nume, int varsta) {
        for(Proba proba: probeList) {
           if(proba.getNume().equals(nume) && proba.getCategorieVarsta().equals(determinaCategorieVarsta(varsta))) {
                return proba.getId();
            }
        }
        return null;
    }

    private Tuple<Integer, Integer> getProbesIdForParticipant(List<String> probeList, int varsat) {
        Tuple<Integer, Integer> probeId = new Tuple<>(null, null);

        for(String proba : probeList) {
            Integer id = findProbaIdFromGivenNameAndAge(proba, varsat);
            if(id != null){
                if (probeId.first == null) {
                    probeId.first = id;
                } else if (probeId.second == null) {
                    probeId.second = id;
                }
            }
        }
        return probeId;

    }

    private List<String> getDataFromCheckBox() {
        List<String> selectedDiscipline = new ArrayList<>();

        for (CheckBox checkBox : List.of(disciplina1CheckBox, disciplina2CheckBox, disciplina3CheckBox)) {
            if (checkBox.isSelected()) {
                selectedDiscipline.add(checkBox.getText());
            }
        }
        return selectedDiscipline;
    }

    @FXML
    private void handleInregistreaza() {
        String nume = numeParticipantField.getText().trim();
        String cnp = cnpParticipantField.getText().trim();
        List<String> selectedProbe = getDataFromCheckBox();
        // TODO
        // find Proba id
        // varsat din cnp a user ului sa nu fie mai mare decat

        if (!validateRegistrationInput(nume, cnp, selectedProbe)) {
            return;
        }
        try {

            int varsta = calculeazaVarstaDinCNP(cnp);
            Participant participant = new Participant(cnp, nume, varsta);
            Tuple<Integer,Integer> probeIds= getProbesIdForParticipant(selectedProbe, varsta);

            // Add participant and register
            participant = server.addParticipant(participant);
            if (participant == null) {
                showAlert("Error", "Registration failed", "Participant already registered");
                return;
            }
            if(probeIds.first != null) {
                server.addParticipantToProba(participant.getId(),probeIds.first);
            }

            if(probeIds.second != null)
                server.addParticipantToProba(participant.getId(),probeIds.second);

            probeModel.clear();
            probeModel.setAll(StreamSupport.stream(probeList.spliterator(), false)
                    .collect(Collectors.toList()));

            refreshData();
            clearRegistrationFields();
        } catch (ConcursException e) {
            showAlert("Error", "Registration failed", e.getMessage());
            logger.error("Registration failed", e);
        }
    }

    private boolean validateRegistrationInput(String nume, String cnp, List<String> proba) {
        if (nume.isEmpty() || cnp.isEmpty()) {
            showAlert("Error", "Missing data", "Please complete all fields");
            return false;
        }
        if(proba.isEmpty()) {
            showAlert("Error", "No discipline selected", "Please select at least one discipline");
            return false;
        }
        if (proba.size() > 2) {
            showAlert("Error", "Too many disciplines selected", "Please select a maximum of 2 disciplines");
            return false;
        }

        //TODO
        // mai am de implementat mai multe chestii pentru cnp, in special sa verific daca varsta din cnp este intre 9 si 15

        if (!cnp.matches("\\d{13}")) {

            showAlert("Error", "Invalid CNP", "CNP must contain exactly 13 digits");
            return false;
        }

        return true;
    }

    @FXML
    public void logout() {
        try {
            if (crtPersoanaOficiu != null) {
                server.logout(crtPersoanaOficiu, null);
                logger.info("User {} logged out", crtPersoanaOficiu.getUsername());
            }

            // Return to login screen
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(mainParent));
            loginStage.show();
        } catch (ConcursException e) {
            showAlert("Error", "Logout failed", e.getMessage());
            logger.error("Logout failed", e);
        }
    }

    private void refreshData() {
        probeTableView.refresh();
        loadProbe();
        handleShowParticipantiSelected();
    }

    private void refreshData(Integer probaId) {
        probeTableView.refresh();
        loadProbe();
        handleShowParticipanti();

        if (probeTableView.getSelectionModel().getSelectedItem() != null) {
            handleShowParticipanti();
        }
    }

    private void clearRegistrationFields() {
        numeParticipantField.clear();
        cnpParticipantField.clear();
        disciplina1CheckBox.setSelected(false);
        disciplina2CheckBox.setSelected(false);
        disciplina3CheckBox.setSelected(false);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleLogOut(ActionEvent actionEvent) {
        logout();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.debug("init: am in lista de probe");
        List<CheckBox> checkBoxes = List.of(disciplina1CheckBox, disciplina2CheckBox, disciplina3CheckBox);

        for (CheckBox cb : checkBoxes) {
            cb.setOnAction(event -> {
                long selectedCount = checkBoxes.stream().filter(CheckBox::isSelected).count();
                if (selectedCount > 2) {
                    cb.setSelected(false); // disallow more than 2
                    // Optionally show a message
                    System.out.println("Selectie maxima: 2 discipline.");
                }
            });
        }
    }
    private Proba findProbaById(Integer probaId) {
        if (probaId == null) return null;

        return StreamSupport.stream(probeList.spliterator(), false)
                .filter(p -> p.getId().equals(probaId))
                .findFirst()
                .orElse(null);
    }


    // TODO
    // aici nu mmi se pare prea corect ceea ce fac
    @Override
    public void inscriereConcurs(Inscriere inscriere) throws ConcursException {
//        Platform.runLater(() -> {
//            try {
//                // Salvează ID-ul probei selectate (dacă există)
//                Proba selected = selectedProba;
//                Integer selectedProbaId = (selected != null) ? findProbaId(selected) : null;
//
//                // Reîncarcă probele (va recrea obiectele în tabel)
//                loadProbe();
//
//                // Dacă e selectată o probă și e aceeași cu cea înscrisă
//                if (selectedProbaId != null && selectedProbaId.equals(inscriere.getProba())) {
//                    // Găsește din nou proba în lista actualizată și selecteaz-o din nou în tabel
//                    for (Proba proba : probeModel) {
//                        if (findProbaId(proba).equals(selectedProbaId)) {
//                            probeTableView.getSelectionModel().select(proba);
//                            break;
//                        }
//                    }
//
//                    // Reîncarcă participanții pentru acea probă
//                    handleShowParticipantiForSelectedProba(selected);
//                }
//
//                logger.info("Received registration update for proba ID {}", inscriere.getProba());
//            } catch (Exception e) {
//                logger.error("Error handling registration update", e);
//            }
//        });

        Platform.runLater(() -> {
            try {
                // 1. Refresh the competitions list
                loadProbe();

                // 2. Check if the updated competition is currently selected
                Proba updatedProba = findProbaById(inscriere.getProba());

                if (updatedProba != null) {
                    // 3. If we have a selected competition and it matches the updated one
                    if (selectedProba != null && selectedProba.getId().equals(updatedProba.getId())) {
                        // Refresh participants for the selected competition
                        handleShowParticipantiForSelectedProba(updatedProba);

                        // Update the selection to maintain UI state
                        probeTableView.getSelectionModel().select(updatedProba);
                        selectedProba = updatedProba;
                    }

                    // 4. Update the specific competition in the table
                    int index = probeModel.indexOf(updatedProba);
                    if (index >= 0) {
                        probeModel.set(index, updatedProba);
                    }
                }
            } catch (Exception e) {
                logger.error("Error handling registration update", e);
                Platform.runLater(() ->
                        showAlert("Error", "Update Failed", "Could not refresh competition data"));
            }
        });

    }


    private int calculeazaVarstaDinCNP(String CNP){
        try {
            int sexSecol = Character.getNumericValue(CNP.charAt(0));
            int an = Integer.parseInt(CNP.substring(1, 3));
            int luna = Integer.parseInt(CNP.substring(3, 5));
            int zi = Integer.parseInt(CNP.substring(5, 7));

            int anComplet;

            if (sexSecol == 5 || sexSecol == 6) {
                anComplet = 2000 + an;
            } else {
                throw new ConcursException("Prima cifra din CNP este invalida");
            }

            LocalDate dataNasterii = LocalDate.of(anComplet, luna, zi);
            LocalDate today = LocalDate.now();

            return Period.between(dataNasterii, today).getYears();
        } catch (Exception e) {
            return -1;
        }
    }

    private String determinaCategorieVarsta(int varsta) {
        if (varsta >= 6 && varsta <= 8) return "6-8";
        if (varsta >= 9 && varsta <= 11) return "9-11";
        if (varsta >= 12 && varsta <= 15) return "12-15";
        return "alta categorie";
    }
}