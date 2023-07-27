package Nick.Controller;

import Helper.DBHelper;
import Helper.JDBC;
import Nick.Modal.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.*;
import java.util.ResourceBundle;


/**
 * Class that controls the add appointment screen
 */
public class Add_AppointmentController implements Initializable {
    public TextField titleText;
    public TextField descriptionText;
    public TextField locationText;
    public ComboBox<Contact> contactCombo;
    public DatePicker dateSelector;
    public TextField startTimeText;
    public TextField endTimeText;
    public Button saveButton;
    public Button cancelButton;
    public ComboBox<Customer> customerCombo;
    public ComboBox<User> userCombo;
    public TextField typeText;

    /**
     * method that runs on the opening of the add appointment screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.customerCombo.setItems(DBHelper.getCustomers());
        this.contactCombo.setItems(DBHelper.getContacts());
        this.userCombo.setItems(DBHelper.getUsers());
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
     * method that runs when the save button is clicked. Inserts a new appointment into the database containing the data entered into the text fields.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    public void onSaveClicked(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
        boolean noErrors = true;
        LocalDate startDate = dateSelector.getValue();
        LocalTime startTime = LocalTime.parse(startTimeText.getText());
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalTime endTime = LocalTime.parse(endTimeText.getText());
        LocalDateTime end = LocalDateTime.of(startDate, endTime);
        ZonedDateTime businessStart = LocalDateTime.of(startDate, LocalTime.of(8,0,0)).atZone(ZoneId.of("-05:00"));
        ZonedDateTime businessEnd = LocalDateTime.of(startDate, LocalTime.of(22,0,0)).atZone(ZoneId.of("-05:00"));
        try {
            String statement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                        if(app.getStartDate().equals(this.dateSelector.getValue()) && (!(startTime.isBefore(app.getStartTime()) && endTime.isBefore(app.getStartTime())) && !(startTime.isAfter(app.getEndTime()) && endTime.isAfter(app.getEndTime())))){
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
     * Returns user to the main Screen scene when the cancel button is clicked.
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
}
