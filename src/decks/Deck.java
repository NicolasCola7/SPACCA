package decks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import cards.Card;
import cards.Seed;
import cards.WeaponCard;
import cards.actions.AttackCard;
import cards.actions.BoardingCard;
import cards.actions.GauntletCard;
import cards.actions.HealingPotionCard;
import cards.actions.MeteorsRainCard;
import cards.actions.SauronEyeCard;
import cards.events.DoomsdayCard;
import cards.events.IdentityTheftCard;
import cards.events.MiracleCard;
import cards.statics.AztecCurseCard;
import cards.statics.BlackWidowsPoisonCard;
import cards.statics.EnchantedMirrorCard;
import cards.statics.HologramCard;
import cards.statics.RingCard;
import cards.statics.ShieldCard;

public class Deck implements Serializable {
	
	private static final long serialVersionUID = -5509099123307094083L;
	private LinkedList<Card> deck;
	private LinkedList<Card> stockpile;

	public Deck() {
		deck = new LinkedList<Card>();
		stockpile = new LinkedList<Card>();
		this.buildStaticCards();
		this.buildActionCards();
		this.buildWeaponCards();
		this.buildEventCards();
		this.shuffle();
	}

	// build static cards
	private void buildStaticCards() {
		try {
			File staticCards = new File("./Files/CardsFiles/StaticCards.csv");
			Scanner scan = new Scanner(staticCards);
			while (scan.hasNextLine()) {
				String[] line = scan.nextLine().split(",");
				String name = line[0];
				int copy = Integer.parseInt(line[2]);
				switch (name) {
				case "Ring":
					for (int i = 0; i < copy; i++)
						deck.add(new RingCard());
					break;
				case "Enchanted Mirror":
					for (int i = 0; i < copy; i++)
						deck.add(new EnchantedMirrorCard());
					break;
				case "Black Widow's Poison":
					for (int i = 0; i < copy; i++)
						deck.add(new BlackWidowsPoisonCard());
					break;
				case "Aztec Curse":
					for (int i = 0; i < copy; i++)
						deck.add(new AztecCurseCard());
					break;

				case "Hologram":
					for (int i = 0; i < copy; i++)
						deck.add(new HologramCard());
					break;
				case "Shield":
					for (int i = 0; i < copy; i++)
						deck.add(new ShieldCard());
					break;
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	// build action cards
	private void buildActionCards() {
		try {
			File actionCards = new File("./Files/CardsFiles/ActionCards.csv");
			Scanner scan = new Scanner(actionCards);
			while (scan.hasNextLine()) {
				String[] line = scan.nextLine().split(",");
				String name = line[0];
				int copy = Integer.parseInt(line[2]);
				switch (name) {
				
				case "Attack":
					for (int i = 0; i < copy; i++)
						deck.add(new AttackCard());
					break;

				case "Infinity's Gauntlet":
					for (int i = 0; i < copy; i++)
						deck.add(new GauntletCard());
					break;
					
				case "Healing Potion":
					for (int i = 0; i < copy; i++)
						deck.add(new HealingPotionCard());
					break;
				case "Meteors Rain":
					for (int i = 0; i < copy; i++)
						deck.add(new MeteorsRainCard());
					break;

				case "Sauron's Eye":
					for (int i = 0; i < copy; i++)
						deck.add(new SauronEyeCard());
					break;
					
				case "Pirates Boarding":
					for (int i = 0; i < copy; i++)
						deck.add(new BoardingCard());
					break;
				}

			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	// build weapon cards
	private void buildWeaponCards() {
		try {
			File weaponCards = new File("./Files/CardsFiles/WeaponCards.csv");
			Scanner scan = new Scanner(weaponCards);
			while (scan.hasNextLine()) {
				String[] line = scan.nextLine().split(",");
				String name = line[0];
				String seed = line[1];
				int damage = Integer.parseInt(line[2]);
				int copy = Integer.parseInt(line[3]);
				Seed s = null;
				switch (seed) {
				
				case "HP":
					s = Seed.HP;
					break;

				case "SW":
					s = Seed.SW;
					break;
					
				case "MV":
					s = Seed.MV;
					break;
					
				case "PC":
					s = Seed.PC;
					break;
					
				case "SA":
					s = Seed.SA;
					break;
				}
				
				for (int i = 0; i < copy; i++)
					deck.add(new WeaponCard(name, s, damage));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	private void buildEventCards() {
		for(int i=0;i<100;i++)
		deck.add(new DoomsdayCard());	
		deck.add(new IdentityTheftCard());
		deck.add(new MiracleCard());
	}

	public Card drawCard() { // pescare una carta dal deck
		if (!deck.isEmpty()) {
			return deck.removeFirst();
		} else { // se il deck è vuoto viene rimpiazzato dalla pila degli scarti
			this.replaceDeckWithStockpile();
			return this.drawCard();
		}
	}

	// mischia il mazzo
	private void shuffle() {
		Collections.shuffle(deck);
	}

	// draw a card from the deck and check if its seed equals a certain seed
	public boolean drawAndCheck(Seed s) {
		if (!deck.isEmpty()) {
			Card c = deck.removeFirst();
			deck.addLast(c);
			return c.getSeed().equals(s);
		}
		else {
			this.replaceDeckWithStockpile();
			return drawAndCheck(s);
		}
	}

	public ArrayList<Card> drawHand() { // metodo per pescare la mano a inizio partita
		ArrayList<Card> hand = new ArrayList<Card>(5);
		for (int i = 0; i < 5; i++)
			hand.add(drawCard());
		return hand;
	}

	private void replaceDeckWithStockpile() { // rimpiazza il mazzo con la pila degli scarti
		if (deck.isEmpty()) {
			deck.addAll(stockpile);
			stockpile.removeAll(stockpile);
			this.shuffle();
		}
	}

	public void addToStockPile(Card c) { // aggiunge una carta alla pila degli scart
		stockpile.add(c);
	}

	public LinkedList<Card> getStockpile() {
		return stockpile;
	}

	public void reset() {
		deck.addAll(stockpile);
	this.shuffle();
	}
}
