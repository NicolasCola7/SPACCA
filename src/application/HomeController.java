package application;


import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
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
	@FXML
    private AnchorPane pane;

    @FXML
    private ImageView logo;

    @FXML
    private Button playButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button rulesButton;
    
    
    public void initialize() {
        // Definisci le proprietà iniziali e finali per l'animazione del logo
        KeyValue logoInitialTranslateY = new KeyValue(logo.translateYProperty(), -100);
        KeyValue logoFinalTranslateY = new KeyValue(logo.translateYProperty(), 0);
        KeyValue logoInitialOpacity = new KeyValue(logo.opacityProperty(), 0);
        KeyValue logoFinalOpacity = new KeyValue(logo.opacityProperty(), 1);

        // Definisci le proprietà iniziali e finali per l'animazione del pulsante "Gioca Ora"
        KeyValue playButtonInitialTranslateY = new KeyValue(playButton.translateYProperty(), -100);
        KeyValue playButtonFinalTranslateY = new KeyValue(playButton.translateYProperty(), 0);
        KeyValue playButtonInitialOpacity = new KeyValue(playButton.opacityProperty(), 0);
        KeyValue playButtonFinalOpacity = new KeyValue(playButton.opacityProperty(), 1);

        // Definisci le proprietà iniziali e finali per l'animazione del pulsante "Login Admin"
        KeyValue adminButtonInitialTranslateY = new KeyValue(adminButton.translateYProperty(), -100);
        KeyValue adminButtonFinalTranslateY = new KeyValue(adminButton.translateYProperty(), 0);
        KeyValue adminButtonInitialOpacity = new KeyValue(adminButton.opacityProperty(), 0);
        KeyValue adminButtonFinalOpacity = new KeyValue(adminButton.opacityProperty(), 1);

        // Definisci le proprietà iniziali e finali per l'animazione del pulsante "Regolamento"
        KeyValue rulesButtonInitialTranslateY = new KeyValue(rulesButton.translateYProperty(), -100);
        KeyValue rulesButtonFinalTranslateY = new KeyValue(rulesButton.translateYProperty(), 0);
        KeyValue rulesButtonInitialOpacity = new KeyValue(rulesButton.opacityProperty(), 0);
        KeyValue rulesButtonFinalOpacity = new KeyValue(rulesButton.opacityProperty(), 1);

        // Definisci la timeline per l'animazione del logo
        Timeline logoTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, logoInitialTranslateY, logoInitialOpacity),
                new KeyFrame(Duration.seconds(2), logoFinalTranslateY, logoFinalOpacity)
        );

        // Definisci la timeline per l'animazione del pulsante "Gioca Ora"
        Timeline playButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, playButtonInitialTranslateY, playButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), playButtonFinalTranslateY, playButtonFinalOpacity)
        );

        // Definisci la timeline per l'animazione del pulsante "Login Admin"
        Timeline adminButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, adminButtonInitialTranslateY, adminButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), adminButtonFinalTranslateY, adminButtonFinalOpacity)
        );

        // Definisci la timeline per l'animazione del pulsante "Regolamento"
        Timeline rulesButtonTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, rulesButtonInitialTranslateY, rulesButtonInitialOpacity),
                new KeyFrame(Duration.seconds(2), rulesButtonFinalTranslateY, rulesButtonFinalOpacity)
        );

        // Avvia le timeline
        logoTimeline.play();
        playButtonTimeline.play();
        adminButtonTimeline.play();
        rulesButtonTimeline.play();
    }
	
	public void goToAdmin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LoginAdmin.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void goToPlayer(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Player.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void goToRules(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Rules.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}



