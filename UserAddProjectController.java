/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class UserAddProjectController implements Initializable {

    @FXML
    private TextField champ_titre;
    @FXML
    private TextField champ_description;
    @FXML
    private TextField champ_competance;
    @FXML
    private TextField champ_budget;
    @FXML
    private Button valider;
    @FXML
    private DatePicker datelim;
    @FXML
    private Label btn_back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void checkadd(ActionEvent event) {
    }

    @FXML
    private void goBack(MouseEvent event) {
    }
    
}
