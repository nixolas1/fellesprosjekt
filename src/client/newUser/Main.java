package client.newUser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import network.Query;
import network.ThreadClient;

import java.util.Hashtable;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));;
			Scene scene = new Scene(root,1200,800);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean createUser(final String user, final String pass, final String domain) {
        Hashtable<String, String> data = new Hashtable<String, String>() {{
            put("user",user);
            put("pass", pass);
            put("domain", domain);
        }};
        ThreadClient socket = new ThreadClient();

        Query reply = socket.send(new Query("create", data));
        Hashtable<String, Boolean> response = reply.data;

        if(response.get("reply")){
            System.out.println("success");
        }

        return response.get("reply");

    }

    static Stage stage;
    public static void show(Stage primaryStage) {
        try {
            stage = primaryStage;
            GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}


}
