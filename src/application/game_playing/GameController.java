package application.game_playing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import cards.Card;
import cards.WeaponCard;
import cards.statics.StaticCard;
import game.ClassicGame;
import game.Game;
import game.InformationAlert;
import game.Tournament;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import leaderboard.LeaderboardData;
import player.Bot;
import player.Player;

public abstract class GameController implements GameControllerInterface{
	protected String gameCode;
	protected String  adminUsername;

	protected int currentPlayer;
	protected int actualNumberOfPlayers;
	protected ToggleGroup group;
	protected ArrayList<ToggleButton> currentPlayerHand;
	
	protected Clip backgroundTrack;
	protected Clip cardSound;
	
	protected Stage primaryStage;
	
	
	@FXML protected ScrollPane cardsScroller;
	@FXML protected Label playerUsernameLabel;
	@FXML protected Label turnLabel;
	@FXML protected Button drawCardButton;
	@FXML protected Button submitCardButton;
	@FXML protected Button discardCardButton;
	@FXML protected Button submitPlayerButton;
	@FXML protected Button characterInfosButton;
	@FXML protected Button equipedWeaponButton;
	@FXML protected Button boardInfosButton;
	@FXML protected HBox cardsBox;
	@FXML protected VBox infoBox;
	@FXML protected VBox gameButtonsBox;
	@FXML protected VBox nameTurnBox;
	@FXML protected HBox playersBox;
	@FXML protected MenuButton menu;
	@FXML protected Pane deckPane;
	@FXML protected Pane latestPlayedCardPane;
	@FXML protected AnchorPane backGround;
	@FXML protected ToggleButton volumeButton;
	
	
	public abstract void initializeCardsBox(int currentPlayer);
	public abstract void initializePlayersBox();
	public abstract void setBindings();
	public abstract void seeEquipedWeapon(ActionEvent event) ;
	public abstract void seeCharacterInfos(ActionEvent event) ;
	public abstract void seeBoard(ActionEvent event) ;
	public abstract void drawCard(ActionEvent event) ;
	public abstract  void discardCard(ActionEvent event);;
	public abstract void submitCard(ActionEvent event);;
	public abstract void submitPlayer(ActionEvent event);;
	public abstract void useBotRoutine();
	public abstract void checkCurrentPlayerElimination(int targetPlayer);
	public abstract void checkConcurrentElimination() ;
	public abstract void updateCurrentPlayer(int currentPlayer);
	public abstract void serialize(String filename);
	public abstract void closeWindowEvent(WindowEvent event);;
	public abstract TableView<LeaderboardData> getLeaderboard();
	public abstract void save(ActionEvent event) ;
	public abstract void quit(ActionEvent event);
	public abstract void saveAndQuit(ActionEvent event);;
	public abstract void showLeaderboard(ActionEvent event) ;
	public abstract  ObservableList<LeaderboardData> getDataFromLeaderboardFile();
	public abstract void getCharacterInfos(int player);
	
	
	
	//setting game code to initialize classic game
	public void setGameCode(String code) { 
		gameCode=code;
	}
	
	//setting admin username to initialize classic game
	public void setAdminUsername(String name) { //metodo che viene chiamato dal playerController per settare l'adminUsername
		adminUsername=name;
	}
	
	protected void addToCardsBox(ToggleButton btn) { //aggiunge una determinata carta/bottone dalla UI
		currentPlayerHand.add(btn);
		group.getToggles().add(btn);	
        btn.setPrefHeight(215);
        btn.setPrefWidth(145);
        setCardImage(btn);
		cardsBox.getChildren().add(btn);
	}
	
	protected void removeFromCardsBox(ToggleButton btn) { //rimuove una determinata carta/bottone dalla UI
		currentPlayerHand.remove(btn);
		cardsBox.getChildren().remove(btn);
		group.getToggles().remove(btn);
	}
	
	//showing latest played or discarded card
	protected void setLatestPlayedCard(Card c) {
		latestPlayedCardPane.getChildren().clear();
		ImageView latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+c.getName().replaceAll("\\s+", "")+".png")));
		latestPlayedCard.setFitHeight(200);
		latestPlayedCard.setFitWidth(140);
		latestPlayedCardPane.getChildren().add(latestPlayedCard);
	}
	
	private void setMenuButtonStyle() {
	   menu.setLayoutX(primaryStage.getX());
       menu.setLayoutY(primaryStage.getY()+20);
       ImageView menuImg=new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Menu1.png")));
       menuImg.setFitWidth(menu.getPrefWidth()); 
       menuImg.setFitHeight(menu.getPrefHeight());
       menu.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       menu.setGraphic(menuImg);
	}
	
	protected void deleteGameFromGamesDatasFile() {
	   File file=new File("./Files/ConfigurationFiles/GamesDatas.csv");
	   ArrayList<String> datas=new ArrayList<String>();
	   try ( Scanner scan=new Scanner(file);
			 PrintWriter pw=new PrintWriter(file)){
		  
		   while(scan.hasNextLine()) {
			   String line=scan.nextLine();
			   String[] splittedLine=line.split(",");
			   if(!splittedLine[2].equals(gameCode)) 
				   datas.add(line);
		   }
		   
		   for(String line:datas)
			   pw.println(line);
	   }
	   catch(IOException e) {
		   Alert alert=new Alert(AlertType.ERROR);
    	   alert.setHeaderText("Si è verificato un errore:");
    	   alert.setContentText("Riprova più tardi!");
    	   alert.showAndWait();
	   }
   }
	   
	protected void deleteSerializationFile() {
	   File serializationFile= new File("./Files/ConfigurationFiles/"+gameCode+".ser");
		   if(serializationFile.exists())
			   serializationFile.delete();
	}		
	   
	protected void setCardImage(ToggleButton btn) {
	
       Image icon = new Image(getClass().getResourceAsStream("CardsImages/"+btn.getText().replaceAll("\\s+", "")+".png"));
       ImageView iconView = new ImageView(icon);
       iconView.setFitWidth(btn.getPrefWidth()); 
       iconView.setFitHeight(btn.getPrefHeight());
       
       btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       btn.setGraphic(iconView);  
       
       setCardStyle(btn);
	}	
	   
	protected void setCardStyle(ToggleButton btn) {
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
	
	protected  boolean isSerialized(String filename) {
        File file = new File(filename);
        return file.exists();
    }
	
	protected void setSceneStyle() {
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
       
       nameTurnBox.setLayoutY(primaryStage.getY()+20);
       nameTurnBox.setLayoutX(gameButtonsBox.getLayoutX()-150);
      // nameTurnBox.setAlignment(Pos.TOP_RIGHT);
       
       ImageView deckImg=new ImageView(new Image(getClass().getResourceAsStream("CardsImages/Deck.png")));
       deckImg.setFitHeight(300);
       deckImg.setFitWidth(270);
       deckPane.getChildren().add(deckImg);
       
       AnchorPane.setTopAnchor(latestPlayedCardPane, (primaryStage.getHeight() - latestPlayedCardPane.getPrefHeight()) / 3);
       AnchorPane.setLeftAnchor(latestPlayedCardPane, (primaryStage.getWidth() - latestPlayedCardPane.getPrefWidth())/2);
     
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
    	   Alert alert=new Alert(AlertType.ERROR);
    	   alert.setHeaderText("File audio non supportato:");
    	   alert.setContentText("Riprova più tardi!");
    	   alert.showAndWait();
       } catch (IOException e) {
    	   Alert alert=new Alert(AlertType.ERROR);
    	   alert.setHeaderText("Si è verificato un errore:");
    	   alert.setContentText("Riprova più tardi!");
    	   alert.showAndWait();
       }catch(LineUnavailableException e) {
    	   Alert alert=new Alert(AlertType.ERROR);
    	   alert.setHeaderText("Si è verificato un errore:");
    	   alert.setContentText("Riprova più tardi!");
    	   alert.showAndWait();
       }
       
       setMenuButtonStyle();
       setButtonImages();
   }	
   
	 private void setButtonImages() {
		 ImageView chButtonImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Character.png")));
		 chButtonImg.setFitWidth(50);
		 chButtonImg.setFitHeight(50);
		 characterInfosButton.setGraphic(chButtonImg);
		 
		 ImageView equipedWeaponImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Weapon3.png")));
		 equipedWeaponImg.setFitWidth(50);
		 equipedWeaponImg.setFitHeight(50);
		 equipedWeaponButton.setGraphic(equipedWeaponImg);
		 
		 ImageView boardImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Board.png")));
		 boardImg.setFitWidth(50);
		 boardImg.setFitHeight(50);
		 boardInfosButton.setGraphic(boardImg);
		
		 ImageView discardImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/DiscardCard.png")));
		 discardImg.setFitWidth(50);
		 discardImg.setFitHeight(50);
		 discardCardButton.setGraphic(discardImg);
		 
		 ImageView drawImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/DrawCard.png")));
		 drawImg.setFitWidth(50);
		 drawImg.setFitHeight(50);
		 drawCardButton.setGraphic(drawImg);
		 
		 ImageView submitImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/PlayCard.png")));
		 submitImg.setFitWidth(50);
		 submitImg.setFitHeight(50);
		 submitCardButton.setGraphic(submitImg);
		 
		 ImageView nextPlayerImg= new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/NextPlayer.png")));
		 nextPlayerImg.setFitWidth(50);
		 nextPlayerImg.setFitHeight(50);
		 submitPlayerButton.setGraphic(nextPlayerImg);
		 
		 volumeButton.setLayoutX(menu.getLayoutX()+60);
		 volumeButton.setLayoutY(menu.getLayoutY()-10);
		 ImageView volumeOnImage = new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/VolumeOn.png")));
		 volumeOnImage.setFitWidth(35);
		 volumeOnImage.setFitHeight(35);
		 ImageView volumeOffImage = new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/VolumeOff.png")));
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
	   }
	
	 protected void showBoard(StaticCard[] board) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Board");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.getDialogPane().getStyleClass().add("game-alert");
		alert.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		
		String staticCard1=(board[0]==null?"Vuoto":board[0].getName().replaceAll("\\s+", ""));
		String staticCard2=(board[1]==null?"Vuoto":board[1].getName().replaceAll("\\s+", ""));
		ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+staticCard1+".png")));
	    ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+staticCard2+".png")));
	    
	    imageView1.setFitWidth(280);
        imageView1.setFitHeight(350);
        imageView2.setFitWidth(280);
        imageView2.setFitHeight(350);
        
        HBox boardBox = new HBox(10);
        boardBox.getChildren().addAll(imageView1, imageView2);
        
        alert.getDialogPane().setContent(boardBox);
		alert.showAndWait();	
	 }
	
	 protected void showEquipedWeapon(WeaponCard wc) {
	 	String fileName=(wc==null?"Vuoto.png":wc.getName().replaceAll("\\s+", "")+".png");
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Arma Equipaggiata");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.getDialogPane().getStyleClass().add("game-alert");
		alert.getDialogPane().getScene().getStylesheets().add("application/game_playing/GameAlertStyle.css");
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		
		ImageView weaponImage = new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+fileName)));
		weaponImage.setFitWidth(280);
		weaponImage.setFitHeight(350);	
		
		alert.getDialogPane().setContent(weaponImage);
		alert.getDialogPane().setPadding(new Insets(10));
		alert.showAndWait();
	 }
}
