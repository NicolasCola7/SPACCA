package cards.statics;

import cards.Card;
import cards.Seed;
import cards.Type;

public class StaticCard extends Card {

	private static final long serialVersionUID = 6020006893053826122L;
	
	public StaticCard(String n, Seed s) {
		super(n, Type.StaticCard, s);
	}
}
