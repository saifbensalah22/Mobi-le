package controller;
import API.SendSMS;
import entity.Reclamation;
import entity.Session;
import entity.User;
import static entity.User.checkIfEmailExists;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ReclamationService;
import service.UserService;
public class ReclamationUserController implements Initializable {
    @FXML
    private TextArea pUserRec;


    @FXML
    private TextArea telRec;

    @FXML
    private TextArea descRec;

    @FXML
    private Button ajouterRec;
    
    Reclamation r = new Reclamation();
    
    
    public static boolean isValidDate(String dateString) {
    String[] dateParts = dateString.split("/");
    if (dateParts.length != 3) {
      return false;
    }
    
    int day;
    int month;
    int year;
    try {
      day = Integer.parseInt(dateParts[0]);
      month = Integer.parseInt(dateParts[1]);
      year = Integer.parseInt(dateParts[2]);
    } catch (NumberFormatException e) {
      return false;
    }
    
    if (month < 1 || month > 12) {
      return false;
    }
    if (year <2023){
        return false;}
    
    int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   
    int maxDays = daysInMonth[month - 1];
    if (month == 2 && isLeapYear(year)) {
      maxDays = 29;
    }
    
    if (day < 1 || day > maxDays) {
      return false;
    }
    
    return true;
  }
  
  public static boolean isLeapYear(int year) {
    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
      return true;
    }
    return false;
  }
    @FXML
    private TextArea recherche;
    @FXML
    private Button pdfbutton;
  
     List <User> utilisateurs;
  
  public boolean isPhoneNumberValid(String phoneNumber) {
    // Expression régulière pour un numéro de téléphone de 8 chiffres (sans les indicateurs de pays)
    String regex = "^[0-9]{8}$";
    return phoneNumber.matches(regex);
}


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
     @FXML
    public void addRec(MouseEvent event) throws SQLException, IOException {
        
     // List <User> utilisateurs = new ArrayList<>();
         UserService us = new UserService();
         utilisateurs = us.readAll();
        Session session = Session.getInstance();
        String mail = session.getMail();
        String puser = pUserRec.getText(); // bch te5ou text mawjoud f label w thotou f variable
        //String date = dateRec.getText();
        String telephone = telRec.getText();
        String description = descRec.getText();
      
        if ( puser.isEmpty()||  telephone.isEmpty() || description.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("Veuiller Remplir Les champs!!"); // controle de saisie
             alert.showAndWait();          
         
        
       /* }else if (isValidDate(date)==false)
         {
         Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("Date non valide"); // controle de saisie
             alert.showAndWait();*/
         } 
         else if(isPhoneNumberValid(telephone)==false){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("Veuiller Saisir un nombre de telephone valide!"); // controle de saisie
             alert.showAndWait();    
         
         }else if(checkIfEmailExists(puser ,utilisateurs)){
               r.setReportEmail(puser);                   
               r.setEmail(mail);
               r.setDescription(description);
               r.setTelephone(telephone);
                ReclamationService rc = new ReclamationService();
                rc.ajouterReclamation(r);
                SendSMS sm = new SendSMS();
                //sm.sendSMS(r);
        
            
                pUserRec.setText(null);
                //dateRec.setText(null);
                telRec.setText(null);
                descRec.setText(null);
                Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
         }
               
  
         else{
             

             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("ce mail n'existe pas"); 
             alert.showAndWait();

        }
        

    }
    
}
