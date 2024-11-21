package LMS;

import com.google.zxing.WriterException;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Insets;

import static LMS.HandleAlertOperations.showAlert;
import static LMS.HandleAlertOperations.showConfirmation;

public class AdminController implements Initializable {

    private static final Library library = Library.getInstance();
    private final ArrayList<Borrower> users = library.getBorrowers();
    private final ArrayList<Librarian> librarians = library.getLibrarians();
    private final API_TEST apiTest = new API_TEST();
    private final ObservableList<Book> apiBooksList = FXCollections.observableArrayList();
    // Biến lưu trữ truy vấn tìm kiếm hiện tại
    private final AtomicReference<String> searchQuery = new AtomicReference<>("");
    // Biến điều chỉnh thời gian debounce (500ms)
    private final long debounceDelay = 500;
    // Scheduler để thực hiện debounce
    private final ScheduledExecutorService debounceScheduler = Executors.newSingleThreadScheduledExecutor();
    // Executor cho các tác vụ bất đồng bộ
    private final ExecutorService apiExecutor = Executors.newCachedThreadPool();
    private final ArrayList<Book> books = library.getBooks();
    @FXML
    TableView<Book> tableAddBooks;
    private ScheduledFuture<?> scheduledFuture;
    // Biến theo dõi thời gian lần gọi API gần nhất (sử dụng để throttle)
    private volatile long lastApiCallTime = 0;
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
    private TableColumn<Book, Void> bookOptionsColumn;
    @FXML
    private Label bookTitle;
    //  @FXML
//  private HBox box;
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
    private ImageView qrImage;
    @FXML
    private TableColumn<Book, String> addBookAuthorColumn;
    @FXML
    private TableColumn<Book, Void> addBookBtnColumn;
    @FXML
    private TableColumn<Book, String> addBookTitleColumn;
    @FXML
    private StackPane box;

    @FXML
    void chooseImageButtonPushed(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        labelTotalBooks.setText(String.valueOf(books.size()));
        labelTotalUsers.setText(String.valueOf(users.size()));

        initializeTableBooks();
        initializeTableUsers();
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
            textSubTiltle.setText(selectedBook.getSubtitle());

            // Thay toàn bộ nội dung của StackPane
            box.getChildren().setAll(cardBox);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableBooks() {
        // Thiết lập các cột với thuộc tính của lớp Book
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
        bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

        // Đánh dấu bảng có thể chỉnh sửa
        tableBooks.setEditable(true);
        bookTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bookAuthorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bookIsIssuedColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

        bookTitleColumn.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setTitle(event.getNewValue());
            //library.setBooksInLibrary(book.getID(), book);
        });

        bookAuthorColumn.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setAuthor(event.getNewValue());
        });

        bookOptionsColumn.setCellFactory(column -> new TableCell<>() {
            private final MenuButton menuButton = new MenuButton("");

            {
                // Create MenuItems for each action
                MenuItem holdItem = new MenuItem("Place book on Hold");
                holdItem.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleHoldBookAction(book); // Implement your method for adding a book
                });

                MenuItem checkOutItem = new MenuItem("Check out");
                checkOutItem.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleCheckOutBookAction(book); // Implement your method for editing a book
                });

                MenuItem checkInItem = new MenuItem("Check in");
                checkInItem.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleCheckInBookAction(book); // Implement your method for deleting a book
                });

                MenuItem deleteItem = new MenuItem("Remove");
                deleteItem.setOnAction(event -> {
                    Book selectedBook = getTableView().getItems().get(getIndex());
                    if (selectedBook != null) {
                      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                      alert.setTitle("Xác nhận xóa");
                      alert.setHeaderText("Bạn có chắc chắn muốn xóa sách này?");
                      alert.setContentText("Sách: " + selectedBook.getTitle());

                      Optional<ButtonType> result = alert.showAndWait();
                      if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Xóa trong ObservableList
                        bookList.remove(selectedBook);


                      }
                      // Xóa trong ArrayList gốc
                      books.remove(selectedBook);

                      System.out.println("Đã xóa sách: " + selectedBook.getTitle());
                    }

                  });

                MenuItem showHoldRequestQueue = new MenuItem("Show hold request queue");
                showHoldRequestQueue.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleShowHoldRequestQueue(book);
                });

                // Add all menu items to the MenuButton
                menuButton.getItems().addAll(holdItem, checkOutItem, checkInItem, showHoldRequestQueue, deleteItem);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(menuButton); // Show the MenuButton in the cell
                }
            }
        });



        // Tạo ObservableList từ danh sách sách gốc
        bookList = FXCollections.observableArrayList(books);

        // Tạo FilteredList để hỗ trợ tìm kiếm
        FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);

        // Gán dữ liệu cho bảng
        tableBooks.setItems(filteredData);

        // Lắng nghe sự thay đổi của thanh tìm kiếm
        bookSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return book.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                        book.getAuthor().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(book.getID()).contains(lowerCaseFilter);
            });
        });

        // Lắng nghe phím Backspace trên bảng
        tableBooks.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận xóa");
                    alert.setHeaderText("Bạn có chắc chắn muốn xóa sách này?");
                    alert.setContentText("Sách: " + selectedBook.getTitle());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Xóa trong ObservableList
                        bookList.remove(selectedBook);

                        // Xóa trong ArrayList gốc
                        books.remove(selectedBook);

                        System.out.println("Đã xóa sách: " + selectedBook.getTitle());
                    }
                }
            }
        });

        // Lắng nghe khi chọn dòng mới và hiển thị chi tiết sách
        tableBooks.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showBookDetails(newValue);
                    }
                });
    }

    @FXML
    private void handleHoldBookAction(Book book) {
        // Similar to handleHoldRequest in OnTerminal.java
        Borrower borrower = handleFindBorrower();
        if (borrower != null) {
            showAlert("Place Book on Hold operation", book.makeHoldRequest(borrower));
        }
    }

    @FXML
    private void handleCheckOutBookAction(Book book) {
        // Similar to handleBookIssue in OnTerminal.java
        Borrower borrower = handleFindBorrower();
        if (borrower != null) {
            String message = book.issueBook(borrower, (Librarian) library.getUser());
            boolean secondConfirm = showConfirmation("Check Out Book Operation", message);
            if (message.contains("Would you like to place the book on hold?") && secondConfirm) {
                showAlert("Place Book on Hold operation", book.makeHoldRequest(borrower));
            }
        }
    }

    @FXML
    private void handleCheckInBookAction(Book book) {
        // Similar to handleBookReturn in OnTerminal.java
        if (!book.getIssuedStatus()) {
            showAlert("Check In Operation", "This book has not been issued yet!");
        } else {
            Loan loan = book.getLoan();
            if (showConfirmation("Check in Confirmation", "This book is now borrowed by "
                    + loan.getBorrower().getName() + ".\nCheck in this Book?")) {
                String message = book.returnBook(loan.getBorrower(), loan, (Librarian) library.getUser());
                showAlert("Check In Operation", message);
            }
        }
    }

    @FXML
    private void handleDeleteBookAction(Book book) {
        String result = library.removeBookfromLibrary(book);

        showAlert("Remove Book from Library", result);

        if (result.endsWith("The book is successfully removed.\n")) {
            bookList.remove(book);
        }
    }


    @FXML
    private void handleShowHoldRequestQueue(Book book) {
        if (book == null) {
            showAlert("Error", "No book selected to show hold requests.");
            return;
        }

        List<HoldRequest> holdRequests = book.getHoldRequests(); // Assume this method exists
        Stage stage = new Stage();
        stage.setTitle("Hold Request Queue for " + book.getTitle());

        if (holdRequests.isEmpty()) {
            showAlert("Information", "No hold requests available for this book.");
            return;
        }

        TableView<HoldRequest> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<HoldRequest, String> noColumn = new TableColumn<>("No.");
        noColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(holdRequests.indexOf(data.getValue()) + 1)));
        noColumn.setPrefWidth(40);

        TableColumn<HoldRequest, String> titleColumn = new TableColumn<>("Book's Title");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
        titleColumn.setCellFactory( column -> {
            return new TableCell<HoldRequest, String>() {
                private final Text text;

                {
                    text = new Text();
                    text.wrappingWidthProperty().bind(column.widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
        });
        titleColumn.setPrefWidth(500);

        TableColumn<HoldRequest, Integer> borrowerIDColumn = new TableColumn<>("BorrowerID");
        borrowerIDColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBorrower().getID()).asObject());
        borrowerIDColumn.setPrefWidth(90);

        TableColumn<HoldRequest, String> borrowerColumn = new TableColumn<>("Borrower's Name");
        borrowerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrower().getName()));
        borrowerColumn.setPrefWidth(150);

        TableColumn<HoldRequest, String> dateColumn = new TableColumn<>("Request Date");
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRequestDate().toString()));

        table.getColumns().addAll(noColumn, titleColumn, borrowerIDColumn, borrowerColumn, dateColumn);

        ObservableList<HoldRequest> holdRequestList = FXCollections.observableArrayList(holdRequests);
        table.setItems(holdRequestList);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(new Label("Hold Request Queue for: " + book.getTitle()), table);

        Scene scene = new Scene(vbox, 1000, 400);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Borrower handleFindBorrower() {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Search Borrower");
        dialog.setHeaderText("Search for a Borrower using specific criteria");

        // Set the dialog's buttons
        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        // Create a dropdown (ChoiceBox) and a TextField
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("ID", "Name");
        choiceBox.setValue("ID"); // Default selection

        TextField textField = new TextField();
        textField.setPromptText("Enter search value");

        // Layout: Arrange the ChoiceBox and TextField
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Search By:"), 0, 0);
        grid.add(choiceBox, 1, 0);
        grid.add(new Label("Search Value:"), 0, 1);
        grid.add(textField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result when the "Search" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButtonType) {
                return new Pair<>(choiceBox.getValue(), textField.getText());
            }
            return null;
        });

        // Show the dialog and process the result
        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            String searchType = result.get().getKey();
            String searchValue = result.get().getValue();

            // Perform the search based on the selected type
            Borrower borrower = null;
            try {
                switch (searchType) {
                    case "ID":
                        borrower = library.logicalFindBorrower(Integer.parseInt(searchValue));
                        break;
                    case "Name":
//                        borrower = library.findBorrowerByName(searchValue);
                        break;
                }

                // Display the result
                if (borrower != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Borrower Found");
                    alert.setHeaderText("Borrower's Name");
                    alert.setContentText(borrower.getName());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Borrower Not Found");
                    alert.setHeaderText("No Borrower Found");
                    alert.setContentText("No borrower matches the provided information.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            return borrower;
        }

        return null;
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
                } else {
                    return !(person instanceof Librarian) && "false".contains(
                            lowerCaseFilter); // Khớp với is_Librarian là False
                }
// Không khớp
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

//

    @FXML
    void handleAddBook() {
        showPane(paneAddBook);

        // Thiết lập cột với thuộc tính của lớp Book
        addBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        addBookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // Thiết lập cột hành động với nút "Add"
        addBookBtnColumn.setCellFactory(column -> new TableCell<>() {
            private final Button addButton = new Button("Add");

            {
                addButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());

                    // Kiểm tra xem sách đã tồn tại trong danh sách hay chưa
                    boolean bookExists = bookList.stream().anyMatch(existingBook ->
                            existingBook.getTitle().equalsIgnoreCase(book.getTitle()) &&
                                    existingBook.getAuthor().equalsIgnoreCase(book.getAuthor())
                    );

                    if (bookExists) {
                        // Hiển thị thông báo nếu sách đã tồn tại
                        System.out.println("Book already exists: " + book.getTitle());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Duplicate Book");
                        alert.setHeaderText("This book already exists in the library.");
                        alert.setContentText("You cannot add the same book again.");
                        alert.showAndWait();
                    } else {
                        // Thêm sách mới nếu chưa tồn tại
                        System.out.println("Adding book: " + book.getTitle());
                        Book newBook = new Book(book.getCurrentIdNumber() + 1, book.getTitle(),
                                book.getSubtitle(),
                                book.getAuthor(), book.getIssuedStatus(), book.getImageLink(),
                                book.getPreviewLink());
                        bookList.add(newBook);
                        library.addBookinLibrary(newBook);
                    }
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

        tableAddBooks.setItems(apiBooksList);

        // Thực hiện debounce và throttle tìm kiếm
        bookApiSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                apiBooksList.clear(); // Xóa bảng nếu từ khóa tìm kiếm trống
                return;
            }

            // Chỉ tìm kiếm nếu từ khóa có tối thiểu 3 ký tự
            if (newValue.trim().length() < 3) {
                return;
            }

            // Cập nhật giá trị tìm kiếm hiện tại
            searchQuery.set(newValue.trim());

            // Hủy bỏ nhiệm vụ đã lên lịch trước đó nếu có
            if (scheduledFuture != null && !scheduledFuture.isDone()) {
                scheduledFuture.cancel(false);
            }

            // Lên lịch gọi API sau 500ms (debounce)
            scheduledFuture = debounceScheduler.schedule(
                    this::performSearch, debounceDelay, TimeUnit.MILLISECONDS
            );
        });
    }

    private void performSearch() {
        String query = searchQuery.get();
        CompletableFuture.supplyAsync(() -> {
            try {
                // Chỉ gọi API mỗi giây một lần để tránh quá tải (throttle)
                if (System.currentTimeMillis() - lastApiCallTime < 500) {
                    return null;
                }

                lastApiCallTime = System.currentTimeMillis();
                String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=10";
                String jsonResponse = apiTest.getHttpResponse(apiUrl);

                if (jsonResponse != null) {
                    return apiTest.getBooksFromJson(jsonResponse);
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi gọi API: " + e.getMessage());
            }
            return null;
        }).thenAccept(booksFromAPI -> {
            if (booksFromAPI == null) {
                return;
            }

            // Chỉ cập nhật nếu kết quả khác biệt
            Platform.runLater(() -> {
                if (!apiBooksList.equals(booksFromAPI)) {
                    apiBooksList.clear();
                    apiBooksList.addAll(booksFromAPI);
                }
            });
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
