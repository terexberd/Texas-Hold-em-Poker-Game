
public class Card {
	
	private final int rank;
    private final int suit;
    private String cardIndex;

    // Kinds of suits
    public final static int DIAMONDS = 1;
    public final static int CLUBS    = 2;
    public final static int HEARTS   = 3;
    public final static int SPADES   = 4;

    // Kinds of ranks
    public final static int ACE   = 1;
    public final static int DEUCE = 2;
    public final static int THREE = 3;
    public final static int FOUR  = 4;
    public final static int FIVE  = 5;
    public final static int SIX   = 6;
    public final static int SEVEN = 7;
    public final static int EIGHT = 8;
    public final static int NINE  = 9;
    public final static int TEN   = 10;
    public final static int JACK  = 11;
    public final static int QUEEN = 12;
    public final static int KING  = 13;

    public Card(int rank, int suit) {
       	//if rank or suit is invalid, program will throw an error
        assert isValidRank(rank);
        assert isValidSuit(suit);
        
        this.rank = rank;
        this.suit = suit;

        cardIndex = new String("");
    }

    public String getIndex(){
        return cardIndex;
    }

    
    public boolean compare(Card card1) {
    	
    	if(card1.getRank() == rank && card1.getSuit() == suit) {
    		
    		return true;
    	}
    	else {
    		
    		return false;
    	}
    }
    

    public void setCardIndex(int j, int i){
        cardIndex = new String(suitToString(j)+i);
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    //Ensures inputted rank is higher than/equal to ace and lower than/equal to king
    public static boolean isValidRank(int rank) {
        return ACE <= rank && rank <= KING;
    }

    //Ensures inputted suit is between/equal to diamonds (1) and spades (4)
    public static boolean isValidSuit(int suit) {
        return DIAMONDS <= suit && suit <= SPADES;
    }

    //Because this class indentifies all cards by number
    //the following ToString methods are necessary
    public static String rankToString(int rank) {
        switch (rank) {
	        case ACE:
	            return "Ace";
	        case DEUCE:
	            return "Deuce";
	        case THREE:
	            return "Three";
	        case FOUR:
	            return "Four";
	        case FIVE:
	            return "Five";
	        case SIX:
	            return "Six";
	        case SEVEN:
	            return "Seven";
	        case EIGHT:
	            return "Eight";
	        case NINE:
	            return "Nine";
	        case TEN:
	            return "Ten";
	        case JACK:
	            return "Jack";
	        case QUEEN:
	            return "Queen";
	        case KING:
	            return "King";
	        default:
            
            //Handle an illegal argument.  There are generally two
            //ways to handle invalid arguments, throwing an exception
            //(see the section on Handling Exceptions) or return null
            return null;
        }    
    }
    
    public static String suitToString(int suit) {
        switch (suit) {
        case DIAMONDS:
            return "diamonds";
        case CLUBS:
            return "clubs";
        case HEARTS:
            return "hearts";
        case SPADES:
            return "spades";
        default:
            return null;
        }    
    }

    public static void main(String[] args) {
    	
    	// must run program with -ea flag (java -ea ..) to
    	// use assert statements
        assert rankToString(ACE) == "Ace";
        assert rankToString(DEUCE) == "Deuce";
        assert rankToString(THREE) == "Three";
        assert rankToString(FOUR) == "Four";
        assert rankToString(FIVE) == "Five";
        assert rankToString(SIX) == "Six";
        assert rankToString(SEVEN) == "Seven";
        assert rankToString(EIGHT) == "Eight";
        assert rankToString(NINE) == "Nine";
        assert rankToString(TEN) == "Ten";
        assert rankToString(JACK) == "Jack";
        assert rankToString(QUEEN) == "Queen";
        assert rankToString(KING) == "King";

        assert suitToString(DIAMONDS) == "Diamonds";
        assert suitToString(CLUBS) == "Clubs";
        assert suitToString(HEARTS) == "Hearts";
        assert suitToString(SPADES) == "Spades";

    }

}

//Reference
//https://docs.oracle.com/javase/tutorial/java/javaOO/examples/Card.java
//Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.


