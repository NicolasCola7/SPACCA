package game;

import java.util.ArrayList;

import cards.Card;
import decks.Deck;
import javafx.beans.property.SimpleBooleanProperty;
import leaderboard.Leaderboard;
import player.Player;

public interface GameInterface {
	
	public Card drawCard(int currentPlayer);
	
	public void discardCard(int currentPlayer,int selectedCard);
	
	public void submitEventCard(int submittedCard, int player, int target);
	
	public boolean submitWeaponCard(int submittedCard, int player);
	
	public boolean submitStaticCard(int submittedCard, int player);
	
	public void submitActionCard(int submittedCard, int currentPlayer, int target);

	public void changeTurn();
	
	public void insertPlayers();
	
	public void buildPlayersHands();
	
	public void eliminatePlayer(int player);
	
	public Player getPlayer(int player);
	
	public ArrayList<Card> getPlayersHand(int currentPlayer);
	
	public ArrayList<String> getPlayersNames();
	
	public int getNOfPlayers();
	
	public String getAdmin();
	
	public void setAdmin(String admin);
	
	public Deck getDeck();
		
	public int getTurn();
	
	public int getCurrentPlayer();

	public void setCurrentPlayer(int currentPlayer);
	
	public Leaderboard getLeaderboard() ;
	
	public SimpleBooleanProperty getHasDrawed();
	
	public SimpleBooleanProperty getHasAttacked();
	
	public SimpleBooleanProperty getHasDiscarded();
	
	public void setHasDrawed(boolean value);
	
	public void setHasAttacked(boolean value);
	
	public void setHasDiscarded(boolean value);
	
	public void initializeProperties();
	
	public GameType getGameType();
	
	public String getActionMessage(int currentPlayer);
	
}
