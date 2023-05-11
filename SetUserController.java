package controller;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.UserService;
import source.DataSource;
public class SetUserController implements Initializable {
        ObservableList<User>recList;
    
    Connection cnx;
    PreparedStatement ste;
    int ref;

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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AfficherUser();
        LoadUserID();
        btn_set.setVisible(false);
        // TODO
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
        col_pswd.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));
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
    private void selected(MouseEvent event) {
        
        User clicked = tb_user.getSelectionModel().getSelectedItem();
        int id=Integer.parseInt(String.valueOf(clicked.getId_user()));
        String fname=String.valueOf(clicked.getNom());
        String mail=String.valueOf(clicked.getMail());
        String password=String.valueOf(clicked.getPassword());
        String address=String.valueOf(clicked.getAdresse());
        String phone=String.valueOf(clicked.getNuméroTéléphone());
        btn_set.setVisible(true);
    }
    @FXML
    private void goToSetUserInterface(ActionEvent event) throws IOException {
        User clicked = tb_user.getSelectionModel().getSelectedItem();
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/setUserInterface.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setUserData(clicked);
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
    
}
