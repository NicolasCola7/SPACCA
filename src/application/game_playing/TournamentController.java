package application.game_playing;
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
import game.InformationAlert;
import game.Tournament;
import game.TournamentPhase;
import leaderboard.LeaderboardData;
import player.Bot;
import player.Player;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import application.RulesController;


public class TournamentController extends GameController implements Initializable{
	private Tournament tournament;
	
	@FXML private Button tournamentBracketButton;
	@FXML private TournamentBracketController tournamentBracketController;
	
	private ArrayList<String> actualGamePlayersNames;
	
	private FXMLLoader tournamentBracketLoader;
	private Parent tournamentBracketScene;
	private  Stage bracketStage;
	private  Scene bracketScene;

	
	@Override
	//init or load game
	public void initialize(URL arg0, ResourceBundle arg1) {
	    
		Platform.runLater(() -> {//delay istructions, setGameCode() e setAdminUsername() need to be executed before init method when called by PlayerController
			 initTournamentBracketScene();
			
			if(isSerialized("./Files/ConfigurationFiles/"+gameCode+".ser")){
				tournament=deserialize("./Files/ConfigurationFiles/"+gameCode+".ser");
				currentPlayer=tournament.getCurrentPlayer();
				tournament.initializeProperties();
				loadTournamentBracketProgress();
			}
			else {
				tournament=new Tournament(gameCode,adminUsername);
				currentPlayer=0;
			}
			 
			//init quarter finalist slots in tournament bracket
			for(int i=0;i<tournament.getNOfPlayers();i++)
				tournamentBracketController.setPlayer(tournament.getPlayersNames().get(i), i+1, TournamentPhase.QUARTI);
			
			currentPlayerHand=new ArrayList<ToggleButton>();
			actualGamePlayersNames=tournament.getActualGamePlayersNames();
			
			primaryStage=(Stage) drawCardButton.getScene().getWindow();
			primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);				
		    primaryStage.setMaximized(true);
		   
		    Card latest=(tournament.getDeck().getStockpile().size()==0?null:tournament.getDeck().getStockpile().getLast());
		    setLatestPlayedCard(latest);
		    
		    setBracketButtonImage();
		    setSceneStyle();
		    
		    InformationAlert.display("Messaggio informativo",tournament.getCurrentGameMessage());
		    initializeCardsBox(currentPlayer);
		    
	    }); 
		
	}
	
	//init UI of curent player
	public void initializeCardsBox(int currentPlayer) {
		currentPlayerHand.clear();
		turnLabel.setText(tournament.getTurn()+"° turno");
		playerUsernameLabel.setText("Username:"+tournament.getPlayer(currentPlayer).getUsername());
		group=new ToggleGroup();
		
		if(areBothBot()) {
			int looser=(int)(Math.random()*2);
			tournament.eliminatePlayer(looser);
			
			if(tournament.isTournamentGameOver()) 
				endTournament(currentPlayer);
			else 
				endCurrentGame(currentPlayer);	
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
	
	//init player box of current player
	public void initializePlayersBox() {
		Player player=tournament.getPlayer(1-currentPlayer);
		Character ch=player.getCharacter();
		HBox playerBox=new HBox(1);
		playerBox.setStyle("-fx-border-width:3;-fx-border-color:orange;");
		playerBox.prefWidthProperty().bind(playersBox.prefWidthProperty());
		
		ImageView chImage=new ImageView(new Image(getClass().getResourceAsStream("CharactersImages/"+ch.getName()+".png")));
		chImage.setFitHeight(playersBox.getPrefHeight());
		chImage.setFitWidth(80);
		
		Label playerName=new Label(player.getUsername());
		playerName.setTextFill(Color.WHITE);
		
		HBox hbox=new HBox(3);
		
		Button moreInfos=new Button("");
		moreInfos.setPrefHeight(playersBox.getPrefHeight());
		moreInfos.setPrefWidth(50);
		
		ImageView infoImg=new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Info.png")));
		infoImg.setFitHeight(50);
		infoImg.setFitWidth(50);
		
		moreInfos.setGraphic(infoImg);
		moreInfos.setOnAction(e -> getCharacterInfos(actualGamePlayersNames.indexOf(playerName.getText())));
		
		hbox.getChildren().addAll(playerName,moreInfos);
		playerBox.getChildren().addAll(chImage,hbox);
		moreInfos.setPrefWidth(playerBox.getPrefWidth());
		playersBox.getChildren().add(playerBox);
	}
	
	//set boolean proprieties for activate/disable buttons  
	public void setBindings() { 
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
		
    	drawCardButton.disableProperty().bind(hasDrawed); //disable draw button if already drawed 
		discardCardButton.disableProperty().bind(hasDiscarded.or(isSelected)); //disable discard button if player already discarded a card or not selected one
		submitCardButton.disableProperty().bind(isSelected.or(hasAttacked.and(isSelectedAttackCard)).or(isFirstTurn.and(isSelectedEventOrActionCard)));//disable button for use a card if It is not selected or if player already attacked 
	}
	
	//allow to see cards on actual player's board
	public void seeBoard(ActionEvent event){ 
		StaticCard[] board=tournament.getPlayer(currentPlayer).getBoard();
		showBoard(board);
	}
	
	//allows to see info about current player 
	public void seeCharacterInfos(ActionEvent event) {
		getCharacterInfos(currentPlayer);
	}
	
	//allows to see equipped weapon
	public void seeEquipedWeapon(ActionEvent event){
		WeaponCard wc=tournament.getPlayer(currentPlayer).getEquipedWeapon();
		showEquipedWeapon(wc);
	}
	
	//draw a card
	public void drawCard(ActionEvent event)  { 
		Card drawedCard=tournament.drawCard(currentPlayer);
		ToggleButton btn=new ToggleButton(drawedCard.getName());
		addToCardsBox(btn); // add draw card to current player's UI
	}
	
	//discard a card
	public void discardCard(ActionEvent event){ 
		ToggleButton btn=(ToggleButton) group.getSelectedToggle();
		setLatestPlayedCard(tournament.getPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)));
		tournament.discardCard(currentPlayer,currentPlayerHand.indexOf(btn));
		removeFromCardsBox(btn);// remove discrad card from current player's UI
	}
	
	//use a card
	public void submitCard(ActionEvent event){ 
		ToggleButton btn=(ToggleButton)group.getSelectedToggle(); //get selected button
		int submittedCardIndex=currentPlayerHand.indexOf(btn); // get index of card in player's hand
		Card submittedCard=tournament.getPlayer(currentPlayer).getHand().get(submittedCardIndex); // get card from player's card index 
		String toAttack=actualGamePlayersNames.get(1-currentPlayer); // get adversary name
		int targetPlayer=0;
		
		//attack with different type of card
		
		if(submittedCard instanceof ActionCard ) {
	        targetPlayer=actualGamePlayersNames.indexOf(toAttack);
        	tournament.submitActionCard(submittedCardIndex, currentPlayer,targetPlayer);
        	removeFromCardsBox(btn);
	        if(submittedCard instanceof BoardingCard) // update hand to see stolen card from adversary hand
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
		
		if(tournament.isTournamentGameOver()) //check if there is only 1 player left
			endTournament(currentPlayer);
		
		else if(tournament.isActualGameOver()) {
			 endCurrentGame(currentPlayer);
		}
	}
	
	//pass game to next player
	public void submitPlayer(ActionEvent event){ 
		updateCurrentPlayer(currentPlayer);
	}
	
	//check if player is a bot
	private boolean isBot(int current) {
		if (tournament.getPlayer(current) instanceof Bot)
			return true;
		else
			return false;		
	}
	
	//check if both players are bot
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
	
	//bot actions
	public void useBotRoutine() {
		Bot bot =(Bot) tournament.getPlayer(currentPlayer);
		String botActionsMessage="Il bot ha eseguito le seguenti azioni:\n";
		//bot's board 
		StaticCard[] board=bot.getBoard();
		
		// 1°: draw a card;
		tournament.drawCard(currentPlayer);
		botActionsMessage=botActionsMessage+"-Pescato una carta;\n";
		
		//2° check if have an equipped weapon, if not find it in the hand and equip
		if(bot.getEquipedWeapon()==null) {
			for(Card c:bot.getHand())
				if(c instanceof WeaponCard) {
					botActionsMessage=botActionsMessage+"-Equipaggiato un'arma;\n";
					tournament.submitWeaponCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		
		if(tournament.getTurn()!=1) {
			//3° use any  card except action card
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
					
					//check if there is only 1 player left 
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
			
			//4° check if have an attack card and use it
			if(bot.hasAttackCard()) {
				//choose a player to attack
				int targetPlayer=1-currentPlayer;
				//search for card position
				for(Card c:bot.getHand())
					if(c instanceof AttackCard) {
						botActionsMessage=botActionsMessage+"-Attaccato "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
						setLatestPlayedCard(c);
						tournament.submitActionCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						break;
					}
				checkElimination();
				checkCurrentPlayerElimination(targetPlayer);
				//check if there is only 1 player left 
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
			
			//5° event card management, use it if you have one(miracle card only if  <= life points)
			if(bot.hasEventCard()) {
				int targetPlayer=1-currentPlayer;
				for(Card c:bot.getHand()) {
					if(c instanceof DoomsdayCard) {
						botActionsMessage=botActionsMessage+"-Eliminato, usando la carta Giorno del Giudizio, "+tournament.getPlayer(targetPlayer).getUsername()+";\n";
						tournament.submitEventCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						checkElimination();
						checkCurrentPlayerElimination(targetPlayer);
						//check if there is only 1 player left
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
		
		//6° place every static card in free positions
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
		
		//7° discard a card
		if(!bot.getHand().isEmpty()) {
			int toDiscard=(int)(Math.random()*bot.getHand().size());
			botActionsMessage=botActionsMessage+"-Scartato una carta;\n";
			setLatestPlayedCard(bot.getHand().get(toDiscard));
			tournament.discardCard(currentPlayer,toDiscard);
		}
		
		//show bot actions 
		InformationAlert.display("Messaggio informativo",botActionsMessage);
		
		//8° update turn
		updateCurrentPlayer(currentPlayer);  
	}
	
	//check needed if the eliminated player was before current player
	public void checkElimination() {
		if(actualGamePlayersNames.size()==1 && currentPlayer==1) {
			currentPlayer=0;
			tournament.setCurrentPlayer(currentPlayer);
		}
	}
		
	// striker hit by veleno di vedova nere,specchio or both
	public void checkCurrentPlayerElimination(int targetPlayer) {
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
	
	//players  eliminate each other
	public void checkConcurrentElimination() {
		if(tournament.getActualGamePlayers().size()==0) { 
			ArrayList<String> latestTwo=tournament.getLatestTwoEliminated();
			InformationAlert.display("Messaggio informativo", "Vi siete eliminati a vicenda, verrà lanciata una moneta per decretare il vincitore: se esce testa vince "+latestTwo.get(0)+", se esce croce "+latestTwo.get(1));
			int winner=(int)(Math.random()*2);
			 
			
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
	
	//update current player
	public void updateCurrentPlayer(int currentPlayer) {
		tournament.setHasAttacked(false);
		tournament.setHasDiscarded(false);
		tournament.setHasDrawed(false);
		if(currentPlayer==1)
			tournament.changeTurn();
		
		this.currentPlayer=1-currentPlayer;
		tournament.setCurrentPlayer(this.currentPlayer);
		
		refreshCardsBox(this.currentPlayer);
	}
	
	//update UI
	private void refreshCardsBox(int currentPlayer) {
		cardsBox.getChildren().clear();
		playersBox.getChildren().clear();
		initializeCardsBox(currentPlayer);
	}
	
	//end tournament
	private void endTournament(int currentPlayer) { 
		InformationAlert.display("Messaggio informativo","Congratulazioni "+actualGamePlayersNames.get(currentPlayer)+", hai vinto il torneo!");
		assignScore(actualGamePlayersNames.get(currentPlayer));
		tournamentBracketController.setWinner(actualGamePlayersNames.get(currentPlayer));
		deleteGameFromGamesDatasFile();
		deleteSerializationFile();
		disableButtons();
		showLeaderboard();
		
	}
	
	//end current game of tournament
	private void endCurrentGame(int currentPlayer) {
		String actualWinner=actualGamePlayersNames.get(currentPlayer);
		InformationAlert.display("Messaggio informativo","Congratulazioni "+actualWinner+", hai vinto la partita e sei passato alla fase successiva!");
		
		TournamentPhase phase=(tournament.getTournamentPhase().equals(TournamentPhase.QUARTI) ? TournamentPhase.SEMIFINALI : TournamentPhase.FINALE );
		tournamentBracketController.setPlayer(actualWinner, tournament.getGameNumber(), phase);
		tournament.switchGame();
		setLatestPlayedCard(null);
		
		actualGamePlayersNames=tournament.getActualGamePlayersNames();
		refreshCardsBox(tournament.getCurrentPlayer());
	}
	
	//disable all butttons
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
	
	//save datas on file
	public void serialize(String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            
            out.writeObject(tournament);
       
            Alert saveAlert = new Alert(AlertType.INFORMATION);
            saveAlert.setHeaderText(null);
            saveAlert.setContentText("Progressi salvati correttamente!");
            saveAlert.showAndWait();
        } catch (IOException e) {
        	showErrorMessage("Si è verificato un errore nel salvataggio:", "Riprova più tardi!");
        	e.printStackTrace();
        }
    }
    
	//deserialize TournamentController
    public Tournament deserialize(String filename) {
       Tournament tournament = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn)){
            tournament = (Tournament) in.readObject();
            Alert loadingProgressAlert = new Alert(AlertType.INFORMATION);
            loadingProgressAlert.setHeaderText(null);
            loadingProgressAlert.setContentText("Progressi caricati correttamente!");
            loadingProgressAlert.showAndWait();
           
        } catch (IOException | ClassNotFoundException e) {
        	showErrorMessage("Si è verificato un errore nel caricamento dei dati salvati:", "Riprova più tardi!");
     	   e.printStackTrace();
        }
        return tournament;
    }
    
    //assign score to normal player 
    private void assignScore(String currentPlayerName) {
    	if(currentPlayerName!="bot")
    		tournament.getLeaderboard().increaseScore(currentPlayerName);
    }
    
  //window closing management 
   public void closeWindowEvent(WindowEvent event) {
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
   
   //create leaderboard view 
   public TableView<LeaderboardData> getLeaderboard() {
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
   
   
   public void save(ActionEvent event) {
  	   serialize("./Files/ConfigurationFiles/"+gameCode+".ser");
   }		
  
   //quit game without save
   public void quit(ActionEvent event) {
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setGraphic(null);
		alert.setHeaderText("Stai per uscire dalla partita senza salvare i progressi!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			
			Stage stage = (Stage) ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
		    Parent root;
			try {
				root = FXMLLoader.load(new File("FXML/home.fxml").toURI().toURL());
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
				e.printStackTrace();
			}
			backgroundTrack.stop();
		}	
   }	
   
   public void saveAndQuit(ActionEvent event) {
		String serializationFile="./Files/ConfigurationFiles/"+gameCode+".ser";
		serialize(serializationFile);
		Alert alert=new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setGraphic(null);
		alert.setHeaderText("Stai per uscire dalla partita!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			
			Stage stage = (Stage) ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
	        Parent root;
			try {
				root = FXMLLoader.load(new File("FXML/home.fxml").toURI().toURL());
				 Scene scene = new Scene(root);
				 stage.setScene(scene);
				 stage.show();
			} catch (IOException e) {
				showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
				e.printStackTrace();
			}
			backgroundTrack.stop();
		}
   }
   
   public void showLeaderboard(ActionEvent event){
	   VBox vbox = new VBox(getLeaderboard());
       Scene scene = new Scene(vbox, 300, 200);
       scene.getStylesheets().add("application/game_playing/LeaderboardStyle.css");
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
       scene.getStylesheets().add("application/game_playing/LeaderboardStyle.css");
       Stage popupLeaderboard = new Stage();
       popupLeaderboard.setResizable(false);
       popupLeaderboard.initModality(Modality.APPLICATION_MODAL);
       popupLeaderboard.setTitle("Leaderboard");
       popupLeaderboard.setScene(scene);
       popupLeaderboard.show();
   }
   
   public void showRules(ActionEvent event){
		try {
			FXMLLoader loader = new FXMLLoader(new File("FXML/Rules.fxml").toURI().toURL());
	        Parent root = loader.load();
			
			RulesController controller = loader.getController();
	 		controller.hideHomeButton(); //hide home button in the rules stage
	 		
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Regolamento");
			popupStage.setScene(new Scene(root));
			popupStage.showAndWait();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
			e.printStackTrace();
		}
  }
   
   public  ObservableList<LeaderboardData> getDataFromLeaderboardFile() {
	   ObservableList<LeaderboardData> data = FXCollections.observableArrayList();
	   File leaderboardFile=new File("./Files/ConfigurationFiles/"+adminUsername+"TournamentsLeaderboard.csv");
	   try (Scanner scan=new Scanner(leaderboardFile)){
		   int position=1;
		   while(scan.hasNextLine()){
			   String[] line=scan.nextLine().split(",");
			   String name=line[0];
			   int score=Integer.parseInt(line[1]);
			   data.add(new LeaderboardData(position,name,score));
			   position++;
		   }
 
       } catch (FileNotFoundException e) {
    	   showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
    	   e.printStackTrace();
       }
	   return data;
   }
   
   public void getCharacterInfos(int player) {
	   Character character=tournament.getPlayer(player).getCharacter();
	   ImageView chImage = new ImageView(new Image(getClass().getResourceAsStream("CharactersCardsImages/"+character.getName()+".png")));
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
	   lifeLabel.setTextFill(Color.WHITE); 
	   lifeLabel.setFont(Font.font("System", 14)); 
	   // overlay label on progress bar
       StackPane lifePane = new StackPane();
       lifePane.getChildren().addAll(lifeBar, lifeLabel);
  
       // set label position 
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
		  attackLabel.setTextFill(Color.WHITE); 
		  attackLabel.setFont(Font.font("System", 14)); 

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
	   alert.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
	   alert.setTitle("Personaggio");
	   alert.getButtonTypes().remove(ButtonType.OK);
	   alert.getButtonTypes().add(ButtonType.CLOSE);
	   alert.getDialogPane().setContent(box);
	   alert.showAndWait();
   }
   
   private void initTournamentBracketScene() {
	   File fxmlFile = new File("FXML/TournamentBracket.fxml");
	
		try {
			tournamentBracketLoader = new FXMLLoader(fxmlFile.toURI().toURL());
			tournamentBracketScene=tournamentBracketLoader.load();
		} catch (IOException e) {
			showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
	    	e.printStackTrace();	
		}
		
		tournamentBracketController=tournamentBracketLoader.getController();
		
		bracketScene=new Scene(tournamentBracketScene);
		bracketStage=new Stage();
		bracketStage.setResizable(false);
		bracketStage.initModality(Modality.APPLICATION_MODAL);
		bracketStage.initOwner(primaryStage);
		bracketStage.setTitle("Avanzamento Torneo");
		bracketStage.setScene(bracketScene);
	
	}
   
   private void loadTournamentBracketProgress() {
		for(int i=0;i<tournament.getSemifinalists().size();i++) {
			tournamentBracketController.setPlayer(tournament.getSemifinalists().get(i), i+1, TournamentPhase.SEMIFINALI);
		}
		for(int i=0;i<tournament.getFinalists().size();i++) {
			tournamentBracketController.setPlayer(tournament.getFinalists().get(i), i+1, TournamentPhase.FINALE);
		}
	}
	
	private void setBracketButtonImage() {
		 ImageView bracketImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/bracket.png")));
		 bracketImg.setFitWidth(50);
		 bracketImg.setFitHeight(50);
		 tournamentBracketButton.setGraphic(bracketImg);	
	}		
   
   public void showTournamentBracket(ActionEvent event) {
	   bracketStage.showAndWait();
   }
 
}