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
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
		registerButton.disableProperty().bind(adminUsername.textProperty().isEmpty().or(
				psw.textProperty().isEmpty().or(
				confirmPsw.textProperty().isEmpty())));
		
		ImageView homeImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Home2.png")));
		homeImg.setFitWidth(homeButton.getPrefWidth());
		homeImg.setFitHeight(homeButton.getPrefHeight());
		homeButton.setGraphic(homeImg);
	}
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void signup(ActionEvent event)throws IOException {
		boolean checkUser=true;
		boolean checkPsw=true;
		try {
			Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/Login.csv"));
	        while (scan.hasNextLine()) {
	        	String[] line= scan.nextLine().split(",");
	        	if(line[0].equals(adminUsername.getText())) {
	        		checkUser=false;
	        		break;
	        	}
	        }
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		
		if(!psw.getText().equals(confirmPsw.getText())) {
			checkPsw=false;
		}
		
		if(checkUser && checkPsw) {
			addNewAdmin();
    		createPlayersList();
    		updateCurrentAdmin();
    
			root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
			errorMsg.setVisible(false);
		}
		else
			errorMsg.setVisible(true);
			
	}
	private void createPlayersList() {
		try {
			PrintWriter playersList=new PrintWriter("./Files/ConfigurationFiles/"+adminUsername.getText()+"ListaGiocatori.csv");
			playersList.close();
		} catch (FileNotFoundException e) {// 
			e.printStackTrace();
		}
	}
	private void updateCurrentAdmin() {
		PrintWriter actualAdmin;
		try {
			actualAdmin = new PrintWriter("./Files/ConfigurationFiles/AdminAttuale.csv");
			actualAdmin.println(adminUsername.getText());
			actualAdmin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void addNewAdmin() {
		try {
			FileWriter fw = new FileWriter("./Files/ConfigurationFiles/Login.csv", true);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(adminUsername.getText()+","+psw.getText());
			pw.println();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
