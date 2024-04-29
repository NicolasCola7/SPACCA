package game;

public class NoWinnerException extends Exception {
	private static final long serialVersionUID = -1979150823001233047L;
   
	public NoWinnerException(String message) {
        super(message);
    }
}
