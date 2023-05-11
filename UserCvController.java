/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Session;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.CV;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.CvUserService;
import service.UserService;
import source.DataSource;

/**
 * FXML Controller class
 *
 * @author ounis
 */
public class UserCvController implements Initializable {
    
     Connection cnx;
     PreparedStatement ste;
     PreparedStatement pst;
     ResultSet rs;
     ResultSet r;
     String user_url ;
     
     @FXML
    private Label btn_profil;

      @FXML
    private ImageView user_image_profil;

    @FXML
    private Label name;

    @FXML
    private Button btn_img;

    @FXML
    private Label acceuil;

    @FXML
    private Button btn;

    @FXML
    private TextField t_com;

    @FXML
    private TextField t_ed;

    @FXML
    private TextField t_exp;

    @FXML
    private Button affichCv;

    @FXML
    void goToHome(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserHome.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


    }
    
    @FXML
    void goToProfil(MouseEvent event) throws IOException {
         Parent path = FXMLLoader.load(getClass().getResource("../view/UserProfil.fxml"));
                Scene scene = new Scene(path);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


    }

    @FXML
    void importCv(ActionEvent event) throws SQLException {
      Session session = Session.getInstance();
    String com=t_com.getText();
    String ed=t_ed.getText();
    String exp=t_exp.getText();
   
    
    UserService pst= new UserService()  ;
    User u1 = pst.getUtilisateur(session.getMail(), session.getPassword());
         int p_id_user;
         p_id_user = u1.getId_user();
         CV c = new CV(com,ed,exp,p_id_user);
         CvUserService s = new CvUserService();
         s.insert(c);
        

    }

    @FXML
    void pdfCv(ActionEvent event) throws SQLException, FileNotFoundException, DocumentException, IOException {
        Session session = Session.getInstance();
  
    UserService pst= new UserService()  ;
    User u1 = pst.getUtilisateur(session.getMail(), session.getPassword());
         int p_id_user = u1.getId_user();
    String sql = "select * from cv where id_user_id=?";
    ste=cnx.prepareStatement(sql);
    ste.setInt(1, p_id_user);
    ResultSet rst=ste.executeQuery();
    Document doc = new Document();
    PdfWriter.getInstance(doc, new FileOutputStream("./MonCv.pdf"));
    doc.open();
   
    doc.add(new Paragraph("   "));
    doc.add(new Paragraph(" ***************************************** Mon cv ************************************* "));
    doc.add(new Paragraph("   "));

    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(80);
    PdfPCell cell;
    
    cell = new PdfPCell(new Phrase("competances", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
   
    cell = new PdfPCell(new Phrase("experience", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    cell = new PdfPCell(new Phrase("education", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    while (rst.next()) {

        CV e = new CV();
      
        e.setCompétances(rst.getString("competances"));
        e.setExperiences(rst.getString("experience"));
        e.setÉducation(rst.getString("education"));
       
        cell = new PdfPCell(new Phrase(e.getCompétances(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(e.getExperiences(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(e.getÉducation(), FontFactory.getFont("Comic Sans MS", 14)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
       
   }
    doc.add(table);
    doc.close();
    Desktop.getDesktop().open(new File("./MonCv.pdf"));

}

 
    
      public void setUser() throws SQLException {
    // Initialiser les champs de texte avec les informations de l'utilisateur.
     Session session = Session.getInstance();
    name.setText(session.getNom()+" "+session.getPrénom());
    

    pst = cnx.prepareStatement("select * from user where id=?");
    pst.setInt(1, session.getId_user());
    rs = pst.executeQuery();
         while(rs.next()){
            user_url=rs.getString("image");
            if(user_url != null){
               user_image_profil.setImage(new Image(user_url));
            }
          else{
          File file1= new File("C:/Users/ounis/Desktop/freelanci/freelanci/src/image/user.png");
          Image image = new Image(file1.toURI().toString());
          user_image_profil.setImage(image);
          }   
     }} 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cnx=DataSource.getInstance().getCnx();
        try {
            setUser();
        } catch (SQLException ex) {
            Logger.getLogger(UserProfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
             setUser();
         } catch (SQLException ex) {
             Logger.getLogger(UserCvController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }    
    
}
