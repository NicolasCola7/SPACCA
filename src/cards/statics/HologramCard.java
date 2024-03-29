package cards.statics;

import cards.Seed;
import decks.Deck;
import game.Player;

public class HologramCard extends StaticCard {

	public HologramCard() {
		super("Ologramma",Seed.SW);
	}
	public boolean getEffect(Player targetPlayer,Deck deck) {// se posizionata e si viene attaccati, pesca una carta dal mazzo, se il seme della carta è uguale a quello del proprio personaggio l'attacco fallisce
		return deck.drawAndCheck(targetPlayer.getCharacter().getSeed());
	}
}
