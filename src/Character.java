import java.util.Random;
public class Character{
	
	public Random rand;
	private String name;
	private double life;
	private double attack;
	private int precision;
	private int luck;
	private boolean attCount;
	public Character(String n, double l,double a,int p,int luck) {
		
		name = n;
		life = l;
		attack = a;
		precision = p;
		this.luck = luck;
		attCount = false;
		rand = new Random();
		}
	//rolls player's precision
	public boolean rollPrecision() {
		Random rand = new Random();
		int roll = rand.nextInt(0,10);
		if(roll<=this.precision&&roll>=0) {
			return true;
		}
		else return false;
	}
	//rolls player's luck
	public boolean rollLuck() {
		
		
		int roll = rand.nextInt(0,10);
		if(roll>this.luck) {
			return true;
		}
		else return false;
	}
	//getters and setters
	public String getName() {
		return name;
	}
	public double getLife() {
		return life;
	}
	public void setLife(double newLife) {
		life = newLife;
	}
	public double getAttack() {
		return attack;
	}
	public int getPrecision() {
		return precision;
	}
	public int getLuck() {
		return luck;
	}
	public boolean getCount() {
		return attCount;
	}
	public void setAttack(double newAttack) {
		attack = newAttack;
	}
	
}

