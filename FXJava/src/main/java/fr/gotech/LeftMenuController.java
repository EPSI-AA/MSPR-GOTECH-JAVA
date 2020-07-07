package fr.gotech;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeftMenuController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    @FXML
    private void actionAddClient(ActionEvent actionEvent) {
        app.showClientAdd();

    }

    @FXML
    private void actionAddEquipement(ActionEvent actionEvent) {
        app.showEquipementAdd();
    }

    @FXML
    private void actionClientView(ActionEvent actionEvent) {
        app.showClientOverview();
    }


    @FXML
    private void actionEquipementView(ActionEvent actionEvent) {
        app.showEquipementView();

    }
}
