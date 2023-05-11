/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Projet;
import entity.Session;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import service.SendVerificationEmail;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class affichProjetController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortBox = new ComboBox<>();
    @FXML
    private TableView<Projet> tab_projet;
    @FXML
    private TableColumn<Projet, Integer> tab_idproj;
    @FXML
    private TableColumn<Projet, String> tab_titre;
    @FXML
    private TableColumn<Projet, String> tab_categ;
    @FXML
    private TableColumn<Projet, Integer> tab_budget;
    @FXML
    private TableColumn<Projet, Integer> col_iduser;
    @FXML
    private TableColumn<Projet, String> tab_comp;
    @FXML
    private TableColumn<Projet, String> tab_desc;
    @FXML
    private TableColumn<Projet, String> tab_date;
    @FXML
    private Button btn_stat;
    @FXML
    private Button btn_ajout;
    @FXML
    private Button btn_offre;
    public User targetUser;
    /**
     * Initializes the controller class.
     */
    Connection mc;
    PreparedStatement ste;
    ObservableList<Projet> prolist;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_offre.setVisible(false);
        col_iduser.setVisible(false);
        ObservableList<String> items = FXCollections.observableArrayList(
                "ID",
                "Titre",                
                "Budget"
                
        );
        sortBox.setItems(items);
        // TODO
         searchField.textProperty().addListener((observable, oldValue, newValue) -> search());
        sortBox.setOnAction(event -> sort());
        
        afficher();
        btn_ajout.setOnAction(event -> {

            try {
                Parent page1 = FXMLLoader.load(getClass().getResource("../view/userSearchProjet.fxml"));
                Scene scene = new Scene(page1);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(affichProjetController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }    
    private void search() {
    String query = searchField.getText();
    ObservableList<Projet> filteredList = FXCollections.observableArrayList();
    for (Projet projet : prolist) {
        if (projet.getTitre().toLowerCase().contains(query.toLowerCase())) {
            filteredList.add(projet);
        }
    }
    tab_projet.setItems(filteredList);
}
    private void sort() {
    String selectedOption = sortBox.getValue();
    if (selectedOption == null) {
        return;
    }
    switch (selectedOption) {
        case "ID":
            prolist.sort((p1, p2) -> Integer.compare(p1.getId_projet(), p2.getId_projet()));
            break;
        case "Titre":
            prolist.sort((p1, p2) -> p1.getTitre().compareToIgnoreCase(p2.getTitre()));
            break;
        case "Prix":
            prolist.sort((p1, p2) -> Float.compare(p1.getPrix(), p2.getPrix()));
            break;
        default:
            break;
    }
    tab_projet.setItems(prolist);
}
    private void afficher() {

        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();
        
        try {
            String requete = "select * from projet e";
            Statement st;
            st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Projet e;
                e = new Projet();
               
                
                e.setId_projet(rs.getInt("id_projet"));
                
                e.setId_user_id(rs.getInt("setId_user_id"));
                e.setTitre(rs.getString("titre"));

                e.setDescription(rs.getString("description"));

                e.setPrix(rs.getInt("prix"));

                e.setCompetences(rs.getString("competence"));
                
                e.setCategorie(rs.getString("categorie"));
                
                e.setDatelim(rs.getString("datelim"));
                
                System.out.println("les projets ajoutees :" + e.toString());
                prolist.add(e);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tab_idproj.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_projet()));
        tab_titre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));
        tab_categ.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie()));
        tab_budget.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getPrix()));
        col_iduser.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_user_id()));
        tab_comp.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCompetences()));
        tab_desc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        tab_date.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatelim()));
        
        tab_projet.setItems(prolist);
        refresh();
    }
    
    @FXML
    private void selected(MouseEvent event) throws SQLException {
        
        Projet clicked = tab_projet.getSelectionModel().getSelectedItem();
        int id_user=Integer.parseInt(String.valueOf(clicked.getId_user_id()));
        btn_offre.setVisible(true);
        targetUser = getUserTarget(id_user);
        System.out.println("user ======="+targetUser);
        //System.out.println("id user =   "+id_user);
    }
    
   
    @FXML
    private void PasserUnOffre(ActionEvent event) throws MessagingException {
        Session session = Session.getInstance();
        String user_fullname = session.getNom()+session.getPrénom();
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
       // System.out.println("un email sera envoyer de A =  "+user_fullname);
        //System.out.println("pour un autre user =  "+targetUser.getNom());
       sendVerificationEmail.envoyerProjetNotificatio(targetUser.getMail(), targetUser.getNom(), user_fullname);
        
    }

    public void refresh() {
        prolist.clear();
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();
        

        try {
            String requete = "select * from projet e";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Projet e;
                e = new Projet();
               
                
                e.setId_projet(rs.getInt("id_projet"));

                e.setTitre(rs.getString("titre"));

                e.setDescription(rs.getString("description"));

                e.setPrix(rs.getInt("prix"));
                e.setId_user_id(rs.getInt("id_user_id"));

                e.setCategorie(rs.getString("categorie"));

                e.setCompetences(rs.getString("competence"));
                
                e.setDatelim(rs.getString("datelim"));
                
                System.out.println("les projets ajoutees :" + e.toString());
                prolist.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        tab_projet.setItems(prolist);

    }
    @FXML
    private void generateStatisticsPopup() {
        ObservableList<Projet> sortedList = prolist.sorted((p1, p2) -> Float.compare(p2.getPrix(), p1.getPrix()));
    PieChart chart = new PieChart();
    chart.setTitle("Top Projects with Biggest Budgets");
    
    float totalBudget = 0.0f;
    for (Projet projet : sortedList) {
        totalBudget += projet.getPrix();
    }
    
    int count = 0;
    for (Projet projet : sortedList) {
        if (count == 5) {
            break;
        }
        
        float percentage = (projet.getPrix() / totalBudget) * 100;
        chart.getData().add(new PieChart.Data(projet.getTitre() + " (" + String.format("%.2f", percentage) + "%)", projet.getPrix()));
        count++;
    }
    
    Scene scene = new Scene(new Group(chart));
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
}

    private User getUserTarget(int id_user) throws SQLException {
        String fName ,lname ,mail;
        User user=new User();
         mc = DataSource.getInstance().getCnx();
           
        
        String requete = "select * from user where id=? ";
                 mc = DataSource.getInstance().getCnx();

        PreparedStatement statement =  mc.prepareStatement(requete);
        
        statement.setInt(1,id_user);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            fName=rs.getString("nom");
            lname=rs.getString("prenom");
            mail=rs.getString("mail");
            user.setId_user(id_user);
            user.setNom(fName+" "+lname);
            user.setMail(mail);
            
            
        }
        
        return user;
    }
    
    
    
}
