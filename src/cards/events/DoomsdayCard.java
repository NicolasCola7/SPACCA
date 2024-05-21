package cards.events;

import cards.Seed;
import decks.Deck;
import player.Player;

public class DoomsdayCard extends EventCard{ //eliminate player
	private static final long serialVersionUID = 889827892558517459L;
	
	public DoomsdayCard() {
		super("Giorno Del Giudizio",Seed.NS);
	}
	
	public static Player onUse(Player attackingPlayer, Player targetPlayer, Deck deck) {
		deck.addToStockPile(new DoomsdayCard());
		return targetPlayer;
	}
}
