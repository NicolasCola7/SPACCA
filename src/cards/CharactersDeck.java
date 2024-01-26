package cards;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;


public class CharactersDeck {
	private LinkedList<Character> charactersDeck;
	
	public CharactersDeck(){
		charactersDeck=new LinkedList<Character>();
		this.buildCharactersDeck();
		this.shuffle();
	}
	
	private void buildCharactersDeck() {
		charactersDeck.add(new HarryPotterCh());
		charactersDeck.add(new VoldemortCh());
		charactersDeck.add(new DarthVaderCh());
		charactersDeck.add(new YodaCh());
		charactersDeck.add(new JackSparrowCh());
		charactersDeck.add(new DavyJonesCh());
		charactersDeck.add(new SauronCh());
		charactersDeck.add(new FrodoCh());
		charactersDeck.add(new ThorCh());
		charactersDeck.add(new ThanosCh());
	}
	private void shuffle(){
		Collections.shuffle(charactersDeck);
	}
	public Character getCharacter() {
		return charactersDeck.removeFirst();
	}
}
