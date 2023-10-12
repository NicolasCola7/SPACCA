
public class MjolnirCard extends Gun{

	
	
	public MjolnirCard() {
	super("MjolnirCard",14.0);
		
	}
	public boolean equipe(Player a) {
		MjolnirCard MC = new MjolnirCard();
		if(a.equipeGun(MC)) {
			a.equipeGun(MC);
			a.removeCardFromHand(MC);
			return true;
		}
		else return false;
	}
	
	
}
