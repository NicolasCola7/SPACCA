package application;

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
import javafx.stage.Stage;

public class TournamentController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private ListView<String> playersSelection;
	@FXML private CheckBox botCheck;
	@FXML private TextField gameCode;
	private Alert alert;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	private Scanner scan;
	
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
		root = FXMLLoader.load(getClass().getResource("MatchesManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		players=new ArrayList<String>();
		try {
			scan = new Scanner(new File("./Files/AdminAttuale.csv"));
		
	        while (scan.hasNextLine()) {
	        	String riga=scan.nextLine();
	        	adminUsername=riga;
	        }
	        playersList=new File("./Files/"+adminUsername+"ListaGiocatori.csv");
			scan = new Scanner(playersList);
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		playersSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		playersSelection.getItems().addAll(players);
		 }
	private boolean gameCodeCheck(String code) throws FileNotFoundException {
		boolean check=true;
		scan=new Scanner(new File("./Files/GamesDatas.csv"));
		while(scan.hasNextLine()) {
			String[] gameInfos=scan.nextLine().split(",");
			if(gameInfos[1].equals(code)) {
				check=false;
				break;
			}
			else
				check=true;
		}
		return check;
	}
	public void confirm(ActionEvent e) throws IOException {
		final int numberOfPlayers=8;
		ArrayList<String> gamePlayers=new ArrayList<String>( numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		if((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers)) {
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot");
			FileWriter writer=new FileWriter(new File("./Files/GamesDatas.csv"),true);
			PrintWriter pw=new PrintWriter(writer);
			Collections.shuffle(gamePlayers);
			String datas="tournament,"+gameCode.getText()+","+numberOfPlayers+","+gamePlayers.get(0);
			for(int i=1;i< numberOfPlayers;i++)
				datas+=","+gamePlayers.get(i);
			pw.println(datas);
			pw.close();
			gameCode.clear();
			botCheck.setSelected(false);
			playersSelection.getSelectionModel().clearSelection();
		}
		else if(!botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Giocatori selezionati insufficienti o troppi:");
			alert.setContentText("Seleziona "+numberOfPlayers+" giocatori per continuare!");
			alert.showAndWait();
		}
		else if(gameCode.getText().isEmpty()) {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Codice partita non inserito:");
			alert.setContentText("Inserisci il codice partita per continuare!");
			alert.showAndWait();
		}
		else if(gameCodeCheck(gameCode.getText())==false) {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Codice partita già uso:");
			alert.setContentText("Inserisci un nuovo codice partita!");
			alert.showAndWait();
			gameCode.clear();
		}
	}
}