package game;

public enum GameType {
	CLASSIC("ClassicGame"),TOURNAMENT("Tournament");
	private String description;

    GameType(String description) {
        this.description = description;
    }

    // return description
    @Override
    public String toString() {
        return description;
    }
}
