package application;

import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SuspendedGamesController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private ChoiceBox<String> gameSelection;
	@FXML private Button deleteButton;
	private String adminUsername;
	private File gamesList;
	private ArrayList<String> adminGamesList;
	@FXML private Button homeButton;
	@FXML private Button backButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getCurrentAdmin();
		populateAdminGamesList();
		deleteButton.disableProperty().bind(gameSelection.getSelectionModel().selectedItemProperty().isNull());
		 
	}
	
	public void goToHome(ActionEvent event)  {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			try {
				root = FXMLLoader.load(getClass().getResource("home.fxml"));
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
			root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
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
	
	public void deleteGame(ActionEvent event)  {
		String deletedGame=gameSelection.getSelectionModel().getSelectedItem();
		deleteGameFromGamesDatasFile(deletedGame);
		deleteSerializationFile(deletedGame);
		gameSelection.getSelectionModel().clearSelection();
		gameSelection.getItems().remove(deletedGame);
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
	
	private void populateAdminGamesList() {
		gamesList=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		adminGamesList=new ArrayList<String>();
		try (Scanner scan = new Scanner(gamesList)){
			
			while(scan.hasNextLine()) {
				String line=scan.nextLine();
				String[] splittedLine=line.split(",");
				if(splittedLine[0].equals(adminUsername))
					adminGamesList.add(line);
			}
			gameSelection.getItems().addAll(adminGamesList);
			
		} catch (FileNotFoundException e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
			alert.setContentText("Riprova più tardi!");
			alert.showAndWait();
		}
		
	}
	
	private void deleteGameFromGamesDatasFile(String game) {
	   ArrayList<String> datas=new ArrayList<String>();
	   try ( Scanner scan=new Scanner(gamesList);
			   PrintWriter pw=new PrintWriter(gamesList)){
		  
		   while(scan.hasNextLine()) {
			   String line=scan.nextLine();
			   if(!line.equals(game)) 
				   datas.add(line);
		   }
		  
		   for(String line:datas)
			   pw.println(line);
		 
	   }
	   catch(IOException e) {
		   Alert alert=new Alert(AlertType.ERROR);
		   alert.setHeaderText("Si è verificato un errore:");
		   alert.setContentText("Riprova più tardi!");
		   alert.showAndWait();
	   }
	}
	
	private void deleteSerializationFile(String game) {
	   String[] splittedLine=game.split(",");
	   String gameCode=splittedLine[2];
	   File serializationFile= new File("./Files/ConfigurationFiles/"+gameCode+".ser");
	   if(serializationFile.exists())
		   serializationFile.delete();
	}
}
