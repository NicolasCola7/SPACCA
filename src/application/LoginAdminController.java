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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	@FXML private Button homeButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginButton.disableProperty().bind(adminUsername.textProperty().isEmpty().or(psw.textProperty().isEmpty()));
	}
	
	public void goToHome(ActionEvent event)  {
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
	
	public void login(ActionEvent event)  {
		try(Scanner scan = new Scanner(new File("./Files/ConfigurationFiles/Login.csv"));
				PrintWriter actualAdmin=new PrintWriter("./Files/ConfigurationFiles/AdminAttuale.csv")) {
		
	        while (scan.hasNextLine()) {
	        	String[] line= scan.nextLine().split(",");
	        	if(line[0].equals(adminUsername.getText()) && line[1].equals(psw.getText())) {
	        		String username=adminUsername.getText();
	        		actualAdmin.println(username);
	      
					root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
					stage=(Stage)((Node)event.getSource()).getScene().getWindow();
					scene=new Scene(root);
					stage.setScene(scene);
					stage.show();
	        	}
	        	else
	        		errorMsg.setVisible(true);	
	        }
		}catch(FileNotFoundException e) {
			Alert errorAlert=new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Si è verificato un errore:");
			errorAlert.setContentText("Riprova più tardi!");
			errorAlert.showAndWait();
			e.printStackTrace();
		}catch(IOException e) {
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
			alert.setContentText("Riprova più tardi!");
			alert.showAndWait();
			e.printStackTrace();
		}
	}
	
	public void signup(ActionEvent event)  {
		try {
			root = FXMLLoader.load(getClass().getResource("AdminSignup.fxml"));
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
