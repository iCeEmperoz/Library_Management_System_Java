package LMS;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

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
    public static void showAlert(String title, String content) {
        Platform.runLater(() -> { // Đảm bảo đoạn mã này chạy trên FX Application Thread
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void showDialoge(String title, String content) {

    }
}
