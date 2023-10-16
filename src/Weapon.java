
public class Weapon extends StaticCard{

	private double attackPower;
	
	public Weapon(String n,Seed seed,double ap) {
		super(n,seed);
		attackPower = ap;
	}
	
	public double getAttackPower() {
		return attackPower;
	}
	
}
