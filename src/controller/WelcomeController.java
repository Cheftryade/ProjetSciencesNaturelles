package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WelcomeController {

    /*Récupération des éléments placés sur l'interface graphique sous forme FXML*/
    @FXML
    private TextField fieldTime;

    @FXML
    private TextField fieldName;

    private static double temps = 0;

    /*Méthode créant une page d'erreur avec le message donné en paramètre*/
    public void dialog(String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Une erreur est survenue");
        alert.setHeaderText("Saisie invalide !");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML protected void handleSubmitButtonAction(ActionEvent event) {

        /*Vérification de la cohérence des informations de l'utilisateur*/
        if(fieldName.getText().isEmpty()){
            dialog("Veuillez indiquer votre nom !");
            return;
        }
        try{
            temps = Double.parseDouble(fieldTime.getText());
        }catch(NumberFormatException nfe){
            dialog("Veuillez indiquer une valeur numérique dans le champs, et non autre chose");
            return;
        }

        /*Création du nouveau Stage -> vue principale*/
        Stage stage = new Stage();
        stage.setTitle("TP Sciences Nat Nom : " + fieldName.getText());
        Pane myPane = null;
        try {
            myPane = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
        stage.show();

        Node  source = (Node)  event.getSource();
        Stage stage2  = (Stage) source.getScene().getWindow();
        stage2.close();
    }

    public static double getTemps(){
        return temps;
    }


}
