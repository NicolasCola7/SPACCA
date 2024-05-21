package cards.statics;

import cards.Seed;
import player.Player;

public class AztecCurseCard extends StaticCard{ //if in board, stiker's precision decrease

	private static final long serialVersionUID = -3439228550110142437L;
	
	public AztecCurseCard() {
		super("Maledizione Azteca",Seed.PC);
	}
	
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreasePrecision(1);
	}
}
