package base;
import java.util.Vector;

import cards.*;

public abstract class Player {
	static int idGenerator = 0;
	final public int id;
	public int number;
	public Partie partie;
	public Deck deck;
	public Decklist decklist;
	public Hand hand;
	public DiscardPile defausse;
	public Board board;
	public String name;
	public int leftActions = 1;
	public int leftBuys = 1;
	public int leftGold = 0;
	public int leftPotions = 0;
	public boolean playSomething;
	public boolean buySomething;
	public Constantes C;
	int PointsDeVictoire = 0;

	Player(){
		id = idGenerator++;
		name = "Player " + Integer.toString(id);
		hand = new Hand(this);
		defausse = new DiscardPile(this);
		board = new Board(this);
		C = new Constantes();
	}

	Player(Partie p,Constantes CS, boolean alter){
		this();
		if (alter) {C = CS.alter();}
		else C = CS;
	}

	void beginGame(Partie par) {
		partie=par;
		deck = new Deck(this);
		decklist = new Decklist(this);
		decklist.setupOnDeck(deck);
		deck.shuffle();
		newHand();
	}
	
	void play(AbstractCard c) {c.playedBy(this);}

	void buy(AbstractCard c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
		leftGold -= c.getGoldCost(this);
		leftBuys -= 1;
		System.out.println(name+" buys "+c+" | buys left: "+leftBuys+" | gold left: "+leftGold);
		gainToDiscard(partie.theShop.getCard(c));
	}

	public AbstractCard gainToDiscard(AbstractCard c) {
		defausse.add(c);
		System.out.println(name+" gains "+c+" on his discard pile");
		c.onGain(this);
		return c;
	}

	public AbstractCard gainToHand(AbstractCard c) {
		hand.add(c);
		System.out.println(name+" gains "+c+" to his hand");
		c.onGain(this);
		return c;
	}

	Vector<AbstractCard> playables() {
		Vector<AbstractCard> res  = new Vector<AbstractCard>(hand.size());
		for (AbstractCard c: hand) {
			if (c.isA(AbstractCard.Type.ACTION)) {
				res.add(c);
			}
		}
		return res;	
	}

	Vector<AbstractCard> buyables() {
		Vector<AbstractCard> result = new Vector<AbstractCard>(10);
		if (leftBuys == 0) {return result;}
		Vector<AbstractCard> shopAvailable = partie.theShop.buyables();
		for (AbstractCard c: shopAvailable) {
			if (c.getGoldCost(this) <= leftGold) {
				result.add(c);
			}
		}
		result.addElement(null);
		return result;	
	}

	public boolean draw(int number) {
		boolean shuffle=false;
		for(int i=0;i<number;i++)
			shuffle|=draw();
		return shuffle;
	}

	public boolean draw() {
		boolean shuffle=false;
		if (deck.isEmpty()) {
			shuffleDiscardToDeck();
			shuffle=true;
		}
		if (deck.size() != 0) {
			hand.add(deck.pop());
		}
		return shuffle;
	}

	public void mill() {
		System.out.println(name+" mills "+deck.peek());
		defausse.add(deck.pop());
	}

	public void discard(AbstractCard c) {
		System.out.println(name+" discards "+c);
		defausse.add(hand.retire(c));
	}

	public boolean trash(AbstractCard c) {
		System.out.println(name+" trashes "+c);
		return(hand.retire(c)!=null);
	}

	//useful for Spy
	public AbstractCard reveal() {
		if(!deck.isEmpty()) {
			AbstractCard res=deck.peek();
			System.out.println(name+" reveals "+res);
			return res;
		}
		else {
			System.out.println(name+" cannot reveal: (s)he has no more card in deck...");
			return null;
		}
	}

	public AbstractCard[] reveal(int number) {
		AbstractCard[] res=new AbstractCard[number];
		for(int i=1;i<number+1;i++) {
			if(deck.size()>=i) {
				AbstractCard temp = deck.peek(i);
				System.out.println(name+" reveals "+temp);
				res[i]=temp;
			}
			else {
				System.out.println(name+" cannot reveal: (s)he has no more card in deck...");
				return null;
			}
		}
		return res;
	}

	public void shuffleDiscardToDeck() {
		int imax = defausse.size();
		for (int i = 0; i<imax; i++) {
			deck.add(defausse.pop());
		}
		deck.shuffle();
		System.out.println(name+" shuffles his discard pile and his deck");
	}

	void discardBoard() {
		int imax = board.size();
		for (int i = 0; i<imax; i++) {
			defausse.add(board.popACard());
		}
	}

	boolean newHand() {
		return draw(5);
	}

	//on défausse chaque carte de la main en comptant sa valeur si c'est un tresor, puis on ajoute le total
	//RAPPEL: les bonus en or sont comptes lorsque la carte est jouee
	void playTreasures() {
		int NCartes = hand.size();
		for(int i = 0; i< NCartes;i++) {
			if (hand.get(0).isA(AbstractCard.Type.TREASURE)){
				play(hand.get(0));
			}
			else defausse.add(hand.retire(hand.get(0)));
		}
	}

	int countVictoryPoints() {
		int total = 0;
		for (AbstractCard c:decklist) {
			total+=c.getVP(this);
		}
		PointsDeVictoire=total;
		return total;
	}

	////// TOUR DE JEU ET METHODES DE CHOIX ///////
	public void reset() {
		playSomething = true;
		buySomething = true;
		leftBuys = 1;
		leftActions = 1;
		leftGold = 0;
	}

	public void tourDeJeu(boolean printDetails) {
		reset();
		System.out.println("===================== " +name+"'s TURN ===============================");
		System.out.println(this);
		while(leftActions>0 && playSomething) {
			AbstractCard c = chooseToPlay();
			if (c!=null) {
				play(c);
			}
			else
				playSomething=false;
		}
		System.out.println("END OF ACTION PHASE");
		playTreasures();
		while(leftBuys>0 && buySomething) {
			AbstractCard c = chooseToGain(buyables());
			if (c!=null)
				buy(c);
			else 
				buySomething=false;
		}
		System.out.println("END OF BUYING PHASE");
		discardBoard();
		newHand();
		if(printDetails) System.out.println(this);
		System.out.println("END OF "+name+"'s TURN\n"); 
	}

	public abstract AbstractCard chooseToPlay();

	public abstract AbstractCard chooseToGain(Vector<AbstractCard> available);

	//return the preferred card to discard
	public abstract AbstractCard chooseToDiscard(Vector<AbstractCard> available);
	
	public AbstractCard chooseToDiscard() {return chooseToDiscard(hand);}
	
	public AbstractCard askToDiscard() {
		AbstractCard c = chooseToDiscard();
		discard(c);
		return c;
	}

	public AbstractCard[] askToDiscard(int number) {
		AbstractCard[] res = new AbstractCard[number];
		for(int i=0;i<number;i++) {
			res[i]=askToDiscard();
		}
		return res;
	}

	//returns null if player prefer not to discard
	public abstract AbstractCard decideToDiscard(Vector<AbstractCard> available, String benefit);
	
	public AbstractCard decideToDiscard(String benefit) {return decideToDiscard(hand, benefit);}

	public abstract boolean decideToDiscard(AbstractCard c,String benefit);
	
	public abstract boolean decideToReshuffle();

	//return the preferred card to trash
	public AbstractCard chooseToTrash() {return chooseToTrash(this.hand);}
	
	public abstract AbstractCard chooseToTrash(Vector<AbstractCard> available);
	
	public AbstractCard askToTrash() {
		AbstractCard c = chooseToTrash();
		trash(c);
		return c;
	}

	public AbstractCard[] askToTrash(int number) {
		AbstractCard[] res = new AbstractCard[number];
		for(int i=0;i<number;i++) {
			res[i]=askToTrash();
		}
		return res;
	}

	//returns null if player prefer not to trash
	public abstract AbstractCard decideToTrash(Vector<AbstractCard> available, String benefit);
	
	public AbstractCard decideToTrash(String benefit) {return decideToTrash(hand, benefit);}

	public abstract boolean decideToTrash(AbstractCard c,String benefit);
	
	//OTHERS
	public abstract AbstractCard askToReact(AbstractCard att);

	public abstract boolean decideSpy(Player pla,AbstractCard shown);

	public String toString() {
		String s = name;
		s+= " | actions left: "+leftActions+" | buys left: "+leftBuys+" | gold left: "+leftGold + "\n";
		s+=deck.toString()+"\n";
		s+=defausse.toString()+"\n";
		s+=hand.toString();
		return s;
	}
}

