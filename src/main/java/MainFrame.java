import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.*;

@SuppressWarnings("unchecked")

public class MainFrame extends JFrame {
	private Image stackImage;
	private Image chips;
	private Image back;
	private Image nvback;
	private Timer timer;
	static private int theme = 0;
	private int numOfAI;
	private int cardCount = 51;
	private JPanel northAI1 = new JPanel();
	private JPanel northAI2 = new JPanel();
	private JPanel northAI3 = new JPanel();
	private JPanel p = new JPanel(new GridLayout(3, 4, 2, 2));
	private JPanel p2 = new JPanel(new GridLayout(2, 4, 2, 2));
	private JPanel p3 = new JPanel(new GridLayout(1, 4, 2, 2));
	private JPanel player = new JPanel();
	private JPanel pot = new JPanel();
	private JPanel eastAI1 = new JPanel();
	private JPanel eastAI2 = new JPanel();
	private JPanel westAI1 = new JPanel();
	private JPanel westAI2 = new JPanel();
	private JPanel fiveCards = new JPanel();
	private JLabel userNameL = new JLabel();
	private Hand centerHand = new Hand();
	private JLabel moneyInPotLabel = new JLabel();
	private JLabel roundLabel = new JLabel();
	private JLabel userStack = new JLabel();
	private JTextPane playerAction = new JTextPane();
	private JLabel dealerLabel = new JLabel();
	private JLabel dealerIDLabel = new JLabel();
	private JLabel handLabel = new JLabel();
	private JPanel timerPanel = new JPanel();
	private JLabel timerTextLabel = new JLabel();
	private static JLabel timerLabel = new JLabel();

	private int betAmount = 0;
	private JButton betButton = new JButton("Bet");
	private JButton bet1Button = new JButton("$1");
	private JButton bet5Button = new JButton("$5");
	private JButton bet10Button = new JButton("$10");
	private JButton bet25Button = new JButton("$25");
	private JButton bet50Button = new JButton("$50");
	private JButton bet100Button = new JButton("$100");
	private JButton clearButton = new JButton("CLEAR");
	private JButton callButton = new JButton("Call");
	private JButton foldButton = new JButton("Fold");
	private JButton smallBlind = new JButton("Small Blind");
	private JButton bigBlind = new JButton("Big Blind");
	private JTextField betAmt = new JTextField("$" + betAmount);

	private ImageIcon winningC1Icon;
	private ImageIcon winningC2Icon;
	private ImageIcon winningC3Icon;
	private ImageIcon winningC4Icon;
	private ImageIcon winningC5Icon;

	private String userName = new String();
	private boolean isBlind = true;
	private Player user;
	private Player betOwner = null;
	private String action;
	private int moneyInPot = 0;

	private ArrayList<Player> players;
	private static ArrayList<Card> deck;
	private JLabel avatar;

	static PrintWriter logWriter = null;
	static int handNumber = 1;
	static int dealerID = -1;
	int nextIndex = -1;
	int gameRound = -1;
	int highBet = 0;
	int callBet = 0;
	boolean ifUserFold = false;
	boolean userMoved = false;
	static private boolean timeLimit;
	int loop = 1;

	/**
	 * Constructor
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public MainFrame(Player newUser, ArrayList<Player> newPlayers, int whichTheme, JLabel newAvatar, boolean limit)
			throws InterruptedException, IOException {
		super("Texas Hold'em");
		user = newUser;
		userName = user.getName();
		timeLimit = limit;
		players = (ArrayList<Player>) newPlayers.clone();
		theme = whichTheme;
		avatar = newAvatar;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getStack() <= 0) {
				players.remove(i);
			}
		}
		numOfAI = players.size();
		String playerName = user.getName();
		deck = new ArrayList<Card>();
		buildDeck();
		shuffle();
		// Only initialize the log file in the first hand
		if (handNumber == 1) {
			try {
				initLog(playerName, numOfAI);
			} catch (Exception e) {
			}
		}
		else {
			logWriter.println("\n\n- Hand " + handNumber + "\n\nPreflop:\nCards Dealt:");
		}
		initFrame();
		// set the size of the frame
		setSize(1190, 1650);
		// click X to exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
    	@Override
    	public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        	String endTime = new SimpleDateFormat("dd MMMM yyyy  -  HH : mm")
				.format(Calendar.getInstance().getTime());
				logWriter.println("\n- Game ends " + endTime);
				logWriter.close();
            System.exit(0);
    		}
		});

		// Must be the last line of this constructor
		setVisible(true);
	}

	/**
	 * @throws IOException
	 * @wbp.parser.constructor
	 */
	public MainFrame(Player user2, ArrayList<Player> players2, JFrame frame, int themeMode, JLabel newAvatar,
			boolean newLimit) throws InterruptedException, IOException {
		frame.setVisible(false);
		frame.dispose();
		new MainFrame(user2, players2, themeMode, newAvatar, newLimit);
	}

	/**
	 * Shuffle the deck
	 */
	private static void shuffle() {
		Collections.shuffle(deck);
	}

	/**
	 * Populates deck with card objects
	 */
	private static void buildDeck() {
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

	/**
	 * Initialize the logWriter and log the basic info about the game
	 */
	private void initLog(String name, int num) throws FileNotFoundException {
		logWriter = new PrintWriter("log.txt");
		String startTime = new SimpleDateFormat("dd MMMM yyyy  -  HH : mm").format(Calendar.getInstance().getTime());
		logWriter.println("       * * * Game Log * * *\n" + "- Game started " + startTime);
		logWriter.print("- Username: " + name + "\n- Players: " + num + "\n  ");
		for (int i = 0; i < num - 1; i++)
			logWriter.print(players.get(i).getName() + ", ");
		logWriter.println(players.get(num - 1).getName() + "\n\n- Hand 1\n\nPreflop:\nCards Dealt:");
		// cards dealt are recorded when initialize (AI)players
	}

	/**
	 * Construct the main frame
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void initFrame() throws InterruptedException, IOException {
		setBounds(100, 100, 450, 450);
		JPanel contentPane = new JPanel();

		if (theme == 1) {
			contentPane.setBackground(new Color(22, 65, 111));
			pot.setBackground(new Color(42, 84, 136));
			fiveCards.setBackground(new Color(42, 84, 136));
			playerAction.setBackground(new Color(42, 84, 136));
			p.setBackground(new Color(31, 114, 205));
			p2.setBackground(new Color(31, 114, 205));
			p3.setBackground(new Color(31, 114, 205));
			timerPanel.setBackground(new Color(31, 114, 205));
			player.setBackground(new Color(31, 114, 205));
		} else {
			contentPane.setBackground(new Color(9, 120, 0));
			pot.setBackground(new Color(4, 95, 0));
			fiveCards.setBackground(new Color(4, 95, 0));
			playerAction.setBackground(new Color(4, 95, 0));
			p.setBackground(new Color(43, 151, 0));
			p2.setBackground(new Color(43, 151, 0));
			p3.setBackground(new Color(43, 151, 0));
			timerPanel.setBackground(new Color(43, 151, 0));
			player.setBackground(new Color(43, 151, 0));
		}

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		getContentPane().setLayout(null);

		try {
			stackImage = ImageIO.read(getClass().getResource("/stacks.png"));
			chips = ImageIO.read(getClass().getResource("/pokerchips.png"));
			back = ImageIO.read(getClass().getResource("/back.png"));
			nvback = ImageIO.read(getClass().getResource("/navycard.png"));
			Image clubs2 = ImageIO.read(getClass().getResource("/clubs2.png"));
			Image clubs3 = ImageIO.read(getClass().getResource("/clubs3.png"));
			Image clubs4 = ImageIO.read(getClass().getResource("/clubs4.png"));
			Image clubs5 = ImageIO.read(getClass().getResource("/clubs5.png"));
			Image clubs6 = ImageIO.read(getClass().getResource("/clubs6.png"));
			Image clubs7 = ImageIO.read(getClass().getResource("/clubs7.png"));
			Image clubs8 = ImageIO.read(getClass().getResource("/clubs8.png"));
			Image clubs9 = ImageIO.read(getClass().getResource("/clubs9.png"));
			Image clubs10 = ImageIO.read(getClass().getResource("/clubs10.png"));
			Image clubs11 = ImageIO.read(getClass().getResource("/clubs11.png"));
			Image clubs12 = ImageIO.read(getClass().getResource("/clubs12.png"));
			Image clubs13 = ImageIO.read(getClass().getResource("/clubs13.png"));
			Image clubs1 = ImageIO.read(getClass().getResource("/clubs1.png"));
			Image diamonds2 = ImageIO.read(getClass().getResource("/diamonds2.png"));
			Image diamonds3 = ImageIO.read(getClass().getResource("/diamonds3.png"));
			Image diamonds4 = ImageIO.read(getClass().getResource("/diamonds4.png"));
			Image diamonds5 = ImageIO.read(getClass().getResource("/diamonds5.png"));
			Image diamonds6 = ImageIO.read(getClass().getResource("/diamonds6.png"));
			Image diamonds7 = ImageIO.read(getClass().getResource("/diamonds7.png"));
			Image diamonds8 = ImageIO.read(getClass().getResource("/diamonds8.png"));
			Image diamonds9 = ImageIO.read(getClass().getResource("/diamonds9.png"));
			Image diamonds10 = ImageIO.read(getClass().getResource("/diamonds10.png"));
			Image diamonds11 = ImageIO.read(getClass().getResource("/diamonds11.png"));
			Image diamonds12 = ImageIO.read(getClass().getResource("/diamonds12.png"));
			Image diamonds13 = ImageIO.read(getClass().getResource("/diamonds13.png"));
			Image diamonds1 = ImageIO.read(getClass().getResource("/diamonds1.png"));
			Image spades2 = ImageIO.read(getClass().getResource("/spades2.png"));
			Image spades3 = ImageIO.read(getClass().getResource("/spades3.png"));
			Image spades4 = ImageIO.read(getClass().getResource("/spades4.png"));
			Image spades5 = ImageIO.read(getClass().getResource("/spades5.png"));
			Image spades6 = ImageIO.read(getClass().getResource("/spades6.png"));
			Image spades7 = ImageIO.read(getClass().getResource("/spades7.png"));
			Image spades8 = ImageIO.read(getClass().getResource("/spades8.png"));
			Image spades9 = ImageIO.read(getClass().getResource("/spades9.png"));
			Image spades10 = ImageIO.read(getClass().getResource("/spades10.png"));
			Image spades11 = ImageIO.read(getClass().getResource("/spades11.png"));
			Image spades12 = ImageIO.read(getClass().getResource("/spades12.png"));
			Image spades13 = ImageIO.read(getClass().getResource("/spades13.png"));
			Image spades1 = ImageIO.read(getClass().getResource("/spades1.png"));
			Image hearts2 = ImageIO.read(getClass().getResource("/hearts2.png"));
			Image hearts3 = ImageIO.read(getClass().getResource("/hearts3.png"));
			Image hearts4 = ImageIO.read(getClass().getResource("/hearts4.png"));
			Image hearts5 = ImageIO.read(getClass().getResource("/hearts5.png"));
			Image hearts6 = ImageIO.read(getClass().getResource("/hearts6.png"));
			Image hearts7 = ImageIO.read(getClass().getResource("/hearts7.png"));
			Image hearts8 = ImageIO.read(getClass().getResource("/hearts8.png"));
			Image hearts9 = ImageIO.read(getClass().getResource("/hearts9.png"));
			Image hearts10 = ImageIO.read(getClass().getResource("/hearts10.png"));
			Image hearts11 = ImageIO.read(getClass().getResource("/hearts11.png"));
			Image hearts12 = ImageIO.read(getClass().getResource("/hearts12.png"));
			Image hearts13 = ImageIO.read(getClass().getResource("/hearts13.png"));
			Image hearts1 = ImageIO.read(getClass().getResource("/hearts1.png"));
		} catch (IOException ioex) {
			System.exit(1);
		}

		// NORTH AND WEST
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		if (numOfAI <= 3)
			setNorth();
		if (numOfAI > 3 && numOfAI <= 5) {
			setNorth();
			setWest();
		}

		// CENTER
		// stack chips
		setPokerChips();
		pot.setBorder(BorderFactory.createLineBorder(Color.white, 3));
		pot.setBounds(326, 180, 630, 320);
		// space holding the five cards
		fiveCards.setBounds(336, 235, 430, 95);
		getContentPane().add(fiveCards);
		handLabel.setText("Hand " + handNumber);
		handLabel.setFont(new Font("Optima", Font.BOLD, 23));
		handLabel.setForeground(Color.yellow);
		pot.add(handLabel);
		moneyInPotLabel.setText("Money in the pot: " + String.valueOf(moneyInPot));
		moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
		moneyInPotLabel.setForeground(Color.white);
		pot.add(moneyInPotLabel);
		playerAction.setBounds(336, 363, 430, 132);
		getContentPane().add(playerAction);
		// "Dealer: "
		dealerLabel.setText("Dealer:");
		dealerLabel.setFont(new Font("Optima", Font.BOLD, 23));
		dealerLabel.setForeground(Color.yellow);
		dealerLabel.setBounds(772, 320, 100, 25);
		getContentPane().add(dealerLabel);
		// name of the dealer
		dealerIDLabel.setBounds(772, 293, 175, 132);
		getContentPane().add(dealerIDLabel);
		getContentPane().add(pot);

		// NORTH, WEST, AND EAST
		if (numOfAI > 5) {
			setNorth();
			setWest();
			setEast();
		}

		// SOUTH
		if (timeLimit) {
			timerTextLabel.setBounds(10, 500, 110, 30);
			timerTextLabel.setText("Time Left:");
			timerTextLabel.setFont(new Font("Optima", Font.BOLD, 20));
			timerTextLabel.setForeground(Color.white);

			timerPanel.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			timerPanel.setBounds(10, 531, 110, 40);

			timerLabel.setText("-- s");
			timerLabel.setFont(new Font("Optima", Font.BOLD, 20));
			timerLabel.setForeground(Color.white);
			timerPanel.add(timerLabel);

			getContentPane().add(timerTextLabel);
			getContentPane().add(timerPanel);
		}

		player.add(avatar);
		player.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		player.setBounds(135, 506, 1050, 196);

		centerHand.addCard(deck.get(cardCount--));
		centerHand.addCard(deck.get(cardCount--));
		centerHand.addCard(deck.get(cardCount--));
		initPlayer(player, -1, 0);
		getContentPane().add(player);
		betButton.setEnabled(false);

		// FORMATTING FOR USER OPTIONS (in SOUTH)
		p.add(betButton);
		p.add(callButton);
		p.add(smallBlind);
		p2.add(bet1Button);
		p2.add(bet5Button);
		p2.add(bet10Button);
		p2.add(bet25Button);
		p2.add(bet50Button);
		p2.add(bet100Button);

		player.add(p);
		p.add(p2);
		p.add(foldButton);
		p.add(bigBlind);
		p3.add(clearButton);
		betAmt.setPreferredSize(new Dimension(50, 24));

		clearButton.setPreferredSize(new Dimension(50, 24));
		p3.add(betAmt);
		betAmt.setEditable(false);
		p.add(p3);

		p.setVisible(true);
		p2.setVisible(true);
		p3.setVisible(true);

		// START GAME!
		showRound();
		setVisible(true);
		gameStart();
	}

	/*
	 * private void counter() { Thread timerThread = new Thread();
	 * 
	 * }
	 */

	private void counter() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = new Timer();
		timer.schedule(new timeTask(), 0, 1000);
		/*
		 * Thread timerThread = new Thread() { public void run() {} };
		 * timerThread.start();
		 */
	}

	private class timeTask extends TimerTask {
		int timeLeft = 25;

		public void run() {
			timerLabel.setText(timeLeft + " s");
			timerLabel.revalidate();
			timeLeft--;

			if (timeLeft == -1) {
				timerLabel.setText("FOLD");
				cancel();
				user.playerHasGone();
				logWriter.println(userName + " Has Folded.");
				user.setFold();
				try {
					remainingBets();
				} catch (InterruptedException | IOException e1) {
				}
			}
		}
	}

	/**
	 * Display the center hand cards
	 */
	private void displayCenterCards(Hand centerHand, int round) {
		if (round == -1)
			return;
		ImageIcon tempIcon;
		Image tempImg;
		Image tempImg2;
		Image cardImg;

		JLabel card1Display = new JLabel();
		JLabel card2Display = new JLabel();
		JLabel card3Display = new JLabel();
		JLabel card4Display = new JLabel();
		JLabel card5Display = new JLabel();

		try {
			if (round == 1) {
				Card c1 = centerHand.getCard(0);
				Card c2 = centerHand.getCard(1);
				Card c3 = centerHand.getCard(2);

				cardImg = ImageIO.read(getClass().getResource(c1.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card1Display.setIcon(tempIcon);

				cardImg = ImageIO.read(getClass().getResource(c2.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card2Display.setIcon(tempIcon);

				cardImg = ImageIO.read(getClass().getResource(c3.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card3Display.setIcon(tempIcon);

				fiveCards.add(card1Display);
				fiveCards.add(card2Display);
				fiveCards.add(card3Display);
				fiveCards.revalidate();
			} else if (round == 2) {
				Card c4 = centerHand.getCard(3);
				cardImg = ImageIO.read(getClass().getResource(c4.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card4Display.setIcon(tempIcon);
				fiveCards.add(card4Display);
				fiveCards.revalidate();
			} else {
				Card c5 = centerHand.getCard(4);
				cardImg = ImageIO.read(getClass().getResource(c5.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card5Display.setIcon(tempIcon);
				fiveCards.add(card5Display);
				fiveCards.revalidate();
			}
			pot.revalidate();
		} catch (IOException ioex) {
		}
	}

	/**
	 * Show the round number
	 */
	private void showRound() {
		if (gameRound == -1)
			roundLabel.setText("Round: Preflop");
		else if (gameRound == 0)
			roundLabel.setText("Round: Flop");
		else if (gameRound == 1)
			roundLabel.setText("Round: Turn");
		else if (gameRound == 2)
			roundLabel.setText("Round: River");
		roundLabel.setFont(new Font("Optima", Font.BOLD, 23));
		roundLabel.setForeground(Color.yellow);
		pot.add(roundLabel);
		pot.revalidate();
	}

	/**
	 * Handle transitions and buttons
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void transition() throws InterruptedException, IOException {
		// currently in preflop; to enter the flop round

		if (gameRound == -1) {
			resetGone();
			user.newRoundNotGone();
			loop = 1;
			deal();
		} else if (gameRound == 0) {
			// go to the moves for the first round
			highBet = 0;
			callBet = 0;
			loop = 1;
			playerHasRaisedOrNewRound();
			// log the round name
			isBlind = false;
			logWriter.println("\nFlop:");
			displayCenterCards(centerHand, 1);
			centerHand.addCard(deck.get(cardCount--));
			showRound();
			betting();
		}
		// to enter the turn round
		else if (gameRound == 1) {
			highBet = 0;
			callBet = 0;
			loop = 1;
			playerHasRaisedOrNewRound();
			logWriter.println("\nTurn:");
			displayCenterCards(centerHand, 2);
			centerHand.addCard(deck.get(cardCount--));
			showRound();
			betting();
		}
		// to enter the river round
		else if (gameRound == 2) {
			highBet = 0;
			callBet = 0;
			loop = 1;
			playerHasRaisedOrNewRound();
			logWriter.println("\nRiver:");
			displayCenterCards(centerHand, 3);
			if (user.getFold())
				logWriter.println(userName + " folded");
			if (user.getFold()) {
				for (int i = 0; i < players.size(); i++) {
					logWriter.println(players.get(i).getName() + " calls");
				}
			}
			showRound();
			betting();
		}
		// to get the result
		else if (gameRound == 3) {
			highBet = 0;
			callBet = 0;
			loop = 1;
			playerHasRaisedOrNewRound();
			logWriter.println("\nFinal:");
			String result = new String();
			gameRound++;
			showRound();
			betting();

			// disable all the buttons for the user
			boolean tie = false;
			int tieIndex = -1;
			// display the cards

			int winnerIndex = -1;
			int maxScore = 0;
			// check the user and AIs' score
			if (!user.getFold()) {
				maxScore = user.getHand().checkScore(centerHand);
			}

			int aiHighestScore = 0;
			for (int i = 0; i < players.size(); i++) {

				if (!players.get(i).getFold()) {
					aiHighestScore = players.get(i).getHand().checkScore(centerHand);
					if (aiHighestScore > maxScore) {
						maxScore = aiHighestScore;
						winnerIndex = i;
					} else if (aiHighestScore == maxScore) {

						int tieBreak = 0;
						if (winnerIndex == -1) {

							tieBreak = tieBreaker(user, players.get(i));
							if (tieBreak == 2) {

								maxScore = aiHighestScore;
								winnerIndex = i;
							}
						} else {

							tieBreak = tieBreaker(players.get(winnerIndex), players.get(i));
							if (tieBreak == 2) {

								maxScore = aiHighestScore;
								winnerIndex = i;
							}
						}
						if (tieBreak == 0) {

							tie = true;
							tieIndex = i;
						}
					}
				}
			}
			// display AI cards
			if (numOfAI == 1)
				initPlayer(northAI1, 0, 1);
			else if (numOfAI == 2) {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
			} else if (numOfAI == 3) {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
				initPlayer(northAI3, 2, 1);
			} else if (numOfAI == 4) {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
				initPlayer(northAI3, 2, 1);
				initPlayer(westAI1, 3, 1);
			} else if (numOfAI == 5) {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
				initPlayer(northAI3, 2, 1);
				initPlayer(westAI1, 3, 1);
				initPlayer(westAI2, 4, 1);
			} else if (numOfAI == 6) {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
				initPlayer(northAI3, 2, 1);
				initPlayer(westAI1, 3, 1);
				initPlayer(westAI2, 4, 1);
				initPlayer(eastAI1, 5, 1);
			} else {
				initPlayer(northAI1, 0, 1);
				initPlayer(northAI2, 1, 1);
				initPlayer(northAI3, 2, 1);
				initPlayer(westAI1, 3, 1);
				initPlayer(westAI2, 4, 1);
				initPlayer(eastAI1, 5, 1);
				initPlayer(eastAI2, 6, 1);
			}

			if (winnerIndex == -1 && tie == false && !user.getFold()) {
				user.setStack(user.getStack() + moneyInPot);
				result = "\nYou win with " + handType(user) + ", and you win $" + moneyInPot;
				logWriter.println(result);
				// move the money in the pot to the user's pocket
				userStack.setText("Balance:" + user.getStack());
				userStack.setForeground(Color.white);
				player.revalidate();
			} else if (winnerIndex >= 0 && tie == false) {
				players.get(winnerIndex).setStack(players.get(winnerIndex).getStack() + moneyInPot);
				result = "The winner is " + players.get(winnerIndex).getName() + " with "
						+ handType(players.get(winnerIndex)) + ", and " + players.get(winnerIndex).getName() + " wins $"
						+ moneyInPot;
				logWriter.println(result);
			} else if (winnerIndex == -1 && tie == true) {
				user.setStack(user.getStack() + moneyInPot / 2);
				players.get(tieIndex).setStack(players.get(tieIndex).getStack() + moneyInPot / 2);
				result = "There is a tie between you and " + players.get(tieIndex).getName() + " with "
						+ handType(players.get(tieIndex)) + ", and " + "both of you recieve" + " wins $"
						+ moneyInPot / 2;
				logWriter.println(result);
			} else if (winnerIndex >= 0 && tie == true) {
				players.get(tieIndex).setStack(players.get(tieIndex).getStack() + moneyInPot / 2);
				players.get(winnerIndex).setStack(players.get(winnerIndex).getStack() + moneyInPot / 2);
				result = "There is a tie between " + players.get(tieIndex).getName() + " and "
						+ players.get(winnerIndex).getName() + " with " + handType(players.get(winnerIndex))
						+ " ,and both players recieve" + " wins $" + moneyInPot / 2;
				logWriter.println(result);
			} else {
				result = "\nYou win with " + handType(user) + ", and you win $" + moneyInPot;
				logWriter.println(result);
				// move the money in the pot to the user's pocket
				user.setStack(user.getStack() + moneyInPot);
				userStack.setText("Balance:" + user.getStack());
				userStack.setForeground(Color.white);
				player.revalidate();
			}

			// log the end time of the game and close the file writing
			// TODO: Check remaining player number and see if the writer
			// should be closed or not
			if (true) {
				
			}

			if (tie != true) {
				setPlayerWinningCardsIcon(winnerIndex);
			}

			user.clearHand();
			for (int j = 0; j < players.size(); j++) {
				players.get(j).clearHand();
			}

			if (timeLimit)
				timer.cancel();
			
			disableButtons(3);

			new resultFrame(result, user, players, this, logWriter, theme, avatar, timeLimit, winningC1Icon,
					winningC2Icon, winningC3Icon, winningC4Icon, winningC5Icon);
		} else
			return;
	}

	private void setPlayerWinningCardsIcon(int index) throws IOException {
		ArrayList<Integer> winningCards;
		if (index >= 0) {
			Player p = players.get(index);
			winningCards = p.getHand().getList();
			Card winningCard1 = p.getHand().getCard(winningCards.get(0));
			Card winningCard2 = p.getHand().getCard(winningCards.get(1));
			Card winningCard3 = p.getHand().getCard(winningCards.get(2));
			Card winningCard4 = p.getHand().getCard(winningCards.get(3));
			Card winningCard5 = p.getHand().getCard(winningCards.get(4));

			Image cardImg = ImageIO.read(getClass().getResource(winningCard1.getIndex() + ".png"));
			ImageIcon tempIcon = new ImageIcon(cardImg);
			Image tempImg = tempIcon.getImage();
			Image tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC1Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard2.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC2Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard3.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC3Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard4.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC4Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard5.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC5Icon = tempIcon;

		} else {
			winningCards = user.getHand().getList();
			Card winningCard1 = user.getHand().getCard(winningCards.get(0));
			Card winningCard2 = user.getHand().getCard(winningCards.get(1));
			Card winningCard3 = user.getHand().getCard(winningCards.get(2));
			Card winningCard4 = user.getHand().getCard(winningCards.get(3));
			Card winningCard5 = user.getHand().getCard(winningCards.get(4));

			Image cardImg = ImageIO.read(getClass().getResource(winningCard1.getIndex() + ".png"));
			ImageIcon tempIcon = new ImageIcon(cardImg);
			Image tempImg = tempIcon.getImage();
			tempIcon = new ImageIcon(tempImg);
			Image tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC1Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard2.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempIcon = new ImageIcon(tempImg);
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC2Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard3.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempIcon = new ImageIcon(tempImg);
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC3Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard4.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempIcon = new ImageIcon(tempImg);
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC4Icon = tempIcon;

			cardImg = ImageIO.read(getClass().getResource(winningCard5.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempIcon = new ImageIcon(tempImg);
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			winningC5Icon = tempIcon;
		}

	}

	private void deal() throws InterruptedException, IOException {

		if (dealerID < players.size()) {
			dealerIDLabel.setText(players.get(dealerID).getName());
		} else {
			dealerIDLabel.setText(user.getName());
		}

		if (dealerID != players.size() && dealerID < players.size()) {

			action = players.get(dealerID).getName() + " Has Dealt.";
		}

		else {
			action = userName + " Has Dealt.";
		}

		dealerIDLabel.setFont(new Font("Optima", Font.BOLD, 16));
		dealerIDLabel.setForeground(Color.white);
		dealerIDLabel.revalidate();
		logWriter.println(action);
		playerAction.setText(action);
		playerAction.setFont(new Font("Optima", Font.BOLD, 20));
		playerAction.setForeground(Color.white);
		playerAction.revalidate();
		// disableButtons();
		smallBlind();
	}

	/**
	 * To find who the next player is
	 * 
	 * @param cur
	 *            the current player's index
	 */
	public Player findNext(int cur) {
		Player next = null;
		if (players.size() == 1) {
			if (cur == 0)
				next = user;
			if (cur == 1)
				next = players.get(0);
		} else if (players.size() == 2) {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = user;
			if (cur == 2)
				next = players.get(0);
		} else if (players.size() == 3) {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = players.get(2);
			if (cur == 2)
				next = user;
			if (cur == 3)
				next = players.get(0);
		} else if (players.size() == 4) {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = players.get(2);
			if (cur == 2)
				next = user;
			if (cur == 4)
				next = players.get(3);
			if (cur == 3)
				next = players.get(0);
		} else if (players.size() == 5) {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = players.get(2);
			if (cur == 2)
				next = user;
			if (cur == 5)
				next = players.get(4);
			if (cur == 4)
				next = players.get(3);
			if (cur == 3)
				next = players.get(0);
		} else if (players.size() == 6) {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = players.get(2);
			if (cur == 2)
				next = players.get(5);
			if (cur == 5)
				next = user;
			if (cur == 6)
				next = players.get(4);
			if (cur == 4)
				next = players.get(3);
			if (cur == 3)
				next = players.get(0);
		} else {
			if (cur == 0)
				next = players.get(1);
			if (cur == 1)
				next = players.get(2);
			if (cur == 2)
				next = players.get(5);
			if (cur == 5)
				next = players.get(6);
			if (cur == 6)
				next = user;
			if (cur == 7)
				next = players.get(4);
			if (cur == 4)
				next = players.get(3);
			if (cur == 3)
				next = players.get(0);
		}
		return next;
	}

	/**
	 * Start a game and go into three transitions
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void gameStart() throws InterruptedException, IOException {
		// Dealer is randomly determined only in the first round
		if (handNumber == 1) {
			Random rand = new Random();
			dealerID = rand.nextInt(players.size() + 1);
			// NOTE: we increment handNumber in the resultFrame, not here
		}
		transition();

		// BUTTONS:
		bet1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// handles min bet amount
				if (user.getStack() >= betAmount + 1) {
					betAmount += 1;
					betAmt.setText("$" + betAmount);
					if (betAmount >= 10 && betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet1Button.setEnabled(false);
			}
		});

		bet5Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getStack() >= betAmount + 5) {
					betAmount += 5;
					betAmt.setText("$" + betAmount);
					if (betAmount >= 10 && betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet5Button.setEnabled(false);
			}
		});

		bet10Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getStack() >= betAmount + 10) {
					betAmount += 10;
					betAmt.setText("$" + betAmount);
					if (betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet10Button.setEnabled(false);
			}
		});
		bet25Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getStack() >= betAmount + 25) {
					betAmount += 25;
					betAmt.setText("$" + betAmount);
					if (betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet25Button.setEnabled(false);
			}
		});
		bet50Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getStack() >= betAmount + 50) {
					betAmount += 50;
					betAmt.setText("$" + betAmount);
					if (betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet50Button.setEnabled(false);
			}
		});
		bet100Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// hide the frame
				if (user.getStack() >= betAmount + 100) {
					betAmount += 100;
					betAmt.setText("$" + betAmount);
					if (betAmount >= highBet) {
						// handles minimum bet amt
						betButton.setEnabled(true);
					}
				} else
					bet100Button.setEnabled(false);
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// hide the frame
				betAmount = 0;
				betAmt.setText("$" + betAmount);
				betButton.setEnabled(false);
				bet1Button.setEnabled(true);
				bet5Button.setEnabled(true);
				bet10Button.setEnabled(true);
				bet25Button.setEnabled(true);
				bet50Button.setEnabled(true);
				bet100Button.setEnabled(true);
			}
		});
		betButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (betAmount <= user.getStack() || betAmount > highBet) {

					user.playerHasGone();
					logWriter.println(userName + " Has Bet " + betAmount);

					// update the pot money
					user.setStack(user.getStack() - betAmount);
					moneyInPot += betAmount;
					betOwner = user;
					userStack.setText("Balance:" + user.getStack());
					userStack.setForeground(Color.white);
					player.revalidate();
					highBet = betAmount;
					if (user.getStack() <= 0) {

						user.allIn();
					}
					betAmount = 0; // reset the bet amount
					betAmt.setText("$" + betAmount);
					moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
					moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
					moneyInPotLabel.setForeground(Color.white);
					pot.add(moneyInPotLabel);
					pot.revalidate();

					// bet button is disabled before the next round
					betButton.setEnabled(false);

					try {
						resetGone();
						remainingBets();
					} catch (InterruptedException | IOException e1) {
					}
				}
			}
		});
		smallBlind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.playerHasGone();
				user.setStack(user.getStack() - 10);
				userStack.setText("Balance:" + user.getStack());
				userStack.setForeground(Color.white);
				player.revalidate();
				moneyInPot += 10;
				moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
				moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
				moneyInPotLabel.setForeground(Color.white);
				pot.add(moneyInPotLabel);
				pot.revalidate();
				enableButtons();
				try {
					bigBlind();
				} catch (InterruptedException | IOException e1) {
				}
			}
		});
		bigBlind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.playerHasGone();
				user.setStack(user.getStack() - 20);
				moneyInPot += 20;
				userStack.setText("Balance:" + user.getStack());
				userStack.setForeground(Color.white);
				player.revalidate();
				moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
				moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
				moneyInPotLabel.setForeground(Color.white);
				pot.add(moneyInPotLabel);
				pot.revalidate();
				enableButtons();
				try {
					betting();

				} catch (InterruptedException | IOException e1) {
				}
			}
		});
		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.playerHasGone();
				logWriter.println(userName + " Has Folded.");
				user.setFold();
				try {
					remainingBets();
				} catch (InterruptedException | IOException e1) {
				}
			}
		});
		callButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.playerHasGone();
				if (isBlind == true) {

					if (user.getStack() <= 20) {
						moneyInPot += user.getStack();
						user.setStack(0);
					} else {
						moneyInPot += 20;
						user.setStack(user.getStack() - 20);
					}

				} else {

					if (user.getStack() <= highBet) {
						moneyInPot += user.getStack();
						user.setStack(0);
					} else {
						moneyInPot += highBet;
						user.setStack(user.getStack() - highBet);
					}

				}

				userStack.setText("Balance:" + user.getStack());
				userStack.setForeground(Color.white);
				player.revalidate();
				logWriter.println(userName + " Has Called.");
				try {
					remainingBets();
				} catch (InterruptedException | IOException e1) {
				}
			}
		});
	}

	/**
	 * Add the poker chips images
	 */
	private void setPokerChips() {
		// poker chips in the pot
		ImageIcon tempIcon = new ImageIcon(chips);
		Image tempImg = tempIcon.getImage();
		Image tempImg2 = tempImg.getScaledInstance(165, 108, java.awt.Image.SCALE_SMOOTH);
		tempIcon = new ImageIcon(tempImg2);
		JLabel chips = new JLabel();
		chips.setBounds(775, 215, 165, 108);
		chips.setIcon(tempIcon);
		getContentPane().add(chips);
		// stack image in the player area
		tempIcon = new ImageIcon(stackImage);
		tempImg = tempIcon.getImage();
		tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
		tempIcon = new ImageIcon(tempImg2);
		JLabel stacks = new JLabel();
		stacks.setBounds(15, 639, 65, 67);
		stacks.setIcon(tempIcon);
		getContentPane().add(stacks);
	}

	private void displayAICards(int i, JPanel panel, JLabel c1Dis, JLabel c2Dis) {
		Card c1 = players.get(i).getCard1();
		Card c2 = players.get(i).getCard2();
		try {
			Image cardImg = ImageIO.read(getClass().getResource(c1.getIndex() + ".png"));
			ImageIcon tempIcon = new ImageIcon(cardImg);
			Image tempImg = tempIcon.getImage();
			Image tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			c1Dis.setIcon(tempIcon);
			// players.get(i).setCardImage1(tempIcon);
			panel.add(c1Dis);

			cardImg = ImageIO.read(getClass().getResource(c2.getIndex() + ".png"));
			tempIcon = new ImageIcon(cardImg);
			tempImg = tempIcon.getImage();
			tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
			tempIcon = new ImageIcon(tempImg2);
			c2Dis.setIcon(tempIcon);
			// players.get(i).setCardImage2(tempIcon);
			panel.add(c2Dis);
		} catch (IOException ioex) {
		}
	}

	/**
	 * Initialize a player
	 */
	private void initPlayer(JPanel panel, int num, int display) {
		ImageIcon tempIcon = new ImageIcon(back);
		if (theme == 1)
			tempIcon = new ImageIcon(nvback);
		Image tempImg = tempIcon.getImage();
		Image tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
		tempIcon = new ImageIcon(tempImg2);
		String name;
		int stack;
		Image cardImg;
		JLabel card1Display = new JLabel();
		JLabel card2Display = new JLabel();
		Card c1 = null;
		Card c2 = null;
		Image aiAvatar = null;
		ImageIcon tempAvatar = null;
		if (num != -1) {
			card1Display.setIcon(tempIcon);
			card2Display.setIcon(tempIcon);
			panel.add(card1Display);
			panel.add(card2Display);
			if (display == 1 && !players.get(num).getFold()) {
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				displayAICards(num, panel, card1Display, card2Display);
				aiAvatar = players.get(num).getAvatar();
				tempAvatar = new ImageIcon(aiAvatar);
				name = players.get(num).getName();
				stack = players.get(num).getStack();
				JLabel nameL = new JLabel(name);
				JLabel label = new JLabel();
				label.setIcon(tempAvatar);
				label.setText("Balance: " + players.get(num).getStack());
				label.setForeground(Color.white);
				panel.add(label);
				panel.add(nameL);
				return;
			}
			else if(display == 1) {
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				aiAvatar = players.get(num).getAvatar();
				tempAvatar = new ImageIcon(aiAvatar);
				name = players.get(num).getName();
				stack = players.get(num).getStack();
				JLabel nameL = new JLabel(name);
				JLabel label = new JLabel();
				label.setIcon(tempAvatar);
				label.setText("Balance: " + players.get(num).getStack());
				label.setForeground(Color.white);
				panel.add(label);
				panel.add(nameL);
				return;
			}
			c1 = deck.get(cardCount--);
			c2 = deck.get(cardCount--);
			aiAvatar = players.get(num).getAvatar();
			tempAvatar = new ImageIcon(aiAvatar);
			name = players.get(num).getName();
			stack = players.get(num).getStack();
			players.get(num).setCard1(c1);
			players.get(num).setCard2(c2);
			log(name, 0, c1, c2);
			JLabel label = new JLabel();
			label.setIcon(tempAvatar);
			label.setText("Balance: " + players.get(num).getStack());
			label.setForeground(Color.white);
			panel.add(label);
			JLabel nameL = new JLabel(name);
			panel.add(nameL);
		} else {
			c1 = deck.get(cardCount--);
			c2 = deck.get(cardCount--);
			Card userC1 = new Card(c1.getRank(), c1.getSuit());
			Card userC2 = new Card(c2.getRank(), c2.getSuit());
			stack = user.getStack();
			user.setCard1(userC1);
			user.setCard2(userC2);
			log(userName, 0, c1, c2);
			try {
				cardImg = ImageIO.read(getClass().getResource(c1.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card1Display.setIcon(tempIcon);

				// user.setCardImage1(tempIcon);
				cardImg = ImageIO.read(getClass().getResource(c2.getIndex() + ".png"));
				tempIcon = new ImageIcon(cardImg);
				tempImg = tempIcon.getImage();
				tempImg2 = tempImg.getScaledInstance(65, 80, java.awt.Image.SCALE_SMOOTH);
				tempIcon = new ImageIcon(tempImg2);
				card2Display.setIcon(tempIcon);

				// user.setCardImage2(tempIcon);
				panel.add(card1Display);
				panel.add(card2Display);
			} catch (IOException ioex) {
			}
			userNameL.setText(userName);
			panel.add(userNameL);
			userStack.setText("Balance:" + user.getStack());
			userStack.setForeground(Color.white);
			panel.add(userStack);
			panel.revalidate();
		}
	}

	/**
	 * Set the north AI players
	 */
	private void setNorth() {
		if (theme == 1)
			northAI1.setBackground(new Color(31, 114, 205));
		else
			northAI1.setBackground(new Color(43, 151, 0));
		northAI1.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		northAI1.setBounds(95, 30, 310, 130);
		initPlayer(northAI1, 0, 0);
		getContentPane().add(northAI1);
		if (numOfAI == 2 || numOfAI == 3 || numOfAI > 3) {
			if (theme == 1)
				northAI2.setBackground(new Color(31, 114, 205));
			else
				northAI2.setBackground(new Color(43, 151, 0));
			northAI2.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			northAI2.setBounds(481, 30, 310, 130);
			initPlayer(northAI2, 1, 0);
			getContentPane().add(northAI2);
		}
		if (numOfAI == 3 || numOfAI > 3) {
			if (theme == 1)
				northAI3.setBackground(new Color(31, 114, 205));
			else
				northAI3.setBackground(new Color(43, 151, 0));
			northAI3.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			northAI3.setBounds(866, 30, 310, 130);
			initPlayer(northAI3, 2, 0);
			getContentPane().add(northAI3);
		}
	}

	/**
	 * Set the west AI players
	 */
	private void setWest() {
		if (theme == 1)
			westAI1.setBackground(new Color(31, 114, 205));
		else
			westAI1.setBackground(new Color(43, 151, 0));
		westAI1.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		westAI1.setBounds(10, 180, 310, 130);
		initPlayer(westAI1, 3, 0);
		getContentPane().add(westAI1);
		if (numOfAI == 5 || numOfAI > 5) {
			if (theme == 1)
				westAI2.setBackground(new Color(31, 114, 205));
			else
				westAI2.setBackground(new Color(43, 151, 0));
			westAI2.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			westAI2.setBounds(10, 330, 310, 130);
			initPlayer(westAI2, 4, 0);
			getContentPane().add(westAI2);
		}
	}

	/**
	 * Set the east AI players
	 */
	private void setEast() {
		if (theme == 1)
			eastAI1.setBackground(new Color(31, 114, 205));
		else
			eastAI1.setBackground(new Color(43, 151, 0));
		eastAI1.setBorder(BorderFactory.createLineBorder(Color.white, 2));
		eastAI1.setBounds(962, 180, 310, 130);
		initPlayer(eastAI1, 5, 0);
		getContentPane().add(eastAI1);
		if (numOfAI == 7) {
			if (theme == 1)
				eastAI2.setBackground(new Color(31, 114, 205));
			else
				eastAI2.setBackground(new Color(43, 151, 0));
			eastAI2.setBorder(BorderFactory.createLineBorder(Color.white, 2));
			eastAI2.setBounds(962, 330, 310, 130);
			initPlayer(eastAI2, 6, 0);
			getContentPane().add(eastAI2);
		}
	}

	/**
	 * Log the event in a text file
	 */
	@SuppressWarnings("static-access")
	private void log(String name, int event, Card c1, Card c2) {
		// event: cards dealt
		if (event == 0) {
			logWriter.print(name + ": ");
			logWriter.print(c1.suitToString(c1.getSuit()) + " " + c1.rankToString(c1.getRank()).toLowerCase() + ", ");
			logWriter.println(c2.suitToString(c2.getSuit()) + " " + c2.rankToString(c2.getRank()).toLowerCase());
		}
		// event: calls
		if (event == 1) {
			logWriter.println(name + " calls");
		}
		// event: folds
		if (event == 2) {
			logWriter.println(name + " folds");
		}
		// pass c1 as the flop/river/turn card, c2 as null
		if (event == 3) {
			Card center1 = centerHand.getCard(0);
			Card center2 = centerHand.getCard(1);
			Card center3 = centerHand.getCard(2);
			logWriter.print("Flop: " + center1.suitToString(c1.getSuit()) + " "
					+ center1.rankToString(center1.getRank()).toLowerCase() + ", ");
			logWriter.print(center2.suitToString(center2.getSuit()) + " "
					+ center2.rankToString(center2.getRank()).toLowerCase() + ", ");
			logWriter.println(center3.suitToString(center3.getSuit()) + " "
					+ center3.rankToString(center3.getRank()).toLowerCase());
		}
		if (event == 4) {
			logWriter.println(
					"River: " + c1.suitToString(c1.getSuit()) + " " + c1.rankToString(c1.getRank()).toLowerCase());
		}
		if (event == 5) {
			logWriter.println(
					"Turn: " + c1.suitToString(c1.getSuit()) + " " + c1.rankToString(c1.getRank()).toLowerCase());
		}
	}

	private String handType(Player player) {

		int rank = player.getHand().getScore() % 100;
		int type = player.getHand().getScore() - rank;
		String rankString;

		switch (rank) {
		case 2:
			rankString = "Two";
			break;
		case 3:
			rankString = "Three";
			break;
		case 4:
			rankString = "Four";
			break;
		case 5:
			rankString = "Five";
			break;
		case 6:
			rankString = "Sixe";
			break;
		case 7:
			rankString = "Seven";
			break;
		case 8:
			rankString = "Eight";
			break;
		case 9:
			rankString = "Nine";
			break;
		case 10:
			rankString = "Ten";
			break;
		case 11:
			rankString = "Jack";
			break;
		case 12:
			rankString = "Queen";
			break;
		case 13:
			rankString = "King";
			break;
		case 14:
			rankString = "Ace";
			break;
		default:
			rankString = "Two";
			break;
		}

		switch (type) {
		case 1000:
			return "A Royale Flush";
		case 900:
			return ("A " + rankString + "High Stright Flush");
		case 800:
			return ("A Four of a Kind with " + rankString + "s");
		case 700:
			return ("A Full House,  " + rankString + "s High");
		case 600:
			return ("A " + rankString + " High Flush");
		case 500:
			return ("A " + rankString + " High Straight");
		case 400:
			return ("A Three of a Kind with " + rankString + "s");
		case 300:
			return ("Two Pair, " + rankString + "s High");
		case 200:
			return ("A pair of " + rankString + "s");
		case 100:
			return (rankString + "s High");
		default:
			return (rankString + "s High");
		}
	}

	private int tieBreaker(Player player1, Player player2) {
		int player1HighCard;
		int player2HighCard;
		int player1Kicker;
		int player2Kicker;
		if (player1.getCard1().getRank() == 1) {

			player1HighCard = 14;
			player1Kicker = player1.getCard2().getRank();
		} else if (player1.getCard2().getRank() == 1) {

			player1HighCard = 14;
			player1Kicker = player1.getCard1().getRank();
		} else if (player1.getCard1().getRank() > player1.getCard2().getRank()) {

			player1HighCard = player1.getCard1().getRank();
			player1Kicker = player1.getCard2().getRank();
		} else {

			player1HighCard = player1.getCard2().getRank();
			player1Kicker = player1.getCard1().getRank();
		}
		if (player2.getCard1().getRank() == 1) {

			player2HighCard = 14;
			player2Kicker = player2.getCard2().getRank();
		} else if (player2.getCard2().getRank() == 1) {

			player2HighCard = 14;
			player2Kicker = player2.getCard1().getRank();
		} else if (player2.getCard1().getRank() > player2.getCard2().getRank()) {

			player2HighCard = player2.getCard1().getRank();
			player2Kicker = player2.getCard2().getRank();
		} else {

			player2HighCard = player2.getCard2().getRank();
			player2Kicker = player2.getCard1().getRank();

		}
		if (player1HighCard > player2HighCard) {

			return 1;
		}

		else if (player1HighCard < player2HighCard) {

			return 2;
		} else if (player1Kicker > player2Kicker) {

			return 1;
		} else if (player1Kicker < player2Kicker) {

			return 2;
		} else if(player1HighCard == player2HighCard && player1Kicker == player2Kicker) {

			return 0;
		}
		else {
			
			return 1;
		}
	}

	public int getHandNumber() {
		return handNumber;
	}

	public void setHandNumber(int newHandNumber) {
		handNumber = newHandNumber;
	}

	public int getDealerID() {
		return dealerID;
	}

	public void setDealerID(int dealerNum) {
		do {
			if (dealerNum == 4)
				dealerNum = 3;
			else if (dealerNum == 3)
				dealerNum = 0;
			else if (dealerNum == 2 && players.size() <= 5) {
				if (players.size() == 2) {
					dealerNum = 0;
				} else {
					dealerNum = players.size();
				}
				break;
			} else if (dealerNum == 2)
				dealerNum = 5;
			// 2 -> user if the size == 3
			else if (dealerNum + 2 > players.size() && dealerNum != players.size()) {
				dealerNum = players.size();
				break;
			} else if (dealerNum == 0 || dealerNum == 1 || dealerNum == 5) {
				if (dealerNum == 5) {
					if (players.size() == 5)
						dealerNum = 4;
					if (players.size() == 7)
						dealerNum = 6;
					if (players.size() == 4)
						dealerNum = 3;
					if (players.size() == 3)
						dealerNum = 0;
				} else if (dealerNum == 1 && players.size() == 1) {
					dealerNum = 0;
				} else if (dealerNum == 0 && players.size() == 2) {
					dealerNum = 1;
				} else if (dealerNum == 0 && players.size() == 3)
					dealerNum = 1;
				else
					dealerNum++;
			} else {
				if (players.size() >= 5)
					dealerNum = 4;
				if (players.size() == 4)
					dealerNum = 3;
				if (players.size() <= 3)
					dealerNum = 0;
			}
		} while (players.get(dealerNum).ifOutOfGame() && !user.ifOutOfGame());
		dealerID = dealerNum;
	}

	public boolean isPlayersEmpty() {
		return players.size() == 0;
	}

	public int getPlayerIndex(Player p) {
		int index = -1;
		for (int i = 0; i < players.size(); i++)
			if (players.get(i).getName().equals(p.getName()) || user.getName().equals(p.getName())) {
				index = i;
			}
		return index;
	}

	public JLabel getDealerIDLabel() {
		return dealerIDLabel;
	}

	// 0 - raise; 1 - fold; 2 - call;
	public String aiRandomAction(int round, int aiIndex, Player p) throws InterruptedException {
		String action = "";
		int moves = -10;

		if (round == 0) {

			moves = p.AIBehaviour(isBlind, centerHand, loop, highBet);

			if (moves == 0) {
				int betAmt = 20;
				if ((p.getStack() - betAmt) <= 0) {
					betAmt = p.getStack();
				}
				p.call(20);
				action = p.getName() + " Has Called " + betAmt;
				moneyInPot = betAmt + moneyInPot;
				JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(p)).getComponent(2));
				tempLabel.setText("Balance: " + p.getStack());
				tempLabel.setForeground(Color.white);

				logWriter.println(action);
				playerAction.setText(action);
				playerAction.setFont(new Font("Optima", Font.BOLD, 20));
				playerAction.setForeground(Color.white);
				playerAction.revalidate();

				// update money in pot
				moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
				moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
				moneyInPotLabel.setForeground(Color.white);
				pot.add(moneyInPotLabel);
				pot.revalidate();
			} else {
				p.fold();

				if (playersStillInTheGame() == true) {

					action = p.getName() + " Has Folded.";
					JPanel panel = new JPanel();
					panel = getPanelNum(getPlayerIndex(p));
					removeAICards(panel, getPlayerIndex(p));
				} else {

					p.newRoundUnFold();
					int call = p.call(20);
					action = p.getName() + " Has Called.";
					moneyInPot = call + moneyInPot;
					JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(p)).getComponent(2));
					tempLabel.setText("Balance: " + p.getStack());
					tempLabel.setForeground(Color.white);
					logWriter.println(action);
					playerAction.setText(action);
					playerAction.setFont(new Font("Optima", Font.BOLD, 20));
					playerAction.setForeground(Color.white);
					playerAction.revalidate();

					// update money in pot
					moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
					moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
					moneyInPotLabel.setForeground(Color.white);
					pot.add(moneyInPotLabel);
					pot.revalidate();
				}

			}
		}

		// if in the first round
		else {
			moves = p.AIBehaviour(isBlind, centerHand, loop, highBet);
			if (moves == 0 && p.getStack() <= 0) {
				moves++;
			}

			if (moves >= 0) {

				int betAmt = moves;

				action = p.getName() + " Has Bet " + betAmt;
				moneyInPot = betAmt + moneyInPot;
				logWriter.println(action);
				playerAction.setText(action);
				playerAction.setFont(new Font("Optima", Font.BOLD, 20));
				playerAction.setForeground(Color.white);
				playerAction.revalidate();
				highBet = betAmt;
				// update money in pot
				moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
				moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
				moneyInPotLabel.setForeground(Color.white);
				pot.add(moneyInPotLabel);
				pot.revalidate();
				playerHasRaisedOrNewRound();
				user.newRoundNotGone();
				JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(p)).getComponent(2));
				tempLabel.setText("Balance: " + p.getStack());
				tempLabel.setForeground(Color.white);
				betOwner = p;
			} else if (moves == -1) {
				p.fold();
				if (playersStillInTheGame() == true || user.getFold() == false) {

					action = p.getName() + " Has Folded.";
					JPanel panel = new JPanel();
					panel = getPanelNum(getPlayerIndex(p));
					removeAICards(panel, getPlayerIndex(p));
				} else {

					p.newRoundUnFold();
					int call = p.call(highBet);
					moneyInPot = call + moneyInPot;
					action = p.getName() + " Has Called.";
					// update money in pot
					moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
					moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
					moneyInPotLabel.setForeground(Color.white);
					pot.add(moneyInPotLabel);
					pot.revalidate();
					JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(p)).getComponent(2));
					tempLabel.setText("Balance: " + p.getStack());
					tempLabel.setForeground(Color.white);
				}

			} else {
				int call = p.call(highBet);
				action = p.getName() + " Has Called.";
				moneyInPot = call + moneyInPot;
				logWriter.println(action);
				playerAction.setText(action);
				moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
				moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
				moneyInPotLabel.setForeground(Color.white);
				pot.add(moneyInPotLabel);
				pot.revalidate();
				JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(p)).getComponent(2));
				tempLabel.setText("Balance: " + p.getStack());
				tempLabel.setForeground(Color.white);
			}
		}
		return action;
	}

	public void disableButtons(int bigOrSmall) {
		betButton.setEnabled(false);
		bet1Button.setEnabled(false);
		bet5Button.setEnabled(false);
		bet10Button.setEnabled(false);
		bet25Button.setEnabled(false);
		bet50Button.setEnabled(false);
		bet100Button.setEnabled(false);
		if (bigOrSmall == 0) {
			smallBlind.setEnabled(true);
			bigBlind.setEnabled(false);
			foldButton.setEnabled(false);
			callButton.setEnabled(false);
		}
		if (bigOrSmall == 1) {
			smallBlind.setEnabled(false);
			bigBlind.setEnabled(true);
			foldButton.setEnabled(false);
			callButton.setEnabled(false);
		}
		if (bigOrSmall == 2) {
			smallBlind.setEnabled(false);
			bigBlind.setEnabled(false);
			foldButton.setEnabled(true);
			callButton.setEnabled(true);
		}
		if(bigOrSmall == 3) {
			
			smallBlind.setEnabled(false);
			bigBlind.setEnabled(false);
			foldButton.setEnabled(false);
			callButton.setEnabled(false);
		}

		clearButton.setEnabled(false);
	}

	public void enableButtons() {
		betButton.setEnabled(false);
		foldButton.setEnabled(true);

		if (highBet == 0) {

			callButton.setText("Check");
		}

		else {

			callButton.setText("Call");
		}
		callButton.setEnabled(true);
		bet1Button.setEnabled(true);
		bet5Button.setEnabled(true);
		bet10Button.setEnabled(true);
		bet25Button.setEnabled(true);
		bet50Button.setEnabled(true);
		bet100Button.setEnabled(true);
		smallBlind.setEnabled(false);
		bigBlind.setEnabled(false);
		clearButton.setEnabled(true);
	}

	private void smallBlind() throws InterruptedException, IOException {

		user.newRoundUnFold();
		user.newRoundNotAllIn();
		for (int i = 0; i < players.size(); i++) {
			players.get(i).newRoundUnFold();
			players.get(i).newRoundNotGone();
		}
		moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
		moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
		moneyInPotLabel.setForeground(Color.white);
		pot.add(moneyInPotLabel);
		pot.revalidate();
		int cur = dealerID;
		Player nextS = null;
		nextS = findNext(cur);
		if (nextS != user) {
			action = nextS.getName() + " is the small blind.";
			players.get(getPlayerIndex(nextS)).setStack(players.get(getPlayerIndex(nextS)).getStack() - 10);
			JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(nextS)).getComponent(2));
			tempLabel.setText("Balance: " + nextS.getStack());
			tempLabel.setForeground(Color.white);
			tempLabel.revalidate();
			moneyInPot += 10;
			playerAction.setText(action);
			playerAction.setFont(new Font("Optima", Font.BOLD, 20));
			playerAction.setForeground(Color.white);
			playerAction.revalidate();
			pot.revalidate();
			nextS.playerHasGone();
			bigBlind();
		}
		// if user is the small blind
		else {
			logWriter.println("You are the small blind");

			playerAction.setText("You are the small blind");
			playerAction.setFont(new Font("Optima", Font.BOLD, 20));
			playerAction.setForeground(Color.white);
			playerAction.revalidate();
			disableButtons(0);
			// update money in pot
			// TimeUnit.MILLISECONDS.sleep(520);
			// handle buttons
		}

	}

	public void bigBlind() throws InterruptedException, IOException {

		moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
		moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
		moneyInPotLabel.setForeground(Color.white);
		pot.add(moneyInPotLabel);
		pot.revalidate();
		Player nextS = null;
		int cur = getDealerID();
		nextS = findNext(cur);

		int sbIndex = -1;
		if (nextS != user && nextS != null)
			sbIndex = getPlayerIndex(nextS);
		else
			sbIndex = players.size();
		// big blind
		int curS = sbIndex;
		Player nextB = null;
		do {
			nextB = findNext(curS);
		} while (nextB.ifOutOfGame());
		// if the next one is not the user
		if (nextB != user) {
			action = nextB.getName() + " is the big blind.";
			players.get(getPlayerIndex(nextB)).setStack(players.get(getPlayerIndex(nextB)).getStack() - 20);

			JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(nextB)).getComponent(2));
			tempLabel.setText("Balance: " + nextB.getStack());
			tempLabel.setForeground(Color.white);
			tempLabel.revalidate();
			moneyInPot += 20;
			playerAction.setText(action);
			playerAction.setFont(new Font("Optima", Font.BOLD, 20));
			playerAction.setForeground(Color.white);
			playerAction.revalidate();
			logWriter.println(action);
			pot.revalidate();
			nextB.playerHasGone();
			betting();

		}
		// If user is the big blind
		else {
			pot.revalidate();
			action = nextB.getName() + " is the big blind.";
			logWriter.println("You are the big blind");
			playerAction.setText("You are the big blind");
			playerAction.setFont(new Font("Optima", Font.BOLD, 20));
			playerAction.setForeground(Color.white);
			playerAction.revalidate();
			playerAction.setText(action);
			playerAction.setFont(new Font("Optima", Font.BOLD, 20));
			playerAction.setForeground(Color.white);
			playerAction.revalidate();
			logWriter.println(action);
			pot.revalidate();
			disableButtons(1);
			// update money in pot

		}
	}

	public void betting() throws InterruptedException, IOException {

		// for players not folding
		if (highBet > 0) {

			logWriter.println("Player's turn, Calling Bets " + highBet);
			playerAction.setText(betOwner.getName() + " Has the High Bet \n Calling Bets " + highBet);
		} else {

			logWriter.println("Player's turn, Calling Bets " + highBet);
			playerAction.setText("Player's turn, Calling Bets " + highBet);
		}
		playerAction.setFont(new Font("Optima", Font.BOLD, 20));
		playerAction.setForeground(Color.white);
		playerAction.revalidate();
		moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
		moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
		moneyInPotLabel.setForeground(Color.white);
		pot.add(moneyInPotLabel);
		pot.revalidate();
		int cur = getDealerID();
		callBet = highBet;
		Player nextB = findNext(cur);
		int bbIndex = -1;
		int scoreCheck = highBet;

		if (playersStillInTheGame() && !user.getFold()) {

			if (nextB != user && nextB != null) {

				bbIndex = getPlayerIndex(nextB);
			}

			else if (user.getFold() && nextB != null) {

				bbIndex = getPlayerIndex(nextB);
				nextB = findNext(bbIndex);
				bbIndex = getPlayerIndex(nextB);
			}

			else {

				if (isBlind == true && !user.hasGone()) {

					logWriter.println("Player's turn, Calling Bets 20");
					playerAction.setText("Player's turn, Calling Bets 20");
					disableButtons(2);
					if (timeLimit)
						counter();
				} else if (!user.hasGone() && playersStillInTheGame()) {

					if (highBet > 0) {

						logWriter.println("Player's turn, Calling Bets " + highBet);
						playerAction.setText(betOwner.getName() + " Has the High Bet\n Calling Bets " + highBet);
					} else {

						logWriter.println("Player's turn, Calling Bets " + highBet);
						playerAction.setText("Player's turn, Calling Bets " + highBet);
					}
					if (timeLimit)
						counter();
					enableButtons();
				}

				bbIndex = getPlayerIndex(nextB);
				nextB = findNext(bbIndex);
				bbIndex = getPlayerIndex(nextB);

			}

			Player next = nextB;
			for (int i = 0; i < players.size() + 1; i++) {

				if (!next.getFold() && !next.hasGone()) {
					if (next != user) {
						if (isBlind == true) {
							aiRandomAction(0, getPlayerIndex(next), next);
						} else {
							aiRandomAction(1, getPlayerIndex(next), next);
						}

						if (next.getFold() == false) {

							JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(next)).getComponent(2));
							tempLabel.setText("Balance: " + next.getStack());
							tempLabel.setForeground(Color.white);
							tempLabel.revalidate();
						}

						moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
						moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
						moneyInPotLabel.setForeground(Color.white);
						pot.add(moneyInPotLabel);
						pot.revalidate();
						next.playerHasGone();
						bbIndex = getPlayerIndex(next);
						if (bbIndex == -1)
							bbIndex = players.size();
						next = findNext(bbIndex);
					} else if (!user.getFold() && playersStillInTheGame()) {

						if (isBlind == true && !user.hasGone()) {

							logWriter.println("Player's turn, Calling Bets 20");
							playerAction.setText("Player's turn, Calling Bets 20");
							if (timeLimit)
								counter();
							disableButtons(2);
						} else if (!user.hasGone()) {
							if (highBet > 0) {

								logWriter.println("Player's turn, Calling Bets " + highBet);
								playerAction
										.setText(betOwner.getName() + " Has the High Bet\n Calling Bets " + highBet);
							} else {

								logWriter.println("Player's turn, Calling Bets " + highBet);
								playerAction.setText("Player's turn, Calling Bets " + highBet);
							}
							if (timeLimit)
								counter();
							enableButtons();
							break;

						}
						playerAction.setFont(new Font("Optima", Font.BOLD, 20));
						playerAction.setForeground(Color.white);
						playerAction.revalidate();
						pot.revalidate();

						bbIndex = getPlayerIndex(next);
						if (bbIndex == -1)
							bbIndex = players.size();
						next = findNext(bbIndex);

					}
				}
				if (next == user && user.hasGone()) {
					next = findNext(bbIndex + 1);
				}

				else if (next.hasGone()) {

					bbIndex = getPlayerIndex(next);
					next = findNext(bbIndex);

				} else if (!next.hasGone() && next.getFold()) {

					bbIndex = getPlayerIndex(next);
					next = findNext(bbIndex);

				} else {

					next = findNext(bbIndex);
				}

			}
			if (user.getFold()) {
				remainingBets();

			} else if (user.hasGone()) {
				user.newRoundNotGone();
				resetGone();
				highBet = 0;
				callBet = 0;
				gameRound++;
				transition();
			}
		}
		else {
			
			user.newRoundNotGone();
			resetGone();
			highBet = 0;
			callBet = 0;
			gameRound++;
			transition();
		}

	}

	public void remainingBets() throws InterruptedException, IOException {

		pot.revalidate();
		int cur = getDealerID();
		Player nextB = findNext(cur);
		int bbIndex = -1;
		if (nextB != user && nextB != null) {

			bbIndex = getPlayerIndex(nextB);
		}

		else {
			bbIndex = players.size();

		}

		Player next = nextB;
		for (int i = 0; i < players.size() + 1; i++) {

			if (!next.getFold() && !next.hasGone()) {
				if (next != user) {
					if (isBlind == true) {

						aiRandomAction(0, getPlayerIndex(next), next);
					} else {

						aiRandomAction(1, getPlayerIndex(next), next);
					}

					if (next.getFold() == false) {

						JLabel tempLabel = ((JLabel) getPanelNum(getPlayerIndex(next)).getComponent(2));
						tempLabel.setText("Balance: " + next.getStack());
						tempLabel.setForeground(Color.white);
						tempLabel.revalidate();
					}
					moneyInPotLabel.setText("Money in the pot: " + moneyInPot);
					moneyInPotLabel.setFont(new Font("Optima", Font.BOLD, 23));
					moneyInPotLabel.setForeground(Color.white);
					pot.add(moneyInPotLabel);
					pot.revalidate();
					next.playerHasGone();
					bbIndex = getPlayerIndex(next);
					if (bbIndex == -1) {

						bbIndex = players.size();
					}
				}
			}
			bbIndex = getPlayerIndex(next);
			if (bbIndex == -1) {
				bbIndex = players.size();
			}
			if (next == user) {
				next = findNext(bbIndex + 1);
			} else {
				next = findNext(bbIndex);
			}
		}

		if (highBet > callBet) {
			if (!user.getFold()) {
				if (highBet > 0) {

					logWriter.println("Player's turn, Calling Bets " + highBet);
					playerAction.setText(betOwner.getName() + " Has the High Bet\n Calling Bets " + highBet);
				} else {

					logWriter.println("Player's turn, Calling Bets " + highBet);
					playerAction.setText("Player's turn, Calling Bets " + highBet);
				}
				playerAction.setFont(new Font("Optima", Font.BOLD, 20));
				playerAction.setForeground(Color.white);
				playerAction.revalidate();
				pot.revalidate();
			}
			loop++;
			betting();
		}

		else {
			resetGone();
			user.newRoundNotGone();
			highBet = 0;
			callBet = 0;
			gameRound++;
			transition();
		}

	}

	public void resetGone() throws InterruptedException {

		pot.revalidate();
		int cur = getDealerID();
		Player nextB = findNext(cur);
		int bbIndex = -1;
		if (nextB != user && nextB != null) {
			bbIndex = getPlayerIndex(nextB);
		}

		else {
			bbIndex = players.size();

		}

		Player next = null;
		next = findNext(bbIndex);
		for (int i = 0; i < players.size() + 1; i++) {

			if (!next.getFold()) {
				if (next != user) {
					pot.revalidate();
					next.newRoundNotGone();
					bbIndex = getPlayerIndex(next);
					if (bbIndex == -1)
						bbIndex = players.size();
					next = findNext(bbIndex);
				} else {

					bbIndex = getPlayerIndex(next) + 1;
					if (bbIndex == -1)
						bbIndex = players.size();
					next = findNext(bbIndex);
				}

			}
		}
	}

	private void removeAICards(JPanel panel, int index) {
		JLabel nameL = new JLabel();
		JLabel stackL = new JLabel();
		stackL.setText("Balance: " + players.get(index).getStack());
		stackL.setForeground(Color.white);
		panel.removeAll();
		nameL = new JLabel(players.get(index).getName());
		panel.add(stackL);
		panel.add(nameL);
		panel.repaint();
		panel.revalidate();
	}

	private JPanel getPanelNum(int index) {
		if (index == 0)
			return northAI1;
		else if (index == 1)
			return northAI2;
		else if (index == 2)
			return northAI3;
		else if (index == 3)
			return westAI1;
		else if (index == 4)
			return westAI2;
		else if (index == 5)
			return eastAI1;
		else if (index == 6)
			return eastAI2;
		return null;
	}

	private void playerHasRaisedOrNewRound() {

		user.newRoundNotGone();
		for (int i = 0; i < players.size(); i++) {

			players.get(i).newRoundNotGone();

		}
	}

	private boolean playersStillInTheGame() {

		for (int i = 0; i < players.size(); i++) {

			if (players.get(i).getFold() == false) {

				return true;
			}
		}
		return false;
	}
}
