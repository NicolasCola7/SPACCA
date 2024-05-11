package game;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import leaderboard.Leaderboard;
import player.Player;
import javafx.beans.property.SimpleBooleanProperty;
import cards.*;
import decks.*;	
	
public abstract class Game implements Serializable, GameInterface{

		private static final long serialVersionUID = -1894678914308683663L;
		protected Deck deck;
		protected CharactersDeck chDeck;
		protected LinkedList<Player> players;
		protected String gameCode;
		protected ArrayList<String> playersNames;
		protected  int nOfPlayers;;
		protected String admin;
		protected  transient File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
		protected int turn;
		protected int currentPlayer;
		protected transient SimpleBooleanProperty hasDrawed;
		protected transient SimpleBooleanProperty hasAttacked;
		protected transient SimpleBooleanProperty hasDiscarded;
		protected transient SimpleBooleanProperty isFirstTurn;
		protected boolean hasDrawedValue;
		protected boolean hasAttackedValue;
		protected boolean hasDiscardedValue;
		protected boolean isFirstTurnValue;
		protected GameType gameType;
		protected  Leaderboard leaderboard;
		protected  ArrayList<String> actionMessages;

	public Game(String code, String admin) {
		gameCode=code;
		this.admin=admin;
		chDeck=new CharactersDeck();
		turn=1;
		currentPlayer=0;
	}
	
	@Override
	abstract public void insertPlayers();
	
	@Override
	abstract public void buildPlayersHands();
	
	@Override
	abstract public Player getPlayer(int player);
	
	@Override
	abstract public void eliminatePlayer(int player);
	
	@Override
	abstract public void submitActionCard(int submittedCard, int currentPlayer, int target);
	
	@Override
	abstract public boolean submitStaticCard(int submittedCard, int player);
	
	@Override
	abstract public boolean submitWeaponCard(int submittedCard, int player);
	
	@Override
	abstract public void submitEventCard(int submittedCard, int player, int target);
	
	@Override
	abstract public Card drawCard(int currentPlayer);
	
	@Override
	abstract public void discardCard(int currentPlayer,int cardIndex);
	
	@Override
	public void changeTurn() {//cambio turno
		turn++;
	}
	
	@Override
	public ArrayList<Card> getPlayersHand(int currentPlayer){// ritorna la mano del gicatore corrente
		return players.get(currentPlayer).getHand();
	}
	
	@Override
	public ArrayList<String> getPlayersNames() {
		return playersNames;
	}
	
	@Override
	public int getNOfPlayers() {
		return nOfPlayers;
	}
	
	@Override
	public String getAdmin() {
		return admin;
	}
	
	@Override
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	@Override
	public Deck getDeck() {
		return deck;
	}
	
	@Override
	public int getTurn() {
		return turn;
	}
	
	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer=currentPlayer;
	}
	
	@Override
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
	
	@Override
	public SimpleBooleanProperty getHasDrawed() {
		return hasDrawed;
	}
	
	@Override
	public SimpleBooleanProperty getHasAttacked() {
		return hasAttacked;
	}
	
	@Override
	public SimpleBooleanProperty getHasDiscarded() {
		return hasDiscarded;
	}
	
	@Override
	public void setHasDrawed(boolean value) {
		hasDrawedValue=value;
		hasDrawed.set(hasDrawedValue);
	}
	
	@Override
	public void setHasAttacked(boolean value) {
		hasAttackedValue=value;
		hasAttacked.set(hasAttackedValue);
	}
	
	@Override
	public void setHasDiscarded(boolean value) {
		hasDiscardedValue=value;
		hasDiscarded.set(hasDiscardedValue);
	}
	
	@Override
	public void initializeProperties() {
		hasDrawed=new SimpleBooleanProperty(hasDrawedValue);
		hasDiscarded=new SimpleBooleanProperty(hasDiscardedValue);
		hasAttacked=new SimpleBooleanProperty(hasAttackedValue);
	}
	
	@Override
	public GameType getGameType() {
		return gameType;
	}
	
	@Override
	public String getActionMessage(int currentPlayer) {
		String message=actionMessages.get(currentPlayer);
		actionMessages.set(currentPlayer, "");
		return message;
	}
}
