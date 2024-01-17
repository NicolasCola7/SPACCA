package cards;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Deck {
	private Scanner scan;
	private LinkedList<Card> deck;
	private ArrayList<Card> stockpile;
	private Random rand;
	private final int numberOfCards=110;

	public Deck() {
		deck = new LinkedList<Card>();
		stockpile=new ArrayList<Card>();
		this.buildStaticCards();
		this.buildActionCards();
		this.buildWeaponCards();
		this.buildEventCards();
		this.shuffle();
	}
	
	//build static cards
	private void buildStaticCards() {
		try {
			File staticCards=new File("./Files/CardsFiles/StaticCards.csv");
			scan=new Scanner(staticCards);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				String name=line[0];
				String seed=line[1];
				//int duration=Integer.parseInt(line[2]);
				int copy=Integer.parseInt(line[2]);
				Seed s=null;
				switch (name) {
			    case "Ring":
			    	for(int i=0;i<copy;i++)
					      deck.add(new RingCard());
			        break;
			        
			  /*  case "Energetic Shield":
			    	for(int i=0;i<copy;i++)
					      deck.add(new EnergeticShieldCard());
			    	 break;*/
			    case "Horcrux":
			    	for(int i=0;i<copy;i++)
					      deck.add(new HorcruxCard());
			        break;
			    case "Black widow's Poison":
			    	for(int i=0;i<copy;i++)
					      deck.add(new BlackWidowsPoisonCard());
			        break;
			    case "Aztec Curse":
			    	for(int i=0;i<copy;i++)
					      deck.add(new AztecCurseCard());
			        break;
				
				case "Hologram":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new HologramCard()); 
			        break;
				case "Shield":
					for(int i=0;i<copy;i++) 
					      deck.add(new ShieldCard()); 
			        break;
				}
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	//build action cards
	private void buildActionCards()  {
		try {
			File actionCards=new File("./Files/CardsFiles/ActionCards.csv");
			scan=new Scanner(actionCards);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				String name=line[0];
				String seed=line[1];
				int copy=Integer.parseInt(line[2]);
				Seed s=null;
				switch (name) {
			    case "Attack":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new AttackCard()); 
			        break;

			    case "infinity's Gauntlet":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new GauntletCard()); 
			    	 break;
			    case "Healing Potion":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new HealingPotionCard()); 
			        break;
			    case "Meteors Rain":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new MeteorsRainCard()); 
			        break;
			    
			    case "Sauron's Eye":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new SauronEyeCard()); 
			        break;
			    case "Pirates Boarding":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new BoardingCard()); 
			        break;
				}
				 
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
	//build weapon cards
	private void buildWeaponCards(){
		try {
			File weaponCards=new File("./Files/CardsFiles/WeaponCards.csv");
			scan=new Scanner(weaponCards);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				String name=line[0];
				String seed=line[1];
				int damage=Integer.parseInt(line[2]);
				int copy=Integer.parseInt(line[3]);
				Seed s=null;
				switch (seed) {
			    case "HP":
			    	s=Seed.HP;
			        break;

			    case "SW":
			    	s=Seed.SW;
			    	 break;
			    case "MV":
			    	s=Seed.MV;
			        break;
			    case "PC":
			    	s=Seed.PC;
			        break;
			    case "SA":
			    	s=Seed.SA;
			        break;
				}
			for(int i=0;i<copy;i++) 
				 deck.add(new WeaponCard(name,s,damage));
				
			}
			
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	private void buildEventCards()  {
		/*try {
			File eventCards=new File("./Files/CardsFiles/EventCards.csv");
			scan=new Scanner(eventCards);
			while(scan.hasNextLine()) {
				String[] line=scan.nextLine().split(",");
				String name=line[0];
				int copy=Integer.parseInt(line[1]);
				Seed s=Seed.NS;
				switch (name) {
			    case "":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new Card()); 
			        break;

			    case "":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new Card()); 
			    	 break;
			    case "":
			    	for(int i=0;i<copy;i++) 
					      deck.add(new Card()); 
			        break;
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}*/
		deck.add(new IdentityTheftCard());
		deck.add(new DoomsdayCard());
		deck.add(new MiracleCard());
	}
	
	public Card drawCard() {
		if (!this.isEmpty()) {
			return deck.removeFirst();
		}
		else {
			this.replaceDeckWithStockpile();
			return this.drawCard();
		}		
	}
	
	// shuffle deck
	private void shuffle() {
		Collections.shuffle(deck);
	}
	
	//draw a card from the deck and check if its seed equals a certain seed
	public boolean drawAndCheck(Seed s) {
		if (!this.isEmpty()) {
			Card c=deck.removeFirst();
			this.addToStockPile(c);
			return c.getSeed().equals(s);
		}
		
		else {
			this.replaceDeckWithStockpile();
			return drawAndCheck(s);
		}		
		
	}
	
	public ArrayList<Card> drawHand(){
		ArrayList<Card> hand=new ArrayList<Card>(5);
		for(int i=0;i<5;i++)
			hand.add(drawCard());
		return hand;
	}
	
	private boolean isEmpty() {
		return deck.size()==0;
	}
	
	private void replaceDeckWithStockpile() {
		if (this.isEmpty()) {
			deck.addAll(stockpile);
			this.shuffle();
		}
	}
	
	public void addToStockPile(Card c) {
		stockpile.add(c);
	}
	
	public ArrayList<Card> getStockpile(){
		return stockpile;
	}
	
}
