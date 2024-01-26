package cards;
public class HorcruxCard extends StaticCard{

	public HorcruxCard() { //porta a 1 i punti vita del giocatore che la utilizza, ma se si viene attaccati, l'attacco fallisce
		super("HorcruxCard",Seed.HP);
	}
}
