
public class Partie {
Player [] joueurs;
Shop theShop;
int iCurrentPlayer;
boolean hasEnded = false;
static final int NJOUEURS = 4;
static final int NOMBRE_DE_PARTIES = 1000;
static final int NOMBRE_DE_TOURS = 17;


Partie(){	
	Shop s = new Shop();
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(s, this);
	joueurs[0].id = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(s, joueurs[0].C, true);
	joueurs[i].id = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}

Partie(Constantes Co){
	Shop s = new Shop();
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(s, Co, false);
	joueurs[0].id = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(s, joueurs[0].C, true);
	joueurs[i].id = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}




void joueUnTourComplet(int first) {
	
	for (int i = first; i<4+first; i++) {
		//System.out.println("tour du joueur " + i);
		//if (i%4 ==0) {
		//System.out.println(joueurs[i%4]);}
		joueurs[i%4].tourDeJeu();
		//if (i%4 ==0) {
		//System.out.println(joueurs[i%4]);}
		//System.out.println(theShop);
	}
}

Player partie(int NTours) {
	int first = (int) (Math.random()*4);
	for (int i = 0; i<NTours; i++) {
		joueUnTourComplet(first);
	}
	for (int i = 0; i<NJOUEURS; i++) {
		//int points = joueurs[i].countVictoryPoints();
		if (Apprentissage.wannaPrint) {
		//System.out.println("joueur " + i + " : " + points);
		if (i == 3) {System.out.println("");}}
	}
	Player gagnant = gagnant();
	//System.out.println(gagnant.id);
	
	return gagnant;
}

Player partie() {
	int first = (int) (Math.random()*4);
	while(!hasEnded()) {		
		joueUnTourComplet(first);
		
	}
	for (int i = 0; i<NJOUEURS; i++) {
		int points = joueurs[i].countVictoryPoints();
		if (Apprentissage.wannaPrint) {
		//System.out.println("joueur " + i + " : " + points);
		if (i == 3) {System.out.println("");}}
	}
	Player gagnant = gagnant();
	//System.out.println(gagnant.id);
	
	return gagnant;
}

boolean hasEnded() {
	return theShop.nombrePilesVides()>3 | !theShop.ilResteDesProvinces();
}



Player gagnant() {
	int max = 0;
	for (int i = 0; i<NJOUEURS; i++) {
		if (joueurs[i].PointsDeVictoire>max) {max = joueurs[i].PointsDeVictoire;}
	}
	int p = 0; 
	for (int i = 0; i<NJOUEURS; i++) {
		if (joueurs[i].PointsDeVictoire == max) {p++;}
	}
	Player [] gagnants = new Player[p];
	p=0;
	for (int i=0; i<NJOUEURS; i++) {
		if (joueurs[i].PointsDeVictoire == max) {gagnants[p] = joueurs[i];p++; }
	}
	int g = (int)(Math.random()*p);
	return gagnants[g];
}

Partie reinitialise() {
	Partie nouv = new Partie();
	for (int i = 0; i<NJOUEURS; i++) {
		nouv.joueurs[i].C = joueurs[i].C;
	}
	return nouv;
}

public Card [] scryAll() {
	//utile pour l'espion
	Card [] liste = new Card[Partie.NJOUEURS];
	for (int i = 0; i<NJOUEURS; i++) {
		liste[i] = joueurs[i].deck.peek();
	}
	return liste;
}


public String toString() {
	String s = "appercu de la partie : "+ "\n";
	s+= "j0 : "+ "\n" +  joueurs[0].deck.decklist.toString();
	s+= "j1 : "+ "\n" +  joueurs[1].deck.decklist.toString();
	s+= "j2 : "+ "\n" +  joueurs[2].deck.decklist.toString();
	s+= "j3 : "+ "\n" +  joueurs[3].deck.decklist.toString();
	return s;
}



public static void main(String[] args) {
	Card.initialise();
	Partie p = new Partie();
	p.joueurs[0].hand.add(Card.getCardByName("Puits aux Souhaits"));
	p.joueurs[0].play(Card.getCardByName("Puits aux Souhaits"));
	System.out.println(p.joueurs[0].deck);
	//p.joueurs[0].applyEffect(Card.getCardByName("Chambre du Conseil"));
	System.out.println(p.joueurs[0].hand);
	System.out.println(p.joueurs[1].hand);
	System.out.println(p.joueurs[2].hand);
	System.out.println(p.joueurs[3].hand);
	//p.partie();
}

}
