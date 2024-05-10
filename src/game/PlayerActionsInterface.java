package game;

import cards.Card;

public interface PlayerActionsInterface {

	public Card drawCard(int currentPlayer);
	
	public void discardCard(int currentPlayer,int selectedCard);
	
	public void submitEventCard(int submittedCard, int player, int target);
	
	public boolean submitWeaponCard(int submittedCard, int player);
	
	public boolean submitStaticCard(int submittedCard, int player);
	
	public void submitActionCard(int submittedCard, int currentPlayer, int target);
	
}
