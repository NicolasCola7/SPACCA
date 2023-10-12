
public class AttackCard extends ActionCard{
	
	public AttackCard() {
		super("AttackCard");
		
	}
	
	public boolean getEffect(Player a,Player b) {
		MissCard m = new MissCard();
		double newLife;
		if(a.getCharacter().rollPrecision() 
				
				&& b.getCharacter().rollLuck() 
				) {
			if(b.hasMiss()) {
				b.removeCardFromHand(m);
				return false;
			}
			if(b.hasRing()) {
				return false;
			}
			else {
				
			newLife = b.getCharacter().getLife() - (a.getCharacter().getAttack() +
					a.getAttackPower());
			
			b.setLife(newLife);
			return true;
				
			}
				
				}
		else {
			return false;
		}
	}
	public boolean discard(Player a) {
		AttackCard ac = new AttackCard();
		
		if(a.removeCardFromHand(ac)) {
			a.removeCardFromHand(ac);
			return true;
		}
		else return false;
	}

}
