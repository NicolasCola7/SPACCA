package cards;

public class DoomsdayCard extends EventCard{
	public DoomsdayCard() {
		super("DoomsdayCard",Seed.NS);
	}
	public Player onUse(Player attackingPlayer, Player targetPlayer, Deck deck) {
		deck.addToStockPile(this);
		attackingPlayer.getHand().remove(this);
		return targetPlayer;
	}
}
