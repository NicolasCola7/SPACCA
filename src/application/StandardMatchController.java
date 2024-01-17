/*package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class StandardMatchController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
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
}*/

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StandardMatchController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private ListView<String> playersSelection;
	@FXML private ChoiceBox<Integer> numberOfPlayerSelection;
	@FXML private CheckBox botCheck;
	@FXML private TextField gameCode;
	@FXML private Alert alert;
	@FXML private Button confirmButton;
	private ArrayList<String> players;
	private String adminUsername;
	private File playersList;
	private Scanner scan;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		players=new ArrayList<String>();
		try {
			scan = new Scanner(new File("./Files/ConfigurationFiles/AdminAttuale.csv"));
		
	        while (scan.hasNextLine()) {
	        	String riga=scan.nextLine();
	        	adminUsername=riga;
	        }
	        playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
			scan = new Scanner(playersList);
			while(scan.hasNextLine()) {
				players.add(scan.nextLine());
			}
		
		playersSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		playersSelection.getItems().addAll(players);
		numberOfPlayerSelection.getItems().addAll(2,3,4,5);  
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		confirmButton.disableProperty().bind(
					Bindings.isEmpty(gameCode.textProperty())
				.or(Bindings.isNull(numberOfPlayerSelection.valueProperty()))
				.or(Bindings.isEmpty(playersSelection.getSelectionModel().getSelectedItems())));
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
		root = FXMLLoader.load(getClass().getResource("MatchesManagement.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	private boolean gameCodeCheck(String code){
		boolean check=true;
		try {
			scan=new Scanner(new File("./Files/ConfigurationFiles/GamesDatas.csv"));
		
		while(scan.hasNextLine()) {
			String[] gameInfos=scan.nextLine().split(",");
			if(gameInfos[2].equals(code))
				check=false;
			else
				check=true;
		}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		return check;
	}
	public void confirm(ActionEvent e) {
		int numberOfPlayers=numberOfPlayerSelection.getSelectionModel().getSelectedItem();
		ArrayList<String> gamePlayers=new ArrayList<String>( numberOfPlayers);
		gamePlayers.addAll(playersSelection.getSelectionModel().getSelectedItems());
		if((botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) || (!botCheck.isSelected() && gamePlayers.size()== numberOfPlayers) && gameCodeCheck(gameCode.getText())) {
			for(int i=gamePlayers.size();i< numberOfPlayers;i++)
				gamePlayers.add(i,"bot");
			
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
				alert=new Alert(AlertType.CONFIRMATION);
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
			} catch (IOException e1) {
				System.out.println("File not found");
				e1.printStackTrace();
			}
		}
		else if(!botCheck.isSelected() && gamePlayers.size()!= numberOfPlayers) {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Giocatori selezionati insufficienti o troppi:");
			alert.setContentText("Seleziona "+numberOfPlayers+" giocatori per continuare!");
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
