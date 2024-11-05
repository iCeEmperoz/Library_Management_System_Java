package LMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller implements Initializable {

  @FXML
  private HBox cardLayout;

  @FXML
  private GridPane bookContainer;

  private List<Book> recentlyAdded;
  private List<Book> recommended;

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
  }

  private List<Book> recentlyAdded() {
    List<Book> ls = new ArrayList<>();

    Book book = new Book();
    book.setTitle("FORTRESSBLOOD");
    book.setAuthor("L.D. GOFFIGAN");
    book.setSubtitle("nov");
    ls.add(book);

//    book = new Book();
//    book.setTitle("FROST ARCH");
//    book.setAuthor("KATE BLOOMFIELD");
//    book.setSubtitle("nov");
//    ls.add(book);
//
//    book = new Book();
//    book.setTitle("THE LAST FOR THINGS");
//    book.setAuthor("PAUL HOFFMAN");
//    book.setSubtitle("nov");
//    ls.add(book);
    return ls;
  }

  private List<Book> books() {
    List<Book> ls = new ArrayList<>();

    Book book = new Book();
    book.setTitle("FORTRESSBLOOD");
    book.setAuthor("L.D. GOFFIGAN");
    book.setSubtitle("nov");
    ls.add(book);

//    book = new Book();
//    book.setTitle("FROST ARCH");
//    book.setAuthor("KATE BLOOMFIELD");
//    book.setSubtitle("nov");
//    ls.add(book);
//
//    book = new Book();
//    book.setTitle("THE LAST FOR THINGS");
//    book.setAuthor("PAUL HOFFMAN");
//    book.setSubtitle("nov");
//    ls.add(book);
    return ls;
  }
}
