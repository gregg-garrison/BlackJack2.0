package components;

public class Card {
	private static String[] faces = { "Joker", "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen",
			"King" };
	private static String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };

	private int face;
	private int suit;
	private int value;

	public Card(int face, int suit) {
		this.face = face;
		this.suit = suit;
	}

	@Override
	public String toString() {
		return faces[face] + " of " + suits[suit];
	}

	public int getFace() {
		return face;
	}

	public int getSuit() {
		return suit;
	}

	public int getValue() {
		if (face > 10) {
			value = 10;
		} else if (face == 1) {
			value = 11;
		} else {
			value = face;
		}
		return value;
	}
}
