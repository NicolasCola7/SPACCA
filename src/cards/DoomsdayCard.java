package cards;

public class DoomsdayCard extends EventCard{ //elimina un giocatore a scelta
	public DoomsdayCard() {
		super("DoomsdayCard",Seed.NS);
	}
	public Player onUse(Player attackingPlayer, Player targetPlayer, Deck deck) {
		deck.addToStockPile(this);
		return targetPlayer;
	}
}
