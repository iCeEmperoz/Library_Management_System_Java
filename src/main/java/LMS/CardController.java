package LMS;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CardController {

  @FXML
  private HBox box;

  @FXML
  private Label bookAuthor;

  @FXML
  private Label bookSubject;

  @FXML
  private Label bookTitle;

  @FXML
  private ImageView bookImage;

  private final String[] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

  public void setData(Book book) {
    Image image = new Image(getClass().getResourceAsStream(book.getTitle() + ".jpg"));

    bookImage.setImage(image);
    bookTitle.setText(book.getTitle());
    bookAuthor.setText(book.getAuthor());
    bookSubject.setText(book.getSubject());
    box.setStyle("-fx-background-color: #" + colors[(int) (Math.random() * colors.length)] + ";"
        + " -fx-background-radius: 15;"
        + " -fx-effect: dropShadow(three-pass-box, rgba(0,0,0,0),10,0,0,10);");
  }
}
