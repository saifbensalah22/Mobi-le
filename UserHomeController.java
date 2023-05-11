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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ounis
 */
public class UserHomeController implements Initializable {
    
     @FXML
    private Button out;
     @FXML
    private Button btn_cours;
     @FXML
    private Label profil;
         @FXML
    private Label create_offre;

     @FXML
    private Label chercherF;
       @FXML
    private Label chercherProjet;
         @FXML
    private Label btn_addprojet; 
     @FXML
    private Label rec;
  

    @FXML
    public void logOut(ActionEvent event) {

    }
    @FXML
    public void goToCours(ActionEvent event) throws IOException {
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/ajouterCours.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    
    @FXML
    private Label event;
     @FXML
    void goToEvent(MouseEvent event) throws IOException {
        
          Parent page1 = FXMLLoader.load(getClass().getResource( "../view/EvenementFXML.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
     @FXML
    void createOffre(MouseEvent event) throws IOException {
        
          Parent page1 = FXMLLoader.load(getClass().getResource( "../view/CreateOffreFXML.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    
    @FXML
    public void goToRec(MouseEvent event) throws IOException {
        
         Parent page1 = FXMLLoader.load(getClass().getResource( "../view/ReclamationUser.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    
     @FXML
    public void chercherProjet(MouseEvent event) throws IOException {
        
         Parent page1 = FXMLLoader.load(getClass().getResource( "../view/affichprojet.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
     @FXML
    public void addprojet(MouseEvent event) throws IOException {
        
         Parent page1 = FXMLLoader.load(getClass().getResource( "../view/userSearchProjet.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    
    @FXML
    void chercherFreelance(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/listDesFreelances.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
      

   
    @FXML
    void goToProfil(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserProfil.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
    }    
    
    
}
