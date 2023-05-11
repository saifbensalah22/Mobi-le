/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Cours;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import service.CoursServiceImpl;

/**
 * FXML Controller class
 *
 * @author PC
 */

public class AjouterCoursController implements Initializable {
     @FXML
    private TextField txtNameCours=new TextField();
    int id;
    @FXML
       ObservableList<Cours> dataList;
     @FXML
    private TextField FilterField=new TextField();
     @FXML 
    private TextField txtNomTuteur=new TextField();
 @FXML
    private TextField txtIdCours=new TextField();
  @FXML
    private TextField txtPrix=new TextField();
   @FXML
    private TextField txtIdUser=new TextField();
    @FXML
    private TextField txtDescription;

    @FXML
    private TableView<Cours> table;

    @FXML
    private TableColumn<Cours, Integer> IdColmn= new TableColumn<>();

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
    private Button btn_acheter;
    @FXML
    private Button btn_bestSeller;
    

       @FXML
   private void goToBestSeller(ActionEvent events)throws Exception{
        Parent page1 = FXMLLoader.load(getClass().getResource( "../view/LineChartEx.java"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) events.getSource()).getScene().getWindow();
       // stage.setUserData(user);
        stage.setScene(scene);
        stage.show();
       
       
   }
    @FXML
   private void Acheter(ActionEvent event)throws Exception{
       /* Stage stage = (Stage) btn_acheter.getScene().getWindow();
        stage.close();
        Stage primaryStage =  new Stage();
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("../CoursTestINTERF.fxml"));
         Parent root = (Parent) loader.load();
        CoursTestINTERFController controllerInterface = loader.getController();
        System.out.println("in page 1 "+txtIdCours.getText());
        System.out.println("in page 1 controller   "+controllerInterface);
        if(!txtIdCours.getText().equals("IdCours")){
        controllerInterface.setCourId(Integer.parseInt(txtIdCours.getText()));
        }
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        
     Parent page1 = FXMLLoader.load(getClass().getResource( "../view/CoursTestINTERF.fxml"));
        Scene scene = new Scene(page1);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       // stage.setUserData(user);
        stage.setScene(scene);
        stage.show();
        

    }
    

   public void table() {
    // Create an ObservableList of courses
    ObservableList<Cours> courses = FXCollections.observableArrayList();
    CoursServiceImpl coursService = new CoursServiceImpl();
    courses.addAll(coursService.readAll());

    // Create a FilteredList from the ObservableList
    FilteredList<Cours> filteredCourses = new FilteredList<>(courses);

    // Bind the text property of the search field to the predicate property of the filtered list
    FilterField.textProperty().addListener((observable, oldValue, newValue) ->
            filteredCourses.setPredicate(course -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return course.getCours_name().toLowerCase().contains(lowerCaseFilter);
            }));

    // Wrap the filtered list in a SortedList
    SortedList<Cours> sortedCourses = new SortedList<>(filteredCourses);
    sortedCourses.comparatorProperty().bind(table.comparatorProperty());

    // Set the table's items to the sorted list
    table.setItems(sortedCourses);

    // Set up the table columns
    IdColmn.setCellValueFactory(f -> new SimpleIntegerProperty(f.getValue().getId_cours()).asObject());
    NomCoursColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getCours_name()));
    NomTuteurColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getNom_tuteur()));
    DescriptionColmn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getDescription()));
    PrixColmn.setCellValueFactory(f -> new SimpleObjectProperty(f.getValue().getPrix()));
    idUser.setCellValueFactory(f -> new SimpleIntegerProperty(f.getValue().getId_user()).asObject());

    // Set up the row factory to handle row selection
    table.setRowFactory(tv -> {
        TableRow<Cours> row = new TableRow<>();
        row.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1 && !row.isEmpty()) {
                Cours rowData = row.getItem();
                txtIdCours.setText(Integer.toString(rowData.getId_cours()));
                txtNameCours.setText(rowData.getCours_name());
                txtNomTuteur.setText(rowData.getNom_tuteur());
                txtDescription.setText(rowData.getDescription());
                txtPrix.setText(Double.toString(rowData.getPrix()));
                txtIdUser.setText(Integer.toString(rowData.getId_user()));
            }
        });
        return row;
    });
    
    
      }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.table();
    }      
    
}
