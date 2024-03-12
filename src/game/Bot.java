package game;

import java.awt.AWTException;
import java.awt.event.InputEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import cards.characters.Character;

public class Bot extends Player {

	public Bot(String name,Character character) {
		super(name,character);
	}
	public void drawCard(Button drawButton ){
		drawButton.setOnAction(event -> {
	        simulateMouseClick(drawButton);
	    });
	}
	public void discardwCard(Button discardButton) {
	    discardButton.setOnAction(event -> {
	        simulateMouseClick(discardButton);
	        // Aggiungi qui altre azioni dopo il clic su "Scarta carta"
	    });
	}
	public void submitCard(Button submitCardButton) {
	    submitCardButton.setOnAction(event -> {
	        simulateMouseClick(submitCardButton);
	        // Aggiungi qui altre azioni dopo il clic su "Gioca carta"
	    });
	}
	public void submitPlayer(Button submitPlayerButton) {
	    submitPlayerButton.setOnAction(event -> {
	        simulateMouseClick(submitPlayerButton);
	        // Aggiungi qui altre azioni dopo il clic su "Gioca carta"
	    });
	}
	 // Metodo per simulare un clic del mouse su un elemento JavaFX
    private void simulateMouseClick(Button button) {
        // Ottieni le coordinate del bottone
		Point2D point = button.localToScreen(button.getBoundsInLocal().getMinX() + 5,
		        button.getBoundsInLocal().getMinY() + 5);

		// Crea un'istanza di Robot e muovi il mouse sul bottone
		Robot robot = new Robot();
		robot.mouseMove((int) point.getX(), (int) point.getY());

		// Simula un clic sinistro del mouse
		robot.mousePress(MouseButton.PRIMARY);
		robot.mouseRelease(MouseButton.PRIMARY);
    }

}
