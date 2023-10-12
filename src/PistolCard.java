
public class PistolCard extends Gun{

	
	
	public PistolCard() {
		super("PistolCard",5.0);
		
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
	
