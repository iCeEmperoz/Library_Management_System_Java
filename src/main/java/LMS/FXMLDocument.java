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

/**
 * The FXMLDocument class serves as the controller for the Library Management System's JavaFX application.
 * It handles various user interactions within the application, such as logging in, creating accounts,
 * changing passwords, and recovering forgotten passwords. The class is responsible for managing the
 * visibility of different forms and validating user inputs.
 * 
 * <p>
 * The class contains several methods annotated with {@code @FXML} to handle specific action events
 * triggered by user interactions with the UI components defined in the FXML file. These methods include:
 * </p>
 * 
 * <ul>
 *   <li>{@link #handleBack(ActionEvent)}: Handles the back button action event to switch between forms.</li>
 *   <li>{@link #handleChangePassProceed(ActionEvent)}: Handles the action event for changing the password.</li>
 *   <li>{@link #handleCreateAccount(ActionEvent)}: Handles the action event for creating a new account.</li>
 *   <li>{@link #handleForgotPassword(ActionEvent)}: Handles the action event for the "Forgot Password" option.</li>
 *   <li>{@link #handleForgotProceed(ActionEvent)}: Handles the action event for the "Forgot Password" proceed button.</li>
 *   <li>{@link #handleLogin(ActionEvent)}: Handles the login action when the login button is pressed.</li>
 *   <li>{@link #handleSignUp(ActionEvent)}: Handles the sign-up action triggered by the user.</li>
 *   <li>{@link #togglePasswordVisibility()}: Toggles the visibility of the password field in the login form.</li>
 *   <li>{@link #initialize()}: Initializes the controller class after the FXML file has been loaded.</li>
 * </ul>
 * 
 * <p>
 * The class also contains private helper methods for validating login credentials, recovering passwords,
 * registering new accounts, and displaying alert messages.
 * </p>
 * 
 * <p>
 * The following FXML components are injected into the class:
 * </p>
 * 
 * <ul>
 *   <li>{@code ResourceBundle resources}</li>
 *   <li>{@code URL location}</li>
 *   <li>{@code Button changePass_backBtn}</li>
 *   <li>{@code PasswordField changePass_cPassword}</li>
 *   <li>{@code AnchorPane changePass_form}</li>
 *   <li>{@code PasswordField changePass_password}</li>
 *   <li>{@code Button changePass_proceedBtn}</li>
 *   <li>{@code TextField forgot_answer}</li>
 *   <li>{@code Button forgot_backBtn}</li>
 *   <li>{@code AnchorPane forgot_form}</li>
 *   <li>{@code Button forgot_proceedBtn}</li>
 *   <li>{@code ComboBox<String> forgot_selectQuestion}</li>
 *   <li>{@code TextField forgot_username}</li>
 *   <li>{@code Button login_btn}</li>
 *   <li>{@code Button login_createAccount}</li>
 *   <li>{@code Hyperlink login_forgotPassword}</li>
 *   <li>{@code AnchorPane login_form}</li>
 *   <li>{@code PasswordField login_password}</li>
 *   <li>{@code CheckBox login_selectShowPassword}</li>
 *   <li>{@code TextField login_showPassword}</li>
 *   <li>{@code TextField login_username}</li>
 *   <li>{@code AnchorPane main_form}</li>
 *   <li>{@code TextField signup_answer}</li>
 *   <li>{@code Button signup_btn}</li>
 *   <li>{@code TextField signup_email}</li>
 *   <li>{@code AnchorPane signup_form}</li>
 *   <li>{@code Button signup_loginAccount}</li>
 *   <li>{@code PasswordField signup_password}</li>
 *   <li>{@code ComboBox<String> signup_selectQuestion}</li>
 *   <li>{@code TextField signup_username}</li>
 * </ul>
 * 
 * <p>
 * The {@link #initialize()} method ensures that all the necessary FXML components are properly injected
 * and initializes the selection options for the security questions in the forgot password and signup forms.
 * </p>
 * 
 * <p>
 * The {@link #showAlert(String, String)} method displays an information alert with the specified title
 * and content.
 * </p>
 */
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

  /**
   * Handles the back button action event. This method is triggered when the back button is pressed.
   * It checks which form is currently visible (signup, forgot password, or change password) and 
   * switches the visibility to the login form.
   *
   * @param event the action event triggered by the back button
   */
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

  /**
   * Handles the action event for changing the password.
   * <p>
   * This method is triggered when the user attempts to change their password.
   * It retrieves the new password and the confirmation password from the input fields,
   * checks if they match and are not empty, and then proceeds to update the password
   * in the database. If the passwords match and are not empty, a success alert is shown,
   * and the form visibility is updated. Otherwise, an error alert is shown.
   * </p>
   *
   * @param event the action event triggered by the user
   */
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

  /**
   * Handles the action event for creating a new account.
   * This method is triggered when the user initiates the account creation process.
   * It hides the login form and displays the signup form.
   *
   * @param event the action event triggered by the user interaction
   */
  @FXML
  void handleCreateAccount(ActionEvent event) {
    login_form.setVisible(false);
    signup_form.setVisible(true);
  }

  /**
   * Handles the action event triggered when the "Forgot Password" option is selected.
   * This method hides the login form and displays the forgot password form.
   *
   * @param event the action event triggered by the user interaction
   */
  @FXML
  void handleForgotPassword(ActionEvent event) {
    login_form.setVisible(false);
    forgot_form.setVisible(true);
  }

  /**
   * Handles the action event triggered when the user clicks the "Forgot Password" proceed button.
   * 
   * This method retrieves the username, security question, and answer provided by the user,
   * and attempts to recover the password. If the recovery is successful, it displays a success
   * alert and switches the visible form to the password change form. If the recovery fails,
   * it displays an error alert.
   * 
   * @param event The action event triggered by the user's interaction.
   */
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

  /**
   * Attempts to recover the password for a user based on their security question and answer.
   *
   * @param username The username of the account for which the password recovery is being attempted.
   * @param question The security question associated with the account.
   * @param answer The answer to the security question.
   * @return true if the password recovery is successful, false otherwise.
   */
  private boolean recoverPassword(String username, String question, String answer) {
    return true; // Example success
  }

  /**
   * Handles the login action when the login button is pressed.
   * 
   * @param event The ActionEvent triggered by the login button.
   * 
   * This method retrieves the username and password from the login form,
   * validates the credentials, and displays an appropriate alert message.
   * If the login is successful, it makes the main form visible and hides the login form.
   */
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

  /**
   * Validates the login credentials.
   *
   * @param username the username to be validated
   * @param password the password to be validated
   * @return true if the username is "admin" and the password is "password", false otherwise
   */
  private boolean validateLogin(String username, String password) {
    return "admin".equals(username) && "password".equals(password); // Simple example
  }

  /**
   * Handles the sign-up action triggered by the user.
   * 
   * This method retrieves the input values from the sign-up form fields, 
   * including email, username, password, security question, and answer. 
   * It then attempts to register a new account with these details.
   * 
   * If the registration is successful, a success alert is shown, the sign-up 
   * form is hidden, and the login form is made visible. If the registration 
   * fails, an error alert is displayed.
   * 
   * @param event The ActionEvent triggered by the sign-up button.
   */
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

  /**
   * Registers a new account with the provided details.
   *
   * @param email The email address of the user.
   * @param username The username chosen by the user.
   * @param password The password chosen by the user.
   * @param question The security question for account recovery.
   * @param answer The answer to the security question.
   * @return true if the account registration is successful, false otherwise.
   */
  private boolean registerAccount(String email, String username, String password, String question,
      String answer) {
    return true; // Example success
  }

  /**
   * Toggles the visibility of the password field in the login form.
   * When the "Show Password" checkbox is selected, the plain text password is displayed.
   * When the checkbox is not selected, the password is hidden and displayed as masked text.
   * 
   * This method switches the visibility between two text fields:
   * - `login_password`: The password field that masks the input.
   * - `login_showPassword`: The text field that shows the plain text password.
   * 
   * The method checks the state of the `login_selectShowPassword` checkbox to determine
   * which field should be visible and updates the text accordingly.
   */
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


  /**
   * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
   * It ensures that all the necessary FXML components are properly injected and initializes the selection
   * options for the security questions in the forgot password and signup forms.
   *
   * Assertions:
   * - Ensures that all the FXML components are not null and have been properly injected.
   *
   * Initialization:
   * - Populates the security questions dropdowns for the forgot password and signup forms with predefined questions.
   */
  @FXML
  void initialize() {
    assert changePass_backBtn
        != null : "fx:id=\"changePass_backBtn\" was not injected: check your FXML file 'Login.fxml'.";
    assert changePass_cPassword
        != null : "fx:id=\"changePass_cPassword\" was not injected: check your FXML file 'Login.fxml'.";
    assert changePass_form
        != null : "fx:id=\"changePass_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert changePass_password
        != null : "fx:id=\"changePass_password\" was not injected: check your FXML file 'Login.fxml'.";
    assert changePass_proceedBtn
        != null : "fx:id=\"changePass_proceedBtn\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_answer
        != null : "fx:id=\"forgot_answer\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_backBtn
        != null : "fx:id=\"forgot_backBtn\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_form
        != null : "fx:id=\"forgot_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_proceedBtn
        != null : "fx:id=\"forgot_proceedBtn\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_selectQuestion
        != null : "fx:id=\"forgot_selectQuestion\" was not injected: check your FXML file 'Login.fxml'.";
    assert forgot_username
        != null : "fx:id=\"forgot_username\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_btn
        != null : "fx:id=\"login_btn\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_createAccount
        != null : "fx:id=\"login_createAccount\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_forgotPassword
        != null : "fx:id=\"login_forgotPassword\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_form
        != null : "fx:id=\"login_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_password
        != null : "fx:id=\"login_password\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_selectShowPassword
        != null : "fx:id=\"login_selectShowPassword\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_username
        != null : "fx:id=\"login_username\" was not injected: check your FXML file 'Login.fxml'.";
    assert main_form
        != null : "fx:id=\"main_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_answer
        != null : "fx:id=\"signup_answer\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_btn
        != null : "fx:id=\"signup_btn\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_email
        != null : "fx:id=\"signup_email\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_form
        != null : "fx:id=\"signup_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_loginAccount
        != null : "fx:id=\"signup_loginAccount\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_password
        != null : "fx:id=\"signup_password\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_selectQuestion
        != null : "fx:id=\"signup_selectQuestion\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_username
        != null : "fx:id=\"signup_username\" was not injected: check your FXML file 'Login.fxml'.";
    forgot_selectQuestion.getItems()
        .addAll("What is your childhood nickname?", "What is the name of your first pet?",
            "What is your favorite food?", "What was the name of your elementary school?",
            "Who is your best friend?");

    signup_selectQuestion.getItems()
        .addAll("What is your childhood nickname?", "What is the name of your first pet?",
            "What is your favorite food?", "What was the name of your elementary school?",
            "Who is your best friend?");
  }

  /**
   * Displays an information alert with the specified title and content.
   *
   * @param title   the title of the alert dialog
   * @param content the content text of the alert dialog
   */
  private void showAlert(String title, String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
