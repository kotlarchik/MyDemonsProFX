package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.ConnectionDataBase;
import sample.model.Person;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    ObservableList<Person> personList = FXCollections.observableArrayList();

//    DataBase Connection;
    Connection connection = null;
    public Controller(){
        connection = new ConnectionDataBase().getConnection();
    }


//    AnchorPane(person_table);
    @FXML
    private TableView<Person> tablePerson;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, String> firstNameColumn;

//    AnchorPane(person_profile);
    @FXML
    private Label labelLastName;
    @FXML
    private Label labelFirstName;
    @FXML
    private Label labelStreet;
    @FXML
    private Label labelCity;
    @FXML
    private Label labelPostalCode;
    @FXML
    private Label labelBirthday;
    @FXML
    private ImageView personImage;
    @FXML
    private Label labelId;
    @FXML
    private Button newUser;
    @FXML
    private Button editUser;

//    method show lastName and firstName from table;
    public void initialize(){
        try{
            String sqlSelect = "SELECT * FROM person";
            PreparedStatement statement = connection.prepareStatement(sqlSelect);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){ personList.add(new Person(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)));
            }
            showPersonDetails(null);

            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tablePerson.setItems(personList);


    // Слушаем изменения выбора, и при изменении отображаем дополнительную информацию об адресате.
            tablePerson.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    method show full information of person;
    private void showPersonDetails(Person person){
        if (person != null){
            labelLastName.setText(person.getLastName());
            labelFirstName.setText(person.getFirstName());
            labelStreet.setText(person.getStreet());
            labelCity.setText(person.getCity());
            labelPostalCode.setText(person.getPostalCode());
            labelBirthday.setText(person.getBirthday());
            try {
                personImage.setImage(new Image(new FileInputStream(person.getImage())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            labelLastName.setText("Пусто");
            labelFirstName.setText("Пусто");
            labelStreet.setText("Пусто");
            labelCity.setText("Пусто");
            labelPostalCode.setText("Пусто");
            labelBirthday.setText("Пусто");
            try {
                personImage.setImage(new Image(new FileInputStream("image/2.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void newUserButton(ActionEvent event) throws IOException {
        newUser.getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = new FXMLLoader().load(getClass().getResource("/sample/view/personInsert.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("New user");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/icon.jpg")));
        stage.showAndWait();
    }

    public void editUserButton(ActionEvent event) throws IOException {
        editUser.getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = new FXMLLoader().load(getClass().getResource("/sample/view/personEdit.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Edit user");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/icon.jpg")));
        stage.showAndWait();
    }

    public void deleteUserButton(ActionEvent event) throws SQLException {
        int selectedIndex = tablePerson.getSelectionModel().getSelectedIndex();
        Person person = tablePerson.getSelectionModel().getSelectedItem();

        if (selectedIndex >= 0){
            tablePerson.getItems().remove(selectedIndex);
        }

        String sqlDelete = "DELETE FROM person WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sqlDelete);
        statement.setInt(1,person.getId());
        statement.executeUpdate();
        }
    }
