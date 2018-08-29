import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {

	private boolean isAI = false;
	private String name = "";
	private int stack = 1000;
	private Hand playerHand;
	private boolean ifFold = false;
	private boolean ifOut = false;
	private boolean allIn = false;
	private boolean hasGone = false;
	private Image avatar;
	private double handstrength = 0;
	private double aggression = 0;

	public Player(boolean ai, String playerName, int playerMoney, int avatarNum) throws IOException {
		isAI = ai;
		name = playerName;
		stack = playerMoney;
		playerHand = new Hand();
		String image = "/" + avatarNum + ".png";
		avatar = ImageIO.read(getClass().getResource(image));
	}

	public String getName() {
		return name;
	}

	public int getStack() {
		return stack;
	}
	
	public double getAggro() {
		return aggression;
	}

	public boolean getFold() {
		return ifFold;
	}

	public Image getAvatar() {
		return avatar;
	}

	public boolean isAllIn() {
		return allIn;
	}

	public boolean isPlayerAi() {
		return isAI;
	}

	public void playerHasGone() {
		hasGone = true;
	}

	public void allIn() {
		allIn = true;
	}

	public void newRoundUnFold() {
		ifFold = false;
	}

	public boolean hasGone() {
		return hasGone;
	}

	public void newRoundNotGone() {
		hasGone = false;
	}

	public void newRoundNotAllIn() {
		allIn = false;
	}

	public Card getCard1() {
		return playerHand.getCard(0);
	}

	public Card getCard2() {
		return playerHand.getCard(1);
	}

	public void outOfGame() {
		ifOut = true;
	}
	
	public void setAggro(double agro) {
		aggression = agro;
	}

	public void setCard1(Card card) {
		playerHand.addCard(card, 0);
	}

	public void setCard2(Card card) {
		playerHand.addCard(card, 1);
	}

	public void setFold() {
		ifFold = true;
	}

	public void setStack(int newStack) {
		stack = newStack;
	}

	public int call(int highBet) throws InterruptedException {
		if ((stack - highBet) <= 0) {
			highBet = stack;
		}
		stack -= highBet;
		
		return highBet;
	}

	public int bet(int highBet, int loop) throws InterruptedException {
		Random rand = new Random(System.currentTimeMillis());
		if (highBet == 0) {
			highBet = 10;
		}

		double aiBet = (10*aggression*(handstrength/(loop)) * (stack/50));
		if((int)aiBet <= 1) {
			
			aiBet = 5;
		}
		int betAmt = rand.nextInt((int)aiBet) + highBet;
		
		if(betAmt >= stack) {
			
			betAmt = stack;
			stack = 0;

			allIn = true;
			
		}
		stack -= betAmt;
	

		return betAmt;
	}

	public void blind(int betAmt) throws InterruptedException {

		stack -= betAmt;
		if (stack == 0)
			allIn = true;
	}

	public void fold() throws InterruptedException {

		setFold();
	}

	public Hand getHand() {
		return playerHand;
	}

	public void clearHand() {

		playerHand = new Hand();
	}

	public boolean ifOutOfGame() {
		return ifOut;
	}
	
	public int AIBehaviour(boolean anteRound, Hand centerHand, int loop, int highBet) throws InterruptedException {
		
		if(anteRound == true) {
			
			return AIAnteRound();
		}
		
		else {
			
			return  AIBettingRounds(centerHand, loop, highBet);
		}
	}
	
	private int AIAnteRound() {
		
		if(playerHand.checkPairRaw(0) > 0) {
			
			return 0;
		}
		
		else if(getCard1().getRank() + getCard2().getRank() > 10) {
			
			return 0;
		}
		
		else if(getCard1().getSuit() == getCard2().getSuit()) {
			
			return 0;
		}
		
		else if(getCard1().getRank() == 1|| getCard2().getRank() == 1) {
			
			return 0;
		}
		
		else {
			
			return 1;
		}
	}
	
	private int AIBettingRounds(Hand centerHand, int loop, int highBet) throws InterruptedException {
		
		double ahead = 0;
		double tied = 0;
		double behind = 0;
		
		ArrayList<Card> deck1 = new ArrayList<>();
		ArrayList<Card> deck2 = new ArrayList<>();
		buildDeck(deck1);
		buildDeck(deck2);
		Hand checkHand = new Hand();
		Random rand = new Random(System.currentTimeMillis());
		int bench = checkHand.preFinishCheck(centerHand, getCard1(), getCard2());
		
		for(int i = 0; i < deck1.size(); i++) {
			

			for(int j = 0; j < deck2.size(); j++) {
				
				if(deck1.get(i).compare(getCard1()) == false && deck1.get(i).compare(getCard2()) == false && deck2.get(i).compare(getCard2()) == false && deck1.get(i).compare(getCard1()) == false) {
					
					int score = checkHand.preFinishCheck(centerHand, deck1.get(i), deck2.get(j));
					
					if(bench > score) {
						
						ahead ++;
					}
					
					else if(bench == score) {
						
						tied ++;
					}
					else {
						
						behind ++;
					}
				}
			}
		}
		handstrength=((ahead+tied)/2)/(tied+behind+ahead);
		
		if(loop == 1) {
			
			if(handstrength*aggression > 0.20) {
				
				if(highBet >= stack) {
					
					return -2;
				}
				
				else {
					
					return bet(highBet, loop);
				}
				
			}
			
			else if(handstrength*aggression  > 0.5 ) {
				
				return -2;
			}
			
			else if(rand.nextDouble() *aggression >0.35) {
				
				return -2;
			}
			else {
				
				return -1;
			}
		}
		
		else {
			double handModifier = (double) (loop * 0.9);
			
			if(handstrength*(aggression/2)  > 0.40 * handModifier) {
				
				if(highBet >= stack) {
					
					return -2;
				}
				
				else {
					
					return bet(highBet, loop);
				}
			}
			
			else if(handstrength*aggression  > 0.10 /handModifier ) {
				
				return -2;
			}
			
			else if(rand.nextDouble() *aggression >0.45) {
				
				if(rand.nextDouble() *aggression > 0.5) {
					
					return bet(highBet, loop);
				}
				else {
					
					return -2;
				}
				
				
			}
			
			else {
				
				return -1;
			}
		}
		
	}
	
	private void buildDeck(ArrayList<Card> deck) {
		// # of suits
		for (int i = 1; i < 5; i++) {
			// # of ranks
			for (int j = 1; j < 14; j++) {
				Card c = new Card(j, i);
				c.setCardIndex(i, j);
				deck.add(c);
			}
		}
	}
}
