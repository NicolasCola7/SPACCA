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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
		File staticCards = new File("./Files/CardsFiles/StaticCards.csv");
		try (Scanner scan = new Scanner(staticCards)){

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
		
		} catch (FileNotFoundException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		}
	}

	// build action cards
	private void buildActionCards() {
		File actionCards = new File("./Files/CardsFiles/ActionCards.csv");
		try (Scanner scan = new Scanner(actionCards)){

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

		} catch (FileNotFoundException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		  }
	}

	// build weapon cards
	private void buildWeaponCards() {
		File weaponCards = new File("./Files/CardsFiles/WeaponCards.csv");
		try (Scanner scan = new Scanner(weaponCards)){
	
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
		
		} catch (FileNotFoundException e) {
			 Alert alert=new Alert(AlertType.ERROR);
			 alert.setHeaderText("Si è verificato un errore:");
			 alert.setContentText("Riprova più tardi!");
			 alert.showAndWait();
		  }
	}

	private void buildEventCards() {
		for(int i=0;i<100;i++)
		deck.add(new DoomsdayCard());	
		deck.add(new IdentityTheftCard());
		deck.add(new MiracleCard());
	}

	// draw card from deck
	public Card drawCard() { 
		if (!deck.isEmpty()) {
			return deck.removeFirst();
		} else { // if deck is empty replace with Stockpile
			this.replaceDeckWithStockpile();
			return this.drawCard();
		}
	}

	// shuffle deck
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

	//draw hand when game starts
	public ArrayList<Card> drawHand() { 
		ArrayList<Card> hand = new ArrayList<Card>(5);
		for (int i = 0; i < 5; i++)
			hand.add(drawCard());
		return hand;
	}

	//replace deck with Stockpile
	private void replaceDeckWithStockpile() { 
		if (deck.isEmpty()) {
			deck.addAll(stockpile);
			stockpile.removeAll(stockpile);
			this.shuffle();
		}
	}

	//add card to stock pile
	public void addToStockPile(Card c) { 
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
