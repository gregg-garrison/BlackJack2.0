package components;

import java.util.List;

public class Player {
	private String name;
	private double balance;
	private List<Card> hand;
	private List<Hand> hands;
	
	
	public Player() {};
	public Player(String name, double balance) {
		this.name = name;
		this.balance = balance;
	}
	
	
	public Player(String name, double balance, List<Hand> hands) {
		this.name = name;
		this.balance = balance;
		this.hands = hands;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public List<Card> getHand() {
		return hand;
	}
	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
	public List<Hand> getHands() {
		return hands;
	}
	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}
	
	

}
