import java.util.Random;
public class GauntletCard extends ActionCard{

	
	public GauntletCard() {
		super("GauntletCard");
			
	}
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
		
		if(a.removeCardFromHand(gc)) {
			a.removeCardFromHand(gc);
			return true;
		}
		else return false;
	}
	
}
