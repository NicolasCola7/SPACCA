package cards;

public class MeteorsRainCard extends ActionCard {
	public MeteorsRainCard() {
		super("MeteorsRainCard",Seed.SW);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) {
		StaticCard[] board=targetPlayer.getBoard();
		board[0]=null;
		board[1]=null;
		deck.addToStockPile(this);
		attackingPlayer.getHand().remove(this);
	}
}

