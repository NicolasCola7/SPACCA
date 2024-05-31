package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import game.GameType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import leaderboard.Leaderboard;

public class AdminSignupController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private PasswordField psw;
	@FXML private PasswordField confirmPsw;
	@FXML private TextField adminUsername;
	@FXML private Text errorMsg;
	@FXML private Button registerButton;
	@FXML private Button homeButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		registerButton.disableProperty().bind(adminUsername.textProperty().isEmpty().or(	//disable register button 
				psw.textProperty().isEmpty().or(
				confirmPsw.textProperty().isEmpty())));
	}
	
	public void goToHome(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("FXML/home.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
		
	}
	
	//new admin registration
	public void signup(ActionEvent event) {
		boolean checkUser=true;
		boolean checkPsw=true;
		try (Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/Login.csv"))){
			
	        while (scan.hasNextLine()) {
	        	String[] line= scan.nextLine().split(",");
	        	if(line[0].equals(adminUsername.getText())) {
	        		checkUser=false;
	        		break;
	        	}
	        }
		}
		catch(FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
		
		//check if pw e confirm pw are the same
		if(!psw.getText().equals(confirmPsw.getText())) {
			checkPsw=false;
		}
		
		if(checkUser && checkPsw) {
			addNewAdmin();
    		createPlayersList();
    		updateCurrentAdmin();
    		//create the admin leaderboard
    		Leaderboard classicGamesLeaderboard=new Leaderboard(adminUsername.getText(),GameType.CLASSIC);
    		Leaderboard tournamentsLeaderboard=new Leaderboard(adminUsername.getText(),GameType.TOURNAMENT);
    		
			try {
				root = FXMLLoader.load((new File("FXML/Admin.fxml").toURI().toURL()));
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
				scene=new Scene(root);
				stage.setScene(scene);
				stage.show();
				errorMsg.setVisible(false);
			} catch (IOException e) {
				showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
				e.printStackTrace();
			}
			
		}
		else
			errorMsg.setVisible(true);
			
	}
	
	//create players list related to the admin 
	private void createPlayersList() {
		try (PrintWriter playersList=new PrintWriter("./Files/ConfigurationFiles/"+adminUsername.getText()+"ListaGiocatori.csv")){
		
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//update existing admin
	private void updateCurrentAdmin() {
		try (PrintWriter actualAdmin= new PrintWriter("./Files/ConfigurationFiles/AdminAttuale.csv")){
			actualAdmin.println(adminUsername.getText());
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//add new admin in Login file 
	private void addNewAdmin() {
		try (FileWriter fw = new FileWriter("./Files/ConfigurationFiles/Login.csv", true);
				PrintWriter pw = new PrintWriter(fw)){
			
			pw.print(adminUsername.getText()+","+psw.getText());
			pw.println();
			
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	private void showErrorMessage(String header, String content) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
