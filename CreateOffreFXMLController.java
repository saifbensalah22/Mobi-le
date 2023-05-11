/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Demande;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import service.DemandeService;
import service.ProjetService;
import service.SendVerificationEmail;
import service.UserService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author Patoo
 */
public class CreateOffreFXMLController implements Initializable {
      ObservableList<User>recList;
    
    Connection cnx;
    PreparedStatement ste;
    int ref ,id_freelance;
  @FXML
    private AnchorPane interface_adddemande;

    @FXML
    private AnchorPane interface_message;

    @FXML
    private AnchorPane interface_selecteduser;

    @FXML
    private Label L_address;

    @FXML
    private Label L_mail;

    @FXML
    private Label L_nom;

    @FXML
    private Label L_telephone;
    @FXML
    private TextField champ_budget;

    @FXML
    private TextField champ_competance;

    @FXML
    private TextField champ_description;

    @FXML
    private TextField champ_titre;
     @FXML
    private DatePicker datelim;
     @FXML
    private Button valider;
        @FXML
    private Text text;
     @FXML
    private AnchorPane interface_user;
    @FXML
    private TableView<User> tb_user;
    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private TableColumn<User, String> col_mail;
    @FXML
    private TableColumn<User, String> col_pswd;
     @FXML
    private TableColumn<User, Integer> col_phone;
    @FXML
    private TableColumn<User, String> col_addresse;
    @FXML
    private ComboBox<Integer> comboBox_userid;
    @FXML
    private TextField tf_searchUser;
    @FXML
    private Button btn_search;
    @FXML
    private Button btn_set;
    User clicked ;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AfficherUser();
        LoadUserID();
      interface_adddemande.setVisible(false);
 interface_message.setVisible(true);
 interface_selecteduser.setVisible(false);
    interface_user.setVisible(true);
            // TODO
    }
    public void AfficherUser()
      {
          cnx=DataSource.getInstance().getCnx();
          recList = FXCollections.observableArrayList();
   
        
        try {
            String requete = "SELECT * FROM user ";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs =  st.executeQuery(requete); 
            while(rs.next()){
                User r = new User();
                r.setId_user(rs.getInt("id"));
                r.setNom(rs.getString("nom")+" "+rs.getString("prenom"));
                r.setMail(rs.getString("mail"));
                r.setPassword(rs.getString("password"));
                r.setNuméroTéléphone(rs.getInt("numero_telephone"));
                r.setAdresse(rs.getString("addresse"));
                recList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        col_id.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_user()));
        col_name.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        col_mail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMail()));
        //col_pswd.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));
        col_pswd.setCellValueFactory(data -> new SimpleStringProperty("****************"));
        col_phone.setCellValueFactory(data -> new SimpleObjectProperty<> (data.getValue().getNuméroTéléphone()));
        col_addresse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        
        tb_user.setItems(recList);
        LoadUserID();
        search();
      }

    private void LoadUserID() {
         cnx=DataSource.getInstance().getCnx();
         ResultSet rs;
                 
            try { 
                PreparedStatement pst = cnx.prepareStatement("Select id from user");
                rs= pst.executeQuery();
                comboBox_userid.getItems().removeAll();
                comboBox_userid.autosize();
                while(rs.next()){
                    comboBox_userid.getItems().add(rs.getInt(1));
                   
                }
     } catch (SQLException ex) {
                Logger.getLogger(SetUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
    }
    @FXML
    private void SearchUser(ActionEvent event) throws SQLException, IOException {
         Object obj=comboBox_userid.getSelectionModel().getSelectedItem();
         String  id= obj.toString() ;
         ref=Integer.parseInt(id);
         UserService us = new UserService();
         User user = us.readById(ref);
         Parent page1 = FXMLLoader.load(getClass().getResource( "../view/setUserInterface.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setUserData(user);
        stage.setScene(scene);
        stage.show();
 
    }
    @FXML
    private Label btn_retour;
     
   @FXML
    public void goBack(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }    
     
    
    @FXML
    private int selected(MouseEvent event) {
        
        clicked = tb_user.getSelectionModel().getSelectedItem();
        id_freelance=Integer.parseInt(String.valueOf(clicked.getId_user()));
        String fname=String.valueOf(clicked.getNom());
        String mail=String.valueOf(clicked.getMail());
        String password=String.valueOf(clicked.getPassword());
        String address=String.valueOf(clicked.getAdresse());
        String phone=String.valueOf(clicked.getNuméroTéléphone());
        L_nom.setText(fname);
        L_mail.setText(mail);
        L_telephone.setText(phone);
        L_address.setText(address);
        
    interface_adddemande.setVisible(true);
    interface_message.setVisible(false);
    interface_selecteduser.setVisible(true);
    interface_user.setVisible(false);
       return id_freelance;
    }
      @FXML
    private void addDemande(ActionEvent event) throws IOException, MessagingException {
        String testtitre = champ_titre.getText();
        String testbudg = champ_budget.getText();
        String testcomp = champ_competance.getText();
        String testdesc = champ_description.getText();
       // LocalDate testDate = datelim.getValue();
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
                    Session session = Session.getInstance();
                    int id_client = session.getId_user();
//                    LocalDate selectedDate = datelim.getValue();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    String formattedDate = selectedDate.format(formatter); 
                    Demande demande = new Demande(testtitre,testdesc,Float.parseFloat(testbudg),testcomp,"non étudier",id_client,id_freelance);
                    DemandeService ds = new DemandeService();
                    ds.insert(demande);
                    //////////////////////////////
                    SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
                    String user_fullname = session.getNom()+session.getPrénom();
                    sendVerificationEmail.SendenvoyerProjetNotificatio(clicked.getMail(), clicked.getNom(), user_fullname);
                    //////////////////////////////
                } catch (NumberFormatException ex) {
                    Logger.getLogger(UserSearchProjetController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
                Parent path = FXMLLoader.load(getClass().getResource("../view/UserProfil.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
    }

    
    private void search(){
        FilteredList<User>filteredData = new FilteredList<>(recList, b->true);
            tf_searchUser.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(User->{
                    if(newValue == null || newValue.isEmpty()){
                    return true;
                }
               
                String lowerCaseFilter = newValue.toLowerCase();
                 
                  if(String.valueOf(User.getNom()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                  else if(String.valueOf(User.getId_user()).indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                return false;
                }
            });          
        });
        SortedList<User>sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tb_user.comparatorProperty());
        tb_user.setItems(sortedData);
    }

   
    /**
     * Initializes the controller class.
     */
        

    
}
