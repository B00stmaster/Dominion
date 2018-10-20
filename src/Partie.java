
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
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(s, joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}

Partie(Constantes Co){
	Shop s = new Shop();
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(s, Co, false);
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(s, joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}




void joueUnTourComplet(boolean printDetails, int first) {
	
	for (int i = first; i<4+first; i++) {
		if (printDetails) {
		System.out.println("=======================================================================");
		}
		joueurs[i%4].tourDeJeu(printDetails);
		if (printDetails) {
			System.out.println("fin de tour : "); System.out.println("");
		System.out.println(joueurs[i%4]);}
	}
}

Player partie(int NTours, boolean printDetails) {
	int first = (int) (Math.random()*4);
	for (int i = 0; i<NTours; i++) {
		joueUnTourComplet(printDetails, first);
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

Player partie(boolean printDetails) {
	int first = (int) (Math.random()*4);
	while(!hasEnded()) {		
		joueUnTourComplet(printDetails, first);
		
	}
	for (int i = 0; i<NJOUEURS; i++) {
		int points = joueurs[i].updateVictoryPoints();
		if (Apprentissage.wannaPrint) {
		//System.out.println("joueur " + i + " : " + points);
		if (i == 3) {System.out.println("");}}
	}
	Player gagnant = gagnant();
	//System.out.println(gagnant.id);
	
	return gagnant;
}

boolean hasEnded() {
	return theShop.nombrePilesVides()>=3 | !theShop.provincesRemain();
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

public Card [] allRevealTopCard() {
	//utile pour l'espion
	Card [] liste = new Card[Partie.NJOUEURS];
	for (int i = 0; i<NJOUEURS; i++) {
		liste[i] = joueurs[i].deck.peek();
	}
	return liste;
}


public String toString() {
	String s = "appercu de la partie : "+ "\n";
	s+= "j0 : "+ "\n" +  joueurs[0].decklist.toString();
	s+= "j1 : "+ "\n" +  joueurs[1].decklist.toString();
	s+= "j2 : "+ "\n" +  joueurs[2].decklist.toString();
	s+= "j3 : "+ "\n" +  joueurs[3].decklist.toString();
	return s;
}



public static void main(String[] args) {
	Card.initialise();
	Partie p = new Partie();
	p.partie(false);
}

}
