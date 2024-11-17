package LMS;

import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
  private Label bookSubtitle;

  @FXML
  private Label bookTitle;

  @FXML
  private ImageView bookImage;

  public void setData(Book book) {
    Image image = new Image(book.getImageLink());
    bookImage.setImage(image);
    bookTitle.setText(book.getTitle());
    bookAuthor.setText(book.getAuthor());
//    bookSubtitle.setText(book.getSubtitle());
  }
}
