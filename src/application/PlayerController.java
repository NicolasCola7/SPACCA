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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlayerController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	private Alert alert;
	@FXML private TextField gameCode;
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	private boolean checkGameCode(String code) throws FileNotFoundException {
		boolean check=true;
		File datas=new File("./Files/GamesDatas.csv");
		Scanner scan = new Scanner(datas);
		while(scan.hasNextLine()) {
			String[] line=scan.nextLine().split(",");
			if(gameCode.getText().equals(line[1])) {
				check=true;
				break;
			}
			else
				check=false;
		}
		return check;
	}
	public void play(ActionEvent event) throws IOException {
		if(checkGameCode(gameCode.getText())==true) {
			//start match 
		 	FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            root = loader.load();

            // Create a new stage
            Stage gameStage = new Stage();
            gameStage.setTitle("Game");
            gameStage.setScene(new Scene(root));
            // Show the new stage
            gameStage.show();
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
