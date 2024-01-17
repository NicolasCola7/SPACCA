package cards;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
public class Player {

	private String username;
	private int position;
	private Character character;
	private StaticCard[] board;
	private int score;
	private WeaponCard equipedWeapon;
	private int attackPower;
	private ArrayList<Card> hand;
	
	public Player(String u,Character ch) {
		position = 0;
		score = 0;
		username = u;
		character=ch;
		attackPower=character.getAttack();
		hand=new ArrayList<Card>();
		board=new StaticCard[2];
	}
	public Character getCharacter() {
		return character;
	}
	public void setCharacter(Character c) {
		character=c;
	}
	public void setEquipedWeapon(WeaponCard w) {
		if(equipedWeapon.equals(null)) {
			equipedWeapon=w;
			attackPower=equipedWeapon.getDamage()+character.getAttack();
		}
	}
	public WeaponCard getEquipedWeapon() {
		return equipedWeapon;
	}
	
	public boolean addToBoard(StaticCard sc) { //si potrebbe fare che quando vine chiamato il metodo e ritorna false, si mostra un alert che chiede se si vuole sostituire la carta già posizionata con quella nuovo, altrimenti fare come scritto nell'ultimo else
		boolean check=true;
		if(board.length==0) {
			if(sc instanceof ShieldCard || sc instanceof HologramCard)
				board[0]=sc;
			else
				board[1]=sc;
			return true;
		}
		else if(board.length==1) {
			if(board[0]==null && (sc instanceof ShieldCard || sc instanceof HologramCard)) {
				board[0]=sc;
				check=true;
			}
			else if(board[0]!=null && (sc instanceof ShieldCard || sc instanceof HologramCard))
				check=false;
			else if(board[1]==null && !(sc instanceof ShieldCard || sc instanceof HologramCard)) {
				board[1]=sc;
				check=true;
			}
			else
				check=false;
			return check;
		}
		else // you can not change the active static card unless the turn duration ends or it gets destroyed
			return false;
	}
	
	public StaticCard[] getBoard() {
		return board;
	}
	
	public int getAttackPower() {
		if(equipedWeapon!=null) {
			return attackPower;
		}
		else
			return character.getAttack();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public StaticCard removeFromBoard(StaticCard sc) {
		StaticCard temp=null;
		if(sc instanceof ShieldCard || sc instanceof HologramCard) {
			temp=board[0];
			board[0]=null;
		}
		else {
			temp=board[1];
			board[1]=null;
		}
		return temp;
	}
	public StaticCard removeFromBoardInPosition(int position) {
		StaticCard temp=board[position];
		board[position]=null;
		return temp;
	}
	
	public WeaponCard removeEquipedWeapon() {
		WeaponCard removed=equipedWeapon;
		equipedWeapon=null;
		attackPower=character.getAttack();
		return removed;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	/*
	public void decreaseActiveStaticCardTurnDuration() {
		activeStaticCard.decreaseTurnDuration();
		int turnDuration=activeStaticCard.getTurnDuration();
		if(turnDuration==0)
			removeActiveStaticCard();
	}
	*/
	public void setHand(ArrayList<Card> hand) {
		this.hand=hand;
	}
	public ArrayList<Card> getHand(){
		return hand;
	}
	public boolean hasRing() {
		if(board[1]!=null && board[1] instanceof RingCard) 
			return true;
		else
			return false;
	}
	/*
	public boolean hasEnergeticShield() {
		if(board[1]!=null && board[1] instanceof EnergeticShieldCard) 
			return true;
		else
			return false;
	}*/
				
	public boolean hasHorcrux() {
		if(board[1]!=null && board[1] instanceof HorcruxCard) 
			return true;
		else
			return false;
	}
	
	public boolean hasBlackWidowsPoison() {
		if(board[1]!=null && board[1] instanceof BlackWidowsPoisonCard) 
			return true;
		else
			return false;
	}
	
	public boolean hasAztecCurse() {
		if(board[1]!=null && board[1] instanceof AztecCurseCard) 
			return true;
		else
			return false;
	}
	
	public boolean hasShieldCard() {
		if(board[0]!=null && board[0] instanceof ShieldCard) 
			return true;
		else
			return false;
	
	}
	public boolean hasHologram() {
		if(board[0]!=null && board[0] instanceof HologramCard) 
			return true;
		else
			return false;
	
	}
}	
