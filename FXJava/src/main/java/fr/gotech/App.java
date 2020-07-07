package fr.gotech;

import fr.gotech.model.Interface;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

/**
 * JavaFX App
 */

public class App extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Application d'inventaire");

        initRootLayout();
        showClientOverview();
        showLeftMenu();
    }

    public void initRootLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("main.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            rootLayout.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showClientOverview() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("client_view.fxml"));
            AnchorPane clientView = (AnchorPane) loader.load();

            rootLayout.setCenter(clientView);

            ClientViewController clientViewController = loader.getController();
            clientViewController.setMainApp(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showLeftMenu() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("leftmenu.fxml"));
            AnchorPane leftMenu = (AnchorPane) loader.load();

            rootLayout.setLeft(leftMenu);

            LeftMenuController leftMenuController = loader.getController();
            leftMenuController.setMainApp(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showEquipementView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("equipement_view.fxml"));
            AnchorPane equipementView = (AnchorPane) loader.load();

            rootLayout.setCenter(equipementView);
            EquipementViewController equipementViewController = loader.getController();
            equipementViewController.setMainApp(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showEquipementView(TreeItem<ClientViewController.ClientView> clientViewTreeItem){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("equipement_view.fxml"));
            AnchorPane equipementView = (AnchorPane) loader.load();

            rootLayout.setCenter(equipementView);
            EquipementViewController equipementViewController = loader.getController();
            equipementViewController.initTab(clientViewTreeItem.getValue().getName());
            equipementViewController.setMainApp(this);
        }catch (IOException | ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean showClientAdd(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((App.class.getResource("client-add.fxml")));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un client");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the client into the controller
            ClientAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog
            dialogStage.showAndWait();

            return controller.isOkCliked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean showEquipementAdd(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((App.class.getResource("equipement_add.fxml")));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un équipement");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the client into the controller
            EquipementAddController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog
            dialogStage.showAndWait();

            return controller.isOkCliked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean showInterfaceAdd(TreeItem<EquipementViewController.EquipementView> equipementView, String clientID){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((App.class.getResource("interface_add.fxml")));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une interface");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            // Set the client into the controller
            InterfaceAddController controller = loader.getController();
            controller.getEquipementClient(equipementView, clientID);
            controller.setDialogStage(dialogStage);

            // Show the dialog
            dialogStage.showAndWait();
            return controller.isOkCliked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean showClientEditDialog(TreeItem<ClientViewController.ClientView> clientView){
        try{
            //Load the FXML file and create a new stage for the popup dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("client_edit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier un Client");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the client into the controller
            ClientEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setClient(clientView);

            // Show the dialog
            dialogStage.showAndWait();

            return controller.isOkCliked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean showEquipementEditDialog( TreeItem<EquipementViewController.EquipementView> equipementView, String clientID){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("equipement_edit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modification d'un équipement");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            dialogStage.setScene(scene);

            EquipementEditController controller = loader.getController();
            controller.setEquipement(equipementView,clientID);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.isOkCliked();
        }catch (IOException | ParseException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean showInterfaceEditDialog(ObservableList<Interface> interfaces, TreeItem selectedEquipement, String clientID) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("interface_edit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modification d'une ou plusieurs interfaces");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("css/stylesheet.css").toExternalForm());
            dialogStage.setScene(scene);

            InterfaceEditController controller = loader.getController();
            controller.setInterfaces(interfaces,selectedEquipement,clientID);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.isOkCliked();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }


    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}


