package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.ConnectionDataBase;
import sample.Main;
import sample.model.Person;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.zip.CheckedOutputStream;

public class PersonEdit {

    Connection connection = null;
    public PersonEdit(){
        connection = new ConnectionDataBase().getConnection();
    }

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtStreet;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtPostalIndex;
    @FXML
    private TextField txtBirthday;

    @FXML
    private Button executeFX;
    @FXML
    private Button backFX;
    @FXML
    private TextField imagePath;

    public void updateButton(ActionEvent event) throws SQLException {
        String sqlEdit = "update person set first_name=?,last_name=?, street=?, city=?,postal_code=?,birthday=?,image=? where id=?";
        PreparedStatement statement = connection.prepareStatement(sqlEdit);
        statement.setString(1, txtFirstName.getText());
        statement.setString(2, txtLastName.getText());
        statement.setString(3, txtStreet.getText());
        statement.setString(4, txtCity.getText());
        statement.setString(5, txtPostalIndex.getText());
        statement.setString(6, txtBirthday.getText());
        statement.setString(7,imagePath.getText());
        statement.setInt(8,Integer.parseInt(txtId.getText()));

        statement.executeUpdate();

    }
    public void backButton(ActionEvent event) throws IOException {
        backFX.getScene().getWindow().hide();
        Stage stage = new Stage();
        BorderPane borderPane = FXMLLoader.load(getClass().getResource("/sample/view/startMenu.fxml"));
        stage.setScene(new Scene(borderPane));
        stage.setTitle("Person Cart");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/icon.jpg")));
        stage.show();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/sample/view/sample.fxml"));
        AnchorPane anchorPane = (AnchorPane) loader.load();
        borderPane.setCenter(anchorPane);
    }
    public void searchButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Поиск фотографии");
        File dir = fileChooser.showOpenDialog(new Stage());
        if (!(dir == null)) {
            imagePath.setText(dir.getAbsolutePath());
        }
    }
}

