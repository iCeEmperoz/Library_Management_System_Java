package LMS;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLDocument {

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
  private TextField signup_password;

  @FXML
  private ComboBox<String> signup_selectQuestion;

  @FXML
  private TextField signup_username;  // Sửa thành TextField thay vì PasswordField

  // Hàm khởi tạo
  @FXML
  public void initialize() {
    // Thiết lập các giá trị cho ComboBox
    signup_selectQuestion.getItems().addAll("What is your pet's name?", "What is your favorite color?", "What is your mother's maiden name?");
    forgot_selectQuestion.getItems().addAll("What is your pet's name?", "What is your favorite color?", "What is your mother's maiden name?");

    // Tắt hiển thị trường password văn bản (showPassword)
    login_showPassword.setVisible(false);

    // Sự kiện thay đổi trạng thái của checkbox "Show password"
    login_selectShowPassword.setOnAction(event -> togglePasswordVisibility());
  }

  // Hàm xử lý khi nhấn nút "Login"
  @FXML
  public void handleLogin() {
    String username = login_username.getText();
    String password = login_password.getText();

    // Thực hiện xử lý đăng nhập
    if (username.isEmpty() || password.isEmpty()) {
      showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields.");
    } else {
      // Thực hiện logic đăng nhập tại đây
      System.out.println("Logging in with: " + username);
    }
  }

  // Hàm xử lý khi nhấn nút "Create Account"
  @FXML
  public void handleCreateAccount() {
    signup_form.setVisible(true);
    login_form.setVisible(false);
    forgot_form.setVisible(false);
    changePass_form.setVisible(false);
  }

  // Hàm xử lý khi nhấn nút "Sign Up"
  @FXML
  public void handleSignUp() {
    String email = signup_email.getText();
    String username = signup_username.getText();
    String password = signup_password.getText();
    String question = signup_selectQuestion.getValue();
    String answer = signup_answer.getText();

    if (email.isEmpty() || username.isEmpty() || password.isEmpty() || question == null || answer.isEmpty()) {
      showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Please fill in all fields.");
    } else {
      // Thực hiện logic đăng ký tại đây
      System.out.println("Creating account with: " + email);
    }
  }

  // Hàm xử lý khi nhấn nút "Forgot Password"
  @FXML
  public void handleForgotPassword() {
    forgot_form.setVisible(true);
    login_form.setVisible(false);
    signup_form.setVisible(false);
    changePass_form.setVisible(false);
  }

  // Hàm xử lý khi nhấn nút "Proceed" trong form quên mật khẩu
  @FXML
  public void handleForgotProceed() {
    String username = forgot_username.getText();
    String question = forgot_selectQuestion.getValue();
    String answer = forgot_answer.getText();

    if (username.isEmpty() || question == null || answer.isEmpty()) {
      showAlert(Alert.AlertType.ERROR, "Forgot Password Error", "Please fill in all fields.");
    } else {
      // Thực hiện logic quên mật khẩu tại đây
      System.out.println("Verifying user: " + username);
    }
  }

  // Hàm xử lý khi nhấn nút "Proceed" trong form đổi mật khẩu
  @FXML
  public void handleChangePassProceed() {
    String newPassword = changePass_password.getText();
    String confirmPassword = changePass_cPassword.getText();

    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
      showAlert(Alert.AlertType.ERROR, "Change Password Error", "Please fill in all fields.");
    } else if (!newPassword.equals(confirmPassword)) {
      showAlert(Alert.AlertType.ERROR, "Change Password Error", "Passwords do not match.");
    } else {
      // Thực hiện logic đổi mật khẩu tại đây
      System.out.println("Changing password.");
    }
  }

  // Hàm xử lý khi nhấn nút "Back"
  @FXML
  public void handleBack() {
    login_form.setVisible(true);
    signup_form.setVisible(false);
    forgot_form.setVisible(false);
    changePass_form.setVisible(false);
  }

  // Hàm chuyển đổi giữa hiện/ẩn mật khẩu
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

  // Hiển thị thông báo lỗi hoặc thông báo khác
  private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
