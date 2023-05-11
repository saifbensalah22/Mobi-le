/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.CV;
import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import service.CvUserService;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author ounis
 */
public class AjouterCvController implements Initializable {
    
        @FXML
    private TextField t_com;

    @FXML
    private TextField t_ed;

    @FXML
    private TextField t_exp;

    @FXML
    private Button ajoutCV;
    
    User u ;

    @FXML
    void ajouterCV(ActionEvent event) {
        
        
        
         String com=t_com.getText();
        String ed=t_ed.getText();
        String exp=t_exp.getText();
        
         CV user = new CV (com,ed, exp );
        CvUserService  sr = new CvUserService ();
        sr.insert(user);
       
        

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
