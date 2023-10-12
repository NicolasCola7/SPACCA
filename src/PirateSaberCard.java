
public class PirateSaberCard extends Gun{

	
	
	public PirateSaberCard() {
		super("PirateSaberCard",10.0);
		
	
	}
	
	public boolean equipe(Player a) {
		PirateSaberCard PSC = new PirateSaberCard();
		if(a.equipeGun(PSC)) {
			a.equipeGun(PSC);
			a.removeCardFromHand(PSC);
			return true;
		}
		else return false;
	}
}
