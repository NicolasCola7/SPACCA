package application;
import cards.*;
import cards.actions.ActionCard;
import cards.actions.AttackCard;
import cards.actions.BoardingCard;
import cards.actions.HealingPotionCard;
import cards.actions.SauronEyeCard;
import cards.characters.Character;
import cards.events.DoomsdayCard;
import cards.events.EventCard;
import cards.events.IdentityTheftCard;
import cards.events.MiracleCard;
import cards.statics.AztecCurseCard;
import cards.statics.BlackWidowsPoisonCard;
import cards.statics.EnchantedMirrorCard;
import cards.statics.HologramCard;
import cards.statics.RingCard;
import cards.statics.ShieldCard;
import cards.statics.StaticCard;
import game.Bot;
import game.InformationAlert;
import game.Player;
import game.Tournament;
import game.TournamentPhase;
import leaderboard.Leaderboard;
import leaderboard.LeaderboardData;
import javafx.application.Platform;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class TournamentController implements Initializable{
	private String gameCode;
	private String  adminUsername;
	private Tournament tournament;
	private int currentPlayer;
	private Clip backgroundTrack;
	private Clip cardSound;
	
	@FXML private ScrollPane cardsScroller;
	@FXML private Label playerUsernameLabel;
	@FXML private Label turnLabel;
	@FXML private Button drawCardButton;
	@FXML private Button submitCardButton;
	@FXML private Button discardCardButton;
	@FXML private Button submitPlayerButton;
	@FXML private Button characterInfosButton;
	@FXML private Button playersInfosButton;
	@FXML private Button boardInfosButton;
	@FXML private Button equipedWeaponButton;
	@FXML private Button tournamentBracketButton;
	@FXML private HBox cardsBox;
	@FXML private VBox infoBox;
	@FXML private VBox gameButtonsBox;
	@FXML private HBox playersBox;
	@FXML private MenuButton menu;
	@FXML private Pane deckPane;
	@FXML private Pane latestPlayedCardPane;
	@FXML private AnchorPane backGround;
	@FXML private ToggleButton volumeButton;
	
	private ToggleGroup group;
	private ArrayList<ToggleButton> currentPlayerHand;
	private ArrayList<String> actualGamePlayersNames;
	private Stage primaryStage;
	private FXMLLoader tournamentBracketLoader;
	private Parent tournamentBracketScene;
	@FXML private TournamentBracketController tournamentBracketController;
	private  Stage bracketStage;
	private  Scene bracketScene;
	
	
	public void setGameCode(String code) { // metodo che viene chiamato dal playerController per settare il gameCode
		gameCode=code;
	}
	
	public void setAdminUsername(String name) { //metodo che viene chiamato dal playerController per settare l'adminUsername
		adminUsername=name;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {// inizializza la partita 
		
		Platform.runLater(() -> {//serve a ritardare le istruzioni al suo interno dato che quando si passano dati da un altro controller (PlayerController in questo caso)  il metodo initialize viene eseguito prima dei metodi utilizzati nel controller che passa i dati,in guesto caso setGameCode() e setAdminUsername()
			tournamentBracketLoader =new FXMLLoader(getClass().getResource("TournamentBracket.fxml"));
			
			try {
				tournamentBracketScene=tournamentBracketLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			tournamentBracketController=tournamentBracketLoader.getController();
			
			if(isSerialized("./Files/ConfigurationFiles/"+gameCode+".ser")){
				tournament=deserialize("./Files/ConfigurationFiles/"+gameCode+".ser");
				currentPlayer=tournament.getCurrentPlayer();
				tournament.initializeProperties();
				
				//carico l'avanzamento del torneo
				for(int i=0;i<tournament.getSemifinalists().size();i++) {
					tournamentBracketController.setPlayer(tournament.getSemifinalists().get(i), i+1, TournamentPhase.SEMIFINALI);
				}
				for(int i=0;i<tournament.getFinalists().size();i++) {
					tournamentBracketController.setPlayer(tournament.getFinalists().get(i), i+1, TournamentPhase.FINALE);
				}
			}
			
			else {
				tournament=new Tournament(gameCode,adminUsername);
				currentPlayer=0;
			}
			currentPlayerHand=new ArrayList<ToggleButton>();
			actualGamePlayersNames=tournament.getActualGamePlayersNames();
			
			
			for(int i=0;i<tournament.getNOfPlayers();i++)
				tournamentBracketController.setPlayer(tournament.getPlayersNames().get(i), i+1, TournamentPhase.QUARTI);
			
			 bracketScene=new Scene(tournamentBracketScene);
			 bracketStage=new Stage();
			 bracketStage.setResizable(false);
			 bracketStage.initModality(Modality.APPLICATION_MODAL);
			 bracketStage.initOwner(primaryStage);
			 bracketStage.setTitle("Avanzamento Torneo");
			 bracketStage.setScene(bracketScene);
			
			initializeCardsBox(currentPlayer);
			
			primaryStage=(Stage) drawCardButton.getScene().getWindow();
			primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);				
		    primaryStage.setMaximized(true);
		    
			setMenuButtonStyle();
		    setButtonImages();
		    setSceneStyle();
		    
		    InformationAlert.display("Messaggio informativo",tournament.getCurrentGameMessage());
	    });  
	}
	
	private void initializeCardsBox(int currentPlayer) {// inizializza la UI del giocatore corrente
		currentPlayerHand.clear();
		turnLabel.setText(tournament.getTurn()+"° turno");
		playerUsernameLabel.setText("Username:"+tournament.getPlayer(currentPlayer).getUsername());
		group=new ToggleGroup();
		
		if(areBothBot()) {
			int looser=(int)(Math.random()*2);
			tournament.eliminatePlayer(looser);
			endCurrentGame(0);
		}
		else if(isBot(this.currentPlayer)) {
			InformationAlert.display("Messaggio informativo", "Sta giocando "+tournament.getPlayer(this.currentPlayer).getUsername()+"...");
	        useBotRoutine();
		}
		else {
			for(int i=0;i<tournament.getPlayer(currentPlayer).getHand().size();i++) {
				Card card=tournament.getPlayer(currentPlayer).getHand().get(i);
				ToggleButton btn=new ToggleButton(card.getName());
				addToCardsBox(btn);
			}
			setBindings();
			initializePlayersBox();
			String toDisplay=tournament.getActionMessage(currentPlayer);
			if(toDisplay.length()>0)
				InformationAlert.display("Messaggio informativo",toDisplay );
		}
	}
	
	private void initializePlayersBox() {
		Player player=tournament.getPlayer(1-currentPlayer);
		Character ch=player.getCharacter();
		HBox playerBox=new HBox(1);
		playerBox.setStyle("-fx-border-width:3;-fx-border-color:orange;");
		playerBox.prefWidthProperty().bind(playersBox.prefWidthProperty());
		
		ImageView chImage=new ImageView(new Image(getClass().getResourceAsStream("./CharactersImages/"+ch.getName()+".png")));
		chImage.setFitHeight(playersBox.getPrefHeight());
		chImage.setFitWidth(80);
		
		Label playerName=new Label(player.getUsername());
		playerName.setTextFill(Color.WHITE);
		
		HBox hbox=new HBox(3);
		
		Button moreInfos=new Button("");
		moreInfos.setPrefHeight(playersBox.getPrefHeight());
		moreInfos.setPrefWidth(50);
		
		ImageView infoImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Info.png")));
		infoImg.setFitHeight(50);
		infoImg.setFitWidth(50);
		
		moreInfos.setGraphic(infoImg);
		moreInfos.setOnAction(e -> getCharacterInfos(actualGamePlayersNames.indexOf(playerName.getText())));
		
		hbox.getChildren().addAll(playerName,moreInfos);
		playerBox.getChildren().addAll(chImage,hbox);
		moreInfos.setPrefWidth(playerBox.getPrefWidth());
		playersBox.getChildren().add(playerBox);
	}
	
	private void setBindings() { // serve a fare in modo che i bottoni vengano disattivati e attivati  in determinate situazioni
		SimpleBooleanProperty hasDrawed=tournament.getHasDrawed();
		SimpleBooleanProperty hasAttacked=tournament.getHasAttacked();
		SimpleBooleanProperty hasDiscarded=tournament.getHasDiscarded();
		SimpleBooleanProperty isSelectedAttackCard=new SimpleBooleanProperty(false);
		SimpleBooleanProperty isSelectedEventOrActionCard=new SimpleBooleanProperty(false);
		SimpleBooleanProperty isFirstTurn=new SimpleBooleanProperty(tournament.getTurn()==1);
		SimpleBooleanProperty isSelected=new SimpleBooleanProperty(false);
		
		isSelected.bind(group.selectedToggleProperty().isNull());// isSelected diventa true  è selezionata una carta
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			 if (newValue != null) {
	            	isSelectedAttackCard.bind(((ToggleButton)group.getSelectedToggle()).textProperty().isEqualTo("Attacco"));// isSelectedAttackCard diventa true se è selezionata la carta attacco
	            	 
	            	ArrayList<Card> hand=tournament.getPlayer(currentPlayer).getHand();
	            	ToggleButton btn=(ToggleButton) group.getSelectedToggle();
	            	Card c=hand.get(currentPlayerHand.indexOf(btn));
	            	SimpleBooleanProperty isActionOrEventCard= new SimpleBooleanProperty(false);
	            	isActionOrEventCard.set(c instanceof ActionCard || c instanceof EventCard);
	            	isSelectedEventOrActionCard.bind(isActionOrEventCard);
	            }	
	            else {
	            	isSelectedEventOrActionCard.unbind();
	            	isSelectedAttackCard.unbind();
	            }
	            
	        });
		
    	drawCardButton.disableProperty().bind(hasDrawed); //disattiva il bottone per pescare se ha già pescato
		discardCardButton.disableProperty().bind(hasDiscarded.or(isSelected)); //disattiva il bottone per scartare se il giocatore ha già scartato una carta o non ne ha selezionata alcuna
		submitCardButton.disableProperty().bind(isSelected.or(hasAttacked.and(isSelectedAttackCard)).or(isFirstTurn.and(isSelectedEventOrActionCard)));//disattiva il bottone per giocare una carta se non c'è alcuna carta selezionata, oppure se il giocatore ha già attaccato e la carta selezionata è una carta attacco
	}
	
	public void seeBoard(ActionEvent event)throws IOException{ // permette di visualizzare le carte presenti nella board del giocatore corrrente
		StaticCard[] board=tournament.getPlayer(currentPlayer).getBoard();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Board");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.getDialogPane().getStyleClass().add("game-alert");
		alert.getDialogPane().getScene().getStylesheets().add("./application/GameAlertStyle.css");
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		
		String staticCard1=(board[0]==null?"Vuoto":board[0].getName().replaceAll("\\s+", ""));
		String staticCard2=(board[1]==null?"Vuoto":board[1].getName().replaceAll("\\s+", ""));
		ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/"+staticCard1+".png")));
	    ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/"+staticCard2+".png")));
	    
	    imageView1.setFitWidth(280);
        imageView1.setFitHeight(350);
        imageView2.setFitWidth(280);
        imageView2.setFitHeight(350);
        
        HBox boardBox = new HBox(10);
        boardBox.getChildren().addAll(imageView1, imageView2);
        
        alert.getDialogPane().setContent(boardBox);
		alert.showAndWait();	
	}
	
	public void seeCharacterInfos(ActionEvent event) throws IOException{//permette di vedere le informazioni relative al personaggio del giocatore corrente
		getCharacterInfos(currentPlayer);
	}
	
	public void seeEquipedWeapon(ActionEvent event)throws IOException{
		WeaponCard wc=tournament.getPlayer(currentPlayer).getEquipedWeapon();
		String fileName=(wc==null?"Vuoto.png":wc.getName().replaceAll("\\s+", "")+".png");
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Arma Equipaggiata");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.getDialogPane().getStyleClass().add("game-alert");
		alert.getDialogPane().getScene().getStylesheets().add("./application/GameAlertStyle.css");
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		
		ImageView weaponImage = new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/"+fileName)));
		weaponImage.setFitWidth(280);
		weaponImage.setFitHeight(350);	
		
		alert.getDialogPane().setContent(weaponImage);
		alert.getDialogPane().setPadding(new Insets(10));
		alert.showAndWait();
	}
	
	public void drawCard(ActionEvent event) throws IOException { //per pescare una carta 
		Card drawedCard=tournament.drawCard(currentPlayer);
		ToggleButton btn=new ToggleButton(drawedCard.getName());
		addToCardsBox(btn); // aggiunge la carta pescata alla UI del giocatore corrente
	}
	
	public void discardCard(ActionEvent event)throws IOException{ //per scartare una carta
		ToggleButton btn=(ToggleButton) group.getSelectedToggle();
		setLatestPlayedCard(tournament.getPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)));
		tournament.discardCard(currentPlayer,currentPlayerHand.indexOf(btn));
		removeFromCardsBox(btn);// rimuove la carta scartata dalla UI del giocatore corrente	
	}

	public void submitCard(ActionEvent event)throws IOException{ // per giocare una carta
		ToggleButton btn=(ToggleButton)group.getSelectedToggle(); //ottengo il bottone selezionato
		int submittedCardIndex=currentPlayerHand.indexOf(btn); // ottengo l'indice della carta nella mano del giocatore
		Card submittedCard=tournament.getPlayer(currentPlayer).getHand().get(submittedCardIndex); // ottengo la carta nella mano del giocatore corrispondente all'indice del bottone
		String toAttack=actualGamePlayersNames.get(1-currentPlayer); // nome avversario
		int targetPlayer=0;
		
		if(submittedCard instanceof ActionCard ) {
	        targetPlayer=actualGamePlayersNames.indexOf(toAttack);
        	tournament.submitActionCard(submittedCardIndex, currentPlayer,targetPlayer);
        	removeFromCardsBox(btn);
	        if(submittedCard instanceof BoardingCard) // in questo caso aggiorno la mano per vedere la carta rubata dalla mano dell'avversario selezionato
	        	refreshCardsBox(currentPlayer);		
	        
	        setLatestPlayedCard(submittedCard);
		}
		
		else if(submittedCard instanceof EventCard) {
			targetPlayer=actualGamePlayersNames.indexOf(toAttack);
			tournament.submitEventCard(submittedCardIndex, currentPlayer,targetPlayer);
	        removeFromCardsBox(btn);
	        setLatestPlayedCard(submittedCard);
	        if(submittedCard instanceof IdentityTheftCard) {
	        	playersBox.getChildren().clear();
	        	initializePlayersBox();
			}
		}
		
		else if(submittedCard instanceof WeaponCard) {
			
			if(tournament.submitWeaponCard(submittedCardIndex, currentPlayer)) {
				 removeFromCardsBox(btn);
			}
		}
		
		else {
			if(tournament.submitStaticCard(submittedCardIndex, currentPlayer)) {
				 removeFromCardsBox(btn);
			}
		}
		
		checkElimination();
		checkCurrentPlayerElimination(targetPlayer);
		
		if(tournament.isTournamentGameOver()) //controlla se è rimasto solo un giocatore 
			endTournament(currentPlayer);
		
		else if(tournament.isActualGameOver()) {
			 endCurrentGame(currentPlayer);
		}
	}
	
	private void setLatestPlayedCard(Card c) {
		latestPlayedCardPane.getChildren().clear();
		ImageView latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/"+c.getName().replaceAll("\\s+", "")+".png")));
		latestPlayedCard.setFitHeight(200);
		latestPlayedCard.setFitWidth(140);
		latestPlayedCardPane.getChildren().add(latestPlayedCard);
	}
	
	public void submitPlayer(ActionEvent event)throws IOException{ //passa la giocata al prossimo giocatore
		updateCurrentPlayer(currentPlayer);
	}
	
	private boolean isBot(int current) {
		if (tournament.getPlayer(current) instanceof Bot)
			return true;
		else
			return false;		
	}
	
	private boolean areBothBot() {
		boolean check=false;
		for(Player p:tournament.getActualGamePlayers()){
			if(p instanceof Bot)
				check=true;
			else {
				check=false;
				break;
			}
		}
		return check;
	}
	
	private void useBotRoutine() {
		Bot bot =(Bot) tournament.getPlayer(currentPlayer);
		String botActionsMessage="Il bot ha eseguito le seguenti azioni:\n";
		//board del bot
		StaticCard[] board=bot.getBoard();
		
		// 1° azione:pesca una carta;
		tournament.drawCard(currentPlayer);
		botActionsMessage=botActionsMessage+"-Pescato una carta;\n";
		
		//2° azione:controllo se ho un'arma equipaggiata, se non ce l'ho ne cerco una nella mano e la equipaggio
		if(bot.getEquipedWeapon()==null) {
			for(Card c:bot.getHand())
				if(c instanceof WeaponCard) {
					botActionsMessage=botActionsMessage+"-Equipaggiato un'arma;\n";
					tournament.submitWeaponCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		
		if(tournament.getTurn()!=1) {
			//3° azione: utilizzo una qualsiasi carta azione che non sia Attacco
			for(Card c:bot.getHand())
				if(c instanceof ActionCard && !(c instanceof AttackCard)) {
					int targetPlayer=1-currentPlayer;
					
					if(c instanceof HealingPotionCard || c instanceof SauronEyeCard)
						botActionsMessage=botActionsMessage+"-Usato la carta "+c.getName()+";\n";
					else 
						botActionsMessage=botActionsMessage+"-Usato la carta "+c.getName()+" su "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
					setLatestPlayedCard(c);
					tournament.submitActionCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
					checkElimination();
					checkCurrentPlayerElimination(targetPlayer);
					
					//controlla se è rimasto solo un giocatore 
					if(tournament.isTournamentGameOver()) {
						InformationAlert.display("Messaggio informativo",botActionsMessage);
						endTournament(currentPlayer);
						return;
					}
					else if(tournament.isActualGameOver()) {
						InformationAlert.display("Messaggio informativo",botActionsMessage);
						 endCurrentGame(currentPlayer);
						 return;
					}
					break;
				}
			
			//4° azione: controlla se ha carta attacco e la usa
			if(bot.hasAttackCard()) {
				//scelgo un giocatore da attaccare
				int targetPlayer=1-currentPlayer;
				//cerco posizione carta
				for(Card c:bot.getHand())
					if(c instanceof AttackCard) {
						botActionsMessage=botActionsMessage+"-Attaccato "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
						setLatestPlayedCard(c);
						tournament.submitActionCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						break;
					}
				checkElimination();
				checkCurrentPlayerElimination(targetPlayer);
				//controlla se è rimasto solo un giocatore 
				if(tournament.isTournamentGameOver()) {
					InformationAlert.display("Messaggio informativo",botActionsMessage);
					endTournament(currentPlayer);
					return;
				}
				else if(tournament.isActualGameOver()) {
					InformationAlert.display("Messaggio informativo",botActionsMessage);
					endCurrentGame(currentPlayer);
					return;
				}
			}
			//5° azione:gestione carte evento, se ne ha una la usa(la carta miracolo solo se ha <=30 p.ti vita)
			if(bot.hasEventCard()) {
				int targetPlayer=1-currentPlayer;
				for(Card c:bot.getHand()) {
					if(c instanceof DoomsdayCard) {
						botActionsMessage=botActionsMessage+"-Eliminato, usando la carta Giorno del Giudizio, "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
						tournament.submitEventCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						checkElimination();
						checkCurrentPlayerElimination(targetPlayer);
						//controlla se è rimasto solo un giocatore 
						if(tournament.isTournamentGameOver()) {
							InformationAlert.display("Messaggio informativo",botActionsMessage);
							endTournament(currentPlayer);
							return;
						}
						else if(tournament.isActualGameOver()) {
							InformationAlert.display("Messaggio informativo",botActionsMessage);
							 endCurrentGame(currentPlayer);
							 return;
						}
						break;
					}
					if(c instanceof IdentityTheftCard) {
						botActionsMessage=botActionsMessage+"-Cambiato personaggio usando la carta Furto d'identità su "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
						tournament.submitEventCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						break;
					}
					if(c instanceof MiracleCard && bot.getCharacter().getCurrentLife()<=30) {
						botActionsMessage=botActionsMessage+"-Recuperato tutti i punti vita usando la carta Miracolo;\n";
						tournament.submitEventCard(bot.getHand().indexOf(c),currentPlayer,currentPlayer);
						break;
					}
					setLatestPlayedCard(c);
				}
			}
		}
		
		//6° azione: posiziono tutte le carte statiche che posso nelle posizioni libere
		if(board[0]==null) {
			for(Card c:bot.getHand())
				if(c instanceof ShieldCard || c instanceof HologramCard || c instanceof EnchantedMirrorCard) {
					tournament.submitStaticCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		if(board[1]==null) {
			for(Card c:bot.getHand())
				if(c instanceof AztecCurseCard || c instanceof RingCard || c instanceof BlackWidowsPoisonCard) {
					tournament.submitStaticCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		
		//7° azione: scarta una carta
		if(!bot.getHand().isEmpty()) {
			int toDiscard=(int)(Math.random()*bot.getHand().size());
			botActionsMessage=botActionsMessage+"-Scartato una carta;\n";
			setLatestPlayedCard(bot.getHand().get(toDiscard));
			tournament.discardCard(currentPlayer,toDiscard);
		}
		
		//mostro le azioni  rilevanti effettuate dal bot
		InformationAlert.display("Messaggio informativo",botActionsMessage);
		
		//8° azione: passo turno
		updateCurrentPlayer(currentPlayer);  
	}
	
	//controllo che serve per quando viene eliminato un giocatore che in ordine di giocata è prima del giocatore corrente
	private void checkElimination() {
		if(actualGamePlayersNames.size()==1 && currentPlayer==1) {
			currentPlayer=0;
			tournament.setCurrentPlayer(currentPlayer);
		}
	}
		
	 // caso in cui l'attaccate venga eliminato dal veleno di vedova nera o specchio o entrambi
	private void checkCurrentPlayerElimination(int targetPlayer) {
		if(tournament.getPlayer(currentPlayer).getCharacter().getCurrentLife()<=0) {
			String message="";
			Player target =tournament.getPlayer(targetPlayer);
			
			if(target.hasEnchantedMirror() && target.hasBlackWidowsPoison()) {
				target.removeFromBoardInPosition(0);
				message="Il veleno di vedova nera e lo specchio ti hanno ucciso!Sei stato eliminato da "+target.getUsername()+".";
			}
			else if(target.hasEnchantedMirror() && !target.hasBlackWidowsPoison()) {
				target.removeFromBoardInPosition(0);
				message="Lo specchio ti ha ucciso!Sei stato eliminato da "+target.getUsername()+".";
			}
			else
				message="Il veleno di vedova nera ti ha ucciso!Sei stato eliminato da "+target.getUsername()+".";
			
			if(!(tournament.getPlayer(currentPlayer) instanceof Bot)) 
				InformationAlert.display("Messaggio informativo",message);
			
			tournament.eliminatePlayer(currentPlayer);
			currentPlayer=0;
			tournament.setCurrentPlayer(currentPlayer);
		}
		
		checkConcurrentElimination();
	}
	
	//caso in cui i due giocatori si eliminino a vicenda	
	private void checkConcurrentElimination() {
		if(tournament.getActualGamePlayers().size()==0) { 
			ArrayList<String> latestTwo=tournament.getLatestTwoEliminated();
			InformationAlert.display("Messaggio informativo", "Vi siete eliminati a vicenda, verrà lanciata una moneta per decretare il vincitore: se esce testa vince "+latestTwo.get(0)+", se esce croce "+latestTwo.get(1));
			int winner=(int)(Math.random()*2);
			 
			// si potrebbe provare a fare un'animazione di una moneta
			
			if (winner==0) {
				InformationAlert.display("Messaggio informativo", "E' uscito TESTA");
				tournament.eliminatePlayer(1);
			}
			else {
				InformationAlert.display("Messaggio informativo", "E' uscito CROCE");
				tournament.eliminatePlayer(0);
			}
		}
	}
	
	private void removeFromCardsBox(ToggleButton btn) { //rimuove una determinata carta/bottone dalla UI
		currentPlayerHand.remove(btn);
		cardsBox.getChildren().remove(btn);
		group.getToggles().remove(btn);
	}
	
	private void addToCardsBox(ToggleButton btn) { //aggiunge una determinata carta/bottone dalla UI
		currentPlayerHand.add(btn);
		group.getToggles().add(btn);	
        btn.setPrefHeight(215);
        btn.setPrefWidth(145);
        setCardImage(btn);
		cardsBox.getChildren().add(btn);
	}
	
	private void updateCurrentPlayer(int currentPlayer) {
		tournament.setHasAttacked(false);
		tournament.setHasDiscarded(false);
		tournament.setHasDrawed(false);
		if(currentPlayer==1)
			tournament.changeTurn();
		
		this.currentPlayer=1-currentPlayer;
		tournament.setCurrentPlayer(this.currentPlayer);
		
		refreshCardsBox(this.currentPlayer);
	}
	
	private void refreshCardsBox(int currentPlayer) {//aggiorna la UI
		group.getToggles().clear();
		cardsBox.getChildren().clear();
		playersBox.getChildren().clear();
		currentPlayerHand.clear();
		initializeCardsBox(currentPlayer);
	}
	
	private void endTournament(int currentPlayer) { //termina il gioco
		InformationAlert.display("Messaggio informativo","Congratulazioni "+actualGamePlayersNames.get(currentPlayer)+", hai vinto il torneo!");
		assignScore(actualGamePlayersNames.get(currentPlayer));
		tournamentBracketController.setWinner(actualGamePlayersNames.get(currentPlayer));
		deleteGameFromGamesDatasFile();
		deleteSerializationFile();
		disableButtons();
		showLeaderboard();
		
	}
	private void endCurrentGame(int currentPlayer) {
		String actualWinner=actualGamePlayersNames.get(currentPlayer);
		InformationAlert.display("Messaggio informativo","Congratulazioni "+actualWinner+", hai vinto la partita e sei passato alla fase successiva!");
		
		TournamentPhase phase=(tournament.getTournamentPhase().equals(TournamentPhase.QUARTI) ? TournamentPhase.SEMIFINALI : TournamentPhase.FINALE );
		tournamentBracketController.setPlayer(actualWinner, tournament.getGameNumber(), phase);
		tournament.switchGame();
		
		actualGamePlayersNames=tournament.getActualGamePlayersNames();
		refreshCardsBox(tournament.getCurrentPlayer());
	}
	
	private void disableButtons() {
		tournament.setHasAttacked(true);
		tournament.setHasDiscarded(true);
		tournament.setHasDrawed(true);

		submitPlayerButton.disableProperty().set(true);
		characterInfosButton.disableProperty().set(true);
		boardInfosButton.disableProperty().set(true);
		equipedWeaponButton.disableProperty().set(true);
		cardsScroller.disableProperty().set(true);
		
		menu.getItems().get(0).setDisable(true);
		menu.getItems().get(1).setDisable(true);
	}
	
	public void serialize(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tournament);
            out.close();
            fileOut.close();
            System.out.println("serialized successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Static method to deserialize the GameController
    public  Tournament deserialize(String filename) {
       Tournament tournament = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tournament = (Tournament) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("deserialized successfully");
           
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tournament;
    }
    
    private  boolean isSerialized(String filename) {
        File file = new File(filename);
        return file.exists();
    }
    
    private void assignScore(String currentPlayerName) {
    	if(currentPlayerName!="bot")
    		tournament.getLeaderboard().increaseScore(currentPlayerName);
    }
    
    public void saveAndQuit(ActionEvent event) throws IOException{
    	String serializationFile="./Files/ConfigurationFiles/"+gameCode+".ser";
    	serialize(serializationFile);
    	Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setGraphic(null);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per uscire dalla partita!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			backgroundTrack.stop();
			Stage stage = (Stage) ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
	        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
	}
    
   public void save(ActionEvent event) throws IOException{
	   serialize("./Files/ConfigurationFiles/"+gameCode+".ser");
   }
   
   public void quit(ActionEvent event) throws IOException{
	   Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setGraphic(null);
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per uscire dalla partita senza salvare i progressi!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			backgroundTrack.stop();
			Stage stage = (Stage) ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
	        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
   }
   
   private void closeWindowEvent(WindowEvent event) {
	   if(!tournament.isTournamentGameOver()) {
		   Alert alert = new Alert(Alert.AlertType.INFORMATION);
		   alert.getButtonTypes().remove(ButtonType.OK);
		   alert.getButtonTypes().add(ButtonType.CANCEL);
		   alert.getButtonTypes().add(ButtonType.YES);
		   alert.setTitle("Chiusura applicazione");
		   alert.setHeaderText(null);
		   alert.setContentText("Sei sicuro di voler uscire senza salvare?");
		   if(alert.showAndWait().get()==ButtonType.CANCEL) {
		       event.consume();
		   }
	   }
   }
   
   private TableView<LeaderboardData>  getLeaderboard() {
	   TableView<LeaderboardData> leaderboard = new TableView<>();
       TableColumn<LeaderboardData,Integer> positionColumn = new TableColumn<>("POSIZIONE");
       TableColumn<LeaderboardData,String> nameColumn = new TableColumn<>("NOME");
       TableColumn<LeaderboardData,Integer> scoreColumn = new TableColumn<>("VITTORIE");
       
       leaderboard.getColumns().addAll(positionColumn, nameColumn,scoreColumn);
       
       positionColumn.setCellValueFactory(new PropertyValueFactory<LeaderboardData,Integer>("position"));
       nameColumn.setCellValueFactory(new PropertyValueFactory<LeaderboardData,String>("name"));
       scoreColumn.setCellValueFactory(new PropertyValueFactory<LeaderboardData,Integer>("score"));
      
       ObservableList<LeaderboardData> data = getDataFromLeaderboardFile();
       leaderboard.setItems(data);
       
       return leaderboard;
   }
   
   public void showLeaderboard(ActionEvent event)throws IOException{
	   VBox vbox = new VBox(getLeaderboard());
       Scene scene = new Scene(vbox, 300, 200);
       scene.getStylesheets().add("./application/LeaderboardStyle.css");
       Stage popupLeaderboard = new Stage();
       popupLeaderboard.setResizable(false);
       popupLeaderboard.initModality(Modality.APPLICATION_MODAL);
       popupLeaderboard.setTitle("Leaderboard");
       popupLeaderboard.setScene(scene);
       popupLeaderboard.show();;
   }
   
   public void showLeaderboard() {
	   VBox vbox = new VBox(getLeaderboard());
       Scene scene = new Scene(vbox, 300, 200);
       scene.getStylesheets().add("./application/LeaderboardStyle.css");
       Stage popupLeaderboard = new Stage();
       popupLeaderboard.setResizable(false);
       popupLeaderboard.initModality(Modality.APPLICATION_MODAL);
       popupLeaderboard.setTitle("Leaderboard");
       popupLeaderboard.setScene(scene);
       popupLeaderboard.show();
   }
   
   private  ObservableList<LeaderboardData> getDataFromLeaderboardFile() {
	   ObservableList<LeaderboardData> data = FXCollections.observableArrayList();
	   try {
		   File leaderboardFile=new File("./Files/ConfigurationFiles/"+adminUsername+"TournamentsLeaderboard.csv");
		   Scanner scan=new Scanner(leaderboardFile);
		   int position=1;
		   while(scan.hasNextLine()){
			   String[] line=scan.nextLine().split(",");
			   String name=line[0];
			   int score=Integer.parseInt(line[1]);
			   data.add(new LeaderboardData(position,name,score));
			   position++;
		   }
 
       } catch (FileNotFoundException e) {
           e.printStackTrace(); 
       }
	   return data;
   }
   
   private void deleteGameFromGamesDatasFile() {
	   File file=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	   ArrayList<String> datas=new ArrayList<String>();
	   try {
		   Scanner scan=new Scanner(file);
		   while(scan.hasNextLine()) {
			   String line=scan.nextLine();
			   String[] splittedLine=line.split(",");
			   if(!splittedLine[2].equals(gameCode)) 
				   datas.add(line);
		   }
		   PrintWriter pw=new PrintWriter(file);
		   for(String line:datas)
			   pw.println(line);
		   pw.close();
	   }
	   catch(IOException e) {
		   e.printStackTrace();
	   }
   }
   
   private void deleteSerializationFile() {
	   File serializationFile= new File("./Files/ConfigurationFiles/"+gameCode+".ser");
	   if(serializationFile.exists())
		   serializationFile.delete();
   }
   
   private void setCardImage(ToggleButton btn) {
       Image icon = new Image(getClass().getResourceAsStream("./CardsImages/"+btn.getText().replaceAll("\\s+", "")+".png"));
       ImageView iconView = new ImageView(icon);
       iconView.setFitWidth(btn.getPrefWidth()); 
       iconView.setFitHeight(btn.getPrefHeight());
       
       btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       btn.setGraphic(iconView);  
       
      setCardStyle(btn);
   }
   
   private void setCardStyle(ToggleButton btn) {
	   btn.setPadding(new Insets(10, 10, 10, 10));
       btn.setStyle("-fx-background-color: transparent;");
       btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: orange;"));
       btn.setOnMouseExited(e -> {
           if (!btn.isFocused()) {
        	   btn.setStyle("-fx-background-color: transparent;");
           }
       });
       
       btn.selectedProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue) {
        	   btn.setStyle("-fx-background-color:orange;");
        	   if(!volumeButton.isSelected()) {
	        	   cardSound.setMicrosecondPosition(0);
	        	   cardSound.start();
        	   }
           } else {
        	   btn.setStyle("-fx-background-color: transparent;");
           }
       });
   }
   
   private void setButtonImages() {
	   ImageView chButtonImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Character.png")));
	   chButtonImg.setFitWidth(50);
	   chButtonImg.setFitHeight(50);
	   characterInfosButton.setGraphic(chButtonImg);
		 
	   ImageView equipedWeaponImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Weapon3.png")));
	   equipedWeaponImg.setFitWidth(50);
	   equipedWeaponImg.setFitHeight(50);
	   equipedWeaponButton.setGraphic(equipedWeaponImg);
		 
	   ImageView boardImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Board.png")));
	   boardImg.setFitWidth(50);
	   boardImg.setFitHeight(50);
	   boardInfosButton.setGraphic(boardImg);
		
	   ImageView discardImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/DiscardCard.png")));
	   discardImg.setFitWidth(50);
	   discardImg.setFitHeight(50);
	   discardCardButton.setGraphic(discardImg);
	 
	   ImageView drawImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/DrawCard.png")));
	   drawImg.setFitWidth(50);
	   drawImg.setFitHeight(50);
	   drawCardButton.setGraphic(drawImg);
		 
	   ImageView submitImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/PlayCard.png")));
	   submitImg.setFitWidth(50);
	   submitImg.setFitHeight(50);
	   submitCardButton.setGraphic(submitImg);
	   
	   ImageView nextPlayerImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/NextPlayer.png")));
	   nextPlayerImg.setFitWidth(50);
	   nextPlayerImg.setFitHeight(50);
	   submitPlayerButton.setGraphic(nextPlayerImg);
	   
	   volumeButton.setLayoutX(menu.getLayoutX()+60);
	   volumeButton.setLayoutY(primaryStage.getY());
	   ImageView volumeOnImage = new ImageView(new Image("./application/ButtonImages/VolumeOn.png"));
	   volumeOnImage.setFitWidth(35);
	   volumeOnImage.setFitHeight(35);
	   ImageView volumeOffImage = new ImageView(new Image("./application/ButtonImages/VolumeOff.png"));
	   volumeOffImage.setFitWidth(35);
	   volumeOffImage.setFitHeight(35);
	   volumeButton.setGraphic(volumeOnImage);
	   volumeButton.setSelected(false);
	   volumeButton.setOnAction((event) -> {
	        
         if (volumeButton.isSelected()) {
             volumeButton.setGraphic(volumeOffImage);
             volumeButton.setSelected(true);
             backgroundTrack.stop();
             cardSound.stop();
         }
         else {
        	 volumeButton.setGraphic(volumeOnImage);
        	 volumeButton.setSelected(false);
        	 
        	 backgroundTrack.setMicrosecondPosition(backgroundTrack.getMicrosecondPosition());
        	 backgroundTrack.start();
        	 backgroundTrack.loop(Clip.LOOP_CONTINUOUSLY);
     
         }
     });
	   ImageView bracketImg= new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/bracket.png")));
	   nextPlayerImg.setFitWidth(50);
	   nextPlayerImg.setFitHeight(50);
	   tournamentBracketButton.setGraphic(bracketImg);
   }	
   
   private void setMenuButtonStyle() {
	   menu.setLayoutX(primaryStage.getX()+10);
       menu.setLayoutY(primaryStage.getY()+10);
       ImageView menuImg=new ImageView(new Image(getClass().getResourceAsStream("./ButtonImages/Menu1.png")));
       menuImg.setFitWidth(menu.getPrefWidth()); 
       menuImg.setFitHeight(menu.getPrefHeight());
       menu.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       menu.setGraphic(menuImg);
   }
   
   private void getCharacterInfos(int player) {
	   Character character=tournament.getPlayer(player).getCharacter();
	   ImageView chImage = new ImageView(new Image(getClass().getResourceAsStream("./CharactersCardsImages/"+character.getName()+".png")));
	   chImage.setFitWidth(300);
	   chImage.setFitHeight(350);
	   VBox box = new VBox(10);
		
	   HBox lifeBox=new HBox(5);
	   Text txt=new Text("Vita rimanente:");
	   txt.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	   ProgressBar lifeBar = new ProgressBar(character.getCurrentLife()/character.getInitialLife());
	   lifeBar.setPrefWidth(150);
	   lifeBar.setPrefHeight(30);
	   lifeBar.setStyle("-fx-accent:orange;-fx-background-color:purple;-fx-border-color:purple;");
		
	   Label lifeLabel = new Label(Integer.toString(character.getCurrentLife()));
	   lifeLabel.setTextFill(Color.PURPLE); // Imposta il colore del testo a bianco
	   lifeLabel.setFont(Font.font("System", 14)); // Imposta il carattere a bold
	   // Sovrappone la label sulla progress bar
       StackPane lifePane = new StackPane();
       lifePane.getChildren().addAll(lifeBar, lifeLabel);
  
       // Imposta la posizione della label al centro della progress bar
	   StackPane.setMargin(lifeLabel, new Insets(0, 4, 0, 4));
	   StackPane.setAlignment(lifeLabel, javafx.geometry.Pos.CENTER_LEFT);
	   lifeBox.getChildren().addAll(txt,lifePane);
		
	   if(player==currentPlayer) {
		  HBox attackBox=new HBox(5);
		  Text txt2=new Text("Potenza d'attacco:");
		  txt2.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		  int attackPower=tournament.getPlayer(currentPlayer).getAttackPower();
		 
		  ProgressBar attackBar = new ProgressBar(attackPower/24.0);
		  attackBar.setPrefWidth(123);
		  attackBar.setPrefHeight(30);
		  attackBar.setStyle("-fx-accent:orange;-fx-background-color:purple;-fx-border-color:purple;"+ "}");
		 
		  Label attackLabel = new Label(Integer.toString(attackPower));
		  attackLabel.setTextFill(Color.WHITE); // Imposta il colore del testo a bianco
		  attackLabel.setFont(Font.font("System", 14)); // Imposta il carattere a bold

		  StackPane attackPane = new StackPane();
		  attackPane.getChildren().addAll(attackBar, attackLabel);
		  StackPane.setMargin(attackLabel, new Insets(7, 0, 3, 7));
		  StackPane.setAlignment(attackLabel, javafx.geometry.Pos.CENTER_LEFT);
		  attackBox.getChildren().addAll(txt2,attackPane);
		  box.getChildren().addAll(chImage,lifeBox,attackBox);
	   }
	   else
		   box.getChildren().addAll(chImage,lifeBox);
		
	   Alert alert = new Alert(Alert.AlertType.INFORMATION);
	   alert.setHeaderText(null);
	   alert.setGraphic(null);
	   alert.getDialogPane().getStyleClass().add("game-alert");
	   alert.getDialogPane().getScene().getStylesheets().add("./application/GameAlertStyle.css");
	   alert.setTitle("Personaggio");
	   alert.getButtonTypes().remove(ButtonType.OK);
	   alert.getButtonTypes().add(ButtonType.CLOSE);
	   alert.getDialogPane().setContent(box);
	   alert.showAndWait();
   }
   private void setSceneStyle() {
	   gameButtonsBox.setLayoutX(primaryStage.getWidth()-(gameButtonsBox.getPrefWidth()));
       infoBox.setLayoutX(0);
       cardsBox.setLayoutY(primaryStage.getHeight()-(gameButtonsBox.getPrefHeight()));
      
       AnchorPane.setBottomAnchor(cardsScroller, 0.0);
       cardsScroller.setPrefWidth(primaryStage.getWidth());
       cardsScroller.toBack();
       
       AnchorPane.setTopAnchor(playersBox, 10.0); 
       AnchorPane.setLeftAnchor(playersBox, (primaryStage.getWidth() - playersBox.getWidth()) / 2);
      
       AnchorPane.setTopAnchor(deckPane, (primaryStage.getHeight() - deckPane.getPrefHeight()) / 4);
       AnchorPane.setLeftAnchor(deckPane, (primaryStage.getWidth() - deckPane.getPrefWidth())/4);
      
       ImageView deckImg=new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/Deck.png")));
       deckImg.setFitHeight(300);
       deckImg.setFitWidth(270);
       deckPane.getChildren().add(deckImg);
       
       Card latest=(tournament.getDeck().getStockpile().size()==0?null:tournament.getDeck().getStockpile().getLast());
       String latestName=(latest==null?"Vuoto":latest.getName().replaceAll("\\s+", ""));
     
       ImageView latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("./CardsImages/"+latestName+".png")));
       latestPlayedCard.setFitHeight(200);
       latestPlayedCard.setFitWidth(140);
      
       AnchorPane.setTopAnchor(latestPlayedCardPane, (primaryStage.getHeight() - latestPlayedCardPane.getPrefHeight()) / 3);
       AnchorPane.setLeftAnchor(latestPlayedCardPane, (primaryStage.getWidth() - latestPlayedCardPane.getPrefWidth())/2);
       latestPlayedCardPane.getChildren().add(latestPlayedCard);

       File filePath1=new File("soundtrack1.wav");
       File filePath2=new File("CardSound.wav");
      
       try {   
    	   AudioInputStream backgroundAudioStream=AudioSystem.getAudioInputStream(filePath1);
    	   backgroundTrack=AudioSystem.getClip();
    	   backgroundTrack.open(backgroundAudioStream);
    	   backgroundTrack.start();
    	   backgroundTrack.loop(Clip.LOOP_CONTINUOUSLY);
    	   
    	   AudioInputStream cardsAudioStream=AudioSystem.getAudioInputStream(filePath2);
    	   cardSound=AudioSystem.getClip();
    	   cardSound.open(cardsAudioStream);
       } catch (UnsupportedAudioFileException e) {
    	   e.printStackTrace();
       } catch (IOException e) {
    	   e.printStackTrace();
       }catch(LineUnavailableException e) {
    	   e.printStackTrace();
       }
   }
   public void showTournamentBracket(ActionEvent event) throws IOException{
	   bracketStage.showAndWait();
   }
 
}