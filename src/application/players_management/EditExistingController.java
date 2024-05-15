package application.players_management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import game.GameType;
import javafx.beans.binding.Binding;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
				root = FXMLLoader.load((new File("src/application/home.fxml").toURI().toURL()));
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
				scene=new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				Alert errorAlert=new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Si è verificato un errore:");
				errorAlert.setContentText("Riprova più tardi!");
				errorAlert.showAndWait();
				e.printStackTrace();
			}
			
		}
	}
	public void back(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("src/application/Admin.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			Alert errorAlert=new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Si è verificato un errore:");
			errorAlert.setContentText("Riprova più tardi!");
			errorAlert.showAndWait();
			e.printStackTrace();
		}
	}
	
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
	
	public void renamePlayer(ActionEvent event) {
		String selectedName = selectedPlayer.getSelectionModel().getSelectedItem();
		 if (!players.contains(newPlayerName.getText())) {
		 	selectedPlayer.getSelectionModel().clearSelection();
            players.remove(selectedName);
            selectedPlayer.getItems().remove(selectedName);  
        	players.add(newPlayerName.getText());
        	selectedPlayer.getItems().add(newPlayerName.getText());
        	updatePlayersList();
        	classicGamesLeaderboard.renamePlayerInLeaderboard(selectedName,newPlayerName.getText());
            tournamentsLeaderboard.renamePlayerInLeaderboard(selectedName,newPlayerName.getText());
        	newPlayerName.clear();
        }
        else {
        	alert = new Alert(Alert.AlertType.WARNING);
        	alert.setTitle("Errore");
        	alert.setHeaderText("Impossibile modificare nome del giocatore selezionato:");
        	alert.setContentText("Esiste già un giocatore con lo stesso nome!");
        	alert.showAndWait();
        	newPlayerName.clear();
        }
	}
	
	 private void updatePlayersList() {
        try (PrintWriter pw=new PrintWriter(playersList)){
        	
            for (String name : players) {
                pw.println(name);
            }
        	
        } catch (IOException e) {
        	Alert alert=new Alert(AlertType.ERROR);
        	alert.setHeaderText("Si è verificato un errore:");
     	   	alert.setContentText("Riprova più tardi!");
     	   	alert.showAndWait();        }
    }
	 
	private void getCurrentAdmin() {	
		try (Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"))){
			
			  while (scan.hasNextLine()) {
		        	String line=scan.nextLine();
		        	adminUsername=line;
		        }
		
		} catch (FileNotFoundException e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
    	   	alert.setContentText("Riprova più tardi!");
    	   	alert.showAndWait();
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
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
			alert.setContentText("Riprova più tardi!");
			alert.showAndWait();
		}
	}
	
	
}
