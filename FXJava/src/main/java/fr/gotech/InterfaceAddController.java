package fr.gotech;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import fr.gotech.database.Database;
import fr.gotech.model.Interface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class InterfaceAddController implements Initializable {


    @FXML
    private ComboBox idTypeNetwork;
    @FXML
    private TextField idNomInterface;
    @FXML
    private ComboBox idTypeAdr;
    @FXML
    private TextField idIP;
    @FXML
    private TextField idCIDR;
    @FXML
    private TextField idGW;
    @FXML
    private TextField idDNS1;
    @FXML
    private TextField idDNS2;
    @FXML
    private AnchorPane anchorPane;

    private Stage dialogStage;
    private App app;
    private boolean okCliked = false;

    public boolean isOkCliked(){
        return true;
    }
    public void setMainApp(App app) {
        this.app = app;
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }


    private TreeItem<EquipementViewController.EquipementView> equipementView;
    private String pathClientAdd;

    public void getEquipementClient(TreeItem<EquipementViewController.EquipementView> equipementView, String clientID) {
        this.pathClientAdd = clientID;
        this.equipementView = equipementView;
    }

    private void initComboboxNetworkTypeAdr() throws IOException, ExecutionException, InterruptedException {

        Database connect = new Database();

        ObservableList typeNetwork = FXCollections.observableArrayList();
        ApiFuture<QuerySnapshot> future = connect.db.collection("typeConnexion").get();
        List<QueryDocumentSnapshot> documentsNetwork = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsNetwork) {
            typeNetwork.add(document.getId());
        }

        idTypeNetwork.getItems().setAll(typeNetwork);

        ObservableList typeTypAdr = FXCollections.observableArrayList();
        ApiFuture<QuerySnapshot> futureTypeAdr = connect.db.collection("typeAttribution").get();
        List<QueryDocumentSnapshot> documentsTypeAdr = futureTypeAdr.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsTypeAdr) {
            typeTypAdr.add(document.getId());
        }
        idTypeAdr.getItems().setAll(typeTypAdr);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initComboboxNetworkTypeAdr();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Listener pour la combobox DHCP/FIXE
        idTypeAdr.valueProperty().addListener((obs, oldValue, newValue) -> {
            String value = (String) newValue;
            if (newValue.toString().equals("DHCP")) {
                idIP.setDisable(true);
                idGW.setDisable(true);
                idCIDR.setDisable(true);
                idDNS1.setDisable(true);
                idDNS2.setDisable(true);
            }
            else {
                idIP.setDisable(false);
                idGW.setDisable(false);
                idCIDR.setDisable(false);
                idDNS1.setDisable(false);
                idDNS2.setDisable(false);
            }
        });


    }


    @FXML
    private void actionValider(ActionEvent actionEvent) throws IOException {

        Database connect = new Database();
        Interface formAddInterface = new Interface();
        String pathEquipementAdd = equipementView.getValue().getId();

        formAddInterface.typeCon = idTypeNetwork.getSelectionModel().getSelectedItem().toString();
        formAddInterface.name = idNomInterface.getText();
        formAddInterface.typeAdr = idTypeAdr.getSelectionModel().getSelectedItem().toString();
        if (formAddInterface.typeAdr.equals("DHCP"))
        {
            formAddInterface.ip = "";
            formAddInterface.cidr = "";
            formAddInterface.gw = "";
            formAddInterface.dns1 = "";
            formAddInterface.dns2 = "";
        }else{
            formAddInterface.ip = idIP.getText();
            formAddInterface.cidr = idCIDR.getText();
            formAddInterface.gw = idGW.getText();
            formAddInterface.dns1 = idDNS1.getText();
            formAddInterface.dns2 = idDNS2.getText();
        }
        connect.db.collection("client").document(pathClientAdd).collection("equipement").document(pathEquipementAdd).collection("interface").add(formAddInterface);

    }
    /*
    private void alertCreationOK(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Interface ajouté !");
        alert.setHeaderText("Votre interface vient d'étre ajouté");
        alert.setContentText("Shouaitez vous en ajouter une nouvelle ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("OKAY");
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("interface_add.fxml"));
                Parent root2 = (Parent) fxmlLoader.load();
                Stage stageNext = new Stage();
                stageNext.initModality(Modality.WINDOW_MODAL);
                stageNext.initStyle(StageStyle.UNDECORATED);
                InterfaceAddController interfaceController= fxmlLoader.getController();
                interfaceController.getClientEquipement(pathClientAdd, pathEquipementAdd);
                stageNext.setTitle("ABC");
                stageNext.setScene(new Scene(root2));
                stageNext.show();
                Stage stagePrec = (Stage) anchorPane.getScene().getWindow();
                stagePrec.close();

            }catch (Exception e) {
                Stage stagePrec = (Stage) anchorPane.getScene().getWindow();
                stagePrec.close();
            }
        }else {
            System.out.println("Cancel");
        }
    }*/


    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        Stage stagePrec = (Stage) anchorPane.getScene().getWindow();
        stagePrec.close();
    }


}
