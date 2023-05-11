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
public class AdminInterfaceController implements Initializable {
   
    @FXML
    private Button btn_gestion_user;
    @FXML
    private Button btn_gestion_events;
    @FXML
    private Button btn_gestion_cours;
    @FXML
    private Button btn_gestion_reclamation;
    @FXML
    private Button btn_gestion_projets;
    @FXML
    private Button btn_gestion_demande;
      @FXML
    private Label btn_logout;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    @FXML
    private void goToGererUser(ActionEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/setUser.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    
    @FXML
    public void goToGererEvent(ActionEvent event) throws IOException {
        
         Parent path = FXMLLoader.load(getClass().getResource("../view/EvenementFXML.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    // 
     @FXML
    public void goToGererCours(ActionEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/AddCours.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
     @FXML
    public void goToGererReclamation(ActionEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/Reclamation.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        
    }
     @FXML
    public void goToGererProjets(ActionEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/userSearchProjet.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
     @FXML
    public void goToGererDemande(ActionEvent event) throws IOException {
             Parent path = FXMLLoader.load(getClass().getResource("../view/adminDemande.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    @FXML
    public void goToUserProfil(ActionEvent event) {
    }
    @FXML
    public void LogOut(MouseEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/pageAcceuil.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
}
