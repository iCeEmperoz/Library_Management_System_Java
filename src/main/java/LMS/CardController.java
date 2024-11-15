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

  private final String[] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

  public void setData(Book book) {
    Image image = new Image(
        Objects.requireNonNull(getClass().getResource("/images/" + book.getTitle() + ".jpeg"))
            .toString());
    bookImage.setImage(image);
    bookTitle.setText(book.getTitle());
    bookAuthor.setText(book.getAuthor());
//    bookSubtitle.setText(book.getSubtitle());

    box.setStyle("-fx-background-color: #" + colors[(int) (Math.random() * colors.length)] + "; "
        + "-fx-background-radius: 15; "
        + "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 10);");

  }
}
