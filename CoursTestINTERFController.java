/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Cours;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import service.CoursServiceImpl;
import source.DataSource;
import view.LineChartEx;




 

    
 
public class CoursTestINTERFController implements Initializable {
  @FXML
    private Button btnDownload;

    @FXML
    private Button btnQuiz;
     
   
    private int id;
 private Connection conn;
    @FXML
    void Download(ActionEvent event) {
               
        CoursServiceImpl coursService=new CoursServiceImpl();
         System.out.println("download   "+ this.id);
       Cours p= coursService.readById(this.id);
        System.out.println("download   "+ p.getCours_name());
        
          String requete ="select contenu from cours where id = "+this.id;
           

        try {
            conn=DataSource.getInstance().getCnx();
            Statement st=conn.createStatement();
           ResultSet rs= st.executeQuery(requete);
           while(rs.next()){
                  System.out.println("in resut "+rs);
           // Step 3: Create an InputStream object
        InputStream inputStream = new FileInputStream("C:/Users/ounis/Desktop/maher/maher/symfonyPiDev - selim/symfonyPiDev - selim/public/files/cours/"+rs.getString("contenu"));

        // Step 4: Create a FileOutputStream object
        FileOutputStream outputStream = new FileOutputStream("C:/pdfCours/downloaded_file.txt");     
           byte[] buffer = new byte[4096];
        int bytesRead = -1;
        if(Objects.nonNull(inputStream)){
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }}
            String requeteOccurence =" update cours set occurence = "+ (p.getOccurence()+1) + " where id = "+this.id;
            Statement stOccurence=conn.createStatement();
            stOccurence.executeUpdate(requeteOccurence);
        // Close the streams
          if(Objects.nonNull(inputStream)){
        inputStream.close();
        outputStream.close();
           }}
        } catch (SQLException ex) {
            Logger.getLogger(CoursServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          Logger.getLogger(CoursTestINTERFController.class.getName()).log(Level.SEVERE, null, ex);
      }
   
}
    
    void setCourId (int id) {
         System.out.println("coutrs param  "+ id);
            this.id= id;
    }
    @FXML
    void Quizz(ActionEvent event) {
      /*  
        String quizRef="https://docs.google.com/forms/d/1gZl_Hn5bzos42gWdd3LKIzyrJL2ntPZKmuir09kBIq4/prefill";
     openWebpage(quizRef);   
    */
      LineChartEx lineChartEx= new LineChartEx();
    }
    

    private void openWebpage(String quizRef) {
          try {
        Desktop.getDesktop().browse(new URL(quizRef).toURI());
    } catch (Exception e) {
        e.printStackTrace();
    }
          
    }

   
    
  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        // TODO
    }    
    
}
