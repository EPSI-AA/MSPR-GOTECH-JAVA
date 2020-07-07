package fr.gotech;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import fr.gotech.database.Database;
import fr.gotech.model.Equipement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class EquipementEditController implements Initializable {

    @FXML
    private ComboBox idClient;
    @FXML
    private ComboBox idTypeEquipement;
    @FXML
    private ComboBox idMarque;
    @FXML
    private ComboBox idModele;
    @FXML
    private TextField idHostname;
    @FXML
    private TextField idSN;
    @FXML
    private DatePicker idDachat;
    @FXML
    private DatePicker idDgaranti;
    @FXML
    private DatePicker idDinstall;
    @FXML
    private ComboBox idEtat;
    @FXML
    private TextField idAdmin;


    private TreeItem<EquipementViewController.EquipementView> equipementView;
    private Stage dialogStage;
    private boolean okCliked = false;
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public boolean isOkCliked(){
        return okCliked;
    }
    private String clientName = "";
    public void setEquipement(TreeItem<EquipementViewController.EquipementView> equipementView, String client) throws ParseException {
        this.equipementView = equipementView;
        this.clientName=client;

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            idClient.setDisable(true);
            idClient.setValue(client);

            idTypeEquipement.setValue(equipementView.getValue().getType());
            idMarque.setValue(equipementView.getValue().getMarque());
            idModele.setValue(equipementView.getValue().getModele());
            idEtat.setValue(equipementView.getValue().getEtat());

            LocalDate achat = LocalDate.parse(equipementView.getValue().getAchat(),format);
            idDachat.setValue(achat);
            LocalDate garantie = LocalDate.parse(equipementView.getValue().getGarantie(),format);
            idDgaranti.setValue(garantie);
            LocalDate install = LocalDate.parse(equipementView.getValue().getInstall(),format);
            idDinstall.setValue(install);
            System.out.println(LocalDate.now());
            idHostname.setText(equipementView.getValue().getHostname());
            idSN.setText(equipementView.getValue().getSn());
            idAdmin.setText(equipementView.getValue().getAdmin());
    }



    private void initComboboxClient() throws IOException, ExecutionException, InterruptedException {
        Database connect = new Database();
        ObservableList clientList = FXCollections.observableArrayList();
        ApiFuture<QuerySnapshot> clietFuture = connect.db.collection("client").get();
        List<QueryDocumentSnapshot> documentsClient = clietFuture.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsClient) {
            clientList.add(document.getId());
        }
        idClient.getItems().setAll(clientList);
    }
    private void initComboboxTypeMarqueModel() throws IOException, ExecutionException, InterruptedException {
        Database connect = new Database();

        ObservableList typeList = FXCollections.observableArrayList();
        ObservableList marqueList = FXCollections.observableArrayList();
        ObservableList modeleList = FXCollections.observableArrayList();

        ApiFuture<QuerySnapshot> future = connect.db.collection("typeEquipement").get();
        List<QueryDocumentSnapshot> documentsType = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsType) {
            typeList.add(document.getId());
        }
        idTypeEquipement.getItems().setAll(typeList);

        idTypeEquipement.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                idMarque.getItems().clear();
                idMarque.setDisable(true);
            }
            else {
                idModele.getItems().clear();
                modeleList.clear();
                ApiFuture<QuerySnapshot> futureMarque = connect.db.collection("typeEquipement").document((String) newValue).collection("marque").get();
                List<QueryDocumentSnapshot> documentsMarque = null;
                try {
                    documentsMarque = futureMarque.get().getDocuments();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                for (QueryDocumentSnapshot document : documentsMarque) {
                    modeleList.add(document.getId());
                }
                idMarque.getItems().setAll(modeleList);

            }
        });

        idMarque.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                idModele.getItems().clear();
                idModele.setDisable(true);
            }
            else {
                marqueList.clear();
                String type = idTypeEquipement.getSelectionModel().getSelectedItem().toString();
                ApiFuture<QuerySnapshot> futurModele = connect.db.collection("typeEquipement")
                        .document(type)
                        .collection("marque")
                        .document((String) newValue)
                        .collection("modele").get();
                List<QueryDocumentSnapshot> documentsModel=null;
                try {
                    documentsModel = futurModele.get().getDocuments();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                for (QueryDocumentSnapshot document : documentsModel) {
                    marqueList.add(document.getId());
                }
                idModele.getItems().setAll(marqueList);
            }
        });



    }
    private void initComboboxEtat() throws IOException, ExecutionException, InterruptedException {
        Database connect = new Database();
        ObservableList etatList = FXCollections.observableArrayList();
        ApiFuture<QuerySnapshot> futureEtat = connect.db.collection("etat").get();
        List<QueryDocumentSnapshot> documentsEtat = futureEtat.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsEtat) {
            etatList.add(document.getId());
        }
        idEtat.getItems().setAll(etatList);
    }

    private boolean isInputValid() {
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initComboboxClient();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            initComboboxTypeMarqueModel();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            initComboboxEtat();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actionValider(ActionEvent actionEvent) throws IOException {
        if (isInputValid()) {
            String ID = equipementView.getValue().getId().toString();
            Database connect = new Database();
            Equipement editEquipement = new Equipement();
            editEquipement.setType(idTypeEquipement.getSelectionModel().getSelectedItem().toString());
            editEquipement.setMarque(idMarque.getSelectionModel().getSelectedItem().toString());
            editEquipement.setModele(idModele.getSelectionModel().getSelectedItem().toString());
            editEquipement.setHostname(idHostname.getText());
            editEquipement.setSn(idSN.getText());
            editEquipement.setAdmin(idAdmin.getText());
            editEquipement.setAchat(idDachat.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            editEquipement.setGarantie(idDgaranti.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            editEquipement.setInstall(idDinstall.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            editEquipement.setEtat(idEtat.getSelectionModel().getSelectedItem().toString());

            connect.db.collection("client")
                    .document(clientName)
                    .collection("equipement")
                    .document(ID).set(editEquipement);
            System.out.println("Path is C:Client -> D:"+ clientName + " ->C:equipement ->d:" + ID);
            okCliked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
