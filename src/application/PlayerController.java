package application;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import application.game_playing.ClassicGameController;
import application.game_playing.TournamentController;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.concurrent.Task;


public class PlayerController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	private Alert alert;
	private String gameType;
	@FXML private TextField gameCode;
	@FXML private Button playButton;
	private String adminUsername;
	@FXML private Button homeButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playButton.disableProperty().bind(gameCode.textProperty().isEmpty());	//check if gamecode textField is empty
		
	}
	
	public void goToHome(ActionEvent event)  {
		try {
			root = FXMLLoader.load(getClass().getResource("home.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//play a new or a suspended game
	public void play(ActionEvent event)  {
	    if (checkGameCode(gameCode.getText()) == true) {
	        try {
	        	//load loading animation
				root = FXMLLoader.load(getClass().getResource("Loading.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
				e.printStackTrace();
			}
	       

	     // use an async task for load game after animation
	        Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() {
	                try {
	                    Thread.sleep(3000); // attendi 3 secondi per mostrare la scena di loading
	                } catch (InterruptedException e) {
	                	showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
	        			e.printStackTrace();
	                }

	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        if (gameType.equals("classic")) {
	                        	//load classic game
	                            try {
	                                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassicGame.fxml"));
	                                Parent classicGameScene = loader.load();
	                                Scene scene = new Scene(classicGameScene);
	                                ClassicGameController classicGameController = loader.getController();
	                                classicGameController.setGameCode(gameCode.getText());
	                                classicGameController.setAdminUsername(adminUsername);
	                                stage.setScene(scene);
	                                stage.show();
	                            } catch (IOException e) {
	                            	// error message to user
	                            	showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
	                    			e.printStackTrace();
	                            }
	                        } else {
	                        	//load tournament game
	                            try {
	                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Tournament.fxml"));
	                                Parent tournamentScene = loader.load();
	                                Scene scene = new Scene(tournamentScene);
	                                TournamentController tournamentController = loader.getController();
	                                tournamentController.setGameCode(gameCode.getText());
	                                tournamentController.setAdminUsername(adminUsername);
	                                stage.setScene(scene);
	                                stage.show();
	                            } catch (IOException e) {
	                            	// error message to user
	                            	showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
	                    			e.printStackTrace();
	                            }
	                        }
	                    }
	                });
	                return null;
	            }
	        };

	        // Run async tasks
	        new Thread(task).start();
	    } else {
	    	showErrorMessage("Non esiste alcuna partita associata al seguente codice:", "Inserisci un nuovo codice partita!");
	        gameCode.clear();
	    }
	}
	
	//check if admin username and gameType are the same as in the file 
	private boolean checkGameCode(String code) {
		boolean check=true;
		File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		
		try (Scanner scan = new Scanner(datas)){
			
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(gameCode.getText().equals(line[2])) {
					adminUsername=line[0];
					gameType=line[1];
					check=true;
					break;
				}
				else
					check=false;
			}
			
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
		return check;
	}
	
	private void showErrorMessage(String header, String content) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
