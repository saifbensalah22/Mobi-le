/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Evenement;
import entity.StarRating;
import service.IEvenementService;
import source.DataSource;
import service.EvenementService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Reservation;
import entity.Session;
import entity.User;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.IReservationService;
import service.ReservationService;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author TECHN
 */
public class EvenementFXMLController implements Initializable {
    
    @FXML
    private Label retour;
    @FXML
    private TableView<Evenement> tableEvent;
    @FXML
    private TableColumn<Evenement, Integer> id;
    @FXML
    private TableColumn<Evenement, Integer> rate;
     @FXML
    private TableColumn<Evenement, Integer> col_id_user;
    @FXML
    private TableColumn<Evenement, String> lieu;
    @FXML
    private TableColumn<Evenement, String> date;
    @FXML
    private TableColumn<Evenement, String> description;
        @FXML
    private Label goToReservation;
    @FXML
    private TextArea idEvent;
    @FXML
    private TextArea rateEvent;
    @FXML
    private TextArea lieuEvent;
    @FXML
    private TextArea dateEvent;
    @FXML
    private TextArea descriptionEvent;
    @FXML
    private Button ajouterEvent;
    @FXML
    private Button fermerEvent;
    @FXML
    private Button supprimerEvent;
    @FXML
    private Button modifierEvent;
    
     @FXML
    private Button rev;
      Session session;
    int connectedClient;
    Evenement clicked ;
    
    List <User> utilisateurs;
    
    Connection mc;
    PreparedStatement ste;
    ObservableList<Evenement>eventList;
    
    
    
    @FXML
    public void goBack(MouseEvent event) throws IOException {
        
        Session session = Session.getInstance();
        String role = session.getRole();
        if(role.equals("simple user")){
            
             Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        
        }else {
            
              Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        
        }

    }
    
 
    
    @FXML
    private TextArea rechercher;
    @FXML
    private Button filePdf;

    @FXML
    private void addpdf(MouseEvent event) throws SQLException, FileNotFoundException, DocumentException, IOException {
        
        String sql = "SELECT * from evenement";
    ste=mc.prepareStatement(sql);
    ResultSet rs=ste.executeQuery();

    Document doc = new Document();
    PdfWriter.getInstance(doc, new FileOutputStream("./ListeDesEvenements.pdf"));

    doc.open();
   
    doc.add(new Paragraph("   "));
    doc.add(new Paragraph(" ************************************* Liste Des Evenements ************************************* "));
    doc.add(new Paragraph("   "));

    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(50);
    PdfPCell cell;

    cell = new PdfPCell(new Phrase("Date", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
   
    cell = new PdfPCell(new Phrase("Description", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
   
    
    
    cell = new PdfPCell(new Phrase("Lieu", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);

    while (rs.next()) {

        Evenement e = new Evenement();
      
        e.setDate_event(rs.getDate("date_event"));
        e.setDescription_event(rs.getString("description_event"));
       e.setLieu_event(rs.getString("lieu_event"));
       
      
        cell = new PdfPCell(new Phrase(e.getDate_event().toString(), FontFactory.getFont("Comic Sans MS", 12)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(e.getDescription_event(), FontFactory.getFont("Comic Sans MS", 12)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        
  
        
        
        cell = new PdfPCell(new Phrase(e.getLieu_event(), FontFactory.getFont("Comic Sans MS", 12)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    doc.add(table);
    doc.close();
    Desktop.getDesktop().open(new File("./ListeDesEvenements.pdf"));
    }
    class StarRatingCell extends TableCell<Evenement, Integer> {    //pour mettre les etoiles au graphique
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            StarRating starRating = new StarRating(item);
            setText(starRating.display());
        } else {
            setText(null);
        }
    }
}
    
    
    
  
  
  public static boolean isValidDate(String dateString) { //pour valider la date
    String[] dateParts = dateString.split("/");
    if (dateParts.length != 3) {
      return false;
    }
    
    int day;
    int month;
    int year;
    try {
      day = Integer.parseInt(dateParts[0]);
      month = Integer.parseInt(dateParts[1]);
      year = Integer.parseInt(dateParts[2]);
    } catch (NumberFormatException e) {
      return false;
    }
    
    if (month < 1 || month > 12) {
      return false;
    }
    if(year < 1900) 
    { 
     return false;
    }
    int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int maxDays = daysInMonth[month - 1];
    if (month == 2 && isLeapYear(year)) {
      maxDays = 29;
    }
    
    if (day < 1 || day > maxDays) {
      return false;
    }
    
    return true;
  }
  
  public static boolean isLeapYear(int year) {
    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
      return true;
    }
    return false;
  }

    
  
  
  
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         session = Session.getInstance();
                      connectedClient = session.getId_user();
                      if(session.getRole().equals("ROLE_ADMIN")){
                         rev.setVisible(false);
                      }
                      
       afficherEvenements();
    }
 
    
    
    
    void afficherEvenements(){
            mc=DataSource.getInstance().getCnx();
            eventList = FXCollections.observableArrayList();
      
      try {
            String requete = "SELECT * FROM evenement e ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Evenement e = new Evenement();
                e.setId_event(rs.getInt("id"));
                e.setrate(rs.getInt("rate"));
                e.setLieu_event(rs.getString("lieu_event"));
                e.setDate_event(rs.getDate("date_event"));
                e.setDescription_event(rs.getString("description_event"));
                e.setId_user(rs.getInt("id_user_id"));
                System.out.println("id user ========"+e.getId_user());
                //System.out.println("the added events :" +e.toString());
                eventList.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
      
  id.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_event()));
col_id_user.setCellValueFactory(data ->new SimpleObjectProperty(data.getValue().getId_user()));

  rate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getrate())); //rate avec les etoiles
  rate.setCellFactory(col -> new StarRatingCell());
  lieu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLieu_event()));
  date.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDate_event()));
  description.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription_event()));
  
  tableEvent.setItems(eventList);
  
  refresh();
  
  }
  
  public void refresh(){
            eventList.clear();
            mc=DataSource.getInstance().getCnx();
            eventList = FXCollections.observableArrayList();
      
      try {
            String requete = "SELECT * FROM evenement e ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Evenement e = new Evenement();
                e.setId_event(rs.getInt("id"));
                e.setrate(rs.getInt("rate"));
                e.setLieu_event(rs.getString("lieu_event"));
                e.setDate_event(rs.getDate("date_event"));
                e.setDescription_event(rs.getString("description_event"));
                  e.setId_user(rs.getInt("id_user_id"));
                System.out.println("the added events :" +e.toString());
                
                eventList.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableEvent.setItems(eventList);
        search();
  }




    
//AddEvent
    @FXML
    private void addEvent(MouseEvent event) {
        
        UserService us = new UserService();
        utilisateurs = us.readAll(); 
        Session session = Session.getInstance();
        String rate =rateEvent.getText();
        String lieu =lieuEvent.getText();
        String date =dateEvent.getText();
        String description =descriptionEvent.getText();
        int id_user = session.getId_user();
        if (rate.isEmpty() || lieu.isEmpty()|| description.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("Il y a un champ vide !!"); // controle de saisie vide
             alert.showAndWait();          
         }
       /* else if(isValidDate(date)==false)
        {
        Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("votre date est invalide !!"); // controle de saisie aal date
             alert.showAndWait();
        }*/
        else if(Integer.parseInt(rate)>5)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("MAL RATING !!"); // controle de saisie aal rate 
             alert.showAndWait();
        
        }
         else{
        Evenement e= new Evenement(id_user,Integer.parseInt(rate),lieu,description); //zid attribut mtaa id_user
        IEvenementService es= new EvenementService();
        es.ajouterEvenement(e);  
        rateEvent.setText(null);
        lieuEvent.setText(null);                               //yrodlik les text area vide baad AJOUT
       // dateEvent.setText(null);
        descriptionEvent.setText(null);
            System.out.println("event::::::"+e);
        
      //  SendSMS sm = new SendSMS();
       // sm.sendSMS(e);
        
       refresh();
    }
    }
    
    
    
    @FXML
    private void closeEvent(MouseEvent event) {
        Stage stage =(Stage) fermerEvent.getScene().getWindow();
        stage.close();  
        }
       @FXML
    void GoToReservation(MouseEvent event) throws IOException {
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/Reservation.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    private void deleteEvent(MouseEvent event) {
        
      
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setHeaderText("Warning");
       alert.setContentText("Es-tu sûre de supprimer!");
        
        int id_user;
        String Value1 = idEvent.getText();
        String rate =rateEvent.getText();
        String lieu =lieuEvent.getText();
        String date =dateEvent.getText();
        String description =descriptionEvent.getText();
        Optional<ButtonType>result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
      
         Evenement e= new Evenement(Integer.parseInt(Value1));
        IEvenementService es= new EvenementService();
        es.supprimerEvenement(e);
        refresh();
      rateEvent.setText(null);
        lieuEvent.setText(null);
        dateEvent.setText(null);
        descriptionEvent.setText(null);
        }
       
        }
 
  
    
    
    
    @FXML
    private void updateEvent(MouseEvent event) {
        
          UserService us = new UserService();
        utilisateurs = us.readAll(); 
        Session session = Session.getInstance();
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Es-tu sûre de modifier!");
        String Value1 = idEvent.getText();
        String rate =rateEvent.getText();
        String lieu =lieuEvent.getText();
//        String date =dateEvent.getText();
        String description =descriptionEvent.getText();
        int id_user =session.getId_user();
        if (rate.isEmpty() || lieu.isEmpty()||  description.isEmpty()){
             alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("Il y a un champ vide !!"); // controle de saisie
             alert.showAndWait();          
         }
        /*else if(isValidDate(date)==false)
        {
             alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("votre date est invalide !!"); // controle de saisie
             alert.showAndWait();
        }
        
        */
        else if(Integer.parseInt(rate)>5)
        {
             alert = new Alert(Alert.AlertType.ERROR);
             alert.setContentText("MAL RATING !!"); // controle de saisie
             alert.showAndWait();
        
        }
        
         else{
            Optional<ButtonType>result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
                Evenement e= new Evenement(Integer.parseInt(Value1),id_user,Integer.parseInt(rate),lieu,description);
        IEvenementService es= new EvenementService();
        es.modifierEvenement(e);
        
        
        refresh();
    }
    }
    }
    
    
    
    @FXML
    private void selected(MouseEvent event) {
        clicked = tableEvent.getSelectionModel().getSelectedItem();
        idEvent.setText(String.valueOf(clicked.getId_event()));
        rateEvent.setText(String.valueOf(clicked.getrate()));
        lieuEvent.setText(String.valueOf(clicked.getLieu_event()));
        dateEvent.setText(String.valueOf(clicked.getDate_event()));
        descriptionEvent.setText(String.valueOf(clicked.getDescription_event()));
    }
    @FXML
    void goToRev(ActionEvent event) throws IOException {
        Reservation reservation = new Reservation();
        reservation.setId_event(clicked.getId_event());
        reservation.setId_user(connectedClient);
        IReservationService rs= new ReservationService();
        rs.ajouterReservation(reservation);
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/Reservation.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }   
    
   
    
    
    private void search() {      
        FilteredList<Evenement>filteredData = new FilteredList<>(eventList, b->true);
        rechercher.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(Evenement->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
               
                String lowerCaseFilter = newValue.toLowerCase(); 
                 if(String.valueOf(Evenement.getLieu_event()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                  else if(String.valueOf(Evenement.getDate_event()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                return false;
                }
            });          
        });
        SortedList<Evenement>sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableEvent.comparatorProperty());
        tableEvent.setItems(sortedData);
    }
    
    
}
