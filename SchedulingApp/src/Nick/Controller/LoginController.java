package Nick.Controller;

import Helper.DBHelper;
import Nick.Modal.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 *class that controls the login screen
 */
public class LoginController implements Initializable {

    public Button loginButton;
    public Button exitButton;
    public Label loginLabel;
    public Label passwordLabel;
    public Label userNameLabel;
    public TextField userNameText;
    public TextField passwordText;
    public static User currentUser;
    public Label zoneIdLabel;
    ResourceBundle rs;
    public static LocalTime loginTime;

    /**
     *method that runs on the opening of the login screen. Checks location and adjusts language to french or english and creates login_activity.txt file if it doesn't exit.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    passwordText.setEditable(true);
    passwordText.setDisable(false);
    Locale defLocale = Locale.getDefault();
    Locale.setDefault(defLocale);
    ZoneId curZone = ZoneId.systemDefault();
    resourceBundle = ResourceBundle.getBundle("Nick\\Languages", Locale.getDefault());

    zoneIdLabel.setText(String.valueOf(Locale.getDefault()) + " " + curZone.getDisplayName(TextStyle.FULL, Locale.getDefault()));
    loginLabel.setText(resourceBundle.getString("Login"));
    passwordLabel.setText(resourceBundle.getString("Password"));
    userNameLabel.setText(resourceBundle.getString("Username"));
    loginButton.setText(resourceBundle.getString("Login"));
    exitButton.setText(resourceBundle.getString("exit"));
    userNameText.setPromptText(resourceBundle.getString("Username"));
    passwordText.setPromptText(resourceBundle.getString("Password"));
    rs = resourceBundle;

        try {
            File myObj = new File("C:\\Users\\LabUser\\Desktop\\SchedulingApp\\src\\login_activity.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println(myObj.getName() + " already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Method that runs when login button is clicked. Verifies Username and Password and writes to login_activity.txt file
     * @param actionEvent
     * @throws IOException
     */
    public void onLoginClicked(ActionEvent actionEvent) throws IOException {
        boolean matchFound = false;

        for(User x: DBHelper.getUsers()){
            if(!x.getUser_Name().equals(userNameText.getText()) || !x.getPassword().equals(passwordText.getText())){

            }
            else{
                FileWriter loginTracker = new FileWriter("C:\\Users\\LabUser\\Desktop\\SchedulingApp\\src\\login_activity.txt", true);
                currentUser = x;
                loginTime = LocalTime.now();
                matchFound = true;
                loginTracker.append("Login attempt made by Username: " + userNameText.getText() + " Date: " + Timestamp.valueOf(LocalDateTime.now()) + "Login Status: Success\n");
                loginTracker.close();
                Parent root = FXMLLoader.load(getClass().getResource("/Nick/Views/Main_Screen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Hello!");
                stage.setScene(scene);
                stage.show();
            }

        }

        if(Locale.getDefault().getLanguage().equals(Locale.FRENCH.toString()))
        {
            if (matchFound == false) {
                Alert alert = new Alert(Alert.AlertType.ERROR, rs.getString("alert"));
                alert.setTitle(rs.getString("Error"));
                alert.showAndWait();
            }
        }
        else if(Locale.getDefault().getLanguage().equals(Locale.ENGLISH.toString())) {
            if (matchFound == false) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "That Username and Password combination was not found.");
                alert.showAndWait();
            }
        }
        if(matchFound == false){
            FileWriter loginTracker = new FileWriter("C:\\Users\\LabUser\\Desktop\\SchedulingApp\\src\\login_activity.txt", true);
            loginTracker.append("Login attempt made by Username: " + userNameText.getText() + " Date: " + Timestamp.valueOf(LocalDateTime.now()) + "Login Status: Failed\n");
            loginTracker.close();
        }



    }

    /**
     * Method that exits program if exit button clicked
     * @param actionEvent
     */
    public void onExitClicked(ActionEvent actionEvent) {
        Platform.exit();
    }
}
