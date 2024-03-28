package cards.statics;
import java.util.Random;

import cards.Seed;
import game.Player;

public class RingCard extends StaticCard{

	public RingCard() {
		super("Anello",Seed.SA);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().increasePrecision(1);
	}
	
}
