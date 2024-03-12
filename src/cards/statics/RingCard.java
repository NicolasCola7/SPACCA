package cards.statics;
import java.util.Random;

import cards.Seed;
import game.Player;

public class RingCard extends StaticCard{

	private Random rand;
	public RingCard() {
		super("RingCard",Seed.SA);
		rand = new Random();
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().increasePrecision(1);
	}
	
}
