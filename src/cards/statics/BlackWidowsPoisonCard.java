package cards.statics;

import cards.Seed;
import player.Player;

public class BlackWidowsPoisonCard extends StaticCard { //se nella board, quando si viene attaccati l'attaccante subisce danni
	
	private static final long serialVersionUID = 3280584016771419820L;
	
	public BlackWidowsPoisonCard() {
		super("Veleno Di Vedova Nera",Seed.MV);
	}
	
	public static void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreaseLife(5);
	}
}