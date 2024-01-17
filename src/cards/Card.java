package cards;
public class Card {

	private String name;
	private Type type;
	private Seed seed;
	public Card(String n,Type t, Seed seed) {
		
		name = n;
		type = t;
		this.seed = seed;
			
	}
	public Seed getSeed() {
		return seed;
	}
	public String getName() {
		return name;
	}
	public Type getType() {
		return type;
	}
	
	
}
