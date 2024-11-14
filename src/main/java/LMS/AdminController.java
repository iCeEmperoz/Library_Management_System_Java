package LMS;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminController implements Initializable {

  private static Library library = Library.getInstance();

  private final ArrayList<Book> books = library.getBooks();

  private final ArrayList<Borrower> users = library.getBorrowers();

  @FXML
  private Button backButton;
  @FXML
  private Label bookAuthor;
  @FXML
  private TableColumn<Book, String> bookAuthorColumn;
  @FXML
  private TableColumn<Book, Integer> bookIdColumn;
  @FXML
  private ImageView bookImage;
  @FXML
  private TableColumn<Book, Boolean> bookIsIssuedColumn;
  @FXML
  private TextField bookSearchTextField;
  @FXML
  private TableColumn<Book, String> bookTilteColumn;
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
  private BorderPane formHome;
  @FXML
  private VBox formUser;
  @FXML
  private AnchorPane paneBooks;
  @FXML
  private AnchorPane paneUsers;
  @FXML
  private AnchorPane paneDashboard;
  @FXML
  private AnchorPane paneHistory;
  @FXML
  private TableView<Book> tableBooks;
  @FXML
  private TableView<Borrower> tableUsers;
  @FXML
  private Label textSubTiltle;
  @FXML
  private TableColumn<Borrower, String> userAdressColumn;
  @FXML
  private TableColumn<Borrower, String> userEmailColumn;
  @FXML
  private TableColumn<Borrower, Integer> userIdColumn;
  @FXML
  private ImageView userImageView;
  @FXML
  private TableColumn<Borrower, String> userNameColumn;
  @FXML
  private TableColumn<Borrower, Integer> userPhoneColumn;
  @FXML
  private TextField userSearchTextField;
  @FXML
  private Label labelTotalBooks;
  @FXML
  private Label labelTotalUsers;

  public static Connection initialize(Library lib) {
    try {
      setupLibrary(lib);
      Connection connection = lib.makeConnection();
      if (connection == null) {
        System.out.println("\nError connecting to Database. Exiting.");
        return null;
      }
      lib.populateLibrary(connection);
      return connection;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private static void setupLibrary(Library library) {
    library.setFine(20);
    library.setRequestExpiry(7);
    library.setReturnDeadline(5);
    library.setName("Library");
  }

  public static void cleanup(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Error closing connection: " + e.getMessage());
      }
    }
  }

  @FXML
  void chooseImageButtonPushed(ActionEvent event) {

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Connection connection = initialize(library);
    if (connection == null) {
      System.out.println("\nError connecting to Database. Exiting.");
      return;
    }

    labelTotalBooks.setText(String.valueOf(books.size()));
    labelTotalUsers.setText(String.valueOf(users.size()));

    /* Thiết lập các cột với thuộc tính của lớp Book */
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    bookTilteColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

    // Chuyển đổi ArrayList<Book> sang ObservableList<Book>
    ObservableList<Book> bookList = FXCollections.observableArrayList(books);

    // Thiết lập dữ liệu cho bảng
    tableBooks.setItems(bookList);

    // Thiết lập các cột với thuộc tính của lớp Borrower
    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
    userNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNo"));
    userAdressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

    // Chuyển đổi ArrayList<Borrower> sang ObservableList<Borrower>
    ObservableList<Borrower> borrowerList = FXCollections.observableArrayList(users);

    // Thiết lập dữ liệu cho bảng
    tableUsers.setItems(borrowerList);
  }

  private void showPane(AnchorPane paneToShow) {
    paneBooks.setVisible(false);
    paneUsers.setVisible(false);
    paneDashboard.setVisible(false);
    paneHistory.setVisible(false);
    paneToShow.setVisible(true);
  }

  @FXML
  void handleAddUser(ActionEvent event) {

  }

  @FXML
  void handleBooks(ActionEvent event) {
    showPane(paneBooks);
  }

  @FXML
  void handleDashBoard(ActionEvent event) {
    showPane(paneDashboard);
  }

  @FXML
  void handleHistory(ActionEvent event) {
    showPane(paneHistory);
  }

  @FXML
  void handleUsers(ActionEvent event) {
    showPane(paneUsers);
  }
}
