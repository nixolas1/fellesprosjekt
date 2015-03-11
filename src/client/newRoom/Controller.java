package client.newRoom;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rae on 11.03.15.
 */
public class Controller implements Initializable{

    @FXML private TextField name, capacity, from, to;
    @FXML private Button add, remove, create, cancel;
    @FXML private ListView utilityList;
    @FXML private URL location;
    @FXML private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addUtility() {

    }

    public void removeUtility() {

    }
}
