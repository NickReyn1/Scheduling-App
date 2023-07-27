package Nick.Controller;

import Helper.DBHelper;
import Helper.JDBC;
import Nick.Modal.Countries;
import Nick.Modal.Customer;
import Nick.Modal.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *class that controls the modify customer screen
 */
public class Modify_CustomerController implements Initializable {

    public TextField nameText;
    public TextField addressText;
    public TextField postalCodeText;
    public TextField phoneText;
    public ComboBox<Divisions> stateCombo;
    public ComboBox<Countries> countryCombo;
    public TextField customerIdText;
    public TableView<Customer> customerTable;
    public TableColumn customerIdColumn;
    public TableColumn customerNameColumn;
    public TableColumn postalCodeColumn;
    public TableColumn phoneColumn;
    public TableColumn addressColumn;
    public TableColumn stateColumn;
    public TableColumn countryColumn;
    public Button saveButton;
    public Button cancelButton;
    public Button deleteButton;
    ObservableList<Customer> custTable = DBHelper.getCustomers();
    ObservableList<Divisions> divList = FXCollections.observableArrayList();
    ObservableList<Countries> countList = DBHelper.getCountries();

    /**
     *method that runs on the opening of the modify customer screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(custTable);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        stateColumn.setCellValueFactory(new  PropertyValueFactory<>("divisionName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));

        this.nameText.setText(Main_ScreenController.selectedCust.getCustomerName());
        this.addressText.setText(Main_ScreenController.selectedCust.getAddress());
        this.postalCodeText.setText(Main_ScreenController.selectedCust.getPostalCode());
        this.phoneText.setText(Main_ScreenController.selectedCust.getPhone());
        this.customerIdText.setText(Main_ScreenController.selectedCust.getCustomerId());
        Divisions div = null;
        Countries count = null;
        this.countryCombo.setItems(countList);
        for(Countries x: countList){
            if(x.getCountryID()== Main_ScreenController.selectedCust.getCountryId()){
                count = x;
                break;
            }
        }
        this.countryCombo.setValue(count);
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

        for(Divisions x: DBHelper.getDivisions()) {
            if (x.getCountryID() == countryCombo.getValue().getCountryID()) {
                divList.add(x);
            }
        }
        this.stateCombo.setItems(divList);

        for(Divisions y: divList){
            if(y.getDivisionID() == Main_ScreenController.selectedCust.getDivisionId()){
                div = y;
                break;
            }
        }

        this.stateCombo.setValue(div);
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

    /**
     * method that updates a customer record in the database when the save button is clicked.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onSaveClicked(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            String statement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement prepStat = JDBC.getConnection().prepareStatement(statement);
            prepStat.setString(1, nameText.getText());
            prepStat.setString(2, addressText.getText());
            prepStat.setString(3, postalCodeText.getText());
            prepStat.setString(4, phoneText.getText());
            Date timestamp = new Date(System.currentTimeMillis());
            prepStat.setDate(5, timestamp);
            prepStat.setString(6, LoginController.currentUser.getUser_Name());
            prepStat.setInt(7, stateCombo.getValue().getDivisionID());
            prepStat.setInt(8, Integer.parseInt(customerIdText.getText()));

            prepStat.execute();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "One of your input values might be the wrong format!");
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
     * method that returns the scene to the main screen when the cancel button is clicked.
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
     * method that deletes a customer and all associated appointments when the delete button is clicked.
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteClicked(ActionEvent actionEvent) throws SQLException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this customer and all of their appointments?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                String sql = "DELETE FROM appointments WHERE Customer_ID = ?";
                PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(customerTable.getSelectionModel().getSelectedItem().getCustomerId()));
                statement.execute();

                String sql2 = "DELETE FROM customers WHERE Customer_ID = ?";
                PreparedStatement statement2 = JDBC.getConnection().prepareStatement(sql2);
                statement2.setInt(1, Integer.parseInt(customerTable.getSelectionModel().getSelectedItem().getCustomerId()));
                statement2.execute();
                custTable.remove(customerTable.getSelectionModel().getSelectedItem());
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected a customer from the table!");
            alert.showAndWait();
        }
    }

    /**
     * method that sets the first level divisions in the assocated combo box based on the country selected.
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
