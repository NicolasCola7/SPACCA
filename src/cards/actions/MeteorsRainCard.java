package cards.actions;

import cards.Seed;
import decks.Deck;
import player.Player;

public class MeteorsRainCard extends ActionCard {

	private static final long serialVersionUID = -4438267731422159950L;
	public MeteorsRainCard() {
		super("Pioggia Di Meteore",Seed.SW);
	}
	public static void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) {  //destroy all cards from other player's board
		targetPlayer.removeFromBoardInPosition(0);
		targetPlayer.removeFromBoardInPosition(1);
		deck.addToStockPile(new MeteorsRainCard());
	}
}

