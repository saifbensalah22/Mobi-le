/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import entity.Commentaire;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import service.CommentaireService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author rayen
 */
public class CommentaireController implements Initializable {

    @FXML
    private TextField champ_iddem;
    @FXML
    private TextField champ_contenue;
    @FXML
    private TextField champ_iduser;
    @FXML
    private Button btn_ajout;
    @FXML
    private TableView<Commentaire> tab_com;
    @FXML
    private TableColumn<Commentaire, Integer> tab_idcom;
    @FXML
    private TableColumn<Commentaire, Integer> tab_iddem;
    @FXML
    private TableColumn<Commentaire, Integer> tab_iduser;
    @FXML
    private TableColumn<Commentaire, String> tab_idcontenu;

    /**
     * Initializes the controller class.
     */
    Connection mc;
    PreparedStatement ste;
    ObservableList<Commentaire> pcomlist;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        afficher();
    }    
    @FXML
    private void afficher() {

        mc = DataSource.getInstance().getCnx();
        pcomlist = FXCollections.observableArrayList();

        try {
            String requete = "select * from commentaire e";
            Statement st;
            st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Commentaire e;
                e = new Commentaire();
                e.setId_comm(rs.getInt("id_comm"));

                e.setContenue(rs.getString("contenue"));

                e.setId_demande(rs.getInt("id_demande"));
                
                e.setId_user(rs.getInt("id_user"));
                
                

                System.out.println("les projets ajoutees :" + e.toString());
                pcomlist.add(e);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tab_idcom.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_comm()));
        tab_iddem.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_demande()));
        tab_iduser.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_user()));
        tab_idcontenu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContenue()));
        
        
        

        tab_com.setItems(pcomlist);
        refresh();
    }
    public void refresh() {
        pcomlist.clear();
        mc = DataSource.getInstance().getCnx();
        pcomlist = FXCollections.observableArrayList();

        try {
            String requete = "select * from commentaire e";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Commentaire e;
                e = new Commentaire();
                e.setId_comm(rs.getInt("id_comm"));

                e.setContenue(rs.getString("contenue"));

                e.setId_demande(rs.getInt("id_demande"));
                
                e.setId_user(rs.getInt("id_user"));
                
                

                System.out.println("les projets ajoutees :" + e.toString());
                pcomlist.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

       tab_com.setItems(pcomlist);

    }

    @FXML
    private void checkadd() {

        String testid = "1";
        String testusr = champ_iduser.getText();
        String testcont = champ_contenue.getText();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (testusr.isEmpty()) {
            champ_iduser.setText("choisis un user");
        }  else if (testcont.isEmpty()) {
            champ_contenue.setText("met de contenue");
        } else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {

                    Commentaire r1 = new Commentaire(champ_contenue.getText(), Integer.parseInt(champ_iddem.getText()), Integer.parseInt(champ_iduser.getText()));
                    CommentaireService rs = new CommentaireService();
                    rs.insert(r1);
                    

                } catch (NumberFormatException ex) {
                    Logger.getLogger(CommentaireController.class.getName()).log(Level.SEVERE, null, ex);
                }
                refresh();
            }

        }

    }

    
    
}
