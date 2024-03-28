package cards.events;

import cards.Seed;
import decks.Deck;
import game.Player;

public class DoomsdayCard extends EventCard{ //elimina un giocatore a scelta
	public DoomsdayCard() {
		super("Giorno Del Giudizio",Seed.NS);
	}
	public Player onUse(Player attackingPlayer, Player targetPlayer, Deck deck) {
		deck.addToStockPile(this);
		return targetPlayer;
	}
}
