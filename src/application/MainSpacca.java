package application;
	
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;


public class MainSpacca extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {			
			Parent root =FXMLLoader.load(getClass().getResource("home.fxml"));
		    
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			
		
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.UTILITY);
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
