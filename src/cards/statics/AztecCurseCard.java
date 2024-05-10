package cards.statics;

import cards.Seed;
import player.Player;

public class AztecCurseCard extends StaticCard{ //se nella board, quando si viene attaccati la precisione dell'attaccatnte cala

	private static final long serialVersionUID = -3439228550110142437L;
	
	public AztecCurseCard() {
		super("Maledizione Azteca",Seed.PC);
	}
	
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreasePrecision(1);
	}
}
