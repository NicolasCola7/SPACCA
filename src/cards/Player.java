package cards;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
public class Player implements Serializable{
	
	private String username;
	private Character character;
	private StaticCard[] board;
	private WeaponCard equipedWeapon;
	private int attackPower;
	private ArrayList<Card> hand;

	
	public Player(String name,Character ch) {
		username = name;
		character=ch;
		attackPower=character.getAttack();
		hand=new ArrayList<Card>();
		board=new StaticCard[2];
	}
	
	public void setEquipedWeapon(WeaponCard w) {
		equipedWeapon=w;
		attackPower=equipedWeapon.getDamage()+character.getAttack();
	}
	public WeaponCard getEquipedWeapon() {
		return equipedWeapon;
	}
	
	public boolean addToBoard(StaticCard sc) { //si potrebbe fare che quando vine chiamato il metodo e ritorna false, si mostra un alert che chiede se si vuole sostituire la carta già posizionata con quella nuovo
		boolean check=true;
		if(board[0]==null && board[1]==null) {
			if(sc instanceof ShieldCard || sc instanceof HologramCard || sc instanceof EnchantedMirrorCard)
				board[0]=sc;
			else
				board[1]=sc;
			return true;
		}
		
		else if(board[0]==null && board[1]!=null) {
			if(sc instanceof ShieldCard || sc instanceof HologramCard || sc instanceof EnchantedMirrorCard) {
				board[0]=sc;
				check=true;
			}
			else
				check=false;
			return check;
		}
		
		else if(board[0]!=null && board[1]==null) {
			if(sc instanceof ShieldCard || sc instanceof HologramCard || sc instanceof EnchantedMirrorCard)
				check=false;
			else {
				check=true;
				board[1]=sc;
			}	
			return check;
		}
		else 
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
	
	public StaticCard removeFromBoard(StaticCard sc) {
		StaticCard temp=null;
		if(sc instanceof ShieldCard || sc instanceof HologramCard || sc instanceof EnchantedMirrorCard) {
			temp=board[0];
			board[0]=null;
		}
		else {
			temp=board[1];
			board[1]=null;
		}
		return temp;
	}
	
	public StaticCard removeFromBoardInPosition(int position) { //rimuove dalla board una carta in una determinata posizione
		StaticCard temp=board[position];
		board[position]=null;
		return temp;
	}
	
	public WeaponCard removeEquipedWeapon() { //rimuove l'arma equipaggiata
		WeaponCard removed=equipedWeapon;
		equipedWeapon=null;
		attackPower=character.getAttack(); //resetta la potenza d'attacco del personaggio
		return removed;
	}
	public ArrayList<Card> getHand(){
		return hand;
	}
	
	//di seguito tutti i metodi per controllare le carte nella board
	public boolean hasRing() {
		if(board[1]!=null && board[1] instanceof RingCard) 
			return true;
		else
			return false;
	}
				
	public boolean hasEnchantedMirror() {
		if(board[0]!=null && board[0] instanceof EnchantedMirrorCard) 
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
	
	public void resetAll() {
		board=new StaticCard[2];
		this.removeEquipedWeapon();
		hand.clear();
		character.resetLife();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character c) {
		character=c;
	}
	
}	
