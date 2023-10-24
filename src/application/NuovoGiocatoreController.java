package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NuovoGiocatoreController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	@FXML
	private TextField usernameGiocatore;
	@FXML
	private Label msg;
	private String nomeFile;
	private String usernameAmministratore;
	private File listaGiocatori;
	private ArrayList<String> giocatori;
	
	public void goToHome(ActionEvent event) throws IOException {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per effettuare il logout!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			root = FXMLLoader.load(getClass().getResource("home.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
	public void indietro(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("GestioneGiocatori.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void aggiungiGiocatore(ActionEvent event) throws IOException {
		
		Scanner scan = new Scanner(new File("C:\\Users\\utente\\eclipse-workspace\\Prova\\Files\\AdminAttuale.csv"));
        while (scan.hasNextLine()) {
        	String riga=scan.nextLine();
        	usernameAmministratore=riga;
        }
        listaGiocatori=new File("C:\\Users\\utente\\eclipse-workspace\\Prova\\Files\\"+usernameAmministratore+"ListaGiocatori.csv");
		scan = new Scanner(listaGiocatori);
		giocatori=new ArrayList<String>();
		while(scan.hasNextLine()) {
			String giocatore=scan.nextLine();
			giocatori.add(giocatore);	
		}
        	if(!giocatori.contains(usernameGiocatore.getText())) {
        		FileWriter fw = new FileWriter(listaGiocatori, true);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(usernameGiocatore.getText());
				pw.close();
				msg.setVisible(true);
				msg.setText("Giocatore aggiunto correttamente!");
				msg.setTextFill(Color.GREEN);
				usernameGiocatore.clear();
        	}
        	else {
        		msg.setVisible(true);
        		msg.setText("Giocatore già esistente!");
				msg.setTextFill(Color.RED);
				usernameGiocatore.clear();
        	}
        
	}
}
