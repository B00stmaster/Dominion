import java.util.Vector;

public class Player {
	static int idGenerator = 0;
final public int id;
public 	int number;
public Partie partie;
public Shop theShop;
public Deck deck;
public Decklist decklist;
public Hand hand;
public Stack defausse;
public Stack board;
public String name;
public int actionsRestantes = 1;
public int achatsRestants = 1;
public int remainingMoney;
public boolean playSomething;
public boolean buySomething;
public Constantes C;
int PointsDeVictoire = 0;


Player(Shop s){
	id = idGenerator++;
	theShop = s;
	deck = new Deck(this);
	deck.shuffle();
	decklist = new Decklist(this);
	decklist.setupOnDeck(deck);
	name = "Player " + Integer.toString(id);
	hand = new Hand(this);
	defausse = new Stack();
	board = new Stack();
	C = new Constantes();
}

Player(Shop s, Partie p){
	this(s);
	partie = p;
}

Player (Shop s, Constantes CS, boolean alter){
	this(s);
	if (alter) {C = CS.alter();}
	else C = CS;
}

void play(Card c) {//on suppose que le joueur a deja l'action dispo pour le faire
	actionsRestantes += c.plusActions - 1;
	achatsRestants += c.plusBuys;
	for (int i = 0; i<c.plusCards; i++) {
		draw();
	}
	applyEffect(c);
	board.add(hand.retire(c));
}

void buy(Card c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
	defausse.add(theShop.getCard(c.name));
	decklist.add(c);
	remainingMoney -= c.cost;
	achatsRestants -= 1;
}

void mill() {
	defausse.add(deck.pop());
}

void applyEffect(Card c) {
	//on liste tous les effets pour appliquer la methode correspondante
	if(c.effet==null) return;
	switch (c.effet) {
	case SORCIERE:
		Card.sorciere(partie, this);
		break;
	case CHAMBRE_DU_CONSEIL:
		Card.chambreDuConseil(partie, this);
		break;
	case PUITS_AUX_SOUHAITS:
		Card [] choosables = Card.getCardByName("Puits aux Souhaits").choosables(partie, this); //fonction unique qui marche pour toutes les cartes qui font choisir parmi des cartes
		Card C = choosables[0]; //CHOIX DU JOUEUR !!! après on aura une fonction non débile pou choisir
		Card.puitsAuxSouhaits(C, this);
		break;
	case ESPION:
		Card [] revealed = partie.allRevealTopCard();
		boolean [] discard  = new boolean[Partie.NJOUEURS];
		for(int i=0;i<revealed.length;i++) {
			if(revealed[i].isA(Card.Type.VICTOIRE) || (revealed[i].isA(Card.Type.TRESOR)&&(revealed[i].plusGold<=partie.joueurs[i].decklist.goldDensity()))) {
				discard[i]=false;
			}
			else {discard[i]=true;}
		}
		Card.espion(partie, discard);	
		break;
	default:
		break;
	}
}

Card [] playables() {
	int n = 0;
	int p = 0;
	if (actionsRestantes == 0) {return new Card[0];}
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
	if (achatsRestants == 0) {return result;}
	for (int i = 0; i< Shop.nItems; i++) {
		if (Shop.avalaible[i].peek().cost <= remainingMoney && Shop.avalaible[i].size()>1) {
			result.add(Shop.avalaible[i].peek());
		}
	}
	return result;	
}

void draw() {
	if (deck.isEmpty()) {
		defausseDansLaBibli();
		deck.shuffle();
	}
	if (deck.size() != 0) {
	hand.add(deck.pop());
	}
}



void defausseDansLaBibli() {
	int imax = defausse.size();
	for (int i = 0; i<imax; i++) {
		Card c0 = defausse.pop();
		deck.add(c0);
	}
}

void newHand() {
	for (int i =0; i<5; i++) {
		draw();
	}
}


int countGoldValue() {//on défausse chaque carte de la main en comptant sa valeur, puis on compte sur les cartes jouées ! 
	int total = 0;
	int NCartes = hand.size();
	for(int i = 0; i< NCartes;i++) {
		if (hand.get(0).isA(Card.Type.TRESOR)){
			total += hand.get(0).plusGold;
			}
		//System.out.println("carte : " + hand.get(i));
		defausse.add(hand.retire(hand.get(0)));
	}
	for (int j = 0; j< board.size(); j++) {
		Card c0 = board.pop();
		total += c0.plusGold;
		defausse.add(c0);	
	}
	return total;
}

int countVictoryPoints() {
	//je compte dans la main, je melange la defausse dans le deck, puis je compte dans le deck
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
	achatsRestants = 1;
	actionsRestantes = 1;
	remainingMoney = 0;
}

public void tourDeJeu(boolean printDetails) {
	reset();
	if(printDetails) {
		System.out.println("TOUR DU JOUEUR " + name);
	System.out.println(this);}
	while(playSomething) {
	Card c = choisitUneAction();
	if (playSomething) {
		play(c);
		if(printDetails) {System.out.println("joue " + c);} }
	}
	remainingMoney = countGoldValue();
	System.out.println("je suis riche de : " + remainingMoney);
	while(buySomething) {
		Card c = laMeilleureNote(printDetails);
		if (buySomething) {
			buy(c);
			}
	}	
	newHand();
}


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
	if (reponse.name != "Cuivre" && printDetails) {
	System.out.println("achete : " + reponse);}
	return reponse;
}


Card choisitUneAction() {
	//fonction toute conne, si il a une carte qui donne des actions il la joue,
	//s'il a pas de carte donnant des actions, il joue la premiere action q'uil voit
	//si il n'a pas d'action il renvoie null et change le boolean playSomething to false
	Card [] playables = playables();
	for (int i = 0; i<playables.length; i++) {
		if (playables[i].plusActions>0) {
			playSomething = true;
			return playables[i];
		}
	}
	if (playables.length !=0){
		playSomething = true;
		return playables[0];
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
	System.out.println("decklist: "+decklist);
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
	String s = "Joueur ";
	s+= "Actions Restantes : " + actionsRestantes + "  |   Achats : " + achatsRestants + "\n";
	s+=deck.toString();
	s+=hand.toString();
	s+="\n" +  "Contenu de la defausse :" + "\n";
	for (int i =0; i<defausse.size();i++) {
		s += defausse.data.get(i).name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	
	return s + "\n";
}

boolean nearEnd() {return theShop.nombrePilesVides()>=C.N2 | theShop.remainingProvinces()<= C.N1;}

public static void main(String [] args) {
	Card.initialise();
	Shop s = new Shop();
	Player p = new Player(s);
	p.newHand();
	for (int i = 0; i<20; i++) {
	p.tourDeJeu(true);}
	System.out.println(p.decklist);
	System.out.println(p.C);
	}

	
}
