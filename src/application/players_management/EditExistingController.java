package application.players_management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import leaderboard.Leaderboard;

public class EditExistingController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button deleteButton;
	@FXML private Button modifyButton;
	@FXML private ChoiceBox<String> selectedPlayer;
	@FXML private TextField newPlayerName;
	@FXML private Alert alert;
	@FXML private Button homeButton;
	@FXML private Button backButton;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	private Leaderboard classicGamesLeaderboard;
	private Leaderboard tournamentsLeaderboard;
	
	@Override
	//get info, leaderboard and disable buttons
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
		getPlayersNames();
		
		classicGamesLeaderboard=new Leaderboard(adminUsername,GameType.CLASSIC);
		tournamentsLeaderboard=new Leaderboard(adminUsername,GameType.TOURNAMENT);
		
		selectedPlayer.getItems().addAll(players);
		deleteButton.disableProperty().bind(selectedPlayer.valueProperty().isNull());
		modifyButton.disableProperty().bind(selectedPlayer.valueProperty().isNull().or(newPlayerName.textProperty().isEmpty()));
		
	}
	public void goToHome(ActionEvent event)  {
		alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			try {
				root = FXMLLoader.load((new File("FXML/home.fxml").toURI().toURL()));
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
	public void back(ActionEvent event)  {
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
	
	//delete player and remove it from leaderboard
	public void deletePlayer(ActionEvent event) {
		String selectedName = selectedPlayer.getSelectionModel().getSelectedItem();
		alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Eliminazione giocatore");
		alert.setHeaderText("Stai per eliminare il giocatore selezionato");
		alert.setContentText("Sei sicuro di voler continuare?");
		if (alert.showAndWait().get()== ButtonType.OK) {
		 	selectedPlayer.getSelectionModel().clearSelection();
            players.remove(selectedName);
            selectedPlayer.getItems().remove(selectedName);
            updatePlayersList();
            classicGamesLeaderboard.deleteFromLeaderboard(selectedName);
            tournamentsLeaderboard.deleteFromLeaderboard(selectedName);
		}
	}
	
	//rename player and update it from leaderboard
	public void renamePlayer(ActionEvent event) {
		String selectedName = selectedPlayer.getSelectionModel().getSelectedItem();
		String newName=newPlayerName.getText();
		 if (!players.contains(newName) && (newName.length()<3 || !newName.substring(0, 3).equalsIgnoreCase("bot"))) {
		 	selectedPlayer.getSelectionModel().clearSelection();
            players.remove(selectedName);
            selectedPlayer.getItems().remove(selectedName);  
        	players.add(newName);
        	selectedPlayer.getItems().add(newName);
        	updatePlayersList();
        	classicGamesLeaderboard.renamePlayerInLeaderboard(selectedName,newName);
            tournamentsLeaderboard.renamePlayerInLeaderboard(selectedName,newName);
        	newPlayerName.clear();
        }
        else {
        	showErrorMessage("Impossibile modificare nome del giocatore selezionato:", "Esiste già un giocatore con lo stesso nome o il nome è invalido!");
        	newPlayerName.clear();
        }
	}
	
	 private void updatePlayersList() {
        try (PrintWriter pw=new PrintWriter(playersList)){
        	
            for (String name : players) {
                pw.println(name);
            }
        	
        } catch (IOException e) {
        	showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
        	e.printStackTrace();     
    	}
    }
	 
	private void getCurrentAdmin() {	
		try (Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"))){
			
			  while (scan.hasNextLine()) {
		        	String line=scan.nextLine();
		        	adminUsername=line;
		        }
		
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	private void getPlayersNames() {
		players=new ArrayList<String>();
		playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
		try (Scanner scan = new Scanner(playersList)){
			
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
		
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	private void showErrorMessage(String header, String content) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
}
