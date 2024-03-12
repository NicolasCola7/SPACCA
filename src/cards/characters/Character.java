package cards.characters;

import java.io.Serializable;
import java.util.Random;

import cards.Seed;

public class Character implements Serializable {

	private static final long serialVersionUID = -6509654064753217547L;
	private String name;
	private int life;
	private int currentLife;
	private int attack;
	private int precision;
	private int luck;
	private Seed seed;
	private int currentPrecision;

	public Character(String n, Seed s, int l, int a, int p) {
		seed = s;
		name = n;
		life = l;
		currentLife = life;
		attack = a;
		precision = p;
		currentPrecision = precision;
	}

	// rolls player's precision
	public boolean rollPrecision() {
		Random rand = new Random();
		int roll = rand.nextInt(0, 10);
		if (roll <= currentPrecision && roll >= 0) {
			return true;
		} else
			return false;
	}

	// getters and setters
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
		currentLife -= l;
	}

	public void increaseLife(int l) {
		currentLife += l;
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
		currentPrecision -= p;
	}

	public void increasePrecision(int p) {
		currentPrecision += p;
	}

	public void resetLife() {
		currentLife = life;
	}

	public void resetPrecision() {
		currentPrecision = precision;
	}

}
