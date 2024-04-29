package cards.statics;

import cards.Seed;
import game.Player;

public class EnchantedMirrorCard extends StaticCard{

	private static final long serialVersionUID = -4601484984417938718L;
	
	public EnchantedMirrorCard() { //quando si viene attaccati,non si subiscono damìnni ma li subisce l'avversario
		super("Specchio Incantato",Seed.HP);
	}
	
	public static void getEffect(Player attackingPlayer) {
		int damage=attackingPlayer.getAttackPower();
		attackingPlayer.getCharacter().decreaseLife(damage);
	}
}
