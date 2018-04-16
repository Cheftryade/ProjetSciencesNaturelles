package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WelcomeController {

    private static String nom;
    @FXML
    private TextField fieldTime;

    @FXML
    private TextField fieldName;

    private static double temps = 0;
    @FXML
    private TextField fieldTimeinterval;
    
     private static double itervalTemps = 0;

    public void dialog(String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Une erreur est survenue");
        alert.setHeaderText("Saisie invalide !");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        if(fieldName.getText().isEmpty()){
            dialog("Veuillez indiquer votre nom !");
            return;
        }
        nom=fieldName.getText();
        try{
            temps = Double.parseDouble(fieldTime.getText());
        }catch(NumberFormatException nfe){
            dialog("Veuillez indiquer une valeur num�rique dans le champs, et non autre chose");
            return;
        }
        try{
            itervalTemps = Double.parseDouble(fieldTimeinterval.getText());
        }catch(NumberFormatException nfe){
            dialog("Veuillez indiquer une valeur num�rique dans le champs, et non autre chose");
            return;
        }
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

public static double getIntervalTemps(){
        return itervalTemps;
    }
public static String getName(){
        return nom;
    }
}
