package LMS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class CreateAccountFXML {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Label backLoginLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
        backLoginLabel.setOnMouseClicked(event -> handleBackLogin());
    }

    @FXML
    private void handleSubmit() {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your name");
            return;
        }
        if (emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your email id");
            return;
        }
        if (phoneNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your phone number");
            return;
        }
        if (addressField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter your address");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter a password");
            return;
        }


        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Registration Successful!", "Welcome " + nameField.getText());
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
    private void handleBackLogin() {
        handleChangeScene();
    }

    @FXML
    public void handleChangeScene() {
        FXMLLoader createAccountLoader = new FXMLLoader(getClass().getResource("/LMS/RegistrationForm.fxml"));

        Stage stage = (Stage) gridPane.getScene().getWindow(); // Use gridPane to get the current stage

        Scene createAccountScene;
        try {
            createAccountScene = new Scene(createAccountLoader.load(), 800, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(createAccountScene);

        stage.setTitle("Login");

        stage.show();
    }
}
