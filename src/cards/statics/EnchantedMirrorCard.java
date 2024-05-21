package cards.statics;

import cards.Seed;
import player.Player;

public class EnchantedMirrorCard extends StaticCard{

	private static final long serialVersionUID = -4601484984417938718L;
	
	public EnchantedMirrorCard() { //if attacked,striker takes damage
		super("Specchio Incantato",Seed.HP);
	}
	
	public static void getEffect(Player attackingPlayer) {
		int damage=attackingPlayer.getAttackPower();
		attackingPlayer.getCharacter().decreaseLife(damage);
	}
}
