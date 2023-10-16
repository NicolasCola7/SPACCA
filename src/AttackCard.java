
public class AttackCard extends ActionCard{
	
	public AttackCard() {
		super("AttackCard",Seed.HP);
		
	}
	
	public boolean getEffect(Player a,Player b) {
		MissCard m = new MissCard();
		
		double newLife;
		boolean check = false;
		if(a.getCharacter().rollPrecision() && b.getCharacter().rollLuck() ) {
			
			check = false;
				
			}
			else if(b.hasMiss()) {
					b.removeCardFromHand(m);
					check = false;
				}
				
				
				else if(b.hasRing()) {
					RingCard r = new RingCard();
					if(r.getEffect()) {
						check = false;
						
					}
				}
				else {
					
					newLife = b.getCharacter().getLife() - (a.getCharacter().getAttack() +
							a.getAttackPower());
					
					b.setLife(newLife);
					check = true;
				}
				discard(a);
				return check;
				
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
