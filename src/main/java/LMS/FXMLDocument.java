package LMS;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLDocument {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button changePass_backBtn;

  @FXML
  private PasswordField changePass_cPassword;

  @FXML
  private AnchorPane changePass_form;

  @FXML
  private PasswordField changePass_password;

  @FXML
  private Button changePass_proceedBtn;

  @FXML
  private TextField forgot_answer;

  @FXML
  private Button forgot_backBtn;

  @FXML
  private AnchorPane forgot_form;

  @FXML
  private Button forgot_proceedBtn;

  @FXML
  private ComboBox<String> forgot_selectQuestion;

  @FXML
  private TextField forgot_username;

  @FXML
  private Button login_btn;

  @FXML
  private Button login_createAccount;

  @FXML
  private Hyperlink login_forgotPassword;

  @FXML
  private AnchorPane login_form;

  @FXML
  private PasswordField login_password;

  @FXML
  private CheckBox login_selectShowPassword;

  @FXML
  private TextField login_showPassword;

  @FXML
  private TextField login_username;

  @FXML
  private AnchorPane main_form;

  @FXML
  private TextField signup_answer;

  @FXML
  private Button signup_btn;

  @FXML
  private TextField signup_email;

  @FXML
  private AnchorPane signup_form;

  @FXML
  private Button signup_loginAccount;

  @FXML
  private PasswordField signup_password;

  @FXML
  private ComboBox<String> signup_selectQuestion;

  @FXML
  private TextField signup_username;

  @FXML
  void handleBack(ActionEvent event) {
    if (signup_form.isVisible()) {
      signup_form.setVisible(false);
      login_form.setVisible(true);
    } else if (forgot_form.isVisible()) {
      forgot_form.setVisible(false);
      login_form.setVisible(true);
    } else if (changePass_form.isVisible()) {
      changePass_form.setVisible(false);
      login_form.setVisible(true);
    }
  }

  @FXML
  void handleChangePassProceed(ActionEvent event) {
    String newPassword = changePass_password.getText();
    String confirmPassword = changePass_cPassword.getText();

    if (newPassword.equals(confirmPassword) && !newPassword.isEmpty()) {
      showAlert("Success", "Password has been changed successfully.");
      // Logic to update the password in the database
      changePass_form.setVisible(false);
      login_form.setVisible(true);
    } else {
      showAlert("Error", "Passwords do not match or are empty.");
    }
  }

  @FXML
  void handleCreateAccount(ActionEvent event) {
    login_form.setVisible(false);
    signup_form.setVisible(true);
  }

  @FXML
  void handleForgotPassword(ActionEvent event) {
    login_form.setVisible(false);
    forgot_form.setVisible(true);
  }

  @FXML
  void handleForgotProceed(ActionEvent event) {
    String username = forgot_username.getText();
    String question = forgot_selectQuestion.getValue();
    String answer = forgot_answer.getText();

    if (recoverPassword(username, question, answer)) {
      showAlert("Success", "Password recovery successful for user: " + username);
      forgot_form.setVisible(false);
      changePass_form.setVisible(true);
    } else {
      showAlert("Error", "Unable to recover password.");
    }
  }

  private boolean recoverPassword(String username, String question, String answer) {
    return true; // Example success
  }

  @FXML
  void handleLogin(ActionEvent event) {
    String username = login_username.getText();
    String password = login_password.getText();

    if (validateLogin(username, password)) {
      showAlert("Success", "Login successful for user: " + username);
      main_form.setVisible(true);
      login_form.setVisible(false);
    } else {
      showAlert("Error", "Invalid username or password.");
    }
  }

  private boolean validateLogin(String username, String password) {
    return "admin".equals(username) && "password".equals(password); // Simple example
  }

  @FXML
  void handleSignUp(ActionEvent event) {
    String email = signup_email.getText();
    String username = signup_username.getText();
    String password = signup_password.getText();
    String question = signup_selectQuestion.getValue();
    String answer = signup_answer.getText();

    if (registerAccount(email, username, password, question, answer)) {
      showAlert("Success", "Registration successful for user: " + username);
      signup_form.setVisible(false);
      login_form.setVisible(true);
    } else {
      showAlert("Error", "Unable to register account.");
    }
  }

  private boolean registerAccount(String email, String username, String password, String question,
      String answer) {
    return true; // Example success
  }

  @FXML
  private void togglePasswordVisibility() {
    if (login_selectShowPassword.isSelected()) {
      login_showPassword.setText(login_password.getText());
      login_showPassword.setVisible(true);
      login_password.setVisible(false);
    } else {
      login_password.setText(login_showPassword.getText());
      login_password.setVisible(true);
      login_showPassword.setVisible(false);
    }
  }


  @FXML
  void initialize() {
    assert changePass_backBtn
        != null : "fx:id=\"changePass_backBtn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert changePass_cPassword
        != null : "fx:id=\"changePass_cPassword\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert changePass_form
        != null : "fx:id=\"changePass_form\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert changePass_password
        != null : "fx:id=\"changePass_password\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert changePass_proceedBtn
        != null : "fx:id=\"changePass_proceedBtn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_answer
        != null : "fx:id=\"forgot_answer\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_backBtn
        != null : "fx:id=\"forgot_backBtn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_form
        != null : "fx:id=\"forgot_form\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_proceedBtn
        != null : "fx:id=\"forgot_proceedBtn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_selectQuestion
        != null : "fx:id=\"forgot_selectQuestion\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert forgot_username
        != null : "fx:id=\"forgot_username\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_btn
        != null : "fx:id=\"login_btn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_createAccount
        != null : "fx:id=\"login_createAccount\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_forgotPassword
        != null : "fx:id=\"login_forgotPassword\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_form
        != null : "fx:id=\"login_form\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_password
        != null : "fx:id=\"login_password\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_selectShowPassword
        != null : "fx:id=\"login_selectShowPassword\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert login_username
        != null : "fx:id=\"login_username\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert main_form
        != null : "fx:id=\"main_form\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_answer
        != null : "fx:id=\"signup_answer\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_btn
        != null : "fx:id=\"signup_btn\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_email
        != null : "fx:id=\"signup_email\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_form
        != null : "fx:id=\"signup_form\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_loginAccount
        != null : "fx:id=\"signup_loginAccount\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_password
        != null : "fx:id=\"signup_password\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_selectQuestion
        != null : "fx:id=\"signup_selectQuestion\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    assert signup_username
        != null : "fx:id=\"signup_username\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
    forgot_selectQuestion.getItems()
        .addAll("What is your childhood nickname?", "What is the name of your first pet?",
            "What is your favorite food?", "What was the name of your elementary school?",
            "Who is your best friend?");

    signup_selectQuestion.getItems()
        .addAll("What is your childhood nickname?", "What is the name of your first pet?",
            "What is your favorite food?", "What was the name of your elementary school?",
            "Who is your best friend?");
  }

  private void showAlert(String title, String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
