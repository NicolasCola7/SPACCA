package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import leaderboard.Leaderboard;

public class AdminController{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button homeButton;
	
	// back to home page
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
	
	//new player creation
	public void newPlayer(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("NewPlayer.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//edit existing player
	public void editExisting(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("EditExisting.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//classic game creation
	public void standardMatch(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ClassicGameCreation.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//tournamnet creation
	public void tournament(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("TournamentCreation.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void suspendedGames(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("SuspendedGames.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
