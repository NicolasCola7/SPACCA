
package application.game_creation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import leaderboard.Leaderboard;

public class ClassicGameCreationController extends GameCreationController implements Initializable {
	
	
	@FXML private ChoiceBox<Integer> numberOfPlayerSelection;
	private int numberOfPlayers;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
	    populatePlayersList();   
		numberOfPlayerSelection.getItems().addAll(2,3,4,5); 
		Leaderboard classicGamesLeaderboard=new Leaderboard(adminUsername,GameType.CLASSIC);	//create leaderboard of game
		
		confirmButton.disableProperty().bind(		//disable confirm button if something is empty
				gameCode.textProperty().isEmpty().or(
				numberOfPlayerSelection.valueProperty().isNull().or(
				playersSelection.getSelectionModel().selectedItemProperty().isNull())));
	
	}
	
	public void goToHome(ActionEvent event){
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			try {
				root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
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
			root = FXMLLoader.load((new File("src/application/Admin.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}	
	}
	
	//confirm button method, create classic game and init proprieties
	public void confirm(ActionEvent e) {
		numberOfPlayers=numberOfPlayerSelection.getSelectionModel().getSelectedItem();
		gamePlayers=new ArrayList<String>( numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		//check if number of players is different than array size and game code is present
		if(((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers)) && gameCodeCheck(gameCode.getText())) {
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot"+i);	//fill array with bot
				addToGamesDatasFile();
				Alert alert=new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Partita creata correttamente:");
				alert.setContentText("Vuoi tornare alla home?");
				if(alert.showAndWait().get()==ButtonType.OK) {
					try {
						root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
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
			gameCode.clear();
			showErrorMessage("Codice partita già uso:", "Inserisci un nuovo codice partita!");
		}
	}
	
	//save game data on file 
	public void addToGamesDatasFile() {
		try(FileWriter writer = new FileWriter(new File("./Files/ConfigurationFiles/GamesDatas.csv"),true);
			PrintWriter pw=new PrintWriter(writer)) {
			
			String datas=adminUsername+",classic,"+gameCode.getText()+","+numberOfPlayers+","+gamePlayers.get(0); //set the output string 	
			for(int i=1;i< numberOfPlayers;i++)
				datas+=","+gamePlayers.get(i);
			pw.println(datas);
			gameCode.clear();
			botCheck.setSelected(false);
			playersSelection.getSelectionModel().clearSelection();
			numberOfPlayerSelection.getSelectionModel().clearSelection();
		}catch(FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
}
