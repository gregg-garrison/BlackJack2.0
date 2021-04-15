package components;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	private List<Card> hand = new ArrayList<>();
	private int value;

	public Hand() {
	};

	public Hand(int value, List<Card> hand) {
		this.value = value;
		this.hand = hand;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return hand + " | " + value;
	}

	public int hit(Deck d) {
		int newVal = 0;
		Card c = d.drawCard();
		System.out.println(c);
		this.getHand().add(c);
		newVal = this.calcValue();
		System.out.println(this+"\n");

		return newVal;
	}

	public int calcValue() {
		int val = 0;
		int aceCount = 0;

		for (int i = 0; i < this.hand.size(); i++) {
			Card c = hand.get(i);
			val += c.getValue();
			if (c.getValue() == 11) {
				aceCount++;
			}
			while (aceCount > 0 && val > 21) {
				val -= 10;
				aceCount--;
			}
		}
		this.setValue(val);
		return val;
	}
	
	public int dealerTurn(Deck d) {
		int newVal = 0;
		while(this.dealerHit() && !this.busted()) {
			System.out.println("Dealer hits!");
			newVal = this.hit(d);
		}
		if(this.busted()) {
			System.out.println("Dealer Busted!\n");
		}
		return newVal;
	}
	
	public Hand splitHand(){
		Hand h = new Hand();
		List<Card> split = new ArrayList<>();
		Card c1 = this.hand.remove(1);
		split.add(c1);
		h.setHand(split);
		this.calcValue();
		h.calcValue();
		
		return h;
	}
	
	public boolean busted() {
		if(this.getValue()>21) {
			return true;
		}
		return false;
	}
	
	public boolean hasBlackJack() {
		if(this.getValue()==21) {
			return true;
		}
		return false;
	}
	
	public boolean dealerHit() {
		if(!this.busted()&&this.getValue()<17) {
			return true;
		}
		return false;
	}
	
	public boolean canSplit() {
		if(this.getHand().get(0).getValue()==this.getHand().get(1).getValue()) {
			return true;
		}
		return false;
	}
	
	public boolean canDouble(double balance, double bet) {
		if(balance >= bet * 2) {
			return true;
		}
		return false;
	}
}
