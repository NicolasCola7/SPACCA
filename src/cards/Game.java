package cards;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JLabel;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;


public class Game {
	private Deck deck;
	private CharactersDeck chDeck;
	private HashMap<Integer,Player> players;
	private String gameCode;
	private String[] playersNames;
	private  int nOfPlayers;;
	private String admin;
	private  File datas=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	private Scanner scan;
	private int currentPlayer=0;
	private Alert alert;
	private int turn;
	
	public Game(String code, String admin) {
		gameCode=code;
		this.admin=admin;
		deck=new Deck();
		chDeck=new CharactersDeck();
		this.insertPlayers();
		this.buildPlayersHands();
		turn=0;
	}
	private void buildPlayersHands() {
		players=new HashMap<Integer,Player>();
		for(int i=0;i<playersNames.length;i++) {
			players.put(i,new Player(playersNames[i],chDeck.getCharacter()));
			players.get(i).getHand().addAll(deck.drawHand());
		}
	}
	private void insertPlayers() {
		try {
			scan=new Scanner(datas);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				if(line[2].equals(gameCode)) {
					nOfPlayers=Integer.parseInt(line[3]);
					playersNames=new String[nOfPlayers];
					for(int i=0;i<nOfPlayers;i++) {
						playersNames[i]=line[3+i];
					}
				}
				else
					continue;
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	private void eliminatePlayer(int i) {
		players.remove(i);
	}
	public boolean isGameOver() {
		if (players.size()==1)
			return true;
		else 
			return false;
	}
	public Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}
	public Player getPreviousPlayer() {
		int index=currentPlayer-1;
		if (index<0)
			return players.get(nOfPlayers-1);
		else
			return players.get(index);
		
	}
	
	public Card getPlayerSelectedCard(int player,int selectedCard) {
		return players.get(player).getHand().get(selectedCard);
	}
	public boolean checkPlayerTurn(Player p) {
		if(players.get(currentPlayer)!=p)
			return false;
		else
			return true;
	}
	public void submitCard(ActionCard c, int currentPlayer, int targetPlayer,int selectedCard) { // per action cards
		Player p=players.get(currentPlayer);
		if (this.checkPlayerTurn(p)) {
			
			switch(c.getName()) {
				case "AttackCard":{
					AttackCard ac=new AttackCard();
					if(ac.onUse(p, players.get(targetPlayer), deck)) { 
						alert=new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Messaggio informativo");
						alert.setHeaderText(null);
						alert.setContentText("Attacco eseguito con successo.");
						alert.showAndWait();
					}
					else {
						alert=new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Messaggio informativo");
						alert.setHeaderText(null);
						alert.setContentText("Attacco fallito.");
						alert.showAndWait();
					}
					break;	
				}
				case "SauronEyeCard":{
					SauronEyeCard sec=new SauronEyeCard();
					sec.onUse(players, p, deck);
					break;
				}
				case "GauntletCard":{
					GauntletCard gc=new GauntletCard();
					gc.onUse(p, players.get(targetPlayer), deck);
					break;
				}
				case "BoardingCard":{
					BoardingCard bc=new BoardingCard();
					bc.onUse(p,players.get(targetPlayer) ,selectedCard, deck);
					break;
				}
				case "HealingPotionCard":{
					HealingPotionCard hpc=new HealingPotionCard();
					hpc.onUse(p, p, deck);
					break;
				}
				case "MeteorsRainCard":{
					MeteorsRainCard mrc=new MeteorsRainCard();
					mrc.onUse(p, players.get(targetPlayer), deck);
					break;
				}
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non è il tuo turno");
			alert.showAndWait();
		}
	}
	public void submitCard(StaticCard c, int currentPlayer) {// per static cards
		Player p=players.get(currentPlayer);      
		if (this.checkPlayerTurn(p)) {
			if(p.addToBoard(c)) //controlla se la board è vuota per la rispettiva posizione della carta
				p.getHand().remove(c);
			else {
				Alert alert=new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Hai già una carta statica posizionata!");
				alert.setContentText("Sei sicuro di volerla sostituire?");
				if(alert.showAndWait().get()==ButtonType.OK) {
					deck.addToStockPile(p.removeFromBoard(c)); // con questo metodo controlla di che carta voglio sostituire, e libera la posizione destinata a quest'ultima
					p.addToBoard(c); // ora la board è vuota, aggiungo la carta nella rispettiva posizione
					p.getHand().remove(c);
				}
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non è il tuo turno");
			alert.showAndWait();
		}
	}
	public void submitCard(WeaponCard c, int currentPlayer) { // per weapon cards
		Player p=players.get(currentPlayer);    
		if (this.checkPlayerTurn(p)) {
			if(p.getEquipedWeapon().equals(null)) {
				p.setEquipedWeapon(c);
				p.getHand().remove(c);
			}
			else{
				Alert alert=new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Attenzione!");
				alert.setHeaderText("Hai già un'arma equipaggiata!");
				alert.setContentText("Sei sicuro di volerla sostituire?");
				if(alert.showAndWait().get()==ButtonType.OK) {
					deck.addToStockPile(p.removeEquipedWeapon());
					p.setEquipedWeapon(c);
					p.getHand().remove(c);
				}
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non è il tupo turno");
			alert.showAndWait();
		}
	}
	public void submitCard(EventCard c, int currentPlayer, int targetPlayer) { // per event cards
		Player p=players.get(currentPlayer);
		if (this.checkPlayerTurn(p)) {
			
			switch(c.getName()) {
				case "IdentityTheftCard":{
					IdentityTheftCard itc=new IdentityTheftCard();
					itc.onUse(p,players.get(targetPlayer) ,deck);
					break;
				}
				case "DoomsdayCard":{
					DoomsdayCard dc=new DoomsdayCard();
					dc.onUse(p,players.get(targetPlayer),deck);
					this.eliminatePlayer(targetPlayer);
					break;
				}
				case "MiracleCard":{
					MiracleCard mc=new MiracleCard();
					mc.onUse(p, deck);
					break;
				}
			}
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non è il tupo turno");
			alert.showAndWait();
		}
	}
	public void discardCard(int player,int selectedCard) {
		deck.addToStockPile(this.getPlayerSelectedCard(player, selectedCard));
	}
	public void changeTurn() {
		turn++;
		/*for(Integer i:players.keySet()) {
			players.get(i).decreaseActiveStaticCardTurnDuration();
		}*/
	}
	public void submitPlayer(int player) { //passa la giocata al prossimo giocatore
		Player p=players.get(player);    
		if (this.checkPlayerTurn(p)) {
			if (currentPlayer==nOfPlayers) {
				currentPlayer=0;
				this.changeTurn();
			}
			else
				currentPlayer++;
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Errore");
			alert.setHeaderText("Non è il tuo turno");
			alert.showAndWait();
		}	
	}
	/*
	public boolean respond(int defendingPlayer) {
		Player d=players.get(defendingPlayer);
		
		ArrayList<Card> hand=getPlayerHand(defendingPlayer);
		boolean check=true;
		for(Card c:hand) 
			if(c.getName().equals("Missed")) 
				check= true;
			else if(c.getName().equals("Hologram")) 
				check= (deck.drawAndCheck(d.getCharacter().getSeed())) ? true:false;
			else
				check=false;
		return check;
		
	}*/
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Deck getDeck() {
		return deck;
	}
	
			
}
	