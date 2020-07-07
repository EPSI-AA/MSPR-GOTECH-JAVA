package fr.gotech;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import fr.gotech.Creation_PDF;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import fr.gotech.database.Database;
import fr.gotech.model.Client;
import fr.gotech.model.Equipement;
import fr.gotech.model.Interface;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class EquipementViewController implements Initializable {


    @FXML
    public String clientID;
    @FXML
    private Label clientLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label marqueLabel;
    @FXML
    private Label modeleLabel;
    @FXML
    private Label nomLabel;
    @FXML
    private Label snLabel;
    @FXML
    private Label achatLabel;
    @FXML
    private Label garantieLabel;
    @FXML
    private Label installationLabel;
    @FXML
    private Label etatLabel;
    @FXML
    private Label urlLabel;
    @FXML
    private JFXTextField idInputText;
    @FXML
    private JFXComboBox idComboboxClient;
    @FXML
    private Label idLabel;
    @FXML
    private TabPane tabPane;



    private App app;
    private boolean okCliked = false;



    public void initClientID (String client){
        this.clientID=client;
    }

    public boolean isOkCliked(){
        return okCliked;
    }

    public void setMainApp(App app){

        this.app = app;
    }









    @FXML
    private JFXTreeTableView<EquipementView> tableauEquipement;
    ObservableList<EquipementView> equipements = FXCollections.observableArrayList();
/*
    @FXML
    private JFXTreeTableView<EquipementView> tableauRouteur;
    ObservableList<EquipementView> routeurs = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<EquipementView> tableauServeur;
    ObservableList<EquipementView> serveurs = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<EquipementView> tableauSwitch;
    ObservableList<EquipementView> switchs = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<EquipementView> tableauImprimante;
    ObservableList<EquipementView> imprimantes = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<EquipementView> tableauNAS;
    ObservableList<EquipementView> nas = FXCollections.observableArrayList();

 */

    public void initTab(String client) throws IOException, ExecutionException, InterruptedException {
        initClientID(client);
        // init list
        equipements.clear();


        // init Database db
        Database handler = new Database();
        ApiFuture<QuerySnapshot> future = handler.db.collection("client")
                .document(client).collection("equipement").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Get value Firestore
        int i=0;
        for (QueryDocumentSnapshot document : documents) {

            String id = document.getId();
            String type = document.getString("type");
            String hostname = document.getString("hostname");
            String marque = document.getString("marque");
            String modele = document.getString("modele");
            String sn = document.getString("sn");
            String etat = document.getString("etat");
            String achat = document.getString("achat");
            String garantie = document.getString("garantie");
            String install = document.getString("install");
            String admin = document.getString("admin");
            i++;

            //Tableau All
            equipements.add(new EquipementView(achat, garantie, install, id, type, hostname, marque, modele, sn, etat, admin));
        }

        // Message de statisque
        if (i==1){
            idLabel.setText("Votre client"+clientID+" possède : "+String.valueOf(i)+" équipement");

        }else if (i > 1 && i!=0){
            idLabel.setText("Votre client possède : "+String.valueOf(i)+" équipements");
        }

        final TreeItem<EquipementView> root = new RecursiveTreeItem<EquipementView>(equipements, RecursiveTreeObject::getChildren);

        // Create View
        tableauEquipement.setRoot(root);
        tableauEquipement.setShowRoot(false);
        showEquipementDetails(null);

        // Listener pour connaitre la nouvelle valeur qui est sélectionné par l'utilisateur lors de la consultation du tableau
        tableauEquipement.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> {
                    try {
                        showEquipementDetails(newValue);
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }));

        idComboboxClient.setValue(clientID);

    }

    // Méthode d'initialisation FXML/Controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            showEquipementDetails(null);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        // Initialise la Combobox qui contient la liste des clients
        try {
            initComboboxClient();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JFXTreeTableColumn<EquipementView,String> typeId = new JFXTreeTableColumn<>("Type");
        typeId.setPrefWidth(150);
        typeId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().type;
            }
        });
        JFXTreeTableColumn<EquipementView,String> nomId = new JFXTreeTableColumn<>("Nom");
        nomId.setPrefWidth(150);
        nomId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().hostname;
            }
        });
        JFXTreeTableColumn<EquipementView,String> marqueID = new JFXTreeTableColumn<>("Marque");
        marqueID.setPrefWidth(150);
        marqueID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().marque;
            }
        });
        JFXTreeTableColumn<EquipementView,String> modeleID = new JFXTreeTableColumn<>("Modele");
        modeleID.setPrefWidth(150);
        modeleID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().modele;
            }
        });

        JFXTreeTableColumn<EquipementView,String> snID = new JFXTreeTableColumn<>("SN");
        snID.setPrefWidth(150);
        snID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().sn;
            }
        });
        JFXTreeTableColumn<EquipementView,String> etatID = new JFXTreeTableColumn<>("Etat");
        etatID.setPrefWidth(75);
        etatID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<EquipementView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<EquipementView, String> param) {
                return param.getValue().getValue().etat;
            }
        });

        tableauEquipement.getColumns().setAll(typeId, nomId, marqueID, modeleID, etatID, snID);


        //Code responsable de la recherche d'une information dans le tableau
        idInputText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tableauEquipement.setPredicate(new Predicate<TreeItem<EquipementView>>() {
                    @Override
                    public boolean test(TreeItem<EquipementView> EquipementViewTreeItem) {
                        Boolean flag = EquipementViewTreeItem.getValue().hostname.getValue().contains(newValue) ||
                                EquipementViewTreeItem.getValue().sn.getValue().contains(newValue) ||
                                EquipementViewTreeItem.getValue().marque.getValue().contains(newValue) ||
                                EquipementViewTreeItem.getValue().type.getValue().contains(newValue) ||
                                EquipementViewTreeItem.getValue().etat.getValue().contains(newValue);
                        return flag;
                    }
                });
            }
        });
    }

    // Requête vers Firestor pour connaitre tous les clients disponible dans l'application pour consulter leur équipements
    public void initComboboxClient() throws IOException, ExecutionException, InterruptedException {
        Database connect = new Database();
        idComboboxClient.getItems().clear();
        ObservableList clientList = FXCollections.observableArrayList();
        ApiFuture<QuerySnapshot> clientFuture = connect.db.collection("client").get();
        List<QueryDocumentSnapshot> documentsClient = clientFuture.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsClient) {
            clientList.add(document.getId());
        }
        idComboboxClient.getItems().setAll(clientList);
    }

    // Affiche les détails de l'équipement qui est selectionné ainsi que ses interfaces
    private void showEquipementDetails(TreeItem<EquipementView> equipement) throws IOException, ExecutionException, InterruptedException {
        if (equipement != null){
            clientLabel.setText(clientID);

            typeLabel.setText(equipement.getValue().getType());
            marqueLabel.setText(equipement.getValue().getMarque());
            modeleLabel.setText(equipement.getValue().getModele());
            nomLabel.setText(equipement.getValue().getHostname());
            snLabel.setText(equipement.getValue().getSn());
            achatLabel.setText(equipement.getValue().getAchat());
            garantieLabel.setText(equipement.getValue().getGarantie());
            installationLabel.setText(equipement.getValue().getInstall());
            etatLabel.setText(equipement.getValue().getEtat());
            urlLabel.setText(equipement.getValue().getAdmin());


            Database connect = new Database();
            ApiFuture<QuerySnapshot> future = connect.db.collection("client").document(clientID)
                    .collection("equipement")
                    .document(equipement.getValue().getId())
                    .collection("interface").get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();



            int j=0;
            int i = tabPane.getTabs().size();
            tabPane.getTabs().remove(1,i);

            Interface getInterface = new Interface();
            for (QueryDocumentSnapshot document : documents) {

                // Get data & set data
                getInterface.name = document.getString("name");
                Label label1NomInterface = new Label(getInterface.getName());
                getInterface.typeCon = document.getString("typeCon");
                Label label2TypeInterface = new Label(getInterface.getTypeCon());
                getInterface.typeAdr = document.getString("typeAdr");
                Label label3GetName = new Label(getInterface.getTypeAdr());


                // Label Init
                Label label1 = new Label("Nom de l'interface :");
                Label label2 = new Label("Type d'interface :");
                Label label3 = new Label("Type d'adressage :");


                // gridInit
                GridPane gridInterface = new GridPane();

                // CSS id
                gridInterface.setId("gridInterfaces");
                gridInterface.getColumnConstraints().add(new ColumnConstraints(180));
                gridInterface.getColumnConstraints().add(new ColumnConstraints(180));
                gridInterface.setHgap(25);
                gridInterface.setVgap(25);
                gridInterface.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                gridInterface.setAlignment(Pos.TOP_LEFT);
                gridInterface.setPadding(new Insets(40,0,0,25));
                gridInterface.setStyle("-fx-font-size: 18px");


                gridInterface.add(label1, 0 , 0);
                gridInterface.add(label1NomInterface,1,0);
                gridInterface.add(label2,0,1);
                gridInterface.add(label2TypeInterface,1,1);
                gridInterface.add(label3,0,2);
                gridInterface.add(label3GetName,1,2);
                // IP fixe ?
                if (getInterface.typeAdr == "DHCP"){
                    getInterface.ip = " ";
                    getInterface.cidr = " ";
                    getInterface.gw = " ";
                    getInterface.dns1 = " ";
                    getInterface.dns2 = " ";
                }
                else {

                    Label label4 = new Label("Adresse IP :");
                    Label label5 = new Label("Masque réseau :");
                    Label label6 = new Label("Passerelle :");
                    Label label7 = new Label("DNS 1:");
                    Label label8 = new Label("DNS 2 :");

                    getInterface.ip = document.getString("ip");
                    Label label4ip = new Label(getInterface.getIp());
                    getInterface.cidr = document.getString("cidr");
                    Label label5cidr = new Label((getInterface.getCidr()));
                    getInterface.gw = document.getString("gw");
                    Label label6gw = new Label((getInterface.getGw()));
                    getInterface.dns1 = document.getString("dns1");
                    Label label7dns1 = new Label((getInterface.getDns1()));
                    getInterface.dns2 = document.getString("dns2");
                    Label label8dns2 = new Label((getInterface.getDns2()));

                    gridInterface.add(label4,0,3);
                    gridInterface.add(label4ip,1,3);
                    gridInterface.add(label5,0,4);
                    gridInterface.add(label5cidr,1,4);
                    gridInterface.add(label6,0,5);
                    gridInterface.add(label6gw,1,5);
                    gridInterface.add(label7,0,6);
                    gridInterface.add(label7dns1,1,6);
                    gridInterface.add(label8,0,7);
                    gridInterface.add(label8dns2,1,7);
                }

                j++;
                Tab tab = new Tab("Interface :" + j);
                tabPane.getTabs().add(tab);
                tab.setContent(gridInterface);
            }
        }
        else{
            clientLabel.setText("");
            typeLabel.setText("");
            marqueLabel.setText("");
            modeleLabel.setText("");
            nomLabel.setText("");
            snLabel.setText("");
            achatLabel.setText("");
            garantieLabel.setText("");
            installationLabel.setText("");
            etatLabel.setText("");
            urlLabel.setText("");
        }
    }

    // Modification d'un équipement selectionné
    @FXML
    private void actionEditer(ActionEvent actionEvent) {
        TreeItem selectedEquipement = tableauEquipement.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null){
            boolean okCliked = app.showEquipementEditDialog(selectedEquipement, clientID);
            if(okCliked){
                app.showEquipementEditDialog(selectedEquipement,clientID);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Auncune selection");
            alert.setHeaderText("Aucun équipement selectionné");
            alert.setContentText("Merci de selectionner un équipement avant d'éditer.");

            alert.showAndWait();
        }
    }

    // Supprimer l'équipement sélectionné
    @FXML
    private void actionSupprimer(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        if (tableauEquipement.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucune Selection");
            alert.setHeaderText("Vous n'avez selectionné aucun équipement");
            alert.setContentText("Merci de selectionner un équipement avant de supprimer.");
            alert.showAndWait();
        }else{
            String id = tableauEquipement.getSelectionModel().getSelectedItem().getValue().getId();

            Database delEquipement = new Database();
            delEquipement.db.collection("client").document(clientID).collection("equipement").document(id).delete();
            initTab(clientID);
        }
    }

    // Méthode pour rafraichir les donnnées présente dans la base de donné firestore
    @FXML
    public void actionRecharger(ActionEvent actionEvent) throws InterruptedException, ExecutionException, IOException {
        initTab(clientID);
    }

    // Requête vers Firestore afin récupérer les équipements du nouveau client selectionné dans la liste déroulante
    @FXML
    private void actionCombobox(ActionEvent actionEvent) throws InterruptedException, ExecutionException, IOException {
        String changeClient = idComboboxClient.getSelectionModel().getSelectedItem().toString();
        initTab(changeClient);
    }

    // Méthode nécessaire à la création du QRCocde : actionQRCode
    private static BitMatrix generateMatrix(final String data, final int size) throws WriterException {
        final BitMatrix bitMatrix = new QRCodeWriter().encode(String.valueOf(data), BarcodeFormat.QR_CODE, size, size);
        return bitMatrix;
    }
    // Méthode nécessaire à la création du QRCocde : actionQRCode
    private static void writeImage(final String outputFileName, final String imageFormat, final BitMatrix bitMatrix) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFileName));
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, fileOutputStream);
        fileOutputStream.close();
    }

    // Méthode pour générer le QRCode d'un équipement selectionné
    @FXML
    private void actionQRCode(ActionEvent actionEvent) throws WriterException, IOException {
        if (tableauEquipement.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucune Selection");
            alert.setHeaderText("Vous n'avez selectionné aucun équipement");
            alert.setContentText("Merci de selectionner un équipement avant de générer un QRCODE.");
            alert.showAndWait();
        }else{
            String path ="";
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarde du PDF matériel");
            FileChooser.ExtensionFilter pdf = new FileChooser.ExtensionFilter("PNG Files (*.png)","*.png");
            fileChooser.getExtensionFilters().add(pdf);
            fileChooser.setInitialFileName(clientID + "_" + tableauEquipement.getSelectionModel().getSelectedItem().getValue().getHostname() + ".png");
            File selectedFile= fileChooser.showSaveDialog(app.getPrimaryStage());

            String clientid = clientID;
            String equipementid = tableauEquipement.getSelectionModel().getSelectedItem().getValue().getId();
            String data = "http://www.mspr.inventaire.com/"+ clientid+":"+equipementid;

            String imageFormat = "png";
            String outputFileName = "/home/advance/Documents/EPSI/"+clientid +"."+imageFormat;
            path = selectedFile.getAbsolutePath();
            System.out.println("Start at " + path);
            System.out.println("Start at " + path.length() + "- 4");
            if(selectedFile == null){
            }
            else{
                if (path.substring(path.length() - 4)!=".png"){
                    path = path + ".png";
                    outputFileName = path;
                }
            }
            final int size = 400;
            final BitMatrix bitMatrix = generateMatrix(data, size);
            writeImage(outputFileName, imageFormat, bitMatrix);
            System.out.println("Ecrit à " + outputFileName);
            System.out.println("SimpleQrcodeGenerator FIN");
        }
    }


    // Méthode pour modifier/supprimer une interface ou plusieurs interfaces d'un équipement
    @FXML
    private void modifierInterface(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        TreeItem selectedEquipement = tableauEquipement.getSelectionModel().getSelectedItem();

        if (tabPane.getTabs().size() > 1 ){
            Database connect = new Database();
            ApiFuture<QuerySnapshot> future = connect.db.collection("client")
                    .document(clientID).collection("equipement")
                    .document(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getId())
                    .collection("interface").get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();


            ObservableList<Interface> interfaces = FXCollections.observableArrayList();
            int i =0;
            for (QueryDocumentSnapshot document : documents) {

                String name = document.getString("name");
                String typeCon = document.getString("typeCon");
                String typeAdr = document.getString("typeAdr");
                String ip = " ";
                String cidr = " ";
                String gw = " ";
                String dns1 = " ";
                String dns2 = " ";
                if ( typeAdr.equals("FIXE")){
                    ip = document.getString("ip");
                    cidr = document.getString("cidr");
                    gw = document.getString("gw");
                    dns1 = document.getString("dns1");
                    dns2 = document.getString("dns2");
                }
                i++;
                interfaces.add(new Interface(ip, cidr, gw, dns1, dns2, name, typeAdr, typeCon ));
            }

            boolean okCliked = app.showInterfaceEditDialog(interfaces, selectedEquipement, clientID);
            if (okCliked) {
                app.showInterfaceEditDialog(interfaces, selectedEquipement, clientID);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Auncune interface");
            alert.setHeaderText("Cet équipement ne dispose pas de configuration réseau");
            alert.setContentText("Merci de vérifier la présence d'une interface avant de vouloir la modifier.");

            alert.showAndWait();
        }

    }

    @FXML
    private void ajouerInterface(ActionEvent actionEvent) {
        TreeItem selectedEquipement = tableauEquipement.getSelectionModel().getSelectedItem();
        app.showInterfaceAdd(selectedEquipement, clientID);

    }

    @FXML
    private void actionPDF(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        if (tableauEquipement.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucune Selection");
            alert.setHeaderText("Vous n'avez selectionné aucun équipement");
            alert.setContentText("Merci de selectionner un équipement avant de générer un PDF.");
            alert.showAndWait();
        }
        else {
            String path = "";
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarde du PDF matériel");
            FileChooser.ExtensionFilter pdf = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(pdf);
            fileChooser.setInitialFileName(clientID + "_" + tableauEquipement.getSelectionModel().getSelectedItem().getValue().getHostname() + ".pdf");
            File selectedFile = fileChooser.showSaveDialog(app.getPrimaryStage());

            path = selectedFile.getAbsolutePath();
            System.out.println("Start at " + path);
            System.out.println("Start at " + path.length() + "- 4");
            if (selectedFile == null) {
            } else {
                if (path.substring(path.length() - 4) != ".pdf") {
                    path = path + ".pdf";
                }
            }
            if (tableauEquipement.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(app.getPrimaryStage());
                alert.setTitle("Aucune Selection");
                alert.setHeaderText("Vous n'avez selectionné aucun équipement");
                alert.setContentText("Merci de selectionner un équipement avant de générer un PDF.");
                alert.showAndWait();
            } else {
                Equipement equipementPDF = new Equipement();
                equipementPDF.setEtat(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getEtat());
                equipementPDF.setInstall(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getInstall());
                equipementPDF.setGarantie(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getGarantie());
                equipementPDF.setAchat(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getAchat());
                equipementPDF.setSn(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getSn());
                equipementPDF.setHostname(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getHostname());
                equipementPDF.setAdmin(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getAdmin());
                equipementPDF.setType(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getType());
                equipementPDF.setModele(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getModele());
                equipementPDF.setMarque(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getMarque());

                Database connect = new Database();
                Client clientPDF = new Client();
                DocumentReference docRef = connect.db.collection("client").document(clientID);
                ApiFuture<DocumentSnapshot> rqClient = docRef.get();
                DocumentSnapshot document = rqClient.get();
                clientPDF.setAdresse(document.get("adresse").toString());
                clientPDF.setVille(document.get("ville").toString());
                clientPDF.setCp(document.get("cp").toString());
                clientPDF.setTel(document.get("tel").toString());
                clientPDF.setEmail(document.get("email").toString());

                System.out.println(clientPDF.getAdresse());
                System.out.println(equipementPDF.getHostname());
                System.out.println(clientID + tableauEquipement.getSelectionModel().getSelectedItem().getValue().getId());
                ApiFuture<QuerySnapshot> future = connect.db.collection("client").document(clientID)
                        .collection("equipement")
                        .document(tableauEquipement.getSelectionModel().getSelectedItem().getValue().getId())
                        .collection("interface").get();

                List<QueryDocumentSnapshot> documents = future.get().getDocuments();
                List<Interface> interfaces = new ArrayList<>();

                for (QueryDocumentSnapshot documentInterface : documents) {

                    String name = documentInterface.getString("name");
                    String typeCon = documentInterface.getString("typeCon");
                    String typeAdr = documentInterface.getString("typeAdr");
                    String ip = " ";
                    String cidr = " ";
                    String gw = " ";
                    String dns1 = " ";
                    String dns2 = " ";
                    if (typeAdr.equals("FIXE")) {
                        ip = documentInterface.getString("ip");
                        cidr = documentInterface.getString("cidr");
                        gw = documentInterface.getString("gw");
                        dns1 = documentInterface.getString("dns1");
                        dns2 = documentInterface.getString("dns2");
                    }
                    interfaces.add(new Interface(ip, cidr, gw, dns1, dns2, name, typeAdr, typeCon));
                    System.out.println(interfaces.size());
                }
                System.out.println(interfaces.size());
                Creation_PDF.Creation(path, clientID, clientPDF, equipementPDF, interfaces);
            }
        }
    }


    //Définition de la classe EquipementView : Type objet présent dans le tableau TreeItem de la librairie JFOENIX
    class EquipementView extends RecursiveTreeObject<EquipementView> {

        StringProperty achat;
        StringProperty garantie;
        StringProperty install;
        StringProperty id;
        StringProperty type;
        StringProperty hostname;
        StringProperty marque;
        StringProperty modele;
        StringProperty sn;
        StringProperty etat;
        StringProperty admin;

        public EquipementView (String achat, String garantie, String install, String id, String type, String hostname, String marque, String modele, String sn, String etat, String admin){

            this.achat = new SimpleStringProperty(achat);
            this.garantie = new SimpleStringProperty(garantie);
            this.install = new SimpleStringProperty(install);
            this.id = new SimpleStringProperty(id);
            this.type = new SimpleStringProperty(type);
            this.hostname = new SimpleStringProperty(hostname);
            this.marque = new SimpleStringProperty(marque);
            this.modele = new SimpleStringProperty(modele);
            this.sn = new SimpleStringProperty(sn);
            this.etat = new SimpleStringProperty(etat);
            this.admin = new SimpleStringProperty(admin);
        }
        public HashMap<String, String> getAll(){
            HashMap<String, String> liste = new HashMap<>();
            liste.put("achat",getAchat());
            liste.put("garantie",getGarantie());
            liste.put("install",getInstall());
            liste.put("id",getId());
            liste.put("type",getType());
            liste.put("hostname",getHostname());
            liste.put("marque",getMarque());
            liste.put("modele",getModele());
            liste.put("sn",getSn());
            liste.put("etat",getEtat());
            liste.put("admin",getAdmin());
            return liste;
        }

        public String getAchat() {
            return achat.get();
        }

        public StringProperty achatProperty() {
            return achat;
        }

        private void setAchat(String achat) {
            this.achat.set(achat);
        }

        public String getGarantie() {
            return garantie.get();
        }

        public StringProperty garantieProperty() {
            return garantie;
        }

        private void setGarantie(String garantie) {
            this.garantie.set(garantie);
        }

        public String getInstall() {
            return install.get();
        }

        public StringProperty installProperty() {
            return install;
        }

        private void setInstall(String install) {
            this.install.set(install);
        }

        public String getId() {
            return id.get();
        }

        public StringProperty idProperty() {
            return id;
        }

        private void setId(String id) {
            this.id.set(id);
        }

        public String getType() {
            return type.get();
        }

        public StringProperty typeProperty() {
            return type;
        }

        private void setType(String type) {
            this.type.set(type);
        }

        public String getHostname() {
            return hostname.get();
        }

        public StringProperty hostnameProperty() {
            return hostname;
        }

        private void setHostname(String hostname) {
            this.hostname.set(hostname);
        }

        public String getMarque() {
            return marque.get();
        }

        public StringProperty marqueProperty() {
            return marque;
        }

        private void setMarque(String marque) {
            this.marque.set(marque);
        }

        public String getModele() {
            return modele.get();
        }

        public StringProperty modeleProperty() {
            return modele;
        }

        private void setModele(String modele) {
            this.modele.set(modele);
        }

        public String getSn() {
            return sn.get();
        }

        public StringProperty snProperty() {
            return sn;
        }

        private void setSn(String sn) {
            this.sn.set(sn);
        }

        public String getEtat() {
            return etat.get();
        }

        public StringProperty etatProperty() {
            return etat;
        }

        private void setEtat(String etat) {
            this.etat.set(etat);
        }

        public String getAdmin() {
            return admin.get();
        }

        public StringProperty adminProperty() {
            return admin;
        }

        private void setAdmin(String admin) {
            this.admin.set(admin);
        }
    }

}
