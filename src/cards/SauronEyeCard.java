package cards;

import java.util.HashMap;

public class SauronEyeCard extends ActionCard{
	
	public SauronEyeCard() {
		super("SauronEye",Seed.SA);
		
	}
	public void onUse(HashMap<Integer,Player> players,Player attackingPlayer,Deck deck) {
		for(Integer i:players.keySet()) {
			StaticCard[] targetPlayersBoard=players.get(i).getBoard();
			if(players.get(i).getUsername()!=attackingPlayer.getUsername()) {
				if(targetPlayersBoard[0]==null)
					players.get(i).getCharacter().decreaseLife(20);
				else if (players.get(i).hasHologram()) {
					HologramCard h=new HologramCard();
					if(!h.getEffect(players.get(i), deck))
						players.get(i).getCharacter().decreaseLife(20);
				}	
			}	
		}
		attackingPlayer.getHand().remove(this);
		deck.addToStockPile(this);
	}
}
