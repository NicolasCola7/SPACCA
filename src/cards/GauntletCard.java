package cards;
import java.util.ArrayList;
import java.util.Random;
public class GauntletCard extends ActionCard{
	public GauntletCard() {
		super("GauntletCard",Seed.MV);
			
	}
	public Card onUse(Player attackingPlayer,Player targetPlayer, Deck deck) { //permetrte di scartare una carta casuale dalla mano dell'avversario
		ArrayList<Card> tH=targetPlayer.getHand();
		ArrayList<Card> aH=attackingPlayer.getHand();
		Random random = new Random();
	     int i=random.nextInt(tH.size());
	     Card discarded=tH.get(i);
		deck.addToStockPile(discarded);
		deck.addToStockPile(this);
		return discarded;
	}
}
