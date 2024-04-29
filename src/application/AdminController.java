package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

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

public class AdminController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	private String adminUsername;
	@FXML private Button homeButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ImageView homeImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Home2.png")));	
		homeImg.setFitWidth(homeButton.getPrefWidth());
		homeImg.setFitHeight(homeButton.getPrefHeight());
		homeButton.setGraphic(homeImg);
		
		//creo i file leaderboard
		Leaderboard classicGamesLeaderboard=new Leaderboard(adminUsername,GameType.CLASSIC);
		Leaderboard tournamentsLeaderboard=new Leaderboard(adminUsername,GameType.TOURNAMENT);
		
	}
	
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
	public void playersManagement(ActionEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource("PlayersManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void matchesManagement(ActionEvent event)  throws IOException{
		root = FXMLLoader.load(getClass().getResource("MatchesManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private void getCurrentAdmin() {	
		try {
			Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"));
			  while (scan.hasNextLine()) {
		        	String line=scan.nextLine();
		        	adminUsername=line;
		        }
			  scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
