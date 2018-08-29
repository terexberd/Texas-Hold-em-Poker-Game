import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.jupiter.api.Test;

class TexasHoldEmTest {
	
	private Player player;
	private Hand hand;
	private ArrayList<Card> deck;
	private Card card1;
	private Card card2;
	private Card card3;
	private Card card4;
	private Card card5;
	private Card card6;
	private Card card7;
	private Card card1a;
	private Card card2a;
	private int moneyInPot;
	
	public void setUp() {
		player = new Player(false, "Tester", 1000, 0);
		hand = new Hand();
		deck = buildDeck();	
		moneyInPot = 1000;

	}

	@Test
	void testPlayerName() {
		setUp();
		assertEquals("Tester", player.getName());
	}
	
	@Test
	void testGetPlayerStack() {
		setUp();
		assertEquals(1000, player.getStack());
	}
	
	@Test
	void testSetPlayerStack() {
		setUp();
		player.setStack(400);
		assertEquals(400, player.getStack());
	}
	
	@Test
	void testClearHand() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(5,2);
		card4 = new Card(8,2);
		card5 = new Card(9,2);
		card6 = new Card(4,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		player.clearHand();
		assertEquals(0, player.getHand().getSize());
	}

	@Test
	void testCheckScore1() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(5,2);
		card4 = new Card(8,2);
		card5 = new Card(9,2);
		card6 = new Card(4,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		assertEquals(510, player.getHand().getScore());
	}
	
	@Test
	void testCheckScore2() {
		setUp();
		card1 = new Card(6,2);
		card2 = new Card(10,1);
		card3 = new Card(5,2);
		card4 = new Card(4,2);
		card5 = new Card(9,2);
		card6 = new Card(4,3);
		card7 = new Card(11,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		assertEquals(204, player.getHand().getScore());
	}
	
	@Test
	void testCheckScore3() {
		setUp();
		card1 = new Card(8,3);
		card2 = new Card(1,2);
		card3 = new Card(6,3);
		card4 = new Card(4,2);
		card5 = new Card(10,2);
		card6 = new Card(4,4);
		card7 = new Card(8,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		assertEquals(308, player.getHand().getScore());
	}
	@Test
	void testTieBreaker() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(5,2);
		card4 = new Card(8,2);
		card5 = new Card(9,2);
		card6 = new Card(4,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		Player player2 = new Player(false, "Joey", 1000, 0);
		card1a = new Card(11,1);
		card2a = new Card(7,2);
		player2.setCard1(card1a);
		player2.setCard2(card2a);
		player2.getHand().checkScore(hand);
		assertEquals(2, tieBreaker(player, player2));
	}
	
	@Test
	void testTieBreaker2() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(8,2);
		card4 = new Card(8,2);
		card5 = new Card(9,2);
		card6 = new Card(4,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		Player player2 = new Player(false, "Joey", 1000, 0);
		card1a = new Card(11,1);
		player2.setCard1(card1a);
		player2.setCard2(card2);
		player2.getHand().checkScore(hand);
		assertEquals(2, tieBreaker(player, player2));
	}
	
	@Test
	void testTieBreaker3() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(8,2);
		card4 = new Card(8,2);
		card5 = new Card(8,2);
		card6 = new Card(10,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		Player player2 = new Player(false, "Joey", 1000, 0);
		card1a = new Card(11,1);
		player2.setCard1(card1a);
		player2.setCard2(card2);
		player2.getHand().checkScore(hand);
		assertEquals(2, tieBreaker(player, player2));
	}
	
	@Test
	void testTieBreaker4() {
		setUp();
		card1 = new Card(6,1);
		card2 = new Card(7,1);
		card3 = new Card(8,2);
		card4 = new Card(8,2);
		card5 = new Card(8,2);
		card6 = new Card(10,2);
		card7 = new Card(10,4);
		player.setCard1(card1);
		player.setCard2(card2);
		hand.addCard(card3);
		hand.addCard(card4);
		hand.addCard(card5);
		hand.addCard(card6);
		hand.addCard(card7);
		player.getHand().checkScore(hand);
		Player player2 = new Player(false, "Joey", 1000, 0);
		player2.setCard1(card1);
		player2.setCard2(card2);
		player2.getHand().checkScore(hand);
		assertEquals(0, tieBreaker(player, player2));
	}
	
	@Test
	void testAIRandomAction() throws InterruptedException {
		setUp();
		Player newPlayer = new Player(false, "Joey", 1000, 0);
		assertTrue(aiRandomAction(1, 0, newPlayer).equals("Joey Has Folded.")||aiRandomAction(1, 0, newPlayer).equals("Joey Has Called."));
	}
	
	@Test
	void testAIRandomAction1() throws InterruptedException {
		setUp();
		Player newPlayer = new Player(false, "Alex", 500, 0);
		assertTrue(aiRandomAction(2, 0, newPlayer).equals("Alex Has Folded.")||aiRandomAction(2, 0, newPlayer).equals("Alex Has Called."));
	}
	
	// 0 - raise; 1 - fold; 2 - call;
		public String aiRandomAction(int round, int aiIndex, Player p) throws InterruptedException {
			String action = "";
			Random rand = new Random();
			int moves = -1;

			// To determine the AI's action randomly
			// if not the first round
			if (round >= 1) {
				moves = rand.nextInt(2);
				if (moves == 0) {
					p.fold();
					action = p.getName() + " Has Folded.";
				} else{
					p.call(1);
					action = p.getName() + " Has Called.";
				}
			}
			// if in the first round
			else {
				moves = rand.nextInt(3);
				if (moves == 0) {
					int betAmt = p.bet(1, 50);
					action = p.getName() + " Has Bet " + betAmt;
					moneyInPot = betAmt + moneyInPot;
				} else if (moves == 1) {
					p.fold();
					action = p.getName() + " Has Folded.";
				} else {
					p.call(1);
					action = p.getName() + " Has Called.";
				}
			}
			// System.out.println(moves);
			return action;
		}
	
	private int tieBreaker(Player player1, Player player2) {
		int player1HighCard;
		int player2HighCard;
		int player1Kicker;
		int player2Kicker;
		if (player1.getCard1().getRank() == 1) {

			player1HighCard = 14;
			player1Kicker = player1.getCard2().getRank();
		} 
		else if(player1.getCard2().getRank() == 1) {
			
			player1HighCard = 14;
			player1Kicker = player1.getCard1().getRank();
		}
		else if (player1.getCard1().getRank() > player1.getCard2().getRank()) {

			player1HighCard = player1.getCard1().getRank();
			player1Kicker = player1.getCard2().getRank();
		} 
		else {

			player1HighCard = player1.getCard2().getRank();
			player1Kicker = player1.getCard1().getRank();
		}
		if (player2.getCard1().getRank() == 1) {

			player2HighCard = 14;
			player2Kicker = player2.getCard2().getRank();
		} 
		else if(player2.getCard2().getRank() == 1) {
			
			player2HighCard = 14;
			player2Kicker = player2.getCard1().getRank();
		}
		else if (player2.getCard1().getRank() > player2.getCard2().getRank()) {

			player2HighCard = player2.getCard1().getRank();
			player2Kicker = player2.getCard2().getRank();
		} 
		else {

			player2HighCard = player2.getCard2().getRank();
			player2Kicker = player2.getCard1().getRank();
			
		}
		if(player1HighCard > player2HighCard) {
			
			return 1;
		}
		
		else if(player1HighCard < player2HighCard) {
			
			return 2;
		}
		else if(player1Kicker > player2Kicker) {
			
			return 1;
		}
		else if(player1Kicker < player2Kicker) {
			
			return 2;
		}
		else {
			
			return 0;
		}

	}
	private static ArrayList<Card> buildDeck() {
		ArrayList<Card> deck = new ArrayList<>();
		// # of suits
		for (int i = 1; i < 5; i++) {
			// # of ranks
			for (int j = 1; j < 14; j++) {
				Card c = new Card(j, i);
				c.setCardIndex(i, j);
				deck.add(c);
			}
		}
		Collections.shuffle(deck);
		return deck;
	}

}
