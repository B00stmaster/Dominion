import java.util.Vector;


import com.sun.xml.internal.ws.util.StringUtils;

public class Player {
	static int idGenerator = 0;
final public int id;
public int number;
public Partie partie;
public Deck deck;
public Decklist decklist;
public Hand hand;
public DiscardPile defausse;
public Stack board;
public String name;
public int leftActions = 1;
public int leftBuys = 1;
public int leftGold;
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
	board = new Stack();
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

//deprecated. Use play()
void play_old(Card c) {//on suppose que le joueur a deja l'action dispo pour le faire
	leftActions += c.plusActions - 1;
	leftBuys += c.plusBuys;
	for (int i = 0; i<c.plusCards; i++) {
		draw();
	}
	applyEffect(c);
	board.add(hand.retire(c));
}

void play(Card c) {
	c.playedBy(this);
}

void buy(Card c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
	defausse.add(partie.theShop.getCard(c.name));
	decklist.add(c);
	leftGold -= c.cost;
	leftBuys -= 1;
	System.out.println(name+" buys "+c+" | buys left: "+leftBuys+" | gold left: "+leftGold);
	System.out.println(partie.theShop.remainingCards(c)+" "+c+" remaining");
}

//deprecated. Use Card.playedBy(Player p) instead
void applyEffect(Card c) {
	//on liste tous les effets pour appliquer la methode correspondante
	if(c.effet==null) return;
	switch (c.effet) {
	case SORCIERE:
		Card.sorciere(this);
		break;
	case CHAMBRE_DU_CONSEIL:
		Card.chambreDuConseil(this);
		break;
	case PUITS_AUX_SOUHAITS:
		Card [] choosables = Card.getCardByName("Puits aux Souhaits").choosables(this); //fonction unique qui marche pour toutes les cartes qui font choisir parmi des cartes
		Card C = choosables[0]; //CHOIX DU JOUEUR !!! après on aura une fonction non débile pou choisir
		Card.puitsAuxSouhaits(this);
		break;
	case ESPION:
		Card [] revealed = partie.allRevealTopCard();
		boolean [] discard  = new boolean[Partie.NJOUEURS];
		for(int i=0;i<revealed.length;i++) {
			if(revealed[i].isA(Card.Type.VICTORY) || (revealed[i].isA(Card.Type.TREASURE)&&(revealed[i].plusGold<=partie.joueurs[i].decklist.goldDensity()))) {
				discard[i]=false;
			}
			else {discard[i]=true;}
		}
		Card.espion(this);	
		break;
	default:
		break;
	}
}

Card [] playables() {
	int n = 0;
	int p = 0;
	if (leftActions == 0) {return new Card[0];}
	for (int i = 0; i<hand.size(); i++) {
		if (hand.get(i).isA(Card.Type.ACTION)) {n++;}
	};
	Card [] reponse = new Card[n];
	for (int i = 0; i<hand.size(); i++) {
		if (hand.get(i).isA(Card.Type.ACTION)) {
			reponse[p] = hand.get(i);
			p++;}
	};
	return reponse;	
}

Vector<Card> buyables() {
	Vector<Card> result = new Vector<Card>(10);
	if (leftBuys == 0) {return result;}
	for (int i = 0; i< partie.theShop.avalaible.size(); i++) {
		if (partie.theShop.avalaible.get(i).peek().cost <= leftGold && partie.theShop.avalaible.get(i).size()>1) {
			result.add(partie.theShop.avalaible.get(i).peek());
		}
	}
	return result;	
}

void draw() {
	if (deck.isEmpty()) {
		defausseDansLaBibli();
		System.out.println(name+" melange son deck");
		deck.shuffle();
	}
	if (deck.size() != 0) {
	hand.add(deck.pop());
	}
}

void mill() {
	System.out.println(name+" mills "+deck.peek());
	defausse.add(deck.pop());
}

void discard(Card c) {
	System.out.println(name+" discards "+c);
	defausse.add(hand.retire(c));
}

void defausseDansLaBibli() {
	int imax = defausse.size();
	for (int i = 0; i<imax; i++) {
		deck.add(defausse.pop());
	}
}

void discardBoard() {
	int imax = board.size();
	for (int i = 0; i<imax; i++) {
		defausse.add(board.pop());
	}
}

void newHand() {
	for (int i =0; i<5; i++) {
		draw();
	}
}

//on défausse chaque carte de la main en comptant sa valeur si c'est un tresor, puis on ajoute le total
//RAPPEL: les bonus en or sont comptes lorsque la carte est jouee
void playTreasures() {
	int NCartes = hand.size();
	for(int i = 0; i< NCartes;i++) {
		if (hand.get(0).isA(Card.Type.TREASURE)){
			hand.get(0).playedBy(this);
		}
		else defausse.add(hand.retire(hand.get(0)));
	}
}

int updateVictoryPoints() {
	int total = 0;
	for (int i = 0; i<decklist.size(); i++) {
		total+=decklist.get(i).VP;
	}
	PointsDeVictoire = total;
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
		Card c = chooseAction();
		if (playSomething) {
			play(c);
		}
	}
	playTreasures();
	while(leftBuys>0 && buySomething) {
		Card c = chooseCard();
		if (c!=null){
			buy(c);
		}
		else buySomething=false;
	}
	if(printDetails) System.out.println("FIN ACHATS");
	discardBoard();
	newHand();
	if(printDetails) System.out.println(this);
	System.out.println("FIN DU TOUR DE "+name+"\n"); 
}

//BUYING/RECEIVING CARDS
private int valueCard(Card c, String stratName) {
	System.out.println("card "+c);
	return valueCard(c.name, stratName,this.partie.theShop);
}

private int valueCard(Card c, String stratName, Shop theShop) {
	return valueCard(c.name, stratName,theShop);
}

//STRATEGIES CODED HERE. default is BM
//Higher value means higher priority. Negative value means you prefer to buy nothing
private int valueCard(String cardName, String stratName, Shop theShop) {
	switch (stratName) {
	case "BM":
		switch (cardName) {
		case "Province":
			return 1000;
		case "Or":
			return 200;
		case "Argent":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		default:
			return -100;
		}
	case "OptimizedBM":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Or")==0 && this.decklist.cardCount("Argent")<5) return 150;
			else return 1600;
		case "Or":
			return 200;
		case "Duche":
			if(theShop.remainingProvinces()==5) return 150;
			if(theShop.remainingProvinces()<=4) return 800;
			else return 0;
		case "Argent":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		case "Domaine":
			if(theShop.remainingProvinces()<=2) return 400;
			if(theShop.remainingProvinces()==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "SmithyBM":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Or")==0 && this.decklist.cardCount("Argent")<5) return 150;
			else return 1600;
		case "Or":
			return 200;
		case "Forgeron":
			if(this.decklist.cardCount("Forgeron")==0 || (this.decklist.size()>=14 && this.decklist.cardCount("Forgeron")==1)) return 150;
			else return -50;
		case "Duche":
			if(theShop.remainingProvinces()==5) return 150;
			if(theShop.remainingProvinces()<=4) return 800;
			else return 0;
		case "Argent":
			if(this.decklist.goldDensity()<2) return 100;
			else return -50;
		case "Domaine":
			if(theShop.remainingProvinces()<=2) return 400;
			if(theShop.remainingProvinces()==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "BasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Or")==0 && this.decklist.cardCount("Argent")<5) return 150;
			else return 1600;
		case "Or":
			if(this.decklist.cardCount("Or")==0) return 400;
			else return -50;
		case "Forgeron":
			if(this.decklist.cardCount("Forgeron")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.cardCount("Forgeron")) return 250;
			else return -50;
		case "Marche":
			if(this.decklist.cardCount("Marche")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.cardCount("Forgeron")) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.cardCount("Forgeron"))<=1) return 200;
			else return -50;
		case "Duche":
			if(theShop.remainingProvinces()==4) return 150;
			if(theShop.remainingProvinces()<=3) return 600;
			else return 0;
		case "Argent":
			if(this.decklist.cardCount("Argent")==0) return 200;
			else return -50;
		case "Domaine":
			if(theShop.remainingProvinces()<=2) return 400;
			if(theShop.remainingProvinces()==3) return 50;
			else return -50;
		default:
			return -100;
		}
	case "MilitiaBasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Or")==0 && this.decklist.cardCount("Argent")<5) return 150;
			else return 1600;
		case "Or":
			if(this.decklist.cardCount("Or")==0) return 500;
			else return -50;
		case "Milice":
			if(this.decklist.cardCount("Milice")==0) return 400;
			else return -50;
		case "Forgeron":
			if(this.decklist.cardCount("Forgeron")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.typeCount(Card.Type.TERMINAL_ACTION)) return 250;
			else return -50;
		case "Marche":
			if(this.decklist.cardCount("Marche")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.typeCount(Card.Type.TERMINAL_ACTION)) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.typeCount(Card.Type.TERMINAL_ACTION))<=1) return 200;
			else return -50;
		case "Duche":
			if(theShop.remainingProvinces()==4) return 150;
			if(theShop.remainingProvinces()<=3) return 600;
			else return 0;
		case "Argent":
			if(this.decklist.cardCount("Argent")==0) return 200;
			else return -50;
		case "Domaine":
			if(theShop.remainingProvinces()<=1) return 400;
			if(theShop.remainingProvinces()==2) return 50;
			else return -50;
		default:
			return -100;
		}
	case "WitchBasicEngine":
		switch (cardName) {
		case "Province":
			if(this.decklist.cardCount("Or")==0 && this.decklist.cardCount("Argent")<5) return 150;
			else return 1600;
		case "Or":
			if(this.decklist.cardCount("Or")==0) return 500;
			else return -50;
		case "Milice":
			if(this.decklist.cardCount("Milice")==0) return 400;
			else return -50;
		case "Sorciere":
			if(this.decklist.cardCount("Sorciere")==0) return 450;
			else return -50;
		case "Forgeron":
			if(this.decklist.cardCount("Forgeron")==0) return 300;
			if(this.decklist.cardCount("Village")>=this.decklist.typeCount(Card.Type.TERMINAL_ACTION)) return 250;
			else return -50;
		case "Marche":
			if(this.decklist.cardCount("Marche")<=4) return 350;
			else return -50;
		case "Village":
			if(this.decklist.cardCount("Village")<this.decklist.typeCount(Card.Type.TERMINAL_ACTION)) return 250;
			if((this.decklist.cardCount("Village")-this.decklist.typeCount(Card.Type.TERMINAL_ACTION))<=1) return 200;
			else return -50;
		case "Duche":
			if(theShop.remainingProvinces()==4) return 150;
			if(theShop.remainingProvinces()<=3) return 600;
			else return 0;
		case "Argent":
			if(this.decklist.cardCount("Argent")==0) return 200;
			else return -50;
		case "Domaine":
			if(theShop.remainingProvinces()<=1) return 400;
			if(theShop.remainingProvinces()==2) return 50;
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

public Card chooseCard() {
	return chooseCard(this.stratName);
}

public Card chooseCard(String stratName) {
	return chooseCard(stratName, partie.theShop);
}

public Card chooseCard(String stratName, Shop theShop) {
	Vector<Card> buyables = buyables();
	double noteMax = 0;
	Card res = null;
	for (int i = 0; i<buyables.size(); i++) {
		int note = valueCard(buyables.get(i), stratName, theShop);
		//System.out.println("Considering "+buyables.get(i)+" note: "+note);
		if (note > noteMax) {
			res = buyables.get(i);
			noteMax = note;
		}
	}
	return res;
}

//DISCARDING

//STRATEGIES MATTERS HERE
//Higher value means higher priority. Negative value means you prefer to discard nothing vs drawing a card
public int valueToDiscard(Card c, String stratName) {
	if(c.isA(Card.Type.VICTORY) && !c.isA(Card.Type.TREASURE) && !c.isA(Card.Type.ACTION)) return 1600;
	if( ((this.hand.typeCount(Card.Type.TERMINAL_ACTION) - this.hand.typeCount(Card.Type.VILLAGE))>1) && c.isA(Card.Type.TERMINAL_ACTION)) return 1000-c.cost*100;
	if(c.isA(Card.Type.TREASURE)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) ((this.decklist.goldDensity()+0.8-c.plusGold)*100);
		else return (int) ((this.decklist.goldDensity()-c.plusGold)*100);
	}
	if(c.isA(Card.Type.ACTION)) {
		//Engine decks prefer to have actions in their hands
		if(stratName.contains("Engine")) return (int) (-200*(1-this.decklist.typeDensity(Card.Type.ACTION)));
		else return -80;
	}
	return -50;
}

//return the preferred card to discard
public Card chooseToDiscard() {
	return chooseToDiscard(this.stratName);
}

public Card chooseToDiscard(String stratName) {
	double noteMax = -1000000;
	Card res = null;
	for (int i = 0; i<this.hand.size(); i++) {
		int note = valueToDiscard(this.hand.get(i), stratName);
		if (note > noteMax) {
			res = this.hand.get(i);
			noteMax = note;
		}
	}
	return res;
}

//OTHERS
public boolean[] decideEspion() {
	Card [] revealed = partie.allRevealTopCard();
	boolean [] discard  = new boolean[Partie.NJOUEURS];
	for(int i=0;i<revealed.length;i++) {
		if(revealed[i].isA(Card.Type.VICTORY) || (revealed[i].isA(Card.Type.TREASURE)&&(revealed[i].plusGold<=partie.joueurs[i].decklist.goldDensity()))) discard[i]=false;
		else discard[i]=true;
		if(partie.joueurs[i]==this) discard[i]= !discard[i];
	}
	return discard;
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
	int NProvinces = fakeShop.remainingProvinces();
	for(int i=NProvinces;i>0;i--) {
		res+=pad(fakeShop.remainingProvinces()+" provinces left | ",20);
		for(int j=8;j>=0;j--) {
			leftGold=j;
			leftBuys=1;
			Card c = chooseCard(stratName,fakeShop);
			if(c==null) res+= pad("None",12) +" | ";
			else res+= pad(c.name,12) +" | ";
		}
		res+="\n";
		fakeShop.getCard("Province");
	}
	return res;
}

//DEPRECATED

Card laPlusChere() {
	//un debut de fonction pour decider quoi faire, c'est debile, mais c'est juste pour tester
	Vector<Card> buyables = buyables();
	int maxCost = 0;
	buySomething = false;
	Card reponse = Card.getCardByName("Cuivre");
	for (int i = 0; i<buyables.size(); i++) {
		if (buyables.get(i).cost > maxCost) {
			buySomething = true;
			reponse = buyables.get(i);
			maxCost = reponse.cost;
		}
	}
	return reponse;
}

Card laMeilleureNote(boolean printDetails) {
	Vector<Card> buyables = buyables();
	double noteMax = 0;
	buySomething = false;
	Card reponse = Card.getCardByName("Cuivre");
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

Card chooseAction() {
	//fonction toute conne, si il a une carte qui donne des actions il la joue,
	//s'il a pas de carte donnant des actions, il joue son action la plus chere
	//si il n'a pas d'action il renvoie null et change le boolean playSomething to false
	Card [] playables = playables();
	for (int i = 0; i<playables.length; i++) {
		if (playables[i].plusActions>0) {
			playSomething = true;
			return playables[i];
		}
	}

	if (playables.length > 0){
		playSomething = true;
		int maxCost=0;
		int maxIndex=-1;
		for (int i = 0; i<playables.length; i++) {
			if(playables[i].cost>maxCost) {
				maxCost=playables[i].cost;
				maxIndex=i;
			}
		}
		return playables[maxIndex];
	}
	playSomething = false;
	return null;
}

private double incrementGoldDensity(Decklist nouv) {
	 return nouv.goldDensity()- decklist.goldDensity();
}

private double incrementCardValue(Decklist nouv) {
	return nouv.givenCardsDensity() - decklist.givenCardsDensity();
}

private double incrementEnAction(Decklist nouv) {
	return (nouv.givenActionDensity() - decklist.givenActionDensity())*(1 + decklist.typeDensity(Card.Type.ACTION));
}

private double incrementEnAchat(Decklist nouv) {
	return nouv.givenAchatDensity() - decklist.givenAchatDensity();
}

private double PdV(Card c) {
	return c.VP;
}

public double note(Card c, boolean printDetails) {
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

public double noteCardStratGold(Card c, boolean printDetails){ 

if(c.name =="Province") {return 5;} 

if(c.name == "Or"){return 4;} 

if(c.name == "Forgeron" && decklist.proportion(Card.getCardByName( c.name))< C.Seuil1){return 3;} 

if (c.name == "Argent"){return 2;} 

return 0; 

} 

public double noteCardStratCycling(Card c, boolean PrintDetails){ 

if(c.name =="Marche"|| c.name == "Laboratoire") {return 5+Math.random();} 

if(c.name == "Or" && decklist.proportion(Card.getCardByName(c.name))<C.Seuil2){return 6;} 

if(c.name == "Province"){return 7;} 

if(c.name == "Forgeron"){return 5;} 

if (c.name == "Village"){return 2;} 

return 0; 

} 

public String toString() {
	String s = name;
	s+= " | actions left: "+leftActions+" | buys left: "+leftBuys+" | gold left: "+leftGold + "\n";
	s+=deck.toString()+"\n";
	s+=hand.toString()+"\n";
	s+=defausse.toString();
	return s;
}

boolean nearEnd() {return partie.theShop.nombrePilesVides()>=C.N2 | partie.theShop.remainingProvinces()<= C.N1;}

public static void main(String [] args) {
	Card.initialise();
	Partie p = new Partie(1);
	p.joueurs[0].buy(Card.getCardByName("Or"));
	p.joueurs[0].buy(Card.getCardByName("Forgeron"));
	System.out.println(p.joueurs[0].decklist);
	System.out.println("No terminal prob: "+p.joueurs[0].decklist.noTerminalProb());
	System.out.println("One terminal prob: "+p.joueurs[0].decklist.oneTerminalProb());
	System.out.println("Terminal coll prob: "+p.joueurs[0].decklist.terminalCollisionProb());
	System.out.println(p.joueurs[0].getStrategyTab("SmithyBM"));
}

	
}
