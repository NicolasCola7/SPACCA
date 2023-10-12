
public class Gun extends StaticCard{

	private double attackPower;
	
	public Gun(String n,double ap) {
		super(n);
		attackPower = ap;
	}
	
	public double getAttackPower() {
		return attackPower;
	}
	
}
