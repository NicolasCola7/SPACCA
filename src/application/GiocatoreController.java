package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GiocatoreController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
