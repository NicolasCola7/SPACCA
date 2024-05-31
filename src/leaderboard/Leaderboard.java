package leaderboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import game.GameType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Leaderboard implements Serializable{
	
	private static final long serialVersionUID = 3946677509468116035L;
	private LinkedList<String> names;
	private LinkedList<Integer> scores;
	private File leaderboardFile;
	private String adminUsername;
	private ArrayList<String>playersNames;
	
	//init leaderboard 
	public Leaderboard(String adminUsername,GameType gameType) {
		names=new LinkedList<String>();
		scores=new LinkedList<Integer>();
		this.adminUsername=adminUsername;
		leaderboardFile=new File("./Files/ConfigurationFiles/"+adminUsername+gameType.toString()+"sLeaderboard.csv");
		if(leaderboardFile.length()==0)
			initializeLeaderboardFile();
		getData();
	}
	
	//get players info from file
	public void getData() {
		int score=0;
		try(Scanner scan=new Scanner(leaderboardFile)) {
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				names.addLast(line[0]);
				score=Integer.parseInt(line[1]);
				scores.addLast(score);
			}
		}catch(IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//order players in leaderbord
	public void orderLeaderboard() {
		//insertion sort
		 for (int i = 1; i < scores.size(); i++) {
			   // Memorizza l'elemento corrente di scores e il corrispondente elemento di names
			    int score = scores.get(i);
			    String name = names.get(i);
			    
			    // Inizializza j come l'indice immediatamente precedente a i
			    int j = i - 1;

			    // Ciclo interno che confronta l'elemento corrente con quelli precedenti nella lista ordinata
			    // Sposta gli elementi che sono minori dell'elemento corrente una posizione avanti per fare spazio
			    while (j >= 0 && scores.get(j) < score) {
			        // Sposta l'elemento di scores in avanti
			        scores.set(j + 1, scores.get(j));
			        // Sposta l'elemento corrispondente di names in avanti
			        names.set(j + 1, names.get(j));
			        // Decrementa j per continuare a controllare gli elementi precedenti
			        j--;
			    }
			    
			    // Posiziona l'elemento corrente (score) nella posizione corretta nella lista ordinata
			    scores.set(j + 1, score);
			    // Posiziona il nome corrispondente nella posizione corretta nella lista ordinata
			    names.set(j + 1, name);
			}
        updateLeaderboard();
	}
	
	//update leaderboard file
	private void updateLeaderboard() {
		try(PrintWriter pw=new PrintWriter(leaderboardFile)) {
			for(int i=0;i<names.size();i++) {
				pw.println(names.get(i)+","+scores.get(i));
			}
		}catch(IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//delete a player from leaderbord 
	public void deleteFromLeaderboard(String player) {
		if(names.contains(player)) {
			int position=names.indexOf(player);
			names.remove(player);
			scores.remove(position);
			orderLeaderboard();
		}
	}
	
	//increase the score of a player
	public void increaseScore(String player) {
		if(names.contains(player)) {
			int position=names.indexOf(player);
			int previousScore=scores.get(position);
			scores.set(position, ++previousScore);
			orderLeaderboard();
		}
	}
	
	//rename a player in leaderboard
	public void renamePlayerInLeaderboard(String oldName,String newName) {
		if(names.contains(oldName)) {
			int position=names.indexOf(oldName);
			names.set(position, newName);
			orderLeaderboard();
		}
	}
	
	//getting players' names from admin's player list
	private void getPlayersNames() {
		playersNames=new ArrayList<String>();
		File playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
		try (Scanner scan = new Scanner(playersList)){
			
			while(scan.hasNextLine()) {
				playersNames.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
	}
	
	//initialize leaderboard file
	public void initializeLeaderboardFile() {
		getPlayersNames();
		try (PrintWriter pw=new PrintWriter(leaderboardFile)){
			for(String name:playersNames) {
				pw.println(name+","+0);
			}
		}catch(IOException e) {
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
