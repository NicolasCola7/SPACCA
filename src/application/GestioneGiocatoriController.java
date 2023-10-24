package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class GestioneGiocatoriController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	public void goToHome(ActionEvent event) throws IOException {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("home.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	public void indietro(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Amministratore.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void nuovoGiocatore(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("NuovoGiocatore.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void modificaEsistenti(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ModificaEsistenti.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
