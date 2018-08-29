import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Hand {

	private ArrayList<Card> hand;
	private int score;
	private ArrayList<Integer> winningCards;

	public Hand() {

		hand = new ArrayList<>();
		winningCards = new ArrayList<>(5);
	}

	public boolean isEmpty() {
		return hand.size() == 0;
	}

	public int getSize() {
		return hand.size();
	}

	public int getScore() {

		return score;
	}
	
	public ArrayList<Integer> getList() {
		int temp = -1;
		int a = 0;
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getIndex().equals("") || hand.get(i).getIndex() == null) {
				hand.get(i).setCardIndex(hand.get(i).getSuit(), hand.get(i).getRank());
			}
		}
		while (winningCards.size() < 5) {
			a = 0;
			for (int i = 0; i < hand.size(); i++) {
				if (!winningCards.contains(i) && hand.get(i).getRank() > a
						|| !winningCards.contains(i) && hand.get(i).getRank() == 1) {
					if (hand.get(i).getRank() == 1) {
						a = 14;
						temp = i;
					} else {
						temp = i;
						a = hand.get(i).getRank();
					}
				}
			}
			winningCards.add(temp);
		}
		return winningCards;
	}

	// implement FLOP transition
	public Hand(Card card1, Card card2, Card card3) {
		hand = new ArrayList<>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		score = 0;
	}

	public void addCard(Card newCard) {
		hand.add(newCard);
	}

	public void addCard(Card newCard, int number) {


		hand.add(number, newCard);
	}


	public Card getCard(int card) {
		return hand.get(card);
	}

	private void combineHands(Hand centerHand, ArrayList<Card> checkHand) {
		for (int i = 0; i < centerHand.getSize(); i++) {
			checkHand.add(centerHand.getCard(i));
		}
	}

	public int preFinishCheck(Hand centerHand, Card card1, Card card2) {

		ArrayList<Card> checkHand = new ArrayList<>();
		checkHand.add(card1);
		checkHand.add(card2);
		combineHands(centerHand, checkHand);

		// score of 1000
		if (checkRoyalFlush(checkHand) != 0) {

			score = checkRoyalFlush(checkHand);
			return score;
		}

		// score of 900 + high rank (accept ace, as that would be a royal flush)
		else if (checkStraightFlush(checkHand) != 0) {

			score = checkStraightFlush(checkHand);
			return score;
		}

		// score of 800 + rank
		else if (checkFourOfAKind(checkHand) != 0) {

			score = checkFourOfAKind(checkHand);
			return score;
		}

		// score of 700 + rank of three of a kind
		else if (checkFullHouse(checkHand) != 0) {

			score = checkFullHouse(checkHand);
			return score;
		}

		// score of 600 + high rank
		else if (checkFlush(checkHand) != 0) {

			score = checkFlush(checkHand);
			return score;
		}

		// score of 500 + high card
		else if (checkStraight(checkHand) != 0) {

			score = checkStraight(checkHand);
			return score;
		}

		// score of 400 + rank
		else if (checkThreeOfAKind(checkHand) != 0) {

			score = checkThreeOfAKind(checkHand);
			return score;
		}

		// score of 300 + highest rank of pair
		else if (checkTwoPair(checkHand) != 0) {

			score = checkTwoPair(checkHand);
			return score;
		}

		// score of 200 + rank
		else if (checkPair(0, checkHand) != 0) {

			score = checkPair(0, checkHand);
			return score;

		}

		// score of 100 + high card
		else {

			score = checkHighCard(checkHand);
			return score;
		}

	}

	// Main method for checking and returning scores
	public int checkScore(Hand centerHand) {


		combineHands(centerHand, hand);


		// score of 1000
		if (checkRoyalFlush(hand) != 0) {

			score = checkRoyalFlush(hand);
			return score;
		}

		// score of 900 + high rank (accept ace, as that would be a royal flush)

		else if (checkStraightFlush(hand) != 0) {

			score = checkStraightFlush(hand);

			return score;
		}

		// score of 800 + rank

		else if (checkFourOfAKind(hand) != 0) {

			score = checkFourOfAKind(hand);

			return score;
		}

		// score of 700 + rank of three of a kind

		else if (checkFullHouse(hand) != 0) {

			score = checkFullHouse(hand);

			return score;
		}

		// score of 600 + high rank

		else if (checkFlush(hand) != 0) {

			score = checkFlush(hand);

			return score;
		}

		// score of 500 + high card

		else if (checkStraight(hand) != 0) {

			score = checkStraight(hand);

			return score;
		}

		// score of 400 + rank

		else if (checkThreeOfAKind(hand) != 0) {

			score = checkThreeOfAKind(hand);

			return score;
		}

		// score of 300 + highest rank of pair

		else if (checkTwoPair(hand) != 0) {

			score = checkTwoPair(hand);

			return score;
		}

		// score of 200 + rank

		else if (checkPair(0, hand) != 0) {

			score = checkPair(0, hand);

			return score;
		}

		// score of 100 + high card
		else {


			score = checkHighCard(hand);

			return score;
		}

	}

	private int checkRoyalFlush(ArrayList<Card> checkHand) {

		int suit = 0;
		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();
			// searches for an ace, than sets the royal flush rank accordingly

			if (checkHand.get(i).getRank() == 1) {

				winningCards.add(i);

				suit = checkHand.get(i).getSuit();
				for (int j = 0; j < checkHand.size(); j++) {

					// we proceed to search the rest of thecheckHand for the cards needed in the
					// suit needed
					if (checkHand.get(j).getRank() == 10 && checkHand.get(j).getSuit() == suit) {

						winningCards.add(j);

						for (int k = 0; k < checkHand.size(); k++) {

							if (checkHand.get(k).getRank() == 11 && checkHand.get(k).getSuit() == suit) {

								winningCards.add(k);

								for (int m = 0; m < checkHand.size(); m++) {

									if (checkHand.get(m).getRank() == 12 && checkHand.get(m).getSuit() == suit) {

										winningCards.add(m);

										for (int n = 0; n < checkHand.size(); n++) {

											if (checkHand.get(n).getRank() == 13
													&& checkHand.get(n).getSuit() == suit) {

												winningCards.add(n);

												return 1000;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;

	}

	private int checkStraightFlush(ArrayList<Card> checkHand) {

		int suit = 0;
		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();

			rank = checkHand.get(i).getRank();
			suit = checkHand.get(i).getSuit();
			// similar sorting to the royal flush, except the starting card is irrelevant
			for (int j = 0; j < checkHand.size(); j++) {

				if (checkHand.get(j).getRank() == rank + 1 && checkHand.get(j).getSuit() == suit) {

					winningCards.add(j);

					for (int k = 0; k < checkHand.size(); k++) {

						if (checkHand.get(k).getRank() == rank + 2 && checkHand.get(k).getSuit() == suit) {

							winningCards.add(k);

							for (int m = 0; m < checkHand.size(); m++) {

								if (checkHand.get(m).getRank() == rank + 3 && checkHand.get(m).getSuit() == suit) {

									winningCards.add(m);

									for (int n = 0; n < checkHand.size(); n++) {

										// if we were to happen to get a card ending on an ace, we would have a royal
										// flush
										// therefore, the straight flush cannot end with a 14 score

										if (checkHand.get(n).getRank() == rank + 4
												&& checkHand.get(n).getSuit() == suit) {

											winningCards.add(n);

											for (int o = 0; o < checkHand.size(); o++) {

												if (checkHand.get(o).getRank() == rank + 5
														&& checkHand.get(o).getSuit() == suit) {
													winningCards.add(o);

													for (int p = 0; p < checkHand.size(); p++) {


														if (checkHand.get(p).getRank() == rank + 6 && checkHand.get(p).getSuit() == suit) {
															winningCards.add(p);
															winningCards.remove(0);

															return 900 + (rank + 6);
														}
													}
													return 900 + (rank + 5);
												}
											}
											return 900 + (rank + 4);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	private int checkFourOfAKind(ArrayList<Card> checkHand) {

		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();
			rank = checkHand.get(i).getRank();
			int count = 0;
			winningCards.add(i);
			// we check a card rank, and see if we can find four of them in the deck

			for (int x = 0; x < checkHand.size(); x++) {

				if (checkHand.get(x).getRank() == rank && x != i) {

					winningCards.add(x);
					count++;
				}

				if (count == 3) {

					if (rank == 1) {

						return 814;
					} else {

						return 800 + rank;
					}
				}
			}
		}
		return 0;
	}

	private int checkThreeOfAKind(ArrayList<Card> checkHand) {

		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();
			rank = checkHand.get(i).getRank();
			int count = 0;
			winningCards.add(i);
			for (int x = 0; x < checkHand.size(); x++) {

				if (checkHand.get(x).getRank() == rank && x != i) {

					winningCards.add(x);
					count++;
				}

				if (count == 2) {

					if (rank == 1) {

						return 414;
					} else {

						return 400 + rank;
					}
				}
			}
		}
		return 0;
	}

	private int checkPair(int bRank, ArrayList<Card> checkHand) {

		// we add in checking for a bad rank for looking
		// at full houses and two pairs, to make sure we don't repeat

		int badRank = bRank;
		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();
			rank = checkHand.get(i).getRank();
			winningCards.add(i);
			if (rank != badRank) {

				int count = 0;
				for (int x = 0; x < checkHand.size(); x++) {

					if (checkHand.get(x).getRank() == rank && x != i) {

						winningCards.add(x);
						count++;
					}

					if (count == 1) {

						if (rank == 1) {

							return 214;
						} else {

							return 200 + rank;
						}
					}
				}
			}
		}
		return 0;
	}

	public int checkPairRaw(int bRank) {

		// we add in checking for a bad rank for looking
		// at full houses and two pairs, to make sure we don't repeat

		int badRank = bRank;
		int rank = 0;

		for (int i = 0; i < hand.size(); i++) {

			rank = hand.get(i).getRank();

			if (rank != badRank) {

				int count = 0;
				for (int x = 0; x < hand.size(); x++) {

					if (hand.get(x).getRank() == rank && x != i) {

						count++;
					}

					if (count == 1) {

						if (rank == 1) {

							return 214;
						} else {

							return 200 + rank;
						}
					}
				}
			}
		}
		return 0;
	}

	private int checkHighCard(ArrayList<Card> checkHand) {

		int rank = checkHand.get(0).getRank();

		for (int i = 1; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();

			if (checkHand.get(i).getRank() > rank) {

				winningCards.add(i);

				rank = checkHand.get(i).getRank();

				if (rank == 1) {

					return 114;
				}

			}
		}

		return rank + 100;
	}

	private int checkFullHouse(ArrayList<Card> checkHand) {

		int threeKind = checkThreeOfAKind(checkHand);
		int pair = 0;

		if (threeKind != 0) {

			ArrayList<Integer> temp = (ArrayList<Integer>) winningCards.clone();

			threeKind = threeKind - 400;

			if (threeKind == 14) {

				threeKind = 1;
			}

			pair = checkPair(threeKind, checkHand);

			if (pair != 0) {

				for (int i = 0; i < temp.size(); i++) {
					winningCards.add(temp.get(i));
				}

				if (threeKind == 1) {

					threeKind = 14;
				}

				return (threeKind) + 700;
			} else {

				return 0;
			}

		} else {

			return 0;
		}
	}

	private int checkFlush(ArrayList<Card> checkHand) {

		int suit = 0;
		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();

			suit = checkHand.get(i).getSuit();
			if (checkHand.get(i).getRank() == 1) {

				rank = 14;
			}
			rank = checkHand.get(i).getRank();
			winningCards.add(i);
			int count = 0;
			for (int x = 0; x < checkHand.size(); x++) {

				if (checkHand.get(x).getSuit() == suit && x != i) {

					if (checkHand.get(x).getRank() > rank) {

						rank = checkHand.get(x).getRank();
					}

					if (checkHand.get(x).getRank() == 1) {

						rank = 14;
					}

					winningCards.add(x);
					count++;
				}

				if (count == 4) {

					if (rank == 1) {

						return 614;
					}

					else {

						return 600 + rank;
					}

				}
			}
		}
		return 0;
	}

	private int checkStraight(ArrayList<Card> checkHand) {

		int rank = 0;

		for (int i = 0; i < checkHand.size(); i++) {
			winningCards = new ArrayList<Integer>();

			rank = checkHand.get(i).getRank();

			winningCards.add(i);

			for (int j = 0; j < checkHand.size(); j++) {

				if (checkHand.get(j).getRank() == rank + 1) {

					winningCards.add(j);
					for (int k = 0; k < checkHand.size(); k++) {

						if (checkHand.get(k).getRank() == rank + 2) {

							winningCards.add(k);
							for (int m = 0; m < checkHand.size(); m++) {

								if (checkHand.get(m).getRank() == rank + 3) {

									winningCards.add(m);
									for (int n = 0; n < checkHand.size(); n++) {

										if (checkHand.get(n).getRank() == rank + 4) {

											winningCards.add(n);
											for (int o = 0; o < checkHand.size(); o++) {

												if (checkHand.get(o).getRank() == rank + 5) {

													winningCards.add(o);
													winningCards.remove(0);
													for (int p = 0; p < checkHand.size(); p++) {

														if (checkHand.get(p).getRank() == rank + 6) {
															winningCards.add(p);
															winningCards.remove(0);
															return 500 + (rank + 6);
														} else if ((rank + 6) == 14) {

															if (checkHand.get(n).getRank() == 1) {
																winningCards.add(p);
																winningCards.remove(0);
																return 514;
															}
														}
													}
													return 500 + (rank + 5);
												} else if ((rank + 5) == 14) {

													if (checkHand.get(n).getRank() == 1) {

														return 514;
													}
												}
											}
											return 500 + (rank + 4);
										} else if ((rank + 4) == 14) {

											if (checkHand.get(n).getRank() == 1) {

												return 514;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return 0;
	}

	private int checkTwoPair(ArrayList<Card> checkHand) {

		int pair1 = checkPair(0, checkHand);

		int pair2 = 0;

		ArrayList<Integer> temp = (ArrayList<Integer>) winningCards.clone();

		if (pair1 != 0) {

			if (pair1 == 214)

				pair2 = checkPair(1, checkHand);
			else

				pair2 = checkPair(pair1 - 200, checkHand);

			if (pair2 != 0) {

				for (int i = 0; i < temp.size(); i++) {
					winningCards.add(temp.get(i));
				}

				if (pair1 - 200 == 1 || pair2 - 200 == 1) {

					return 314;
				}

				else if (pair1 > pair2) {

					return (pair1 - 200) + 300;
				} else {

					return (pair2 - 200) + 300;
				}
			} else {

				return 0;
			}

		} else {

			return 0;
		}
	}
}