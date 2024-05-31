package decks;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import cards.Seed;
import cards.characters.*;
import cards.characters.Character;

public class CharactersDeck implements Serializable {
	
	private static final long serialVersionUID = 4378656253029144470L;
	private LinkedList<Character> charactersDeck;

	public CharactersDeck() {
		charactersDeck = new LinkedList<Character>();
		this.buildCharactersDeck();
		this.shuffle();
	}

	private void buildCharactersDeck() {
		charactersDeck.add(new Character("HarryPotterCh",Seed.HP,85,8,6));
		charactersDeck.add(new Character("VoldemortCh",Seed.HP,100,9,7));
		charactersDeck.add(new Character("DarthVaderCh",Seed.SW,100,8,8));
		charactersDeck.add(new Character("YodaCh",Seed.SW,90,9,8));
		charactersDeck.add(new Character("ThorCh",Seed.MV,110,8,7));
		charactersDeck.add(new Character("ThanosCh",Seed.MV,80,10,6));
		charactersDeck.add(new Character("FrodoCh",Seed.SA,80,5,4));
		charactersDeck.add(new Character("SauronCh",Seed.SA,110,10,7));
		charactersDeck.add(new Character("JackSparrowCh",Seed.PC,90,6,9));
		charactersDeck.add(new Character("DavyJonesCh",Seed.PC,90,8,9));
	}

	private void shuffle() {
		Collections.shuffle(charactersDeck);
	}

	public Character getCharacter() {
		return charactersDeck.removeFirst();
	}
}
