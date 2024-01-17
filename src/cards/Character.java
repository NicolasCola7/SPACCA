package cards;
import java.util.Random;
public class Character{
	
	public Random rand;
	private String name;
	private int life;
	private int currentLife;
	private int attack;
	private int precision;
	private int luck;
	private Seed seed;
	private int currentPrecision;
	private int currentLuck;
	public Character(String n,Seed s, int l,int a,int p,int luck) {
		seed=s;
		name = n;
		life = l;
		currentLife=life;
		attack = a;
		precision = p;
		currentPrecision=precision;
		this.luck = luck;
		currentLuck=luck;
		rand = new Random();
		}
	//rolls player's precision
	public boolean rollPrecision() {
		Random rand = new Random();
		int roll = rand.nextInt(0,10);
		if(roll<=currentPrecision&&roll>=0) {
			return true;
		}
		else 
			return false;
	}
	//rolls player's luck
	public boolean rollLuck() {
		int roll = rand.nextInt(0,10);
		if(roll>currentLuck) {
			return true;
		}
		else
			return false;
	}
	//getters and setters
	public String getName() {
		return name;
	}
	public double getInitialLife() {
		return life;
	}
	public int getAttack() {
		return attack;
	}
	public int getInitialPrecision() {
		return precision;
	}
	public int getInitialLuck() {
		return luck;
	}
	public void decreaseLife(int l) {
		currentLife-=l;
	}
	public void increaseLife(int l) {
		currentLife+=l;
	}
	public int getCurrentLife() {
		return currentLife;
	}
	public Seed getSeed() {
		return seed;
	}
	public int getCurrentPrecision() {
		return currentPrecision;
	}
	public void decreasePrecision(int p) {
		currentPrecision-=p;
	}
	public void increasePrecision(int p) {
		currentPrecision+=p;
	}
	public int getCurrentLuck() {
		return currentLuck;
	}
	public void decreaseLuck(int l) {
		currentLuck-=l;
	}
	public void increaseLuck(int l) {
		currentLuck+=l;
	}
	public void resetLife() {
		currentLife=life;
	}
	public void resetPrecision() {
		currentPrecision=precision;
	}
	public void resetLuck() {
		currentLuck=luck;
	}
	
}

