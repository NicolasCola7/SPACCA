
public class Main {

	public static void main(String[] args) {
		HarryPotterCh harry = new HarryPotterCh();
		SauronCh sauron = new SauronCh();
		Player marco = new Player("marco",harry);
		Player paolo = new Player("paolo",sauron);
		MissCard m = new MissCard();
		AttackCard at = new AttackCard();
		KingsSwardCard ksc = new KingsSwardCard();
		LightSaberCard lsc = new LightSaberCard();
		Deck main = new Deck(10);
		main.Add(lsc);
		main.Add(ksc);
		main.Add(m);
		main.Add(at);
		main.shuffle();
		
		
		marco.addCardTH(at);
		paolo.addCardTH(m);
		
		ksc.equipe(marco);
		
		
		//at.getEffect(marco, paolo);
		System.out.println("contiene " + paolo.removeCardFromHand(m));
		paolo.removeCardFromHand(m);
		System.out.println("Vita secondo giocatore: " + paolo.getCharacter().getLife());
		System.out.println("contiene " + paolo.hasMiss());
	}

}
