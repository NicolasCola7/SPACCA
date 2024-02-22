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

import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewPlayerController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button addButton;
	@FXML private TextField playerUsername;
	@FXML private Label msg;
	private String adminUsername;
	private File playersList;
	private ArrayList<String> players;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
		 addButton.disableProperty().bind(Bindings.isEmpty(playerUsername.textProperty()));
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
		root = FXMLLoader.load(getClass().getResource("PlayersManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void addPlayer(ActionEvent event) throws IOException {
		String name=playerUsername.getText();
        playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
		Scanner scan = new Scanner(playersList);
		players=new ArrayList<String>();
		while(scan.hasNextLine()) {
			String player=scan.nextLine();
			players.add(player);	
		}
        	if(!players.contains(name)) {
        		addToPlayersList();
        		addToClassicGamesLeaderboard(name);
        		addToTournamentsLeaderboard(name);
				msg.setVisible(true);
				msg.setText("Giocatore aggiunto correttamente!");
				msg.setTextFill(Color.GREEN);
				playerUsername.clear();
        	}
        	else {
        		msg.setVisible(true);
        		msg.setText("Giocatore già esistente!");
				msg.setTextFill(Color.RED);
				playerUsername.clear();
        	}
	}
	private void addToPlayersList() {
		try {
			FileWriter fw = new FileWriter(playersList, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(playerUsername.getText());
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
	private void addToClassicGamesLeaderboard(String player) {
		File leaderboard=new File("./Files/ConfigurationFiles/"+adminUsername+"ClassigGamesLeaderboard.csv");
		try {
			FileWriter fw = new FileWriter(leaderboard, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(player+","+0);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void addToTournamentsLeaderboard(String player) {
		File leaderboard=new File("./Files/ConfigurationFiles/"+adminUsername+"TournamentsLeaderboard.csv");
		try {
			FileWriter fw = new FileWriter(leaderboard, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(player+","+0);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
