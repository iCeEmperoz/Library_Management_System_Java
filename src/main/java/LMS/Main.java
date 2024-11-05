package LMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class for the Library Management System (LMS) application.
 * This class launches the JavaFX application.
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * This method is called after the init method has returned, and after the system is ready for the application to begin running.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception if something goes wrong during loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Image icon = new Image(getClass().getResource("/images/icon.png").toString());

        primaryStage.getIcons().add(icon);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LMS/User.fxml"));

        Scene loginScene = new Scene(loginLoader.load(), 1096, 640);

        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    /**
     * The main method is ignored in JavaFX applications.
     * main() serves only as fallback in case the application is launched in a way that doesn't support JavaFX.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
