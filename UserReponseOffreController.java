/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Demande;
import entity.ReponseOffre;
import entity.Session;
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
import service.ReponseOffreService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class UserReponseOffreController implements Initializable {

    @FXML
    private Label back;
    @FXML
    private TableView<ReponseOffre> tab_projet;
    @FXML
    private TableColumn<ReponseOffre, Integer> col_idReponse;
    @FXML
    private TableColumn<ReponseOffre, Integer> col_idDemande;
    @FXML
    private TableColumn<ReponseOffre, String> col_reponseOffre;
    @FXML
    private Button btn_supprimer;
    @FXML
    private Button btn_details;
    @FXML
    private TextField searchField;
    @FXML
    private Button btn_modifier;
     @FXML
    private Label L_competence;

    @FXML
    private Label L_date_creation;

    @FXML
    private Label L_date_limite;

    @FXML
    private Label L_description;

    @FXML
    private Label L_prix;

    @FXML
    private Label L_titre;
    @FXML
    private AnchorPane interface_details;

    Connection mc;
    int selectedFreelance,selectedReponse,SelectedDemande,connectedClient ,selectedProject;
    String selectedEtat;
    PreparedStatement ste;
    ObservableList<ReponseOffre> prolist;
    ReponseOffre clicked;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               Session session = Session.getInstance();
                      connectedClient = session.getId_user();  
        interface_details.setVisible(false);
        btn_details.setVisible(false);
        btn_modifier.setVisible(false);
        btn_supprimer.setVisible(false);
       
        afficher();
        // TODO
        // TODO
    }    

    @FXML
    private void goBack(MouseEvent event) throws IOException {
             Parent path = FXMLLoader.load(getClass().getResource("../view/userAffaire.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
               
    }
    private void afficher() {
        prolist = FXCollections.observableArrayList();
        try {
        mc = DataSource.getInstance().getCnx();
            String requete = "SELECT * FROM demande_offre where id_freelance=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, connectedClient);
            ResultSet rs = statement.executeQuery();
    while (rs.next()) {
                ReponseOffre reponse = new ReponseOffre();
                reponse.setId_reponse(rs.getInt("id"));
                reponse.setId_demande(rs.getInt("id_demande_id"));
                reponse.setId_freelance(rs.getInt("id_freelance"));
                reponse.setReponse_offre(rs.getString("reponse_demande"));
                prolist.add(reponse);

            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        col_idReponse.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_reponse()));
        col_idDemande.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_demande()));
        col_reponseOffre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReponse_offre()));
        tab_projet.setItems(prolist);
     refresh();
    }
public void refresh() {
        prolist.clear();
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();
       

       try {
        mc = DataSource.getInstance().getCnx();
            String requete = "SELECT * FROM demande_offre where id_freelance=? ";
            PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
            statement.setInt(1, connectedClient);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ReponseOffre reponse = new ReponseOffre();
                reponse.setId_reponse(rs.getInt("id"));
                reponse.setId_demande(rs.getInt("id_demande_id"));
                reponse.setId_freelance(rs.getInt("id_freelance"));
                reponse.setReponse_offre(rs.getString("reponse_demande"));
                prolist.add(reponse);

            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        tab_projet.setItems(prolist);
search();
    }
private void search(){
        FilteredList<ReponseOffre>filteredData = new FilteredList<>(prolist, b->true);
            searchField.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(ReponseOffre->{
                    if(newValue == null || newValue.isEmpty()){
                    return true;
                }
               
                String lowerCaseFilter = newValue.toLowerCase();
                 
                  if(String.valueOf(ReponseOffre.getReponse_offre()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                return false;
                }
            });          
        });
        SortedList<ReponseOffre>sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab_projet.comparatorProperty());
        tab_projet.setItems(sortedData);
    }

    @FXML
    private void selected(MouseEvent event) {
        clicked = tab_projet.getSelectionModel().getSelectedItem();
        selectedReponse=clicked.getId_reponse();
        SelectedDemande=clicked.getId_demande();
        btn_modifier.setVisible(true);
        btn_supprimer.setVisible(true);
        btn_details.setVisible(true);
       
    }
    @FXML
     void retour(MouseEvent event) {
        interface_details.setVisible(false);
        btn_details.setVisible(false);
        btn_modifier.setVisible(false);
        btn_supprimer.setVisible(false);
       

    }
     @FXML
   private void delete(ActionEvent event) {
        ReponseOffreService ros = new ReponseOffreService();
        ros.delete(clicked);
        DemandeService ds = new DemandeService();
        Demande demande = new Demande();
        demande=ds.readById(SelectedDemande);
        demande.setEtat("non étudier");
        ds.update(demande);
        refresh();
    }
@FXML
    private void modifier(ActionEvent event) {
        DemandeService ds = new DemandeService();
        ReponseOffreService ros = new ReponseOffreService();
        Demande demande = new Demande();
        ReponseOffre reponseOffre=new ReponseOffre();
        demande=ds.readById(SelectedDemande);
        reponseOffre= ros.readById(selectedReponse);
        if(reponseOffre.getReponse_offre().equals("Accepter")){
            reponseOffre.setReponse_offre("Refuser");
            demande.setEtat("Refuser");
            ros.update(reponseOffre);
            ds.update(demande);
            refresh();
        }else{
            reponseOffre.setReponse_offre("Accepter");
            demande.setEtat("Accepter");
            ros.update(reponseOffre);
            ds.update(demande);
            refresh();
        }
           
    }

    @FXML
    private void goToDetails(ActionEvent event) {
        interface_details.setVisible(true);  
        btn_modifier.setVisible(true);
        btn_supprimer.setVisible(true);
        DemandeService ds = new DemandeService();
        Demande demande = new Demande();
        demande=ds.readById(SelectedDemande);
        L_titre.setText(demande.getTitre());
        L_description.setText(demande.getDescription());
        L_competence.setText(demande.getCompetence());
        L_prix.setText(Float.toString(demande.getPrix()));
        L_date_limite.setText(demande.getDatelim().toString());
        L_date_creation.setText(demande.getDatecreation().toString());    
    }
   
}
