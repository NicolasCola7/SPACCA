package cards.events;

import cards.Seed;
import decks.Deck;
import player.Player;

public class MiracleCard extends EventCard{

	private static final long serialVersionUID = 2299336651302912365L;
	
	public MiracleCard() {
		super("Miracolo",Seed.NS);
	}
	
	public static void onUse(Player targetPlayer,Deck deck) { //allow to recover all life points
		targetPlayer.getCharacter().resetLife();
		deck.addToStockPile(new MiracleCard());
	}
}
