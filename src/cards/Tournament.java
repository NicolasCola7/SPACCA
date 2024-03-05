package cards;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import application.Leaderboard;

public class Tournament implements Serializable{

	private static final long serialVersionUID = 5021983098796754824L;
	private Deck deck;
	private CharactersDeck chDeck;
	private LinkedList<Player> players;
	private String gameCode;
	private ArrayList<String> playersNames;
	private  int nOfPlayers;;
	private String admin;
	private  transient File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	private transient Alert alert;
	private int turn;
	private int currentPlayer;
	private transient SimpleBooleanProperty hasDrawed;
	private transient SimpleBooleanProperty hasAttacked;
	private transient SimpleBooleanProperty hasDiscarded;
	private boolean hasDrawedValue;
	private boolean hasAttackedValue;
	private boolean hasDiscardedValue;
	private GameType gameType;
	private  Leaderboard leaderboard;
	private TournamentPhase tournamentPhase;
	private int gameNumber;
	private ArrayList<Player> actualGamePlayers;
	private ArrayList<String> actualGamePlayersNames;
	private LinkedList<Player> eliminated;
	
	public Tournament(String code, String admin) {
		eliminated=new LinkedList<Player>();
		gameNumber=1;
		tournamentPhase=TournamentPhase.QUARTI;
		gameCode=code;
		this.admin=admin;
		chDeck=new CharactersDeck();
		players=new LinkedList<Player>();
		insertPlayers();
		buildPlayersHands();
		gameType=GameType.TOURNAMENT;
		leaderboard=new Leaderboard(admin,GameType.TOURNAMENT);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Messaggio informativo");
		alert.setHeaderText(null);
		alert.setContentText(tournamentPhase + ", " + gameNumber +"° partita: "+ actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1));
		alert.showAndWait();
	}
	public void buildPlayersHands() {
		actualGamePlayersNames=new ArrayList<String>();
		actualGamePlayers=new ArrayList<Player>();
		hasDrawedValue=false;
		hasDiscardedValue=false;
		hasAttackedValue=false;
		initializeProperties();
		currentPlayer=0;
		turn=1;
		deck=new Deck();
	
		for(int i=(gameNumber-1)*2;i<gameNumber*2;i++) {
			
			if(tournamentPhase.equals(TournamentPhase.QUARTI))
				players.get(i).getHand().addAll(deck.drawHand());
			else {
				players.get(i).resetAll();
				players.get(i).getHand().addAll(deck.drawHand());
			}
			
			actualGamePlayers.add(players.get(i));
			actualGamePlayersNames.add(players.get(i).getUsername());
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
						players.add(new Player(playersNames.get(i),chDeck.getCharacter()));
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
	
	public void eliminatePlayer(int player) {
		eliminated.addLast(actualGamePlayers.remove(player));
		actualGamePlayersNames.remove(player);
	}
	
	public boolean isTournamentGameOver() {
		if (tournamentPhase.equals(TournamentPhase.FINALE) && eliminated.size()==7)
			return true;
		else 
			return false;
	}
	public boolean isActualGameOver() {
		if (actualGamePlayers.size()==1) {
			return true;
		}
		else 
			return false;
	}
	public void switchGame() { //mettere degli alert per quando si cambia fase o partita
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Messaggio informativo");
		alert.setHeaderText(null);
		if (tournamentPhase.equals(TournamentPhase.QUARTI)) {
			if(gameNumber==4) {
				players.removeAll(eliminated);
				alert.setContentText("Quarti di finale terminati, si passa alle semifinali!");
				alert.showAndWait();
				tournamentPhase=TournamentPhase.SEMIFINALI;
				gameNumber=1;
			}
			else 
				gameNumber++;
		}
		
		else if(tournamentPhase.equals(TournamentPhase.SEMIFINALI)){
			if(gameNumber==2) {
				players.removeAll(eliminated);
				alert.setContentText("Semifinali terminate, si passa alla finale!");
				alert.showAndWait();
				tournamentPhase=TournamentPhase.FINALE;
				gameNumber=1;
				}
			else 
				gameNumber++;
		}
		
		buildPlayersHands();
		
		if(tournamentPhase.equals(TournamentPhase.FINALE)) {
			alert.setContentText("FINALE:" + actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1));
			alert.showAndWait();
		}
		else {
			alert.setContentText(tournamentPhase + ", " + gameNumber +"° partita: "+ actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1));
			alert.showAndWait();
		}
				
	}
	
	public Player getPlayer(int player) {
		return actualGamePlayers.get(player);
	}
	
	public void submitActionCard(int submittedCard, int currentPlayer, int target) { // per action cards
		Player attackingPlayer=actualGamePlayers.get(currentPlayer);
		Player targetPlayer=actualGamePlayers.get(target);
		ActionCard submittedActionCard=(ActionCard)attackingPlayer.getHand().get(submittedCard);
		switch(submittedActionCard.getName()) {
			case "AttackCard":{
				hasAttackedValue=true;
				hasAttacked.set(hasAttackedValue);
				AttackCard ac=new AttackCard();
				ac.onUse(attackingPlayer,targetPlayer, deck);
				if(targetPlayer.getCharacter().getCurrentLife()<=0) {// caso in cui con l'attacco si elimina un giocatore
					this.eliminatePlayer(target);
					alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Hai eliminato "+targetPlayer.getUsername()+".");
					alert.showAndWait();
				}
				break;	
			}
			case "SauronEyeCard":{
				SauronEyeCard sec=new SauronEyeCard();
				sec.onUse(actualGamePlayers, attackingPlayer, deck);
				int hit=(currentPlayer==0?1:0);
				
				alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				
				if(this.getPlayer(hit).getCharacter().getCurrentLife()<=0){// controllo se ho eliminato l'avversario  utilizzando l'occhio di sauron
					alert.setContentText("Hai inflitto 20 p.ti danno all'avversario e lo hai eliminato!");
					this.eliminatePlayer(hit);
				}
				else
					alert.setContentText("Hai inflitto 20 p.ti danno all'avversario!");
				
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
		this.currentPlayer=currentPlayer;
		attackingPlayer.getHand().remove(submittedCard);	
	}
	public boolean submitStaticCard(int submittedCard, int player) {// per static cards
		Player currentPlayer=actualGamePlayers.get(player);  
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
		this.currentPlayer=player;
		return check;
	}
	public boolean submitWeaponCard(int submittedCard, int player) { // per weapon cards
		Player currentPlayer=actualGamePlayers.get(player);   
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
		this.currentPlayer=player;
		return check;
	}
	public void submitEventCard(int submittedCard, int player, int target) { // per event cards
		Player currentPlayer=actualGamePlayers.get(player);
		Player targetPlayer=actualGamePlayers.get(target);
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
		this.currentPlayer=player;
		currentPlayer.getHand().remove(submittedCard);
	}
	public void discardCard(int currentPlayer,int selectedCard) { //scarta una carta
		deck.addToStockPile(this.getCurrentPlayersHand(currentPlayer).remove(selectedCard));
		this.currentPlayer=currentPlayer;
		hasDiscardedValue=true;
		hasDiscarded.set(hasDiscardedValue); //HasDiscarded diventa true in modo che il giocatore corrente non possa scartare più di una carta
	}
	public void changeTurn() {//cambio turno
		turn++;
	}
	public Card drawCard(int currentPlayer) {//metodo per pescare la carta e aggiungerla alla mano 
		Card c=deck.drawCard();
		this.currentPlayer=currentPlayer;
		this.getCurrentPlayersHand(currentPlayer).add(c);
		hasDrawedValue=true;
		hasDrawed.set(hasDrawedValue); // hasDrawed diventa true cosi che il giocatore non possa pescare più di una carta per turno in quanto viene disattivato il bottone per pescare
		return c;
	}
	
	public SimpleBooleanProperty getHasDrawed() {
		return hasDrawed;
	}
	public SimpleBooleanProperty getHasAttacked() {
		return hasAttacked;
	}
	public SimpleBooleanProperty getHasDiscarded() {
		return hasDiscarded;
	}
	public void setHasDrawed(boolean value) {
		hasDrawedValue=value;
		hasDrawed.set(hasDrawedValue);
	}
	public void setHasAttacked(boolean value) {
		hasAttackedValue=value;
		hasAttacked.set(hasAttackedValue);
	}
	public void setHasDiscarded(boolean value) {
		hasDiscardedValue=value;
		hasDiscarded.set(hasDiscardedValue);
	}
	public void initializeProperties() {
		hasDrawed=new SimpleBooleanProperty(hasDrawedValue);
		hasDiscarded=new SimpleBooleanProperty(hasDiscardedValue);
		hasAttacked=new SimpleBooleanProperty(hasAttackedValue);
	}
	public ArrayList<Card> getCurrentPlayersHand(int currentPlayer){// ritorna la mano del gicatore corrente
		return actualGamePlayers.get(currentPlayer).getHand();
	}
	public ArrayList<String> getPlayers() {
		return playersNames;
	}
	public ArrayList<Player> getActualGamePlayers() {
		return actualGamePlayers;
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
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer=currentPlayer;
	}
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
	public GameType getGameType() {
		return gameType;
	}
	public TournamentPhase getTournamentPhase() {
		return tournamentPhase;
	}
	public void setTournamentPhase(TournamentPhase tournamentPhase) {
		this.tournamentPhase = tournamentPhase;
	}
	public int getGameNumber() {
		return gameNumber;
	}
	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}
	public ArrayList<String> getActualGamePlayersNames() {
		return actualGamePlayersNames;
	}
	public void setActualGamePlayersNames(ArrayList<String> actualGamePlayersNames) {
		this.actualGamePlayersNames = actualGamePlayersNames;
	}
	public ArrayList<String> getLatestTwoEliminated() {
		ArrayList<String> latestTwo=new ArrayList<String>();
		Player first=eliminated.removeLast();
		Player second=eliminated.removeLast();
		actualGamePlayers.add(second);
		actualGamePlayers.add(first);
		latestTwo.add(second.getUsername());
		latestTwo.add(first.getUsername());
		actualGamePlayersNames.addAll(latestTwo);
		return latestTwo;
	}
}
	