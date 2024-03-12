package cards.events;

import java.io.Serializable;

import cards.Seed;
import cards.characters.Character;
import decks.Deck;
import game.Player;

public class IdentityTheftCard extends EventCard {
	public IdentityTheftCard() {
		super("IdentityTheftCard",Seed.NS);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) { //scambia il personaggio con quello dell'avversario scelto
		Character a=attackingPlayer.getCharacter();
		Character b=targetPlayer.getCharacter();
		attackingPlayer.setCharacter(b);
		targetPlayer.setCharacter(a);
		deck.addToStockPile(this);
	}
}
