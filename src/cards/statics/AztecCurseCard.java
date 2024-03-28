package cards.statics;

import cards.Seed;
import game.Player;

public class AztecCurseCard extends StaticCard{ //se nella board, quando si viene attaccati la precisione dell'attaccatnte cala

	public AztecCurseCard() {
		super("Maledizione Azteca",Seed.PC);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreasePrecision(1);
	}
}
