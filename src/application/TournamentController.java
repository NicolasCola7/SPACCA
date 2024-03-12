package application;
import cards.*;
import cards.actions.ActionCard;
import cards.actions.BoardingCard;
import cards.actions.HealingPotionCard;
import cards.actions.SauronEyeCard;
import cards.characters.Character;
import cards.events.EventCard;
import cards.events.MiracleCard;
import cards.statics.StaticCard;
import game.Player;
import game.Tournament;
import game.TournamentPhase;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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


public class TournamentController implements Initializable{
	private String gameCode;
	private String  adminUsername;
	private Tournament tournament;
	private int currentPlayer;
	private SimpleBooleanProperty hasDrawed;
	private SimpleBooleanProperty hasAttacked;
	private SimpleBooleanProperty hasDiscarded;
	private SimpleBooleanProperty isSelected;
	private SimpleBooleanProperty isSelectedAttackCard;
	
	
	@FXML private Label playerUsernameLabel;
	@FXML private Label turnLabel;
	@FXML private Button drawCardButton;
	@FXML private Button submitCardButton;
	@FXML private Button discardCardButton;
	@FXML private Button submitPlayerButton;
	@FXML private Button characterInfosButton;
	@FXML private Button playersInfosButton;
	@FXML private Button boardInfosButton;
	@FXML private HBox cardsBox;
	@FXML private VBox infoBox;
	@FXML private MenuButton menu;
	
	private ToggleGroup group;
	private final Insets MARGIN =new Insets(10, 2, 10, 2); // sono le spaziature tra le carte
	private ArrayList<ToggleButton> currentPlayerHand;
	private ArrayList<String> actualGamePlayersNames;
	private Stage primaryStage;
	
	public void setGameCode(String code) { // metodo che viene chiamato dal playerController per settare il gameCode
		gameCode=code;
	}
	
	public void setAdminUsername(String name) { //metodo che viene chiamato dal playerController per settare l'adminUsername
		adminUsername=name;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // inizializza la partita 
		Platform.runLater(() -> {// metodo da capire meglio, serve a ritardare le istruzioni al suo interno dato che quando si passano dati da un altro controller (PlayerController in questo caso)  il metodo initialize viene eseguito prima dei metodi utilizzati nel controller che passa i dati,in guesto caso setGameCode() e setAdminUsername()
			if(isSerialized("./Files/ConfigurationFiles/"+gameCode+".ser")){
				tournament=deserialize("./Files/ConfigurationFiles/"+gameCode+".ser");
				currentPlayer=tournament.getCurrentPlayer();
				tournament.initializeProperties();
				initializeCardsBox(currentPlayer);
				}
			else {
				tournament=new Tournament(gameCode,adminUsername);
				currentPlayer=0;
				initializeCardsBox(currentPlayer);
			}
		
		    primaryStage = (Stage) drawCardButton.getScene().getWindow();
			primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
	    });  
	}
	private void initializeCardsBox(int currentPlayer) {// inizializza la UI del giocatore corrente
		currentPlayerHand=new ArrayList<ToggleButton>();
		actualGamePlayersNames=tournament.getActualGamePlayersNames();
		turnLabel.setText(tournament.getTurn()+"° turno");
		playerUsernameLabel.setText("Username:"+tournament.getPlayer(currentPlayer).getUsername());
		group=new ToggleGroup();
		for(int i=0;i<tournament.getPlayer(currentPlayer).getHand().size();i++) {
			Card card=tournament.getPlayer(currentPlayer).getHand().get(i);
			ToggleButton btn=new ToggleButton(card.getName());
			addToCardsBox(btn);
		}
		setBindings();	
	}
	private void setBindings() { // serve a fare in modo che i bottoni vengano disattivati e attivati  in determinate situazioni
		hasDrawed=tournament.getHasDrawed();
		hasAttacked=tournament.getHasAttacked();
		hasDiscarded=tournament.getHasDiscarded();
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
		submitCardButton.disableProperty().bind(isSelected.or(hasAttacked.and(isSelectedAttackCard)));//disattiva il bottone per giocare una carta se non c'è alcuna carta selezionata, oppure se il giocatore ha già attaccato e la carta selezionata è una carta attacco
	}
	
	public void seeBoard(ActionEvent event)throws IOException{ // permette di visualizzare le carte presenti nella board del giocatore corrrente
		StaticCard[] board=tournament.getPlayer(currentPlayer).getBoard();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informazione");
		alert.setHeaderText(null);
		String staticCard1=(board[0]==null?"vuoto":board[0].getName());
		String staticCard2=(board[1]==null?"vuoto":board[1].getName());
		alert.setContentText("Pos1:"+staticCard1+"\n"+
							 "Pos2:"+staticCard2);
		alert.showAndWait();	
	}
	
	public void seeCharacterInfos(ActionEvent event) throws IOException{//permette di vedere le informazioni relative al personaggio del giocatore corrente
		Character character=tournament.getPlayer(currentPlayer).getCharacter();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informazione");
		alert.setHeaderText(null);
		alert.setContentText("Personaggio:"+character.getName()+"\n"+
							 "Seme:"+character.getSeed()+"\n"+
							 "Attacco:"+character.getAttack()+"\n"+
							 "Arma equipaggiata:"+(tournament.getPlayer(currentPlayer).getEquipedWeapon()==null?"nessuna":tournament.getPlayer(currentPlayer).getEquipedWeapon().getName())+"\n"+
							 "Potenza d'attacco:"+tournament.getPlayer(currentPlayer).getAttackPower()+"\n"+
							 "Vita:"+character.getCurrentLife()+"\n"+
							 "Precisione:"+character.getCurrentPrecision()+"\n");
							 
		alert.showAndWait();
	}
	
	public void seePlayersInfos(ActionEvent event)throws IOException{ //serve a vedere informazioni (limitate) degli altri giocatori
		String toSee=(currentPlayer==0?actualGamePlayersNames.get(1):actualGamePlayersNames.get(0));
    	Player player=tournament.getPlayer(actualGamePlayersNames.indexOf(toSee));
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informazione");
		alert.setHeaderText(null);
		alert.setContentText("Nome:"+player.getUsername()+"\n"+
							"Personaggio:"+player.getCharacter().getName()+"\n"+
							"Vita rimanente:"+player.getCharacter().getCurrentLife());
		alert.showAndWait();
	}
	
	public void drawCard(ActionEvent event) throws IOException { //per pescare una carta 
		Card drawedCard=tournament.drawCard(currentPlayer);
		ToggleButton btn=new ToggleButton(drawedCard.getName());
		addToCardsBox(btn); // aggiunge la carta pescata alla UI del giocatore corrente
		
	}
	
	public void discardCard(ActionEvent event)throws IOException{ //per scartare una carta
		ToggleButton btn=(ToggleButton) group.getSelectedToggle();
		tournament.discardCard(currentPlayer,currentPlayerHand.indexOf(btn));
		removeFromCardsBox(btn);// rimuove la carta scartata dalla UI del giocatore corrente
		
	}

	public void submitCard(ActionEvent event)throws IOException{ // per giocare una carta
		ToggleButton btn=(ToggleButton)group.getSelectedToggle(); //ottengo il bottone selezionato
		Card submittedCard=tournament.getCurrentPlayersHand(currentPlayer).get(currentPlayerHand.indexOf(btn)); // ottengo la carta nella mano del giocatore corrispondente all'indice del bottone
		int submittedCardIndex=currentPlayerHand.indexOf(btn); // ottengo l'indice della carta nella mano del giocatore 
		String toAttack=actualGamePlayersNames.get(1-currentPlayer); // nome avversario
		int targetPlayer=0;
		if(submittedCard instanceof ActionCard ) {
			
			if(submittedCard instanceof HealingPotionCard || submittedCard instanceof SauronEyeCard ) { // in questo caso nel metodo passo 2 volte currentPlayer perchè per queste 2 carte non serve passare il target Player
				tournament.submitActionCard(submittedCardIndex, currentPlayer,currentPlayer);
				removeFromCardsBox(btn);
			}
				
			else {
		        targetPlayer=actualGamePlayersNames.indexOf(toAttack);
	        	tournament.submitActionCard(submittedCardIndex, currentPlayer,targetPlayer);
	        	removeFromCardsBox(btn);
		      
		        if(submittedCard instanceof BoardingCard) // in questo caso aggiorno la mano per vedere la carta rubata dalla mano dell'avversario selezionato
		        	refreshCardsBox(currentPlayer);
			}		
		}
		else if(submittedCard instanceof EventCard) {
			
			if(submittedCard instanceof MiracleCard) {
				tournament.submitEventCard(submittedCardIndex, currentPlayer,currentPlayer); // stesso caso delle actionCard HralingPotion e SuronEye
				 removeFromCardsBox(btn);
			}
			else {
				targetPlayer=actualGamePlayersNames.indexOf(toAttack);
				tournament.submitEventCard(submittedCardIndex, currentPlayer,targetPlayer);
		        removeFromCardsBox(btn);
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
		
		if(actualGamePlayersNames.size()==1 && currentPlayer==1) {//controllo che serve per quando viene eliminato un giocatore che in ordine di giocata è prima del giocatore corrente
			currentPlayer=0;
			tournament.setCurrentPlayer(currentPlayer);
		}
		
		if(tournament.getPlayer(currentPlayer).getCharacter().getCurrentLife()<=0) {
		 // caso in cui l'attaccate venga eliminato dal veleno di vedova nera o specchio o entrambi
				Alert alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				Player target =tournament.getPlayer(targetPlayer);
				if(target.hasEnchantedMirror() && target.hasBlackWidowsPoison()) {
					target.removeFromBoardInPosition(0);
					alert.setContentText("Il veleno di vedova nera e lo specchio ti hanno ucciso!Sei stato eliminato da "+target.getUsername()+".");
				}
				else if(target.hasEnchantedMirror() && !target.hasBlackWidowsPoison()) {
					target.removeFromBoardInPosition(0);
					alert.setContentText("Lo specchio ti ha ucciso!Sei stato eliminato da "+target.getUsername()+".");
				}
				else
					alert.setContentText("Il veleno di vedova nera ti ha ucciso!Sei stato eliminato da "+target.getUsername()+".");
				alert.showAndWait();
				
			tournament.eliminatePlayer(currentPlayer);
			currentPlayer=0;
			tournament.setCurrentPlayer(currentPlayer);
			
			if(tournament.getActualGamePlayers().size()==0) { //caso in cui i due giocatori si eliminino a vicenda
					ArrayList<String> latestTwo=tournament.getLatestTwoEliminated();
					alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Vi siete eliminati a vicenda, verrà lanciata una moneta per decretare il vincitore: se esce testa vince "+latestTwo.get(0)+", se esce croce "+latestTwo.get(1));
					alert.showAndWait();
					int winner=(int)(Math.random()+1);
					if (winner==0) {
						alert.setContentText("E' uscito TESTA");
						alert.showAndWait();
						tournament.eliminatePlayer(1);
					}
					else {
						alert.setContentText("E' uscito CROCE");
						alert.showAndWait();
						tournament.eliminatePlayer(0);
					}
			}
		}
		if(tournament.isTournamentGameOver()) //controlla se è rimasto solo un giocatore 
			endTournament(currentPlayer);
		
		else if(tournament.isActualGameOver()) {
			 endCurrentGame(currentPlayer);
		}
		
	}
	
	public void submitPlayer(ActionEvent event)throws IOException{ //passa la giocata al prossimo giocatore
		updateCurrentPlayer(currentPlayer);
		tournament.setHasAttacked(false);
		tournament.setHasDiscarded(false);
		tournament.setHasDrawed(false);
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
	
	private void updateCurrentPlayer(int currentPlayer) {
		if(currentPlayer==1)
			tournament.changeTurn();
		
		this.currentPlayer=1-currentPlayer;
		tournament.setCurrentPlayer(this.currentPlayer);
		refreshCardsBox(this.currentPlayer);
	}
	
	private void refreshCardsBox(int currentPlayer) { //aggiorna la UI
		group.getToggles().clear();
		cardsBox.getChildren().clear();
		currentPlayerHand.clear();
		initializeCardsBox(currentPlayer);
	}
	
	private void endTournament(int currentPlayer) { //termina il gioco
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Messaggio informativo");
		alert.setHeaderText(null);
		alert.setContentText("Congratulazioni "+actualGamePlayersNames.get(currentPlayer)+", hai vinto il torneo!");
		alert.showAndWait();
		assignScore(actualGamePlayersNames.get(currentPlayer));
		deleteGameFromGamesDatasFile();
		deleteSerializationFile();
		disableButtons();
		showLeaderboard();
		
	}
	private void endCurrentGame(int currentPlayer) {
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Messaggio informativo");
		alert.setHeaderText(null);
		alert.setContentText("Congratulazioni "+actualGamePlayersNames.get(currentPlayer)+", hai vinto la partita e sei passato alla fase successiva!");
		alert.showAndWait();
		tournament.switchGame();
		refreshCardsBox(tournament.getCurrentPlayer());
	}
	private void disableButtons() {
		tournament.setHasAttacked(true);
		tournament.setHasDiscarded(true);
		tournament.setHasDrawed(true);
		isSelected.unbind();
		isSelected.set(true);

		submitPlayerButton.disableProperty().set(true);
		characterInfosButton.disableProperty().set(true);
		playersInfosButton.disableProperty().set(true);
		boardInfosButton.disableProperty().set(true);
		menu.setDisable(true);
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
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per uscire dalla partita!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
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
		alert.setTitle("Logout");
		alert.setHeaderText("Stai per uscire dalla partita senza salvare i progressi!");
		alert.setContentText("Sei sicuro di voler continuare?");
		if(alert.showAndWait().get()==ButtonType.OK) {
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
       Stage popupLeaderboard = new Stage();
       popupLeaderboard.setTitle("Leaderboard");
       popupLeaderboard.setScene(scene);
       popupLeaderboard.show();
   }
   public void showLeaderboard() {
	   VBox vbox = new VBox(getLeaderboard());
       Scene scene = new Scene(vbox, 300, 200);
       Stage popupLeaderboard = new Stage();
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
}