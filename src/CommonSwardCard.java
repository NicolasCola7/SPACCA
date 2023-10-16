
public class CommonSwardCard extends Weapon{

	
	
	public CommonSwardCard() {
		super("CommonSwardCard",Seed.PC,7.0);
		
	}
	public boolean equipe(Player a) {
		CommonSwardCard CSC = new CommonSwardCard();
		if(a.equipeGun(CSC)) {
			a.equipeGun(CSC);
			a.removeCardFromHand(CSC);
			return true;
		}
		else return false;
	}
}
