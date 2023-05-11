package controller;
  
import entity.Evenement;
import entity.Reservation;
import entity.Session;
import entity.User;
import java.io.IOException;
import service.IEvenementService;
import service.IReservationService;
import source.DataSource;
import service.EvenementService;
import service.ReservationService;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.stage.Stage;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author TECHN
 */
public class ReservationController implements Initializable {
    
      @FXML
    private Label retour;
      
    @FXML
    private Label L_lieu;

    @FXML
    private Label L_date;

    @FXML
    private Label L_rate;

    @FXML
    private Label L_description;
      @FXML
    private AnchorPane interface_details;
@FXML
    private Label L_arriere;
    @FXML
    private TableView<Reservation> tableRes;
    @FXML
    private TableColumn<Reservation, Integer> id_user;
    @FXML
    private TableColumn<Reservation, Integer> idreservation;
    @FXML
    private TableColumn<Reservation, Integer> id_event;
    @FXML
    private TextArea idReservation;
    @FXML
    private TextArea etatReservation;
    @FXML
    private ComboBox<Integer> idEvent;
    @FXML
    private Button supprimerRes;
    @FXML
    private Button detailsRes;
    Connection mc;
    PreparedStatement ste;
    ObservableList<Reservation>resList;
    @FXML
    private Button fermerRes;
    Session session;
    int connectedClient;
Reservation clicked;
User useruser;
String userRole;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        session = Session.getInstance();
                      connectedClient = session.getId_user();
                      UserService us = new UserService();
                      ReservationService rs = new ReservationService();
                      useruser=us.readById(connectedClient);
                      userRole=useruser.getRole();
                      if(userRole.equals("ROLE_ADMIN")){
                       supprimerRes.setVisible(false);
                          detailsRes.setVisible(true);
                          afficherReservations();
        
                      }
                      else {
                         
                          supprimerRes.setVisible(true);
                          detailsRes.setVisible(false);
                              afficherUserReservations(connectedClient);
                      }
                      interface_details.setVisible(false);
                      
    
    
    }  
    
     @FXML
    public void goToEvent(MouseEvent event) throws IOException {
        
         Parent path = FXMLLoader.load(getClass().getResource("../view/EvenementFXML.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        
     }
    
     @FXML
    public void arriere(MouseEvent event) throws IOException {
        interface_details.setVisible(false);
        
     }
        
        
         

    

    void afficherReservations(){
            mc=DataSource.getInstance().getCnx();
            resList = FXCollections.observableArrayList();
      
      try {
            String requete = "SELECT * FROM reservation r ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Reservation r = new Reservation();
                r.setId_reservation(rs.getInt("id"));
                r.setId_event(rs.getInt("id_event_id"));
                r.setId_user(rs.getInt("id_user_id"));
                
                System.out.println("the added reservations :" +r.toString());
                resList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
  id_user.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_user()));
  idreservation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_reservation()));
  id_event.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_event()));
  tableRes.setItems(resList);
  
  refresh();
    
  }
  
  
    
    public void refresh(){
            resList.clear();
            mc=DataSource.getInstance().getCnx();
            resList = FXCollections.observableArrayList();
      
      try {
            String requete = "SELECT * FROM reservation r ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Reservation r = new Reservation();
                r.setId_reservation(rs.getInt("id"));
                r.setId_event(rs.getInt("id_event_id"));
                r.setId_user(rs.getInt("id_user_id"));
                
                System.out.println("the added reservations :" +r.toString());
                resList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableRes.setItems(resList);
       
  }
    
    @FXML
    private void selected(MouseEvent res) {
          clicked = tableRes.getSelectionModel().getSelectedItem();
        
    }
////////////////////////////////////////////////////////////////////////
    void afficherUserReservations(int id_connected){
            mc=DataSource.getInstance().getCnx();
            resList = FXCollections.observableArrayList();
      
      try {
            String requete = "SELECT * FROM reservation where id_user_id = ?";
            PreparedStatement st = DataSource.getInstance().getCnx().prepareStatement(requete);
            st.setInt(1, id_connected);
            ResultSet rs =  st.executeQuery(); 
            while(rs.next()){
                Reservation r = new Reservation();
                r.setId_reservation(rs.getInt("id"));
                r.setId_event(rs.getInt("id_event_id"));
                r.setId_user(rs.getInt("id_user_id"));
                
                System.out.println("the added reservations :" +r.toString());
                resList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
  id_user.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_user()));
  idreservation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_reservation()));
  id_event.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_event()));
  tableRes.setItems(resList);
  
  refreshuser(connectedClient);
    
  }
    
    public void refreshuser(int id_connected){
            resList.clear();
            mc=DataSource.getInstance().getCnx();
            resList = FXCollections.observableArrayList();
      
      try {
           String requete = "SELECT * FROM reservation where id_user_id = ?";
            PreparedStatement st = DataSource.getInstance().getCnx().prepareStatement(requete);
            st.setInt(1, id_connected);
            ResultSet rs =  st.executeQuery(); 
            while(rs.next()){
                Reservation r = new Reservation();
                r.setId_reservation(rs.getInt("id"));
                r.setId_event(rs.getInt("id_event_id"));
                r.setId_user(rs.getInt("id_user_id"));
                System.out.println("the added reservations :" +r.toString());
                resList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableRes.setItems(resList);
       
  }
////////////////////////////////////////////////////////////////////////    


    @FXML
    private void deleteRes(MouseEvent event) {
        if(clicked.getId_user()== connectedClient){
        Reservation reservation = new Reservation();
        reservation.setId_event(clicked.getId_event());
        reservation.setId_reservation(clicked.getId_reservation());
        reservation.setId_user(clicked.getId_user());
        IReservationService rs= new ReservationService();
        rs.supprimerReservation(reservation);
        refreshuser(connectedClient);
        }
        
       
    }

    @FXML
    private void detailsReservation(MouseEvent event) {
        interface_details.setVisible(true);
        EvenementService es = new EvenementService();
        Evenement evenement = new Evenement();
        evenement=es.readById(clicked.getId_event());
        L_rate.setText(String.valueOf(evenement.getrate()) );
        L_description.setText(evenement.getDescription_event());
        L_lieu.setText(evenement.getLieu_event());
        L_date.setText(evenement.getDate_event().toString());
        
    }

    
    
    
    
    
    
    @FXML
    private void closeRes(MouseEvent event) {
        Stage stage =(Stage) fermerRes.getScene().getWindow();
        stage.close(); 
    }
    










    
}