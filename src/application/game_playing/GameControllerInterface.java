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
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import leaderboard.LeaderboardData;

public interface GameControllerInterface {
	
	public void initializeCardsBox(int currentPlayer);
	public void initializePlayersBox();
	public  void setBindings();
	public  void seeEquipedWeapon(ActionEvent event) ;
	public  void seeCharacterInfos(ActionEvent event) ;
	public  void seeBoard(ActionEvent event) ;
	public  void drawCard(ActionEvent event) ;
	public   void discardCard(ActionEvent event);;
	public  void submitCard(ActionEvent event);;
	public  void submitPlayer(ActionEvent event);;
	public  void useBotRoutine();
	public   void checkCurrentPlayerElimination(int targetPlayer);
	public  void checkConcurrentElimination() ;
	public  void updateCurrentPlayer(int currentPlayer);
	public  void serialize(String filename);
	public  void closeWindowEvent(WindowEvent event);;
	public  TableView<LeaderboardData> getLeaderboard();
	public  void save(ActionEvent event) ;
	public  void quit(ActionEvent event);
	public  void saveAndQuit(ActionEvent event);;
	public  void showLeaderboard(ActionEvent event) ;
	public   ObservableList<LeaderboardData> getDataFromLeaderboardFile();
	public  void getCharacterInfos(int player);
	
}
