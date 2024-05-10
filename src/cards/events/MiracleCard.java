package cards.events;

import cards.Seed;
import decks.Deck;
import player.Player;

public class MiracleCard extends EventCard{

	private static final long serialVersionUID = 2299336651302912365L;
	
	public MiracleCard() {
		super("Miracolo",Seed.NS);
	}
	
	public static void onUse(Player targetPlayer,Deck deck) { //permette di recuperare tutti i p.ti vita persi, quindi se utilizzata quando si hanno tutti i punti vita iniziali non serve a ninte
		targetPlayer.getCharacter().resetLife();
		deck.addToStockPile(new MiracleCard());
	}
}
