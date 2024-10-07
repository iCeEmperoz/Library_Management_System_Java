package LMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

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

    public static void ClearingRequiredAreaOfScreen() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

    public static int takeInput(int min, int max) {
        String choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter your choice: ");

            choice = input.next();
            if ((!choice.matches(".*[a-zA-Z]+.*]")) &&
                    (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max))
            {
                return Integer.parseInt(choice);
            }

            else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void allFunctionalities(Person person, int choice) throws IOException {
        //Librarian lib = Library.getInstance();

        Scanner scanner = new Scanner(System.in);
        int input = 0;

        if (choice == 1) {
            //lib.searchForBooks();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
