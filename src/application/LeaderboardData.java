package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaderboardData {
	private SimpleIntegerProperty position;
	private SimpleStringProperty name;
	private SimpleIntegerProperty score;
	
	public  LeaderboardData(int position,String name, int score) {
		this.position=new SimpleIntegerProperty(position);
		this.name=new SimpleStringProperty(name);
		this.score=new SimpleIntegerProperty(score);
	
	}

	public int getPosition() {
		return position.get();
	}

	public void setPosition(int position) {
		this.position.set(position);;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);;
	}

	public int getScore() {
		return score.get();
	}

	public void setScore(int score) {
		this.score.set(score);
	}
}
