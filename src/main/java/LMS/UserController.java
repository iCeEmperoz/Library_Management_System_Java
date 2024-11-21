package LMS;

import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class UserController implements Initializable {

  private static final Library library = Library.getInstance();

  private final ArrayList<Book> books = library.getBooks();

  private ObservableList<Book> bookList;

  private List<Book> recentlyAdded;

  private List<Book> recommended;

  private FileChooser fileChooser;

  private File filePath;
  @FXML
  private HBox cardLayout;

  @FXML
  private GridPane bookContainer;

  @FXML
  private ImageView userImageView;

  @FXML
  private BorderPane dashboard_form;
  @FXML
  private VBox user_form;
  @FXML
  private TableView<Book> tableBooks;
  @FXML
  private TableColumn<Book, String> bookAuthorColumn;
  @FXML
  private TableColumn<Book, Integer> bookIdColumn;
  @FXML
  private TableColumn<Book, Boolean> bookIsIssuedColumn;
  @FXML
  private TextField bookSearchTextField;
  @FXML
  private TableColumn<Book, String> bookTitleColumn;
  @FXML
  private AnchorPane paneBooks;
  @FXML
  private AnchorPane paneFavorite;
  @FXML
  private AnchorPane paneHistory;
  @FXML
  private AnchorPane paneTopBooks;
  @FXML
  private AnchorPane paneYourShelf;
  @FXML
  private ImageView qrImage;
  @FXML
  private Label textSubTiltle;
  @FXML
  private StackPane box;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    recentlyAdded = recentlyAdded();
    recommended = books();
    int column = 0;
    int row = 1;
    try {
      for (Book value : recentlyAdded) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
        HBox cardBox = fxmlLoader.load();
        CardController cardController = fxmlLoader.getController();
        cardController.setData(value);
        cardLayout.getChildren().add(cardBox);
      }

      for (Book book : recommended) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/LMS/Book.fxml"));
        VBox BookBox = fxmlLoader.load();
        BookController bookController = fxmlLoader.getController();
        bookController.setData(book);

        if (column == 6) {
          column = 0;
          row++;
        }

        bookContainer.add(BookBox, column++, row);
        GridPane.setMargin(BookBox, new Insets(10));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Circle clip = new Circle(100, 100, 100);
    userImageView.setClip(clip);

    initializeTableBooks();
  }

  private void showBookDetails(Book selectedBook) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
      HBox cardBox = fxmlLoader.load();
      CardController cardController = fxmlLoader.getController();
      cardController.setData(selectedBook);

      // Cập nhật thông tin sách
      qrImage.setImage(selectedBook.generateQRCodeImage(selectedBook.getPreviewLink(), 150, 150));
      textSubTiltle.setText(selectedBook.getTitle());

      // Thay toàn bộ nội dung của StackPane
      box.getChildren().setAll(cardBox);
    } catch (IOException | WriterException e) {
      e.printStackTrace();
    }
  }

  private void initializeTableBooks() {
    // Thiết lập các cột với thuộc tính của lớp Book
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

    // Chuyển đổi ArrayList<Book> sang ObservableList<Book>
    bookList = FXCollections.observableArrayList(books);

    // Tạo FilteredList để hỗ trợ tìm kiếm
    FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);

    // Thiết lập dữ liệu cho bảng
    tableBooks.setItems(filteredData);

    // Lắng nghe sự thay đổi của thanh tìm kiếm
    bookSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(book -> {
        // Nếu thanh tìm kiếm trống, hiển thị tất cả sách
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // So khớp từ khóa (không phân biệt chữ hoa/thường)
        String lowerCaseFilter = newValue.toLowerCase();

        if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tiêu đề sách
        } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tác giả
        } else {
          return String.valueOf(book.getID()).contains(lowerCaseFilter); // Khớp với ID
        }
// Không khớp
      });
    });

    tableBooks.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            showBookDetails(newValue);
          }
        });
  }

  public void chooseImageButtonPushed(ActionEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    fileChooser = new FileChooser();
    fileChooser.setTitle("Open image");

    // Set to user's directory or go to the default C drive if cannot access
    String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
    File userDirectory = new File(userDirectoryString);

    if (!userDirectory.canRead()) {
      userDirectory = new File("c:/");
    }

    fileChooser.setInitialDirectory(userDirectory);

    this.filePath = fileChooser.showOpenDialog(stage);

    // Update the image
    try {
      BufferedImage bufferedImage = ImageIO.read(filePath);
      Image image = SwingFXUtils.toFXImage(bufferedImage, null);
      userImageView.setImage(image);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<Book> recentlyAdded() {
    List<Book> ls = new ArrayList<>();
    return ls;
  }

  private List<Book> books() {
    List<Book> ls = new ArrayList<>();
    return ls;
  }

  private void showPane(AnchorPane paneToShow) {
    paneTopBooks.setVisible(false);
    paneBooks.setVisible(false);
    paneYourShelf.setVisible(false);
    paneFavorite.setVisible(false);
    paneHistory.setVisible(false);
    paneToShow.setVisible(true);
  }

  @FXML
  void handleBooks(ActionEvent event) {
    showPane(paneBooks);
  }

  @FXML
  void handleFavorite(ActionEvent event) {
    showPane(paneFavorite);
  }

  @FXML
  void handleHistory(ActionEvent event) {
    showPane(paneHistory);
  }

  @FXML
  void handleTopBooks(ActionEvent event) {
    showPane(paneTopBooks);
  }

  @FXML
  void handleYourShelf(ActionEvent event) {
    showPane(paneYourShelf);
  }

  @FXML
  void handleLogOut(ActionEvent event) throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("Bạn có chắc chắn muốn đăng xuất?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      // Tải file FXML của dashboard
      FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LMS/Login.fxml"));

      Scene loginScene = new Scene(loginLoader.load(), 372, 594);

      primaryStage.setTitle("Login");

      // Chuyển sang Scene của dashboard
      primaryStage.setScene(loginScene);
    }
  }
}
