import java.util.Arrays;
//bite MDR
public class Player {
	
public static final boolean urMomGay = true;
public Shop theShop;
public Deck deck;
public Hand hand;
public Stack defausse;
public Stack board;
public int actionsRestantes = 1;
public int achatsRestants = 1;
public int remainingMoney;
public boolean playSomething;
public boolean buySomething;

Player(Shop s){
	theShop = s;
	deck = new Deck(this);
	deck.shuffle();
	hand = new Hand(this);
	defausse = new Stack();
	board = new Stack();
}

void play(Card c) {//on suppose que le joueur a deja l'action dispo pour le faire
	actionsRestantes += c.actions - 1;
	achatsRestants += c.achats;
	for (int i = 0; i<c.cartes; i++) {
		draw();
	}
	board.add(hand.retire(c));
}

void buy(Card c) {//on suppose que le joueur a deja les achats et l'argent dispo pour le faire
	defausse.add(theShop.getCard(c.name));
	remainingMoney -= c.cost;
	achatsRestants -= 1;
}

Card [] playables() {
	int n = 0;
	int p = 0;
	if (actionsRestantes == 0) {return new Card[0];}
	for (int i = 0; i<hand.NCartes; i++) {
		if (hand.cartes[i].type == Card.Type.ACTION) {n++;}
	};
	Card [] reponse = new Card[n];
	for (int i = 0; i<hand.NCartes; i++) {
		if (hand.cartes[i].type == Card.Type.ACTION) {
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
		if (c0.type == Card.Type.TRESOR) 
		{TOTAL += c0.value;}
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
		System.out.println(hand.cartes[i].name + " : " + hand.cartes[i].VP);
		TOTAL += hand.cartes[i].VP;
	}
	defausseDansLaBibli();
	for (int i = 0; i<deck.NCartes; i++) {
		System.out.println(deck.cartes[i].name + " : " + deck.cartes[i].VP);
		TOTAL += deck.cartes[i].VP;
	}
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
public void tourDeJeu() {
	reset();
	//playSomething = true; //on peut passer �a en fin de tour plutot en vrai
	//doit choisir quelle carte action jouer methode Carte choose()
	while(playSomething) {
	Card c = choisitUneAction();
	if (playSomething) {play(c);}
	}
	countGoldValue();
	//pareil que precedemmet avec l'achat
	//buySomething = true;
	while(buySomething) {
		Card c = laPlusChere();
		if (buySomething) {buy(c);}
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
	for (int i = 0; i<playables.length; i++) {
		playSomething = true;
		return playables[i];
	}
	playSomething = false;
	return null;
}



////// INDICATEURS //////



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


public static void main(String [] args) {
	Card.initialise();
	Shop s = new Shop();
	Player p = new Player(s);
	p.deck.add(Card.cards[6]); p.deck.add(Card.cards[7]); p.deck.add(Card.cards[8]);
	p.newHand();
	for (int i = 0; i<10; i++) {
	p.tourDeJeu();
	}
	}
}
