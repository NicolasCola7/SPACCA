package cards.actions;
import java.util.List;
import cards.Seed;
import decks.Deck;
import player.Player;

public class SauronEyeCard extends ActionCard{
	
	private static final long serialVersionUID = -3319638517347241358L;
	public SauronEyeCard() {
		super("Occhio Di Sauron",Seed.SA);
		
	}
	public static void onUse(List<Player> players,Player attackingPlayer,Deck deck) {
		for(int i=0;i<players.size();i++) {
			if(players.get(i).getUsername()!=attackingPlayer.getUsername()) 
				players.get(i).getCharacter().decreaseLife(20);
		}
		
		deck.addToStockPile(new SauronEyeCard());
	}
}
