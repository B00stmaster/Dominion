package base;
public class Partie {
public Player [] joueurs;
public Shop theShop;
public int iCurrentPlayer;
public boolean hasEnded = false;
public static final int NJOUEURS = 4;
public static final int NOMBRE_DE_PARTIES = 1000;
public static final int NOMBRE_DE_TOURS = 17;


Partie(){	
	Shop s = new Shop(NJOUEURS);
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(this);
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(this,joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}

Partie(int players){	
	Shop s = new Shop(players);
	theShop = s;
	joueurs = new Player [players];
	for (int i = 0; i<players; i++) joueurs[i] = new Player(this);
	for (int i = 0; i<players; i++) joueurs[i].newHand();
}

Partie(String[] strats){	
	Shop s = new Shop(strats.length);
	theShop = s;
	joueurs = new Player [strats.length];
	for (int i = 0; i<joueurs.length; i++) joueurs[i] = new Player(this,strats[i]);
	for (int i = 0; i<joueurs.length; i++) joueurs[i].newHand();
}

Partie(Constantes Co){
	Shop s = new Shop(NJOUEURS);
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(this,Co, false);
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(this,joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}




void joueUnTourComplet(boolean printDetails, int first) {
	for (int i = first; i<4+first; i++) {
		joueurs[i%4].tourDeJeu(printDetails);
	}
}

//DEPRECATED
Player partie(int NTours, boolean printDetails) {
	int first = (int) (Math.random()*4);
	for (int i = 0; i<NTours; i++) {
		joueUnTourComplet(printDetails, first);
	}
	for (int i = 0; i<NJOUEURS; i++) {
		//int points = joueurs[i].countVictoryPoints();
		//System.out.println("joueur " + i + " : " + points);
	}
	Player gagnant = gagnant();
	//System.out.println(gagnant.id);
	return gagnant;
}

Player partie(boolean printDetails) {
	int first = (int) (Math.random()*this.joueurs.length);
	int turn=0;
	while(!hasEnded()) {
		joueurs[(turn+first)%this.joueurs.length].tourDeJeu(printDetails);
		turn++;
	}
	System.out.println("===================== PARTIE TERMINEE ===============================");
	for (int i = 0; i<this.joueurs.length; i++) {
		System.out.println("decklist de "+joueurs[i].name+":\n"+joueurs[i].decklist);
		int points = joueurs[i].updateVictoryPoints();
		System.out.println(joueurs[i].name+" a " + points + " PV");
	}
	Player gagnant = gagnant();
	return gagnant;
}

boolean hasEnded() {
	return theShop.nombrePilesVides()>=3 || (theShop.remainingCards("Province")==0);
}

Player gagnant() {
	int max = 0;
	for (int i = 0; i<this.joueurs.length; i++) {
		if (joueurs[i].PointsDeVictoire>max) {max = joueurs[i].PointsDeVictoire;}
	}
	int p = 0; 
	for (int i = 0; i<this.joueurs.length; i++) {
		if (joueurs[i].PointsDeVictoire == max) {p++;}
	}
	Player [] gagnants = new Player[p];
	p=0;
	for (int i=0; i<this.joueurs.length; i++) {
		if (joueurs[i].PointsDeVictoire == max) {gagnants[p] = joueurs[i];p++; }
	}
	int g = (int)(Math.random()*p);
	return gagnants[g];
}

Partie reinitialise() {
	Partie nouv = new Partie();
	for (int i = 0; i<this.joueurs.length; i++) {
		nouv.joueurs[i].C = joueurs[i].C;
	}
	return nouv;
}

//utile pour l'espion
public Card [] allRevealTopCard() {
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
	Partie p = new Partie(new String[] {"OptimizedBM","WitchBasicEngine"});
	p.partie(false);
}

}
