package application.game_playing;

import game.TournamentPhase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
 
//controller of TournamentBracket schema
public class TournamentBracketController{
	@FXML private Label quarter1;
	@FXML private Label quarter2;
	@FXML private Label quarter3;
	@FXML private Label quarter4;
	@FXML private Label quarter5;
	@FXML private Label quarter6;
	@FXML private Label quarter7;
	@FXML private Label quarter8;
	
	@FXML private Label semi1;
	@FXML private Label semi2;
	@FXML private Label semi3;
	@FXML private Label semi4;
	
	@FXML private Label finalist1;
	@FXML private Label finalist2;
	
	@FXML private Label winner;
	
	public void setWinner(String name) {
		winner.setText(name);
	}
	
	public void setPlayer(String name, int playerNumber, TournamentPhase tournamentPhase) {
		if (tournamentPhase.equals(TournamentPhase.QUARTI))
			switch(playerNumber) {
			
			case 1:
				quarter1.setText(name);
				break;
			
			case 2:
				quarter2.setText(name);
				break;
			
			case 3:
				quarter3.setText(name);
				break;
			
			case 4:
				quarter4.setText(name);
				break;
			
			case 5:
				quarter5.setText(name);
				break;
			
			case 6:
				quarter6.setText(name);
				break;
			
			case 7:
				quarter7.setText(name);
				break;
			
			case 8:
				quarter8.setText(name);
				break;

		}
		else if(tournamentPhase.equals(TournamentPhase.SEMIFINALI))
			switch(playerNumber) {
			
			case 1:
				semi1.setText(name);
				break;
			
			
			case 2:
				semi2.setText(name);
				break;
			
			
			case 3:
				semi3.setText(name);
				break;
			
			
			case 4:
				semi4.setText(name);
				break;
		}
		else
			switch(playerNumber) {
			
			case 1:
				finalist1.setText(name);
				break;
			
			
			case 2:
				finalist2.setText(name);
				break;
		}
	}

}

