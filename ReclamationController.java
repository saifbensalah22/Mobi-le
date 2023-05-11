/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import service.ReclamationService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Reclamation;
import entity.Reponse;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.ReponseService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Saif
 */
public class ReclamationController implements Initializable {
    
     @FXML
    private Button rep;

    @FXML
    private TableView<Reclamation> tableRec;
    @FXML
    private TableColumn<Reclamation, Integer> id;
   
    @FXML
    private TableColumn<Reclamation, String> date;
    @FXML
    private TableColumn<Reclamation, String> telph;
    @FXML
    private TableColumn<Reclamation, String> desc;
    @FXML
    private Button ajouterRec;
    @FXML
    private Button supprimerRec;
    @FXML
    private Button modifierRec;
    @FXML
    private Button btn_bloquer;
    @FXML
    private Button btn_verify;
    
    @FXML
    private Text btn_save;
        @FXML
    private TextArea text_description;

    @FXML
    private TextArea text_traitement;
    ObservableList<Reclamation>recList;
    public String EmailABloquer;
    Connection mc;
    PreparedStatement ste;
    Reclamation clicked;
    
    @FXML
    private Button close;
    @FXML
    private AnchorPane fermer ;
      @FXML
    private Pane interface_addReponse;
    @FXML
    private TableColumn<Reclamation, String> username;
    @FXML
    private TableColumn<Reclamation, String> reportUser;
    @FXML
    private TextArea userRec;
    @FXML
    private TextArea pUserRec;
    
      @FXML
    public void goToRep(ActionEvent event) throws IOException {
        
         Parent path = FXMLLoader.load(getClass().getResource("../view/ReponseFXML.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
}
    
  public static boolean isValidDate(String dateString) {
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
    if (year <2023){
        return false;}
    
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
    @FXML
    private TextArea recherche;
    @FXML
    private Button pdfbutton;
  
  
  public boolean isPhoneNumberValid(String phoneNumber) {
    // Expression régulière pour un numéro de téléphone de 8 chiffres (sans les indicateurs de pays)
    String regex = "^[0-9]{8}$";
    return phoneNumber.matches(regex);
}
@Override
    public void initialize(URL url, ResourceBundle rb) {
       afficherReclamations();
       btn_bloquer.setVisible(false);
       interface_addReponse.setVisible(false);
    }    

   
    public  void afficherReclamations()
      {
          mc=DataSource.getInstance().getCnx();
          recList = FXCollections.observableArrayList();
   
        
        try {
            String requete = "SELECT * FROM reclamation r ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));
                r.setEmail(rs.getString("email"));
                r.setReportEmail(rs.getString("reported_email"));
                r.setDate(rs.getDate("date"));
                r.setTelephone(rs.getString("telephone")); 
                 r.setDescription(rs.getString("description"));
                recList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
       
        
        id.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId()));
        username.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        reportUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReportEmail()));
        date.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDate()));
        telph.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        desc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        
        
        tableRec.setItems(recList);
        
        search();
      
      
      }
    @FXML
    public void selected(MouseEvent event) {
        
         clicked = tableRec.getSelectionModel().getSelectedItem();
        
    }
    @FXML
    public void addReponse(MouseEvent event)  {
          interface_addReponse.setVisible(true);
        refresh();
        
        
        
        }
        
        
    
    

    @FXML
    public void deleteRec(MouseEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Es-tu sûre de supprimer!");
       
        Optional<ButtonType>result =  alert.showAndWait(); 
        if(result.get() == ButtonType.OK){ 
         
            
         ReclamationService rc = new ReclamationService();
             rc.supprimerReclamation(clicked);
        
             refresh();}
         
        
    }

    @FXML
    public void closeRec(MouseEvent event) {
  
        
    }
    
    
     public void refresh(){
        
         recList.clear();
       
          
          mc=DataSource.getInstance().getCnx();

        recList = FXCollections.observableArrayList();
        
        String sql="select * from reclamation";
        try {
            ste=mc.prepareStatement(sql);
            ResultSet rs=ste.executeQuery();
            while(rs.next()){
                Reclamation e = new Reclamation();
                e.setId(rs.getInt("id"));
                e.setEmail(rs.getString("email"));
                e.setReportEmail(rs.getString("reported_email"));
                e.setDate(rs.getDate("date"));
                e.setTelephone(rs.getString("telephone"));
                e.setDescription(rs.getString("description"));
                
                
                
                recList.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         tableRec.setItems(recList);   
         search();
    }

    
    @FXML
    void Save(MouseEvent event) throws IOException {
        String traitement=text_traitement.getText();
        String description=text_description.getText();
        Reponse reponse = new Reponse();
        reponse.setId_reclamation(clicked.getId());
        reponse.setTraitement(traitement);
        reponse.setContenu_reponse(description);
        ReponseService rs = new ReponseService();
        rs.ajouterReponseRec(reponse);
        Parent path = FXMLLoader.load(getClass().getResource("../view/ReponseFXML.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }
    
     @FXML
    private Label btn_retour;
     
     
     
   @FXML
    public void goBack(MouseEvent event) throws IOException {
                Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
    
    public void search() {      
        
        FilteredList<Reclamation>filteredData = new FilteredList<>(recList, b->true);
        recherche.textProperty().addListener((observable, oldValue, newValue)->{
           
            filteredData.setPredicate(Reclamation->{
               
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
               
                String lowerCaseFilter = newValue.toLowerCase();
                 
                  if(String.valueOf(Reclamation.getTelephone()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                  else if(String.valueOf(Reclamation.getEmail()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                return false;
                }
            });          
        });
        SortedList<Reclamation>sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableRec.comparatorProperty());
        tableRec.setItems(sortedData);
    }

    @FXML
    public void addpdf(MouseEvent event) throws SQLException, FileNotFoundException, DocumentException, IOException {
        String sql = "SELECT * from reclamation";
    ste=mc.prepareStatement(sql);
    ResultSet rs=ste.executeQuery();

    Document doc = new Document();
    PdfWriter.getInstance(doc, new FileOutputStream("./ListeDesReclamations.pdf"));

    doc.open();
   
    doc.add(new Paragraph("   "));
    doc.add(new Paragraph(" ************************************* Liste Des Reclamations ************************************* "));
    doc.add(new Paragraph("   "));

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(80);
    PdfPCell cell;

    cell = new PdfPCell(new Phrase("date", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
   
    cell = new PdfPCell(new Phrase("reportedEmail", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    cell = new PdfPCell(new Phrase("username", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    
    cell = new PdfPCell(new Phrase("description", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    
     cell = new PdfPCell(new Phrase("telephone", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);

    while (rs.next()) {

        Reclamation e = new Reclamation();
      
      e.setDate(rs.getDate("date"));
        e.setReportEmail(rs.getString("reported_email"));
       e.setEmail(rs.getString("email"));
       e.setDescription(rs.getString("description"));
       e.setTelephone1(rs.getString("telephone"));
       
      
        cell = new PdfPCell(new Phrase(e.getDate().toString(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(e.getReportEmail(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(e.getEmail(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase(e.getDescription(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
         cell = new PdfPCell(new Phrase(e.getTelephone(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    doc.add(table);
    doc.close();
    Desktop.getDesktop().open(new File("./ListeDesReclamations.pdf"));
    
    }
     @FXML
    private void verify(ActionEvent event) throws SQLException {
        btn_verify.setVisible(false);
          Reclamation clicked = tableRec.getSelectionModel().getSelectedItem();

        String mail = clicked.getReportEmail();
        verifUser(mail);
       //bloquerUser(mail);
    }
     public void verifUser(String mail) throws SQLException{
              String requete = "SELECT COUNT(reported_email) FROM reclamation where reported_email=? ";
               PreparedStatement statement = DataSource.getInstance().getCnx().prepareStatement(requete);
             statement.setString(1, mail);
             ResultSet result = statement.executeQuery();
       
            while(result.next()){
                    int count = result.getInt(1);
                if(count<3){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("ce utilisateur a été réclamé seulement  "+count+" fois ");
                    alert.showAndWait();

                    
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("ce utilisateur a été réclamé  "+count+" fois\n Voulez vous changer l'etat de ce utilisateur !!! ");
                    alert.showAndWait();
                    btn_bloquer.setVisible(true);
                    EmailABloquer=mail;
                    }
            }
        
        
          
      }
     @FXML
    private void bloquer(ActionEvent event) throws SQLException {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("En cours de blocage de cette utilisateur!!! ");
                    alert.showAndWait();
       
       //bloquerUser(mail);
    }
}
