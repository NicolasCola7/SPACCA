import java.util.Random;

public class RingCard extends StaticCard{

	
	private Random rand;
	public RingCard() {
		super("RingCard",Seed.SA);
		
		rand = new Random();
	}
	public boolean getEffect() {
		int roll = rand.nextInt(0, 100);
		if(roll<26) {
			
			return true;
			
		}
		else return false;
		
	}
	
}
