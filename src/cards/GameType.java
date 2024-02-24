package cards;

public enum GameType {
	CLASSIC("ClassicGame"),TOURNAMENT("Tournament");
	private String description;

    GameType(String description) {
        this.description = description;
    }

    // Metodo toString personalizzato per restituire la descrizione
    @Override
    public String toString() {
        return description;
    }
}
