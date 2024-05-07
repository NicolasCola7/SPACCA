package application;
import javafx.animation.RotateTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class LoadingController {
	private Scene scene;
	private Stage stage;
	private Parent root;

    @FXML
    private ImageView loadingImage;

    public void initialize() {
        RotateTransition rotateAnimation = new RotateTransition(Duration.seconds(3), loadingImage);
        rotateAnimation.setByAngle(360);
        rotateAnimation.setCycleCount(RotateTransition.INDEFINITE);
        rotateAnimation.play();
    }
    
    public void goToPlay(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Player.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}