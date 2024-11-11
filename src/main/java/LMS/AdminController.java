package LMS;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminController implements Initializable {

  @FXML
  private Button backButton;

  @FXML
  private Label bookAuthor;

  @FXML
  private ImageView bookImage;

  @FXML
  private Label bookTitle;

  @FXML
  private HBox box;

  @FXML
  private Button btnAddBook;

  @FXML
  private Button btnAddUser;

  @FXML
  private Button btnBooks;

  @FXML
  private Button btnDashboard;

  @FXML
  private Button btnHistory;

  @FXML
  private Button btnUser;

  @FXML
  private Button btnUsers;

  @FXML
  private Button changeAvatarButton;

  @FXML
  private BorderPane home_form;

  @FXML
  private AnchorPane paneBooks;

  @FXML
  private AnchorPane paneUsers;

  @FXML
  private TextField searchBooks;

  @FXML
  private TextField searchUsers;

  @FXML
  private TableView<?> tableBooks;

  @FXML
  private TableView<?> tableUsers;

  @FXML
  private Label textSubTiltle;

  @FXML
  private ImageView userImageView;

  @FXML
  private VBox user_form;

  @FXML
  void chooseImageButtonPushed(ActionEvent event) {

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    
  }
}
