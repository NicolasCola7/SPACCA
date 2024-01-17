package cards;
public class BlackWidowsPoisonCard extends StaticCard {

	public BlackWidowsPoisonCard() {
		super("BlackWidowsPoisonCard",Seed.MV);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreaseLife(5);
	}
}