
package cards;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


public class Game {
	private Deck deck;
	private CharactersDeck chDeck;
	private ArrayList<Player> players;
	private String gameCode;
	private ArrayList<String> playersNames;
	private  int nOfPlayers;;
	private String admin;
	private  File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	private Alert alert;
	private int turn;
	
	public Game(String code, String admin) {
		gameCode=code;
		this.admin=admin;
		deck=new Deck();
		chDeck=new CharactersDeck();
		insertPlayers();
		buildPlayersHands();
		turn=1;
	}
	private void buildPlayersHands() {
		players=new ArrayList<Player>();
		for(int i=0;i<nOfPlayers;i++) {
			players.add(new Player(playersNames.get(i),chDeck.getCharacter()));
			players.get(i).getHand().addAll(deck.drawHand());
		}
	}
	private void insertPlayers() {
		try {
			Scanner scan=new Scanner(datas);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(line[2].equals(gameCode)) {
					
					nOfPlayers=Integer.parseInt(line[3]);
					playersNames=new ArrayList<String>(nOfPlayers);
					for(int i=0;i<nOfPlayers;i++) {
						playersNames.add(line[4+i]);
					}
					break;
				}
				else
					continue;
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	private void eliminatePlayer(int i) {
		players.remove(i);
		playersNames.remove(i);
		nOfPlayers--;
	}
	
	public boolean isGameOver() {
		if (players.size()==1)
			return true;
		else 
			return false;
	}
	
	public Player getPlayer(int player) {
		return players.get(player);
	}

	
	public void submitActionCard(int submittedCard, int currentPlayer, int target) { // per action cards
		Player attackingPlayer=players.get(currentPlayer);
		Player targetPlayer=players.get(target);
		ActionCard submittedActionCard=(ActionCard)attackingPlayer.getHand().get(submittedCard);
		switch(submittedActionCard.getName()) {
			case "AttackCard":{
				AttackCard ac=new AttackCard();
				ac.onUse(attackingPlayer,targetPlayer, deck);
				if(targetPlayer.getCharacter().getCurrentLife()<=0) {// caso in cui con l'attacco si elimina un giocatore
					this.eliminatePlayer(target);
					alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Hai eliminato "+targetPlayer.getUsername()+".");
				}
				if(attackingPlayer.getCharacter().getCurrentLife()<=0) { // caso in cui l'attaccate venga eliminato dal veleno di vedova nera
					this.eliminatePlayer(currentPlayer);
					alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Il veleno di vedova nera ti ha ucciso!Sei stato eliminato da "+targetPlayer.getUsername()+".");
					alert.showAndWait();
				}
				break;	
			}
			case "SauronEyeCard":{
				SauronEyeCard sec=new SauronEyeCard();
				sec.onUse(players, attackingPlayer, deck);
				ArrayList<String> eliminated=new ArrayList<String>();
				for(int i=0;i<players.size();i++) { // controllo se ho eliminato qualce giocatore utilizzano l'occhio di sauron
					if(players.get(i).getCharacter().getCurrentLife()<=0 && players.get(i).getUsername()!=attackingPlayer.getUsername()) {
						eliminated.add(players.get(i).getUsername());
					}
				}
				alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				if(eliminated.size()!=0) 
					alert.setContentText("Hai inflitto 15 p.ti danno ha tutti i giocatori, e hai eliminato "+eliminated.toString());
				else
					alert.setContentText("Hai inflitto 15 p.ti danno ha tutti i giocatori!");
				alert.showAndWait();
				
				break;
			}
			case "GauntletCard":{
				GauntletCard gc=new GauntletCard();
				Card discarded=gc.onUse(attackingPlayer,targetPlayer, deck);
				targetPlayer.getHand().remove(discarded);
				alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				alert.setContentText("La carta '"+discarded.getName()+"' è stata scartata dalla mano di "+targetPlayer.getUsername());
				alert.showAndWait();
				break;
			}
			case "BoardingCard":{
				BoardingCard bc=new BoardingCard();
				Card stolen=bc.onUse(attackingPlayer,players.get(target) , deck);
				attackingPlayer.getHand().add(stolen);
				targetPlayer.getHand().remove(stolen);
				alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				alert.setContentText("La carta '"+stolen.getName()+"' è stata rubata dalla mano di "+targetPlayer.getUsername());
				alert.showAndWait();
				break;
			}
			case "HealingPotionCard":{
				HealingPotionCard hpc=new HealingPotionCard();
				hpc.onUse(attackingPlayer, deck);
				break;
			}
			case "MeteorsRainCard":{
				MeteorsRainCard mrc=new MeteorsRainCard();
				mrc.onUse(attackingPlayer, targetPlayer, deck);
				break;
			}
		}
		attackingPlayer.getHand().remove(submittedCard);	
	}
	public boolean submitStaticCard(int submittedCard, int player) {// per static cards
		Player currentPlayer=players.get(player);  
		StaticCard submitted=(StaticCard)currentPlayer.getHand().get(submittedCard);
		boolean check=true;
		if(currentPlayer.addToBoard(submitted)) { //controlla se la board è vuota per la rispettiva posizione della carta
			currentPlayer.getHand().remove(submittedCard); //rimuove la carta usata dalla mano
			check=true;
		}
		else {
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Hai già una carta statica posizionata!");
			alert.setContentText("Sei sicuro di volerla sostituire?");
			if(alert.showAndWait().get()==ButtonType.OK) {
				deck.addToStockPile(currentPlayer.removeFromBoard(submitted)); // con questo metodo controlla di che carta voglio sostituire, e libera la posizione destinata a quest'ultima
				currentPlayer.addToBoard(submitted); // ora la board è vuota, aggiungo la carta nella rispettiva posizione
				currentPlayer.getHand().remove(submittedCard);//rimuovo la carta utilizzata dalla mano
				check=true;
			}
			else
				check=false;
		}
		return check;
	}
	public boolean submitWeaponCard(int submittedCard, int player) { // per weapon cards
		Player currentPlayer=players.get(player);   
		WeaponCard submittedWeapon=(WeaponCard)currentPlayer.getHand().get(submittedCard);
		boolean check=true;
		if(currentPlayer.getEquipedWeapon()==null) {
			currentPlayer.setEquipedWeapon(submittedWeapon);
			currentPlayer.getHand().remove(submittedCard);
			check=true;
		}
		else{
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Attenzione!");
			alert.setHeaderText("Hai già un'arma equipaggiata!");
			alert.setContentText("Sei sicuro di volerla sostituire?");
			if(alert.showAndWait().get()==ButtonType.OK) {
				deck.addToStockPile(currentPlayer.removeEquipedWeapon());
				currentPlayer.setEquipedWeapon(submittedWeapon);
				currentPlayer.getHand().remove(submittedCard);
				check=true;
			}
			else
				check=false;
		}
		return check;
	}
	public void submitEventCard(int submittedCard, int player, int target) { // per event cards
		Player currentPlayer=players.get(player);
		Player targetPlayer=players.get(target);
		EventCard submittedEventCard=(EventCard)currentPlayer.getHand().get(submittedCard);
		switch(submittedEventCard.getName()) {
			case "IdentityTheftCard":{
				IdentityTheftCard itc=new IdentityTheftCard();
				itc.onUse(currentPlayer,targetPlayer,deck);
				break;
			}
			case "DoomsdayCard":{
				DoomsdayCard dc=new DoomsdayCard();
				dc.onUse(currentPlayer,targetPlayer,deck);
				alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				alert.setContentText("Il giorno del giudizio è arrivato per "+targetPlayer.getUsername());
				alert.showAndWait();
				this.eliminatePlayer(target);
				break;
			}
			case "MiracleCard":{
				MiracleCard mc=new MiracleCard();
				mc.onUse(currentPlayer, deck);
				break;
			}
		}
		currentPlayer.getHand().remove(submittedCard);
	}
	public void discardCard(int currentPlayer,int selectedCard) { //scarta una carta
		deck.addToStockPile(this.getCurrentPlayersHand(currentPlayer).remove(selectedCard));
	}
	public void changeTurn() {//cambio turno
		turn++;
	}
	public Card drawCard(int currentPlayer) {//metodo per pescare la carta e aggiungerla alla mano 
		Card c=deck.drawCard();
		this.getCurrentPlayersHand(currentPlayer).add(c);
		return c;
	}
	public ArrayList<Card> getCurrentPlayersHand(int currentPlayer){// ritorna la mano del gicatore corrente
		return players.get(currentPlayer).getHand();
	}
	public ArrayList<String> getPlayers() {
		return playersNames;
	}
	public int getNOfPlayers() {
		return nOfPlayers;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Deck getDeck() {
		return deck;
	}
	public int getTurn() {
		return turn;
	}
	
			
}
	