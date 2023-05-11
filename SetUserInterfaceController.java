package controller;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;
import source.DataSource;
public class SetUserInterfaceController implements Initializable {
    @FXML
    private ImageView user_image_profil;
    @FXML
    private ImageView btn_back;
    @FXML
    private ImageView btnbtn;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_bloquer;
    @FXML
    private Button btn_modifier;
    @FXML
    private TextField tfnom;
    @FXML
    private TextField tfmail;
    @FXML
    private TextField tfpswd;
    @FXML
    private TextField tfphone;
    @FXML
    private TextField tfaddresse;
    @FXML
    private TextField tfprenom;
    File selectedFile ;
    Connection cnx;
    PreparedStatement pst;
    User ref;
    ResultSet rs;
    int user_id,user_phone,user_grad;
    String user_fname,user_lname,user_mail,user_password,user_addresse,user_role,user_image ,user_url,url;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cnx=DataSource.getInstance().getCnx();

       
    }  
    @FXML
    private void DeleteUser(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Es-tu sûre de supprimer!");
           
        Optional<ButtonType>result =  alert.showAndWait(); 
        if(result.get() == ButtonType.OK){ 
                UserService us = new UserService();
             us.delete(ref); ;
            // refresh();
        }
         tfnom.setText(null);
        tfmail.setText(null);
        tfpswd.setText(null);
        tfaddresse.setText(null);
        tfphone.setText(null);
    
    }
    @FXML
    private void ModifierUser(ActionEvent event) throws IOException {
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setHeaderText("Warning");
       alert.setContentText("Es-tu sûre de modifier?!");
       String nom=tfnom.getText();
       String prenom=tfprenom.getText();
       String mail=tfmail.getText();
       String password=tfpswd.getText();
       String addresse=tfaddresse.getText();
       int phone=Integer.parseInt( tfphone.getText());
        if (nom.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre nom");
        alert1.showAndWait();
        tfnom.requestFocus();
        } else if (prenom.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre prenom");
        alert1.showAndWait();
        tfprenom.requestFocus();
        } else if (mail.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre mail");
        alert1.showAndWait();
        tfmail.requestFocus();
        } else if (password.isEmpty() || password.length()<8){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre password");
        alert1.showAndWait();
        tfpswd.requestFocus();
        } else if (addresse.isEmpty()){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Attention");
        alert1.setHeaderText(null);
        alert1.setContentText("verifiez votre addresse");
        alert1.showAndWait();
        tfaddresse.requestFocus();
        } else if ( tfphone.getText().toString().length() < 8 || tfphone.getText().toString().length() > 8 ){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre telephone");
        alert.showAndWait();
        tfphone.requestFocus();
        }
        else {
            if(url != null) {
            Optional<ButtonType>result =  alert.showAndWait(); 
            if(result.get() == ButtonType.OK){ 
            User user= new User(user_id,nom,prenom,mail,password,phone,user_role,user_grad,addresse,url);
            UserService us = new UserService();
            us.update(user);
          Parent page1 = FXMLLoader.load(getClass().getResource( "../view/gererUser.fxml"));
          Scene scene = new Scene(page1);
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          stage.setScene(scene);
          stage.show();
    }
        }else {
                Optional<ButtonType>result =  alert.showAndWait(); 
            if(result.get() == ButtonType.OK){ 
            User user= new User(user_id,nom,prenom,mail,password,phone,user_role,user_grad,addresse,user_url);
            UserService us = new UserService();
            us.update(user);
            //refresh();
          
         // user_image_profil.setImage(new Image(user_url));
          Parent page1 = FXMLLoader.load(getClass().getResource( "../view/gererUser.fxml"));
          Scene scene = new Scene(page1);
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          stage.setScene(scene);
          stage.show();
                ////////////////////////////////
                
        
       }
    }
        }
       
    }
    
    @FXML
    private void goBack(MouseEvent event) throws IOException {
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/gererUser.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML 
    private void uploadImage (MouseEvent event){
         FileChooser fc = new FileChooser();
        fc.setTitle("Open File");
        selectedFile = fc.showOpenDialog(null);
        url = selectedFile.toURI().toString();
        if(selectedFile != null) {
            user_image_profil.setImage(new Image(url));
        }
        
    }
    @FXML
    private void AfficherUser(ActionEvent event) throws SQLException, IOException {
        Node node = (Node) event.getSource();
          Stage stage = (Stage) node.getScene().getWindow();
          ref= (User) stage.getUserData();
        //System.out.println("id user "+ref);
          RecupereUser(ref);
    }
    
    private void RecupereUser(User ref) throws SQLException, IOException{
            pst = cnx.prepareStatement("select * from user where id=?");
            pst.setInt(1, ref.getId_user());
            rs = pst.executeQuery();
           // System.out.println("id user "+ref.getId_user());
            while(rs.next()){
          user_id = rs.getInt("id");
          user_phone = rs.getInt("numero_telephone");
          user_grad = rs.getInt("grad");
          user_fname=rs.getString("nom");
          user_lname=rs.getString("prenom");
          user_mail=rs.getString("mail");
          user_password=rs.getString("password");
          user_addresse=rs.getString("addresse");
          user_role=rs.getString("role");
          user_url=rs.getString("image");
          //Affection vers les champs
          tfnom.setText(user_fname);
          tfprenom.setText(user_lname);
          tfmail.setText(user_mail);
          tfpswd.setText(user_password);
          tfaddresse.setText(user_addresse);
          tfphone.setText(String.valueOf(user_phone));
          if(user_url != null){
              
                        user_image_profil.setImage(new Image(user_url));
          }
          else{
          File file1= new File("images.png");
          Image image = new Image(file1.toURI().toString());
          user_image_profil.setImage(image);
         
}
         }
    }
}
