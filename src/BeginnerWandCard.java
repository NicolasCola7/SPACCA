
public class BeginnerWandCard extends Gun{

	

	public BeginnerWandCard() {
		super("BeginnerWandCard",9.0);
		
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

