package LMS;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Controller implements Initializable {

  @FXML
  private HBox cardLayout;

  @FXML
  private GridPane bookContainer;

  @FXML
  private Button backButton;

  @FXML
  private Button changeAvatarButton;

  @FXML
  private ImageView userImageView;

  @FXML
  private BorderPane dashboard_form;

  @FXML
  private Button user_btn;

  @FXML
  private VBox user_form;

  private List<Book> recentlyAdded;

  private List<Book> recommended;

  private FileChooser fileChooser;
  private File filePath;

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

    Circle clip = new Circle(100,100,100);
    userImageView.setClip(clip);
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

    Book book = new Book();
    book.setTitle("FORTRESSBLOOD");
    book.setAuthor("L.D. GOFFIGAN");
    ls.add(book);
    return ls;
  }

  private List<Book> books() {
    List<Book> ls = new ArrayList<>();

    Book book = new Book();
    book.setTitle("FORTRESSBLOOD");
    book.setAuthor("L.D. GOFFIGAN");
    ls.add(book);
    return ls;
  }
}
