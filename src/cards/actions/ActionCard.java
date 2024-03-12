package cards.actions;

import java.io.Serializable;

import cards.Card;
import cards.Seed;
import cards.Type;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class ActionCard extends Card {

	public ActionCard(String n, Seed seed) {
		super(n, Type.ActionCard, seed);
	}
}
