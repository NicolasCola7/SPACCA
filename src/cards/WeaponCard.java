package cards;
public class WeaponCard extends Card{

	private int damage;
	
	public WeaponCard(String n,Seed seed,int d) {
		super(n,Type.WeaponCard,seed);
		damage =d;
	}
	
	public int getDamage() {
		return damage;
	}
	/*
	public boolean equipe(Player a) {
		if(a.setEquipedWeapon(this)) {
			a.setEquipedWeapon(this);
			a.discardCardFromHand(this);
			return true;
		}
		else 
			return false;
	}
	*/
}
