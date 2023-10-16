import java.util.ArrayList;
public class Player {

	private String username;
	private int position;
	private Character character;
	private ArrayList<Card>  hand;
	private ArrayList<StaticCard>  board;
	private int score;
	private Weapon wp;
	private boolean passed;
	
	
	public Player(String u,Character c) {
		position = 0;
		score = 0;
		username = u;
		character = c;
		hand = new ArrayList<Card>();	
		board = new ArrayList<StaticCard>();
		wp = null;
		passed = false;
	}
	
	
	
	//return true if player has missCard
	public boolean hasMiss() {
		boolean check = false;
		for(int i = 0;i<hand.size();i++) {
			if(hand.get(i) instanceof MissCard) {
				
				check = true;
				break;
				
				}
			else 
				check =  false;
				
		}
		return check;
		
	}
	public boolean hasRing() {
		RingCard r = new RingCard();
		return board.contains(r);
	}
	
	//add card to hand
	public boolean addCardTH(Card a) {
		if(a!=null) {
			hand.add(a);
			return true;
			
		}
		else return false;
	}
	//add card to board
	public boolean addCardTB(StaticCard a) {
		if(a!=null) {
			board.add(a);
			return true;
			
		}
		else return false;
	}
	//equipe gun to gun slot
	public boolean equipeGun(Weapon wp) {
		if(wp!=null) {
			this.wp = wp;
			return true;
		}
		else 
			return false;
			
	}
	public double getAttackPower() {
		if(wp!=null) {
			return wp.getAttackPower() + character.getAttack();
		}
		else
			return character.getAttack();
	}
	//remove card from player hand
	public boolean removeCardFromHand(Card c) {
		boolean check = false;
		if(!hand.isEmpty()) {
		for(int i=0;i<hand.size();i++) {
			if(hand.get(i).equals(c)) {
				hand.remove(i);
				check = true;
				break;
			}
			else 
				check = false;
			}
	
		}
		return check;
	}
	//remove card from player hand by index
	public boolean removeCard(int index) {
		if(!hand.isEmpty()) {
			hand.remove(index);
			return true;
		}
		else return false;
	}
	
	//pass turn or reset turn variable
	public void pass(Game g) {
		if(position == g.getPlayers().length) {
			passed = !passed;
			g.setTurns(0);
			g.setRounds(g.getRounds()+1);
		}
		else {
		passed = !passed;
		g.setTurns(g.getTurns()+1);
		}
		
	}
	//getters and setters
	public  ArrayList<Card> getHand(){
		return hand;
	}
	public void setLife(Double newLife) {
		character.setLife(newLife);
		
	}
	public Character getCharacter() {
		return character;
	}
}
