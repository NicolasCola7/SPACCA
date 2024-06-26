package game;



import java.io.File;
import java.net.MalformedURLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

//set alert UI

public class InformationAlert {
	
	public static void display(String title, String message) {
		Stage window=new Stage();
		window.setTitle(title);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(350);
		window.setResizable(false);
		
		Text label=new Text(message);
		label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		
		Button closeButton=new Button("OK");
		closeButton.setStyle("-fx-background-color:white;-fx-border-color:black;");
		closeButton.setOnAction(e->window.close());
		closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-border-color: orange;"));
		closeButton.setOnMouseExited(e -> {
	         if (!closeButton.isFocused()) {
	        	 closeButton.setStyle("-fx-border-color: black;-fx-background-color:white;");
	         }
	     });
		closeButton.setOnMousePressed(event -> {
			closeButton.setStyle("-fx-background-color:orange;"); 
	     });
		closeButton.setOnMouseReleased(event -> {closeButton.setStyle("-fx-background-color:white;-fx-border-color:black;");});
		
		HBox buttonBox=new HBox(10);
		buttonBox.getChildren().add(closeButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(8,8,8,8));

		
		VBox layout=new VBox(10);
		layout.getChildren().addAll(label,buttonBox);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(8,12,0,8));
        
		Scene scene=new Scene(layout);
		File css =new File("FXML/SceneStyle.css");
		try {
			scene.getStylesheets().add(css.toURI().toURL().toExternalForm());
		} catch (MalformedURLException e1) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
			 e1.printStackTrace();
		}
		window.setScene(scene);
		window.showAndWait();
	}
	

}
