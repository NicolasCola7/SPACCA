package cards.actions;

import cards.Seed;
import decks.Deck;
import game.Player;

public class HealingPotionCard extends ActionCard{

	private int healRate;
	
	public HealingPotionCard() {
		super("Pozione Curativa",Seed.SA);
		healRate = 15;
	}
	
	public void onUse(Player attackingPlayer,Deck deck) { //permette di aggiungere 15 p.ti vita
		attackingPlayer.getCharacter().increaseLife(healRate);
		deck.addToStockPile(this);
	}
}
