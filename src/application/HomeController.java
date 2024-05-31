package application;


import java.io.File;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomeController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private AnchorPane pane;

    @FXML  private ImageView logo;

    @FXML private Button playButton;

    @FXML private Button adminButton;

    @FXML  private Button rulesButton;
    
    
    public void initialize() {
    	
    	//create drop-down animation
    	
    	// define init and final proprieties for every component
  
        KeyValue logoInitialTranslateY = new KeyValue(logo.translateYProperty(), -100);
        KeyValue logoFinalTranslateY = new KeyValue(logo.translateYProperty(), 0);
        KeyValue logoInitialOpacity = new KeyValue(logo.opacityProperty(), 0);
        KeyValue logoFinalOpacity = new KeyValue(logo.opacityProperty(), 1);

        KeyValue playButtonInitialTranslateY = new KeyValue(playButton.translateYProperty(), -100);
        KeyValue playButtonFinalTranslateY = new KeyValue(playButton.translateYProperty(), 0);
        KeyValue playButtonInitialOpacity = new KeyValue(playButton.opacityProperty(), 0);
        KeyValue playButtonFinalOpacity = new KeyValue(playButton.opacityProperty(), 1);

        KeyValue adminButtonInitialTranslateY = new KeyValue(adminButton.translateYProperty(), -100);
        KeyValue adminButtonFinalTranslateY = new KeyValue(adminButton.translateYProperty(), 0);
        KeyValue adminButtonInitialOpacity = new KeyValue(adminButton.opacityProperty(), 0);
        KeyValue adminButtonFinalOpacity = new KeyValue(adminButton.opacityProperty(), 1);

        KeyValue rulesButtonInitialTranslateY = new KeyValue(rulesButton.translateYProperty(), -100);
        KeyValue rulesButtonFinalTranslateY = new KeyValue(rulesButton.translateYProperty(), 0);
        KeyValue rulesButtonInitialOpacity = new KeyValue(rulesButton.opacityProperty(), 0);
        KeyValue rulesButtonFinalOpacity = new KeyValue(rulesButton.opacityProperty(), 1);
        
        //define timeline for every component

        Timeline logoTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, logoInitialTranslateY, logoInitialOpacity),
                new KeyFrame(Duration.seconds(2), logoFinalTranslateY, logoFinalOpacity)
        );

        Timeline playButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, playButtonInitialTranslateY, playButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), playButtonFinalTranslateY, playButtonFinalOpacity)
        );

        Timeline adminButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, adminButtonInitialTranslateY, adminButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), adminButtonFinalTranslateY, adminButtonFinalOpacity)
        );

        Timeline rulesButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, rulesButtonInitialTranslateY, rulesButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), rulesButtonFinalTranslateY, rulesButtonFinalOpacity)
        );

        //play timeline
        logoTimeline.play();
        playButtonTimeline.play();
        adminButtonTimeline.play();
        rulesButtonTimeline.play();
    }
	
	public void goToAdmin(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("FXML/LoginAdmin.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
		
	}
	
	public void goToPlayer(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("FXML/Player.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	public void goToRules(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("FXML/Rules.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
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



