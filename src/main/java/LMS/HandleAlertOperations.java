package LMS;

public class HandleAlertOperations {
    /**
     * Displays an information alert with the specified title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the content text of the alert dialog
     */
    public static void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
