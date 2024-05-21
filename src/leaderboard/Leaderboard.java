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
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		}
	}
	
	public void orderLeaderboard() {
		//insertion sort
		 for (int i = 1; i < scores.size(); i++) {
	            int score = scores.get(i);
	            String name=names.get(i);
	            int j = i - 1;

	            while (j >= 0 && scores.get(j) < score) {
	                scores.set(j+1,scores.get(j));
	                names.set(j+1, names.get(j));
	                j--;
	            }
	            scores.set(j+1,score);
                names.set(j+1, name);
	        }
        updateLeaderboard();
	}
	
	private void updateLeaderboard() {
		try(PrintWriter pw=new PrintWriter(leaderboardFile)) {
			for(int i=0;i<names.size();i++) {
				pw.println(names.get(i)+","+scores.get(i));
			}
		}catch(IOException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		}
	}
	
	public void deleteFromLeaderboard(String player) {
		if(names.contains(player)) {
			int position=names.indexOf(player);
			names.remove(player);
			scores.remove(position);
			orderLeaderboard();
		}
	}
	
	public void increaseScore(String player) {
		if(names.contains(player)) {
			int position=names.indexOf(player);
			int previousScore=scores.get(position);
			scores.set(position, ++previousScore);
			orderLeaderboard();
		}
	}
	
	public void renamePlayerInLeaderboard(String oldName,String newName) {
		if(names.contains(oldName)) {
			int position=names.indexOf(oldName);
			names.set(position, newName);
			orderLeaderboard();
		}
	}
	
	private void getPlayersNames() {
		playersNames=new ArrayList<String>();
		File playersList=new File("./Files/ConfigurationFiles/"+adminUsername+"ListaGiocatori.csv");
		try {
			Scanner scan = new Scanner(playersList);
			while(scan.hasNextLine()) {
				playersNames.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		}
	}
	
	public void initializeLeaderboardFile() {
		getPlayersNames();
		try (PrintWriter pw=new PrintWriter(leaderboardFile)){
			for(String name:playersNames) {
				pw.println(name+","+0);
			}
		}catch(IOException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();	
		 }
	}
}
