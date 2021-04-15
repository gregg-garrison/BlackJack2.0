package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import components.Deck;
import components.Hand;
import components.Player;

public class Test2 {

	private static int deckCount = 4;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Deck d = new Deck(deckCount);
		Collections.shuffle(d.getDeck());
		Hand p1 = new Hand();
		Hand d1 = new Hand();
		boolean keepGoing = true;
		Player player = new Player("Gregg", 100.0);
		startGame(d, p1, d1, player, sc, keepGoing);
	}

	public static void startGame(Deck d, Hand playerHand, Hand dealerHand, Player player, Scanner sc,
			boolean keepGoing) {
		String ans = "";
		
		Scanner scanner = new Scanner(System.in);
		
		while(player.getBalance()>0.0) {
			System.out.println("Welcome back " + player.getName() + "!\nLet's plays some BlackJack!\n");
			System.out.println("Your current balance is " + player.getBalance());
			System.out.println("How much would you like to bet?");
			double bet = sc.nextDouble();
			if (bet > player.getBalance()) {
				System.out.println("Insufficient Funds!!\nWould you like to make a deposit?");
				ans = scanner.nextLine();
				if (ans.equalsIgnoreCase("yes")) {
					System.out.println("How much would you like to deposit?");
					double deposit = sc.nextDouble();
					player.setBalance(player.getBalance() + deposit);
					System.out.println("Deposit complete!");
					System.out.println("Player's new balance is: " + player.getBalance());
					System.out.println("How much would you like to bet?");
					bet = sc.nextDouble();
				}
			}
			
			System.out.println("You are currently betting " + bet);
			
			d.deal(playerHand.getHand(), dealerHand.getHand(), playerHand, dealerHand, player);
			
			System.out.println("\nPlayer's cards are:");
			System.out.println(playerHand + "\n");
			System.out.println("Dealer is showing: \n" + dealerHand.getHand().get(0) + "\n");
			
			afterDealCheck(playerHand, dealerHand, player, bet, keepGoing, d, sc);
			
			int choice = sc.nextInt();
			playerOptions(choice, playerHand, dealerHand, d, player, bet, sc, keepGoing);
			
		}
		
		System.out.println("Insufficient Funds!!\nWould you like to make a deposit?");
		ans = scanner.nextLine();
		if (ans.equalsIgnoreCase("yes")) {
			System.out.println("How much would you like to deposit?");
			double deposit = sc.nextDouble();
			player.setBalance(player.getBalance() + deposit);
			System.out.println("Deposit complete!");
			System.out.println("Player's new balance is: " + player.getBalance());
			
		}

	}

	public static void playerOptions(int choice, Hand pH, Hand dH, Deck d, Player player, double bet, Scanner sc,
			boolean keepGoing) {
		double newBal = 0;
		while (choice == 1) {
			pH.hit(d);
			if (pH.busted()) {
				System.out.println("Player has busted!\n");
				newBal = player.getBalance() - bet;
				player.setBalance(newBal);
				System.out.println("Player's updated balance is: " + player.getBalance());
				lose();
				keepGoing = false;
				gameOver(d, keepGoing, player, pH, dH, sc);
				break;
			} else {
				System.out.println("What would you like to do?\n1. hit\n2. stand\n");
				choice = sc.nextInt();
			}
		}

		if (choice == 2) {
			System.out.println("Dealer's hand is:\n" + dH + "\n");
			dH.dealerTurn(d);
			showDown(pH, dH, player, bet, sc, d);
		}

		if (choice == 3) {
			bet = (bet * 2);
			System.out.println("The Player has Doubled Down!");
			pH.hit(d);
			if (pH.busted()) {
				System.out.println("Player has busted!\n");
				newBal = player.getBalance() - bet;
				player.setBalance(newBal);
				System.out.println("Player's updated balance is: " + player.getBalance());
				lose();
				gameOver(d, keepGoing, player, pH, dH, sc);
			}
			System.out.println("Dealer's hand is:\n" + dH + "\n");
			dH.dealerTurn(d);
			showDown(pH, dH, player, bet, sc, d);
		}

		if (choice == 4) {
			Hand h1 = pH.splitHand();
			System.out.println("\nPlayer's first hand is:");
			pH.hit(d);
			pOpt(pH, player, bet);
			int response = sc.nextInt();
			while (response == 1) {
				pH.hit(d);
				if (pH.busted()) {
					System.out.println("Player Hand 1 has busted!\n");
					newBal = player.getBalance() - bet;
					player.setBalance(newBal);
					System.out.println("pBal: " + player.getBalance());
					response = 2;
					break;
				} else {
					System.out.println("What would you like to do?\n1. hit\n2. stand\n");
					response = sc.nextInt();
				}
			}

			if (response == 2) {
				newBal = 0;
				System.out.println("\nPlayer's second hand is: ");
				h1.hit(d);
				pOpt(h1, player, bet);
				int response2 = sc.nextInt();
				while (response2 == 1) {
					h1.hit(d);
					if (h1.busted()) {
						System.out.println("player Hand 2 has busted!\n");
						newBal = player.getBalance() - bet;
						player.setBalance(newBal);
						System.out.println("pBal2: " + player.getBalance());
						response2 = 2;
						break;
					} else {
						System.out.println("What would you like to do?\n1. hit\n2. stand\n");
						response2 = sc.nextInt();
					}
				}
			}

			List<Hand> hands = new ArrayList<>();
			hands.add(pH);
			hands.add(h1);

			System.out.println("Dealer's hand is:\n" + dH + "\n");

			if (pH.busted() && h1.busted()) {
				System.out.println("Player hands both busted!");
				System.out.println("Player's updated balance is: " + player.getBalance());
				keepGoing = false;
				System.out.println("\nWould you like to play again?");
				@SuppressWarnings("resource")
				String ans = new Scanner(System.in).nextLine();
				runItBack(ans, d, pH, dH, player, keepGoing, sc);
			} else {
				dH.dealerTurn(d);
				showDown(hands, dH, player, bet, sc, d);
			}
		}
	}

	public static void afterDealCheck(Hand p1, Hand d1, Player player, double bet, boolean keepGoing, Deck d,
			Scanner sc) {
		while (keepGoing) {
			double newBal = 0;
			if (p1.hasBlackJack() && d1.hasBlackJack()) {
				System.out.println("Both Player and Dealer have BlackJack!");
				push();
				keepGoing = false;
				gameOver(d, keepGoing, player, p1, d1, sc);
				break;
			} else if (d1.hasBlackJack()) {
				newBal = player.getBalance() - bet;
				player.setBalance(newBal);
				System.out.println("Dealer has BlackJack!");
				lose();
				System.out.println("\nYour updated Balance is: " + player.getBalance());
				keepGoing = false;
				gameOver(d, keepGoing, player, p1, d1, sc);
				break;
			} else if (p1.hasBlackJack()) {
				newBal = player.getBalance() + (bet * 1.5);
				player.setBalance(newBal);
				System.out.println("Player has BlackJack!");
				win();
				System.out.println("\nYour updated Balance is: " + player.getBalance());
				keepGoing = false;
				gameOver(d, keepGoing, player, p1, d1, sc);
				break;
			} else {
				playerOptions(p1, d1, player, bet, keepGoing);
				break;
			}
		}
	}

	public static void gameOver(Deck d, boolean keepGoing, Player player, Hand pH, Hand dH, Scanner sc) {
		System.out.println("\nWould you like to play again?");
		@SuppressWarnings("resource")
		String ans = new Scanner(System.in).nextLine();
		runItBack(ans, d, pH, dH, player, keepGoing, sc);
	}

	public static void runItBack(String ans, Deck d, Hand pH, Hand dH, Player player, boolean keepGoing, Scanner sc) {
		if (ans.equalsIgnoreCase("yes")) {
			keepGoing = true;
			pH.getHand().clear();
			dH.getHand().clear();
			int cardsLeft = d.getDeck().size();
			Deck dd = new Deck(deckCount);
			System.out.println("\n*********************NEW GAME*********************");
			System.out.println("cardsLeft: " + cardsLeft);
			if (cardsLeft < 10) {
				System.out.println("New deck coming in!!!");
				d = dd;
			}
			startGame(d, pH, dH, player, sc, keepGoing);
		} else {
			System.out.println("Thanks for playing!");
			keepGoing = false;
			System.exit(0);
		}
	}

	public static void showDown(Hand playerHand, Hand dealerHand, Player player, double bet, Scanner sc, Deck d) {
		double newBal = 0.0;
		int pVal = 21 - playerHand.getValue();
		int dVal = 21 - dealerHand.getValue();
		boolean keepGoing = true;
		while (keepGoing) {
			if (dVal >= 0 && pVal < dVal || dealerHand.busted()) {
				newBal = player.getBalance() + bet;
				player.setBalance(newBal);
				win();
				System.out.println("\nYour updated Balance is: " + player.getBalance());
				keepGoing = false;
				break;
			} else if (pVal == dVal) {
				push();
				System.out.println("\nYour Balance is still: " + player.getBalance());
				keepGoing = false;
				break;
			} else {
				newBal = player.getBalance() - bet;
				player.setBalance(newBal);
				lose();
				System.out.println("\nYour updated Balance is: " + player.getBalance());
				keepGoing = false;
				break;
			}
		}

		System.out.println("\nWould you like to play again?");
		@SuppressWarnings("resource")
		String ans = new Scanner(System.in).nextLine();
		runItBack(ans, d, playerHand, dealerHand, player, keepGoing, sc);
	}

	public static void showDown(List<Hand> hands, Hand dealerHand, Player player, double bet, Scanner sc, Deck d) {
		boolean keepGoing = true;
		double newBal = 0.0;
		int n = hands.size();
		int dVal = 21 - dealerHand.getValue();
		int pVal = 0;
		for (int i = 0; i < n; i++) {
			keepGoing = true;
			Hand h = hands.get(i);
			pVal = 21 - h.getValue();
			System.out.println("Player's Hand " + (i + 1) + " Result:\n***********************");
			System.out.println(h);
			while (keepGoing) {
				if (dVal >= 0 && pVal < dVal || dealerHand.busted()) {
					newBal = player.getBalance() + bet;
					player.setBalance(newBal);
					win();
					System.out.println("Your updated Balance is: " + player.getBalance() + "\n");
					keepGoing = false;
					break;
				} else if (pVal == dVal) {
					push();
					System.out.println("Your Balance is still: " + player.getBalance() + "\n");
					keepGoing = false;
					break;
				} else {
					newBal = player.getBalance() - bet;
					player.setBalance(newBal);
					lose();
					System.out.println("Your updated Balance is: " + player.getBalance() + "\n");
					keepGoing = false;
					break;
				}
			}
		}

		System.out.println("\nWould you like to play again?");
		@SuppressWarnings("resource")
		String ans = new Scanner(System.in).nextLine();
		runItBack(ans, d, hands.get(0), dealerHand, player, keepGoing, sc);
	}

	public static void playerOptions(Hand p1, Hand d1, Player player, double bet, boolean keepGoing) {
		while (keepGoing) {
			if (p1.canDouble(player.getBalance(), bet) && p1.canSplit()) {
				System.out.println("What would you like to do?\n1. hit\n2. stand\n3. double\n4. split\n");
			} else if (p1.canDouble(player.getBalance(), bet)) {
				System.out.println("What would you like to do?\n1. hit\n2. stand\n3. double\n");
			} else {
				System.out.println("What would you like to do?\n1. hit\n2. stand\n");
			}
			keepGoing = false;
			break;
		}
	}

	public static void pOpt(Hand p1, Player player, double bet) {
		if (p1.canDouble(player.getBalance(), bet) && p1.canSplit()) {
			System.out.println("What would you like to do?\n1. hit\n2. stand\n3. double\n4. split\n");
		} else if (p1.canDouble(player.getBalance(), bet)) {
			System.out.println("What would you like to do?\n1. hit\n2. stand\n3. double\n");
		} else {
			System.out.println("What would you like to do?\n1. hit\n2. stand\n");
		}
	}

	public static void win() {
		System.out.println("Player Wins!");
	}

	public static void lose() {
		System.out.println("Dealer Wins!");
	}

	public static void push() {
		System.out.println("Push!");
	}

}
