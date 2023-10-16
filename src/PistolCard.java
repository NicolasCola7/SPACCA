
public class PistolCard extends Weapon{

	
	
	public PistolCard() {
		super("PistolCard",Seed.PC,5.0);
		
	}
	public boolean equipe(Player a) {
		PistolCard PC = new PistolCard();
		if(a.equipeGun(PC)) {
			a.equipeGun(PC);
			a.removeCardFromHand(PC);
			return true;
		}
		else return false;
	}
}
	
