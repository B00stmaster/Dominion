import java.util.Arrays;

public class Player {
	int id;
public static final boolean urMomGay = true;
public Partie partie;
public Shop theShop;
public Deck deck;
public Hand hand;
public Stack defausse;
public Stack board;
public int actionsRestant
es = 1;
public int achatsRestants = 1;
public int remainingMoney;
public boolean playSomething;
public boolean buySomething;
public Constantes C;
int PointsDeVictoire = 0;




Player(Shop s){
	theShop = s;
	deck = new Deck(this);
	deck.shuffle();
	hand = new Hand(this);
	defausse = new Stack();
	board = new Stack();
	C = new Constantes();
}

Player(Shop s, Partie p){
	partie = p;
	theShop = s;
	deck = new Deck(this);
	deck.shuffle();
	hand = new Hand(this);
	defausse = new Stack();
	board = new Stack();
	C = new Constantes();
}

Player (Shop s, Constantes CS, boolean alter){
	theShop = s;
	deck = new Deck(this);
	deck.shuffle();
	hand = new Hand(this);
	defausse = new Stack();
	board = new Stack();
	if (alter) {C = CS.alter();}
	else C = CS;
}

void play(Card c) {//on suppose que le joueur a deja l'action dispo pour le faire
	actionsRestantes += c.actions - 1;
	achatsRestants += c.achats;
	for (int i = 0; i<c.cartes; i++) {
		draw();
	}
	applyEffect(c);
	board.add(hand.retire(c));
}

void buy(Card c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
	defausse.add(theShop.getCard(c.name));
	deck.decklist.add(c);
	remainingMoney -= c.cost;
	achatsRestants -= 1;
}

void mill() {
	defausse.add(deck.pop());
}

void applyEffect(Card c) {
	//on liste tous les effets pour appliquer la methode correspondante
	Card.Effet e = c.effet;
	if (e == Card.Effet.SORCIERE) {
		Card.sorciere(partie, this);
	}
	else if (e == Card.Effet.CHAMBRE_DU_CONSEIL) {
		Card.chambreDuConseil(partie, this);
	}
	else if (e == Card.Effet.PUITS_AUX_SOUHAITS) {
		Card [] choosables = Card.getCardByName("Puits aux Souhaits").choosables(partie, this); //fonction unique qui marche pour toutes les cartes qui font choisir parmis des cartes
		Card C = choosables[0]; //CHOIX DU JOUEUR !!! apr�s on aura une fonction non d�bile pou choisir
		Card.puitsAuxSouhaits(C, this);
	}
	else if (e == Card.Effet.ESPION) {
		Card [] scried = partie.scryAll();
		boolean [] b  = new boolean[Partie.NJOUEURS];
		//faire la fonction pour les choix qui change b en fonction du tablea scried
		Card.espion(partie, b);		
	}
}

Card [] playables() {
	int n = 0;
	int p = 0;
	if (actionsRestantes == 0) {return new Card[0];}
	for (int i = 0; i<hand.NCartes; i++) {
		if (hand.cartes[i].isAnAction()) {n++;}
	};
	Card [] reponse = new Card[n];
	for (int i = 0; i<hand.NCartes; i++) {
		if (hand.cartes[i].isAnAction()) {
			reponse[p] = hand.cartes[i];
			p++;}
	};
	return reponse;	
}

Card [] buyables() {
	int n = 0;
	int p = 0;
	if (achatsRestants == 0) {return new Card[0];}
	for (int i = 0; i< Shop.nItems; i++) {
		if (Shop.avalaible[i].peek().cost <= remainingMoney && Shop.avalaible[i].NCartes>1) {
			n++;
		}
	}
	Card [] reponse = new Card[n];
	for (int i = 0; i<Shop.nItems; i++) {
		if (Shop.avalaible[i].peek().cost <= remainingMoney && Shop.avalaible[i].NCartes>1) {
			reponse[p] = Shop.avalaible[i].peek();
			p++;
		}
	};
	

	return reponse;	
}

void draw() {
	if (deck.isEmpty()) {
		defausseDansLaBibli();
		deck.shuffle();
	}
	if (deck.NCartes != 0) {
	hand.add(deck.pop());
	}
}



void defausseDansLaBibli() {
	int imax = defausse.NCartes;
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


int countGoldValue() {//on d�fausse chaque carte de la main en comptant sa valeur, puis on compte sur les cartes jou�es ! 
	int TOTAL = 0;
	int imax = hand.NCartes; 
	for(int i = 0; i< imax;i++) {
		Card c0 = hand.pop();
		if (c0.isATreasure()){
			TOTAL += c0.value;
			}
		defausse.add(c0);
	}
	int jmax = board.NCartes;
	for (int j = 0; j< jmax; j++) {
		Card c0 = board.pop();
		TOTAL += c0.value;
		defausse.add(c0);	
	}
	remainingMoney = TOTAL;
	return TOTAL;
}

int countVictoryPoints() {
	//je compte dans la main, je melange la defausse dans le deck, puis je compte dans le deck
	int TOTAL = 0;
	for (int i = 0; i<hand.NCartes; i++) {
		//System.out.println(hand.cartes[i].name + " : " + hand.cartes[i].VP);
		TOTAL += hand.cartes[i].VP;
	}
	defausseDansLaBibli();
	for (int i = 0; i<deck.NCartes; i++) {
		//System.out.println(deck.cartes[i].name + " : " + deck.cartes[i].VP);
		TOTAL += deck.cartes[i].VP;
	}
	PointsDeVictoire = TOTAL;
	return TOTAL;
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
		System.out.println("tour du joueur " + id);
	System.out.println(this);}
	while(playSomething) {
	Card c = choisitUneAction();
	if (playSomething) {
		play(c);
		if(printDetails) {System.out.println("joue " + c);} }
	}
	countGoldValue();	
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
	Card [] buyables = buyables();
	int maxCost = 0;
	buySomething = false;
	Card reponse = Card.getCardByName("Cuivre");
	for (int i = 0; i<buyables.length; i++) {
		if (buyables[i].cost > maxCost) {
			buySomething = true;
			reponse = buyables[i];
			maxCost = reponse.cost;
		}
	}
	return reponse;
}

Card laMeilleureNote(boolean printDetails) {
	Card [] buyables = buyables();
	double noteMax = 0;
	buySomething = false;
	Card reponse = Card.getCardByName("Cuivre");
	for (int i = 0; i<buyables.length; i++) {
		double note = note(buyables[i], printDetails);
		//System.out.println(buyables[i] +  " : " + note);
		if (note > noteMax) {
			buySomething = true;
			reponse = buyables[i];
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
		if (playables[i].actions>0) {
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
	 return nouv.goldDensity()- deck.goldDensity();
}

private double incrementCardValue(Decklist nouv) {
	return nouv.averageDrawnCards() - deck.decklist.averageDrawnCards();
}

private double incrementEnAction(Decklist nouv) {
	return (nouv.givenActionDensity() - deck.decklist.givenActionDensity())*(1 + deck.decklist.actionDensity());
}

//private double noteCartes(Decklist nouv) {
//	return nouv.averageDrawnCards() - deck.decklist.averageDrawnCards();
//}

private double incrementEnAchat(Decklist nouv) {
	return nouv.givenAchatDensity() - deck.decklist.givenAchatDensity();
}

private double PdV(Card c) {
	return c.VP;
}

public double note(Card c, boolean printDetails) {
	Decklist nouv = new Decklist(deck.decklist, c, this);
	double noteGold  = incrementGoldDensity(nouv);
	double noteCard = incrementCardValue(nouv);
	double noteAction = incrementEnAction(nouv);
	double noteAchat = incrementEnAchat(nouv);
	double notePdV = PdV(c);
	double note = C.k2*noteGold+ C.k3*noteCard+ C.k4*noteAction + C.k5*noteAchat+ C.k1*notePdV;
	if (printDetails & false) {
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



public String toString() {

	
	String s = "Joueur ";
	s+= "Actions Restantes : " + actionsRestantes + "  |   Achats : " + achatsRestants + "\n";
	s += "Contenu du deck :" + "\n";
	for (int i =0; i<deck.NCartes;i++) {
		s += deck.cartes[i].name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	s +="\n" +  "Contenu de la main :" + "\n";
	for (int i =0; i<hand.NCartes;i++) {
		s += hand.cartes[i].name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	s +="\n" +  "Contenu de la defausse :" + "\n";
	for (int i =0; i<defausse.NCartes;i++) {
		s += defausse.cartes[i].name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	
	return s + "\n";
}

boolean nearEnd() {
	return theShop.nombrePilesVides()>=C.N2 | theShop.provincesRestantes()<= C.N1;
}


public static void main(String [] args) {
	Card.initialise();
	Shop s = new Shop();
	Player p = new Player(s);
	p.newHand();
	for (int i = 0; i<20; i++) {
	p.tourDeJeu(true);}
	System.out.println(p.deck.decklist);
	System.out.println(p.C);
	}

	
}
