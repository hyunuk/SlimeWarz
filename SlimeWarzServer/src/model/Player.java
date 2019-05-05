package model;

public class Player {
	public int userID;
	public String name;
	public boolean isReady;

	public Player(int userID, String name){
		this.userID = userID;
		this.name = name;
		isReady = false;
	}
}
