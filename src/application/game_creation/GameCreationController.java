package application.game_creation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public abstract class GameCreationController{
	@FXML protected ListView<String> playersSelection;
	@FXML protected CheckBox botCheck;
	@FXML protected TextField gameCode;
	@FXML protected Button confirmButton;
	@FXML protected Button homeButton;
	@FXML protected Button backButton;
	protected ArrayList<String> players;
	protected String adminUsername;
	protected File playersList;
	protected ArrayList<String> gamePlayers;
	protected Scene scene;
	protected Stage stage;
	protected Parent root;

	public abstract void addToGamesDatasFile(); 
	
	protected void populatePlayersList() {
		players=new ArrayList<String>();
		playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
	
		try (Scanner scan = new Scanner(playersList)){
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
			playersSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			playersSelection.getItems().addAll(players);
		} catch (FileNotFoundException e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
			alert.setContentText("Riprova più tardi!");
			alert.showAndWait();
		}
	}
	
	protected void getCurrentAdmin() {	
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
	
	protected boolean gameCodeCheck(String code){
		boolean check=true;
		File file=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		
		if(file.length()!=0) {
			try(Scanner scan=new Scanner(file)) {
				while(scan.hasNextLine()) {
					String[] gameInfos=scan.nextLine().split(",");
					if(gameInfos[2].equals(code)) {
						check=false;
						break;
					}
					else
						check=true;
				}
			} catch (FileNotFoundException e) {
				Alert alert=new Alert(AlertType.ERROR);
				alert.setHeaderText("Si è verificato un errore:");
				alert.setContentText("Riprova più tardi!");
				alert.showAndWait();
			}
		}
		return check;
	}
}
