
public class Partie {
Player [] joueurs;
Shop theShop;
int iCurrentPlayer;
static final int NJOUEURS = 4;
static final int NOMBRE_DE_PARTIES = 1000;
static final int NOMBRE_DE_TOURS = 17;


Partie(){	
	Shop s = new Shop();
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(s);
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
		joueurs[i%4].tourDeJeu();
		//System.out.println(joueurs[i]);
		//System.out.println(theShop);
	}
}

Player partie(int NTours) {
	int first = (int) (Math.random()*4);
	for (int i = 0; i<NTours; i++) {
		joueUnTourComplet(first);
	}
	for (int i = 0; i<NJOUEURS; i++) {
		int points = joueurs[i].countVictoryPoints();
		if (Apprentissage.wannaPrint) {
		System.out.println("joueur " + i + " : " + points);
		if (i == 3) {System.out.println("");}}
	}
	Player gagnant = gagnant();
	//System.out.println(gagnant.id);
	
	return gagnant;
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
	int j0 = 0;
	int j1 = 0;
	int j2 = 0; 
	int j3 = 0;
	Partie p = new Partie();
	Player gagnant = p.partie(NOMBRE_DE_TOURS);
	for (int k = 0; k<NOMBRE_DE_PARTIES;k++) {
			p = p.reinitialise();
			Player g = p.partie(NOMBRE_DE_TOURS);
			if (g.id == 0) {j0++;}
			if (g.id == 1) {j1++;}
			if (g.id == 2) {j2++;}
			if (g.id == 3) {j3++;}
			}
	System.out.println("j0 : " + j0);
	System.out.println("j1 : " + j1);
	System.out.println("j2 : " + j2);
	System.out.println("j3 : " + j3);
	int max= j0;
	Player reponse = p.joueurs[0];
	if (j1>max) {max = j1; reponse = p.joueurs[1];}
	if (j2>max) {max = j2; reponse = p.joueurs[2];}
	if (j3>max) {max = j3; reponse = p.joueurs[3];}
	System.out.println(reponse.id);

}

}
