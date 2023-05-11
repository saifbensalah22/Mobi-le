package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class GererUserController {

    @FXML
    private TableView<?> tb_user;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_name;

    @FXML
    private TableColumn<?, ?> col_mail;

    @FXML
    private TableColumn<?, ?> col_pswd;

    @FXML
    private TableColumn<?, ?> col_phone;

    @FXML
    private TableColumn<?, ?> col_addresse;

    @FXML
    private ComboBox<?> comboBox_userid;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_set;

    @FXML
    private TextField tf_searchUser;

    @FXML
    private Label btn_retour;

    @FXML
    void SearchUser(ActionEvent event) {

    }

    @FXML
    void goBack(MouseEvent event) {

    }

    @FXML
    void goToSetUserInterface(ActionEvent event) {

    }

    @FXML
    void selected(MouseEvent event) {

    }

}
