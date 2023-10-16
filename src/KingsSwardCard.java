
public class KingsSwardCard extends Weapon{

	
	
	public KingsSwardCard() {
		super("KingsSwardCard",Seed.SA,12.0);
		
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
