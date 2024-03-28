package cards.statics;

import cards.Seed;
import game.Player;

public class EnchantedMirrorCard extends StaticCard{

	public EnchantedMirrorCard() { //quando si viene attaccati,non si subiscono damìnni ma li subisce l'avversario
		super("Specchio Incantato",Seed.HP);
	}
	public void getEffect(Player attackingPlayer) {
		int damage=attackingPlayer.getAttackPower();
		attackingPlayer.getCharacter().decreaseLife(damage);
	}
}
