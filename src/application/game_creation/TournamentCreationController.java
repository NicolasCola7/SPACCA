package application.game_creation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

import game.GameType;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import leaderboard.Leaderboard;

public class TournamentCreationController extends GameCreationController implements Initializable  {

	private final int numberOfPlayers=8;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
	    populatePlayersList();
		
		Leaderboard tournamentsLeaderboard=new Leaderboard(adminUsername,GameType.TOURNAMENT);
		
		confirmButton.disableProperty().bind(
				gameCode.textProperty().isEmpty().or(
				playersSelection.getSelectionModel().selectedItemProperty().isNull()));
		
	 }
	
	public void goToHome(ActionEvent event) throws IOException {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	public void back(ActionEvent event) throws IOException {
		root = FXMLLoader.load(new File("src/application/Admin.fxml").toURI().toURL());
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void confirm(ActionEvent e) throws IOException  {
		gamePlayers=new ArrayList<String>(numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		
		if(((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers)) && gameCodeCheck(gameCode.getText())) {
			
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot"+i);
			
			addToGamesDatasFile(); 
			
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Torneo creato correttamente:");
			alert.setContentText("Vuoi tornare alla home?");
			if(alert.showAndWait().get()==ButtonType.OK) {
				root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
				stage=(Stage)((Node)e.getSource()).getScene().getWindow();
				scene=new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
			
		}
		else if(!botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Giocatori selezionati insufficienti o troppi:");
			alert.setContentText("Seleziona "+numberOfPlayers+" giocatori per continuare!");
			alert.showAndWait();
		}
		else if(gameCodeCheck(gameCode.getText())==false) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Codice partita già uso:");
			alert.setContentText("Inserisci un nuovo codice partita!");
			alert.showAndWait();
			gameCode.clear();
		}
	}
	
	public void addToGamesDatasFile()  {
		try {
			FileWriter writer = new FileWriter(new File("./Files/ConfigurationFiles/GamesDatas.csv"),true);
			PrintWriter pw=new PrintWriter(writer);
			Collections.shuffle(gamePlayers);
			String datas=adminUsername+",tournament,"+gameCode.getText()+","+numberOfPlayers+","+gamePlayers.get(0);
			for(int i=1;i< numberOfPlayers;i++)
				datas+=","+gamePlayers.get(i);
			pw.println(datas);
			pw.close();
			gameCode.clear();
			botCheck.setSelected(false);
			playersSelection.getSelectionModel().clearSelection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}