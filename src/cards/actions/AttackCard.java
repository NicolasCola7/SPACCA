package cards.actions;
import cards.Seed;
import cards.characters.Character;
import cards.statics.BlackWidowsPoisonCard;
import cards.statics.EnchantedMirrorCard;
import cards.statics.HologramCard;
import decks.Deck;
import game.InformationAlert;
import player.Bot;
import player.Player;

public class AttackCard extends ActionCard{
	
	private static final long serialVersionUID = 869320498933823179L;
	public AttackCard() {
		super("Attacco",Seed.NS);	
	}
	
	// 1 check for attack result
	private static boolean firstCheck(Character aC, Character tC,Player attackingPlayer) {
		if(aC.rollPrecision())  // striker precion is true and luck of attacked is false is possible to attack
			return true;
		else {// if precision is false attack is not performed
			if(!(attackingPlayer instanceof Bot))
				InformationAlert.display("Messaggio informativo","Hai mancato il bersaglio, attacco fallito!");
			return false;
		}
	}
	
	// 2 check for attack result
	private static boolean secondCheck(Player attackingPlayer, Player targetPlayer,Deck deck) {
		HologramCard hologram=new HologramCard();
		if(targetPlayer.hasShieldCard()) { //if shield is in use attack is failed 
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0)); //remove from attacked board shield
			
			if(!(attackingPlayer instanceof Bot))
				InformationAlert.display("Messaggio informativo","Il tuo attacco è stato fermato dallo Scudo, attacco fallito!");
			return false;
		}
		else if(targetPlayer.hasHologram() && hologram.getEffect(targetPlayer, deck) ) { //if use hologram 
			
			if(!(attackingPlayer instanceof Bot))
				InformationAlert.display("Messaggio informativo","Sei stato distratto dall'ologramma, attacco fallito!");
			
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0)); //remove hologram from board used for fight attack
			return false;
		}
		else
			return true;
	}
	
	public static void onUse(Player attackingPlayer, Player targetPlayer,Deck deck) {
		Character aC=attackingPlayer.getCharacter();
		Character tC=targetPlayer.getCharacter();
		deck.addToStockPile(new AttackCard()); 
	
		 if(attackingPlayer.hasRing() && !targetPlayer.hasAztecCurse()) {//striker have ring e attacked don't have curse
			aC.increasePrecision(1);
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) { // if true execute attack
				 String message="Attacco eseguito con successo!\n";
				
				if(targetPlayer.hasBlackWidowsPoison()) {//if attacked have veleno di vedova nera, striker takes 5 demage
					BlackWidowsPoisonCard.getEffect(attackingPlayer);
					message=message+"Sei anche stato avvelenato dal veleno di vedova nera, hai perso 5 punti vita.\n";
				}
				if(targetPlayer.hasEnchantedMirror()) {//if attacked have specchio incantato, striker self-clps 
					EnchantedMirrorCard.getEffect(attackingPlayer);
					deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
					message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else 
					tC.decreaseLife(attackingPlayer.getAttackPower()); //decrease attacked life
				
				if(!(attackingPlayer instanceof Bot)) {
					InformationAlert.display("Messaggio informativo",message);
				}
			}
			aC.resetPrecision();
		}
		
		else if(targetPlayer.hasAztecCurse() && !attackingPlayer.hasRing()) {//striker dont' have ring e attacked has maledizione azteca
			aC.decreasePrecision(1); //decrease striker precision
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				
				 String message="Attacco eseguito con successo!\n";
				
				 if(targetPlayer.hasEnchantedMirror()) {
					 EnchantedMirrorCard.getEffect(attackingPlayer);
					 deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
					 message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else
					tC.decreaseLife(attackingPlayer.getAttackPower());
				 
				 if(!(attackingPlayer instanceof Bot)) {
					 InformationAlert.display("Messaggio informativo",message);
				}
			}
			aC.resetPrecision();
		}
	
		else if(!targetPlayer.hasAztecCurse()){ //striker dont' have ring e attacked don't have maledizione azteca
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				
				 String message="Attacco eseguito con successo!\n";
				
				if(targetPlayer.hasBlackWidowsPoison()) {
					BlackWidowsPoisonCard.getEffect(attackingPlayer);
					message=message+"Sei anche stato avvelenato dal veleno di vedova nera, hai perso 5 punti vita.\n";
				}
				if(targetPlayer.hasEnchantedMirror()) {
					EnchantedMirrorCard.getEffect(attackingPlayer);
					deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
					message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else
					tC.decreaseLife(attackingPlayer.getAttackPower());
				
				if(!(attackingPlayer instanceof Bot)) {
					InformationAlert.display("Messaggio informativo",message);
				}
			}
		}
		 
		else {//striker have ring e attacked don't have maledizione azteca
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) { 
				 String message="Attacco eseguito con successo!\n";
			
				if(targetPlayer.hasEnchantedMirror()) {
					EnchantedMirrorCard.getEffect(attackingPlayer);
					deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
					message=message+"Ma sei stato incantato dallo specchio, ti sei autocolpito!";
				}	
				else 
					tC.decreaseLife(attackingPlayer.getAttackPower()); 
				
				if(!(attackingPlayer instanceof Bot)) {
					InformationAlert.display("Messaggio informativo",message);
				}
			}
		}
	}
}
		
	

