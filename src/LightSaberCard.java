
public class LightSaberCard extends Weapon{


	
	public LightSaberCard() {
		super("LightSaberCard",Seed.SW,13.0);
		
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
