package client.newAppointment;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = (GridPane) FXMLLoader.load(Main.class.getResource("view.fxml"));;
			Scene scene = new Scene(root,500,475);
			/*scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
