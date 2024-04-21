package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PlayerController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	private Alert alert;
	private String gameType;
	@FXML private TextField gameCode;
	@FXML private Button playButton;
	private String adminUsername;
	@FXML private Button homeButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playButton.disableProperty().bind(gameCode.textProperty().isEmpty());
		
		ImageView homeImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Home2.png")));
		homeImg.setFitWidth(homeButton.getPrefWidth());
		homeImg.setFitHeight(homeButton.getPrefHeight());
		homeButton.setGraphic(homeImg);
	}
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void play(ActionEvent event)  {
		if(checkGameCode(gameCode.getText())==true) {
			if(gameType.equals("classic")) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassicGame.fxml"));
				
				try {
					Parent classicGameScene = loader.load();
					Scene scene=new Scene(classicGameScene);
					ClassicGameController classicGameController=loader.getController();
		            classicGameController.setGameCode(gameCode.getText());
		            classicGameController.setAdminUsername(adminUsername);
		            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Tournament.fxml"));
				
				try {
					Parent tournamentScene = loader.load();
					Scene scene=new Scene(tournamentScene);
					TournamentController tournamentController=loader.getController();
		            tournamentController.setGameCode(gameCode.getText());
		            tournamentController.setAdminUsername(adminUsername);
		            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non esiste alcuna partita associata al seguente codice:");
			alert.setContentText("Inserisci un nuovo codice partita!");
			alert.showAndWait();
			gameCode.clear();
		}	
	}
	
	private boolean checkGameCode(String code) {
		boolean check=true;
		File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		Scanner scan;
		try {
			scan = new Scanner(datas);
		while(scan.hasNextLine()) {
			String[] line=scan.nextLine().split(",");
			if(gameCode.getText().equals(line[2])) {
				adminUsername=line[0];
				gameType=line[1];
				check=true;
				break;
			}
			else
				check=false;
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return check;
	}
	
}
