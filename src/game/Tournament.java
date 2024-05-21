package game;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import application.game_playing.TournamentBracketController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import leaderboard.Leaderboard;
import player.Bot;
import player.Player;
import cards.*;
import cards.actions.*;
import cards.statics.*;
import cards.events.*;
import decks.*;	
public class Tournament extends Game  {

	private static final long serialVersionUID = 8046043559234131541L;
	private TournamentPhase tournamentPhase;
	private int gameNumber;
	private ArrayList<Player> actualGamePlayers;
	private ArrayList<String> actualGamePlayersNames;
	private ArrayList<String>  semifinalists;
	private ArrayList<String>  finalists;
	private LinkedList<Player> eliminated;
	private String currentGameMessage;
	
	//init a tournament
	public Tournament(String code, String admin){
		super(code,admin);
		semifinalists=new ArrayList<String>(4);
		finalists=new ArrayList<String>(2);
		actionMessages=new ArrayList<String>();
		actualGamePlayersNames=new ArrayList<String>(2);
		actualGamePlayers=new ArrayList<Player>(2);
		eliminated=new LinkedList<Player>();
		deck=new Deck();
		gameNumber=1;
		tournamentPhase=TournamentPhase.QUARTI;
		gameType=GameType.TOURNAMENT;
		leaderboard=new Leaderboard(admin,gameType);
		
		insertPlayers();
		buildPlayersHands();
	
		currentGameMessage=tournamentPhase + ", " + gameNumber +"° partita: "+ actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1);;
	}
	
	@Override
	//build every player hand 
	public void buildPlayersHands() {
		actionMessages.clear();
		actualGamePlayersNames.clear();
		actualGamePlayers.clear();
		
		hasDrawedValue=false;
		hasDiscardedValue=false;
		hasAttackedValue=false;
		initializeProperties();
		currentPlayer=0;
		turn=1;
		deck.reset();
	
		for(int i=(gameNumber-1)*2;i<gameNumber*2;i++) {
			
			if(!tournamentPhase.equals(TournamentPhase.QUARTI))
				players.get(i).resetAll();
			
			players.get(i).getHand().addAll(deck.drawHand());
			
			actualGamePlayers.add(players.get(i));
			actualGamePlayersNames.add(players.get(i).getUsername());
			actionMessages.add("");
		}
	}
	
	@Override
	//insert players into the game
	public void insertPlayers() {
		players=new LinkedList<Player>();
		try (Scanner scan=new Scanner(datas)){

			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(line[2].equals(gameCode)) {
					nOfPlayers=Integer.parseInt(line[3]);
					playersNames=new ArrayList<String>(nOfPlayers);
					for(int i=0;i<nOfPlayers;i++) {
						playersNames.add(line[4+i]);

						if( line[4+i].length()>3 && line[4+i].substring(0, 3).equals("bot"))
							players.add(new Bot(playersNames.get(i),chDeck.getCharacter()));
						else
							players.add(new Player(playersNames.get(i),chDeck.getCharacter()));	
					}
					break;
				}
				else
					continue;
			}
		}catch(FileNotFoundException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		}	
	}
	
	
	@Override
	public void eliminatePlayer(int player) {
		eliminated.addLast(actualGamePlayers.remove(player));
		actualGamePlayersNames.remove(player);
	}
	
	public boolean isTournamentGameOver() {
		if (tournamentPhase.equals(TournamentPhase.FINALE) && eliminated.size()==7) {
			return true;
		}
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
	
	//switch game status from quarter to semifinals
	public void switchGame() {
		//from quarter to semifinals
		if (tournamentPhase.equals(TournamentPhase.QUARTI)) {
			semifinalists.add(actualGamePlayersNames.get(0));
			if(gameNumber==4) {
				players.removeAll(eliminated);
				InformationAlert.display("Messaggio informativo", "Quarti di finale terminati, si passa alle semifinali!");
				tournamentPhase=TournamentPhase.SEMIFINALI;
				gameNumber=1;
			}
			else {
				gameNumber++;
			}
		}
		
		//from semifinals to finals
		else if(tournamentPhase.equals(TournamentPhase.SEMIFINALI)){
			finalists.add(actualGamePlayersNames.get(0));
			if(gameNumber==2) {
				players.removeAll(eliminated);
				InformationAlert.display("Messaggio informativo","Semifinali terminate, si passa alla finale!"); 
				tournamentPhase=TournamentPhase.FINALE;
				gameNumber=1;
			}
			else 
				gameNumber++;
		}
		
		buildPlayersHands();
		
		if(tournamentPhase.equals(TournamentPhase.FINALE)) 
			currentGameMessage="FINALE:" + actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1);
		
		else 
			currentGameMessage=tournamentPhase + ", " + gameNumber +"° partita: "+ actualGamePlayersNames.get(0) + " VS " + actualGamePlayersNames.get(1);	
		
		InformationAlert.display("Messaggio informativo",currentGameMessage);
	}
	
	
	@Override
	public Player getPlayer(int player) {
		return actualGamePlayers.get(player);
	}
	
	
	@Override
	//action card management
	public void submitActionCard(int submittedCard, int currentPlayer, int target) {
		Player attackingPlayer=actualGamePlayers.get(currentPlayer);
		Player targetPlayer=actualGamePlayers.get(target);
		ActionCard submittedActionCard=(ActionCard)attackingPlayer.getHand().get(submittedCard);
		String message="";
		boolean isBot=(actualGamePlayers.get(this.currentPlayer) instanceof Bot);
		switch(submittedActionCard.getName()) {
			case "Attacco":{
				hasAttackedValue=true;
				hasAttacked.set(hasAttackedValue);
				AttackCard.onUse(attackingPlayer,targetPlayer, deck);
				if(targetPlayer.getCharacter().getCurrentLife()<=0) {//if using card a player is eliminate
					this.eliminatePlayer(target);
					message="Hai eliminato "+targetPlayer.getUsername()+".";
				}
				actionMessages.set(target, actionMessages.get(target)+ "-Sei stato attaccato da "+ attackingPlayer.getUsername()+"\n");
				break;	
			}
			case "Occhio Di Sauron":{
				SauronEyeCard.onUse(actualGamePlayers, attackingPlayer, deck);
				int hit=1-currentPlayer;

				if(this.getPlayer(hit).getCharacter().getCurrentLife()<=0){// check if a player is eliminate
					message="Hai inflitto 20 p.ti danno all'avversario e lo hai eliminato!";
					this.eliminatePlayer(hit);
				}
				else
					message="Hai inflitto 20 p.ti danno all'avversario!";
				
				actionMessages.set(hit, actionMessages.get(hit)+ "-Hai subito 20 p.ti danno dall'Occhio di Sauron \n");
				break;
			}
			case "Guanto Di Thanos":{
				Card discarded=GauntletCard.onUse(attackingPlayer,targetPlayer, deck);
				targetPlayer.getHand().remove(discarded);
				message="La carta '"+discarded.getName()+"' è stata scartata dalla mano di "+targetPlayer.getUsername();
				actionMessages.set(target,actionMessages.get(target)+"-"+attackingPlayer.getUsername()+" ti ha scartato la carta "+discarded.getName()+"\n");
				break;
			}
			case "Arrembaggio":{
				Card stolen=BoardingCard.onUse(attackingPlayer,players.get(target) , deck);
				attackingPlayer.getHand().add(stolen);
				targetPlayer.getHand().remove(stolen);
				message="La carta '"+stolen.getName()+"' è stata rubata dalla mano di "+targetPlayer.getUsername();
				actionMessages.set(target,actionMessages.get(target)+"-"+attackingPlayer.getUsername()+" ti ha rubato la carta "+stolen.getName()+"\n");
				break;
			}
			case "Pozione Curativa":{
				HealingPotionCard.onUse(attackingPlayer, deck);
				break;
			}
			case "Pioggia Di Meteore":{
				MeteorsRainCard.onUse(attackingPlayer, targetPlayer, deck);
				actionMessages.set(target,actionMessages.get(target)+  "-La tua board è stata distrutta  da "+ attackingPlayer.getUsername()+"\n");
				break;
			}
		}
		if(!isBot && message.length()>0) {
			InformationAlert.display("Messaggio informativo",message);
		}
		this.currentPlayer=currentPlayer;
		attackingPlayer.getHand().remove(submittedCard);	
	}
	
	
	@Override
	//static card management
	public boolean submitStaticCard(int submittedCard, int player) {
		Player currentPlayer=actualGamePlayers.get(player);  
		StaticCard submitted=(StaticCard)currentPlayer.getHand().get(submittedCard);
		boolean check=true;
		if(currentPlayer.addToBoard(submitted)) { //check if board is empty for place the card in correct position
			currentPlayer.getHand().remove(submittedCard); //remove used card from hand
			check=true;
		}
		else {
			Alert alert=new Alert(AlertType.CONFIRMATION);
			alert.setGraphic(null);
			alert.getDialogPane().getStyleClass().add("game-alert");
			alert.getDialogPane().getScene().getStylesheets().add("./application/GameAlertStyle.css");
			alert.setTitle("Conferma");
			alert.setHeaderText("Hai già una carta statica posizionata!");
			alert.setContentText("Sei sicuro di volerla sostituire?");
			if(alert.showAndWait().get()==ButtonType.OK) {
				deck.addToStockPile(currentPlayer.removeFromBoard(submitted)); // check card to replace and release position
				currentPlayer.addToBoard(submitted); // board is empty, add card
				currentPlayer.getHand().remove(submittedCard);
				check=true;
			}
			else
				check=false;
		}
		this.currentPlayer=player;
		return check;
	}
	
	
	@Override
	//weapon card management
	public boolean submitWeaponCard(int submittedCard, int player) { 
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
			alert.setGraphic(null);
			alert.getDialogPane().getStyleClass().add("game-alert");
			alert.getDialogPane().getScene().getStylesheets().add("./application/GameAlertStyle.css");
			alert.setTitle("Conferma");
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
	
	@Override
	//event card management
	public void submitEventCard(int submittedCard, int player, int target) { 
		Player currentPlayer=actualGamePlayers.get(player);
		Player targetPlayer=actualGamePlayers.get(target);
		EventCard submittedEventCard=(EventCard)currentPlayer.getHand().get(submittedCard);
		switch(submittedEventCard.getName()) {
			case "Furto Di Identità":{
				IdentityTheftCard.onUse(currentPlayer,targetPlayer,deck);
				actionMessages.set(target, actionMessages.get(target)+ "-Il tuo personaggio è stato scambiato con quello di "+ currentPlayer.getUsername()+"\n");
				break;
			}
			case "Giorno Del Giudizio":{
				DoomsdayCard.onUse(currentPlayer,targetPlayer,deck);
				if(!(actualGamePlayers.get(this.currentPlayer) instanceof Bot)) {
					InformationAlert.display("Messaggio informativo","Il giorno del giudizio è arrivato per "+targetPlayer.getUsername());
				}
				this.eliminatePlayer(target);
				break;
			}
			case "Miracolo":{
				MiracleCard.onUse(currentPlayer, deck);
				break;
			}
		}
		this.currentPlayer=player;
		currentPlayer.getHand().remove(submittedCard);
	}
	
	
	@Override
	//draw card and add to hand
	public Card drawCard(int currentPlayer) {
		Card c=deck.drawCard();
		this.currentPlayer=currentPlayer;
		actualGamePlayers.get(currentPlayer).getHand().add(c);
		hasDrawedValue=true;
		hasDrawed.set(hasDrawedValue);// hasDrawed true allows to disable button and avoid to draw only one time
		return c;
	}
	
	
	@Override
	public void discardCard(int currentPlayer,int selectedCard) { 
		deck.addToStockPile(actualGamePlayers.get(currentPlayer).getHand().remove(selectedCard));
		this.currentPlayer=currentPlayer;
		hasDiscardedValue=true;
		hasDiscarded.set(hasDiscardedValue); 
	}
	
		
	public ArrayList<Player> getActualGamePlayers() {
		return actualGamePlayers;
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
	
	public String getCurrentGameMessage() {
		return currentGameMessage;
	}
	
	public  ArrayList<String> getSemifinalists(){
		return semifinalists;
	}
	
	public  ArrayList<String> getFinalists(){
		return finalists;
	}
}
	