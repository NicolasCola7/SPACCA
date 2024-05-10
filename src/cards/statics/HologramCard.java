package cards.statics;

import cards.Seed;
import decks.Deck;
import player.Player;

public class HologramCard extends StaticCard {
	private static final long serialVersionUID = 3524309103914460998L;
	
	public HologramCard() {
		super("Ologramma",Seed.SW);
	
	}
	public boolean getEffect(Player targetPlayer,Deck deck) {// se posizionata e si viene attaccati, pesca una carta dal mazzo, se il seme della carta è uguale a quello del proprio personaggio l'attacco fallisce
		return deck.drawAndCheck(targetPlayer.getCharacter().getSeed());
	}
}
