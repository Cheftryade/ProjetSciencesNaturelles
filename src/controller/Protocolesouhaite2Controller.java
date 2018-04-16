/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ak47@minduos
 */
public class Protocolesouhaite2Controller implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
@FXML protected void handleSubmitButtonAction(ActionEvent event) {
        Stage stage = new Stage();
        //stage.setTitle("TP Sciences Nat Nom : " + fieldName.getText());
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
    
}
