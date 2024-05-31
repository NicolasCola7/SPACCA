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
import game.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
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
	
	public void goToHome(ActionEvent event) {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			try {
				root = FXMLLoader.load(new File("FXML/home.fxml").toURI().toURL());
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
				scene=new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
				e.printStackTrace();
			}
		}
	}
	
	public void back(ActionEvent event){
		try {
			root = FXMLLoader.load((new File("FXML/Admin.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//confirm button method, create tournament game and init proprieties
	public void confirm(ActionEvent e) {
		gamePlayers=new ArrayList<String>(numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		
		//check if number of players is different than array size and game code is present
		if(((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers)) && gameCodeCheck(gameCode.getText())) {
			
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot"+i);
			
			addToGamesDatasFile(); 
			
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Torneo creato correttamente:");
			alert.setContentText("Vuoi tornare alla home?");
			if(alert.showAndWait().get()==ButtonType.OK) {
				try {
					root = FXMLLoader.load(new File("FXML/home.fxml").toURI().toURL());
					stage=(Stage)((Node)e.getSource()).getScene().getWindow();
					scene=new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e1) {
					showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
					e1.printStackTrace();
				}
			}
			
		}
		//bot is not selected and players are too much or not enough
		else if(!botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) {
			showErrorMessage("Giocatori selezionati insufficienti o troppi:", "Seleziona "+numberOfPlayers+" giocatori per continuare!");
		}
		//check if game code is not insert
		else if(gameCodeCheck(gameCode.getText())==false) {
			showErrorMessage("Codice partita già uso:", "Inserisci un nuovo codice partita!");
			gameCode.clear();
		}
	}
	
	//save game data on file 
	public void addToGamesDatasFile()  {
		try (FileWriter writer = new FileWriter(new File("./Files/ConfigurationFiles/GamesDatas.csv"),true);
			PrintWriter pw=new PrintWriter(writer)){
			
			Collections.shuffle(gamePlayers);
			String datas=adminUsername+",tournament,"+gameCode.getText()+","+numberOfPlayers+","+gamePlayers.get(0);	//set the output string 	
			for(int i=1;i< numberOfPlayers;i++)
				datas+=","+gamePlayers.get(i);
			pw.println(datas);
			gameCode.clear();
			botCheck.setSelected(false);
			playersSelection.getSelectionModel().clearSelection();
		}catch(FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
}