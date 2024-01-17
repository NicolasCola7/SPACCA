package cards;
import java.util.ArrayList;
import java.util.Random;
public class GauntletCard extends ActionCard{

	
	public GauntletCard() {
		super("GauntletCard",Seed.MV);
			
	}
	/*
	public boolean getEffect(Player a,Player b) {
		Random rand = new Random();
		int indexCard = rand.nextInt();
		Card c = b.getHand().get(indexCard);
		
			if(a.addCardTH(c)) {
				b.removeCard(indexCard);
				a.addCardTH(c);
				return true;
			}
			else 
				return false;
	}
	public boolean discard(Player a) {
		GauntletCard gc = new GauntletCard();
		
		if(a.discardCardFromHand(gc)) {
			a.discardCardFromHand(gc);
			return true;
		}
		else 
			return false;
	}
	*/
	public void onUse(Player attackingPlayer,Player targetPlayer, Deck deck) {
		ArrayList<Card> tH=targetPlayer.getHand();
		ArrayList<Card> aH=attackingPlayer.getHand();
		int x=(int)(1+Math.random()*tH.size()-1);
		deck.addToStockPile(tH.remove(x));
		deck.addToStockPile(this);
		aH.remove(this);
	}
}
