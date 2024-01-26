package cards;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public  abstract class ActionCard extends Card{

	public ActionCard(String n,Seed seed) {
		super(n,Type.ActionCard,seed);
	}
}	
/*infoBox=new VBox();
infoBox.getChildren().add(new Label("Personaggio:"+game.getPlayer(currentPlayer).getCharacter().getName()));
infoBox.getChildren().add(new Label("Arma"+(game.getPlayer(currentPlayer).getEquipedWeapon()==null ?"nessuna":game.getPlayer(currentPlayer).getEquipedWeapon().getName())));
*/