package cards;

import java.util.ArrayList;

import javafx.scene.control.Alert;

public class AttackCard extends ActionCard{
	
	public AttackCard() {
		super("AttackCard",Seed.NS);	
	}
	private boolean firstCheck(Character aC, Character tC) {
		
		// primo controllo per l'esito dell'attacco
		if(aC.rollPrecision() && !tC.rollLuck())  // se la precisione dell'ttaccante è true e la fortuna dell'attaccato è false l'attacco può essere eseguito
			return true;
		else if(!aC.rollPrecision()){// se la precisione è false, l'attacco non è eseguito
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Hai mancato il bersaglio, attacco fallito!");
			alert.showAndWait();
			return false;
		}
		else 
			return true;
	}
	
	private boolean secondCheck(Player attackingPlayer, Player targetPlayer,Deck deck) {//secondo controllo per l'esito dell'attacco
		HologramCard hologram=new HologramCard();
		if(targetPlayer.hasShieldCard()) { //se ha lo scudo piazzato l'attacco non va a buon fine
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0)); //rimuovo dalla board dell'attaccato lo scudo usato per parare l'attacco
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Il tuo attacco è stato fermato dallo Scudo, attacco fallito!");
			alert.showAndWait();
			return false;
		}
		else if(targetPlayer.hasHologram() && hologram.getEffect(targetPlayer, deck) ) { //se ha l'ologramma dipende dall'esito del suo effetto
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Messaggio informativo");
			alert.setHeaderText(null);
			alert.setContentText("Sei stato distratto dall'ologramma, attacco fallito!");
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
	
		 if(targetPlayer.hasRing()) {//casio in cui l'attaccato abbia l'anello nella board
			tC.increaseLuck(1); //aumenta la fortuna dell' attaccato grazuie all'anello
			
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) { //eseguo i due controlli, se sono entrambi true, l'attacco parte
				Alert alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				alert.setContentText("Attacco eseguito con successo!");
				alert.showAndWait();
				tC.decreaseLife(attackingPlayer.getAttackPower()); //diminusco la vita dell'attaccato
				tC.resetLuck(); //resetto la fortuna dell'attaccato che era aumentata grazie all'anello
			}
		}
		
		else if(targetPlayer.hasAztecCurse()) {// caso in cui l'attaccato abbia la maledizione azteca nella board
			aC.decreasePrecision(1); //diminuisco la precisione dell'attaccante grazie alla maledizione azteca
			
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				Alert alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Messaggio informativo");
				alert.setHeaderText(null);
				alert.setContentText("Attacco eseguito con successo!");
				alert.showAndWait();
				tC.decreaseLife(attackingPlayer.getAttackPower());
				aC.resetPrecision();
			}
		}
	
		else { //caso in cui l'attaccato non ha  anello ne maledizione azteca
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				if(targetPlayer.hasBlackWidowsPoison()) {//se l'attacco è partito e l'attaccato ha il veleno di vedova nera, l'attaccante subisce 5 danni
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
					Alert alert=new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Messaggio informativo");
					alert.setHeaderText(null);
					alert.setContentText("Attacco eseguito con successo! Ma sei anche stato avvelenato dal veleno di vedova nera, hai perso 5 punti vita.");
					alert.showAndWait();
					tC.decreaseLife(attackingPlayer.getAttackPower());
				}
			}
		}
	}

}
		
	

