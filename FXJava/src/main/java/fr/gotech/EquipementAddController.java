package fr.gotech;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import fr.gotech.database.Database;
import fr.gotech.model.Equipement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class EquipementAddController implements Initializable {

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
    @FXML
    private AnchorPane anchorPane;

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
            etatList.add(   document.getId());
        }
        idEtat.getItems().setAll(etatList);
    }

    private void alert1(){
        Alert a1 = new Alert(Alert.AlertType.ERROR);
        a1.setTitle("Hostname déjà présent");
        a1.setContentText("Le nom de l'équipement existe déjà pour un autre équipement");
        a1.setHeaderText(null);
        a1.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initComboboxClient();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            initComboboxTypeMarqueModel();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            initComboboxEtat();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void actionValider(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        Equipement formAddEquipement = new Equipement();
        Database connect = new Database();
        String clientAdd = idClient.getSelectionModel().getSelectedItem().toString();

        formAddEquipement.hostname=idHostname.getText();
        formAddEquipement.type=idTypeEquipement.getSelectionModel().getSelectedItem().toString();
        formAddEquipement.marque=idMarque.getSelectionModel().getSelectedItem().toString();
        formAddEquipement.modele=idModele.getSelectionModel().getSelectedItem().toString();
        formAddEquipement.etat=idEtat.getSelectionModel().getSelectedItem().toString();
        formAddEquipement.sn=idSN.getText();
        formAddEquipement.achat = idDachat.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        formAddEquipement.garantie = idDgaranti.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        formAddEquipement.install = idDinstall.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        formAddEquipement.admin = idAdmin.getText();


        Query query = connect.db.collection("client").document(clientAdd)
                .collection("equipement")
                .whereEqualTo("hostname", formAddEquipement.hostname);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        boolean trouver = false;
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            if (! document.getId().equals(null)){
                trouver=true;
                alert1();
                break;
            }
        }
        if (trouver==false){
            connect.db.collection("client").document(clientAdd).collection("equipement").add(formAddEquipement);
            alertCreationOK();
        }

    }

    private void alertCreationOK(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Equipement ajouté !");
        alert.setHeaderText("Votre équipement vient d'étre ajouté");
        alert.setContentText("");

        Optional<ButtonType> result = alert.showAndWait();

    }




    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    private Stage dialogStage;
    private App app;
    private boolean okCliked = false;

    public boolean isOkCliked(){
        return true;
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void setMainApp(App app) {
        this.app = app;
    }

}


