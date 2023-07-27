package Nick;

import Helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(getClass().getResource("/Nick/Views/Login.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Nick/Views/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //Locale.setDefault(Locale.FRENCH);
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
