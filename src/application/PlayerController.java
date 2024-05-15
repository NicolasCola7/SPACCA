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
		playButton.disableProperty().bind(gameCode.textProperty().isEmpty());
		
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
	
	public void play(ActionEvent event)  {
	    if (checkGameCode(gameCode.getText()) == true) {
	        try {
				root = FXMLLoader.load(getClass().getResource("Loading.fxml"));
				 stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				 scene = new Scene(root);
				 stage.setScene(scene);
				 stage.show();
			} catch (IOException e) {
				Alert errorAlert=new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Si è verificato un errore:");
				errorAlert.setContentText("Riprova più tardi!");
				errorAlert.showAndWait();
				e.printStackTrace();
			}
	       

	        // Utilizza un task asincrono per caricare la scena del gioco classico o del torneo
	        Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() {
	                try {
	                    Thread.sleep(3000); // attendi 3 secondi per mostrare la scena di loading
	                } catch (InterruptedException e) {
	                	Alert alert=new Alert(AlertType.ERROR);
	        			alert.setHeaderText("Si è verificato un errore:");
	        			alert.setContentText("Riprova più tardi!");
	        			alert.showAndWait();
	                }

	                Platform.runLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        if (gameType.equals("classic")) {
	                           
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
	                                Alert alert=new Alert(AlertType.ERROR);
	                    			alert.setHeaderText("Si è verificato un errore:");
	                    			alert.setContentText("Riprova più tardi!");
	                    			alert.showAndWait();
	                            }
	                        } else {
	                          
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
	                            	Alert alert=new Alert(AlertType.ERROR);
	                    			alert.setHeaderText("Si è verificato un errore:");
	                    			alert.setContentText("Riprova più tardi!");
	                    			alert.showAndWait();
	                            }
	                        }
	                    }
	                });
	                return null;
	            }
	        };

	        // Esegui il task asincrono
	        new Thread(task).start();
	    } else {
	        alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Errore");
	        alert.setHeaderText("Non esiste alcuna partita associata al seguente codice:");
	        alert.setContentText("Inserisci un nuovo codice partita!");
	        alert.showAndWait();
	        gameCode.clear();
	    }
	}
	
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
			Alert alert=new Alert(AlertType.ERROR);
			alert.setHeaderText("Si è verificato un errore:");
			alert.setContentText("Riprova più tardi!");
			alert.showAndWait();	
		}
		return check;
	}
	
}
