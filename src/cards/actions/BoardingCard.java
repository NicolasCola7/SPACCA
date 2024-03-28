package cards.actions;

import java.util.ArrayList;
import java.util.Random;

import cards.Card;
import cards.Seed;
import decks.Deck;
import game.Player;

public class BoardingCard extends ActionCard{ //permette di rubare una carta casuale dalla mano dell'avversario

	public BoardingCard() {
		super("Arrembaggio",Seed.PC);
	}
	public Card onUse(Player attackingPlayer,Player targetPlayer, Deck deck) {
		ArrayList<Card> tH=targetPlayer.getHand();
		ArrayList<Card> aH=attackingPlayer.getHand();
		 Random random = new Random();
	     int i=random.nextInt(tH.size());
	     Card stolen=tH.get(i);
	     deck.addToStockPile(this);
		return stolen;
	}
}