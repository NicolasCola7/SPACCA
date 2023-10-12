
public class KingsSwardCard extends Gun{

	
	
	public KingsSwardCard() {
		super("KingsSwardCard",12.0);
		
	}
	public boolean equipe(Player a) {
		KingsSwardCard KSC = new KingsSwardCard();
		if(a.equipeGun(KSC)) {
			a.equipeGun(KSC);
			a.removeCardFromHand(KSC);
			return true;
		}
		else return false;
	}
}
