package LMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class Controller implements Initializable {

  @FXML
  private HBox cardLayout;
  private List<Book> recentlyAdded;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    recentlyAdded = new ArrayList<>(recentlyAdded());
    try {
      for (int i = 0; i < recentlyAdded.size(); i++) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Card.fxml"));
        HBox cardBox = fxmlLoader.load();
        CardController cardController = fxmlLoader.getController();
        cardController.setData(recentlyAdded.get(i));
        cardLayout.getChildren().add(cardBox);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<Book> recentlyAdded() {
    List<Book> ls = new ArrayList<>();

    Book book = new Book();
    book.setTitle("FORTRESS BLOOD");
    book.setAuthor("L.D. GOFFIGAN");
    book.setSubject("nov");
    ls.add(book);

    book = new Book();
    book.setTitle("FROST ARCH");
    book.setAuthor("KATE BLOOMFIELD");
    book.setSubject("nov");
    ls.add(book);

    book = new Book();
    book.setTitle("THE LAST FOR THINGS");
    book.setAuthor("PAUL HOFFMAN");
    book.setSubject("nov");
    ls.add(book);
    return ls;
  }
}
