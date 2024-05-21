/*TODO
 * controllo errori/provare il gioco
 * gestione eccezioni
 * presentazione
 * commenti
 * riorganizzazione packages
 * cpire perchè quando esco e rientro scazza
 * fare .jar
 */
package application.game_playing;
import cards.*;
import cards.actions.*;
import cards.events.*;
import cards.statics.*;
import cards.characters.*;
import cards.characters.Character;
import game.GameType;
import game.InformationAlert;
import game.ClassicGame;
import leaderboard.Leaderboard;
import game.NoWinnerException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import leaderboard.LeaderboardData;
import player.Bot;
import player.Player;

import java.beans.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class ClassicGameController extends GameController implements Initializable {
	
	private ClassicGame game;
	private ArrayList<String> players;
	
	//init or load game
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//delay the scene loading(waiting for setGameCode() and setAdminUsername()
		Platform.runLater(() -> {
			if(isSerialized("./Files/ConfigurationFiles/"+gameCode+".ser")){
				game=deserialize("./Files/ConfigurationFiles/"+gameCode+".ser");
				currentPlayer=game.getCurrentPlayer();
				game.initializeProperties();
				}
			else {
				game=new ClassicGame(gameCode,adminUsername);
				currentPlayer=0;
			}
			// init hand
			currentPlayerHand=new ArrayList<ToggleButton>();
			group=new ToggleGroup();
			initializeCardsBox(currentPlayer);
			
			//init stage
			primaryStage= (Stage) drawCardButton.getScene().getWindow();
			//manage window closing
			primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);				
		    primaryStage.setMaximized(true);
		    
		    Card latest=(game.getDeck().getStockpile().size()==0?null:game.getDeck().getStockpile().getLast());
		    String latestName=(latest==null?"Vuoto":latest.getName().replaceAll("\\s+", ""));
	     
		    ImageView latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+latestName+".png")));
		    latestPlayedCard.setFitHeight(200);
		    latestPlayedCard.setFitWidth(140);
		    latestPlayedCardPane.getChildren().add(latestPlayedCard);
		    
		    setSceneStyle();
	    });  
	}
	
	//init current player hand GUI
	public void initializeCardsBox(int currentPlayer) {
		currentPlayerHand.clear();
		players=game.getPlayersNames(); 
		actualNumberOfPlayers=game.getNOfPlayers();
		turnLabel.setText(game.getTurn()+"° turno");
		playerUsernameLabel.setText("Username:"+game.getPlayer(currentPlayer).getUsername());
		group.getToggles().clear();
		for(int i=0;i<game.getPlayer(currentPlayer).getHand().size();i++) {
			Card card=game.getPlayer(currentPlayer).getHand().get(i);
			ToggleButton btn=new ToggleButton(card.getName());
			addToCardsBox(btn);
		}
		setBindings();
		initializePlayersBox();
		
		//show other players actions on current player
		String toDisplay=game.getActionMessage(currentPlayer);
		if(toDisplay.length()>0)
			InformationAlert.display("Messaggio informativo",toDisplay );
	}
	
	//init other players information
	public void initializePlayersBox() {
		actualNumberOfPlayers=game.getNOfPlayers();
		
		for (int i=0;i<actualNumberOfPlayers;i++) {
			if(i!=currentPlayer) {
				Player player=game.getPlayer(i);
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
				moreInfos.setOnAction(e -> getCharacterInfos(players.indexOf(playerName.getText())));
				
				hbox.getChildren().addAll(playerName,moreInfos);
				playerBox.getChildren().addAll(chImage,hbox);
				moreInfos.setPrefWidth(playerBox.getPrefWidth());
				playersBox.getChildren().add(playerBox);
			}
		}
	}
	
	//set bindings to disable buttons
	public void setBindings() { 
		SimpleBooleanProperty hasDrawed=game.getHasDrawed();
		SimpleBooleanProperty hasAttacked=game.getHasAttacked();
		SimpleBooleanProperty hasDiscarded=game.getHasDiscarded();
		
		SimpleBooleanProperty isSelectedAttackCard=new SimpleBooleanProperty(false);
		SimpleBooleanProperty isSelectedEventOrActionCard=new SimpleBooleanProperty(false);
		SimpleBooleanProperty isFirstTurn=new SimpleBooleanProperty(game.getTurn()==1);
		SimpleBooleanProperty isSelected=new SimpleBooleanProperty(false);
		
		isSelected.bind(group.selectedToggleProperty().isNull());
		
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	isSelectedAttackCard.bind(((ToggleButton)group.getSelectedToggle()).textProperty().isEqualTo("Attacco"));
            	 
            	ArrayList<Card> hand=game.getPlayer(currentPlayer).getHand();
            	ToggleButton btn=(ToggleButton) group.getSelectedToggle();
            	int cardIndex=currentPlayerHand.indexOf(btn);
            	Card c=hand.get(cardIndex);
            	SimpleBooleanProperty isActionOrEventCard=new SimpleBooleanProperty(c instanceof ActionCard || c instanceof EventCard);
            	isSelectedEventOrActionCard.bind(isActionOrEventCard);
            }	
        });
		
    	drawCardButton.disableProperty().bind(hasDrawed); 
		discardCardButton.disableProperty().bind(hasDiscarded.or(isSelected)); 
		submitCardButton.disableProperty().bind(isSelected.or(hasAttacked.and(isSelectedAttackCard)).or(isFirstTurn.and(isSelectedEventOrActionCard)));
	}
	
	//show current player's  board
	public void seeBoard(ActionEvent event){ 
		StaticCard[] board=game.getPlayer(currentPlayer).getBoard();
		showBoard(board);
	}
	
	//show current player character's info
	public void seeCharacterInfos(ActionEvent event){//permette di vedere le informazioni relative al personaggio del giocatore corrente
		getCharacterInfos(currentPlayer);
	}
	
	//show current player's equiped weapon
	public void seeEquipedWeapon(ActionEvent event){
		WeaponCard wc=game.getPlayer(currentPlayer).getEquipedWeapon();
		showEquipedWeapon(wc);
	}
	
	//draw a card
	public void drawCard(ActionEvent event) {
		Card drawedCard=game.drawCard(currentPlayer);
		ToggleButton btn=new ToggleButton(drawedCard.getName());
		addToCardsBox(btn); 
	}
	
	//discard a card
	public void discardCard(ActionEvent event){ 
		ToggleButton btn=(ToggleButton) group.getSelectedToggle();
		setLatestPlayedCard(game.getPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)));
		game.discardCard(currentPlayer,currentPlayerHand.indexOf(btn));
		removeFromCardsBox(btn);
	}
	
	//submit a card
	public void submitCard(ActionEvent event){ 
		ToggleButton btn=(ToggleButton)group.getSelectedToggle(); //selected toggleButton (card)
		Card submittedCard=game.getPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)); // selcted card
		int submittedCardIndex=currentPlayerHand.indexOf(btn); 
		ArrayList<String> toAttack=new ArrayList<String>(); //players to attack
		toAttack.addAll(players);
		toAttack.remove(currentPlayer);
		int targetPlayer=0;
		if(submittedCard instanceof ActionCard ) {
			
			if(submittedCard instanceof HealingPotionCard || submittedCard instanceof SauronEyeCard ) { 
				game.submitActionCard(submittedCardIndex, currentPlayer,0);
				 removeFromCardsBox(btn);
			}
				
			else {
				ChoiceDialog<String> dialog = new ChoiceDialog<>(toAttack.get(0),toAttack);
				dialog.setTitle("Selezione");
				dialog.setGraphic(null);
				dialog.getDialogPane().getStyleClass().add("game-alert");
				dialog.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
		        dialog.setHeaderText("Seleziona un giocatore:");
		        Optional<String> result = dialog.showAndWait();
		        targetPlayer=players.indexOf(dialog.getSelectedItem());
		       
		        if(result.isPresent() || result.get()!=null ) { 
		        	game.submitActionCard(submittedCardIndex, currentPlayer,targetPlayer);
		        	 removeFromCardsBox(btn);
		      
		        if(submittedCard instanceof BoardingCard) // in questo caso aggiorno la mano per vedere la carta rubata dalla mano dell'avversario selezionato
		        	refreshCardsBox(currentPlayer);
		        }	
		        
			}
			setLatestPlayedCard(submittedCard);
		}
		
		else if(submittedCard instanceof EventCard) {
			
			if(submittedCard instanceof MiracleCard) {
				game.submitEventCard(submittedCardIndex, currentPlayer,0); // stesso caso delle actionCard HealingPotion e SuronEye
				 removeFromCardsBox(btn);
			}
			else {
				ChoiceDialog<String> dialog = new ChoiceDialog<>(toAttack.get(0),toAttack);
				dialog.setTitle("Selezione");
				dialog.setGraphic(null);
				dialog.getDialogPane().getStyleClass().add("game-alert");
				dialog.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
		        dialog.setHeaderText("Seleziona un giocatore:");
		        Optional<String> result = dialog.showAndWait();
		        targetPlayer=players.indexOf(dialog.getSelectedItem());
		        if(result.isPresent() || result.get()!=null ) {
		        	game.submitEventCard(submittedCardIndex, currentPlayer,targetPlayer);
		        	removeFromCardsBox(btn);
		        }
		        if(submittedCard instanceof IdentityTheftCard) {
		        	playersBox.getChildren().clear();
		        	initializePlayersBox();
				}
			}
			setLatestPlayedCard(submittedCard);
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
		
		checkElimination(targetPlayer);
		checkCurrentPlayerElimination(targetPlayer);
		
		if(game.isGameOver()) 
			endGame(currentPlayer);
	}
	
	//submit the turn the next player
	public void submitPlayer(ActionEvent event){ //passa la giocata al prossimo giocatore
		updateCurrentPlayer(currentPlayer);
		game.setHasAttacked(false);
		game.setHasDiscarded(false);
		game.setHasDrawed(false);
	}
	
	//checks if previous players have been eliminated and update the currentPlayer
	public void checkElimination(int targetPlayer) {
		if(players.size()<actualNumberOfPlayers) {
			playersBox.getChildren().clear();
			initializePlayersBox();
			
			if(targetPlayer<currentPlayer) {
				currentPlayer=game.getPlayersNames().indexOf(playerUsernameLabel.getText().substring(9));
				game.setCurrentPlayer(currentPlayer);
				actualNumberOfPlayers=game.getNOfPlayers();
				playersBox.getChildren().clear();
				initializePlayersBox();
			}
		}
	}
	
	 // check if currentPlayer has been eliminated due to a targetPlayer staticCard
	public void checkCurrentPlayerElimination(int targetPlayer) {
		if(game.getPlayer(currentPlayer).getCharacter().getCurrentLife()<=0) {
			
			Player target =game.getPlayer(targetPlayer);
			String message="";
			
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
			
			if(!(game.getPlayer(currentPlayer) instanceof Bot)) 
				InformationAlert.display("Messaggio informativo",message);
			
			game.eliminatePlayer(currentPlayer);
			
			checkConcurrentElimination();
		}
	}
	
	//check if 2 players eliminate themselves concurrently
	public  void checkConcurrentElimination() {
		if(currentPlayer==actualNumberOfPlayers-1 || players.size()<actualNumberOfPlayers) {
			
			// if the 2 eliminated players were the latest 2 remaining the game end in a draw
			if(players.size()==0) { 
				try {
					throw new NoWinnerException("Partita terminata in pareggio, non ci sono vincitori!");
				}catch(NoWinnerException exception) {
					InformationAlert.display("Messaggio informativo",exception.getMessage());
					deleteSerializationFile();
					disableButtons();	
				}
				deleteGameFromGamesDatasFile();
			}
			else
				updateCurrentPlayer(currentPlayer);
		}
		else
			refreshCardsBox(currentPlayer);
	}
	
	//check if the currentPlayer is a Bot
	private boolean isBot(int current) {
		if (game.getPlayer(current) instanceof Bot)
			return true;
		else
			return false;		
	}
	
	//bot actions
	public synchronized void useBotRoutine() {	    
		Bot bot =(Bot) game.getPlayer(currentPlayer);
		String botActionsMessage="Il bot ha eseguito le seguenti azioni:\n";
		//bot's board 
		StaticCard[] board=bot.getBoard();
		
		// 1°: draw a card;
		game.drawCard(currentPlayer);
		botActionsMessage=botActionsMessage+"-Pescato una carta;\n";
		
		//2° check if have an equipped weapon, if not find it in the hand and equip
		if(bot.getEquipedWeapon()==null) {
			for(Card c:bot.getHand())
				if(c instanceof WeaponCard) {
					botActionsMessage=botActionsMessage+"-Equipaggiato un'arma;\n";
					game.submitWeaponCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		//actions 3,4 e 5 only if turn != 1
		if(game.getTurn()!=1) {
			//3° use any card except action card
			for(Card c:bot.getHand())
				if(c instanceof ActionCard && !(c instanceof AttackCard)) {
					int targetPlayer=(currentPlayer==game.getNOfPlayers()-1?0:currentPlayer+1);
					
					if(c instanceof HealingPotionCard || c instanceof SauronEyeCard)
						botActionsMessage=botActionsMessage+"-Usato la carta "+c.getName()+";\n";
					else 
						botActionsMessage=botActionsMessage+"-Usato la carta "+c.getName()+" su "+game.getPlayer(targetPlayer).getUsername()+";\n";
					
					setLatestPlayedCard(c);
					game.submitActionCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
					checkElimination(targetPlayer);
					checkCurrentPlayerElimination(targetPlayer);
					//check if there is only 1 player left
					if(game.isGameOver()) {
						InformationAlert.display("Messaggio informativo",botActionsMessage);
						endGame(currentPlayer);
						return;
					}
					break;
				}
			
			//4° check if have an attack card and use it
			if(bot.hasAttackCard()) {
				//choose a player to attack
				int targetPlayer=(currentPlayer==game.getNOfPlayers()-1?0:currentPlayer+1);
				//search for card position
				for(Card c:bot.getHand())
					if(c instanceof AttackCard) {
						setLatestPlayedCard(c);
						botActionsMessage=botActionsMessage+"-Attaccato "+game.getPlayer(targetPlayer).getUsername()+";\n";
						game.submitActionCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						break;
					}
				checkElimination(targetPlayer);
				checkCurrentPlayerElimination(targetPlayer);
				//check if there is only 1 player left
				if(game.isGameOver()) {
					InformationAlert.display("Messaggio informativo",botActionsMessage);
					endGame(currentPlayer);
					return;
				}
			}
			
			//5° event card management, use it if you have one(miracle card only if  <= life points)
			if(bot.hasEventCard()) {
				int targetPlayer=(currentPlayer==game.getNOfPlayers()-1?0:currentPlayer+1);
				for(Card c:bot.getHand()) {
					if(c instanceof DoomsdayCard) {
						botActionsMessage=botActionsMessage+"-Eliminato, usando la carta Giorno del Giudizio, "+game.getPlayer(targetPlayer).getUsername()+";\n";
						game.submitEventCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						checkElimination(targetPlayer);
						checkCurrentPlayerElimination(targetPlayer);
						//check if there is only 1 player left
						if(game.isGameOver()) {
							InformationAlert.display("Messaggio informativo",botActionsMessage);
							endGame(currentPlayer);
							return;
						}
						break;
					}
					if(c instanceof IdentityTheftCard) {
						botActionsMessage=botActionsMessage+"-Cambiato personaggio usando la carta Furto d'identità su "+game.getPlayer(targetPlayer).getUsername()+";\n";
						game.submitEventCard(bot.getHand().indexOf(c),currentPlayer,targetPlayer);
						break;
					}
					if(c instanceof MiracleCard && bot.getCharacter().getCurrentLife()<=30) {
						botActionsMessage=botActionsMessage+"-Recuperato tutti i punti vita usando la carta Miracolo;\n";
						game.submitEventCard(bot.getHand().indexOf(c),currentPlayer,currentPlayer);
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
					game.submitStaticCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		if(board[1]==null) {
			for(Card c:bot.getHand())
				if(c instanceof AztecCurseCard || c instanceof RingCard || c instanceof BlackWidowsPoisonCard) {
					game.submitStaticCard(bot.getHand().indexOf(c),currentPlayer);
					break;
				}
		}
		
		//7° discard a card
		if(!bot.getHand().isEmpty()) {
			int toDiscard=(int)(Math.random()*bot.getHand().size());
			botActionsMessage=botActionsMessage+"-Scartato una carta;\n";
			setLatestPlayedCard(bot.getHand().get(toDiscard));
			game.discardCard(currentPlayer,toDiscard);
		}
		
		//show bot actions 
		InformationAlert.display("Messaggio informativo",botActionsMessage);
		
		//8° update turn
		updateCurrentPlayer(currentPlayer);  
	}
	
	//update current player
	public void updateCurrentPlayer(int currentPlayer) {
		actualNumberOfPlayers=game.getNOfPlayers();
		if(currentPlayer>=actualNumberOfPlayers-1) {	
			this.currentPlayer=0;
			game.changeTurn();
		}
		else
			this.currentPlayer++;
		
		game.setCurrentPlayer(this.currentPlayer);
		
		if(isBot(this.currentPlayer)) {
		   InformationAlert.display("Messaggio informativo", "Sta giocando "+game.getPlayer(this.currentPlayer).getUsername()+"...");
	       useBotRoutine();
		}
		else {
			refreshCardsBox(this.currentPlayer);
		}
	}
	
	//refresh user interface
	private void refreshCardsBox(int currentPlayer) { 
		cardsBox.getChildren().clear();
		playersBox.getChildren().clear();
		initializeCardsBox(currentPlayer);
	}
	
	//end game
	private void endGame(int currentPlayer) { 
		InformationAlert.display("Messaggio informativo","Congratulazioni "+players.get(currentPlayer)+", hai vinto la partita!");
		assignScore(currentPlayer);
		deleteGameFromGamesDatasFile();
		deleteSerializationFile();
		disableButtons();
		backgroundTrack.stop();
		showLeaderboard();
	}
	
	//disable all butttons
	private void disableButtons() {
		game.setHasAttacked(true);
		game.setHasDiscarded(true);
		game.setHasDrawed(true);

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
            
            out.writeObject(game);
            Alert saveAlert = new Alert(AlertType.INFORMATION);
            saveAlert.setHeaderText(null);
            saveAlert.setContentText("Progressi salvati correttamente!");
            saveAlert.showAndWait();
        } catch (IOException e) {
        	showErrorMessage("Si è verificato un errore nel salvataggio:", "Riprova più tardi!");
     	   e.printStackTrace();
        }
    }
	
    // Static method to deserialize  GameController
    public  ClassicGame deserialize(String filename) {
    	ClassicGame game = null;
        try ( FileInputStream fileIn = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn)){
    
            game = (ClassicGame) in.readObject();
            Alert loadingProgressAlert = new Alert(AlertType.INFORMATION);
            loadingProgressAlert.setHeaderText(null);
            loadingProgressAlert.setContentText("Progressi caricati correttamente!");
            loadingProgressAlert.showAndWait();
           
        } catch (IOException | ClassNotFoundException e) {
        	showErrorMessage("Si è verificato un errore nel caricamento dei dati salvati:", "Riprova più tardi!");
        	e.printStackTrace();
        }
        return game;
    }
    
    
    //assign score to normal player 
    private void assignScore(int currentPlayer) {
    	if(!(game.getPlayer(currentPlayer) instanceof Bot))
    		game.getLeaderboard().increaseScore(game.getPlayer(currentPlayer).getUsername());
    }
    
    //window closing management 
    public void closeWindowEvent(WindowEvent event) {
	   if(!game.isGameOver()) {
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
    public TableView<LeaderboardData>  getLeaderboard() {
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
   
    //show leaderboard
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
       popupLeaderboard.show();;
   }
   
   public  ObservableList<LeaderboardData> getDataFromLeaderboardFile() {
	   ObservableList<LeaderboardData> data = FXCollections.observableArrayList();
	   try (Scanner scan=new Scanner(new File("./Files/ConfigurationFiles/"+adminUsername+game.getGameType().toString()+"sLeaderboard.csv"))){
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
   
   public void save(ActionEvent event) {
	   	serialize("./Files/ConfigurationFiles/"+gameCode+".ser");
   }		
   
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
				root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
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
				root = FXMLLoader.load(new File("src/application/home.fxml").toURI().toURL());
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
   
   public void getCharacterInfos(int player) {
	   Character character=game.getPlayer(player).getCharacter();
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
	   lifeLabel.setTextFill(Color.BLACK); // Imposta il colore del testo a bianco
	   lifeLabel.setFont(Font.font("System", 14)); // Imposta il carattere a bold
	   // Sovrapponi la label sulla progress bar
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
		  int attackPower=game.getPlayer(currentPlayer).getAttackPower();
		 
		  ProgressBar attackBar = new ProgressBar(attackPower/24.0);
		  attackBar.setPrefWidth(123);
		  attackBar.setPrefHeight(30);
		  attackBar.setStyle("-fx-accent:orange;-fx-background-color:purple;-fx-border-color:purple;"+ "}");
		 
		  Label attackLabel = new Label(Integer.toString(attackPower));
		  attackLabel.setTextFill(Color.BLACK); // Imposta il colore del testo a bianco
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
	   alert.setTitle("Personaggio");
	   alert.setHeaderText(null);
	   alert.setGraphic(null);
	   alert.getDialogPane().getStyleClass().add("game-alert");
	   alert.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
	   alert.getButtonTypes().remove(ButtonType.OK);
	   alert.getButtonTypes().add(ButtonType.CLOSE);
	   alert.getDialogPane().setContent(box);
	   alert.showAndWait();
   }

}