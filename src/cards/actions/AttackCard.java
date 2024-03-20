package cards.actions;

import java.util.ArrayList;

import cards.Seed;
import cards.characters.Character;
import cards.statics.BlackWidowsPoisonCard;
import cards.statics.EnchantedMirrorCard;
import cards.statics.HologramCard;
import decks.Deck;
import game.Bot;
import game.Player;
import javafx.scene.control.Alert;

public class AttackCard extends ActionCard{
	
	public AttackCard() {
		super("AttackCard",Seed.NS);	
	}
	private boolean firstCheck(Character aC, Character tC,Player attackingPlayer) {
		
		// primo controllo per l'esito dell'attacco
		if(aC.rollPrecision())  // se la precisione dell'ttaccante è true e la fortuna dell'attaccato è false l'attacco può essere eseguito
			return true;
		else {// se la precisione è false, l'attacco non è eseguito
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Hai mancato il bersaglio, attacco fallito!");
			if(!(attackingPlayer instanceof Bot))
				alert.showAndWait();
			return false;
		}
	}
	
	private boolean secondCheck(Player attackingPlayer, Player targetPlayer,Deck deck) {//secondo controllo per l'esito dell'attacco
		HologramCard hologram=new HologramCard();
		if(targetPlayer.hasShieldCard()) { //se ha lo scudo piazzato l'attacco non va a buon fine
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0)); //rimuovo dalla board dell'attaccato lo scudo usato per parare l'attacco
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Il tuo attacco è stato fermato dallo Scudo, attacco fallito!");
			if(!(attackingPlayer instanceof Bot))
					alert.showAndWait();
			return false;
		}
		else if(targetPlayer.hasHologram() && hologram.getEffect(targetPlayer, deck) ) { //se ha l'ologramma dipende dall'esito del suo effetto
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Sei stato distratto dall'ologramma, attacco fallito!");
			if(!(attackingPlayer instanceof Bot))
				alert.showAndWait();
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0)); //rimuovo dalla board dell'attaccato l'ologramma usato per provare a parare l'attacco
			return false;
		}
		else
			return true;
	}
	public void onUse(Player attackingPlayer, Player targetPlayer,Deck deck) {
		Character aC=attackingPlayer.getCharacter();
		Character tC=targetPlayer.getCharacter();
		deck.addToStockPile(this); 
	
		 if(attackingPlayer.hasRing() && !targetPlayer.hasAztecCurse()) {//caso in cui l'attaccante ha anello e attaccato non ha maledizione
			aC.increasePrecision(1);
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) { //eseguo i due controlli, se sono entrambi true, l'attacco parte
				 Alert alert=new Alert(Alert.AlertType.INFORMATION);
				 alert.setTitle("Messaggio informativo");
				 alert.setHeaderText(null);
				 String message="Attacco eseguito con successo!\n";
				
				if(targetPlayer.hasBlackWidowsPoison()) {//se l'attacco è partito e l'attaccato ha il veleno di vedova nera, l'attaccante subisce 5 danni
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
					message=message+"Sei anche stato avvelenato dal veleno di vedova nera, hai perso 5 punti vita.\n";
				}
				if(targetPlayer.hasEnchantedMirror()) {//se l'attacco è partito e l'attaccato ha lo specchio incantato, l'attaccante si autocolpisce danni
					EnchantedMirrorCard em=new EnchantedMirrorCard();
					em.getEffect(attackingPlayer);
					message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else 
					tC.decreaseLife(attackingPlayer.getAttackPower()); //diminusco la vita dell'attaccato
				
				if(!(attackingPlayer instanceof Bot)) {
						alert.setContentText(message);
						alert.showAndWait();
				}
			}
			aC.resetPrecision();
		}
		
		else if(targetPlayer.hasAztecCurse() && !attackingPlayer.hasRing()) {// caso in cui l'attaccato abbia la maledizione azteca nella board e l'attaccante non ha l'anello
			aC.decreasePrecision(1); //diminuisco la precisione dell'attaccante grazie alla maledizione azteca
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				 Alert alert=new Alert(Alert.AlertType.INFORMATION);
				 alert.setTitle("Messaggio informativo");
				 alert.setHeaderText(null);
				 String message="Attacco eseguito con successo!\n";
				
				 if(targetPlayer.hasEnchantedMirror()) {//se l'attacco è partito e l'attaccato ha lo specchio incantato, l'attaccante si autocolpisce danni
						EnchantedMirrorCard em=new EnchantedMirrorCard();
						em.getEffect(attackingPlayer);
						message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else
					tC.decreaseLife(attackingPlayer.getAttackPower());
				 
				 if(!(attackingPlayer instanceof Bot)) {
						alert.setContentText(message);
						alert.showAndWait();
				}
			}
			aC.resetPrecision();
		}
	
		else if(!targetPlayer.hasAztecCurse()){ //caso in cui l'attaccato non ha maledizione azteca e l'attaccante non ha anello
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				 Alert alert=new Alert(Alert.AlertType.INFORMATION);
				 alert.setTitle("Messaggio informativo");
				 alert.setHeaderText(null);
				 String message="Attacco eseguito con successo!\n";
				
				if(targetPlayer.hasBlackWidowsPoison()) {//se l'attacco è partito e l'attaccato ha il veleno di vedova nera, l'attaccante subisce 5 danni
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
					message=message+"Sei anche stato avvelenato dal veleno di vedova nera, hai perso 5 punti vita.\n";
				}
				if(targetPlayer.hasEnchantedMirror()) {//se l'attacco è partito e l'attaccato ha lo specchio incantato, l'attaccante si autocolpisce danni
					EnchantedMirrorCard em=new EnchantedMirrorCard();
					em.getEffect(attackingPlayer);
					message=message+"Sei stato incantato dallo specchio, ti sei autocolpito!";
				}
				else
					tC.decreaseLife(attackingPlayer.getAttackPower());
				
				if(!(attackingPlayer instanceof Bot)) {
					alert.setContentText(message);
					alert.showAndWait();
				}
			}
		}
		 
		else {// caso in cui l'attaccante ha anello e attaccato ha maledizione azteca
			
			if(firstCheck(aC,tC,attackingPlayer) && secondCheck(attackingPlayer, targetPlayer,deck)) { //eseguo i due controlli, se sono entrambi true, l'attacco parte
				 Alert alert=new Alert(Alert.AlertType.INFORMATION);
				 alert.setTitle("Messaggio informativo");
				 alert.setHeaderText(null);
				 String message="Attacco eseguito con successo!\n";
			
				if(targetPlayer.hasEnchantedMirror()) {//se l'attacco è partito e l'attaccato ha lo specchio incantato, l'attaccante si autocolpisce danni
					EnchantedMirrorCard em=new EnchantedMirrorCard();
					em.getEffect(attackingPlayer);
					message=message+"Ma sei stato incantato dallo specchio, ti sei autocolpito!";
				}	
				else 
					tC.decreaseLife(attackingPlayer.getAttackPower()); //diminusco la vita dell'attaccato
				
				if(!(attackingPlayer instanceof Bot)) {
					alert.setContentText(message);
					alert.showAndWait();
				}
			}
		}
	}
}
		
	

