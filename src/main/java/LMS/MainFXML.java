package LMS;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

public class MainFXML {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Label createAccountLabel;

    @FXML
    private Label forgotPasswordLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());

        createAccountLabel.setOnMouseClicked(event -> handleCreateAccount());

        forgotPasswordLabel.setOnMouseClicked(event -> handleForgotPassword());
    }

    @FXML
    private void handleSubmit() {
        if (emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a password");
            return;
        }
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a valid email address");
            return;
        }
        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Registration Successful!", "Welcome ");
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @FXML
    private void handleCreateAccount() {
        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Create new Account", "Function is not available! Try later.");
    }

    @FXML
    private void handleForgotPassword() {
        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Forgot Password", "Function is not available! Try later.");
    }
}
