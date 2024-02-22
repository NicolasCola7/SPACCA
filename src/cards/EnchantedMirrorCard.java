package cards;
public class EnchantedMirrorCard extends StaticCard{

	public EnchantedMirrorCard() { //quando si viene attaccati,non si subiscono damìnni ma li subisce l'avversario
		super("EnchantedMirrorCard",Seed.HP);
	}
	public void getEffect(Player attackingPlayer) {
		int damage=attackingPlayer.getAttackPower();
		attackingPlayer.getCharacter().decreaseLife(damage);
	}
}
