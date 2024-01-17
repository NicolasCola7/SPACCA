package cards;

import java.util.ArrayList;

public class AttackCard extends ActionCard{
	
	public AttackCard() {
		super("AttackCard",Seed.NS);	
	}
	private boolean firstCheck(Character aC, Character tC) {
		if(aC.rollPrecision() && !tC.rollLuck())
			return true;
		else if(!aC.rollPrecision())
			return false;
		else 
			return true;
	}
	
	private boolean secondCheck(Player attackingPlayer, Player targetPlayer,Deck deck) {
		if(targetPlayer.hasShieldCard()) {
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
			return false;
		}
		/*
		else if(targetPlayer.hasEnergeticShield()) {
			EnergeticShieldCard es=new EnergeticShieldCard();
			return !es.getEffect(attackingPlayer);	
		}*/
		else if(targetPlayer.hasHologram()) {
			HologramCard h=new HologramCard();
			deck.addToStockPile(targetPlayer.removeFromBoardInPosition(0));
			return h.getEffect(targetPlayer, null);
		}
		else
			return true;
	}
	public boolean onUse(Player attackingPlayer, Player targetPlayer,Deck deck) {
		Character aC=attackingPlayer.getCharacter();
		Character tC=targetPlayer.getCharacter();
		attackingPlayer.getHand().remove(this);	
		deck.addToStockPile(this);
		boolean check=true;
	
		/*if (targetPlayer.hasAztecCurse() && targetPlayer.hasRing()) {
			aC.decreasePrecision(1);
			tC.increaseLuck(1);
			
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				if(targetPlayer.hasBlackWidowsPoison()) {
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
				}
				tC.decreaseLife(attackingPlayer.getAttackPower());
				aC.resetPrecision();
				tC.resetLuck();
				check=true;
			}
			else
				check=false;
			
			return check;
		}*/
		
		 if(!targetPlayer.hasAztecCurse() && targetPlayer.hasRing()) {
			tC.increaseLuck(1);
			
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				if(targetPlayer.hasBlackWidowsPoison()) {
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
				}
				tC.decreaseLife(attackingPlayer.getAttackPower());
				tC.resetLuck();
				check=true;
			}
			else
				check=false;
		return check;
		}
		
		else if(targetPlayer.hasAztecCurse() && !targetPlayer.hasRing()) {
			aC.decreasePrecision(1);
			
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				if(targetPlayer.hasBlackWidowsPoison()) {
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
				}
				tC.decreaseLife(attackingPlayer.getAttackPower());
				aC.resetPrecision();
				check=true;
			}
			else
				check=false;
		return check;
		}
	
		else {
			if(firstCheck(aC,tC) && secondCheck(attackingPlayer, targetPlayer,deck)) {
				if(targetPlayer.hasBlackWidowsPoison()) {
					BlackWidowsPoisonCard bwp=new BlackWidowsPoisonCard();
					bwp.getEffect(attackingPlayer);
				}
				tC.decreaseLife(attackingPlayer.getAttackPower());
				check=true;
			}
			else
				check=false;
		return check;
		}
		
	}

}
		
	

