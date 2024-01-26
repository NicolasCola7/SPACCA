package cards;

import java.util.Random;

public class MeteorsRainCard extends ActionCard {
	public MeteorsRainCard() {
		super("MeteorsRainCard",Seed.SW);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) { //distrugge una carta casuale nella board dell'avversario
		StaticCard[] board=targetPlayer.getBoard();
		Random rand=new Random();
		int i=rand.nextInt(1);
		board[i]=null;
		deck.addToStockPile(this);
	}
}

