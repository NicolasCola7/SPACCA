package cards;
import java.util.Random;

public class RingCard extends StaticCard{

	private Random rand;
	public RingCard() {
		super("RingCard",Seed.SA);
		rand = new Random();
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().increaseLuck(1);
	}
	
}
