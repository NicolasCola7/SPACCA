import java.io.File;
import java.util.Random;


public class Game {
	
	private String gameName;
	private int nPlayers;
	private Player[] players;
	private Admin admin;
	private File backup;
	private int rounds;
	private int turns;
	private Deck mainDeck;
	private Deck secondDeck;
	private String code;
	private Player[] leaderboard;
	
	
	public Game(String n,int nP,Admin a) {
		gameName = n;
		nPlayers = nP;
		admin = a;
		rounds = 0;
		turns = 0;
		backup = new File(gameName);
		
	}
	//dameges all others players except player a by a specified amount of damage 
	public boolean damageOthers(Player a, int damage) {
		boolean temp = false;
		for(int i = 0;i<players.length;i++) {
			if(players[i]!=a && a!=null) {
				double newLife = players[i].getCharacter().getLife() - damage;
				players[i].setLife(newLife);
				temp =true;
			}
			
		}if(a==null)
			temp= false;
		return temp;
	}
	public boolean Draw(Player a) {
		if(mainDeck.Draw()!=null) {
			a.addCardTH(mainDeck.Draw());
			return true;
		}
		else return false;

	}
	
	
	//getters and setters
	public int getRounds() {
		return rounds;
	}
	public void setRounds(int newR) {
		rounds = newR;
	}
	public void setTurns(int newT) {
		turns = newT;
	}
	public int getTurns() {
		return turns;
	}
	public Player[] getPlayers() {
		return players;
	}
	public int getNPlayers() {
		return nPlayers;
	}
}
