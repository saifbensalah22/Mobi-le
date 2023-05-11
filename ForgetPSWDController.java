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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.UserService;
import service.FullUserValidation;
public class ForgetPSWDController implements Initializable {

    @FXML
    private TextField tf_userotp;
    @FXML
    private Button btn_valide;
    @FXML
    private TextField userCode;
    @FXML
    private Text btn_ref;
    FullUserValidation ref;
String value;
User user;
int code_verif;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userCode.setVisible(false);
        // TODO
    }    

    @FXML
    private void Resetpassword(ActionEvent event) throws IOException {
        if(tf_userotp.getText().toString().equals(value)){
            
                    UserService  sr = new UserService ();
                    sr.insert(user);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Bienvenue Notre nouveau utilisateur !"+user.getNom()+" "+user.getprénom());
        alert.showAndWait();
        
                Parent page1 = FXMLLoader.load(getClass().getResource("../view/signIn.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }else {
            Parent page1 = FXMLLoader.load(getClass().getResource("../view/inscription.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
  
        }
            
    }

    @FXML
    private void getUserOTP(MouseEvent event) {
        Node node = (Node) event.getSource();
          Stage stage = (Stage) node.getScene().getWindow();
          ref= (FullUserValidation) stage.getUserData();
          code_verif=ref.getUserVerifCode();
          value=String.valueOf(code_verif);
          userCode.setText(value);
          user=ref.getUser();
    }
    
}
