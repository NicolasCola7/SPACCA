
public class HealingPotionCard extends ActionCard{

	private double healRate;
	
	public HealingPotionCard() {
		super("HealingPotionCard",Seed.SA);
		healRate = 15;
	}
	public boolean getEffect(Player a) {
		double newLife = a.getCharacter().getLife() + healRate;
		a.setLife(newLife);
		return true;
	}
	public boolean discard(Player a) {
		HealingPotionCard hp = new HealingPotionCard();
		
		if(a.removeCardFromHand(hp)) {
			a.removeCardFromHand(hp);
			return true;
		}
		else return false;
	}
}
