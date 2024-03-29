package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PlayersManagementController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button homeButton;
	@FXML private Button backButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ImageView backImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Back2.png")));
		ImageView homeImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Home2.png")));
		backImg.setFitWidth(backButton.getPrefWidth());
		homeImg.setFitWidth(homeButton.getPrefWidth());
		backImg.setFitHeight(backButton.getPrefHeight());
		homeImg.setFitHeight(homeButton.getPrefHeight());
		homeButton.setGraphic(homeImg);
		backButton.setGraphic(backImg);
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
	public void back(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void newPlayer(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("NewPlayer.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void editExisting(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("EditExisting.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
