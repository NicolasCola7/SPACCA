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
import javafx.stage.Stage;

public class EditExistingController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	@FXML private ChoiceBox<String> selectedPlayer;
	@FXML private TextField newPlayerName;
	@FXML private Alert alert;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	private Scanner scan;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		players=new ArrayList<String>();
		try {
			scan = new Scanner(new File("./Files/AdminAttuale.csv"));
		
        while (scan.hasNextLine()) {
        	String line=scan.nextLine();
        	adminUsername=line;
        }
        playersList=new File("./Files/"+adminUsername+"ListaGiocatori.csv");
		scan = new Scanner(playersList);
		while(scan.hasNextLine()) {
			players.add(scan.nextLine());
		}
		selectedPlayer.getItems().addAll(players);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
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
			 if (selectedName != null) {
				 	selectedPlayer.getSelectionModel().clearSelection();
		            players.remove(selectedName);
		            selectedPlayer.getItems().remove(selectedName);
		            saveNamesToFile();
		            
		        }
		}
	}
	 private void saveNamesToFile() {
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
	public void renamePlayer(ActionEvent event) {
		String selectedName = selectedPlayer.getSelectionModel().getSelectedItem();
		 if (selectedName != null && !players.contains(newPlayerName.getText())) {
			 	selectedPlayer.getSelectionModel().clearSelection();
	            players.remove(selectedName);
	            selectedPlayer.getItems().remove(selectedName);  
            	players.add(newPlayerName.getText());
            	selectedPlayer.getItems().add(newPlayerName.getText());
            	saveNamesToFile();
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
	
	
}
