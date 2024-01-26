package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlayerController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	private Alert alert;
	private GameController GameController;
	@FXML private TextField gameCode;
	@FXML private Button playButton;
	private String adminUsername;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playButton.disableProperty().bind(Bindings.isEmpty(gameCode.textProperty()));
	}
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	private boolean checkGameCode(String code) {
		boolean check=true;
		File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		Scanner scan;
		try {
			scan = new Scanner(datas);
		while(scan.hasNextLine()) {
			String[] line=scan.nextLine().split(",");
			if(gameCode.getText().equals(line[2])) {
				adminUsername=line[0];
				check=true;
				break;
			}
			else
				check=false;
		}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		return check;
	}
	public void play(ActionEvent event)  {
		if(checkGameCode(gameCode.getText())==true) {
			
			//start match 
		 	FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		 	
            try {
				Parent gameScene = loader.load();
				Scene scene=new Scene(gameScene);
				GameController gameController=loader.getController();
	            gameController.setGameCode(gameCode.getText());
	            gameController.setAdminUsername(adminUsername);
	            // Create a new stage
	            Stage gameStage = new Stage();
	            gameStage.setTitle("Game");
	            gameStage.setScene(scene);
	            // Show the new stage
	            gameStage.show();
            } catch (IOException e) {
				System.out.println("File not found");
				e.printStackTrace();
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non esiste alcuna partita associata al seguente codice:");
			alert.setContentText("Inserisci un nuovo codice partita!");
			alert.showAndWait();
			gameCode.clear();
		}	
	}
	
}
