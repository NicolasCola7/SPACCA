package cards;
public class HealingPotionCard extends ActionCard{

	private int healRate;
	
	public HealingPotionCard() {
		super("HealingPotionCard",Seed.SA);
		healRate = 15;
	}
	
	public void onUse(Player attackingPlayer,Player targetPlayer,Deck deck) {
		targetPlayer.getCharacter().increaseLife(healRate);
		targetPlayer.getHand().remove(this);
		deck.addToStockPile(this);
	}
}
