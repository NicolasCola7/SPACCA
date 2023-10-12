
public class SauronEyeCard extends ActionCard{
	
	public SauronEyeCard() {
		super("SauronEye");
		
	}
	
	public boolean getEffect(Game g,Player a,int damage) {
		if(g.damageOthers(a, damage)) {
			g.damageOthers(a, damage);
			return true;
		}
			
		else return false;
	}
	public boolean discard(Player a) {
		SauronEyeCard se = new SauronEyeCard();
		
		if(a.removeCardFromHand(se)) {
			a.removeCardFromHand(se);
			return true;
		}
		else return false;
	}
}
