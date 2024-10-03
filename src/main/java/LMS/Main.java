package LMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image icon = new Image(getClass().getResource("/images/icon.png").toString());

        primaryStage.getIcons().add(icon);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LMS/RegistrationForm.fxml"));

        Scene loginScene = new Scene(loginLoader.load(), 800, 500);

        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
