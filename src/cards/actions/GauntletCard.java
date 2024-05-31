package cards.actions;
import java.util.ArrayList;
import java.util.Random;

import cards.Card;
import cards.Seed;
import decks.Deck;
import player.Player;
public class GauntletCard extends ActionCard{

	private static final long serialVersionUID = 559366357330063678L;
	public GauntletCard() {
		super("Guanto Di Thanos",Seed.MV);
			
	}
	public static Card onUse(Player attackingPlayer,Player targetPlayer, Deck deck) { //allow to discard a card from other player
		ArrayList<Card> tH=targetPlayer.getHand();
		if (tH.size()>0) {
			Random random = new Random();
			int i=random.nextInt(tH.size());
			Card discarded=tH.get(i);
			deck.addToStockPile(discarded);
			deck.addToStockPile(new GauntletCard());
			return discarded;
		}
		else
			return null;
	}
}
