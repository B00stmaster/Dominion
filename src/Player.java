import java.util.Arrays;

public class Player {
public Shop theShop;
public Deck deck;
public Hand hand;
public Stack defausse;
public Stack board;
public int actionsRestantes = 1;
public int achatsRestants = 1;
public int remainingMoney;

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


int countGoldValue() {//on défausse chaque carte de la main en comptant sa valeur, puis on compte sur les cartes jouées ! 
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
	
	System.out.println(p);
	System.out.println(Arrays.deepToString(p.playables()));
	}

}
