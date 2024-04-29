package cards.events;

import cards.Seed;
import decks.Deck;
import game.Player;

public class DoomsdayCard extends EventCard{ //elimina un giocatore a scelta
	private static final long serialVersionUID = 889827892558517459L;
	
	public DoomsdayCard() {
		super("Giorno Del Giudizio",Seed.NS);
	}
	
	public static Player onUse(Player attackingPlayer, Player targetPlayer, Deck deck) {
		deck.addToStockPile(new DoomsdayCard());
		return targetPlayer;
	}
}
