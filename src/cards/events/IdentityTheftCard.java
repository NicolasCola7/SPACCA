package cards.events;
import cards.Seed;
import cards.characters.Character;
import decks.Deck;
import player.Player;

public class IdentityTheftCard extends EventCard {
	
	private static final long serialVersionUID = -8175989974901269409L;
	
	public IdentityTheftCard() {
		super("Furto Di Identità",Seed.NS);
	}
	
	public static void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) { //swap character with other player
		Character a=attackingPlayer.getCharacter();
		Character b=targetPlayer.getCharacter();
		attackingPlayer.setCharacter(b);
		targetPlayer.setCharacter(a);
		deck.addToStockPile(new IdentityTheftCard());
	}
}
