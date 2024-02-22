package cards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Leaderboard {
	private LinkedHashMap<String,Integer> players;
	private File leaderboard;
	
	public Leaderboard(String filepath) {
		leaderboard=new File(filepath);
		players=new LinkedHashMap<String,Integer>();
		getData();
	}
	public void getData() {
		 int score=0;
		 String playersName="";
		try {
			Scanner scan=new Scanner(leaderboard);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				playersName=line[0];
				score=Integer.parseInt(line[1]);
				players.put(playersName,score);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void orderLeaderboard() {
		 // Trasforma la mappa in una lista di voci
        ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<>(players.entrySet());
        // Ordina la lista utilizzando un comparatore personalizzato per i valori (Integer)
        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));
        players.clear();
        for (Map.Entry<String, Integer> entry : entryList) {
            players.put(entry.getKey(), entry.getValue());
        }
        updateLeaderboard();
	}
	private void updateLeaderboard() {
		try {
			PrintWriter pw=new PrintWriter(leaderboard);
			for(String name:players.keySet()) {
				int score=players.get(name);
				pw.println(name+","+score);
			}
			pw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
