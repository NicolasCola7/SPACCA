package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

public class NewPlayerController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	@FXML
	private TextField playerUsername;
	@FXML
	private Label msg;
	private String nomeFile;
	private String adminUsername;
	private File playersList;
	private ArrayList<String> players;
	
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
		
		Scanner scan = new Scanner(new File("./Files/AdminAttuale.csv"));
        while (scan.hasNextLine()) {
        	String line=scan.nextLine();
        	adminUsername=line;
        }
        playersList=new File("./Files/"+adminUsername+"ListaGiocatori.csv");
		scan = new Scanner(playersList);
		players=new ArrayList<String>();
		while(scan.hasNextLine()) {
			String player=scan.nextLine();
			players.add(player);	
		}
        	if(!players.contains(playerUsername.getText())) {
        		FileWriter fw = new FileWriter(playersList, true);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(playerUsername.getText());
				pw.close();
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
}
