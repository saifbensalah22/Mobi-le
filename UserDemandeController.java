/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Demande;
import entity.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.DemandeService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class UserDemandeController implements Initializable {

    @FXML
    private Label back;
    @FXML
   
    private TableView<Demande> tab_projet;
    @FXML
    private TableColumn<Demande, String> col_titre;
    @FXML
    private TableColumn<Demande, String> col_description;
    @FXML
    private TableColumn<Demande, String> col_competence;
    @FXML
    private TableColumn<Demande, String> col_etat;
    @FXML
    private TableColumn<Demande, String> col_datelimite;
    @FXML
    private TableColumn<Demande, Integer> col_freelance;
    @FXML
    private TableColumn<Demande, Integer> col_idDemande; 
    @FXML
    private Button btn_ajout;
    @FXML
    private Button btn_modif;
    @FXML
    private Button btn_delete;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane interface_adddemande;
    @FXML
    private Button valider;
    @FXML
    private DatePicker datelim;
    @FXML
    private TextField champ_budget;
    @FXML
    private TextField champ_competance;
    @FXML
    private TextField champ_description;
    @FXML
    private TextField champ_titre;
    @FXML
    private AnchorPane interface_selecteduser;
    @FXML
    private Label L_nom;
    @FXML
    private Label L_mail;
    @FXML
    private Label L_telephone;
    @FXML
    private Label L_address;
    
    Connection mc;
    int selectedFreelance,connectedClient ,selectedProject;
    String selectedEtat;
    PreparedStatement ste;
    ObservableList<Demande> prolist;
    Demande clicked;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Session session = Session.getInstance();
                      connectedClient = session.getId_user();   
        btn_modif.setVisible(false);
        btn_delete.setVisible(false);
          interface_adddemande.setVisible(false);
       interface_selecteduser.setVisible(false);
        afficher();
        
        // TODO
    }    
    private void afficher() {
        
                    
        prolist = FXCollections.observableArrayList();
        
        try {
        mc = DataSource.getInstance().getCnx();
            String requete = "SELECT * FROM demande where id_client_id=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, connectedClient);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Demande demande = new Demande();
                demande.setTitre(rs.getString("titre"));
                demande.setDescription(rs.getString("description"));
                demande.setCompetence(rs.getString("competence"));
                demande.setEtat(rs.getString("etat"));
                demande.setDatelim(rs.getDate("date_limite"));            
                demande.setDatecreation(rs.getDate("date_creation"));  
                demande.setId_client(rs.getInt("id_client_id"));
                demande.setId_freelance(rs.getInt("id_freelance"));
                demande.setId_projet(rs.getInt("id"));
                System.out.println("les projets ajoutees :" + demande.toString());
                prolist.add(demande);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        col_titre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));
        col_description.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        col_competence.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCompetence()));
        col_etat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEtat()));
        col_datelimite.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDatelim()));
        col_freelance.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_freelance()));
        col_idDemande.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_projet()));
        tab_projet.setItems(prolist);
        search();
        //refresh();
    }
   

    @FXML
    private void goBack(MouseEvent event) throws IOException {
                 Parent path = FXMLLoader.load(getClass().getResource("../view/UserProfil.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }

    @FXML
    private void selected(MouseEvent event) {
        
        clicked = tab_projet.getSelectionModel().getSelectedItem();
        selectedFreelance=clicked.getId_freelance();
        selectedProject=clicked.getId_projet();
        selectedEtat=clicked.getEtat();
        btn_modif.setVisible(true);
        btn_delete.setVisible(true);

        
    }

    @FXML
    private void goToAdd(ActionEvent event) throws IOException {
          Parent path = FXMLLoader.load(getClass().getResource("../view/CreateOffreFXML.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    @FXML
    private void delete(ActionEvent event) {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Es-tu sûre de supprimer!");
           
        Optional<ButtonType>result =  alert.showAndWait(); 
        if(result.get() == ButtonType.OK){ 
                DemandeService us = new DemandeService();
             us.delete(clicked); ;
        refresh();
        btn_modif.setVisible(false);
        btn_delete.setVisible(false);
        
        }
        
    }
    @FXML
    private void goToSet(ActionEvent event) throws IOException {
       interface_adddemande.setVisible(true);
       interface_selecteduser.setVisible(true);
           Connection cnx;
             cnx=DataSource.getInstance().getCnx();
             ResultSet rs;
        try {
             PreparedStatement pst;
             pst = cnx.prepareStatement("select * from user where id=?");
             pst.setInt(1, selectedFreelance);
             rs = pst.executeQuery();
         while(rs.next()){
         L_nom.setText(rs.getString("nom")+" "+rs.getString("prenom"));
         L_mail.setText(rs.getString("mail"));
         L_address.setText(rs.getString("addresse"));
         L_telephone.setText(rs.getString("numero_telephone"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    void setDemande(ActionEvent event) throws SQLException {
        String testtitre = champ_titre.getText();
        String testbudg = champ_budget.getText();
        String testcomp = champ_competance.getText();
        String testdesc = champ_description.getText();
        LocalDate testDate = datelim.getValue();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (testtitre.isEmpty()) {
            champ_titre.setText("choisis une titre");
        } else if (testbudg.isEmpty()) {
            champ_budget.setText("met une budget");
        } else if (testcomp.isEmpty()) {
            champ_competance.setText("met la competance");
        } else if (testdesc.isEmpty()) {
            champ_description.setText("met une description");
        /*}else if (datelim.getValue()== null) {
            datelim.requestFocus();*/
        } else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                   
                    Demande demande =new Demande(selectedProject,testtitre,testdesc,Float.parseFloat(testbudg),testcomp,selectedEtat,connectedClient,selectedFreelance);
                    DemandeService ds = new DemandeService();
                    ds.update(demande);
                    System.out.println(demande);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(UserSearchProjetController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        btn_modif.setVisible(false);
        btn_delete.setVisible(false);
        interface_adddemande.setVisible(false);
        interface_selecteduser.setVisible(false);
        refresh();
     }
 public void refresh() {
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();
        try {       
            String requete = "SELECT * FROM demande where id_client_id=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, connectedClient);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                Demande demande = new Demande();
                demande.setTitre(rs.getString("titre"));
                demande.setDescription(rs.getString("description"));
                demande.setCompetence(rs.getString("competence"));
                demande.setEtat(rs.getString("etat"));
                demande.setDatelim(rs.getDate("date_limite"));            
                demande.setDatecreation(rs.getDate("date_creation"));             
                demande.setId_freelance(rs.getInt("id_freelance"));
                demande.setId_client(rs.getInt("id_client_id"));
                demande.setId_projet(rs.getInt("id"));
                System.out.println("les projets ajoutees :" + demande.toString());
                prolist.add(demande);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        tab_projet.setItems(prolist);
search();
    }
private void search(){
        FilteredList<Demande>filteredData = new FilteredList<>(prolist, b->true);
            searchField.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(Demande->{
                    if(newValue == null || newValue.isEmpty()){
                    return true;
                }
               
                String lowerCaseFilter = newValue.toLowerCase();
                 
                  if(String.valueOf(Demande.getTitre()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                  else if(String.valueOf(Demande.getDescription()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                return false;
                }
            });          
        });
        SortedList<Demande>sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab_projet.comparatorProperty());
        tab_projet.setItems(sortedData);
    }


}
