package application.game_playing;

import java.io.File;
import java.io.IOException;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
	public abstract void discardCard(ActionEvent event);;
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
	
	
	
	//set game code to initialize classic game
	public void setGameCode(String code) { 
		gameCode=code;
	}
	
	//set admin username to initialize classic game
	public void setAdminUsername(String name) { //method called by playerController to set adminUsername
		adminUsername=name;
	}
	
	//add card/button to UI
	protected void addToCardsBox(ToggleButton btn) { 
		currentPlayerHand.add(btn);
		group.getToggles().add(btn);	
        btn.setPrefHeight(215);
        btn.setPrefWidth(145);
        setCardImage(btn);
		cardsBox.getChildren().add(btn);
	}
	
	//remove card/button UI
	protected void removeFromCardsBox(ToggleButton btn) { 
		currentPlayerHand.remove(btn);
		cardsBox.getChildren().remove(btn);
		group.getToggles().remove(btn);
	}
	
	//show latest played or discarded card
	protected void setLatestPlayedCard(Card c) {
		latestPlayedCardPane.getChildren().clear();
		ImageView latestPlayedCard;
		if(c!=null)
				latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("CardsImages/"+c.getName().replaceAll("\\s+", "")+".png")));
		else 
			latestPlayedCard=new ImageView(new Image(getClass().getResourceAsStream("CardsImages/Vuoto.png")));
			
		latestPlayedCard.setFitHeight(200);
		latestPlayedCard.setFitWidth(140);
		latestPlayedCardPane.getChildren().add(latestPlayedCard);
	}
	
	//set save and close menu style
	private void setMenuButtonStyle() {
	   menu.setLayoutX(primaryStage.getX());
       menu.setLayoutY(primaryStage.getY()+20);
       ImageView menuImg=new ImageView(new Image(getClass().getResourceAsStream("GameButtonsImages/Menu1.png")));
       menuImg.setFitWidth(menu.getPrefWidth()); 
       menuImg.setFitHeight(menu.getPrefHeight());
       menu.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       menu.setGraphic(menuImg);
	}
	
	//delete game in filses
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
		   showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
    	   e.printStackTrace();
	   }
   }
	
	//delete serialization file
	protected void deleteSerializationFile() {
	   File serializationFile= new File("./Files/ConfigurationFiles/"+gameCode+".ser");
		   if(serializationFile.exists())
			   serializationFile.delete();
	}		
	
	//set cards image to the board
	protected void setCardImage(ToggleButton btn) {
	
       Image icon = new Image(getClass().getResourceAsStream("CardsImages/"+btn.getText().replaceAll("\\s+", "")+".png"));
       ImageView iconView = new ImageView(icon);
       iconView.setFitWidth(btn.getPrefWidth()); 
       iconView.setFitHeight(btn.getPrefHeight());
       
       btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       btn.setGraphic(iconView);  
       
       setCardStyle(btn);
	}	
	   
	//set card dimension and style
	protected void setCardStyle(ToggleButton btn) {
	   btn.setPadding(new Insets(10, 10, 10, 10));
	   btn.setStyle("-fx-background-color: transparent;");
	   //set different colors on mouse event
	   btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: orange;"));
	   btn.setOnMouseExited(e -> {
	       if (!btn.isFocused()) {
	    	   btn.setStyle("-fx-background-color: transparent;");
	       }
	   });
	   
	   //add listener to btn
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
	
	//set style of game board
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
    	   showErrorMessage("Si è verificato un errore nella riproduzione dell'audio:", "Riprova più tardi!");
    	   e.printStackTrace();
       } catch (IOException e) {
    	   showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
    	   e.printStackTrace();
       }catch(LineUnavailableException e) {
    	   showErrorMessage("Si è verificato un errore:", "Riprova più tardi!");
    	   e.printStackTrace();
       }
       
       setMenuButtonStyle();
       setButtonImages();
   }	
   
	//set buttons style, actions on volume button
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
	
	 //show board when button is clicked
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
	
	//show weapon when button is clicked
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
	
	 protected void showErrorMessage(String header, String content) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Errore");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
