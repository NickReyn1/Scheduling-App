package Nick.Controller;

import Helper.DBHelper;
import Helper.JDBC;
import Nick.Modal.Countries;
import Nick.Modal.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * class that controls the add customer screen
 */
public class Add_CustomerController implements Initializable {

    public TextField nameText;
    public TextField addressText;
    public TextField postalCodeText;
    public TextField phoneText;
    public ComboBox<Divisions> stateCombo;
    public ComboBox<Countries> countryCombo;
    public Button saveButton;
    public Button cancelButton;

    /**
     *method that runs on the opening of the add customer screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //this.stateCombo.setItems(DBHelper.getDivisions());
        //this.countryCombo.setItems(DBHelper.getCountries());

        this.countryCombo.setItems(DBHelper.getCountries());
        countryCombo.setConverter(new StringConverter<Countries>() {

            @Override
            public String toString(Countries object){
                return object.getCountryName();
            }
            @Override
            public Countries fromString(String string){
                return countryCombo.getValue();
            }
        });
    }

    /**
     * method that runs when save button is clicked. Inserts a new customer into the database based on the data entered into the forms text fields.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onSaveClicked(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            String statement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepStat = JDBC.getConnection().prepareStatement(statement);
            prepStat.setString(1, nameText.getText());
            prepStat.setString(2, addressText.getText());
            prepStat.setString(3, postalCodeText.getText());
            prepStat.setString(4, phoneText.getText());
            Date timestamp = new Date(System.currentTimeMillis());
            prepStat.setDate(5, timestamp);
            prepStat.setString(6, LoginController.currentUser.getUser_Name());
            prepStat.setDate(7, timestamp);
            prepStat.setString(8, LoginController.currentUser.getUser_Name());
            prepStat.setInt(9, stateCombo.getValue().getDivisionID());

            prepStat.execute();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "One of your inputs might be in the wrong format!");
            alert.showAndWait();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Main_Screen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method that changes the scene back to the main screen when the cancel button is clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Main_Screen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method that assigns divisions to the division combobox depending on which country is selected.
     * @param actionEvent
     */
    public void onCountrySelected(ActionEvent actionEvent) {
        ObservableList divisionList = FXCollections.observableArrayList();

        for(Divisions div: DBHelper.getDivisions()){
            if(div.getCountryID() == countryCombo.getValue().getCountryID()){
                divisionList.add(div);
            }
        }
        this.stateCombo.setItems(divisionList);
        stateCombo.setConverter(new StringConverter<Divisions>() {

            @Override
            public String toString(Divisions object){
                return object.getDivisionName();
            }
            @Override
            public Divisions fromString(String string){
                return stateCombo.getValue();
            }
        });
    }
}
