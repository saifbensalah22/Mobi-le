/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Demande;
import entity.Session;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.DemandeService;
import service.UserService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class AdminDemandeController implements Initializable {
        int id_user;
        User user;
    
    @FXML
    private Label L_address;

    @FXML
    private Label L_arriere;

    @FXML
    private Label L_competence;

    @FXML
    private Label L_date_creation;

    @FXML
    private Label L_date_limite;

    @FXML
    private Label L_description;

    @FXML
    private Label L_mail;

    @FXML
    private Label L_nom;

    @FXML
    private Label L_prix;

    @FXML
    private Label L_telephone;

    @FXML
    private Label L_titre;

    @FXML
    private Label back;

    @FXML
    private Label btn_Reponse;

    @FXML
    private Button btn_details;

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
    private AnchorPane interface_details;

    @FXML
    private AnchorPane interface_selecteduser;

    @FXML
    private TextField searchField;

    Connection mc;
    int selectedFreelance,connectedClient ,selectedProject;
    String selectedEtat;
    PreparedStatement ste;
    ObservableList<Demande> prolist;
    Demande clicked;
    Session session ;
    User client,freelance;
    String client_fullname,freelance_fullname,client_email;
   

    @FXML
    private Label L_nomclient;
    @FXML
    private Label L_mailclient;
    @FXML
    private Label L_telephoneclient;
    @FXML
    private Label L_addressclient;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_details.setVisible(false);
        interface_selecteduser.setVisible(false);
        interface_details.setVisible(false);
        L_arriere.setVisible(false);
        afficher();
        // TODO
    }    
    @FXML
    private void goBack(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                
    }
     @FXML
    private void arriere(MouseEvent event) {
 interface_selecteduser.setVisible(false);
       interface_details.setVisible(false);
       L_arriere.setVisible(false);
       btn_details.setVisible(false);
    }
    private void afficher() {
        
                    
        
        prolist = FXCollections.observableArrayList();
        
        try {
        mc = DataSource.getInstance().getCnx();
            String requete = "SELECT * FROM demande ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = statement.executeQuery(requete);

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
     refresh();
    }
public void refresh() {
        prolist.clear();
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();
        

        try {
            String requete = "SELECT * FROM demande ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            ResultSet rs = statement.executeQuery(requete);

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
    

        
    
  @FXML
    private void selected(MouseEvent event) {

        clicked = tab_projet.getSelectionModel().getSelectedItem();
        selectedFreelance=clicked.getId_freelance();
        selectedProject=clicked.getId_projet();
        selectedEtat=clicked.getEtat();
        btn_details.setVisible(true);
    }
     @FXML
     private void goToDetails(ActionEvent event) {
       interface_selecteduser.setVisible(true);
       interface_details.setVisible(true);
       L_arriere.setVisible(true);
       client= getClients(selectedProject);
       freelance= getFreelance(selectedProject);
       freelance_fullname=freelance.getNom()+"  "+freelance.getprénom();
       L_nom.setText(freelance_fullname);
       L_mail.setText(freelance.getMail());
       L_address.setText(freelance.getAdresse());
       L_telephone.setText(Integer.toString( freelance.getNuméroTéléphone()));
       ///////////////////////////////////////////////////////////////////
       client_fullname=client.getNom()+"  "+client.getprénom();
       L_nomclient.setText(client_fullname);
       L_mailclient.setText(client.getMail());
       L_addressclient.setText(client.getAdresse());
       L_telephoneclient.setText(Integer.toString( client.getNuméroTéléphone()));
       DemandeService ds = new DemandeService();
       Demande demande = new Demande();
       demande=ds.readById(selectedProject);
       L_titre.setText(demande.getTitre());
       L_description.setText(demande.getDescription());
       L_competence.setText(demande.getCompetence());
       L_prix.setText(Float.toString(demande.getPrix()));
       L_date_limite.setText(demande.getDatelim().toString());   
       L_date_creation.setText(demande.getDatecreation().toString());    
        
    }
    @FXML
    private void goToReponse(MouseEvent event) throws IOException {
            Parent path = FXMLLoader.load(getClass().getResource("../view/adminReponse.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    private User getClients(int selectedProject) {
        
        try {
            String requete = "SELECT * FROM demande where id=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, selectedProject);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                id_user=rs.getInt("id_client_id");
                System.out.println("dans la methode de gettclient dans details l id de clients ="+id_user);
                
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        UserService us =new UserService();
        user=us.readById(id_user);
        DemandeService ds = new DemandeService();
        Demande demande = new Demande();
        demande=ds.readById(selectedProject);
       
        
        
        return user;
    }
    private User getFreelance(int selectedProject) {
        
        try {
            String requete = "SELECT * FROM demande where id=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, selectedProject);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                id_user=rs.getInt("id_freelance");
                System.out.println("dans la methode de gettclient dans details l id de freelance ="+id_user);
                
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        UserService us =new UserService();
        user=us.readById(id_user);
        DemandeService ds = new DemandeService();
        Demande demande = new Demande();
        demande=ds.readById(selectedProject);
       
        
        
        return user;
    }

    
}
