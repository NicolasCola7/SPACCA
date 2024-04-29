package cards.events;
import cards.Card;
import cards.Seed;
import cards.Type;

public class EventCard extends Card {

	private static final long serialVersionUID = 7908164447056134564L;

	public EventCard(String n, Seed s) {
		super(n, Type.EventCard, s);
	}
}
