package controller;
import entity.Session;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import service.FullUserValidation;
import service.SendVerificationEmail;
import service.UserService;
import service.FullUserValidation;

public class SignInController implements Initializable {

    @FXML
    private Button brn_conn;
    @FXML
    private JFXTextField tf_mail;
    @FXML
    private JFXPasswordField tf_pswd;
    @FXML
    private Hyperlink link;
      @FXML
    private ImageView btn_back;
      
      public String ref_mail,usernom,userprenom,useremail,userpswd,useradresse,userimage,userrole;
      public int userid,usertlfn,usergrad,userOPTref;
      public Connection cnx;
      public PreparedStatement pst ;
      User userref;
      public Random random;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancisymfonyfinal?user=root&password=");
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
       @FXML
    private Label btn_getcode;

        public void Random (){
        random = new Random();
    }
    @FXML
    public void getCode(MouseEvent event) throws MessagingException, IOException, SQLException {
        ref_mail = tf_mail.getText();
        User user = getUser(ref_mail);
        System.out.println("user email ::"+ref_mail);
        if(ref_mail.isEmpty() || ! ref_mail.contains("@gmail.com")){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre mail");
        alert.showAndWait();
        tf_mail.requestFocus();
        }
        else {
            Random();
        userOPTref =random.nextInt(10000+1);
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
        sendVerificationEmail.envoyerCodePassword(ref_mail, userOPTref);
        FullUserValidation fullUserValidation = new FullUserValidation(user, userOPTref);
        Parent page1 = FXMLLoader.load(getClass().getResource("../view/restPasswordInterface.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setUserData(fullUserValidation);
                stage.show();
         
        }

    }

    
    @FXML
    void seConnecter(ActionEvent event) throws SQLException, IOException {
        
    String email = tf_mail.getText();
    String password = tf_pswd.getText();
    try{
  
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/freelancisymfonyfinal?user=root&password=");
    //PreparedStatement stmt = conn.prepareStatement("SELECT nom, prénom FROM user WHERE mail=? AND password=?");
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE mail=? AND password=?");
     
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        
        // Vérifier si la requête a renvoyé un enregistrement
        if (rs.next()) {
            
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String mail = rs.getString("mail");
            String pwd = rs.getString("password");
            int tlf = rs.getInt("numero_telephone");
            String role = rs.getString("role");
            int grad = rs.getInt("grad");
            String adresse = rs.getString("addresse");
            String image = rs.getString("image");
            
            // Les informations de connexion sont valides, afficher un message de confirmation
     
            String message = "Bonjour " + prenom + " " + nom + ", vous êtes connecté(e) !\n";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.showAndWait();
            UserService ps=new UserService();
            User utilisateur = ps.getUtilisateur(email, password); 
            String role_user = rs.getString("role");
         
                  
            // Stockage des informations de connexion dans la variable de session
            Session session = Session.getInstance();
            session.setId_user(id);
            session.setNom(nom);
            session.setPrénom(prenom);
            session.setMail(mail);
            session.setPassword(pwd);
            session.setNuméroTéléphone(tlf);
            session.setRole(role);
            session.setGrad(grad);
            session.setAdresse(adresse);
            session.setImage(image);
            
               if( role_user.equals("simple user") ){
                  // Ouverture de la page de profil de l'utilisateur
               FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserProfil.fxml"));
                Parent root = loader.load();
                UserProfilController profileController = loader.getController();
             //   profileController.setUser(utilisateur);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
    
                }  else {
                   Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            
               }

           
            } else {
            // Les informations de connexion sont invalides, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR, "Email ou mot de passe invalide !");
            alert.showAndWait();
            
            }
         
        // Fermer les ressources utilisées
        rs.close();
        stmt.close();
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
         // Afficher un message d'erreur en cas de problème avec la base de données
        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur de connexion à la base de données !");
        alert.showAndWait();
    }


    }

    @FXML
    private void goToInscrire(ActionEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/inscription.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

     
        
    }
    @FXML
    void goBack(MouseEvent event) throws IOException {
     Parent page1 = FXMLLoader.load(getClass().getResource( "../view/pageAcceuil.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
     private User getUser(String mail) throws SQLException {
        pst = cnx.prepareStatement("select * from user where mail=?");
        pst.setString(1, mail);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
             userid=rs.getInt("id");
             usertlfn=rs.getInt("numero_telephone");
             usernom=rs.getString("nom");
             userprenom=rs.getString("prenom");
             useremail=rs.getString("mail");
             userpswd=rs.getString("password");
             useradresse=rs.getString("addresse");
             userrole=rs.getString("role");
             usergrad=rs.getInt("grad");
             userimage=rs.getString("image");
           // User user = new User (nom,prenom,email,pswd, tlfn ,adresse);
             userref= new User(userid,usernom,userprenom,useremail,userpswd,usertlfn,userrole,usergrad,useradresse,userimage);
        }
            return userref;

    }
    
    
}
