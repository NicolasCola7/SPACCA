package cards.actions;

import java.util.ArrayList;
import java.util.Random;

import cards.Card;
import cards.Seed;
import decks.Deck;
import player.Player;

public class BoardingCard extends ActionCard{ //allow to steal a card from other player

	private static final long serialVersionUID = -1012613661486162202L;
	public BoardingCard() {
		super("Arrembaggio",Seed.PC);
	}
	public static Card onUse(Player attackingPlayer,Player targetPlayer, Deck deck) {
		ArrayList<Card> tH=targetPlayer.getHand();
		if(tH.size()>0) {
			Random random = new Random();
		    int i=random.nextInt(tH.size());
		    Card stolen=tH.get(i);
		    deck.addToStockPile(new BoardingCard());
			return stolen;
		}
		else
			return null;
	}
}