package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginAdminController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private PasswordField psw;
	@FXML private TextField adminUsername;
	@FXML private  Text errorMsg;
	@FXML private Button loginButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 loginButton.disableProperty().bind(Bindings.isEmpty(adminUsername.textProperty()).or(Bindings.isEmpty(psw.textProperty())));

	}
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void login(ActionEvent event) throws IOException {
		try {
			Scanner scf = new Scanner(new File("./Files/ConfigurationFiles/Login.csv"));
	        while (scf.hasNextLine()) {
	        	String[] line= scf.nextLine().split(",");
	        	if(line[0].equals(adminUsername.getText()) && line[1].equals(psw.getText())) {
	        		String username=adminUsername.getText();
	        		PrintWriter actualAdmin=new PrintWriter("./Files/ConfigurationFiles/AdminAttuale.csv");
	        		actualAdmin.println(username);
	        		actualAdmin.close();
					root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
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
	public void signup(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("AdminSignup.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
