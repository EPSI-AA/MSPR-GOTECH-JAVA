package fr.gotech;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import fr.gotech.database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class ClientViewController implements Initializable {

    @FXML
    private JFXTreeTableView<ClientView> clientTable;
    @FXML
    private Label nameLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label cpLabel;
    @FXML
    private Label villeLabel;
    @FXML
    private Label telLabel;
    @FXML
    private Label mailLabel;
    @FXML
    private JFXTextField idInput;

    private App app;
    private Label label;

    ObservableList<ClientView> clients = FXCollections.observableArrayList();
    @FXML
    private Label idValue;

    public void initTabClient() throws IOException {

        JFXTreeTableColumn<ClientView,String> nameID = new JFXTreeTableColumn<>("Nom");
        nameID.setPrefWidth(150);
        nameID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClientView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClientView, String> param) {
                return param.getValue().getValue().name;
            }
        });
        JFXTreeTableColumn<ClientView,String> villeID = new JFXTreeTableColumn<>("Ville");
        villeID.setPrefWidth(150);
        villeID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClientView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClientView, String> param) {
                return param.getValue().getValue().ville;
            }
        });
        JFXTreeTableColumn<ClientView,String> mailID = new JFXTreeTableColumn<>("@ Mail");
        mailID.setPrefWidth(150);
        mailID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClientView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClientView, String> param) {
                return param.getValue().getValue().mail;
            }
        });
        JFXTreeTableColumn<ClientView,String> telID = new JFXTreeTableColumn<>("Téléphone");
        telID.setPrefWidth(150);
        telID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClientView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClientView, String> param) {
                return param.getValue().getValue().tel;
            }
        });
        JFXTreeTableColumn<ClientView,String> adrID = new JFXTreeTableColumn<>("Adresse");
        adrID.setPrefWidth(150);
        adrID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ClientView, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ClientView, String> param) {
                return param.getValue().getValue().adresse;
            }
        });

        clients.clear();
        Database connect = new Database();

        ApiFuture<QuerySnapshot> futureClient = connect.db.collection("client").get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = futureClient.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        int i=0;
        for (QueryDocumentSnapshot document : documents) {
            i++;
            String id = document.getId();
            String adresse = document.getString("adresse");
            String cp = document.getString("cp");
            String ville = document.getString("ville");
            String tel = document.getString("tel");
            String mail = document.getString("email");

            clients.add(new ClientView(id,adresse,cp,ville,tel,mail));
        }
        final TreeItem<ClientView> root = new RecursiveTreeItem<>(clients, RecursiveTreeObject::getChildren);
        clientTable.getColumns().setAll(nameID,villeID, mailID, telID,adrID);
        clientTable.setRoot(root);
        clientTable.setShowRoot(false);

        showClientDetails(null);

        idValue.setText("Votre base de données contient : "+String.valueOf(i)+" Clients");


        clientTable.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> showClientDetails(newValue)));

        idInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                clientTable.setPredicate(new Predicate<TreeItem<ClientView>>() {
                    @Override
                    public boolean test(TreeItem<ClientView> clientViewTreeItem) {
                        Boolean flag = clientViewTreeItem.getValue().name.getValue().contains(newValue)||
                                clientViewTreeItem.getValue().ville.getValue().contains(newValue)||
                                clientViewTreeItem.getValue().tel.getValue().contains(newValue)||
                                clientViewTreeItem.getValue().mail.getValue().contains(newValue)||
                                clientViewTreeItem.getValue().tel.getValue().contains(newValue)||
                                clientViewTreeItem.getValue().cp.getValue().contains(newValue);
                        return flag;
                    }
                });
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initTabClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showClientDetails(TreeItem<ClientView> client){
        if (client != null){
            nameLabel.setText(client.getValue().getName());
            adresseLabel.setText(client.getValue().getAdresse());
            cpLabel.setText(client.getValue().getCp());
            villeLabel.setText(client.getValue().getVille());
            telLabel.setText(client.getValue().getTel());
            mailLabel.setText(client.getValue().getMail());
        }else{
            nameLabel.setText("");
            adresseLabel.setText("");
            cpLabel.setText("");
            villeLabel.setText("");
            telLabel.setText("");
            mailLabel.setText("");
        }
    }

    @FXML
    private void actionDeleteClient(ActionEvent actionEvent) throws IOException {
        if (clientTable.getSelectionModel().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucune Selection");
            alert.setHeaderText("Vous n'avez selectionné aucun client");
            alert.setContentText("Merci de selectionner un client avant de supprimer.");
            alert.showAndWait();
        }
        else{
            String name = clientTable.getSelectionModel().getSelectedItem().getValue().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer le client "+ name+" ?");
            alert.setHeaderText("Vous êtes sur le point de supprimer le client "+name+" ainsi que tous les équipements qui lui sont associés.");
            alert.setContentText("Confirmez vous cette suppression irréversible ?");
            Optional<ButtonType> option = alert.showAndWait();



            if (option.get() == ButtonType.OK) {

                Database delClient = new Database();
                delClient.db.collection("client").document(name).delete();

            } else if (option.get() == ButtonType.CANCEL) {

            }
        }

    }



    @FXML
    private void actionVoirEquipement(ActionEvent actionEvent) {
        if (clientTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Aucune Selection");
            alert.setHeaderText("Vous n'avez selectionné aucun client");
            alert.setContentText("Merci de selectionner un client avant de voir leur équipement.");
            alert.showAndWait();
        }else{
            TreeItem<ClientView> selectedClient = clientTable.getSelectionModel().getSelectedItem();
            System.out.println(selectedClient.getValue().getName());
            app.showEquipementView(selectedClient);

        }
    }

    @FXML
    private void actionEditerClient(ActionEvent actionEvent) throws IOException {
        TreeItem<ClientView> selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null){
            boolean okCliked = app.showClientEditDialog(selectedClient);
            if (okCliked) {
                showClientDetails(selectedClient);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("Auncune selection");
            alert.setHeaderText("Aucun client selectionné");
            alert.setContentText("Merci de selectionner un client avant d'éditer.");
            alert.showAndWait();
        }
    }

    @FXML
    public void actionRefresh(ActionEvent actionEvent) throws IOException {
        initTabClient();
    }

    public void setMainApp(App app){
        this.app = app;
    }

    class ClientView extends RecursiveTreeObject<ClientView> {

        StringProperty name;
        StringProperty adresse;
        StringProperty cp;
        StringProperty ville;
        StringProperty tel;
        StringProperty mail;


        public ClientView (String name, String adresse, String cp, String ville, String tel, String mail){
            this.name = new SimpleStringProperty(name);
            this.adresse = new SimpleStringProperty(adresse);
            this.cp = new SimpleStringProperty(cp);
            this.ville = new SimpleStringProperty(ville);
            this.tel = new SimpleStringProperty(tel);
            this.mail = new SimpleStringProperty(mail);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getAdresse() {
            return adresse.get();
        }

        public StringProperty adresseProperty() {
            return adresse;
        }

        public void setAdresse(String adresse) {
            this.adresse.set(adresse);
        }

        public String getCp() {
            return cp.get();
        }

        public StringProperty cpProperty() {
            return cp;
        }

        public void setCp(String cp) {
            this.cp.set(cp);
        }

        public String getVille() {
            return ville.get();
        }

        public StringProperty villeProperty() {
            return ville;
        }

        public void setVille(String ville) {
            this.ville.set(ville);
        }

        public String getTel() {
            return tel.get();
        }

        public StringProperty telProperty() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel.set(tel);
        }

        public String getMail() {
            return mail.get();
        }

        public StringProperty mailProperty() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail.set(mail);
        }
    }


}
