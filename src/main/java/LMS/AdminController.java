package LMS;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class AdminController implements Initializable {

  private static final Library library = Library.getInstance();

  private final ArrayList<Book> books = library.getBooks();

  private final ArrayList<Borrower> users = library.getBorrowers();

  private final ArrayList<Librarian> librarians = library.getLibrarians();

  private final API_TEST apiTest = new API_TEST();

  private final ObservableList<Book> apiBooksList = FXCollections.observableArrayList();

  private ObservableList<Book> bookList;
  @FXML
  private AnchorPane paneAddBook;
  @FXML
  private Button backButton;
  @FXML
  private Button buttonBackFromPaneAPI;
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
  private TableColumn<Book, String> bookTitleColumn;
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
  private TableView<Person> tableUsers;
  @FXML
  private Label textSubTiltle;
  @FXML
  private TableColumn<Borrower, String> userAddressColumn;
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
  private TextField bookApiSearchTextField;
  @FXML
  private Label labelTotalBooks;
  @FXML
  private Label labelTotalUsers;
  @FXML
  private TableColumn<Book, String> addBookAuthorColumn;

  @FXML
  private TableColumn<Book, Void> addBookBtnColumn;

  @FXML
  private TableColumn<Book, Integer> addBookIdColumn;

  @FXML
  private TableColumn<Book, Boolean> addBookIsIssuedColumn;

  @FXML
  private TableColumn<Book, String> addBookTitleColumn;

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

    labelTotalBooks.setText(String.valueOf(books.size()/2));
    labelTotalUsers.setText(String.valueOf(users.size()));

    initializeTableBooks();
    initializeTableUsers();

    tableBooks.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            box.getChildren().clear();
            showBookDetails(newValue);
          }
        });
  }

  private void showBookDetails(Book selectedBook) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
      HBox cardBox = fxmlLoader.load();
      CardController cardController = fxmlLoader.getController();
      cardController.setData(selectedBook);
      box.getChildren().add(cardBox);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initializeTableBooks() {

    // Thiết lập các cột với thuộc tính của lớp Book
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));
    // Đánh dấu bảng là có thể chỉnh sửa
    tableBooks.setEditable(true);
    // Thiết lập cột title, author, và isIssued có thể chỉnh sửa
    bookTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    bookAuthorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    bookIsIssuedColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

    bookTitleColumn.setOnEditCommit(event -> {
      Book book = event.getRowValue();
      book.setTitle(event.getNewValue());
    });

    bookAuthorColumn.setOnEditCommit(event -> {
      Book book = event.getRowValue();
      book.setAuthor(event.getNewValue());
    });

    bookIsIssuedColumn.setOnEditCommit(event -> {
      Book book = event.getRowValue();
      book.setIssuedStatus(event.getNewValue().equals("true"));
    });

    int sizeReal = 0;
    for (int i = 0; i < books.size(); i++) {
      sizeReal = Math.max(books.get(i).getID(), sizeReal);
    }

    int numToRemove = books.size() - sizeReal;

    if (numToRemove > 0) {
      for (int i = 0; i < numToRemove; i++) {
        books.remove(books.size() - 1);
      }
    }

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
        } else if (String.valueOf(book.getID()).contains(lowerCaseFilter)) {
          return true; // Khớp với ID
        }
        return false; // Không khớp
      });
    });
  }

  private void initializeTableUsers() {
    // Thiết lập các cột với thuộc tính của lớp Person (là lớp cha của Borrower và Librarian)
    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
    userNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNo"));
    userAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

    // Tạo thêm cột is_Librarian
    TableColumn<Person, String> isLibrarianColumn = new TableColumn<>("Is Librarian");
    isLibrarianColumn.setCellValueFactory(person -> {
      if (person.getValue() instanceof Librarian) {
        return new SimpleStringProperty("True");
      } else {
        return new SimpleStringProperty("False");
      }
    });

    // Thêm cột isLibrarian vào bảng
    tableUsers.getColumns().add(isLibrarianColumn);

    // Thiết lập bảng để có thể chỉnh sửa
    tableUsers.setEditable(true);

    // Cho phép chỉnh sửa từng cột
    userIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    userEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    userNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    userPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    userAddressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    // Xử lý sự kiện khi chỉnh sửa từng cột
    userIdColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setID(event.getNewValue());
    });

    userEmailColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setEmail(event.getNewValue());
    });

    userNameColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setName(event.getNewValue());
    });

    userPhoneColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setPhoneNo(event.getNewValue());
    });

    userAddressColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setAddress(event.getNewValue());
    });

    ArrayList<Person> combinedPersons = new ArrayList<>();
    combinedPersons.addAll(users);
    combinedPersons.addAll(librarians);

    combinedPersons.sort(Comparator.comparingInt(Person::getID));

    ObservableList<Person> personList = FXCollections.observableArrayList(combinedPersons);

    FilteredList<Person> filteredData = new FilteredList<>(personList, p -> true);

    tableUsers.setItems(filteredData);

    userSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(person -> {
        // Nếu thanh tìm kiếm trống, hiển thị tất cả người dùng
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // So khớp từ khóa (không phân biệt chữ hoa/thường)
        String lowerCaseFilter = newValue.toLowerCase();

        if (String.valueOf(person.getID()).contains(lowerCaseFilter)) {
          return true; // Khớp với ID
        } else if (person.getEmail().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với email
        } else if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tên
        } else if (String.valueOf(person.getPhoneNo()).contains(lowerCaseFilter)) {
          return true; // Khớp với số điện thoại
        } else if (person.getAddress().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với địa chỉ
        } else if (person instanceof Librarian && "true".contains(lowerCaseFilter)) {
          return true; // Khớp với is_Librarian là True
        } else if (!(person instanceof Librarian) && "false".contains(lowerCaseFilter)) {
          return true; // Khớp với is_Librarian là False
        }

        return false; // Không khớp
      });
    });
  }


  private void showPane(AnchorPane paneToShow) {
    paneBooks.setVisible(false);
    paneUsers.setVisible(false);
    paneDashboard.setVisible(false);
    paneHistory.setVisible(false);
    paneAddBook.setVisible(false);
    paneToShow.setVisible(true);
  }

  @FXML
  void handleAddUser(ActionEvent event) {

  }

  @FXML
  void handleAddBook() {
    showPane(paneAddBook);

    // Thiết lập cột với thuộc tính của lớp Book
    addBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    addBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    addBookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    addBookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

    // Thiết lập cột hành động với nút "Add"
    addBookBtnColumn.setCellFactory(column -> new TableCell<>() {
      private final Button addButton = new Button("Add");

      {
        addButton.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());
          //System.out.println("Adding book: " + book.getTitle());
          // Thực hiện thêm sách vào thư viện ở đây
          bookList.add(book);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(addButton);
        }
      }
    });

    // Thiết lập dữ liệu cho bảng
    tableBooks.setItems(apiBooksList);

    // Lắng nghe sự thay đổi của thanh tìm kiếm và gọi API
    bookApiSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.isEmpty()) {
        apiBooksList.clear(); // Xóa bảng nếu từ khóa tìm kiếm trống
        return;
      }

      // Gọi API với từ khóa tìm kiếm
      String apiUrl =
          "https://www.googleapis.com/books/v1/volumes?q=" + newValue + "&maxResults=10";
      String jsonResponse = apiTest.getHttpResponse(apiUrl);

      if (jsonResponse != null) {
        try {
          // Lấy danh sách sách từ phản hồi JSON
          ArrayList<Book> booksFromAPI = apiTest.getBooksFromJson(jsonResponse);

          // Cập nhật ObservableList để hiển thị trên TableView
          apiBooksList.clear();
          apiBooksList.addAll(booksFromAPI);
        } catch (Exception e) {
          System.out.println("Lỗi khi xử lý dữ liệu API: " + e.getMessage());
        }
      } else {
        System.out.println("Không thể lấy dữ liệu từ API");
      }
    });
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

  @FXML
  void handleBackFromPaneAPI(ActionEvent event) {
    showPane(paneBooks);
  }
}
