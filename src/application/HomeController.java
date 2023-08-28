package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomeController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	public void goToAmministratore(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LoginAmministratore.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void goToGiocatore(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Giocatore.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
