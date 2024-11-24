package LMS;

import static LMS.HandleAlertOperations.showAlert;
import static LMS.HandleAlertOperations.showConfirmation;

import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.*;
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
    public Button btnNotifications;

    private ObservableList<Loan> loanBookList;
    private ObservableList<Book> bookList;

    private List<Book> recentlyAdded;

    private List<Book> recommended;

    private FileChooser fileChooser;

    private File filePath;
    @FXML
    private TableView<Loan> tableBookShelf;

    @FXML
    private HBox cardLayout;

    @FXML
    private GridPane bookContainer;

    @FXML
    private ImageView userImageView;

    @FXML

    private BorderPane paneHome;
    @FXML
    private AnchorPane paneInformation;
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
    private AnchorPane paneHistory;
    @FXML
    private AnchorPane paneYourShelf;
    @FXML
    private ImageView qrImage;
    @FXML
    private Label textSubTiltle;
    @FXML
    private Label textLoanDescription;
    @FXML
    private StackPane box;
    @FXML
    private StackPane boxLoan;
    @FXML
    private TextField infoName;
    @FXML
    private TextField infoEmail;
    @FXML
    private TextField infoAddress;
    @FXML
    private TextField infoPhone;
    @FXML
    private Label labelWelcome;
    @FXML
    private TableColumn<Loan, String> bookShelfAuthorColumn;

  @FXML
  private TableColumn<Loan, Integer> bookShelfIdColumn;

  @FXML
  private TextField bookShelfSearchTextField;

    @FXML
    private TableColumn<Loan, String> bookShelfTitleColumn;
    @FXML
    private AnchorPane paneNotifications;
    @FXML
    private TableView<String> tableNotifications;
    @FXML
    private TableColumn<String, Integer> notificationsNoColumn;
    @FXML
    private TableColumn<String, String> notificationsMessageColumn;
    @FXML
    private Button btnClearNotifications;

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
        initializeTableLoanBooks();
        initializeInformation();
        initializeTableNotifications();
    }

    private void showBookDetails(Book selectedBook) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(selectedBook);

            // Cập nhật thông tin sách
            textSubTiltle.setText(selectedBook.getDescription());

            // Thay toàn bộ nội dung của StackPane
            box.getChildren().setAll(cardBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showFullBookDetails(Book selectedBook) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(selectedBook);

            // Cập nhật thông tin sách
            qrImage.setImage(selectedBook.generateQRCodeImage(selectedBook.getPreviewLink(), 150, 150));
            textLoanDescription.setText(selectedBook.getDescription());

            // Thay toàn bộ nội dung của StackPane
            boxLoan.getChildren().setAll(cardBox);
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

    private void initializeTableLoanBooks() {
        Borrower borrower = (Borrower) library.getUser();

        // Thiết lập các cột với thuộc tính của lớp Loan
        bookShelfIdColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getBook().getID()).asObject());
        bookShelfTitleColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        bookShelfAuthorColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));

        loanBookList = FXCollections.observableArrayList(borrower.getBorrowedBooks());

        // Tạo FilteredList để hỗ trợ tìm kiếm
        FilteredList<Loan> filteredData = new FilteredList<>(loanBookList, b -> true);

        // Lắng nghe sự thay đổi từ trường tìm kiếm (searchTextField)
        bookShelfSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(loan -> {
                // Nếu thanh tìm kiếm trống, hiển thị toàn bộ danh sách
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Chuyển từ khóa sang chữ thường để so khớp không phân biệt hoa/thường
                String lowerCaseFilter = newValue.toLowerCase();

                // So khớp từ khóa với các thuộc tính của Loan
                return loan.getBook().getTitle().toLowerCase().contains(lowerCaseFilter)
                        || loan.getBook().getAuthor().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(loan.getBook().getID()).contains(lowerCaseFilter);
            });
        });

        // Thiết lập dữ liệu cho bảng
        tableBookShelf.setItems(filteredData);

        // Lắng nghe sự thay đổi khi chọn một mục trong bảng
        tableBookShelf.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showFullBookDetails(newValue.getBook());
                    }
                });
    }

    private void initializeTableNotifications() {
        ArrayList<String> notifications = library.getUser().getNotifications();
        ObservableList<String> notificationList = FXCollections.observableArrayList(notifications);
        tableNotifications.setItems(notificationList);

        // Set up the columns
        notificationsNoColumn.setCellValueFactory(cellData -> {
            int index = tableNotifications.getItems().indexOf(cellData.getValue()) + 1;
            return Bindings.createIntegerBinding(() -> index).asObject();
        });
        notificationsNoColumn.setPrefWidth(30);

        notificationsMessageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Use a custom cell factory to ensure the row numbers are updated correctly
        notificationsNoColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }


    private void initializeInformation() {
        Borrower librarian = (Borrower) library.getUser();
        infoName.setText(librarian.getName());
        infoEmail.setText(librarian.getEmail());
        infoAddress.setText(librarian.getAddress());
        infoPhone.setText(String.valueOf(librarian.getPhoneNo()));
        labelWelcome.setText("Welcome, " + librarian.getName());
    }

    @FXML
    public void handleHoldBookAction(ActionEvent event) {
        Borrower borrower = (Borrower) library.getUser();
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook.getHoldRequests().isEmpty()) {
            if (showConfirmation("Borrow Book", "Do you want to want to borrow this Book?"
                    + "\nYou will have to wait for acceptance from our Librarians.")) {
                String response = selectedBook.makeHoldRequest(borrower);
                // Notify librarians about borrow action
                selectedBook.notifyObservers("Please service the hold request for " + selectedBook.getTitle() + " by " + borrower.getName() + " (ID: " + borrower.getID() + ").");
            }
        } else {
            if (showConfirmation("Place Book on Hold", "There are earlier requests." +
                    "\nDo you want to place it on hold?")) {
                showAlert("Place Book on Hold operation", selectedBook.makeHoldRequest(borrower));
            }
        }
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
        paneBooks.setVisible(false);
        paneYourShelf.setVisible(false);
        paneHistory.setVisible(false);
        paneNotifications.setVisible(false);
        paneToShow.setVisible(true);
    }

    @FXML
    void handleBooks(ActionEvent event) {
        showPane(paneBooks);
    }

    @FXML
    void handleShowInformation(ActionEvent event) {
        paneHome.setVisible(false);
        paneInformation.setVisible(true);


    }

    @FXML
    void handleBack(ActionEvent event) {
        paneInformation.setVisible(false);
        paneHome.setVisible(true);
    }

    @FXML
    void handleHistory(ActionEvent event) {
        showPane(paneHistory);
    }

    @FXML
    void handleYourShelf(ActionEvent event) {
        showPane(paneYourShelf);
    }

    @FXML
    void handleNotifications(ActionEvent event) {
        showPane(paneNotifications);
    }

    @FXML
    private void handleClearNotifications(ActionEvent event) {
        if (showConfirmation("Clear Notifications", "Are you sure you want to clear all notifications?")) {
            library.getUser().clearNotifications();
            tableNotifications.getItems().clear();
        }
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
