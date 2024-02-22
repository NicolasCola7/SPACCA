package cards;

import java.io.Serializable;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public  abstract class ActionCard extends Card {

	public ActionCard(String n,Seed seed) {
		super(n,Type.ActionCard,seed);
	}
}	
