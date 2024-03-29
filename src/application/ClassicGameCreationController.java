
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
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

public class ClassicGameCreationController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private ListView<String> playersSelection;
	@FXML private ChoiceBox<Integer> numberOfPlayerSelection;
	@FXML private CheckBox botCheck;
	@FXML private TextField gameCode;
	@FXML private Button confirmButton;
	@FXML private Button homeButton;
	@FXML private Button backButton;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	private int numberOfPlayers;
	private ArrayList<String> gamePlayers;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
	    populatePlayersList();   
		numberOfPlayerSelection.getItems().addAll(2,3,4,5); 
		Leaderboard classicGamesLeaderboard=new Leaderboard(adminUsername,GameType.CLASSIC);
		
		confirmButton.disableProperty().bind(
				gameCode.textProperty().isEmpty().or(
				numberOfPlayerSelection.valueProperty().isNull().or(
				playersSelection.getSelectionModel().selectedItemProperty().isNull())));
		
		ImageView backImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Back2.png")));
		ImageView homeImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Home2.png")));
		backImg.setFitWidth(backButton.getPrefWidth());
		homeImg.setFitWidth(homeButton.getPrefWidth());
		backImg.setFitHeight(backButton.getPrefHeight());
		homeImg.setFitHeight(homeButton.getPrefHeight());
		homeButton.setGraphic(homeImg);
		backButton.setGraphic(backImg);
		//homeButton.setStyle("-fx-background-color:transparent;");
		//backButton.setStyle("-fx-background-color:transparent;");
		
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
		root = FXMLLoader.load(getClass().getResource("MatchesManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void confirm(ActionEvent e) throws IOException {
		numberOfPlayers=numberOfPlayerSelection.getSelectionModel().getSelectedItem();
		gamePlayers=new ArrayList<String>( numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		if(((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers)) && gameCodeCheck(gameCode.getText())) {
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot"+i);
				addToGamesDatasFile();
				
				Alert alert=new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Partita creata correttamente:");
				alert.setContentText("Vuoi tornare alla home?");
				if(alert.showAndWait().get()==ButtonType.OK) {
					root = FXMLLoader.load(getClass().getResource("home.fxml"));
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
	private void getCurrentAdmin() {	
		try {
			Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"));
			  while (scan.hasNextLine()) {
		        	String line=scan.nextLine();
		        	adminUsername=line;
		        }
			  scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void populatePlayersList() {
		players=new ArrayList<String>();
		playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
	
		try {
			Scanner scan = new Scanner(playersList);
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		playersSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		playersSelection.getItems().addAll(players);
	}
	
	private boolean gameCodeCheck(String code){
		boolean check=true;
		File file=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		
		if(file.length()!=0) {
			try {
				Scanner scan=new Scanner(file);
				while(scan.hasNextLine()) {
					String[] gameInfos=scan.nextLine().split(",");
					if(gameInfos[2].equals(code)) {
						check=false;
						break;
					}
					else
						check=true;
				}
				scan.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return check;
	}
	private void addToGamesDatasFile() {
		try {
			FileWriter writer = new FileWriter(new File("./Files/ConfigurationFiles/GamesDatas.csv"),true);
			PrintWriter pw=new PrintWriter(writer);
			String datas=adminUsername+",classic,"+gameCode.getText()+","+numberOfPlayers+","+gamePlayers.get(0);
			for(int i=1;i< numberOfPlayers;i++)
				datas+=","+gamePlayers.get(i);
			pw.println(datas);
			pw.close();
			gameCode.clear();
			botCheck.setSelected(false);
			playersSelection.getSelectionModel().clearSelection();
			numberOfPlayerSelection.getSelectionModel().clearSelection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
