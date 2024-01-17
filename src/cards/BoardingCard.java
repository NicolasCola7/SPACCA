package cards;

import java.util.ArrayList;
import java.util.Random;

public class BoardingCard extends ActionCard{

	public BoardingCard() {
		super("BoardingCard",Seed.PC);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,int i, Deck deck) {
		ArrayList<Card> tH=targetPlayer.getHand();
		ArrayList<Card> aH=attackingPlayer.getHand();
		aH.add(tH.remove(i));
		deck.addToStockPile(this);
		aH.remove(this);
	}
}