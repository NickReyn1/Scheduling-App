package Nick.Controller;

import Helper.DBHelper;
import Helper.JDBC;
import Nick.Modal.*;
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
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.IsoFields;
import java.util.ResourceBundle;

/**
 *class that controls the modify appointment screen
 */
public class Modify_AppointmentController implements Initializable {

    public TextField titleText;
    public TextField descriptionText;
    public TextField locationText;
    public ComboBox<Contact> contactCombo;
    public DatePicker dateSelector;
    public TextField startTimeText;
    public TextField endTimeText;
    public Button saveButton;
    public Button cancelButton;
    public TextField appointmentIdText;
    public TableView<Appointment> appointmentTable;
    public TableColumn appointmentIdColumn;
    public TableColumn titleColumn;
    public TableColumn descriptionColumn;
    public TableColumn locationColumn;
    public TableColumn typeColumn;
    public TableColumn startDateColumn;
    public TableColumn startTimeColumn;
    public TableColumn endDateColumn;
    public TableColumn endTimeColumn;
    public TableColumn customerIdColumn;
    public TableColumn userIdColumn;
    public Button deleteButton;
    public RadioButton monthlyRadioButton;
    public RadioButton weeklyRadioButton;
    public ComboBox<Customer> customerCombo;
    public ComboBox<User> userCombo;
    public TextField typeText;

    ObservableList<Appointment> appTable = DBHelper.getAppointments();
    ObservableList<Customer> custList = DBHelper.getCustomers();
    ObservableList<User> userList = DBHelper.getUsers();
    ObservableList<Contact> contList = DBHelper.getContacts();

    /**
     *method that runs on the openining of the modify appointment screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTable.setItems(appTable);

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("User_ID"));

        this.appointmentIdText.setText(Integer.toString(Main_ScreenController.selectedApp.getAppointment_ID()));
        this.titleText.setText(Main_ScreenController.selectedApp.getTitle());
        this.descriptionText.setText(Main_ScreenController.selectedApp.getDescription());
        this.locationText.setText(Main_ScreenController.selectedApp.getLocation());
        this.typeText.setText(Main_ScreenController.selectedApp.getType());
        this.dateSelector.setValue(Main_ScreenController.selectedApp.getEndDate());
        this.startTimeText.setText(Main_ScreenController.selectedApp.getStartTime().toString());
        this.endTimeText.setText(Main_ScreenController.selectedApp.getEndTime().toString());
        this.contactCombo.setItems(DBHelper.getContacts());
        this.customerCombo.setItems(custList);
        this.contactCombo.setItems(contList);
        this.userCombo.setItems(userList);
        User user = null;
        Customer customer = null;
        Contact contact = null;
        for(Customer x: custList){
            if(Integer.parseInt(x.getCustomerId()) == Main_ScreenController.selectedApp.getCustomer_ID()){
                customer = x;
                break;
            }
        }
        for(User x: userList){
            if(x.getUser_ID() == Main_ScreenController.selectedApp.getUser_ID()){
                user = x;
                break;
            }
        }
        for(Contact x: contList){
            if(x.getContact_ID()== Main_ScreenController.selectedApp.getContact_ID()){
                contact = x;
                break;
            }
        }
        this.userCombo.setValue(user);
        this.customerCombo.setValue(customer);
        this.contactCombo.setValue(contact);
        customerCombo.setConverter(new StringConverter<Customer>() {

            @Override
            public String toString(Customer object){
                return object.getCustomerName();
            }
            @Override
            public Customer fromString(String string){
                return customerCombo.getValue();
            }
        });
        contactCombo.setConverter(new StringConverter<Contact>() {

            @Override
            public String toString(Contact object){
                return object.getContact_Name();
            }
            @Override
            public Contact fromString(String string){
                return contactCombo.getValue();
            }
        });
        userCombo.setConverter(new StringConverter<User>() {

            @Override
            public String toString(User object){
                return object.getUser_Name();
            }
            @Override
            public User fromString(String string){
                return userCombo.getValue();
            }
        });

    }

    /**
     * updates a record in the database using the data entered in the forms text fields.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void onSaveClicked(ActionEvent actionEvent) throws IOException, SQLException {
        boolean noErrors = true;
        LocalDate startDate = dateSelector.getValue();
        LocalTime startTime = LocalTime.parse(startTimeText.getText());
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalTime endTime = LocalTime.parse(endTimeText.getText());
        LocalDateTime end = LocalDateTime.of(startDate, endTime);
        ZonedDateTime businessStart = LocalDateTime.of(startDate, LocalTime.of(8,0,0)).atZone(ZoneId.of("-05:00"));
        ZonedDateTime businessEnd = LocalDateTime.of(startDate, LocalTime.of(22,0,0)).atZone(ZoneId.of("-05:00"));

        try {
            String statement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement prepStat = JDBC.getConnection().prepareStatement(statement);
            prepStat.setString(1, titleText.getText());
            prepStat.setString(2, descriptionText.getText());
            prepStat.setString(3, locationText.getText());
            prepStat.setString(4, typeText.getText());

            Boolean overlapAlertShown = false;
            try {
                ZonedDateTime zonedStart = ZonedDateTime.of(start, ZoneId.systemDefault());
                ZonedDateTime zonedEnd = ZonedDateTime.of(end, ZoneId.systemDefault());
                if(zonedStart.withZoneSameInstant(ZoneId.of("-05:00")).isAfter(businessEnd) || zonedStart.withZoneSameInstant(ZoneId.of("-05:00")).isBefore(businessStart)){
                    if(zonedEnd.withZoneSameInstant(ZoneId.of("-05:00")).isAfter(businessEnd) || zonedEnd.withZoneSameInstant(ZoneId.of("-05:00")).isBefore(businessStart)){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "This appointment is outside our business hours.");
                        alert.showAndWait();
                        noErrors = false;
                    }
                }
                else{
                    Boolean valid = false;
                    for(Appointment app: DBHelper.getAppointments()){
                        if((app.getAppointment_ID() != Integer.parseInt(this.appointmentIdText.getText())) && app.getStartDate().equals(this.dateSelector.getValue()) && (!(startTime.isBefore(app.getStartTime()) && endTime.isBefore(app.getStartTime())) && !(startTime.isAfter(app.getEndTime()) && endTime.isAfter(app.getEndTime())))){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "START This appointment overlaps with another appointment.");
                            alert.showAndWait();
                            noErrors = false;
                        }
                        else{
                            valid = true;
                        }
                    }
                    if(valid == true) {
                        prepStat.setTimestamp(5, Timestamp.valueOf(start));
                        prepStat.setTimestamp(6, Timestamp.valueOf(end));
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error with your input start time. Make sure your value is in the format HH/MM/SS " + e );
                alert.showAndWait();
                noErrors = false;
            }
            Date timestamp = new Date(System.currentTimeMillis());
            prepStat.setDate(7, timestamp);
            prepStat.setString(8, LoginController.currentUser.getUser_Name());
            prepStat.setDate(9, timestamp);
            prepStat.setString(10, LoginController.currentUser.getUser_Name());
            prepStat.setInt(11, Integer.parseInt(customerCombo.getValue().getCustomerId()));
            prepStat.setInt(12, userCombo.getValue().getUser_ID());
            prepStat.setInt(13, contactCombo.getValue().getContact_ID());
            prepStat.setInt(14, Integer.parseInt(this.appointmentIdText.getText()));
            if(noErrors == true) {
                prepStat.execute();
            }

        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "One of your inputs might be in the wrong format!" + e);
            alert.showAndWait();
            noErrors = false;
        }
        if(noErrors == true) {
            Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Main_Screen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * method that sets the scene to the main screen when the cancel button is clicked.
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
     * method that deletes an appointment from the database when the delete button is clicked.
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteClicked(ActionEvent actionEvent) throws SQLException {
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
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure you've selected an appointment from the table!");
            alert.showAndWait();
        }
    }

    /**
     * method that filters the appointment table by the current month when monthly radio button is clicked.
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
     * method that filters the appointment table by the current week when the weekly radio button is clicked.
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
     * method that sets the appointment table to all appointments.
     * @param actionEvent
     */
    public void onAllClicked(ActionEvent actionEvent) {
        appointmentTable.setItems(appTable);
    }
}
