package cards;

public class IdentityTheftCard extends EventCard{
	public IdentityTheftCard() {
		super("IdentityTheftCard",Seed.NS);
	}
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) {
		Character a=attackingPlayer.getCharacter();
		Character b=targetPlayer.getCharacter();
		attackingPlayer.setCharacter(b);
		targetPlayer.setCharacter(a);
		deck.addToStockPile(this);
		attackingPlayer.getHand().remove(this);
	}
}
