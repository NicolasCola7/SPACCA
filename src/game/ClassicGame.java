
package game;
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
import leaderboard.Leaderboard;
import cards.*;
import cards.actions.*;
import cards.statics.*;
import cards.events.*;
import cards.characters.*;
import decks.*;	

public class ClassicGame extends Game {

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
	
	public void buildPlayersHands() {
		for(int i=0;i<nOfPlayers;i++) 
			players.get(i).getHand().addAll(deck.drawHand());
	}
	
	public void insertPlayers() {
		players=new LinkedList<Player>();
		try {
			Scanner scan=new Scanner(datas);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(line[2].equals(gameCode)) {
					nOfPlayers=Integer.parseInt(line[3]);
					playersNames=new ArrayList<String>(nOfPlayers);
					
					for(int i=0;i<nOfPlayers;i++) {
						playersNames.add(line[4+i]);
						
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
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	public void eliminatePlayer(int i) {
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
		String message="";
		boolean isBot=players.get(this.currentPlayer) instanceof Bot;
		switch(submittedActionCard.getName()) {
			case "Attacco":{
				hasAttackedValue=true;
				hasAttacked.set(hasAttackedValue);
				AttackCard ac=new AttackCard();
				ac.onUse(attackingPlayer,targetPlayer, deck);
				if(targetPlayer.getCharacter().getCurrentLife()<=0) {// caso in cui con l'attacco si elimina un giocatore
					this.eliminatePlayer(target);
					message="Hai eliminato "+targetPlayer.getUsername()+".";
				}
				break;	
			}
			case "Occhio Di Sauron":{
				SauronEyeCard sec=new SauronEyeCard();
				sec.onUse(players, attackingPlayer, deck);
				String eliminated="";
				int index=0;
				while(index<players.size()){ // controllo se ho eliminato qualce giocatore utilizzano l'occhio di sauron
					if(players.get(index).getCharacter().getCurrentLife()<=0 && players.get(index).getUsername()!=attackingPlayer.getUsername()) {
						eliminated=(eliminated.length()!=0 ? eliminated+"-"+players.get(index).getUsername()+"\n" : "-"+players.get(index).getUsername()+"\n");
						this.eliminatePlayer(index);
						index=(index==0?0:index-1);
					}
					else
						index++;
				}
				if(eliminated.length()!=0) 
					message="Hai inflitto 20 p.ti danno a tutti i giocatori, e hai eliminato:\n "+eliminated;
				else
					message="Hai inflitto 20 p.ti danno ha tutti i giocatori!";

				break;
			}
			case "Guanto Di Thanos":{
				GauntletCard gc=new GauntletCard();
				Card discarded=gc.onUse(attackingPlayer,targetPlayer, deck);
				targetPlayer.getHand().remove(discarded);
				
				message="La carta '"+discarded.getName()+"' è stata scartata dalla mano di "+targetPlayer.getUsername();
				break;
			}
			case "Arrembaggio":{
				BoardingCard bc=new BoardingCard();
				Card stolen=bc.onUse(attackingPlayer,players.get(target) , deck);
				attackingPlayer.getHand().add(stolen);
				
				message="La carta '"+stolen.getName()+"' è stata rubata dalla mano di "+targetPlayer.getUsername();
				break;
			}
			case "Pozione Curativa":{
				HealingPotionCard hpc=new HealingPotionCard();
				hpc.onUse(attackingPlayer, deck);
				break;
			}
			case "Pioggia Di Meteore":{
				MeteorsRainCard mrc=new MeteorsRainCard();
				mrc.onUse(attackingPlayer, targetPlayer, deck);
				break;
			}
		}
		
		if(!isBot && message.length()>0) {
			alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		}
		
		this.currentPlayer=currentPlayer;
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
		this.currentPlayer=player;
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
		this.currentPlayer=player;
		return check;
	}
	public void submitEventCard(int submittedCard, int player, int target) { // per event cards
		Player currentPlayer=players.get(player);
		Player targetPlayer=players.get(target);
		EventCard submittedEventCard=(EventCard)currentPlayer.getHand().get(submittedCard);
		switch(submittedEventCard.getName()) {
			case "Furto Di Identità":{
				IdentityTheftCard itc=new IdentityTheftCard();
				itc.onUse(currentPlayer,targetPlayer,deck);
				break;
			}
			case "Giorno Del Giudizio":{
				DoomsdayCard dc=new DoomsdayCard();
				dc.onUse(currentPlayer,targetPlayer,deck);
				if(!(players.get(this.currentPlayer) instanceof Bot)) {
					alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Il giorno del giudizio è arrivato per "+targetPlayer.getUsername());
					alert.showAndWait();
				}
				this.eliminatePlayer(target);
				break;
			}
			case "Miracolo":{
				MiracleCard mc=new MiracleCard();
				mc.onUse(currentPlayer, deck);
				break;
			}
		}
		this.currentPlayer=player;
		currentPlayer.getHand().remove(submittedCard);
	}
	public Card drawCard(int currentPlayer) {//metodo per pescare la carta e aggiungerla alla mano 
		Card c=deck.drawCard();
		this.currentPlayer=currentPlayer;
		players.get(currentPlayer).getHand().add(c);
		hasDrawedValue=true;
		hasDrawed.set(hasDrawedValue); // hasDrawed diventa true cosi che il giocatore non possa pescare più di una carta per turno in quanto viene disattivato il bottone per pescare
		return c;
	}
	public void discardCard(int currentPlayer,int selectedCard) { //scarta una carta
		deck.addToStockPile(this.getPlayersHand(currentPlayer).remove(selectedCard));
		this.currentPlayer=currentPlayer;
		hasDiscardedValue=true;
		hasDiscarded.set(hasDiscardedValue); //HasDiscarded diventa true in modo che il giocatore corrente non possa scartare più di una carta
	}
}
	