package controller;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import service.UserService;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import service.FullUserValidation;
import service.SendVerificationEmail;
public class InscriptionController implements Initializable {
        Stage stage = new Stage();
    @FXML
    private TextField tf_pswd;
    @FXML
    private TextField tf_mail;
    @FXML
    private TextField tf_prenom;
    @FXML
    private TextField tf_nom;
    @FXML
    private TextField tf_adress;
    @FXML
    private TextField tf_tlfn;
    @FXML
    private Button btn ;
    
    @FXML
    public int userOPTref;
    private ImageView btn_back;
    public Random random;
    
    public void Random(){
         random = new Random();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        } 
    @FXML
    public void inscrire(ActionEvent event) throws IOException, MessagingException {
        Random();
        userOPTref =random.nextInt(10000+1);
        UserService us = new UserService();
        List<User> users = us.readAll();
        User user ;
        
        String fname=tf_nom.getText();
        String lname=tf_prenom.getText();
        String mail=tf_mail.getText();
        String pswd=tf_pswd.getText();
        int tlfn = Integer.parseInt(tf_tlfn.getText());
        String ref = String.valueOf(tlfn) ;
        String addresse=tf_adress.getText();
      
        if (fname.isEmpty()){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre nom");
        alert.showAndWait();
        tf_nom.requestFocus();
        }
        else if (lname.isEmpty()){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre prenom");
        alert.showAndWait();
        tf_prenom.requestFocus();
        }
        else if (mail.isEmpty() || ! mail.contains("@")){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre mail");
        alert.showAndWait();
        tf_mail.requestFocus();
        }
        else if (User.checkIfEmailExists(mail, users)){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("ce mail existe déjà");
        alert.showAndWait();
        tf_prenom.requestFocus();
       
        }
        else if ((pswd.isEmpty() || pswd.length()<8) ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre mot de passe");
        alert.showAndWait();
        tf_pswd.requestFocus();
        }
     
        else if ( tf_tlfn.getText().toString().length() < 8 || tf_tlfn.getText().toString().length() > 8 ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre telephone");
        alert.showAndWait();
        tf_tlfn.requestFocus();
        }
        else if (addresse.isEmpty()  ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre addresse");
        alert.showAndWait();
        tf_adress.requestFocus();
        }
        else { 
         User u = new User (fname,lname,mail,pswd, tlfn ,addresse);
        FullUserValidation fullUserValidation = new FullUserValidation(u,userOPTref);
            SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
            sendVerificationEmail.envoyer(mail, userOPTref);
            // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Bienvenue Notre nouveau utilisateur !"+fname+" "+lname);
        alert.showAndWait();
        // aller vers page de connexion
        Parent page1 = FXMLLoader.load(getClass().getResource("../view/forgetPSWD.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setUserData(fullUserValidation);
                stage.show();
      }
    }
    
    @FXML
    void goBack(MouseEvent event) throws IOException {
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/SignIn.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
      
        
              
                  

}