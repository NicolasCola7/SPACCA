
public class BeginnerWandCard extends Weapon{

	

	public BeginnerWandCard() {
		super("BeginnerWandCard",Seed.HP,9.0);
		
	}
	public boolean equipe(Player a) {
		BeginnerWandCard BWC = new BeginnerWandCard();
		if(a.equipeGun(BWC)) {
			a.equipeGun(BWC);
			a.removeCardFromHand(BWC);
			return true;
		}
		else return false;
	}
}

