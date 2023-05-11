/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import entity.Session;
import entity.User;
import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.mail.MessagingException;
import service.SendVerificationEmail;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author ounis
 */
public class UserSelectedProfilController implements Initializable {

       @FXML
    private ImageView img;

 
    @FXML
    private Label nom;

    @FXML
    private Label numTel;

    @FXML
    private Label adresse;

    @FXML
    private Label mail;

    @FXML
    private Label prenom;
      
      
    @FXML
    private Button cv;
   
    

    
    Connection cnx;  
    ResultSet rs;
    PreparedStatement ste;
    
    
    @FXML
    private Button contacter;

    @FXML
    public void envoyerMail(ActionEvent event) throws MessagingException {
        
        Session session = Session.getInstance();
        
        String senderemail = session.getMail();         //session.getuseremail
        String senderPassword = "13493394";     //seesion.getuserpassword
        String destinataire = mail.getText() ;          //mail.gettext
        SendVerificationEmail sendVerificationEmail= new SendVerificationEmail();
        sendVerificationEmail.envoyerMail(senderemail,senderPassword,destinataire);
        
    }
    
 
    @FXML
    void showCv(ActionEvent event) throws SQLException, FileNotFoundException, DocumentException, IOException {
        
     
        
         String Email = mail.getText(); 
         UserService p= new UserService()  ;
         User u1 = p.getUserByMail(Email);
         
         
    int p_id_user = u1.getId_user();
    String sql = "select * from cv where id=?";
    ste=cnx.prepareStatement(sql);
    ste.setInt(1, p_id_user);
    ResultSet rst=ste.executeQuery();
    Document doc = new Document();
    PdfWriter.getInstance(doc, new FileOutputStream("./Cv.pdf"));
    doc.open();
   
    doc.add(new Paragraph("   "));
    doc.add(new Paragraph(" *************************************** CV ************************************* "));
    doc.add(new Paragraph("   "));

    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(80);
    PdfPCell cell;
    
    cell = new PdfPCell(new Phrase("compétences", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
   
    cell = new PdfPCell(new Phrase("expériences", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    cell = new PdfPCell(new Phrase("éducation", FontFactory.getFont("Comic Sans MS", 14)));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    table.addCell(cell);
    
    while (rst.next()) {

        CV e = new CV();
      
        e.setCompétances(rst.getString("compétances"));
        e.setExperiences(rst.getString("experiences"));
        e.setÉducation(rst.getString("éducation"));
       
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
    Desktop.getDesktop().open(new File("./Cv.pdf"));

}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setUser(User utilisateur) throws SQLException {
    nom.setText(utilisateur.getNom());
    prenom.setText(utilisateur.getprénom());
    mail.setText(utilisateur.getMail());
    numTel.setText(Integer.toString(utilisateur.getNuméroTéléphone()));
    adresse.setText(utilisateur.getAdresse());
    String m = mail.getText();

    if (utilisateur.getImage() != null) {

String imagePath = utilisateur.getImage();       
File imageFile = new File(imagePath);  
Image image = new Image(imageFile.toURI().toString());
img.setImage(image);


    }
     }
    
  public Image convertStringToImage(String imageString) {
    try {
        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return new Image(bis);
    } catch (IllegalArgumentException ex) {
        System.err.println("Erreur lors de la conversion de la chaîne d'encodage en image : " + ex.getMessage());
        return null;
    }
}
  
   
  
    
}
