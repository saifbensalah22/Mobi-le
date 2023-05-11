package controller;
import entity.User;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.UserService;
import service.FullUserValidation;
public class RestPasswordInterfaceController implements Initializable {
    @FXML
    private TextField getcode;
    @FXML
    private Label btn_getcode;
    @FXML
    private Button rbtn_verif_code;
    @FXML
    private TextField newpassword;
    @FXML
    private TextField confirmpassword;
    @FXML
    private TextField entercode;
    @FXML
    private Button btn_savenewpassword;
    public FullUserValidation fullUserValidation ;
    public String value;
    public int ref;
    public User user,setUser;
    public String password1,password2;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newpassword.setVisible(false);
        confirmpassword.setVisible(false);
        btn_savenewpassword.setVisible(false);
        btn_getcode.setText("Get code");
        getcode.setVisible(false);
        // TODO
    }    

    @FXML
    private void getCode(MouseEvent event) {
        Node node = (Node) event.getSource();
          Stage stage = (Stage) node.getScene().getWindow();
          fullUserValidation= (FullUserValidation) stage.getUserData();
          ref=fullUserValidation.getUserVerifCode();
          value=String.valueOf(ref);
          getcode.setText(value);
          user=fullUserValidation.getUser();
    }

    @FXML
    private void btn_verif_code(ActionEvent event) throws IOException {
        if(getcode.getText().toString().equals(value)){
        newpassword.setVisible(true);
        confirmpassword.setVisible(true);
        btn_savenewpassword.setVisible(true);
        }else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre code");
        alert.showAndWait();   
        Parent page1 = FXMLLoader.load(getClass().getResource("../view/singIn.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }
    }

    @FXML
    private void saveNewPassword(ActionEvent event) throws IOException {
        password1 = newpassword.getText().toString();
        password2 = confirmpassword.getText().toString();
        if ((password1.isEmpty() || password1.length()<8) ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre mot de passe");
        alert.showAndWait();
        newpassword.requestFocus();
        }else if ((password2.isEmpty() || password2.length()<8) ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez votre confirmation de  mot de passe");
        alert.showAndWait();
        confirmpassword.requestFocus();
        }else if(!password1.equals(password2)){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("verifiez de nouveau la confirmation de votre mot de passe");
        alert.showAndWait();
        confirmpassword.requestFocus();
        }else {
            //updateuser
            
            setUser= new User(user.getId_user(),user.getNom(),user.getprénom(),user.getMail(),password1,user.getNuméroTéléphone(),user.getRole(),user.getGrad(),user.getAdresse(),user.getImage());
            UserService us = new UserService();
            us.update(setUser);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Votre mot de passe à été changer avec succée");
            alert.showAndWait();
            Parent page1 = FXMLLoader.load(getClass().getResource( "../view/signIn.fxml"));
           Scene scene = new Scene(page1);
           Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           stage.setScene(scene);
           stage.show();
        }
        
    }
    
}
