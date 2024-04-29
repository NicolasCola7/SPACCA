package cards.statics;
import cards.Seed;
import game.Player;

public class RingCard extends StaticCard{

	private static final long serialVersionUID = -7350219279492082579L;
	
	public RingCard() {
		super("Anello",Seed.SA);
	}
	
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().increasePrecision(1);
	}
	
}
