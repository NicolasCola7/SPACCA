package cards;

public class MiracleCard extends EventCard{
	public MiracleCard() {
		super("MiracleCard",Seed.NS);
	}
	public void onUse(Player targetPlayer,Deck deck) {
		targetPlayer.getCharacter().resetLife();
		targetPlayer.getHand().remove(this);
		deck.addToStockPile(this);
	}
}
