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
  private Label bookSubtitle;

  @FXML
  private Label bookTitle;

  @FXML
  private ImageView bookImage;

  public void setData(Book book) {
//    Image image = new Image(
//        Objects.requireNonNull(getClass().getResource(("/images/" + book.getTitle() + ".jpeg"))).toString());
    Image image = new Image("https://th.bing.com/th/id/OIP.Sb3bVfKu5y_jlon2oldTDwHaLH?rs=1&pid=ImgDetMain");
    bookImage.setImage(image);
    bookTitle.setText(book.getTitle());
    bookAuthor.setText(book.getAuthor());
//    bookSubtitle.setText(book.getSubtitle());

  }
}
