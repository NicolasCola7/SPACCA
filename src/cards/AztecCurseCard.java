package cards;
public class AztecCurseCard extends StaticCard{

	public AztecCurseCard() {
		super("AztecCurseCard",Seed.PC);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreasePrecision(1);
	}
}
