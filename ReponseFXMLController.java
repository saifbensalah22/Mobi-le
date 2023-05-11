/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import API.SendSMS;
import service.ReponseService;
import entity.Reclamation;
import entity.Reponse;
import java.io.IOException;
import static java.lang.Boolean.parseBoolean;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import service.ReclamationService;
import source.DataSource;


/**
 * FXML Controller class
 *
 * @author Saif
 */
public class ReponseFXMLController implements Initializable {
    
     @FXML
    private Label btn_retour;
Reponse clicked;
    @FXML
    private TextArea text_description;

    @FXML
    private TextArea text_traitement;
    @FXML
    private Label L_arriere;

    @FXML
    private Label L_date;

    @FXML
    private Label L_description;

    @FXML
    private Label L_phone;

    @FXML
    private Label L_to;
    
    @FXML
    private Label L_from;
    
    @FXML
    private AnchorPane interface_details;
    @FXML
    private Pane interface_addReponse;
@FXML
    private TableView<Reponse> tableRep;
    @FXML
    private TableColumn<Reponse, Integer> idRep;
    @FXML
    private TableColumn<Reponse, Integer> idRec;
    @FXML
    private TableColumn<Reponse, String> traitRep;
        @FXML
    private TableColumn<Reponse, String> C_description;
    @FXML
    private Button btn_detail;
    @FXML
    private Button supprimerRep;
    @FXML
    private Button modifierRep;
    @FXML
    private Button fermerRep;
    @FXML
    private Text btn_save;
    
    
     ObservableList<Reponse>repList;
    
    Connection mc;
    PreparedStatement ste;
    

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        interface_addReponse.setVisible(false);
        interface_details.setVisible(false);
        mc=DataSource.getInstance().getCnx();
        fermerRep.setVisible(false);
        btn_detail.setVisible(false);
        supprimerRep.setVisible(false);
        modifierRep.setVisible(false);
        afficherReponses();
}
        
       
      @FXML
    public void retourRec(MouseEvent event) throws IOException {
        
        Parent path = FXMLLoader.load(getClass().getResource("../view/Reclamation.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    } 
      @FXML
    public void arriere(MouseEvent event) throws IOException {
               interface_details.setVisible(false);
               fermerRep.setVisible(false);
        btn_detail.setVisible(false);
        supprimerRep.setVisible(false);
        modifierRep.setVisible(false);

    }      
    
    
       void afficherReponses()
      {
          mc=DataSource.getInstance().getCnx();
          repList = FXCollections.observableArrayList();
   
        
        try {
            String requete = "SELECT * FROM reponse_reclamation r ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Reponse r = new Reponse();
                r.setId_Reponse(rs.getInt("id"));
                r.setId_reclamation(rs.getInt("id_reclamation_id"));
                r.setTraitement(rs.getString("traitement"));
                r.setContenu_reponse(rs.getString("contenu_reponse"));
                repList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        idRep.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_Reponse()));
        idRec.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_reclamation()));
        traitRep.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTraitement()));
        C_description.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getContenu_reponse()));
        
        tableRep.setItems(repList);
     
      
      
      }
    @FXML
    private void selected(MouseEvent event) {
         clicked = tableRep.getSelectionModel().getSelectedItem();
        fermerRep.setVisible(true);
        btn_detail.setVisible(true);
        supprimerRep.setVisible(true);
        modifierRep.setVisible(true);

       
    }
    @FXML
    private void updateRep(MouseEvent event) {
        interface_addReponse.setVisible(true);
        }
    
    @FXML
    void Save(MouseEvent event) {
        String traitement=text_traitement.getText();
        String description=text_description.getText();
        Reponse reponse = new Reponse();
        reponse.setId_Reponse(clicked.getId_Reponse());
        reponse.setId_reclamation(clicked.getId_reclamation());
        reponse.setTraitement(traitement);
        reponse.setContenu_reponse(description);
        ReponseService rs = new ReponseService();
        rs.modifierReponseRec(reponse);
        refresh();
        interface_addReponse.setVisible(false);
    fermerRep.setVisible(false);
        btn_detail.setVisible(false);
        supprimerRep.setVisible(false);
        modifierRep.setVisible(false);
    }
    @FXML
    void showDetails(MouseEvent event) {
        interface_details.setVisible(true);
        ReclamationService rs = new ReclamationService();
        Reclamation reclamation = new Reclamation();
        reclamation=rs.readById(clicked.getId_reclamation());
        L_from.setText(reclamation.getEmail());
        L_to.setText(reclamation.getReportEmail());
        L_date.setText(reclamation.getDate().toString());
        L_phone.setText(reclamation.getTelephone());
        L_description.setText(reclamation.getDescription());
    
    }  
    
    
      public void refresh(){
        
         repList.clear();
       
          
          mc=DataSource.getInstance().getCnx();

        repList = FXCollections.observableArrayList();
        
        String sql="select * from reponse_reclamation";
        try {
            ste=mc.prepareStatement(sql);
            ResultSet rs=ste.executeQuery();
            while(rs.next()){
                    Reponse r = new Reponse();
                r.setId_Reponse(rs.getInt("id"));
                r.setId_reclamation(rs.getInt("id_reclamation_id"));
                r.setTraitement(rs.getString("traitement"));
                r.setContenu_reponse(rs.getString("contenu_reponse"));
                repList.add(r);  
   }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         tableRep.setItems(repList);   
         
    }



    @FXML
    private void deleteRep(MouseEvent event) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Es-tu sûre de supprimer!");
        
          
        Optional<ButtonType>result =  alert.showAndWait(); 
        if(result.get() == ButtonType.OK){ 
         Reponse reponse = new Reponse();
         reponse.setId_Reponse(clicked.getId_Reponse());
         reponse.setId_reclamation(clicked.getId_reclamation());
         reponse.setTraitement(clicked.getTraitement());
         reponse.setContenu_reponse(clicked.getContenu_reponse());
         ReponseService rc = new ReponseService();
          rc.supprimerReponseRec(reponse);
             refresh();
              
        }
        
    }


    @FXML
    private void close(MouseEvent event) {
        
        Stage stage = (Stage) fermerRep.getScene().getWindow();
    stage.close();
    }
}
