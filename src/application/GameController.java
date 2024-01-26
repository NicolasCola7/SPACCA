/*TODO
 * chiarire gli attributi precisione e fortuna nei personaggi e relativi metodi
 * controllare quando viene eliminato un giocatore o attaccando, o con la carta occhio di suron oppure con il veleno, e vedere come adattare il currentPlayer(es se attacco e vengo eliminato dal veleno, elimino anche l'altro giocatore oppure no
 * gestire nella classe game e GameGontroller la carta statica HorcruxCard
 * pausa partita e salvataggio
 * leaderboard
 * gestire fine partita(ritorno alla home e assegnazione di punteggi per la leaderbord)
 * implementare il bot
 * torneo
 * grafica
 */
package application;
import cards.*;
import cards.Character;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


public class GameController implements Initializable{
	private String gameCode;
	private String  adminUsername;
	private Game game;
	private int currentPlayer;
	private int nOfPlayers;
	private SimpleBooleanProperty hasDrawed;
	private SimpleBooleanProperty hasAttacked;
	private SimpleBooleanProperty hasDiscarded;
	private SimpleBooleanProperty isSelected;
	private SimpleBooleanProperty isSelectedAttackCard;
	
	@FXML private Label codeLabel;
	@FXML private Label adminLabel;
	@FXML private Label playerUsernameLabel;
	@FXML private Label turnLabel;
	@FXML private Button drawCardButton;
	@FXML private Button submitCardButton;
	@FXML private Button discardCardButton;
	@FXML private Button submitPlayerButton;
	@FXML private Button characterInfosButton;
	@FXML private Button playersInfosButton;
	@FXML private Button boardButton;
	@FXML private HBox cardsBox;
	@FXML private VBox infoBox;
	
	private ToggleGroup group;
	private final Insets MARGIN =new Insets(10, 2, 10, 2); // sono le spaziature tra le carte
	private ArrayList<ToggleButton> currentPlayerHand;
	private ArrayList<String> players;
	
	public void setGameCode(String code) { // metodo che viene chiamato dal playerController per settare il gameCode
		gameCode=code;
	}
	public void setAdminUsername(String name) { //metodo che viene chiamato dal playerController per settare l'adminUsername
		adminUsername=name;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // inizializza la partita 
		
		Platform.runLater(() -> { // metodo da capire meglio, serve a ritardare le istruzioni al suo interno dato che quando si passano dati da un altro controller (PlayerController in questo caso)  il metodo initialize viene eseguito prima dei metodi utilizzati nel controller che passa i dati,in guesto caso setGameCode() e setAdminUsername()
			game=new Game(gameCode,adminUsername);
			currentPlayer=0;
			initializeCardsBox(currentPlayer);
	    });  
	}
	private void initializeCardsBox(int currentPlayer) { // inizializza la UI del giocatore corrente
		currentPlayerHand=new ArrayList<ToggleButton>();
		nOfPlayers=game.getNOfPlayers();
		players=game.getPlayers();
		turnLabel.setText(game.getTurn()+"° turno");
		playerUsernameLabel.setText("Username:"+game.getPlayer(currentPlayer).getUsername());
		group=new ToggleGroup();
		for(int i=0;i<game.getPlayer(currentPlayer).getHand().size();i++) {
			Card card=game.getPlayer(currentPlayer).getHand().get(i);
			ToggleButton btn=new ToggleButton(card.getName());
			addToCardsBox(btn);
		}
		setBindings();
		System.out.println(group.selectedToggleProperty().getName());
	}
	private void setBindings() { // serve a fare in modo che i bottoni vengano disattivati e attivati  in determinate situazioni
		hasDrawed=new SimpleBooleanProperty(false);
		hasAttacked=new SimpleBooleanProperty(false);
		hasDiscarded=new SimpleBooleanProperty(false);
		isSelected=new SimpleBooleanProperty(false);
		isSelectedAttackCard=new SimpleBooleanProperty(false);
		
		isSelected.bind(group.selectedToggleProperty().isNull());// isSelected diventa true  è selezionata una carta
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) 
	                isSelectedAttackCard.bind(((ToggleButton)group.getSelectedToggle()).textProperty().isEqualTo("AttackCard"));// isSelectedAttackCard diventa true se è selezionata la carta attacco
	            else 
	            	isSelectedAttackCard.unbind();
	            
	        });
		
    	drawCardButton.disableProperty().bind(hasDrawed); //disattiva il bottone per pescare se ha già pescato
		discardCardButton.disableProperty().bind(hasDiscarded.or(isSelected)); //disattiva il bottone per scartare se il giocatore ha già scartato una carta o non ne ha selezionata alcuna
		//submitPlayerButton.disableProperty().bind(hasDiscarded.not()); //disattiva il bottone per passare se il giocatore non ha scartato una carta
		submitCardButton.disableProperty().bind(isSelected.or(hasAttacked.and(isSelectedAttackCard)));//disattiva il bottone per giocare una carta se non c'è alcuna carta selezionata, oppure se il giocatore ha già attaccato e la carta selezionata è una carta attacco
	}
	
	public void seeBoard(ActionEvent event)throws IOException{ // permette di visualizzare le carte presenti nella board del giocatore corrrente
		StaticCard[] board=game.getPlayer(currentPlayer).getBoard();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informazione");
		alert.setHeaderText(null);
		String staticCard1=(board[0]==null?"vuoto":board[0].getName());
		String staticCard2=(board[1]==null?"vuoto":board[1].getName());
		alert.setContentText("Pos1:"+staticCard1+"\n"+
							 "Pos2:"+staticCard2);
		alert.showAndWait();	
	}
	
	public void seeCharacterInfos(ActionEvent event) throws IOException{ //permette di vedere le informazioni relative al personaggio del giocatore corrente
		Character character=game.getPlayer(currentPlayer).getCharacter();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informazione");
		alert.setHeaderText(null);
		alert.setContentText("Personaggio:"+character.getName()+"\n"+
							 "Seme:"+character.getSeed()+"\n"+
							 "Attacco:"+character.getAttack()+"\n"+
							 "Arma equipaggiata:"+(game.getPlayer(currentPlayer).getEquipedWeapon()==null?"nessuna":game.getPlayer(currentPlayer).getEquipedWeapon().getName())+"\n"+
							 "Potenza d'attacco:"+game.getPlayer(currentPlayer).getAttackPower()+"\n"+
							 "Vita:"+character.getCurrentLife()+"\n"+
							 "Precisione:"+character.getCurrentPrecision()+"\n"+
							 "Fortuna:"+character.getCurrentLuck()+"\n");
		alert.showAndWait();
	}
	
	public void seePlayersInfos(ActionEvent event)throws IOException{ //serve a vedere informazioni (limitate) degli altri giocatori
		ArrayList<String> toSee=new ArrayList<String>();
		toSee.addAll(players);
		toSee.remove(currentPlayer);
		ChoiceDialog<String> dialog = new ChoiceDialog<>(toSee.get(0),toSee);
		dialog.setTitle("Selezione");
        dialog.setHeaderText("Seleziona un giocatore:");
        Optional<String> result = dialog.showAndWait();
        
        if(result.isPresent() || result.get()!=null ) {
        	Player player=game.getPlayer(players.indexOf(dialog.getSelectedItem()));
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Informazione");
			alert.setHeaderText(null);
			alert.setContentText("Nome:"+player.getUsername()+"\n"+
								"Personaggio:"+player.getCharacter().getName()+"\n"+
								"Vita rimanente:"+player.getCharacter().getCurrentLife());
			alert.showAndWait();
        }
	}
	
	public void drawCard(ActionEvent event) throws IOException { //per pescare una carta 
		Card drawedCard=game.drawCard(currentPlayer);
		ToggleButton btn=new ToggleButton(drawedCard.getName());
		addToCardsBox(btn); // aggiunge la carta pescata alla UI del giocatore corrente
		hasDrawed.set(true); // hasDrawed diventa true cosi che il giocatore non possa pescare più di una carta per turno in quanto viene disattivato il bottone per pescare
	}
	
	public void discardCard(ActionEvent event)throws IOException{ //per scartare una carta
		ToggleButton btn=(ToggleButton) group.getSelectedToggle();
		game.discardCard(currentPlayer,currentPlayerHand.indexOf(btn));
		removeFromCardsBox(btn);// rimuove la carta scartata dalla UI del giocatore corrente
		hasDiscarded.set(true); //HasDiscarde diventa false in odo che il giocatore corrente non possa scartare più di una carta
	}

	public void submitCard(ActionEvent event)throws IOException{ // per giocare una carta
		ToggleButton btn=(ToggleButton)group.getSelectedToggle(); //ottengo il bottone selezionato
		Card submittedCard=game.getCurrentPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)); // ottengo la carta nella mano del giocatore corrispondente all'indice del bottone
		int submittedCardIndex=currentPlayerHand.indexOf(btn); // ottengo l'indice della carta nella mano del giocatore 
		ArrayList<String> toAttack=new ArrayList<String>(); // lista dei giocatori che possono essere attaccati
		toAttack.addAll(players);
		toAttack.remove(currentPlayer);
		
		if(submittedCard instanceof ActionCard ) {
			
			if(submittedCard instanceof HealingPotionCard|| submittedCard instanceof SauronEyeCard ) { // in questo caso nel metodo passo 2 volte currentPlayer perchè per queste 2 carte non serve passare il target Player
				game.submitActionCard(submittedCardIndex, currentPlayer,currentPlayer);
				 removeFromCardsBox(btn);
			}
				
			else {
				ChoiceDialog<String> dialog = new ChoiceDialog<>(toAttack.get(0),toAttack);
				dialog.setTitle("Selezione");
		        dialog.setHeaderText("Seleziona un giocatore:");
		        Optional<String> result = dialog.showAndWait();
		        int targetPlayer=players.indexOf(dialog.getSelectedItem());
		        if(result.isPresent() || result.get()!=null ) { //il metodo submitActionCard viene chiamato solo se viene selezionato un giocatore da attaccare
		        	game.submitActionCard(submittedCardIndex, currentPlayer,targetPlayer);
		        	 removeFromCardsBox(btn);
		 			if(submittedCard instanceof AttackCard ) { // se era una carta attacco hasAttacked diventa true in modo che se la prossima carta selezionata  è una carta attacco, il bottone per usarla viene disattivato
			        	hasAttacked.set(true);
			        	if(game.getPlayer(targetPlayer).getCharacter().getCurrentLife()<=0 && targetPlayer<currentPlayer)// vede se con l'attacco ho eliminato l'avversario, e se l'ho fatto e lui era prima di me, scalo l'indice del giocatore corrente di uno
			        		currentPlayer--;
		 			}
		        }
		        else
		        	System.out.print("");
		        if(submittedCard instanceof BoardingCard) { // in questo caso aggiorno la mano per vedere la carta rubata dalla mano dell'avversario selezionato
		        	refreshCardsBox(currentPlayer);
		        }
		        
			}
	        		
		}
		else if(submittedCard instanceof EventCard) {
			
			if(submittedCard instanceof MiracleCard) {
				game.submitEventCard(submittedCardIndex, currentPlayer,currentPlayer); // stesso caso delle actionCard HralingPotion e SuronEye
				 removeFromCardsBox(btn);
			}
			else {
				
				ChoiceDialog<String> dialog = new ChoiceDialog<>(toAttack.get(0),toAttack);
				dialog.setTitle("Selezione");
		        dialog.setHeaderText("Seleziona un giocatore:");
		        int targetPlayer=players.indexOf(dialog.getSelectedItem());
		        Optional<String> result = dialog.showAndWait();
		        if(result.isPresent() || result.get()!=null ) {
		        	game.submitEventCard(submittedCardIndex, currentPlayer,targetPlayer);
		        	 removeFromCardsBox(btn);
		        }
		        if(submittedCard instanceof DoomsdayCard && targetPlayer<currentPlayer)  // vede se con l'attacco ho eliminato l'avversario, e se l'ho fatto e lui era prima di me, scalo l'indice del giocatore corrente di uno
	        		currentPlayer--;
			}
		}
		else if(submittedCard instanceof WeaponCard) {
			if(game.submitWeaponCard(submittedCardIndex, currentPlayer)) {
				 removeFromCardsBox(btn);
			}
		}
		else {
			if(game.submitStaticCard(submittedCardIndex, currentPlayer)) {
				 removeFromCardsBox(btn);
			}
		}
		if(game.isGameOver()) //controlla se è rimasto solo un giocatore 
			endGame(currentPlayer);
	}
	
	public void submitPlayer(ActionEvent event)throws IOException{ //passa la giocata al prossimo giocatore
		nOfPlayers=game.getNOfPlayers();
		if(currentPlayer==nOfPlayers-1) {
			currentPlayer=0;
			game.changeTurn();
		}
		else
			currentPlayer++;
		refreshCardsBox(currentPlayer);
	
	}
	
	private void removeFromCardsBox(ToggleButton btn) { //rimuove una determinata carta/bottone dalla UI
		currentPlayerHand.remove(btn);
		cardsBox.getChildren().remove(btn);
		group.getToggles().remove(btn);
	}
	
	private void addToCardsBox(ToggleButton btn) { //aggiunge una determinata carta/bottone dalla UI
		currentPlayerHand.add(btn);
		group.getToggles().add(btn);	
        // Bind the button's width and height to the containing HBox's width and height
        btn.prefWidthProperty().bind(cardsBox.widthProperty());
        btn.prefHeightProperty().bind(cardsBox.heightProperty());
		cardsBox.getChildren().add(btn);
		HBox.setMargin(btn, MARGIN);
	}
	
	private void refreshCardsBox(int currentPlayer) { //aggiorna la UI
		group.getToggles().clear();
		cardsBox.getChildren().clear();
		currentPlayerHand.clear();
		initializeCardsBox(currentPlayer);
	}
	private void endGame(int currentPlayer) { //termina il gioco
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Messaggio informativo");
		alert.setHeaderText(null);
		alert.setContentText("Congratulazioni "+players.get(currentPlayer)+", hai vinto la partita!");
		alert.showAndWait();
		submitCardButton.setDisable(true);
		drawCardButton.setDisable(true);
		submitPlayerButton.setDisable(true);
		discardCardButton.setDisable(true);
		characterInfosButton.setDisable(true);
		playersInfosButton.setDisable(true);
		boardButton.setDisable(true);
	}
	
}