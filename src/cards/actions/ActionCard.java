package cards.actions;
import cards.Card;
import cards.Seed;
import cards.Type;

public abstract class ActionCard extends Card {
	
	private static final long serialVersionUID = 6421550827180026912L;

	public ActionCard(String n, Seed seed) {
		super(n, Type.ActionCard, seed);
	}
}
