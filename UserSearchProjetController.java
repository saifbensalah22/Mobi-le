package controller;

import entity.Projet;
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
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import service.ProjetService;
import source.DataSource;

public class UserSearchProjetController implements Initializable {

    @FXML
    private TextField champ_titre;
    @FXML
    private TextField champ_description;
    @FXML
    private TextField champ_competance;
    @FXML
    private TextField champ_budget;
    @FXML
    private TextField champ_idprojet;
    @FXML
    private Button valider;
    @FXML
    private Button btn_stat;

    @FXML
    private TableView<Projet> tab_projet;
    @FXML
    private TableColumn<Projet, Integer> tab_idproj;
    @FXML
    private TableColumn<Projet, String> tab_titre;
    @FXML
    private TableColumn<Projet, String> tab_categ;
    @FXML
    private TableColumn<Projet, Integer> tab_budget;
    @FXML
    private TableColumn<Projet, String> tab_comp;
    @FXML
    private TableColumn<Projet, String> tab_desc;
    @FXML
    private TableColumn<Projet, String> tab_date;
    @FXML
    private TextField searchField;
    @FXML
    private Button btn_modif;
    @FXML
    private Button btn_supp;
    @FXML
    private Button btn_save;
    @FXML
    private ComboBox<String> sortBox = new ComboBox<>();
    @FXML
    private ComboBox<String> boxcateg = new ComboBox<>();
    @FXML
    private DatePicker datelim = new DatePicker();
    @FXML
    private Label btn_back;
    Connection mc;
    PreparedStatement ste;
    ObservableList<Projet> prolist;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_modif.setVisible(false);
        btn_supp.setVisible(false);
        btn_save.setVisible(false);
        entity.Session session = entity.Session.getInstance();
        String role = session.getRole();
        if (role.equals("ROLE_ADMIN"))
        {
            btn_modif.setVisible(true);
            btn_supp.setVisible(true);
            btn_save.setVisible(true);
        }

        ObservableList<String> items = FXCollections.observableArrayList(
                "ID",
                "Titre",
                "Prix"
        );
        sortBox.setItems(items);
        ObservableList<String> items2 = FXCollections.observableArrayList(
                "developement front-end",
                "developement back-end",
                "developement full-stack",
                "UX design"
        );
        boxcateg.setItems(items2);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> search());
        sortBox.setOnAction(event -> sort());

        afficher();
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        Parent path = FXMLLoader.load(getClass().getResource("../view/AdminInterface.fxml"));
        Scene scene = new Scene(path);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    
    @FXML
    private void checkmodif(ActionEvent event) throws SQLException {

        Projet clicked = tab_projet.getSelectionModel().getSelectedItem();
        int ref;
        float refref;
        champ_idprojet.setText(String.valueOf(clicked.getId_projet()));
        champ_titre.setText(clicked.getTitre());
        //boxcateg.setText(clicked.getCategorie());
        champ_budget.setText(String.valueOf(clicked.getPrix()));
        champ_competance.setText(clicked.getCompetences());
        champ_description.setText(clicked.getDescription());
        btn_save.setVisible(true);
        btn_modif.setVisible(false);

    }

    @FXML
    private void save(ActionEvent event) throws SQLException {

        entity.Session session = entity.Session.getInstance();
        String testid = champ_idprojet.getText();
        String testtitre = champ_titre.getText();
        String testcatec = boxcateg.getValue();
        String testbudg = champ_budget.getText();
        String testcomp = champ_competance.getText();
        String testdesc = champ_description.getText();
        LocalDate testDate = datelim.getValue();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (testtitre.isEmpty()) {
            champ_description.setText("choisis une titre");
        } else if (testcatec.isEmpty()) {
            champ_description.setText("met un une ctegorie");
        } else if (testid.isEmpty()) {
            champ_description.setText("select un projet");
        } else if (testbudg.isEmpty()) {
            champ_description.setText("met une prix");
        } else if (testcomp.isEmpty()) {
            champ_description.setText("met la competance");
        } else if (testdesc.isEmpty()) {
            champ_description.setText("met une description");
        } else if (datelim.getValue() == null) {
            champ_description.setText("select une date");
        } else {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {

                    Projet r1 = new Projet(Integer.parseInt(champ_idprojet.getText()), session.getId_user(), champ_titre.getText(), champ_description.getText(), Float.parseFloat(champ_budget.getText()), boxcateg.getValue(), champ_competance.getText());
                    ProjetService rs = new ProjetService();
                    LocalDate selectedDate = datelim.getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = selectedDate.format(formatter);
                    r1.setDatelim(formattedDate);
                    rs.insert(r1);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(UserSearchProjetController.class.getName()).log(Level.SEVERE, null, ex);
                }
                refresh();
            }

        }
    }

    @FXML
    private void checkdelete(ActionEvent event) throws SQLException {

        Projet clicked = tab_projet.getSelectionModel().getSelectedItem();
        //System.out.println("ce projet :::::::"+clicked);
        ProjetService ps = new ProjetService();
        ps.delete(clicked);
        //System.out.println("suppresion avec succes");
        refresh();

    }

    private void search() {
        String query = searchField.getText();
        ObservableList<Projet> filteredList = FXCollections.observableArrayList();
        for (Projet projet : prolist) {
            if (projet.getTitre().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(projet);
            }
        }
        tab_projet.setItems(filteredList);
    }

    private void sort() {
        String selectedOption = sortBox.getValue();
        if (selectedOption == null) {
            return;
        }
        switch (selectedOption) {
            case "ID":
                prolist.sort((p1, p2) -> Integer.compare(p1.getId_projet(), p2.getId_projet()));
                break;
            case "Titre":
                prolist.sort((p1, p2) -> p1.getTitre().compareToIgnoreCase(p2.getTitre()));
                break;
            case "Budget":
                prolist.sort((p1, p2) -> Float.compare(p1.getPrix(), p2.getPrix()));
                break;
            default:
                break;
        }
        tab_projet.setItems(prolist);
    }

    private void afficher() {
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();

        try {
            String requete = "select * from projet e";
            Statement st;
            st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Projet e;
                e = new Projet();

                e.setId_projet(rs.getInt("id_projet"));

                e.setTitre(rs.getString("titre"));

                e.setDescription(rs.getString("description"));

                e.setPrix(rs.getInt("prix"));

                e.setCategorie(rs.getString("categorie"));

                e.setCompetences(rs.getString("competence"));

                e.setDatelim(rs.getString("datelim"));

              //  System.out.println("les projets ajoutees :" + e.toString());
                prolist.add(e);

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tab_idproj.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId_projet()));
        tab_titre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));
        tab_categ.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie()));
        tab_budget.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getPrix()));
        tab_comp.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCompetences()));
        tab_desc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        tab_date.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatelim()));

        tab_projet.setItems(prolist);
        refresh();
    }

    public void refresh() {
        prolist.clear();
        mc = DataSource.getInstance().getCnx();
        prolist = FXCollections.observableArrayList();

        try {
            String requete = "select * from projet e";
            Statement st = DataSource.getInstance().getCnx()
                    .createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Projet e;
                e = new Projet();

                e.setId_projet(rs.getInt("id_projet"));

                e.setTitre(rs.getString("titre"));

                e.setDescription(rs.getString("description"));

                e.setPrix(rs.getInt("prix"));

                e.setCategorie(rs.getString("categorie"));

                e.setCompetences(rs.getString("competence"));

                e.setDatelim(rs.getString("datelim"));

               // System.out.println("les projets ajoutees :" + e.toString());
                prolist.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        tab_projet.setItems(prolist);

    }

    @FXML
    private void checkadd() {

        String testtitre = champ_titre.getText();
        String testcatec = boxcateg.getValue();
        String testbudg = champ_budget.getText();
        String testcomp = champ_competance.getText();
        String testdesc = champ_description.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (testtitre.isEmpty()) {
            champ_titre.setText("choisis une titre");
        } else if (testcatec.isEmpty()) {
            champ_budget.setText("met un une ctegorie");
        } else if (testbudg.isEmpty()) {
            champ_budget.setText("met une budget");
        } else if (testcomp.isEmpty()) {
            champ_competance.setText("met la competance");
        } else if (testdesc.isEmpty()) {
            champ_description.setText("met une description");
        } else {
            entity.Session session = entity.Session.getInstance();
            int id_ref = session.getId_user();

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {

                    Projet r1 = new Projet(session.getId_user(), champ_titre.getText(), champ_description.getText(), Float.parseFloat(champ_budget.getText()), boxcateg.getValue(), champ_competance.getText());
                    LocalDate selectedDate = datelim.getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = selectedDate.format(formatter);
                    r1.setDatelim(formattedDate);
                    r1.setId_user_id(id_ref);
                    ProjetService rs = new ProjetService();
                    rs.insert(r1);
                    //handle();

                } catch (NumberFormatException ex) {
                    Logger.getLogger(UserSearchProjetController.class.getName()).log(Level.SEVERE, null, ex);
                }
                refresh();
            }

        }

    }

    public void handle() {
        // Recipient's email address
        String to = "dorra.ferah@esprit.tn";
        // Sender's email address
        String from = "freelanciprojet@outlook.com";
        // Sender's email password
        String password = "aqwzsx@123456";

        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a new session with an authenticator
        Session session;
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            // Create a new message
            Message message = new MimeMessage(session);
            // Set the sender, recipient, subject and body of the message
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Creaction Projet");
            message.setText("Votre Projet a etait crée!");

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error message: " + e.getMessage());
        }
    }

    @FXML
    private void generateStatisticsPopup() {
        ObservableList<Projet> sortedList = prolist.sorted((p1, p2) -> Float.compare(p2.getPrix(), p1.getPrix()));
        PieChart chart = new PieChart();
        chart.setTitle("Top Projects with Biggest Budgets");

        float totalBudget = 0.0f;
        for (Projet projet : sortedList) {
            totalBudget += projet.getPrix();
        }

        int count = 0;
        for (Projet projet : sortedList) {
            if (count == 5) {
                break;
            }

            float percentage = (projet.getPrix() / totalBudget) * 100;
            chart.getData().add(new PieChart.Data(projet.getTitre() + " (" + String.format("%.2f", percentage) + "%)", projet.getPrix()));
            count++;
        }

        Scene scene = new Scene(new Group(chart));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
