package cards;
public class HologramCard extends StaticCard {

	public HologramCard() {
		super("HologramCard",Seed.SW);
	}
	public boolean getEffect(Player targetPlayer,Deck deck) {
		deck.addToStockPile(this);
		return deck.drawAndCheck(targetPlayer.getCharacter().getSeed());
	}
}
