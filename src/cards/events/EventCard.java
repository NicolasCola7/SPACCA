package cards.events;

import java.io.Serializable;

import cards.Card;
import cards.Seed;
import cards.Type;

public class EventCard extends Card {

	public EventCard(String n, Seed s) {
		super(n, Type.EventCard, s);
	}
}
