package game;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import application.Leaderboard;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import cards.*;
import decks.*;	
;public abstract class Game implements Serializable{

	protected Deck deck;
	protected CharactersDeck chDeck;
	protected LinkedList<Player> players;
	protected String gameCode;
	protected ArrayList<String> playersNames;
	protected  int nOfPlayers;;
	protected String admin;
	protected  transient File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	protected transient Alert alert;
	protected int turn;
	protected int currentPlayer;
	protected transient SimpleBooleanProperty hasDrawed;
	protected transient SimpleBooleanProperty hasAttacked;
	protected transient SimpleBooleanProperty hasDiscarded;
	protected boolean hasDrawedValue;
	protected boolean hasAttackedValue;
	protected boolean hasDiscardedValue;
	protected GameType gameType;
	protected  Leaderboard leaderboard;
	
	public Game(String code, String admin) {
		gameCode=code;
		this.admin=admin;
		chDeck=new CharactersDeck();
		turn=1;
		currentPlayer=0;
		
	}
	abstract protected void insertPlayers();
	abstract protected void buildPlayersHands();
	abstract public Player getPlayer(int player);
	abstract public void eliminatePlayer(int player);
	abstract public void submitActionCard(int submittedCard, int currentPlayer, int target);
	abstract public boolean submitStaticCard(int submittedCard, int player);
	abstract public boolean submitWeaponCard(int submittedCard, int player);
	abstract public void submitEventCard(int submittedCard, int player, int target);
	
	public void changeTurn() {//cambio turno
		turn++;
	}
	
	public void discardCard(int currentPlayer,int selectedCard) { //scarta una carta
		deck.addToStockPile(this.getCurrentPlayersHand(currentPlayer).remove(selectedCard));
		this.currentPlayer=currentPlayer;
		hasDiscardedValue=true;
		hasDiscarded.set(hasDiscardedValue); //HasDiscarded diventa true in modo che il giocatore corrente non possa scartare più di una carta
	}
	
	public Card drawCard(int currentPlayer) {//metodo per pescare la carta e aggiungerla alla mano 
		Card c=deck.drawCard();
		this.currentPlayer=currentPlayer;
		this.getCurrentPlayersHand(currentPlayer).add(c);
		hasDrawedValue=true;
		hasDrawed.set(hasDrawedValue); // hasDrawed diventa true cosi che il giocatore non possa pescare più di una carta per turno in quanto viene disattivato il bottone per pescare
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
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer=currentPlayer;
	}
	public Leaderboard getLeaderboard() {
		return leaderboard;
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
	public GameType getGameType() {
		return gameType;
	}
	
}
