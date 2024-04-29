package game;

import cards.Card;
import cards.actions.AttackCard;
import cards.characters.Character;
import cards.events.EventCard;

public class Bot extends Player {

	private static final long serialVersionUID = -5387030367091611978L;
	public Bot(String username, Character character) {
		super(username,character);
	}

    public boolean hasAttackCard() {
    	boolean check=false;
    	for(Card c:hand) {
    		if(c instanceof AttackCard) {
    			check=true;
    			break;
    		}
    	}
    	return check;
    }
    public boolean hasEventCard() {
    	boolean check=false;
    	for(Card c:hand) {
    		if(c instanceof EventCard) {
    			check=true;
    			break;
    		}
    	}
    	return check;
    }

}
