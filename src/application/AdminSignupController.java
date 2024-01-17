package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

public class AdminSignupController implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private PasswordField psw;
	@FXML private PasswordField pswCheck;
	@FXML private TextField username;
	@FXML private Text errorMsg;
	@FXML private Button registerButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		registerButton.disableProperty().bind(
				Bindings.isEmpty(username.textProperty()).or(
				Bindings.isEmpty(psw.textProperty()).or(
				Bindings.isEmpty(pswCheck.textProperty()))));
		
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
		Scanner scf = new Scanner(new File("./Files/ConfigurationFiles/Login.csv"));
        while (scf.hasNextLine()) {
        	String[] line= scf.nextLine().split(",");
        	if(line[0].equals(username.getText())) {
        		checkUser=false;
        	}
        }
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		if(!psw.getText().equals(pswCheck.getText())) {
			checkPsw=false;
		}
		if(checkUser==true && checkPsw==true) {
			FileWriter fw = new FileWriter("./Files/Login.csv", true);
    		PrintWriter pw = new PrintWriter(fw);
    		pw.print(username.getText()+","+psw.getText());
    		pw.println();
    		pw.close();
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
	
}
