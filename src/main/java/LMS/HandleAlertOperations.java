package LMS;

import javafx.application.Platform;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandleAlertOperations {
    /**
     * Displays an information alert with the specified title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the content text of the alert dialog
     */
    public static boolean showAlert(String title, String content) {
        final boolean[] result = {true}; // Default return value is true
        Platform.runLater(() -> { // Ensure this code runs on FX Application Thread
            if (content.contains("?")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> choice = alert.showAndWait();
                result[0] = choice.isPresent() && choice.get() == yesButton; // True if "Yes" is selected, false otherwise
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait(); // Simply show the alert
            }
        });

        try {
            // Wait for the alert to be processed on FX Application Thread
            Thread.sleep(500); // Adjust as necessary based on app responsiveness
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
        }

        return result[0];
    }


    public static void showDialoge(String title, String content) {

    }
}
