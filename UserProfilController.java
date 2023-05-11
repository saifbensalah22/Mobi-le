/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Session;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;
import source.DataSource;



/**
 * FXML Controller class
 *
 * @author ounis
 */


public class UserProfilController implements Initializable {

        
    @FXML
    private TextField p_nom;

    @FXML
    private TextField p_prenom;

    @FXML
    private TextField p_mail;

    @FXML
    private TextField p_tlf;

    @FXML
    private TextField p_adresse;
    
     @FXML
    private TextField p_pwd;
    
     @FXML
    private Button btn;
     
      @FXML
    private Button btn_img;
      
       @FXML
    private ImageView user_image_profil;
       
     @FXML
     private Label name ;
     
     @FXML
     private Label homepage ;
     @FXML
    private Label mes_demandes;
     @FXML
    private Label mes_affaires;
      @FXML
    private TextField t_com;

    @FXML
    private TextField t_ed;

    @FXML
    private TextField t_exp;

    @FXML
    private Button ajoutCV;
    
    @FXML
    private Button affichCv;
    
    @FXML
    private Label btn_cv;

     
     Connection cnx;
     PreparedStatement ste;
     
      File selectedFile ;
     int p_id_user,user_id,user_phone,user_grad;
     String umail,upwd,user_fname,user_lname,user_mail,user_password,user_addresse,user_role,user_image ,user_url,url;
     
  
  
    PreparedStatement pst;
    ResultSet rs;
    ResultSet r;
    FileChooser filechooser;

    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
        
        cnx=DataSource.getInstance().getCnx();
        try {
            setUser();
        } catch (SQLException ex) {
            Logger.getLogger(UserProfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

       
    }
    
    
      @FXML
    public void goToCv(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserCv.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
    
     public void setUser() throws SQLException {
    // Initialiser les champs de texte avec les informations de l'utilisateur.
 
     Session session = Session.getInstance();
     int userId = session.getId_user();
     String nom = session.getNom();
     String prenom = session.getPrénom();
     String mail = session.getMail();
     int tlf = session.getNuméroTéléphone();
     String adresse = session.getAdresse();
     String pwd = session.getPassword();
     
    p_nom.setText(nom);
    p_prenom.setText(prenom);
    p_mail.setText(mail);
    p_tlf.setText(Integer.toString(tlf));
    p_adresse.setText(adresse);
    p_pwd.setText(pwd);
    name.setText(nom+" "+prenom);
    

    pst = cnx.prepareStatement("select * from user where id=?");
    pst.setInt(1, userId);
    rs = pst.executeQuery();
         while(rs.next()){
            user_url=rs.getString("image");
            if(user_url != null){
               user_image_profil.setImage(new Image(user_url));
            }
          else{
          File file1= new File("C:/Users/ounis/Desktop/freelanci/freelanci/src/image/user.png");
          Image image = new Image(file1.toURI().toString());
          user_image_profil.setImage(image);
          }   
     }} 
     
      @FXML
    void ModifierUser(ActionEvent event) throws IOException {
        
        UserService uss = new UserService();
        List<User> users = uss.readAll();
        User u ;
        
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setHeaderText("Warning");
       alert.setContentText("Es-tu sûre de modifier?!");
       
       String nom=p_nom.getText();
       String prenom=p_prenom.getText();
       String mail=p_mail.getText();
       String password=p_pwd.getText();
       String addresse=p_adresse.getText();
       int phone=Integer.parseInt( p_tlf.getText());
       
       Session session = Session.getInstance();
       String url = session.getImage();
        
       
       
       
        if (nom.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre nom");
        alert1.showAndWait();
        p_nom.requestFocus();
        } else if (prenom.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre prenom");
        alert1.showAndWait();
        p_prenom.requestFocus();
        } else if (mail.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre mail");
        alert1.showAndWait();
        p_mail.requestFocus();
        
        } else if (User.checkIfEmailExists(mail, users)){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("ce mail existe déjà");
        alert.showAndWait();
        p_mail.requestFocus();
        }
 
        else if (password.isEmpty() || password.length()<8){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre mot de passe");
        alert1.showAndWait();
        p_pwd.requestFocus();
        } else if (addresse.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre addresse");
        alert1.showAndWait();
        p_adresse.requestFocus();
        } else if ( p_tlf.getText().toString().length() < 8 || p_tlf.getText().toString().length() > 8 ){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre numéro de téléphone");
        alert.showAndWait();
        p_tlf.requestFocus();
        }
        else {
            if(url != null) {
            Optional<ButtonType>result =  alert.showAndWait(); 
            if(result.get() == ButtonType.OK){ 
            User user= new User(session.getId_user(),nom,prenom,mail,password,phone,session.getRole(),session.getGrad(),session.getAdresse(),session.getImage());
            UserService us = new UserService();
            us.update(user);
            //refresh();
         
    }
        }else {
                Optional<ButtonType>result =  alert.showAndWait(); 
            if(result.get() == ButtonType.OK){ 
             User user= new User(session.getId_user(),nom,prenom,mail,password,phone,session.getRole(),session.getGrad(),session.getAdresse(),user_url);
            UserService us = new UserService();
            us.update(user);
                System.out.println("modification effectué");
            }
            
            }

    }
    
}
    
    @FXML
    private void goToDemande(MouseEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/UserDemande.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    } 
    
    @FXML
    private void goToAffaires(MouseEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/userAffaire.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    } 
    @FXML
    void goToHome(MouseEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }

    @FXML
    void uploadImage(ActionEvent event) throws SQLException {
        
     Session session = Session.getInstance();

        FileChooser fc = new FileChooser();
        fc.setTitle("Open File");
        selectedFile = fc.showOpenDialog(null);
        url = selectedFile.toURI().toString();
        if(selectedFile != null) {
            user_image_profil.setImage(new Image(url));
                      
           UserService us = new UserService();
           User utilisateur = us.getUtilisateur(session.getMail(), session.getPassword());
           utilisateur.setImage(url);
           us.update(utilisateur);
           
            System.out.println("image dans path ="+url);
            
    }

}

}
