package fr.gotech;

import fr.gotech.database.Database;
import fr.gotech.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientEditController implements Initializable {
    @FXML
    private TextField idNom;
    @FXML
    private TextField idAdresse;
    @FXML
    private TextField idCP;
    @FXML
    private TextField idVille;
    @FXML
    private TextField idTel;
    @FXML
    private TextField idMail;

    private Stage dialogStage;
    private TreeItem<ClientViewController.ClientView> clientView;
    private boolean okCliked = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setClient(TreeItem<ClientViewController.ClientView> clientView){
        this.clientView = clientView;

        idNom.setText(clientView.getValue().getName());
        idNom.setDisable(true);
        idAdresse.setText(clientView.getValue().getAdresse());
        idCP.setText(clientView.getValue().getCp());
        idVille.setText(clientView.getValue().getVille());
        idTel.setText(clientView.getValue().getTel());
        idMail.setText(clientView.getValue().getMail());
    }

    public boolean isOkCliked(){
        return okCliked;
    }

    @FXML
    private void actionValider(ActionEvent actionEvent) throws IOException {
        if (isInputValid()){
            String nameClient = clientView.getValue().getName().toString();
            Database connect = new Database();
            Client editClient = new Client();
            editClient.setAdresse(idAdresse.getText());
            editClient.setCp(idCP.getText());
            editClient.setVille(idVille.getText());
            editClient.setEmail(idMail.getText());
            editClient.setTel((idTel.getText()));
            connect.db.collection("client").document(nameClient).set(editClient);
            okCliked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        return true;
    }

    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
