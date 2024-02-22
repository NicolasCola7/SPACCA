package cards;

import java.io.Serializable;

public class StaticCard extends Card {
	
	private int turnDuration;
	// per ora ho eliminato la durata per semplicità
	public StaticCard(String n,Seed s) {
		super(n,Type.StaticCard,s);
	}
	public int getTurnDuration() {
		return turnDuration;
	}
	public void setTurnDuration(int td) {
		turnDuration-=td;
	}
	public void decreaseTurnDuration() {
		turnDuration--;
	}
	
}
