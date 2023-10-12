
public class ElderWandCard extends Gun{

	
	
	public ElderWandCard() {
		super("ElderWandCard",15.0);
		
	}
	public boolean equipe(Player a) {
		ElderWandCard EWC = new ElderWandCard();
		if(a.equipeGun(EWC)) {
			a.equipeGun(EWC);
			a.removeCardFromHand(EWC);
			return true;
		}
		else return false;
	}
}
