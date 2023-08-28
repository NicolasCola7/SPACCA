package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginAmministratoreController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	@FXML
	private PasswordField psw;
	@FXML
	private TextField username;
	@FXML
	private  Text errorMsg;
	
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void login(ActionEvent event) throws IOException {
		try {
			Scanner scf = new Scanner(new File("C:\\Users\\utente\\eclipse-workspace\\Prova\\Files\\Login.csv"));
	        while (scf.hasNextLine()) {
	        	String[] riga = scf.nextLine().split(",");
	        	if(riga[0].equals(username.getText()) && riga[1].equals(psw.getText())) {
					root = FXMLLoader.load(getClass().getResource("Amministratore.fxml"));
					stage=(Stage)((Node)event.getSource()).getScene().getWindow();
					scene=new Scene(root);
					stage.setScene(scene);
					stage.show();
	        	}
	        	else
	        		errorMsg.setVisible(true);	
	        }
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	public void registrati(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("RegistrazioneAmministratore.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
