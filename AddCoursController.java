/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Cours;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.css.SimpleStyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.CoursServiceImpl;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class AddCoursController implements Initializable {
  @FXML
    private TextField txtNameCours =new TextField() ;
    int id;
    @FXML
    private TextField txtPrix =new TextField() ; 
    @FXML
    private TextField txtNomTuteur=new TextField();
 @FXML
    private TextField txtIdCours=new TextField();
  @FXML
    private TextField txtNoteCours=new TextField();
   @FXML
    private TextField txtIdUser=new TextField();
    @FXML
    private TextField txtDescription;

    @FXML
    private TableView<Cours> table;
    @FXML
    private Label btn_back;
    @FXML
    private TableColumn<Cours, Integer> IdColmn;

    @FXML
    private TableColumn<Cours, String> NomCoursColmn= new TableColumn<>();

    @FXML
    private TableColumn<Cours, String> NomTuteurColmn= new TableColumn<>();

    @FXML
    private TableColumn<Cours, String> DescriptionColmn= new TableColumn<>();
  
    @FXML
    private TableColumn<Cours, Integer> PrixColmn= new TableColumn<>();

    @FXML
    private TableColumn<Cours, String> ContenuColmn= new TableColumn<>();
    @FXML
    private TableColumn<Cours, Integer> idUser= new TableColumn<>();
    

    @FXML
    private Button btnAdd;
    

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;
    int myIndex;

    @FXML
          private void Add(ActionEvent event) throws IOException {
          System.out.println("idcours   "+txtIdCours.getText());
          System.out.println("idUser  "+txtIdUser.getText());
          System.out.println("txtPrixCours  "+txtPrix.getText());
          Cours p =new Cours();
              if(!txtIdCours.getText().equals("IdCours")&&!txtIdCours.getText().isEmpty()){
       //   p=new Cours(11,Integer.parseInt(txtNoteCours.getText()),ContenuColmn.getText(), txtNameCours.getText(),33,txtDescription.getText(), txtNomTuteur.getText(),Integer.parseInt(txtPrix.getText()));
          p=new Cours(Integer.parseInt(txtIdCours.getText()),Integer.parseInt(txtIdUser.getText()),Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(),txtNomTuteur.getText(),ContenuColmn.getText());
         
              }else{
          p = new Cours(Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(), txtNomTuteur.getText());
            }
        CoursServiceImpl coursService=new CoursServiceImpl();
        coursService.insertVue(p);
        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/AddCours.fxml"));
       
        Parent root=loader.load();
       
        AddCoursController dc=loader.getController();
       // dc.setTxtNameCours(txtNameCours.getText());
        txtNameCours.getScene().setRoot(root);
            
 this.table();
            
             

    }
    public void setTxtNameCours(String txtNameCours) {
        this.txtNameCours.setText(txtNameCours);
    }
    
   @FXML
    void Delete(ActionEvent event) throws IOException {
                  Cours p =new Cours();
              if(txtIdCours.getText()!="IdCours"){
          p=new Cours(Integer.parseInt(txtIdCours.getText()),Integer.parseInt(txtIdUser.getText()),Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(),txtNomTuteur.getText(),ContenuColmn.getText());
         }else{
          p = new Cours(Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(), txtNomTuteur.getText());
            }
        CoursServiceImpl coursService=new CoursServiceImpl();
        coursService.delete(p.getId_cours());
        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/AddCours.fxml"));
       
        Parent root=loader.load();
        
        AddCoursController dc=loader.getController();
        txtNameCours.getScene().setRoot(root);
         this.table();   

    }
     @FXML
    void goBack(javafx.scene.input.MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
    @FXML
    void Update(ActionEvent event) throws IOException {
        System.out.println("idcours  "+txtIdCours.getText());
        System.out.println("idUser  "+txtIdUser.getText());
          System.out.println("txtPrix  "+txtPrix.getText());
                          Cours p =new Cours();
              if(txtIdCours.getText()!="IdCours"){
        p=new Cours(Integer.parseInt(txtIdCours.getText()),Integer.parseInt(txtIdUser.getText()),Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(),txtNomTuteur.getText(),ContenuColmn.getText());
      //  p=new Cours(Integer.parseInt(txtIdCours.getText()),Integer.parseInt(txtPrix.getText()),ContenuColmn.getText(), txtNameCours.getText(),Integer.parseInt(txtIdUser.getText()),txtDescription.getText(), txtNomTuteur.getText());
          }else{
        p = new Cours(Double.parseDouble(txtPrix.getText()),txtDescription.getText(),txtNameCours.getText(), txtNomTuteur.getText());            }
        CoursServiceImpl coursService=new CoursServiceImpl();
        coursService.update(p);
        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/AddCours.fxml"));
       
        Parent root=loader.load();
        
        AddCoursController dc=loader.getController();
        txtNameCours.getScene().setRoot(root);
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Student Registationn");

		
		alert.setHeaderText("Student Registation");
		alert.setContentText("Updateddd!");
                
            this.table();    

    }
    
    public void table()
      {
        myIndex = table.getSelectionModel().getSelectedIndex();
          ObservableList<Cours> students = FXCollections.observableArrayList();
          CoursServiceImpl coursService=new CoursServiceImpl();
            students=  coursService.readAll();
                table.setItems(students);
                IdColmn.setCellValueFactory(f -> new SimpleIntegerProperty(f.getValue().getId_cours()).asObject() );
              
                NomCoursColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue()== null ? "": f.getValue().getCours_name()));
                NomTuteurColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue()== null ? "": f.getValue().getNom_tuteur()));
                DescriptionColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue()== null ? "": f.getValue().getDescription()));
                PrixColmn.setCellValueFactory(f ->new SimpleObjectProperty(f.getValue().getPrix()));
               // ContenuColmn.setCellValueFactory(f ->new ReadOnlyStringWrapper(f.getValue().getCoursContenue()== null ? "": f.getValue().getCoursContenue()));
                idUser.setCellValueFactory(f ->new SimpleIntegerProperty(f.getValue().getId_user()).asObject());
                table.setRowFactory( tv -> {
                    TableRow<Cours> myRow = new TableRow<>();
     myRow.setOnMouseClicked (event ->
     {
        if (event.getClickCount() == 1 && (!myRow.isEmpty()))
        {
            myIndex =  table.getSelectionModel().getSelectedIndex();
           id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId_cours()));
//           IdColmn.setText(String.valueOf (table.getItems().get(myIndex).getId_cours()));
           txtDescription.setText(table.getItems().get(myIndex).getDescription());
           txtNameCours.setText(table.getItems().get(myIndex).getCours_name());
           txtNomTuteur.setText(table.getItems().get(myIndex).getNom_tuteur());
           txtPrix.setText(String.valueOf (table.getItems().get(myIndex).getPrix()));      
           txtIdCours.setText(String.valueOf (table.getItems().get(myIndex).getId_cours()));     
             txtIdUser.setText(String.valueOf (table.getItems().get(myIndex).getId_user()));               
        }
     });
        return myRow;
                   });
   

    
    
      }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.table();
    }    
    
}