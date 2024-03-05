package cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SauronEyeCard extends ActionCard{
	
	public SauronEyeCard() {
		super("SauronEyeCard",Seed.SA);
		
	}
	public void onUse(List<Player> players,Player attackingPlayer,Deck deck) {
		for(int i=0;i<players.size();i++) {
			if(players.get(i).getUsername()!=attackingPlayer.getUsername()) 
				players.get(i).getCharacter().decreaseLife(20);
		}
		attackingPlayer.getHand().remove(this);
		deck.addToStockPile(this);
	}
}
