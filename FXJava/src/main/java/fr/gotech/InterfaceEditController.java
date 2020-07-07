package fr.gotech;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.jfoenix.controls.JFXListView;
import fr.gotech.database.Database;
import fr.gotech.model.Interface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class InterfaceEditController extends ListView<Interface> implements Initializable {

    //App main
    private Stage dialogStage;
    private boolean okCliked = false;
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
    private ComboBox idTypeAdr;
    @FXML
    private ComboBox idTypeNetwork;
    @FXML
    private TextField idNomInterface;
    // Module List View JFOENIX
    @FXML
    private JFXListView<String> listView;
    @FXML
    private Label labelClient;
    @FXML
    private Label labelEquipement;


    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public boolean isOkCliked(){
        return okCliked;
    }
    public String path;

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

    // Reference des objets Interface
    private ObservableList<Interface> listInterface = FXCollections.<Interface>observableArrayList();
    private TreeItem<EquipementViewController.EquipementView> equipementView;
    private String clientName = "";
    public String equipementID ="";

    //Methode de transfert des informations nécessaire pour permettre l'édition en base
    public void setInterfaces(ObservableList<Interface> interfaces, TreeItem selectedEquipement, String clientID) throws IOException {
        this.clientName=clientID;
        this.equipementView=selectedEquipement;
        this.listInterface.setAll(interfaces);

        String hostname = equipementView.getValue().getHostname();
        labelEquipement.setText(hostname);
        labelClient.setText(clientName);


        HashMap<String, Interface> hashMapInterfaces = new HashMap<>();


        ObservableList<String> listName = FXCollections.observableArrayList();
        for( int i=0 ; i < listInterface.size();i++) {
            listName.add(listInterface.get(i).getName());
            hashMapInterfaces.put(listInterface.get(i).getName(),listInterface.get(i));
        }
        listView.setItems(listName);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                idTypeNetwork.setValue(hashMapInterfaces.get(newValue).getTypeCon());
                idNomInterface.setText(hashMapInterfaces.get(newValue).getName());
                idTypeAdr.setValue(hashMapInterfaces.get(newValue).getTypeAdr());
                idIP.setText(hashMapInterfaces.get(newValue).getIp());
                idGW.setText(hashMapInterfaces.get(newValue).getGw());
                idCIDR.setText(hashMapInterfaces.get(newValue).getCidr());
                idDNS1.setText(hashMapInterfaces.get(newValue).getDns1());
                idDNS2.setText(hashMapInterfaces.get(newValue).getDns2());
            }
        });

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

            if (newValue.equals("DHCP")) {
                idNomInterface.setDisable(true);
                idIP.setDisable(true);
                idGW.setDisable(true);
                idCIDR.setDisable(true);
                idDNS1.setDisable(true);
                idDNS2.setDisable(true);
            }
            else {
                idNomInterface.setDisable(true);
                idIP.setDisable(false);
                idGW.setDisable(false);
                idCIDR.setDisable(false);
                idDNS1.setDisable(false);
                idDNS2.setDisable(false);
            }
        });

    }

    @FXML
    private void actionModifier(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        Interface formAddInterface = new Interface();
        Database connect = new Database();
        String equipementID = equipementView.getValue().getId();

        CollectionReference interfaceUpdate = connect.db.collection("client").document(clientName).collection("equipement").document(equipementID).collection("interface");
        Query query = interfaceUpdate.whereEqualTo("name",idNomInterface.getText());

        String pathInterface= new String(" ");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            pathInterface = (document.getId());
        }

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

        interfaceUpdate.document(pathInterface).set(formAddInterface);

    }

    @FXML
    private void actionSupprimer(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        Database connect = new Database();
        String equipementID = equipementView.getValue().getId();

        CollectionReference interfaceUpdate = connect.db.collection("client")
                .document(clientName)
                .collection("equipement")
                .document(equipementID)
                .collection("interface");
        Query query = interfaceUpdate.whereEqualTo("name",idNomInterface.getText());

        String pathInterface= new String(" ");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            pathInterface = (document.getId());
        }
        interfaceUpdate.document(pathInterface).delete();
    }

    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        dialogStage.close();

    }


}
