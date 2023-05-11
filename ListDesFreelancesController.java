/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.CV;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.CvUserService;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author ounis
 */
public class ListDesFreelancesController implements Initializable {
    
    @FXML
    private Label acc;
    
    @FXML
    private VBox shoosenUserCart;

    
    @FXML
    private ScrollPane scroll;
    
    @FXML
    private TextField chercherComp;
    
    @FXML
    private Label userName;

    @FXML
    private ImageView userImg;

    @FXML
    private Label UserMail;

    @FXML
    private Label userTlf;

  
        
    @FXML
    private GridPane grid;
    
    @FXML
    private Button vp;
    
    @FXML
    private TextField chercher;
    private List<User> utilisateursAffiches; 
    private List<CV> cvs; 
     
    List<User> utilisateursFiltres = new ArrayList<>();
    // List<User> freelances = new ArrayList<>();
       UserService us = new UserService();
       List<User> freelances = us.readAll();
       
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        afficherListeUtilisateurs(freelances);

    }
    
     @FXML
    public void goToAcceuil(MouseEvent event) throws IOException {
        
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

    }
    
    
    
    public void afficherListeUtilisateurs(List<User> utilisateurs) {
        List<CV> cv ;
        CvUserService cus = new CvUserService();
        cvs = cus.readAll();
        // Supprimer toutes les lignes existantes de la grille
        grid.getChildren().clear();
        // Ajouter un en-tête à la grille
        Label imageLabel = new Label("Image");
        Label nomLabel = new Label("Nom");
        Label prenomLabel = new Label("Prénom");
        Label telLabel = new Label("Téléphone");
        Label com = new Label("cométences");
        grid.add(imageLabel, 0, 0);
        grid.add(nomLabel, 1, 0);
        grid.add(prenomLabel, 2, 0);
        grid.add(telLabel, 3, 0);
        grid.add(com, 4, 0);
        // Parcourir tous les utilisateurs et ajouter une nouvelle ligne à la grille pour chaque utilisateur
        int row = 1;
        for (User user : utilisateurs) {
            Label id = new Label(String.valueOf(user.getId_user()));
            Label nom = new Label(user.getNom());
            Label prenom = new Label(user.getprénom());
            for(CV i : cvs){
            if( i.getId_user()==user.getId_user()){
              Label compétences = new Label(i.getCompétances());
              grid.add(compétences, 4, row);              
            }
            }
       
            Label telephoneValue = new Label(Integer.toString(user.getNuméroTéléphone()));

           if (user.getImage() != null) {
              ImageView image = new ImageView(new Image(user.getImage()));
              image.setFitWidth(50);
              image.setFitHeight(50);
              grid.add(image, 0, row);
           } 

         
            grid.add(nom, 1, row);
            grid.add(prenom, 2, row);
            grid.add(telephoneValue, 3, row);
             row++;


        }



for (Node node : grid.getChildren()) {
    node.setOnMouseClicked((MouseEvent event) -> {
        // Get the selected item
        Node selectedNode = (Node) event.getSource();
        int selectedRow = GridPane.getRowIndex(selectedNode);
        int selectedColumn = GridPane.getColumnIndex(selectedNode);
            
        // Do something with the selected row and column
       
          Object selectedItem = null;
        for (Node item : grid.getChildren()) {
            if (GridPane.getRowIndex(item) == selectedRow && GridPane.getColumnIndex(item) == selectedColumn) {
                selectedItem = item;
                User selectedUser = utilisateurs.get(selectedRow - 1);
                
            
                 
                 userName.setText(selectedUser.getNom() + " " + selectedUser.getprénom());
                  UserMail.setText(selectedUser.getMail());
                  if(selectedUser.getImage() != null ){
                  userImg.setImage(new Image(selectedUser.getImage()));
                  }else{
                      File file1= new File("C:/Users/ounis/Desktop/freelanci/freelanci/src/image/user.png");
                      Image image = new Image(file1.toURI().toString());
                      userImg.setImage(image);
                      
                  }
                break;
            }
        }
        
        
    });
}

        
    }
    
  

@FXML
public void chercherParNom(ActionEvent event) {
    String nom = chercher.getText().toLowerCase();

    List<User> utilisateursFiltres = freelances.stream()
            .filter(u -> u.getNom().toLowerCase().contains(nom) || u.getprénom().toLowerCase().contains(nom))
            .collect(Collectors.toList());

    afficherListeUtilisateurs(utilisateursFiltres);
}



   @FXML
    void chercherParComp(ActionEvent event) {
        
         List<CV> cv ;
         CvUserService cus = new CvUserService();
         cvs = cus.readAll();
       
          UserService us = new UserService();
          List<User> users = us.readAll();      
          String searchTerm = chercherComp.getText().trim().toLowerCase();
 
    List<User> filteredUsers = new ArrayList<>();

    for (User user : users) {

         for(CV i : cvs){
            if( i != null && i.getCompétances().toLowerCase().contains(searchTerm) && user.getId_user()==i.getId_user()){
                 filteredUsers.add(user);
                            
            }
            }

    }

     afficherListeUtilisateurs(filteredUsers);
    }


  @FXML
    public void voirProfil(ActionEvent event) throws IOException, SQLException {
        
        String mail = UserMail.getText();
        
      
        
        UserService uss = new UserService();
        User utilisateur = uss.getUserByMail(mail);
        if(utilisateur != null){
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserSelectedProfil.fxml"));
                Parent root = loader.load();
                UserSelectedProfilController userSelectedProfilController = loader.getController();
                userSelectedProfilController.setUser(utilisateur);
            
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

    }
    }

}
   



    
       

    

