package application.game_playing;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
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
