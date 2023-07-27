package Nick.Controller;

import Helper.DBHelper;
import Helper.JDBC;
import Nick.Modal.Appointment;
import Nick.Modal.Contact;
import Nick.Modal.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *class that controls the main screen
 */
public class Main_ScreenController implements Initializable {
    public ComboBox<Contact> contactCombo;
    public TextArea reportTextArea;
    public TextField monthTextField;
    public TextField appointmentTypeTextField;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn customerIdColumn;
    @FXML
    private TableColumn customerNameColumn;
    @FXML
    private TableColumn addressColumn;
    @FXML
    private TableColumn phoneColumn;
    @FXML
    private TableColumn postalCodeColumn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn appointmentIdColumn;
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn descriptionColumn;
    @FXML
    private TableColumn locationColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn startDateColumn;
    @FXML
    private TableColumn startTimeColumn;
    @FXML
    private TableColumn endDateColumn;
    @FXML
    private TableColumn endTimeColumn;
    @FXML
    private TableColumn customerIdColumn2;
    @FXML
    private TableColumn userIdColumn;
    ObservableList<Customer> custTable = DBHelper.getCustomers();
    ObservableList<Appointment> appTable = DBHelper.getAppointments();
    public static Customer selectedCust;
    public static Appointment selectedApp;
    ObservableList<Contact> contList = DBHelper.getContacts();


    /**
     *method that runs on the opening of the main screen. Fills tables with results and checks to see if there is an upcoming appointment.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerTable.setItems(custTable);
        appointmentTable.setItems(appTable);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));


        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        customerIdColumn2.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));

        this.contactCombo.setItems(contList);
        contactCombo.setConverter(new StringConverter<Contact>() {

            @Override
            public String toString(Contact object) {
                return object.getContact_Name();
            }

            @Override
            public Contact fromString(String string) {
                return contactCombo.getValue();
            }
        });
        Boolean appFound = false;
        for(Appointment app: appTable){
            if(LoginController.loginTime.isBefore(app.getStartTime()) &&LoginController.loginTime.isAfter(app.getStartTime().minusMinutes(15))){
                Alert alert = new Alert(Alert.AlertType.WARNING, "You have an upcoming appointment!" + " Appointment ID: " + app.getAppointment_ID() +" Appointment Date: " + app.getStartDate() +" Appointment Time: " + app.getStartTime());
                alert.showAndWait();
                appFound = true;
            }
        }
        if(appFound = false){
            Alert alert = new Alert( Alert.AlertType.INFORMATION, "You have no appointments in the next 15 minutes!");
            alert.showAndWait();
        }
    }

    /**
     * method that runs when add customer button is clicked and switches the scene to the add customer form.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomerClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Add_Customer.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method that switches the scene to the modify customer form when the button is clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyCustomerClicked(ActionEvent actionEvent) throws IOException {
        try {
            selectedCust = customerTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Modify_Customer.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected a customer from the table!");
            alert.showAndWait();
        }
    }

    /**
     * Method that deletes a customer from the database and deletes all of the appointments made by that customer.
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteCustomerClicked(ActionEvent actionEvent) throws SQLException {
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
                appTable = DBHelper.getAppointments();
                appointmentTable.setItems(appTable);
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected a customer from the table!");
            alert.showAndWait();
        }
    }

    /**
     * method that changes the scene to the add appointment form when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void onAddAppointmentClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Add_Appointment.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method that changes the scene to the modify appointment form when the button is clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyAppointmentClicked(ActionEvent actionEvent) throws IOException {
        try {
            selectedApp = appointmentTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Modify_Appointment.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected an appointment from the table!");
            alert.showAndWait();
        }
    }

    /**
     * deletes an appointment from the database when the button is clicked.
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteAppointmentClicked(ActionEvent actionEvent) throws SQLException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this appointment? Appointment ID: " + appointmentTable.getSelectionModel().getSelectedItem().getAppointment_ID() + " Appointment Type: "+ appointmentTable.getSelectionModel().getSelectedItem().getType() , ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                String sql = "DELETE FROM appointments WHERE Appointment_Id = ?";
                PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
                statement.setInt(1, appointmentTable.getSelectionModel().getSelectedItem().getAppointment_ID());
                statement.execute();
                appTable.remove(appointmentTable.getSelectionModel().getSelectedItem());
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected an appointment from the table!");
            alert.showAndWait();
        }
    }

    /**
     * filters the appointment table by a certain month when the monthly radio button is clicked
     * @param actionEvent
     */
    public void onMonthlyClicked(ActionEvent actionEvent) {
        ObservableList newAppTable = FXCollections.observableArrayList();
        LocalDate now = LocalDate.now();
        for(Appointment app: appTable){
            if(app.getStartDate().getMonthValue() == now.getMonthValue() ){
                newAppTable.add(app);
            }

        }
        appointmentTable.setItems(newAppTable);
    }

    /**
     * filters the appointment table down to the appointments occurring in the current week when the weekly radio button is clicked.
     * @param actionEvent
     */
    public void onWeeklyClicked(ActionEvent actionEvent) {
        ObservableList newAppTable = FXCollections.observableArrayList();
        LocalDate now = LocalDate.now();
        for(Appointment app: appTable){
            if(app.getStartDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) ){
                newAppTable.add(app);
            }

        }
        appointmentTable.setItems(newAppTable);
    }

    /**
     * sets the appointment table to show all of the appointments
     * @param actionEvent
     */
    public void onAllClicked(ActionEvent actionEvent) {
        appointmentTable.setItems(appTable);
    }

    /**
     * creates a report that shows the numbers of appointments of a certain type in a month.
     * @param actionEvent
     */
    public void onAppointmentTypeButtonClick(ActionEvent actionEvent) {
        reportTextArea.clear();
        int count = 0;
        LocalDate month = null;
        for(Appointment app: appTable){
            if(app.getStartDate().getMonthValue() == Integer.parseInt(monthTextField.getText())){
                if(app.getType().equals(appointmentTypeTextField.getText()) ){
                    count++;
                    month = app.getStartDate();
                }
            }
        }
        if(month != null) {
            reportTextArea.setText(month.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + appointmentTypeTextField.getText() + " " + count);
        }
        else{
            reportTextArea.setText("No appointments in that month could be found.");
        }

    }

    /**
     * creates a report that shows the appointments for a certain contact. Uses lambdas to simplify the iteration of the appointment table and the appending of the reports text to the text area.
     * @param actionEvent
     */
    public void onContactScheduleButtonClick(ActionEvent actionEvent) {
        reportTextArea.clear();
        AtomicInteger count = new AtomicInteger( 1);
        try {
            appTable.forEach( app -> {
                if (app.getContact_ID() == contactCombo.getValue().getContact_ID()) {
                    reportTextArea.appendText(count.getAndIncrement() + ": Appointment ID: " + app.getAppointment_ID() + " Title: " + app.getTitle() + " Type: " + app.getType() + " Description: " + app.getDescription() + " Start Date: " + app.getStartDate() + " Start Time: " + app.getStartTime() + " Customer ID: " + app.getCustomer_ID() +"\n");
                }
            });
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected a contact from the comboBox.");
            alert.showAndWait();
        }
    }

    /**
     * Creates a report that lists all of the customers phones and addresses. Uses lambdas to simplify the iteration of the customerTable and the appending of the reports text to the text area.
     * @param actionEvent
     */
    public void onCustomerListButtonClicked(ActionEvent actionEvent) {
        reportTextArea.clear();
        custTable.forEach( cust -> {reportTextArea.appendText("Customer Name:  " + cust.getCustomerName() +" Phone: "+ cust.getPhone() +" Address: "+ cust.getAddress() + "\n");});

    }

}
