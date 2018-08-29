import java.util.Random;

public class TestWin {

	public static void main(String[] args) throws InterruptedException {
		
		Card card1 = new Card(6,1);
		Card card2 = new Card(7,1);
		
		Card card3 = new Card(5,2);
		Card card4 = new Card(8,2);
		Card card5 = new Card(9,2);
		Card card6 = new Card(4,2);
		Card card7 = new Card(10,4);
		Hand user = new Hand();
		Hand hand1 = new Hand();
		
		user.addCard(card1);
		user.addCard(card2);
		hand1.addCard(card5);
		hand1.addCard(card6);
		hand1.addCard(card7);
		hand1.addCard(card3);
		hand1.addCard(card4);
		
		System.out.println(user.checkScore(hand1));
		System.out.println(1000 % 100);
		System.out.println(bet(10));
		
		

	}
	public static int bet(int highBet) throws InterruptedException{
		Random rand = new Random();
		if(highBet == 0) {
			highBet = 10;
		}
		int betAmt = rand.nextInt(201) + highBet;
		int stack = 100 - betAmt;
		return betAmt;
	}

}
