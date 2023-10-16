import java.util.ArrayList;
import java.util.Random;

public class Deck {

	private ArrayList<Card>  cards;
	private int numCards;
	private ArrayList<Card> discardCards;
	private Random rand;
	public Deck(int nCards) {
		numCards = nCards;
		cards = new ArrayList<Card>(nCards);
		discardCards = new ArrayList<Card>();
		rand = new Random();
		
	}
	public void Add(Card c) {
		cards.add(c);
		
	}

	public Card Draw() {
		
		if(!cards.isEmpty()) {
			Card temp = cards.get(0);
			
			cards.remove(0);
			
			return cards.get(0);
			
		}
		else return null;
	}
	public void shuffle() {
		if(cards.size()!=0) {
		for(int i = 0;i<cards.size();i++) {
			int n = rand.nextInt(0,cards.size());
			cards.set(i, cards.get(n));
			cards.set(n,cards.get(i));
		}
		
	}
		else {
			for(int i =0;i<discardCards.size();i++) {
				cards.add(discardCards.get(i));
				discardCards.remove(i);
				}
			for(int i = 0;i<cards.size();i++) {
				int n = rand.nextInt(0,cards.size());
				cards.set(i, cards.get(n));
				cards.set(n,cards.get(i));
			}
			
		}
		}

	public void discard(Card c) {
		discardCards.add(c);
		
	}
	
}
