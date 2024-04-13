package cards.events;

import cards.Seed;
import decks.Deck;
import game.Player;

public class MiracleCard extends EventCard{
	public MiracleCard() {
		super("Miracolo",Seed.NS);
	}
	public static void onUse(Player targetPlayer,Deck deck) { //permette di recuperare tutti i p.ti vita persi, quindi se utilizzata quando si hanno tutti i punti vita iniziali non serve a ninte
		targetPlayer.getCharacter().resetLife();
		deck.addToStockPile(new MiracleCard());
	}
}
