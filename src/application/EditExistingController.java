package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

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
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EditExistingController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button deleteButton;
	@FXML private Button modifyButton;
	@FXML private ChoiceBox<String> selectedPlayer;
	@FXML private TextField newPlayerName;
	@FXML private Alert alert;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
		getPlayersNames();
		Leaderboard classicGamesLeaderboard=new Leaderboard("./Files/ConfigurationFiles/"+adminUsername+"ClassicGamesLeaderboard.csv");
		Leaderboard tournamentsLeaderboard=new Leaderboard("./Files/ConfigurationFiles/"+adminUsername+"TournamentsLeaderboard.csv");
		
		selectedPlayer.getItems().addAll(players);
		deleteButton.disableProperty().bind(selectedPlayer.valueProperty().isNull());
		modifyButton.disableProperty().bind(selectedPlayer.valueProperty().isNull().or(newPlayerName.textProperty().isEmpty()));

	}
	public void goToHome(ActionEvent event) throws IOException {
		alert=new Alert(AlertType.CONFIRMATION);
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
		root = FXMLLoader.load(getClass().getResource("PlayersManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void deletePlayer(ActionEvent event) throws FileNotFoundException {
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
        try {
        	PrintWriter pw=new PrintWriter(playersList);
            for (String name : players) {
                pw.println(name);
            }
        	pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private void getCurrentAdmin() {	
		try {
			Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"));
			  while (scan.hasNextLine()) {
		        	String line=scan.nextLine();
		        	adminUsername=line;
		        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void getPlayersNames() {
		players=new ArrayList<String>();
		playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
		try {
			Scanner scan = new Scanner(playersList);
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
