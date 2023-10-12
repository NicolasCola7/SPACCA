
public class LightSaberCard extends Gun{


	
	public LightSaberCard() {
		super("LightSaberCard",13.0);
		
	}
	public boolean equipe(Player a) {
		LightSaberCard LSB = new LightSaberCard();
		if(a.equipeGun(LSB)) {
			a.equipeGun(LSB);
			return true;
		}
		else return false;
	}
}
