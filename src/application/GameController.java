package application;
import cards.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class GameController implements Initializable{
	private String gameCode;
	private String  adminUsername;
	private Game game;
	@FXML private Label codeLabel;
	@FXML private Label adminLabel;
	public void setGameCode(String code) {
		gameCode=code;
	}
	public void setAdminUsername(String name) {
		adminUsername=name;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			Game game=new Game(gameCode,adminUsername);
	    });  
		
	}
}