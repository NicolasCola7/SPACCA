package cards;
public class BlackWidowsPoisonCard extends StaticCard { //se nella board, quando si viene attaccati l'attaccante subisce danni

	public BlackWidowsPoisonCard() {
		super("BlackWidowsPoisonCard",Seed.MV);
	}
	public void getEffect(Player targetPlayer) {
		targetPlayer.getCharacter().decreaseLife(5);
	}
}