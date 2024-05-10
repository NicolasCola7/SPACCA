package cards.actions;

import cards.Seed;
import decks.Deck;
import player.Player;

public class HealingPotionCard extends ActionCard{

	private static final long serialVersionUID = -1966149291035799273L;

	public HealingPotionCard() {
		super("Pozione Curativa",Seed.SA);
	}
	
	public static void onUse(Player attackingPlayer,Deck deck) { //permette di aggiungere 15 p.ti vita
		attackingPlayer.getCharacter().increaseLife(15);
		deck.addToStockPile(new HealingPotionCard());
	}
}
