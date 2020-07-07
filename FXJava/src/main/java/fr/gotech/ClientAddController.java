package fr.gotech;

import com.jfoenix.controls.JFXButton;
import fr.gotech.database.Database;
import fr.gotech.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ClientAddController implements Initializable {


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
    @FXML
    private JFXButton idAjouter;
    @FXML
    private JFXButton idAnnuler;

    static boolean isValidMail(String mail){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return mail.matches(regex);
    }


    private Stage dialogStage;
    private App app;
    private boolean okCliked = false;

    public boolean isOkCliked(){

        return true;

    }
    public void setMainApp(App app){

        this.app = app;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    @FXML
    private void actionAjouter(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

    }

    private boolean isInputValid() {
        String errorMessage ="";

        if (idNom.getText() == null || idNom.getText().length() == 0) {
            errorMessage += "Nom de l'entreprise non valide ! \n";
        }
        if (idAdresse.getText() == null || idAdresse.getText().length() == 0) {
            errorMessage += "Adresse de l'entreprise non valide ! \n";
        }
        if (idCP.getText() == null || idCP.getText().length() == 0) {
            errorMessage += "Code postale de l'entreprise non valide ! \n";
        } else {
            try {
                Integer.parseInt(idCP.getText());
            }catch (NumberFormatException e){
                errorMessage += "Le code postale ne doit contenir que des chiffres! \n";
            }
        }
        if (idVille.getText() == null || idVille.getText().length() == 0) {
            errorMessage += "La ville n'est pas valide ! \n";
        }
        if (idTel.getText() == null || idTel.getText().length() == 0) {
            errorMessage += "Le numéro de téléphone n'est pas valide \n";
        }
        if (! (isValidMail(idMail.getText()))) {
            errorMessage += "L'adresse mail n'est pas valide ! \n";
        }
        if (errorMessage.length() == 0){
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Les champs ne sont pas valide. ");
            alert.setHeaderText("Merci de corriger les informations suivantes : ");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    private void actionAnnuler(ActionEvent actionEvent) {
        dialogStage.close();
    }


    @FXML
    private void actionValider(ActionEvent actionEvent) throws IOException {
        if (isInputValid()) {

            String nameClient = idNom.getText().toUpperCase();
            Database connect = new Database();
            Client addClient = new Client();

            addClient.adresse=idAdresse.getText();
            addClient.cp =idCP.getText();
            addClient.ville=idVille.getText().toUpperCase();
            addClient.email=idMail.getText().toLowerCase();
            addClient.tel=idTel.getText();
            connect.db.collection("client").document(nameClient).set(addClient);
            okCliked = true;
            dialogStage.close();
        }
    }
}
