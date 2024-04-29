package cards;

public class WeaponCard extends Card {
	
	private static final long serialVersionUID = -6683575635773691701L;
	private int damage;

	public WeaponCard(String n, Seed seed, int d) {
		super(n, Type.WeaponCard, seed);
		damage = d;
	}

	public int getDamage() {
		return damage;
	}
}
