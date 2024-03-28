package cards.statics;

import cards.Seed;
import game.Player;

public class BlackWidowsPoisonCard extends StaticCard { //se nella board, quando si viene attaccati l'attaccante subisce danni

	public BlackWidowsPoisonCard() {
		super("Veleno Di Vedova Nera",Seed.MV);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreaseLife(5);
	}
}