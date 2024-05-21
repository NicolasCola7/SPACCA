
package game;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
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

public class ClassicGame extends Game {
	private static final long serialVersionUID = 5043888774594965422L;
	
	//init a classic game
	public ClassicGame(String code, String admin) {
		super(code,admin);
		deck=new Deck();
		gameType=GameType.CLASSIC;
		leaderboard=new Leaderboard(admin,gameType);
		insertPlayers();
		buildPlayersHands();
		hasDrawedValue=false;
		hasDiscardedValue=false;
		hasAttackedValue=false;
		initializeProperties();
	}
	
	@Override
	public void buildPlayersHands() {
		for(int i=0;i<nOfPlayers;i++) 
			players.get(i).getHand().addAll(deck.drawHand());
	}
	
	@Override
	//insert players into the game
	public void insertPlayers() {
		actionMessages=new ArrayList<String>();
		players=new LinkedList<Player>();
		try (Scanner scan=new Scanner(datas)){
			
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(line[2].equals(gameCode)) {
					nOfPlayers=Integer.parseInt(line[3]);
					playersNames=new ArrayList<String>(nOfPlayers);
					
					for(int i=0;i<nOfPlayers;i++) {
						playersNames.add(line[4+i]);
						actionMessages.add("");
						if(line[4+i].equals("bot"+i))
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
	public void eliminatePlayer(int i) {
		players.remove(i);
		playersNames.remove(i);
		actionMessages.remove(i);
		nOfPlayers--;
	}
	
	public boolean isGameOver() {
		if (players.size()==1)
			return true;
		else 
			return false;
	}
	
	@Override
	public Player getPlayer(int player) {
		return players.get(player);
	}
	
	@Override
	//action card management
	public void submitActionCard(int submittedCard, int currentPlayer, int target) { 
		Player attackingPlayer=players.get(currentPlayer);
		Player targetPlayer=players.get(target);
		ActionCard submittedActionCard=(ActionCard)attackingPlayer.getHand().get(submittedCard);
		String message="";
		boolean isBot=players.get(this.currentPlayer) instanceof Bot;
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
				SauronEyeCard.onUse(players, attackingPlayer, deck);
				String eliminated="";
				int index=0;
				while(index<players.size()){ // check if a player is eliminate
					if(players.get(index).getCharacter().getCurrentLife()<=0 && players.get(index).getUsername()!=attackingPlayer.getUsername()) {
						eliminated=(eliminated.length()!=0 ? eliminated+"-"+players.get(index).getUsername()+"\n" : "-"+players.get(index).getUsername()+"\n");
						this.eliminatePlayer(index);
						index=(index==0?0:index-1);
					}
					else {
						if(players.get(index).getUsername()!=attackingPlayer.getUsername()) 
							actionMessages.set(index, actionMessages.get(index)+ "-Hai subito 20 p.ti danno dall'Occhio di Sauron \n");
						index++;
					}
				}
				if(eliminated.length()!=0) 
					message="Hai inflitto 20 p.ti danno a tutti i giocatori, e hai eliminato:\n "+eliminated;
				else
					message="Hai inflitto 20 p.ti danno ha tutti i giocatori!";
			
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
		Player currentPlayer=players.get(player);  
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
		Player currentPlayer=players.get(player);
		Player targetPlayer=players.get(target);
		EventCard submittedEventCard=(EventCard)currentPlayer.getHand().get(submittedCard);
		switch(submittedEventCard.getName()) {
			case "Furto Di Identità":{
				IdentityTheftCard.onUse(currentPlayer,targetPlayer,deck);
				actionMessages.set(target, actionMessages.get(target)+ "-Il tuo personaggio è stato scambiato con quello di "+ currentPlayer.getUsername()+"\n");
				break;
			}
			case "Giorno Del Giudizio":{
				DoomsdayCard.onUse(currentPlayer,targetPlayer,deck);
				if(!(players.get(this.currentPlayer) instanceof Bot)) {
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
		players.get(currentPlayer).getHand().add(c);
		hasDrawedValue=true;
		hasDrawed.set(hasDrawedValue); // hasDrawed true allows to disable button and avoid to draw only one time
		return c;
	}
	
	@Override
	public void discardCard(int currentPlayer,int selectedCard) {
		deck.addToStockPile(this.getPlayersHand(currentPlayer).remove(selectedCard));
		this.currentPlayer=currentPlayer;
		hasDiscardedValue=true;
		hasDiscarded.set(hasDiscardedValue); 
	}
}
	