package LMS;

import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookController {

  @FXML
  private Label bookAuthor;

  @FXML
  private Label bookSubject;

  @FXML
  private Label bookTitle;

  @FXML
  private ImageView bookImage;

  public void setData(Book book) {
    Image image = new Image(
        Objects.requireNonNull(getClass().getResource(("/images/" + book.getTitle() + ".jpeg"))).toString());
    bookImage.setImage(image);

    bookTitle.setText(book.getTitle());
    bookAuthor.setText(book.getAuthor());
    bookSubject.setText(book.getSubject());

  }
}
