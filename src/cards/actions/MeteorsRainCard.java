package cards.actions;

import cards.Seed;
import decks.Deck;
import game.Player;

public class MeteorsRainCard extends ActionCard {
	public MeteorsRainCard() {
		super("MeteorsRainCard",Seed.SW);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) { //distrugge tutte le carte posizionate nella board dell'avversario
		targetPlayer.removeFromBoardInPosition(0);
		targetPlayer.removeFromBoardInPosition(1);
		deck.addToStockPile(this);
	}
}

