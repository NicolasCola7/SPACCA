package cards.statics;

import cards.Seed;
import decks.Deck;
import player.Player;

public class HologramCard extends StaticCard {
	private static final long serialVersionUID = 3524309103914460998L;
	
	public HologramCard() {
		super("Ologramma",Seed.SW);
	
	}
	public boolean getEffect(Player targetPlayer,Deck deck) {// if attacked, draw a card, if card's seem is the same as player's character, attack fail
		return deck.drawAndCheck(targetPlayer.getCharacter().getSeed());
	}
}
