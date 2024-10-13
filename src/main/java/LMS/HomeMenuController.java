package LMS;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controller class for the Home Menu in the Library Management System.
 * This class is responsible for handling the interactions and events
 * in the Home Menu FXML file.
 * 
 * <p>It uses JavaFX annotations to link UI components defined in the FXML
 * file to the controller class.</p>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code resources} - ResourceBundle that can be used to localize the UI.</li>
 *   <li>{@code location} - URL location of the FXML file that was given to the FXMLLoader.</li>
 *   <li>{@code x1} - Font object linked to the FXML component with fx:id="x1".</li>
 *   <li>{@code x2} - Color object linked to the FXML component with fx:id="x2".</li>
 *   <li>{@code x3} - Font object linked to the FXML component with fx:id="x3".</li>
 *   <li>{@code x4} - Color object linked to the FXML component with fx:id="x4".</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@code initialize()} - This method is called after the FXML file has been loaded.
 *   It ensures that all the FXML components are properly injected.</li>
 * </ul>
 * 
 * <p>Assertions are used in the {@code initialize()} method to verify that the
 * FXML components are not null, indicating that they were successfully injected.</p>
 */
public class HomeMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void initialize() {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'HomeMenu.fxml'.";
        assert x2 != null : "fx:id=\"x2\" was not injected: check your FXML file 'HomeMenu.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'HomeMenu.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'HomeMenu.fxml'.";

    }

}
