package base;
import java.util.Vector;

import cards.*;

public class Player {
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
public String stratName;
int PointsDeVictoire = 0;

Player(Partie p){
	id = idGenerator++;
	name = "Player " + Integer.toString(id);
	partie = p;
	hand = new Hand(this);
	defausse = new DiscardPile(this);
	board = new Board(this);
	deck = new Deck(this);
	deck.shuffle();
	decklist = new Decklist(this);
	decklist.setupOnDeck(deck);
	C = new Constantes();
}

Player(Partie p,String stratName){
	this(p);
	this.stratName=stratName;
}

Player(Partie p,Constantes CS, boolean alter){
	this(p);
	if (alter) {C = CS.alter();}
	else C = CS;
}

void play(AbstractCard c) {
	if(c.isA(AbstractCard.Type.ACTION)) leftActions--;
	c.onPlay(this);
}

void buy(AbstractCard c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
	defausse.add(partie.theShop.getCard(c.getName()));
	decklist.add(c);
	leftGold -= c.getGoldCost(this);
	leftBuys -= 1;
	System.out.println(name+" buys "+c+" | buys left: "+leftBuys+" | gold left: "+leftGold);
	System.out.println(partie.theShop.remainingCards(c)+" "+c+" remaining");
}

AbstractCard [] playables() {
	int p = 0;
	if (leftActions == 0) {return new AbstractCard[0];}
	AbstractCard [] reponse = new AbstractCard[hand.typeCount(AbstractCard.Type.ACTION)];
	for (AbstractCard c: hand) {
		if (c.isA(AbstractCard.Type.ACTION)) {
			reponse[p++] = c;
			}
	}
	return reponse;	
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
	return result;	
}

public boolean draw(int number) {
	boolean shuffle=false;
	for(int i=0;i<number;i++) {
		shuffle|=draw();
	}
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

public void trash(AbstractCard c) {
	System.out.println(name+" trashes "+c);
	hand.retire(c);
}

public boolean gainToDiscard(AbstractCard c) {
	boolean res=false;
	res|=c.onGain(this);
	defausse.add(c);
	System.out.println(name+" gains "+c+" on his discard pile");
	return res;
}

public boolean gainToHand(AbstractCard c) {
	boolean res=false;
	res|=c.onGain(this);
	hand.add(c);
	System.out.println(name+" gains "+c+" to his hand");
	return res;
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
	System.out.println(name+" mélange sa défausse et son deck");
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
	System.out.println("===================== TOUR DU JOUEUR " +name+" ===============================");
	System.out.println(this);
	while(leftActions>0 && playSomething) {
		AbstractCard c = chooseToPlay();
		if (playSomething) {
			play(c);
		}
	}
	System.out.println("FIN ACTIONS");
	playTreasures();
	while(leftBuys>0 && buySomething) {
		AbstractCard c = chooseToGain(buyables());
		if (c!=null)
			buy(c);
		else 
			buySomething=false;
	}
	System.out.println("FIN ACHATS");
	discardBoard();
	newHand();
	if(printDetails) System.out.println(this);
	System.out.println("FIN DU TOUR DE "+name+"\n"); 
}

public AbstractCard chooseToPlay() {
//tries to play +actions and then most expensive actions, except if has throne room
	if(hand.cardCount("Throne room")>0)
		return hand.findA("Throne room");
	
	AbstractCard [] playables = playables();
	for (int i = 0; i<playables.length; i++) {
		if (playables[i].getPlusActions(this)>0) {
			playSomething = true;
			return playables[i];
		}
	}

	if (playables.length > 0){
		playSomething = true;
		int maxCost=0;
		int maxIndex=-1;
		for (int i = 0; i<playables.length; i++) {
			if(playables[i].getGoldCost(this)>maxCost) {
				maxCost=playables[i].getGoldCost(this);
				maxIndex=i;
			}
		}
		return playables[maxIndex];
	}
	playSomething = false;
	return null;
}

//BUYING/RECEIVING CARDS
public int valueCard(AbstractCard c) {
	return valueCard(c, stratName);
}

public int valueCard(AbstractCard c, String stratName) {
	return valueCard(c.getName(), stratName,this.partie.theShop);
}

private int valueCard(AbstractCard c, String stratName, Shop theShop) {
	return valueCard(c.getName(), stratName,theShop);
}

//STRATEGIES CODED HERE. default is BM
//Higher value means higher priority. Negative value means you prefer to buy nothing
private int valueCard(String cardName, String stratName, Shop theShop) {
	switch (stratName) {
	case "BM":
		switch (cardName) {
		case "Province":
			return 1000;
		case "Gold":
			return 200;
		case "Silver":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		default:
			return -100;
		}
	case "OptimizedBM":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
			else return 1600;
		case "Gold":
			return 200;
		case "Duchy":
			if(theShop.remainingCards("Province")==5) return 150;
			if(theShop.remainingCards("Province")<=4) return 800;
			else return 0;
		case "Silver":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		case "Estate":
			if(theShop.remainingCards("Province")<=2) return 400;
			if(theShop.remainingCards("Province")==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "SmithyBM":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
			else return 1600;
		case "Gold":
			return 200;
		case "Smithy":
			if(this.decklist.cardCount("Smithy")==0 || (this.decklist.size()>=14 && this.decklist.cardCount("Smithy")==1)) return 150;
			else return -50;
		case "Duchy":
			if(theShop.remainingCards("Province")==5) return 150;
			if(theShop.remainingCards("Province")<=4) return 800;
			else return 0;
		case "Silver":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		case "Estate":
			if(theShop.remainingCards("Province")<=2) return 400;
			if(theShop.remainingCards("Province")==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "BasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
			else return 1600;
		case "Gold":
			if(this.decklist.cardCount("Gold")==0) return 400;
			else return -50;
		case "Smithy":
			if(this.decklist.cardCount("Smithy")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.cardCount("Smithy")) return 250;
			else return -50;
		case "Market":
			if(this.decklist.cardCount("Market")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.cardCount("Smithy")) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.cardCount("Smithy"))<=1) return 200;
			else return -50;
		case "Duchy":
			if(theShop.remainingCards("Province")==4) return 150;
			if(theShop.remainingCards("Province")<=3) return 600;
			else return 0;
		case "Silver":
			if(this.decklist.cardCount("Silver")==0) return 200;
			else return -50;
		case "Estate":
			if(theShop.remainingCards("Province")<=2) return 400;
			if(theShop.remainingCards("Province")==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "MilitiaBasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
			else return 1600;
		case "Gold":
			if(this.decklist.cardCount("Gold")==0) return 500;
			else return -50;
		case "Militia":
			if(this.decklist.cardCount("Militia")==0) return 400;
			else return -50;
		case "Smithy":
			if(this.decklist.cardCount("Smithy")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
			else return -50;
		case "Market":
			if(this.decklist.cardCount("Market")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION))<=1) return 200;
			else return -50;
		case "Duchy":
			if(theShop.remainingCards("Province")==4) return 150;
			if(theShop.remainingCards("Province")<=3) return 600;
			else return 0;
		case "Silver":
			if(this.decklist.cardCount("Silver")==0) return 200;
			else return -50;
		case "Estate":
			if(theShop.remainingCards("Province")<=1) return 400;
			if(theShop.remainingCards("Province")==2) return 50;
			else return -50;
		default:
			return -100;
		}
	case "WitchBasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
			else return 1600;
		case "Gold":
			if(this.decklist.cardCount("Gold")==0) return 500;
			else return -50;
		case "Militia":
			if(this.decklist.cardCount("Militia")==0) return 400;
			else return -50;
		case "Witch":
			if(this.decklist.cardCount("Witch")==0) return 450;
			else return -50;
		case "Smithy":
			if(this.decklist.cardCount("Smithy")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
			else return -50;
		case "Market":
			if(this.decklist.cardCount("Market")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION))<=1) return 200;
			else return -50;
		case "Duchy":
			if(theShop.remainingCards("Province")==4) return 150;
			if(theShop.remainingCards("Province")<=3) return 600;
			else return 0;
		case "Silver":
			if(this.decklist.cardCount("Silver")==0) return 200;
			else return -50;
		case "Estate":
			if(theShop.remainingCards("Province")<=1) return 400;
			if(theShop.remainingCards("Province")==2) return 50;
			else return -50;
		default:
			return -100;
		}
	default:
		System.out.println("Unknown stategy. Defaulting to BM...");
		switch (cardName) {
		case "Province":
			return 1000;
		case "Gold":
			return 200;
		case "Silver":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		default:
			return -100;
		}
	}
}

public AbstractCard chooseToGain(Vector<AbstractCard> available) {
	return chooseToGain(available,this.stratName);
}

public AbstractCard chooseToGain(Vector<AbstractCard> available,String stratName) {
	return chooseToGain(available,stratName, partie.theShop);
}

public AbstractCard chooseToGain(Vector<AbstractCard> available,String stratName, Shop theShop) {
	double noteMax = 0;
	AbstractCard res = null;
	for (AbstractCard c: available) {
		int note = valueCard(c, stratName, theShop);
		//System.out.println("Considering "+c+" note: "+note);
		if (note > noteMax) {
			res = c;
			noteMax = note;
		}
	}
	return res;
}

//DISCARDING
//STRATEGIES MATTERS HERE
//Higher value means higher priority. Negative value means you prefer to discard nothing vs drawing a card
public int valueToDiscard(AbstractCard c, String stratName) {
	if(c.isA(AbstractCard.Type.VICTORY) && !c.isA(AbstractCard.Type.TREASURE) && !c.isA(AbstractCard.Type.ACTION)) return 1600;
	if(c.getName().equals("Curse")) return 1200;
	if( ((this.hand.typeCount(AbstractCard.Type.TERMINAL_ACTION) - this.hand.typeCount(AbstractCard.Type.VILLAGE))>1) && c.isA(AbstractCard.Type.TERMINAL_ACTION)) return 1000-c.getGoldCost(this)*100;
	if(c.isA(AbstractCard.Type.TREASURE)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) ((this.decklist.goldDensity()+0.8-c.getPlusGold(this))*100);
		else return (int) ((this.decklist.goldDensity()-c.getPlusGold(this))*100);
	}
	if(c.isA(AbstractCard.Type.ACTION)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) (-200*(1-this.decklist.typeDensity(AbstractCard.Type.ACTION)));
		else return -80;
	}
	return -50;
}

public int valueToDiscard(AbstractCard c) {
	return valueToDiscard(c, stratName);
}

//return the preferred card to discard
public AbstractCard chooseToDiscard() {
	return chooseToDiscard(this.stratName);
}

public AbstractCard chooseToDiscard(String stratName) {
	double noteMax = -1000000;
	AbstractCard res = null;
	for (int i = 0; i<this.hand.size(); i++) {
		int note = valueToDiscard(this.hand.get(i), stratName);
		if (note > noteMax) {
			res = this.hand.get(i);
			noteMax = note;
		}
	}
	return res;
}

public boolean askToDiscard() {
	discard(chooseToDiscard());
	return true;
}

public boolean askToDiscard(int number) {
	boolean res=false;
	for(int i=0;i<number;i++) {
		res|=askToDiscard();
	}
	return res;
}

public AbstractCard decideToDiscard(String benefit) {
	AbstractCard res=chooseToDiscard();
	if(decideToDiscard(res,benefit)) //not optimized
		return res;
	return null;
}

public boolean decideToDiscard(AbstractCard c,String benefit) {
	switch (benefit.toLowerCase().replaceAll("\\s","")) {
	case "+1card":
		return valueToDiscard(c)>0;
	case "+1gold":
		return valueToDiscard(c)>50;
	case "+2gold":
		return valueToDiscard(c)>0;
	default:
		return valueToDiscard(c)>0;
	}
}

public boolean decideToReshuffle() { //TO DO: put a non-trivial decision-maker
	return true;
}

//TRASHING
//STRATEGIES MATTERS HERE
//Higher value means higher priority. Negative value means you prefer to trash nothing without benefit
public int valueToTrash(AbstractCard c, String stratName) {
	if(c.isA(AbstractCard.Type.CURSE)) return 1600;
	if(c.getName().equals("Estate")) return 400*(4-this.partie.theShop.remainingCards("Province"))-10;
	if(c.isA(AbstractCard.Type.TREASURE)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) ((this.decklist.goldDensity()+0.6-c.getPlusGold(this))*100);
		else return (int) ((this.decklist.goldDensity()-c.getPlusGold(this))*100);
	}
	if(c.isA(AbstractCard.Type.ACTION)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) (-400*(1-this.decklist.typeDensity(AbstractCard.Type.ACTION)));
		else return -80;
	}
	return -50;
}

public int valueToTrash(AbstractCard c) {
	return valueToTrash(c, stratName);
}

//return the preferred card to trash
AbstractCard chooseToTrash() {
	return chooseToTrash(this.stratName);
}

AbstractCard chooseToTrash(String stratName) {
	double noteMax = -1000000;
	AbstractCard res = null;
	for (int i = 0; i<this.hand.size(); i++) {
		int note = valueToTrash(this.hand.get(i), stratName);
		if (note > noteMax) {
			res = this.hand.get(i);
			noteMax = note;
		}
	}
	return res;
}

public boolean askToTrash() {
	trash(chooseToTrash());
	return true;
}

public boolean askToTrash(int number) {
	boolean res=false;
	for(int i=0;i<number;i++) {
		res|=askToTrash();
	}
	return res;
}

public AbstractCard decideToTrash(String benefit) {
	AbstractCard res=chooseToTrash();
	if(decideToTrash(res,benefit)) //not optimized
		return res;
	return null;
}

public boolean decideToTrash(AbstractCard c,String benefit) {
	switch (benefit.toLowerCase().replaceAll("\\s","")) {
	case "+1card":
		return valueToTrash(c)>-25;
	case "+3gold":
		return valueToTrash(c)>-50;
	case "none":
		return valueToTrash(c)>0;
	default:
		return valueToTrash(c)>0;
	}
}
//OTHERS
public AbstractCard askToReact(AbstractCard att) {
	AbstractCard res=null;
	for(AbstractCard c: hand) {
		if(c.isA(AbstractCard.Type.REACTION)) {
			switch (c.getName()) {
			case "Moat":
				res= c;
				break;
			default:
				res=c;
				break;
			}
		}
	}
	if(res!=null) {
		//res.onReactToAttack(this);
	}
	return res;
}

public boolean decideSpy(Player pla,AbstractCard shown) {
	boolean res=true;
	if(shown.isA(AbstractCard.Type.VICTORY) || (shown.isA(AbstractCard.Type.TREASURE)&&(shown.getPlusGold(pla)<=pla.decklist.goldDensity()))) res=false;
	if(pla==this) res = !res;
	return res;
}

static private String pad(String s,int size) {
	if(s.length()>size) {
		System.out.println("ERROR: String too big for padding");
		return s;
	}
	else {
		int a = size - s.length();
		String res="";
		for(int i=0;i<a-a/2;i++) {
			res+=" ";
		}
		res+=s;
		for(int i=0;i<a/2;i++) {
			res+=" ";
		}
		return res;
	}
}

public String getStrategyTab(String stratName) {
	Shop fakeShop = new Shop(2);
	String res= "Tab for strategy "+stratName+":\n";
	int temp = res.length();
	res+="                  | ";
	for(int j=8;j>=0;j--) {
		res+=pad(Integer.toString(j)+" gold",12)+" | ";
	}
	res+="\n"+(new String(new char[res.length()-temp]).replace('\0', '-'))+"\n";
	int NProvinces = fakeShop.remainingCards("Province");
	for(int i=NProvinces;i>0;i--) {
		res+=pad(fakeShop.remainingCards("Province")+" provinces left | ",20);
		for(int j=8;j>=0;j--) {
			leftGold=j;
			leftBuys=1;
			AbstractCard c = chooseToGain(buyables(),stratName,fakeShop);
			if(c==null) res+= pad("None",12) +" | ";
			else res+= pad(c.getName(),12) +" | ";
		}
		res+="\n";
		fakeShop.getCard("Province");
	}
	return res;
}

public static void main(String [] args) {
	Partie p = new Partie(1);
	p.joueurs[0].buy(new Gold());
	p.joueurs[0].buy(new Smithy());
	System.out.println(p.joueurs[0].decklist);
	System.out.println("No terminal prob: "+p.joueurs[0].decklist.noTerminalProb());
	System.out.println("One terminal prob: "+p.joueurs[0].decklist.oneTerminalProb());
	System.out.println("Terminal coll prob: "+p.joueurs[0].decklist.terminalCollisionProb());
	System.out.println(p.joueurs[0].getStrategyTab("SmithyBM"));
}

//ALL DEPRECATED
AbstractCard laPlusChere() {
	//un debut de fonction pour decider quoi faire, c'est debile, mais c'est juste pour tester
	Vector<AbstractCard> buyables = buyables();
	int maxCost = 0;
	buySomething = false;
	AbstractCard reponse = null;
	for (int i = 0; i<buyables.size(); i++) {
		if (buyables.get(i).getGoldCost(this) > maxCost) {
			buySomething = true;
			reponse = buyables.get(i);
			maxCost = reponse.getGoldCost(this);
		}
	}
	return reponse;
}

AbstractCard laMeilleureNote(boolean printDetails) {
	Vector<AbstractCard> buyables = buyables();
	double noteMax = 0;
	buySomething = false;
	AbstractCard reponse = null;
	for (int i = 0; i<buyables.size(); i++) {
		double note = note(buyables.get(i), printDetails);
		if (note > noteMax) {
			buySomething = true;
			reponse = buyables.get(i);
			noteMax = note;
		}
	}
	return reponse;
}

private double incrementGoldDensity(Decklist nouv) {
	 return nouv.goldDensity()- decklist.goldDensity();
}

private double incrementCardValue(Decklist nouv) {
	return nouv.givenCardsDensity() - decklist.givenCardsDensity();
}

private double incrementEnAction(Decklist nouv) {
	return (nouv.givenActionDensity() - decklist.givenActionDensity())*(1 + decklist.typeDensity(AbstractCard.Type.ACTION));
}

private double incrementEnAchat(Decklist nouv) {
	return nouv.givenAchatDensity() - decklist.givenAchatDensity();
}

private double PdV(AbstractCard c) {
	return c.getVP(this);
}

public double note(AbstractCard c, boolean printDetails) {
	Decklist nouv = decklist.simulatedAdd(c);
	double noteGold  = incrementGoldDensity(nouv);
	double noteCard = incrementCardValue(nouv);
	double noteAction = incrementEnAction(nouv);
	double noteAchat = incrementEnAchat(nouv);
	double notePdV = PdV(c);
	double note = C.k2*noteGold+ C.k3*noteCard+ C.k4*noteAction + C.k5*noteAchat+ C.k1*notePdV;
	if (printDetails) {
		System.out.println("");
		System.out.println("Carte : " + c);
		System.out.println("note Gold : " + noteGold);
		System.out.println("note Card : " + noteCard);
		System.out.println("note Action : " + noteAction);
		System.out.println("note Achat : " + noteAchat);
		System.out.println("note Points de Victoire : " + notePdV);
		System.out.println("note totale : " + note);
	}
	return note;
}

public double noteCardStratGold(AbstractCard c, boolean printDetails){ 

if(c.getName().equals("Province")) {return 5;} 

if(c.getName().equals("Gold")) {return 4;} 

if(c.getName().equals("Smithy") && decklist.proportion(c.getName())< C.Seuil1){return 3;} 

if (c.getName() == "Silver"){return 2;} 

return 0; 
} 

public String toString() {
	String s = name;
	s+= " | actions left: "+leftActions+" | buys left: "+leftBuys+" | gold left: "+leftGold + "\n";
	s+=deck.toString()+"\n";
	s+=defausse.toString()+"\n";
	s+=hand.toString();
	return s;
}

boolean nearEnd() {return partie.theShop.nombrePilesVides()>=C.N2 | partie.theShop.remainingCards("Province")<= C.N1;}
}
